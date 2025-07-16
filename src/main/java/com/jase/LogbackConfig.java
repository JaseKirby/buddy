package com.jase;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.slf4j.LoggerFactory;

/**
 * Programmatic Logback configuration for Buddy AI Agent
 * Replaces the XML configuration with Java code
 */
public class LogbackConfig {
    
    /**
     * Configure Logback programmatically
     */
    public static void configure() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        
        // Clear any existing configuration
        context.reset();
        
        // Create pattern encoder
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
        encoder.start();
        
        // Create console appender
        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(context);
        consoleAppender.setName("CONSOLE");
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();
        
        // Configure logger for Buddy application (com.jase package)
        Logger buddyLogger = (Logger) LoggerFactory.getLogger("com.jase");
        buddyLogger.addAppender(consoleAppender);
        buddyLogger.setLevel(ch.qos.logback.classic.Level.INFO);
        buddyLogger.setAdditive(false);
        
        // Configure logger for Temporal
        Logger temporalLogger = (Logger) LoggerFactory.getLogger("io.temporal");
        temporalLogger.addAppender(consoleAppender);
        temporalLogger.setLevel(ch.qos.logback.classic.Level.INFO);
        temporalLogger.setAdditive(false);
        
        // Configure root logger
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(consoleAppender);
        rootLogger.setLevel(ch.qos.logback.classic.Level.WARN);
        
        // Mark configuration as complete
        context.start();
    }
    
    /**
     * Set logging level for a specific package
     */
    public static void setLogLevel(String packageName, ch.qos.logback.classic.Level level) {
        Logger logger = (Logger) LoggerFactory.getLogger(packageName);
        logger.setLevel(level);
    }
    
    /**
     * Enable debug logging for Buddy application
     */
    public static void enableDebugLogging() {
        setLogLevel("com.jase", ch.qos.logback.classic.Level.DEBUG);
    }
}
