package org.spa.ui.table.renderer;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * @author hadrian
 * @since 16-May-20
 */
public class DateCellRenderer extends TextCellRenderer {
   private final SimpleDateFormat formatter;

   public DateCellRenderer() {
      this(" HH:mm\ndd-MMM-yy");
   }

   public DateCellRenderer(String format) {
      formatter = new SimpleDateFormat(format);
      setHorizontalAlignment(JLabel.CENTER);
   }

   @Override
   protected String getCellText(Object value) {
      StringBuilder html = new StringBuilder("<html>");
      if (value instanceof Date) {
         html.append(formatter.format((Date)value).replace("\n", "<br>").replace(" ", "&nbsp;&nbsp;&nbsp;&nbsp;")).append("</html>");
         setText(html.toString());
         return null;
      } else if (value instanceof TemporalAccessor) {
         return formatter.format((TemporalAccessor)value);
      }

      return super.getCellText(value);
   }
}
