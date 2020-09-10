package com.ch.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class TraceLogIdConverter extends ClassicConverter {

    private static final String TRACE_LOG_ID ="traceId";
    public TraceLogIdConverter() {
    }

    public String convert(ILoggingEvent event) {
        String v = (String)event.getMDCPropertyMap().get(TRACE_LOG_ID);
        return v == null ? String.valueOf(Thread.currentThread().getId()) : v;
    }
}
