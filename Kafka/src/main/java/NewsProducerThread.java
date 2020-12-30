import api.actor.Biography;
import api.actor.GetBornToday;
import api.actor.GetRecentNews;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import util.Actor;
import util.News;
import util.SDate;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class NewsProducerThread implements Runnable{

    private static String KafkaBrokerEndpoint = "localhost:9092";
    private static String targetTopic = "NewsTopic";
    private final String MOST_POPULAR_PATH = "src/main/resources/actor/mostPopularCelebs.csv";

    private Producer<String, String> ProducerProperties(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaBrokerEndpoint);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaCsvProducer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<String, String>(properties);
    }

    public void addActortoFile(Actor actor){
        System.out.println("write : "+actor);
        FileWriter outputfile = null;
        File file = new File(MOST_POPULAR_PATH);
        try {
            outputfile = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSVWriter writer = new CSVWriter(outputfile);
        writer.writeNext(new String[] { actor.getId(), actor.getName(), actor.getBirthDate(), actor.getBirthPlace(), actor.getGender()});
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void run(){

        Producer<String, String> newsProducer = ProducerProperties();

        List<String> listActors = new ArrayList();
        Map<String, List<News>> newsMap = new HashMap<>();
        SDate dateCourante = new SDate();

        FileReader filereader;
        CSVReader csvReader;
        List<String[]> allData;
        int day, month;
        int nombre_actors = 6;
        Date date;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");;
        String hour_minute;

        while (true){
            try {
                SDate datetemp = new SDate();
                if(listActors.size() == 0 || !datetemp.equals(dateCourante)){
                    newsMap = new HashMap<>();
                    dateCourante = datetemp;
                    filereader = new FileReader(MOST_POPULAR_PATH);
                    csvReader = new CSVReader(filereader);
                    allData = csvReader.readAll();

                    for (int i= 1; i<allData.size(); i++) {
                        month = Integer.parseInt(allData.get(i)[2].split("-")[1]);
                        day = Integer.parseInt(allData.get(i)[2].split("-")[2]);
                        if(dateCourante.sameMonthDay(month, day))
                            listActors.add(allData.get(i)[0]);
                    }

                    if(listActors.size()<nombre_actors){
                        GetBornToday getBornToday = new GetBornToday(dateCourante.getMonth(),dateCourante.getDay());
                        List<String> listBornToday = getBornToday.getAllBornToday();
                        System.out.println("list size : " + listActors.size());
                        for(String id_act : listBornToday){
                            if(listActors.size()>=nombre_actors)
                                break;
                            if(!listActors.contains(id_act)){
                                Biography bioActor = new Biography(id_act);
                                Actor actor = bioActor.getActor();
                                if(actor != null){
                                    System.out.println(actor);
                                    ActorProducerThread actorProducerThread = new ActorProducerThread(actor);
                                    actorProducerThread.run();
                                    addActortoFile(actor);
                                    listActors.add(id_act);
                                }
                            }
                        }
                    }

                    for(int i = 0; i<listActors.size(); i++) {
                        GetRecentNews recentNews = new GetRecentNews(listActors.get(i));
                        News news;
                        while( (news = recentNews.getNextNews(dateCourante)) != null ) {
                            String key = news.getPublishTime().split("T")[1].substring(0,5);
                            if(!newsMap.containsKey(key)){
                                newsMap.put(key,new ArrayList<News>());
                            }
                            List<News> listNews = newsMap.get(key);
                            listNews.add(news);
                            newsMap.put(key, listNews);
                        }
                    }


                }

                date =new Date();
                hour_minute = formatter.format(date);


                if(newsMap.containsKey(hour_minute)) {
                    List<News> listNews = newsMap.get(hour_minute);
                    for(News news : listNews){
                        System.out.println(news);
                        final ProducerRecord<String, String> actorRecord = new ProducerRecord<String, String>(
                                targetTopic, news.getId(), news.jsonAsString());

                        newsProducer.send(actorRecord, (metadata, exception) -> {
                            if(metadata != null){
                                System.out.println("Actor: -> "+ actorRecord.key()+" | "+ actorRecord.value());
                            }
                            else{
                                System.out.println("Error Sending Record -> "+ actorRecord.value());
                            }
                        });
                    }
                    newsMap.remove(hour_minute);
                }else
                    Thread.sleep(1000);
            }
            catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }


    }


}