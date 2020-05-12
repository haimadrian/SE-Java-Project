package org.spa.util.log.factory;

import org.spa.util.log.ComponentLogHandler;
import org.spa.util.log.Logger;

/**
 * Used for constructing concrete log adapters according to the specified parameters.
 * @author hadrian
 * @since 10-May-20
 * @see Logger
 * @see #getLogger(LogType, Object...)
 */
public class LoggerFactory {

   /**
    * Factory method used to create a specific log handler based on the specified type.
    * <p>
    * To get a <b>log4j2</b> logger, you can simply use:
    * </p>
    * <pre><code>
    * public class MyClass {
    *     private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
    *     private static final Logger anotherLogger = LoggerFactory.getLogger(LogType.log4j2, MyClass.class);
    *     // ...
    * }
    * </code></pre>
    * <p>
    * To get the <b>root</b> log4j2 logger, you can use:
    * </p>
    * <pre><code>
    * public class MyClass {
    *     private static final Logger logger = LoggerFactory.getLogger(LogType.root);
    *     // ...
    * }
    * </code></pre>
    * <p>
    * To get a <b>console</b> logger, you can use:
    * </p>
    * <pre><code>
    * public class MyClass {
    *     private static final Logger logger = LoggerFactory.getLogger(LogType.console);
    *     // ...
    * }
    * </code></pre>
    * <p>
    * To get a <b>component</b> logger, which can be used as a log handler to log messages to a UI component, you can use:
    * </p>
    * <pre><code>
    * public class MyClass {
    *     // The handler that will get the log events
    *     private final ComponentLogHandler handler = new MyComponentHandler();
    *     private final Logger logger = LoggerFactory.getLogger(LogType.component, handler);
    *     // ...
    * }
    * </code></pre>
    * @param type The type to create a logger for
    * @param params Additional parameters if required. See the documentation for examples
    * @return A {@link Logger}
    * @see LogType
    */
   public static Logger getLogger(LogType type, Object ... params) {
      Logger logger;

      switch (type) {
         case log4j2:
            if (params.length == 0) {
               logger = new Log4j2Adapter();
            } else if (params[0] instanceof Class) {
               logger = new Log4j2Adapter((Class<?>)params[0]);
            } else {
               logger = new Log4j2Adapter(String.valueOf(params[0]));
            }
            break;
         case console:
            logger = new ConsoleAdapter();
            break;
         case component:
            logger = new ComponentAdapter((ComponentLogHandler)params[0]);
            break;
         default:
            logger = new Log4j2Adapter();
      }

      return logger;
   }

   /**
    * Retrieve a new {@link LogType#log4j2 log4j2} logger for class
    * @param forClass The class to get logger for
    * @return Log4j2 adapter for {@link Logger}
    */
   public static Logger getLogger(Class<?> forClass) {
      return new Log4j2Adapter(forClass);
   }

   /**
    * Retrieve a new {@link LogType#log4j2 log4j2} logger for a name
    * @param forName The name to get logger for
    * @return Log4j2 adapter for {@link Logger}
    */
   public static Logger getLogger(String forName) {
      return new Log4j2Adapter(forName);
   }
}
