package factories;

import publishers.Publisher;
import subscribes.Subscriber;

/**
 * Created by Yarden on 1/14/2018.
 */
public interface DistributionFactory {

     Publisher createPublisher();

    <T> Subscriber<T> createSubscriber(String topic, Class<T> targetClass);
}
