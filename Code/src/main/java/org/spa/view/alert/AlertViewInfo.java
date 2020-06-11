package org.spa.view.alert;

import org.spa.view.table.TableModelIfc;

import java.util.Date;

/**
 * @author Haim Adrian
 * @since 15-May-20
 */
public class AlertViewInfo implements TableModelIfc {
   private final String key;
   private final String message;
   private final long date;
   private final SeverityViewInfo severity;

   public AlertViewInfo(String key, String message, long date, String severity) {
      this.key = key;
      this.message = message;
      this.date = date;
      this.severity = SeverityViewInfo.valueOf(severity);
   }

   @Override
   public Object getAttributeValue(String attributeName) {
      switch (AlertColumn.valueOf(attributeName)) {
         case Message: {
            return message;
         }
         case Date: {
            return new Date(date);
         }
         case Severity: {
            return severity.getIcon();
         }
         default:
            return null;
      }
   }

   @Override
   public void setAttributeValue(String attributeName, Object value) {
      // Not editable.
   }

   public String getKey() {
      return key;
   }

   public String getMessage() {
      return message;
   }

   public long getDate() {
      return date;
   }

   public SeverityViewInfo getSeverity() {
      return severity;
   }

   @Override
   public String toString() {
      return "AlertViewInfo{" +
            "message='" + message + '\'' +
            ", date=" + date +
            ", severity=" + severity +
            '}';
   }
}
