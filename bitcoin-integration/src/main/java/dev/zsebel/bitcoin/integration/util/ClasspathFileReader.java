package dev.zsebel.bitcoin.integration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ClasspathFileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathFileReader.class);

    private final ObjectMapper objectMapper;

    @Autowired
    public ClasspathFileReader(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T read(final Resource resource, final Class<T> targetClass) {
        T result = null;
        try {
            result = objectMapper.readValue(resource.getInputStream(), targetClass);
        } catch (IOException exception) {
            LOGGER.error("Failed to load file.");
        }
        return result;
    }
}
