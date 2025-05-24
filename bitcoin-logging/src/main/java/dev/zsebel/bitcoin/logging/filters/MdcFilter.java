package dev.zsebel.bitcoin.logging.filters;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import dev.zsebel.bitcoin.logging.model.LoggingConstants;

@Component
public class MdcFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain filterChain
    ) throws ServletException, IOException {
        String debugTraceEnabled = request.getHeader(LoggingConstants.Debug.TRACE);
        if (LoggingConstants.Debug.TRACE_ENABLED.equals(debugTraceEnabled)) {
            MDC.put(LoggingConstants.Debug.TRACE, LoggingConstants.Debug.TRACE_ENABLED);
        }
        filterChain.doFilter(request, response);
    }
}
