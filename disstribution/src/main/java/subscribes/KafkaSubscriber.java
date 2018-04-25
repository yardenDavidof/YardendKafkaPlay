//package subscribes;
//
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import scala.Int;
//import serialization.deserializers.Deserializer;
//import serialization.factories.SerializationFactory;
//
//import java.util.HashMap;
//import java.util.Properties;
//import java.util.concurrent.Executor;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//import java.util.function.Consumer;
//import java.util.stream.IntStream;
//
//public class KafkaSubscriber<T> implements Subscriber<T>{
//
//    private static final Logger logger = LoggerFactory.getLogger(KafkaSubscriber.class);
//    private String topic;
//    private String consumerGroup;
//    private Deserializer<String,T> deserializer;
//    private ExecutorService threadPool;
//    private HashMap<Integer, KafkaConsumer<Long,String>> consumersMap;
//
//    public KafkaSubscriber(String consumerGroup, String topic, SerializationFactory<String,T> serializationFactory){
//        this.topic = topic;
//        this.consumerGroup = consumerGroup;
//        consumersMap = new HashMap<>();
//        deserializer = serializationFactory.createDeserializer();
//        initSubscribers();
//    }
//
//    private void initSubscribers(){
//        Properties properties = ConsumerPropertiesBuilder.createConsumerProperties(consumerGroup);
//        int numOfPartitions = getNumOfPartitions(properties);
//        IntStream.range(0,numOfPartitions).forEach(partitionNum -> consumersMap.put(partitionNum, new KafkaConsumer<Long, String>(properties)));
//        threadPool = Executors.newFixedThreadPool(numOfPartitions);
//    }
//
//    private int getNumOfPartitions(Properties properties){
//        final KafkaConsumer consumer = new KafkaConsumer<>(properties);;
//        try {
//            return consumer.partitionsFor(topic).size();
//        }finally {
//            // TODO = check if finally happens
//            consumer.close();
//        }
//
//    }
//
//    @Override
//    public void subscribe(String topic, Consumer<T> messageFunc) {
//        consumersMap.entrySet().stream().forEach((partition,consumer) ->{
//            threadPool.submit(new ConsumerThread<T>())
//        });
//    }
//
//    @Override
//    public void close() throws Exception {
//
//    }
//
//}
