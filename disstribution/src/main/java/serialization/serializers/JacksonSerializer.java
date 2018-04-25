package serialization.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Optional;

public class JacksonSerializer<T> implements Serializer<T,String> {

    private static final Logger logger = LoggerFactory.getLogger(JacksonSerializer.class);
    private final ObjectMapper mapper = new ObjectMapper();

    public JacksonSerializer(){ }

    @Override
    public Optional<String> serialize(T objectToSerialize) {
        try {
            return Optional.of(mapper.writeValueAsString(objectToSerialize));
        } catch (JsonProcessingException e) {
            logger.error("failed to serialize {} object: {} to JSON", objectToSerialize.getClass().getSimpleName(), objectToSerialize, e);
        }
        return Optional.empty();
    }
}
