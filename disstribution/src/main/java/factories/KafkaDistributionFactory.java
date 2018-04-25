package factories;

import com.google.inject.Inject;
import publishers.KafkaPublisher;
import publishers.Publisher;
import serialization.factories.SerializationFactory;
import subscribes.KafkaStreamSubscriber;
import subscribes.Subscriber;

public class KafkaDistributionFactory implements DistributionFactory{

    private SerializationFactory serializationFactory;

    @Inject
    public KafkaDistributionFactory(SerializationFactory serializationFactory){
        this.serializationFactory = serializationFactory;
    }

    @Override
    public <T> Publisher<T> createPublisher() {
        return new KafkaPublisher<>(serializationFactory);
    }

    @Override
    public <T> Subscriber<T> createSubscriber(String topic) {
        return new KafkaStreamSubscriber<>("default", topic, serializationFactory);
    }
}
