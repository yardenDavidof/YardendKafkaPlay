package serialization.deserializers;

import java.util.Optional;

/**
 * deserializer from source type <S> to target type <T>
 * <p>
 * Created by Yarden on 1/16/2018.
 */
public interface Deserializer<S, T> {

    Class<T> getTargetClass();
    Optional<T> deserialize(S objectToDeserialize);
}
