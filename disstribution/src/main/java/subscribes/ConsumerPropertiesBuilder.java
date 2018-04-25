package subscribes;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;

/**
 * Created by Yarden on 1/16/2018.
 */
public class ConsumerPropertiesBuilder {

    // TODO - set or get from zk
    private static final String ZOOKEEPER_CONNECT = "";

    // TODO - use ConsumerConfig like ProducerConfig + what about deserializers
    public static Properties createConsumerProperties(String consumerGroup){
        final Properties props = new Properties();
        props.put("zookeeper.connect", ZOOKEEPER_CONNECT);
        props.put("group.id", consumerGroup);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000"); // TODO - optimize properties
        return props;
    }

    public static Properties createStreamProperties(){
        final Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "yardend-kakfa");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        return props;
    }
}
