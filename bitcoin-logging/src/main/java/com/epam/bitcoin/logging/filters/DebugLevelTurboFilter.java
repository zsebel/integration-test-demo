package com.epam.bitcoin.logging.filters;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.epam.bitcoin.logging.model.LoggingConstants;
import org.slf4j.MDC;
import org.slf4j.Marker;

public class DebugLevelTurboFilter extends TurboFilter {

    // TODO: change package
    private static final String APPLICATION_LOGGER_PREFIX = "com.epam.bitcoin";

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level, String s, Object[] objects, Throwable throwable) {
        FilterReply filterReply = FilterReply.NEUTRAL;
        String debugTraceEnabled = MDC.get(LoggingConstants.Debug.TRACE);
        if (isDebugLoggingEnabled(debugTraceEnabled) && isApplicationLogger(logger, level)) {
            filterReply = FilterReply.ACCEPT;
        }
        return filterReply;
    }

    private static boolean isDebugLoggingEnabled(final String debugTraceEnabled) {
        return LoggingConstants.Debug.TRACE_ENABLED.equals(debugTraceEnabled);
    }

    private static boolean isApplicationLogger(final Logger logger, final Level level) {
        return level == Level.DEBUG && logger.getName().startsWith(APPLICATION_LOGGER_PREFIX);
    }
}
