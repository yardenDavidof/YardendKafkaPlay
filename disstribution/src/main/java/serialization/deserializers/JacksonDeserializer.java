package serialization.deserializers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
        mapper.findAndRegisterModules();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);

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
