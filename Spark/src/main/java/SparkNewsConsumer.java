import cassandra.DBConnector;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.serializer.StringDecoder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.json.JSONObject;

import java.util.*;


public class SparkNewsConsumer implements Runnable{

    private static final String targetTopic = "NewsTopic";

    @Override
    public void run(){

        System.out.println("Spark Streaming started now .....");
        SparkConf conf = new SparkConf().setAppName("kafka-sandbox").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaStreamingContext ssc = new JavaStreamingContext(sc, new Duration(20000));
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", "localhost:9092");
        Set<String> topics = Collections.singleton(targetTopic);
        DBConnector connector = new DBConnector();
        connector.connectdb("localhost", 9042);
        final String insertQuery = "INSERT INTO imdb_keyspace1.newsTable (id, body, head, link, id_actor, publishTime) "
                + "VALUES (?,?,?,?,?,?)";
        PreparedStatement psInsert = connector.getSession().prepare(insertQuery);
        JavaPairInputDStream<String, String> directKafkaStreamNews = KafkaUtils.createDirectStream(ssc, String.class, String.class, StringDecoder.class, StringDecoder.class, kafkaParams, topics);
        directKafkaStreamNews.foreachRDD(rdd -> {
            if(rdd.count() > 0) {
                System.out.println("News new data arrived  " + rdd.partitions().size() +" Partitions and " + rdd.count() + " Records");
                rdd.collect().forEach(rawRecord -> {
                    JSONObject actorJson = new JSONObject(rawRecord._2);
                    String id = actorJson.getString("id");
                    String body = actorJson.getString("body");
                    String head = actorJson.getString("head");
                    String link = actorJson.getString("link");
                    String id_actor = actorJson.getString("id_actor");
                    String publishTime = actorJson.getString("publishTime");
                    
                    System.out.println(id);
                    System.out.println(body);
                    System.out.println(head);
                    System.out.println(link);
                    System.out.println(id_actor);
                    System.out.println(publishTime);

                    BoundStatement bsInsert = psInsert.bind(id, body, head, link, id_actor, publishTime);
                    connector.getSession().execute(bsInsert);

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
