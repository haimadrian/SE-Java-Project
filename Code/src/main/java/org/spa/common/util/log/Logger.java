package org.spa.common.util.log;

import org.spa.common.util.log.factory.LoggerFactory;

import java.util.function.Supplier;

/**
 * The base logger interface of the project.<br/>
 * As we use the Adapter pattern for managing the underlying used logger, we expose an interface for the project, so
 * the API won't be affected by underlying logging implementation changes.
 *
 * <p>
 * The canonical way to obtain a Logger for a class is through {@link LoggerFactory#getLogger(Class)}. Typically, each class
 * gets its own Logger named after its fully qualified class name (the default Logger name when obtained through the
 * {@link LoggerFactory#getLogger(Class)} method). Thus, the simplest way to use this would be like so:
 * </p>
 * <pre><code>
 * public class MyClass {
 *     private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
 *     // ...
 * }
 * </code></pre>
 * <p>
 * Note that beside the {@link LoggerFactory#getLogger(Class)}, you can specify a logger name. e.g.:
 * </p>
 * <pre><code>
 * public class MyClass {
 *     private static final Logger namedLogger = LoggerFactory.getLogger("File");
 *     private static final Logger logger = LoggerFactory.getLogger(MySecondClass.class);
 *     // ...
 * }
 * </code></pre>
 *
 * @author Haim Adrian
 * @since 10-May-20
 * @see LoggerFactory
 */
public interface Logger {

    /**
     * Logs a message which is only to be constructed if the logging level is the {@link Level level}.
     * @param level The level to log a message at
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     */
    void log(Level level, Supplier<CharSequence> messageSupplier);

    /**
     * Logs a message (only to be constructed if the logging level is the {@link Level level}) including the
     * stack trace of the {@link Throwable} <code>thrown</code> passed as parameter.
     * @param level The level to log a message at
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     * @param thrown the exception to log, including its stack trace.
     */
    void log(Level level, Supplier<CharSequence> messageSupplier, Throwable thrown);

    /**
     * Logs a message at the {@link Level level}.
     * @param level The level to log a message at
     * @param message the message CharSequence to log.
     */
    void log(Level level, CharSequence message);

    /**
     * Logs a message at the {@link Level level} including the stack trace of the {@link Throwable}
     * <code>thrown</code> passed as parameter.
     * @param level The level to log a message at
     * @param message the message object to log.
     * @param thrown the exception to log, including its stack trace.
     */
    void log(Level level, CharSequence message, Throwable thrown);

    /**
     * Logs a message which is only to be constructed if the logging level is the {@link Level#TRACE TRACE} level.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     */
    default void trace(Supplier<CharSequence> messageSupplier) { log(Level.TRACE, messageSupplier); }

    /**
     * Logs a message (only to be constructed if the logging level is the {@link Level#TRACE TRACE} level) including the
     * stack trace of the {@link Throwable} <code>thrown</code> passed as parameter.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     * @param thrown the exception to log, including its stack trace.
     */
    default void trace(Supplier<CharSequence> messageSupplier, Throwable thrown) { log(Level.TRACE, messageSupplier, thrown); }

    /**
     * Logs a message at the {@link Level#TRACE TRACE} level.
     * @param message the message CharSequence to log.
     */
    default void trace(CharSequence message) { log(Level.TRACE, message); }

    /**
     * Logs a message at the {@link Level#TRACE TRACE} level including the stack trace of the {@link Throwable}
     * <code>thrown</code> passed as parameter.
     * @param message the message object to log.
     * @param thrown the exception to log, including its stack trace.
     */
    default void trace(CharSequence message, Throwable thrown) { log(Level.TRACE, message, thrown); }

    /**
     * Logs a message which is only to be constructed if the logging level is the {@link Level#DEBUG DEBUG} level.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     */
    default void debug(Supplier<CharSequence> messageSupplier) { log(Level.DEBUG, messageSupplier); }

    /**
     * Logs a message (only to be constructed if the logging level is the {@link Level#DEBUG DEBUG} level) including the
     * stack trace of the {@link Throwable} <code>thrown</code> passed as parameter.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     * @param thrown the exception to log, including its stack trace.
     */
    default void debug(Supplier<CharSequence> messageSupplier, Throwable thrown) { log(Level.DEBUG, messageSupplier, thrown); }

    /**
     * Logs a message at the {@link Level#DEBUG DEBUG} level.
     * @param message the message CharSequence to log.
     */
    default void debug(CharSequence message) { log(Level.DEBUG, message); }

    /**
     * Logs a message at the {@link Level#DEBUG DEBUG} level including the stack trace of the {@link Throwable}
     * <code>thrown</code> passed as parameter.
     * @param message the message object to log.
     * @param thrown the exception to log, including its stack trace.
     */
    default void debug(CharSequence message, Throwable thrown) { log(Level.DEBUG, message, thrown); }

