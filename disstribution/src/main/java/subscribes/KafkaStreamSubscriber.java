package subscribes;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.kafka.clients.consumer.KafkaConsumer;
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
import java.util.stream.IntStream;

/**
 * Created by Yarden on 1/16/2018.
 */
public class KafkaStreamSubscriber<T> implements Subscriber<T> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStreamSubscriber.class);
    private final ConsumerConnector consumerConnector;
    private ExecutorService threadPoll;
    private String topic;
    private int partitionsNum;
    private Deserializer<String, T> deserializer;
    private Map<String, List<KafkaStream<byte[], byte[]>>> streamsMap;

    public KafkaStreamSubscriber(String consumerGroup, String topic, SerializationFactory<String, T> serializationFactory) {
        this.topic = topic;
        this.deserializer = serializationFactory.createDeserializer();
        Properties properties = ConsumerPropertiesBuilder.createConsumerProperties(consumerGroup);
        CalcNumOfPartitions(properties);
        consumerConnector = Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));
        threadPoll = Executors.newFixedThreadPool(partitionsNum);
        initKafkaStreams();
    }

    private void CalcNumOfPartitions(Properties properties){
        KafkaConsumer kafkaConsumer = new KafkaConsumer(properties);
        try {
            partitionsNum = kafkaConsumer.partitionsFor(topic).size();
        }finally {
            kafkaConsumer.close();
        }

    }

    private void initKafkaStreams() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, partitionsNum);
        streamsMap = consumerConnector.createMessageStreams(topicCountMap);
    }

    @Override
    public void subscribe(java.util.function.Consumer<T> onMessageFunc) {
        logger.trace("start sunscribe of {} to topic {}", getClass().getSimpleName(), topic);
        List<KafkaStream<byte[], byte[]>> streams = streamsMap.get(topic);
        IntStream.range(0, partitionsNum).forEach(partitionNum -> threadPoll.execute(new StreamReaderThread<T>(streams.get(partitionNum), partitionNum, onMessageFunc, deserializer)));
    }

    @Override
    public void close() throws Exception {
        logger.info("close {}", getClass().getSimpleName());
        consumerConnector.shutdown();
        ExecutorsUtils.shutdownExecutor(threadPoll);
    }
}
