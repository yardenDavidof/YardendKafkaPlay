package publishers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.zookeeper.Op;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialization.factories.SerializationFactory;
import serialization.serializers.Serializer;
import utils.ExecutorsUtils;

import java.sql.ResultSetMetaData;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Yarden on 1/14/2018.
 */
public class KafkaPublisher implements Publisher {

    private static final Logger logger = LoggerFactory.getLogger(KafkaPublisher.class);
    private ExecutorService threadpoll = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
    private Producer<Long, String> producer;
    private Serializer<?, String> serializer;

    public KafkaPublisher(SerializationFactory<?, String> serializationFactory, Properties props) {
        this.serializer = serializationFactory.createSerializer();
        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public <T> Optional<RecordMetadata> publish(T message, String topic) {
        Optional<String> serializedMessage = ((Serializer<T,String>)serializer).serialize(message);
        if (!serializedMessage.isPresent()) {
            return Optional.empty();
        }

        try {
            final ProducerRecord<Long, String> record =
                    new ProducerRecord(topic, OffsetDateTime.now(),
                            serializedMessage);
            OffsetDateTime startTime = OffsetDateTime.now();
            RecordMetadata metadata = producer.send(record).get();
            Duration timeElasped = Duration.between(startTime, OffsetDateTime.now());
            logger.trace("record was sent in {} milliseconds with meta(partition={}, offset={}) \n  key: {}, value: {} ",
                    timeElasped.toMillis(), metadata.partition(), metadata.offset(), record.key(), record.value());
            return Optional.of(metadata);
        } catch (InterruptedException e) {
            logger.error("sending message: {} was interrupted", message, e);
        } catch (ExecutionException e) {
            logger.error("failed to send message: {}", message, e);
        }
        return Optional.empty();

    }

    @Override
    public <T> CompletableFuture<Optional<RecordMetadata>> publishAsync(T message, String topic) {
        return CompletableFuture.supplyAsync(() -> publish(message, topic), threadpoll);
    }

    @Override
    public void close() throws Exception {
        producer.close();
        ExecutorsUtils.shutdownExecutor(threadpoll);
    }
}
