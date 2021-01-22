import cassandra.DBConnector;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;




public class SparkNewsConsumer implements Runnable{

    private static final String targetTopic = "NewsTopic";
    private List<String> stopwords;
    private String processBody(String body){

        ArrayList<String> allWords =
                Stream.of(body.toLowerCase().split("[^a-zA-Z]"))
                        .collect(Collectors.toCollection(ArrayList<String>::new));

        allWords.removeAll(stopwords);

        Map<String, Integer> countByWords = new HashMap<String, Integer>();
        for(String word : allWords) {
            if(word.length()>2){
                Integer count = countByWords.get(word);
                if (count != null) {
                    countByWords.put(word, count + 1);
                } else {
                    countByWords.put(word, 1);
                }
            }
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(countByWords.entrySet());
        list.sort(Map.Entry.<String, Integer>comparingByValue().reversed());


        int numberWords = 10;

        String resultat = "";
        for(int i=0; i<numberWords; i++ ){
            Map.Entry<String, Integer> entry = list.get(i);
            if(i == numberWords-1)
                resultat += entry.getKey();
            else
                resultat += entry.getKey()+", ";
        }

        return resultat;

    }

    @Override
    public void run(){

        try {
            stopwords = Files.readAllLines(Paths.get("src/main/resources/stopwords.txt"), StandardCharsets.UTF_8);
        }catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Spark Streaming started now .....");
        SparkConf conf = new SparkConf().setAppName("kafka-sandbox").setMaster("local[*]");
        JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(20000));
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", "localhost:9092");
        Set<String> topics = Collections.singleton(targetTopic);
        DBConnector connector = new DBConnector();
        connector.connectdb("localhost", 9042);
        final String insertQuery = "INSERT INTO imdb_keyspace2.newsTable (id, body, head, link, id_actor, publishTime, common_words) "
                + "VALUES (?,?,?,?,?,?,?)";
        PreparedStatement psInsert = connector.getSession().prepare(insertQuery);
        JavaPairInputDStream<String, String> directKafkaStreamNews = KafkaUtils.createDirectStream(ssc, String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);
        directKafkaStreamNews.foreachRDD(rdd -> {
            if(rdd.count() > 0) {
                System.out.println("News new data arrived  " + rdd.partitions().size() +" Partitions and " + rdd.count() + " Records");
                rdd.collect().forEach(rawRecord -> {
                    try {
                        JSONObject actorJson = new JSONObject(rawRecord._2);
                        String id = actorJson.getString("id");
                        String body = actorJson.getString("body");
                        String head = actorJson.getString("head");
                        String link = actorJson.getString("link");
                        String id_actor = actorJson.getString("id_actor");
                        String publishTime = actorJson.getString("publishTime");
                        String common_words = processBody(body);

                        System.out.println(id);
                        System.out.println(body);
                        System.out.println(head);
                        System.out.println(link);
                        System.out.println(id_actor);
                        System.out.println(publishTime);
                        System.out.println(common_words);



                        BoundStatement bsInsert = psInsert.bind(id, body, head, link, id_actor, publishTime, common_words);
                        connector.getSession().execute(bsInsert);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                });
            }
            Thread.sleep(2000);
        });

        ssc.start();
        ssc.awaitTermination();
    }

    public static void main(String[] args) {
        SparkNewsConsumer sparkNewsConsumer = new SparkNewsConsumer();
        sparkNewsConsumer.run();
    }
}
