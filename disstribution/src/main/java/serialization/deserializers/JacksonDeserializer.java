package serialization.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class JacksonDeserializer<T> implements Deserializer<String, T> {

    private static final Logger logger = LoggerFactory.getLogger(JacksonDeserializer.class);
    private Class<T> targetClass;
    private ObjectMapper mapper = new ObjectMapper();

    public JacksonDeserializer(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    @Override
    public Class<T> getTargetClass() {
        return targetClass;
    }

    @Override
    public Optional<T> deserialize(String objectToDeserialize) {
        try {
            return Optional.of(mapper.readValue(objectToDeserialize, targetClass));
        } catch (IOException e) {
            logger.error("failed to deserialize JSON object: {} to {}", objectToDeserialize, targetClass, e);
        }
        return Optional.empty();
    }
}
