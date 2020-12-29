

import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import java.util.*;

public class SparkConsumer {

    private static final String targetTopic = "ActorTopic";


    public static void main(String[] args) {


        System.out.println("Spark Streaming started now .....");

        SparkConf conf = new SparkConf().setAppName("kafka-sandbox").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(20000));

        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", "localhost:9092");
        Set<String> topics = Collections.singleton(targetTopic);

        JavaPairInputDStream<String, String> directKafkaStream = KafkaUtils.createDirectStream(ssc, String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);

        directKafkaStream.foreachRDD(rdd -> {

            System.out.println("New data arrived  " + rdd.partitions().size() +" Partitions and " + rdd.count() + " Records");
            if(rdd.count() > 0) {
                rdd.collect().forEach(rawRecord -> {

                    System.out.println(rawRecord);
                    System.out.println("***************************************");
                    System.out.println(rawRecord._2);

                });
            }
        });

        ssc.start();
        ssc.awaitTermination();
    }


}



