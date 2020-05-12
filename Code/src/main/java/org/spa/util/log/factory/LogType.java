package org.spa.util.log.factory;

import org.spa.util.log.Logger;

/**
 * Represents the type of loggers we can create at {@link LoggerFactory}
 * @author hadrian
 * @since 10-May-20
 * @see Logger
 */
public enum LogType {
   component, console, log4j2, root
}
