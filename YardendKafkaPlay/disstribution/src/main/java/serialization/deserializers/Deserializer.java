package serialization.deserializers;

/**
 * deserializer from source type <S> to target type <T>
 * <p>
 * Created by Yarden on 1/16/2018.
 */
public interface Deserializer<S, T> {

    Class<T> getTargetClass();
    T deserialize(S objectToDeserialize);
}
