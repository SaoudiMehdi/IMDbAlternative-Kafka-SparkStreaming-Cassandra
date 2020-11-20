package runnable;

import api.MovieProvider;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.AppConfig;
import util.Movie;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class MovieProducerThread implements Runnable {
    private Logger log = LoggerFactory.getLogger(MovieProducerThread.class.getSimpleName());

    private final AppConfig appConfig;
    private final CountDownLatch latch;
    private final KafkaProducer<String, Movie> kafkaProducer;
    private final String targetTopic;

    public MovieProducerThread(AppConfig appConfig,
                               CountDownLatch latch) {
        this.appConfig = appConfig;
        this.latch = latch;
        this.kafkaProducer = createKafkaProducer(appConfig);
        this.targetTopic = appConfig.getTopicName();
    }

    public KafkaProducer<String, Movie> createKafkaProducer(AppConfig appConfig) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        //properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        //properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        //properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        //properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());


        //properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //properties.setProperty("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, appConfig.getSchemaRegistryUrl());

        return new KafkaProducer<String, Movie>(properties);
    }

    @Override
    public void run() {
        try {
            Movie movie;
            while ((movie = (MovieProvider.getMovieProvider()).getNext()) != null){

                    log.info("Sending movie  : " + movie);
                    kafkaProducer.send(new ProducerRecord<>(targetTopic, movie));
                    // sleeping to slow down the pace a bit
                    Thread.sleep(appConfig.getProducerFrequencyMs());
                }
        }
        catch (InterruptedException e) {
            log.warn("Avro Producer interrupted");
        } finally {
            close();
        }
    }

    public void close() {
        log.info("Closing Producer");
        kafkaProducer.close();
        latch.countDown();
    }
}
