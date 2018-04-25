package serialization.factories;

import serialization.deserializers.Deserializer;
import serialization.serializers.Serializer;

/**
 * serialization factory which create serializers and deserializers.
 * serializer from target <T> to source <S>
 * and deserializer from source <S> to target <T>
 * <p>
 * Created by Yarden on 1/16/2018.
 */
public interface SerializationFactory<T,S> {

    Serializer<T, S> createSerializer();

    Deserializer<S, T> createDeserializer(Class<T> targetClass);
}
