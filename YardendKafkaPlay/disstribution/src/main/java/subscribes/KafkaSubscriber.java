package subscribes;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import serialization.deserializers.Deserializer;
import serialization.factories.SerializationFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by Yarden on 1/16/2018.
 */
public class KafkaSubscriber<T> implements Subscriber<T> {

    private final ConsumerConnector consumerConnector;
    private ExecutorService threadPoll;
    private String topic;
    private Deserializer<String, T> deserializer;
    private Map<String, List<KafkaStream<byte[], byte[]>>> streamsMap;
    private int numOfThreads;

    public KafkaSubscriber(int parallizeFactor, String topic, SerializationFactory<String, T> serializationFactory) {
        consumerConnector = createConsumer();
        threadPoll = Executors.newFixedThreadPool(parallizeFactor);
        this.topic = topic;
        this.deserializer = serializationFactory.createDeserializer();
        this.numOfThreads = parallizeFactor;
        initKafkaStreams(parallizeFactor);
    }

    private ConsumerConnector createConsumer() {
        Properties consumerProps = ConsumerPropertiesBuilder.createConsumerProperties("yardend_consumer_group");
        return Consumer.createJavaConsumerConnector(new ConsumerConfig(consumerProps));
    }

    private void initKafkaStreams(int parallizmFactor) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, parallizmFactor);
        streamsMap = consumerConnector.createMessageStreams(topicCountMap);
    }

    @Override
    public void subscribe(String topic, java.util.function.Consumer<T> messageFunc) {
        List<KafkaStream<byte[], byte[]>> streams = streamsMap.get(topic);
        IntStream.range(0, numOfThreads).forEach(threadNum -> threadPoll.execute(new SubscriberThread<T>(streams.get(threadNum), threadNum, messageFunc, deserializer)));
    }

    @Override
    public void close() throws Exception {
        consumerConnector.shutdown();
        threadPoll.shutdown();
        try{
            if (!threadPoll.awaitTermination(10000, TimeUnit.MILLISECONDS)){
                threadPoll.shutdownNow();
            }
        }catch (InterruptedException e){
            threadPoll.shutdownNow();
        }
    }
}
