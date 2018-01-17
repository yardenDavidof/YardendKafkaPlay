package subscribes;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialization.deserializers.Deserializer;
import serialization.factories.SerializationFactory;
import utils.ExecutorsUtils;

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

    private static final Logger logger = LoggerFactory.getLogger(KafkaSubscriber.class);
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
    public void subscribe(String topic, java.util.function.Consumer<T> onMessageFunc) {
        logger.trace("start sunscribe of {} to topic {}", getClass().getSimpleName(), topic);
        List<KafkaStream<byte[], byte[]>> streams = streamsMap.get(topic);
        IntStream.range(0, numOfThreads).forEach(threadNum -> threadPoll.execute(new ConsumerThread<T>(streams.get(threadNum), threadNum, onMessageFunc, deserializer)));
    }

    @Override
    public void close() throws Exception {
        logger.info("close {}", getClass().getSimpleName());
        consumerConnector.shutdown();
        ExecutorsUtils.shutdownExecutor(threadPoll);
    }
}
