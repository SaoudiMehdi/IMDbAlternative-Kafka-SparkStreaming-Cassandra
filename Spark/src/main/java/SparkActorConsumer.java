

import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.json.JSONObject;

import java.util.*;

public class SparkActorConsumer implements Runnable{

    private static final String targetTopic = "ActorTopic";


    @Override
    public void run(){


        System.out.println("Spark Streaming started now .....");
        SparkConf conf = new SparkConf().setAppName("kafka-sandbox").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(20000));

        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", "localhost:9092");
        Set<String> topics = Collections.singleton(targetTopic);

        JavaPairInputDStream<String, String> directKafkaStream = KafkaUtils.createDirectStream(ssc, String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);

        directKafkaStream.foreachRDD(rdd -> {

            if(rdd.count() > 0) {
                System.out.println("Actor new data arrived  " + rdd.partitions().size() +" Partitions and " + rdd.count() + " Records");
                rdd.collect().forEach(rawRecord -> {
                    JSONObject actorJson = new JSONObject(rawRecord._2);
                    String id = actorJson.getString("id");
                    String name = actorJson.getString("name");
                    String gender = actorJson.getString("gender");
                    String birthDate = actorJson.getString("birthDate");
                    String birthPlace = actorJson.getString("birthPlace");

                    System.out.println(id);
                    System.out.println(name);
                    System.out.println(gender);
                    System.out.println(birthDate);
                    System.out.println(birthPlace);
                });
            }
            Thread.sleep(2000);
        });

        ssc.start();
        ssc.awaitTermination();
    }

    public static void main(String[] args) {
        SparkActorConsumer sparkActorConsumer = new SparkActorConsumer();
        sparkActorConsumer.run();
    }
}



