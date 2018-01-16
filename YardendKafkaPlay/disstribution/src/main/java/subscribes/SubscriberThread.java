package subscribes;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import serialization.deserializers.Deserializer;

import java.util.function.Consumer;

/**
 * Created by Yarden on 1/16/2018.
 */
public class SubscriberThread<T> implements Runnable {

    private KafkaStream<byte[], byte[]> stream;
    private int threadNum;
    private Consumer<T> messageFunc;
    private Deserializer<String,T> deserializer;

    public SubscriberThread(KafkaStream<byte[], byte[]> stream, int threadNum, Consumer<T> messageFunc, Deserializer<String,T> deserializer) {
        this.stream = stream;
        this.threadNum = threadNum;
        this.messageFunc = messageFunc;
        this.deserializer = deserializer;
    }

    @Override
    public void run() { // TODO - in case the iterator fails ?
        ConsumerIterator<byte[], byte[]> streamIterator = stream.iterator();
        while (streamIterator.hasNext()) {
            String message = new String(streamIterator.next().message());
            T deserializedMsg = deserializer.deserialize(message); //TODO - class tp topic ?
            messageFunc.accept(deserializedMsg);
        }
    }
}
