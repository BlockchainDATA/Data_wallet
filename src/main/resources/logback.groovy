import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.LevelFilter
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.spi.FilterReply

import static ch.qos.logback.classic.Level.*

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%red(%d{HH:mm:ss.SSS}) %blue([%thread %X{traceId},%X{spanId}]) %yellow(%-5level) %green(%logger{36}) - %msg%n"
    }
}

appender("debug", RollingFileAppender) {
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%red(%d{HH:mm:ss.SSS}) %blue([%thread %X{traceId},%X{spanId}]) %yellow(%-5level) %green(%logger{36}) - %msg%n"
    }
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        fileNamePattern = '/data/logs/antube/wallet/debug-%d{yyyy-MM-dd}.%i.log'
        maxFileSize = "50MB"
    }
    filter(ThresholdFilter) {
        level = DEBUG
    }
}

appender("info", RollingFileAppender) {
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%red(%d{HH:mm:ss.SSS}) %blue([%thread %X{traceId},%X{spanId}]) %yellow(%-5level) %green(%logger{36}) - %msg%n"
    }
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        fileNamePattern = '/data/logs/antube/wallet/info-%d{yyyy-MM-dd}.%i.log'
        maxFileSize = "50MB"
    }
    filter(LevelFilter) {
        level = INFO
        onMatch = FilterReply.ACCEPT
        onMismatch = FilterReply.DENY
    }
}

appender("error", RollingFileAppender) {
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%red(%d{HH:mm:ss.SSS}) %blue([%thread %X{traceId},%X{spanId}]) %yellow(%-5level) %green(%logger{36}) - %msg%n"
    }
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        fileNamePattern = '/data/logs/antube/wallet/error-%d{yyyy-MM-dd}.%i.log'
        maxFileSize = "50MB"
    }
    filter(LevelFilter) {
        level = ERROR
        onMatch = FilterReply.ACCEPT
        onMismatch = FilterReply.DENY
    }
}


root(INFO, ["STDOUT", "info", "error"])
