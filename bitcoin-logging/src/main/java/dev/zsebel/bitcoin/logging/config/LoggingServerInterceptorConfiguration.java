package dev.zsebel.bitcoin.logging.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import dev.zsebel.bitcoin.logging.interceptor.LoggingServerInterceptor;

@Configuration
public class LoggingServerInterceptorConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingServerInterceptor());
    }
}
