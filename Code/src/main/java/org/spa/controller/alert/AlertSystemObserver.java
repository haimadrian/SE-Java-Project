package org.spa.controller.alert;

import org.spa.model.Alert;

/**
 * Whoever wants to listen to the {@link AlertSystem} should implement this interface and register itself as
 * observer of the alert system class.
 *
 * @author Haim Adrian
 * @since 16-May-20
 */
public interface AlertSystemObserver {
   /**
    * When alert system finds that an alert should be raised, it will call this method specifying the alert
    *
    * @param alertSystem A reference to {@link AlertSystem}
    * @param alert The alert
    */
   void onAlertTriggered(AlertSystem alertSystem, Alert alert);

   /**
    * When acknowledging an alert or clearing alert system, this method is called
    *
    * @param alertSystem A reference to {@link AlertSystem}
    * @param alert The acknowledged alert
    */
   void onAlertAcknowledged(AlertSystem alertSystem, Alert alert);
}
