

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Stream;

import api.actor.AllFilmography;
import api.actor.MostPopularClebs;
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

        try {
            MostPopularClebs mostPopularClebs = new MostPopularClebs();
            Actor actor;

            while ((actor = mostPopularClebs.getNextActor()) != null){
                final ProducerRecord<String, String> actorRecord = new ProducerRecord<String, String>(
                        targetTopic, actor.getId(), actor.toString());

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
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
