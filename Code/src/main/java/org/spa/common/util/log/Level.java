package org.spa.common.util.log;

/**
 * @author hadrian
 * @since 10-May-20
 */
public enum Level {
    //@formatter:off
    TRACE(org.apache.logging.log4j.Level.TRACE),
    DEBUG(org.apache.logging.log4j.Level.DEBUG),
    INFO(org.apache.logging.log4j.Level.INFO),
    WARN(org.apache.logging.log4j.Level.WARN),
    ERROR(org.apache.logging.log4j.Level.ERROR),
    FATAL(org.apache.logging.log4j.Level.FATAL);
    //@formatter:on

    Level(org.apache.logging.log4j.Level underlyingLevel) {
        this.underlyingLevel = underlyingLevel;
    }

    private final org.apache.logging.log4j.Level underlyingLevel;

    /**
     * @return The level according to the adapted API.
     */
    public org.apache.logging.log4j.Level getUnderlyingLevel() {
        return underlyingLevel;
    }
}