    /**
     * Logs a message which is only to be constructed if the logging level is the {@link Level#INFO INFO} level.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     */
    default void info(Supplier<CharSequence> messageSupplier) { log(Level.INFO, messageSupplier); }

    /**
     * Logs a message (only to be constructed if the logging level is the {@link Level#INFO INFO} level) including the
     * stack trace of the {@link Throwable} <code>thrown</code> passed as parameter.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     * @param thrown the exception to log, including its stack trace.
     */
    default void info(Supplier<CharSequence> messageSupplier, Throwable thrown) { log(Level.INFO, messageSupplier, thrown); }

    /**
     * Logs a message at the {@link Level#INFO INFO} level.
     * @param message the message CharSequence to log.
     */
    default void info(CharSequence message) { log(Level.INFO, message); }

    /**
     * Logs a message at the {@link Level#INFO INFO} level including the stack trace of the {@link Throwable}
     * <code>thrown</code> passed as parameter.
     * @param message the message object to log.
     * @param thrown the exception to log, including its stack trace.
     */
    default void info(CharSequence message, Throwable thrown) { log(Level.INFO, message, thrown); }

    /**
     * Logs a message which is only to be constructed if the logging level is the {@link Level#WARN WARN} level.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     */
    default void warn(Supplier<CharSequence> messageSupplier) { log(Level.WARN, messageSupplier); }

    /**
     * Logs a message (only to be constructed if the logging level is the {@link Level#WARN WARN} level) including the
     * stack trace of the {@link Throwable} <code>thrown</code> passed as parameter.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     * @param thrown the exception to log, including its stack trace.
     */
    default void warn(Supplier<CharSequence> messageSupplier, Throwable thrown) { log(Level.WARN, messageSupplier, thrown); }

    /**
     * Logs a message at the {@link Level#WARN WARN} level.
     * @param message the message CharSequence to log.
     */
    default void warn(CharSequence message) { log(Level.WARN, message); }

    /**
     * Logs a message at the {@link Level#WARN WARN} level including the stack trace of the {@link Throwable}
     * <code>thrown</code> passed as parameter.
     * @param message the message object to log.
     * @param thrown the exception to log, including its stack trace.
     */
    default void warn(CharSequence message, Throwable thrown) { log(Level.WARN, message, thrown); }

    /**
     * Logs a message which is only to be constructed if the logging level is the {@link Level#ERROR ERROR} level.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     */
    default void error(Supplier<CharSequence> messageSupplier) { log(Level.ERROR, messageSupplier); }

    /**
     * Logs a message (only to be constructed if the logging level is the {@link Level#ERROR ERROR} level) including the
     * stack trace of the {@link Throwable} <code>thrown</code> passed as parameter.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     * @param thrown the exception to log, including its stack trace.
     */
    default void error(Supplier<CharSequence> messageSupplier, Throwable thrown) { log(Level.ERROR, messageSupplier, thrown); }

    /**
     * Logs a message at the {@link Level#ERROR ERROR} level.
     * @param message the message CharSequence to log.
     */
    default void error(CharSequence message) { log(Level.ERROR, message); }

    /**
     * Logs a message at the {@link Level#ERROR ERROR} level including the stack trace of the {@link Throwable}
     * <code>thrown</code> passed as parameter.
     * @param message the message object to log.
     * @param thrown the exception to log, including its stack trace.
     */
    default void error(CharSequence message, Throwable thrown) { log(Level.ERROR, message, thrown); }

    /**
     * Logs a message which is only to be constructed if the logging level is the {@link Level#FATAL FATAL} level.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     */
    default void fatal(Supplier<CharSequence> messageSupplier) { log(Level.FATAL, messageSupplier); }

    /**
     * Logs a message (only to be constructed if the logging level is the {@link Level#FATAL FATAL} level) including the
     * stack trace of the {@link Throwable} <code>thrown</code> passed as parameter.
     * @param messageSupplier A function, which when called, produces the desired log message; the format depends on the message factory.
     * @param thrown the exception to log, including its stack trace.
     */
    default void fatal(Supplier<CharSequence> messageSupplier, Throwable thrown) { log(Level.FATAL, messageSupplier, thrown); }

    /**
     * Logs a message at the {@link Level#FATAL FATAL} level.
     * @param message the message CharSequence to log.
     */
    default void fatal(CharSequence message) { log(Level.FATAL, message); }

    /**
     * Logs a message at the {@link Level#FATAL FATAL} level including the stack trace of the {@link Throwable}
     * <code>thrown</code> passed as parameter.
     * @param message the message object to log.
     * @param thrown the exception to log, including its stack trace.
     */
    default void fatal(CharSequence message, Throwable thrown) { log(Level.FATAL, message, thrown); }
}

