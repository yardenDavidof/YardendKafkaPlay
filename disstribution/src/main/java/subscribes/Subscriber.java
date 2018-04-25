package subscribes;

/**
 * Created by Yarden on 1/14/2018.
 */
public interface Subscriber<T> extends AutoCloseable{

    void subscribe(java.util.function.Consumer<T> messageFunc);
}
