package dev.zsebel.bitcoin.client.factory;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import reactor.util.context.Context;

import dev.zsebel.bitcoin.logging.model.LoggingConstants;

@Component
public class ReactiveContextFactory {

    public Context createContext() {
        return Context.of(LoggingConstants.Debug.TRACE, isDebugTraceEnabled());
    }

    private String isDebugTraceEnabled() {
        return MDC.get(LoggingConstants.Debug.TRACE) != null
            ? LoggingConstants.Debug.TRACE_ENABLED
            : LoggingConstants.Debug.TRACE_DISABLED;
    }
}
