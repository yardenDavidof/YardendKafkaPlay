package subscribes;

import java.util.function.Consumer;

/**
 * Created by Yarden on 1/14/2018.
 */
public interface Subscriber<T> extends AutoCloseable{

    void subscribe(Consumer<T> messageFunc);
}
