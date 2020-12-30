import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import util.Actor;

import java.util.Properties;


public class ActorProducerThread implements Runnable{

    private static String KafkaBrokerEndpoint = "localhost:9092";
    private static String targetTopic = "ActorTopic";
    private final String MOST_POPULAR_PATH = "src/main/resources/mostPopularCelebs1.csv";
    Actor actor;

    public ActorProducerThread(){

    }

    public ActorProducerThread(Actor actor){
        this.actor = actor;
    }

    private Producer<String, String> ProducerProperties(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaBrokerEndpoint);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaCsvProducer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return new KafkaProducer<String, String>(properties);
    }



    public void run(){

        final Producer<String, String> actorProducer = ProducerProperties();


        final ProducerRecord<String, String> actorRecord = new ProducerRecord<String, String>(
                targetTopic, actor.getId(), actor.jsonAsString());

        actorProducer.send(actorRecord, (metadata, exception) -> {
            if(metadata != null){
                System.out.println("Actor: -> "+ actorRecord.key()+" | "+ actorRecord.value());
            }
            else{
                System.out.println("Error Sending Record -> "+ actorRecord.value());
            }
        });

    }


}
