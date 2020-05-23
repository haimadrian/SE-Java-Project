package org.spa.controller.alert;

import java.util.Date;

/**
 * Whoever wants to listen to the {@link AlertSystem} should implement this interface and register itself as
 * observer of the alert system class.
 * @author Haim Adrian
 * @since 16-May-20
 */
public interface AlertSystemObserver {
   /**
    * When alert system finds that an alert should be raised, it will call this method specifying alert data as fields
    * so there is no dependency on data layer.
    * @param key Key represents the alert, to support updating existing alert instead of showing a duplicate of it
    * @param message The up to date message
    * @param severity The severity this alert got. One of: NORMAL, LOW, MEDIUM, HIGH
    * @param date When this alert has been generated
    */
   void onAlertTriggered(String key, String message, String severity, Date date);
}
