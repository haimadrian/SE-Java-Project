package org.spa.ui.table.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A text cell renderer used to wrap text when it is too long to fit in table cell.
 * @author hadrian
 * @since 16-May-20
 */
public class TextCellRenderer extends DefaultTableCellRenderer {
   private static final int MAX_ROWS = 4;

   public TextCellRenderer() {
      setHorizontalAlignment(JLabel.LEFT);
      setVerticalAlignment(JLabel.TOP);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

      int colWidth = table.getColumn(Integer.valueOf(column)).getWidth() - 150;
      String cellText = getCellText(value);

      // Derived classes can return null when they handle it by themselves
      if (cellText != null) {
         int stringWidth = getFontMetrics(getFont()).stringWidth(cellText);
         if (stringWidth > colWidth) {
            int totalRows = (int) Math.floor((double) stringWidth / colWidth + 0.5);
            int rowLength = cellText.length() / totalRows - 2;

            StringBuilder html = new StringBuilder("<html><div style=\"width:").append(colWidth).append("px;\">").append(cellText);
            /*int spaceIndex = cellText.indexOf(" ");
            int fromIndex = 0;
            int splitIndex = rowLength;

            // Split the text to rows.
            int rows = 0;
            while (spaceIndex >= 0 && rows < MAX_ROWS) {
               if (spaceIndex > splitIndex) {
                  html.append(cellText.substring(fromIndex, spaceIndex).replace("<", "&lt;").replace(">", "&gt;"));
                  html.append("<br/>");
                  fromIndex = spaceIndex + 1;
                  splitIndex += (rowLength - (spaceIndex - splitIndex));
                  ++rows;
               }
               spaceIndex = cellText.indexOf(" ", spaceIndex + 1);
            }

            if (spaceIndex >= 0 && rows == MAX_ROWS) {
               html.append("...<br/>");
            } else if (fromIndex < cellText.length()) {
               html.append(cellText.substring(fromIndex).replace("<", "&lt;").replace(">", "&gt;"));
            }*/

            html.append("</div></html>");
            setText(html.toString());
         } else {
            setText(cellText);
         }
      }

      return this;
   }

   protected String getCellText(Object value) {
      return String.valueOf(value);
   }
}
