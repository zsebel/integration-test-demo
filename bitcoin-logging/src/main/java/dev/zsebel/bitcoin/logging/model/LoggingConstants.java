package dev.zsebel.bitcoin.logging.model;

public class LoggingConstants {

    private LoggingConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static class Debug {
        public static final String TRACE = "Debug-Trace";
        public static final String TRACE_ENABLED = "enabled";
        public static final String TRACE_DISABLED = "";

        private Debug() {
            throw new IllegalStateException("Utility class");
        }
    }
}
