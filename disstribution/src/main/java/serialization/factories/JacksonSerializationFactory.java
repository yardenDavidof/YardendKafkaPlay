package serialization.factories;

import serialization.deserializers.Deserializer;
import serialization.deserializers.JacksonDeserializer;
import serialization.serializers.JacksonSerializer;
import serialization.serializers.Serializer;

public class JacksonSerializationFactory<T> implements SerializationFactory<T,String> {

    @Override
    public Serializer<T, String> createSerializer() {
        return new JacksonSerializer();
    }

    @Override
    public Deserializer<String, T> createDeserializer(Class<T> targetClass) {
        return new JacksonDeserializer(targetClass);
    }
}
