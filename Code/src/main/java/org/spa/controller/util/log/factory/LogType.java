package org.spa.controller.util.log.factory;

import org.spa.controller.util.log.Logger;

/**
 * Represents the type of loggers we can create at {@link LoggerFactory}
 *
 * @author Haim Adrian
 * @see Logger
 * @since 10-May-20
 */
public enum LogType {
   component, console, log4j2, root
}
