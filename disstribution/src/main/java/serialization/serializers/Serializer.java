package serialization.serializers;

import java.util.Optional;

/**
 * serializer from source type <S> to target type <T>
 *
 * Created by Yarden on 1/16/2018.
 */
public interface Serializer<S,T> {

    Optional<T> serialize(S objectToSerialize);
}
