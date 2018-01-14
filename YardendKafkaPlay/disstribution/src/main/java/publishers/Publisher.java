package publishers;

import java.util.concurrent.CompletableFuture;

/**
 * Created by Yarden on 1/14/2018.
 */
public interface Publisher<T> extends AutoCloseable{

    void publish(T message, String topic);
    CompletableFuture<Void> publishAsync(T message, String topic);
}
