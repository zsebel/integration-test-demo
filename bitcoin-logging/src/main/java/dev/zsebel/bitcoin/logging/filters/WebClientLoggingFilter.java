package dev.zsebel.bitcoin.logging.filters;

import dev.zsebel.bitcoin.logging.model.LoggingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;
import reactor.util.context.ContextView;

@Component
public class WebClientLoggingFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebClientLoggingFilter.class);

    public ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            LOGGER.info("Outgoing {} request to {}", clientRequest.method(), clientRequest.url());
            return Mono.just(clientRequest);
        });
    }

    public ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            HttpStatusCode status = clientResponse.statusCode();
            return clientResponse.bodyToMono(String.class)
                .defaultIfEmpty("")
                .flatMap(body ->
                    Mono.deferContextual(contextView -> {
                        configureMdcForDebugLogging(contextView);
                        LOGGER.info("Response Status: {}", status);
                        LOGGER.debug("Response Body: {}", body);
                        MDC.clear();
                        ClientResponse rebuilt = createClientResponse(clientResponse, body);
                        return Mono.just(rebuilt);
                    })
                );
        });
    }

    private static void configureMdcForDebugLogging(final ContextView contextView) {
        String debugTraceEnabled = contextView.getOrDefault(LoggingConstants.Debug.TRACE, LoggingConstants.Debug.TRACE_DISABLED);
        if (LoggingConstants.Debug.TRACE_ENABLED.equals(debugTraceEnabled)) {
            MDC.put(LoggingConstants.Debug.TRACE, LoggingConstants.Debug.TRACE_ENABLED);
        }
    }

    private static ClientResponse createClientResponse(final ClientResponse clientResponse, final String body) {
        return ClientResponse
            .create(clientResponse.statusCode())
            .headers(headers -> headers.addAll(clientResponse.headers().asHttpHeaders()))
            .body(body)
            .build();
    }
}
