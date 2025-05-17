package com.epam.bitcoin.client.factory;

import com.epam.bitcoin.logging.model.LoggingConstants;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import reactor.util.context.Context;

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
