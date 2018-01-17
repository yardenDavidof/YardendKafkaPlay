package subscribes;

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
}
