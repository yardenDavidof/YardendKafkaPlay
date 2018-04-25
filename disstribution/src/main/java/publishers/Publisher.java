package publishers;

import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Created by Yarden on 1/14/2018.
 */
public interface Publisher extends AutoCloseable{

    <T> Optional<RecordMetadata> publish(T message, String topic);
    <T> CompletableFuture<Optional<RecordMetadata>> publishAsync(T message, String topic);
}
