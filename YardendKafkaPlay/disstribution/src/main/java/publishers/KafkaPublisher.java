package publishers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Yarden on 1/14/2018.
 */
public class KafkaPublisher<T> implements Publisher<T> {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    @Override
    public void publish(T message, String topic) {

    }

    @Override
    public CompletableFuture<Void> publishAsync(T message, String topic) {
        return CompletableFuture.runAsync(() -> publish(message, topic), executorService);
    }

    @Override
    public void close() throws Exception {

    }
}
