package dev.zsebel.bitcoin.integration.stub.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;

public class ClasspathFileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClasspathFileReader.class);

    private final ObjectMapper objectMapper;

    public ClasspathFileReader(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T read(final Resource resource, final Class<T> targetClass) {
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, targetClass);
        } catch (IOException ioException) {
            LOGGER.error(ioException.getMessage(), ioException);
            throw new MissingResourceException("Failed to load file from classpath.", resource.getClass().getName(), resource.getFilename());
        }
    }
}
