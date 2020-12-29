

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

import api.actor.AllFilmography;
import api.actor.MostPopularClebs;
import com.opencsv.CSVReader;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import util.Actor;
import util.Movie;


public class ActorProducerThread implements Runnable{

    private static String KafkaBrokerEndpoint = "localhost:9092";
    private static String targetTopic = "ActorTopic";

    private Producer<String, String> ProducerProperties(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaBrokerEndpoint);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaCsvProducer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<String, String>(properties);
    }



    public void run(){

        final Producer<String, String> ActorProducer = ProducerProperties();
        final String MOST_POPULAR_PATH = "src/main/resources/actor/mostPopularCelebs.csv";
        try {
            FileReader filereader = new FileReader(MOST_POPULAR_PATH);
            CSVReader csvReader = new CSVReader(filereader);
            List<String[]> allData = csvReader.readAll();
            for (int i= 1; i<allData.size(); i++) {
                final ProducerRecord<String, String> actorRecord = new ProducerRecord<String, String>(
                        targetTopic, allData.get(0)[0], allData.toString());

                ActorProducer.send(actorRecord, (metadata, exception) -> {
                    if(metadata != null){
                        System.out.println("Actor: -> "+ actorRecord.key()+" | "+ actorRecord.value());
                    }
                    else{
                        System.out.println("Error Sending Record -> "+ actorRecord.value());
                    }
                });

                Thread.sleep(50);
            }
        }
        catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


}
