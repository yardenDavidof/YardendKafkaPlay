package factories;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import publishers.KafkaPublisher;
import publishers.Publisher;
import serialization.factories.SerializationFactory;
import subscribes.KafkaStreamSubscriber;
import subscribes.Subscriber;

import java.util.Properties;

public class KafkaDistributionFactory implements DistributionFactory {

    private SerializationFactory serializationFactory;
    private Properties consumerProps;
    private Properties producerProps;

    @Inject
    public KafkaDistributionFactory(SerializationFactory serializationFactory,
                                    @Named("consumer") Properties consumerProperties,
                                    @Named("producer") Properties producerProperties) {
        this.serializationFactory = serializationFactory;
        this.consumerProps = consumerProperties;
        this.producerProps = producerProperties;
    }

    @Override
    public Publisher createPublisher() {
        return new KafkaPublisher(serializationFactory, producerProps);
    }

    @Override
    public <T> Subscriber<T> createSubscriber(String topic, Class<T> targetClass) {
        return new KafkaStreamSubscriber<>(topic, targetClass, serializationFactory, consumerProps);
    }
}
