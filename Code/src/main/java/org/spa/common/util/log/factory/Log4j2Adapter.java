package org.spa.common.util.log.factory;

import org.apache.logging.log4j.LogManager;
import org.spa.common.util.log.Level;
import org.spa.common.util.log.Logger;

import java.util.function.Supplier;

/**
 * An internal adapter to wrap access to an underlying logger implementation.<br/>
 * We use Log4j2.
 *
 * @author hadrian
 * @since 10-May-20
 */
class Log4j2Adapter implements Logger {
    private final org.apache.logging.log4j.Logger logger;

    public Log4j2Adapter() {
        logger = LogManager.getRootLogger();
    }

    public Log4j2Adapter(Class<?> loggingClass) {
        logger = LogManager.getLogger(loggingClass);
    }

    public Log4j2Adapter(String loggerName) {
        logger = LogManager.getLogger(loggerName);
    }

    @Override
    public void log(Level level, Supplier<CharSequence> messageSupplier) {
        logger.log(level.getUnderlyingLevel(), messageSupplier);
    }

    @Override
    public void log(Level level, Supplier<CharSequence> messageSupplier, Throwable thrown) {
        logger.log(level.getUnderlyingLevel(), messageSupplier, thrown);
    }

    @Override
    public void log(Level level, CharSequence message) {
        logger.log(level.getUnderlyingLevel(), message);
    }

    @Override
    public void log(Level level, CharSequence message, Throwable thrown) {
        logger.log(level.getUnderlyingLevel(), message, thrown);
    }

    @Override
    public void trace(Supplier<CharSequence> messageSupplier) {
        logger.trace(messageSupplier);
    }

    @Override
    public void trace(Supplier<CharSequence> messageSupplier, Throwable thrown) {
        logger.trace(messageSupplier, thrown);
    }

    @Override
    public void trace(CharSequence message) {
        logger.trace(message);
    }

    @Override
    public void trace(CharSequence message, Throwable thrown) {
        logger.trace(message, thrown);
    }

    @Override
    public void debug(Supplier<CharSequence> messageSupplier) {
        logger.debug(messageSupplier);
    }

    @Override
    public void debug(Supplier<CharSequence> messageSupplier, Throwable thrown) {
        logger.debug(messageSupplier, thrown);
    }

    @Override
    public void debug(CharSequence message) {
        logger.debug(message);
    }

    @Override
    public void debug(CharSequence message, Throwable thrown) {
        logger.debug(message, thrown);
    }

    @Override
    public void info(Supplier<CharSequence> messageSupplier) {
        logger.info(messageSupplier);
    }

    @Override
    public void info(Supplier<CharSequence> messageSupplier, Throwable thrown) {
        logger.info(messageSupplier, thrown);
    }

    @Override
    public void info(CharSequence message) {
        logger.info(message);
    }

    @Override
    public void info(CharSequence message, Throwable thrown) {
        logger.info(message, thrown);
    }

    @Override
    public void warn(Supplier<CharSequence> messageSupplier) {
        logger.warn(messageSupplier);
    }

    @Override
    public void warn(Supplier<CharSequence> messageSupplier, Throwable thrown) {
        logger.warn(messageSupplier, thrown);
    }

    @Override
    public void warn(CharSequence message) {
        logger.warn(message);
    }

    @Override
    public void warn(CharSequence message, Throwable thrown) {
        logger.warn(message, thrown);
    }

    @Override
    public void error(Supplier<CharSequence> messageSupplier) {
        logger.error(messageSupplier);
    }

    @Override
    public void error(Supplier<CharSequence> messageSupplier, Throwable thrown) {
        logger.error(messageSupplier, thrown);
    }

    @Override
    public void error(CharSequence message) {
        logger.error(message);
    }

    @Override
    public void error(CharSequence message, Throwable thrown) {
        logger.error(message, thrown);
    }

    @Override
    public void fatal(Supplier<CharSequence> messageSupplier) {
        logger.fatal(messageSupplier);
    }

    @Override
    public void fatal(Supplier<CharSequence> messageSupplier, Throwable thrown) {
        logger.fatal(messageSupplier, thrown);
    }

    @Override
    public void fatal(CharSequence message) {
        logger.fatal(message);
    }

    @Override
    public void fatal(CharSequence message, Throwable thrown) {
        logger.fatal(message, thrown);
    }
}

