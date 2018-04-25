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

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

public class KafkaTest {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTest.class);
    private final Injector injector = Guice.createInjector(new DistributionModule());
    private static final String TOPIC = "Test";
    private Publisher publisher;
    private Subscriber<TestObject> subscriber;
    private TestObject testObject;

    @BeforeTest
    public void setup(){
        DistributionFactory distributionFactory = injector.getInstance(DistributionFactory.class);
        publisher = distributionFactory.createPublisher();
        subscriber = distributionFactory.createSubscriber(TOPIC, TestObject.class);
        testObject = new TestObject("yardend", 25);
    }

    @Test
    void testKafka(){
        subscriber.subscribe(this::testKafka);
        publisher.publishAsync(testObject,TOPIC);
            while (true){}

    }

    private void testKafka(TestObject message){
        logger.info("message received : {}", message);
        Assert.assertEquals(message.getAge(), testObject.getAge());
        Assert.assertEquals(message.getUUId(), testObject.getUUId());
        Assert.assertEquals(message.getCreationTime(), testObject.getCreationTime());
        Assert.assertEquals(message.getName(), testObject.getName());
        Assert.assertEquals(message.getTime(), testObject.getTime());
        logger.info("time elasped since creation: {}", Duration.between(testObject.getCreationTime(), OffsetDateTime.now()).toMillis());
    }
}
