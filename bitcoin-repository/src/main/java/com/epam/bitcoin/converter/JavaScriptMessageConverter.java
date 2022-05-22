package com.epam.bitcoin.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

@Component
public class JavaScriptMessageConverter extends AbstractJackson2HttpMessageConverter {

    private static final MediaType MEDIA_TYPE_APPLICATION_JAVASCRIPT = new MediaType("application", "javascript");

    private JavaScriptMessageConverter() {
        super(Jackson2ObjectMapperBuilder.json().build(), MEDIA_TYPE_APPLICATION_JAVASCRIPT);
    }
}
