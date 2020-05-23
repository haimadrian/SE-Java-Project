package org.spa.ui.alert;

import org.spa.ui.table.TableModelIfc;

import java.util.Date;

/**
 * @author Haim Adrian
 * @since 15-May-20
 */
public class AlertViewInfo implements TableModelIfc {
   private final String message;
   private final long date;
   private final SeverityViewInfo severity;

   public AlertViewInfo(String message, long date, String severity) {
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

   @Override
   public String toString() {
      return "AlertViewInfo{" +
            "message='" + message + '\'' +
            ", date=" + date +
            ", severity=" + severity +
            '}';
   }
}
