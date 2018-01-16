package serialization.serializers;

/**
 * serializer from source type <S> to target type <T>
 *
 * Created by Yarden on 1/16/2018.
 */
public interface Serializer<S,T> {

    T serialize(S objectToSerialize);
}
