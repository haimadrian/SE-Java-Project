package org.spa.controller.alert;

/**
 * An interface created to represent alert in the application
 *
 * @author Haim Adrian
 * @since 11-Jun-20
 */
public interface Alert {
   /**
    * The key of the alert - This is the item ID which we raise an alert about its stock
    * @return The key of this alert
    */
   String getKey();

   /**
    * @return The alert message to display to the user
    */
   String getMessage();

   /**
    * @return When the alert raised
    */
   long getDate();

   /**
    * @return The {@link Severity} of the alert
    */
   Severity getSeverity();
}
