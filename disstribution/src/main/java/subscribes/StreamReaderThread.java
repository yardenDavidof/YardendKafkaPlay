package subscribes;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialization.deserializers.Deserializer;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Yarden on 1/16/2018.
 */
public class StreamReaderThread<T> implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(StreamReaderThread.class);
    private KafkaStream<byte[], byte[]> stream;
    private int threadNum;
    private Consumer<T> onMessageFunc;
    private Deserializer<String, T> deserializer;

    public StreamReaderThread(KafkaStream<byte[], byte[]> stream, int threadNum, Consumer<T> onMessageFunc, Deserializer<String, T> deserializer) {
        this.stream = stream;
        this.threadNum = threadNum;
        this.onMessageFunc = onMessageFunc;
        this.deserializer = deserializer;
    }

    @Override
    public void run() { // TODO - in case the iterator fails ?
        ConsumerIterator<byte[], byte[]> streamIterator = stream.iterator();
        while (streamIterator.hasNext()) {
            MessageAndMetadata<byte[], byte[]> currIt = streamIterator.next();
            String message = new String(currIt.message());
            logger.trace("message recieved at {} number {} from topic: {}, partition:{}. /n message: {}", getClass().getSimpleName(), threadNum, currIt.topic(), currIt.partition(), message);
            Optional<T> deserializedMsg = deserializer.deserialize(message);
            deserializedMsg.ifPresentOrElse(onMessageFunc::accept, () -> logger.error("message: {} didnt processed because of an deserializarion problem", message));
        }
    }
}
