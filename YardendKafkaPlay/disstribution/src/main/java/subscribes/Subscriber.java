package subscribes;

/**
 * Created by Yarden on 1/14/2018.
 */
public interface Subscriber extends AutoCloseable{

    void subscribe(String topic);
}
