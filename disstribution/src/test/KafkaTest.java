import com.google.inject.Guice;
import com.google.inject.Injector;
import factories.DistributionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import publishers.Publisher;
import subscribes.Subscriber;

public class KafkaTest {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTest.class);
    private final Injector injector = Guice.createInjector(new DistributionModule());
    private static final String TOPIC = "Test";
    private Publisher publisher;
    private Subscriber<TestObject> subscriber;

    @BeforeTest
    public void setup(){
        DistributionFactory distributionFactory = injector.getInstance(DistributionFactory.class);
        publisher = distributionFactory.createPublisher();
        subscriber = distributionFactory.createSubscriber(TOPIC, TestObject.class);
    }

    @Test
    void testKafka(){
        subscriber.subscribe(this::testKafka);
        TestObject testObject = new TestObject("yardend", 25);
        publisher.publish(testObject,TOPIC);
    }

    private void testKafka(TestObject testObject){
        logger.info("message received : {}", testObject);
    }
}
