//package subscribes;
//
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.function.Consumer;
//
//public class ConsumerThread<T> implements Runnable {
//
//    private static final Logger logger = LoggerFactory.getLogger(ConsumerThread.class);
//    private KafkaConsumer<Long,String> kafkaConsumer;
//    private int partition;
//    private String topic;
//    private Consumer<T> messageFunc;
//
//    public ConsumerThread(KafkaConsumer<Long,String> kafkaConsumer, Consumer<T> messageFunc, int partition, String topic){
//        this.topic = topic;
//        this.kafkaConsumer = kafkaConsumer;
//        this.partition = partition;
//        this.messageFunc = messageFunc;
//    }
//
//    @Override
//    public void run() {
//        try {
//            kafkaConsumer.su
//            while (true){
//                kafkaConsumer.
//            }
//        }finally {
//
//        }
//
//    }
//}
