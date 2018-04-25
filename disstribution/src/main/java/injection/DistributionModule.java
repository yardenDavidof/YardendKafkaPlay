package injection;

import com.google.inject.AbstractModule;
import factories.DistributionFactory;
import factories.KafkaDistributionFactory;
import serialization.factories.JacksonSerializationFactory;
import serialization.factories.SerializationFactory;

/**
 * Created by Yarden on 1/14/2018.
 */
public class DistributionModule extends AbstractModule {
    protected void configure() {
        bind(DistributionFactory.class).to(KafkaDistributionFactory.class);
        bind(SerializationFactory.class).to(JacksonSerializationFactory.class);
    }
}
