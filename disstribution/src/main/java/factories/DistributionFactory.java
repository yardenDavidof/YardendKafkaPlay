package factories;

import publishers.Publisher;
import subscribes.Subscriber;

/**
 * Created by Yarden on 1/14/2018.
 */
public interface DistributionFactory {

    <T> Publisher<T> createPublisher();

    <T> Subscriber<T> createSubscriber(String topic);
}
