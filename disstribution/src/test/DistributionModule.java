import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import factories.DistributionFactory;
import factories.KafkaDistributionFactory;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import serialization.factories.JacksonSerializationFactory;
import serialization.factories.SerializationFactory;

import java.util.Properties;

public class DistributionModule extends AbstractModule {

    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String ZOOKEEPER_CONNECT = "localhost:2181";
    private static final String CONSUMER_GROUP = "default";

    @Override
    protected void configure() {
        bind(DistributionFactory.class).to(KafkaDistributionFactory.class);
        bind(SerializationFactory.class).to(JacksonSerializationFactory.class);
    }

    @Provides @Named("consumer")
    Properties consumerPropertiesProvider(){
        final Properties props = new Properties();
        props.put("zookeeper.connect", ZOOKEEPER_CONNECT);
        props.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.put("group.id", CONSUMER_GROUP);
        props.put("key.deserializer", LongDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("zookeeper.session.timeout.ms", "20000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000"); // TODO - optimize properties
        return props;
    }

    @Provides @Named("producer")
    Properties producerPropertiesProvider(){
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        return props;
    }
}
