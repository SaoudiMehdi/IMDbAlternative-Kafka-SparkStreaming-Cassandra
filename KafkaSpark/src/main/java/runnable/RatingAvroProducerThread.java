package Runnable;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import util.AppConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

import util.RatingApi;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class RatingAvroProducerThread implements Runnable  {
    private Logger log = LoggerFactory.getLogger(RatingAvroProducerThread.class.getSimpleName());

    private final AppConfig appConfig;
    private final ArrayBlockingQueue<RatingApi> ratingQueue;
    private final CountDownLatch latch;
    private final KafkaProducer<Object, RatingApi> kafkaProducer;
    private final String targetTopic;

    public RatingAvroProducerThread(AppConfig appConfig, ArrayBlockingQueue<RatingApi> ratingQueue, CountDownLatch latch) {
        this.appConfig = appConfig;
        this.ratingQueue = ratingQueue;
        this.latch = latch;
        this.kafkaProducer = createKafkaProducer(appConfig);
        this.targetTopic = appConfig.getTopicName();
    }

    public KafkaProducer<Object, RatingApi> createKafkaProducer(AppConfig appConfig) {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, appConfig.getBootstrapServers());
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, appConfig.getSchemaRegistryUrl());

        return new KafkaProducer<>(properties);
    }

    @Override
    public void run() {
        try {
            while (latch.getCount() > 1 || ratingQueue.size() > 0){
               RatingApi rating = ratingQueue.poll();
                if (rating == null) {
                    Thread.sleep(200);
                } else {
                    log.info("Sending rating " + rating + ": " + rating);
                    kafkaProducer.send(new ProducerRecord<>(targetTopic, rating));
                    // sleeping to slow down the pace a bit
                    Thread.sleep(appConfig.getProducerFrequencyMs());
                }
            }
        } catch (InterruptedException e) {
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
