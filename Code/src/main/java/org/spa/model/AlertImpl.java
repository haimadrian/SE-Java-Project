package org.spa.model;

import org.spa.controller.alert.Alert;
import org.spa.controller.alert.Severity;

/**
 * @author Haim Adrian
 * @since 15-May-20
 */
public class AlertImpl implements Alert {
   private final String key;
   private String message;
   private long date;
   private Severity severity;

   public AlertImpl(String key, String message, long date, Severity severity) {
      this.key = key;
      this.message = message;
      this.date = date;
      this.severity = severity;
   }

   @Override
   public String getKey() {
      return key;
   }

   @Override
   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   @Override
   public long getDate() {
      return date;
   }

   public void setDate(long date) {
      this.date = date;
   }

   @Override
   public Severity getSeverity() {
      return severity;
   }

   public void setSeverity(Severity severity) {
      this.severity = severity;
   }

   @Override
   public String toString() {
      return "Alert{" +
            "key='" + key + '\'' +
            ", message='" + message + '\'' +
            ", date=" + date +
            ", severity=" + severity +
            '}';
   }
}
