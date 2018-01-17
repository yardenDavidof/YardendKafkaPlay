package publishers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialization.factories.SerializationFactory;
import serialization.serializers.Serializer;
import utils.ExecutorsUtils;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Yarden on 1/14/2018.
 */
public class KafkaPublisher<T> implements Publisher<T> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaPublisher.class);
    private ExecutorService threadpoll = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
    private Producer<Long, String> producer;
    private Serializer<T, String> serializer;

    public KafkaPublisher(SerializationFactory<String, T> serializationFactory) {
        this.serializer = serializationFactory.createSerializer();
        this.producer = createProducer();
    }

    private Producer<Long, String> createProducer() {
        Properties properties = ProducerPropertiesBuilder.createProducerProperties();
        return new KafkaProducer<>(properties);
    }

    @Override
    public void publish(T message, String topic) {
        String serializedMessage = serializer.serialize(message);
        try {
            final ProducerRecord<Long, String> record =
                    new ProducerRecord(topic, OffsetDateTime.now(),
                            serializedMessage);

            OffsetDateTime startTime = OffsetDateTime.now();
            RecordMetadata metadata = producer.send(record).get();

            Duration timeElasped = Duration.between(startTime, OffsetDateTime.now());

            logger.trace("record was sent in {} milliseconds with meta(partition={}, offset={}) \n  key: {}, value: {} ",
                    timeElasped.toMillis(), metadata.partition(), metadata.offset(), record.key(), record.value());

        } catch (InterruptedException e) {
            logger.error("sending message: {} was interrupted", message, e);
        } catch (ExecutionException e) {
            logger.error("failed to send message: {}", message, e);
        }

    }

    @Override
    public CompletableFuture<Void> publishAsync(T message, String topic) {
        return CompletableFuture.runAsync(() -> publish(message, topic), threadpoll);
    }

    @Override
    public void close() throws Exception {
        producer.close();
        ExecutorsUtils.shutdownExecutor(threadpoll);
    }
}
