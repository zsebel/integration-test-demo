package com.epam.bitcoin.integration;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.epam.bitcoin.BitcoinResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReader.class);

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T read(Resource resource, Class<T> targetClass) {
        Object result = null;
        try {
            result = objectMapper.readValue(resource.getInputStream(), targetClass);
        } catch (IOException exception) {
            LOGGER.error("Failed to load file.");
        }
        return (T) result;
    }
}
