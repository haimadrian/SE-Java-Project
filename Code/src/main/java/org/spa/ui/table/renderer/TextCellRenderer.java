package org.spa.ui.table.renderer;

import org.apache.commons.text.StringEscapeUtils;
import org.spa.common.util.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A text cell renderer used to wrap text when it is too long to fit in table cell.
 * @author hadrian
 * @since 16-May-20
 */
public class TextCellRenderer extends DefaultTableCellRenderer {
   public TextCellRenderer() {
      setHorizontalAlignment(JLabel.LEFT);
      setVerticalAlignment(JLabel.TOP);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

      int colWidth = table.getColumn(Integer.valueOf(column)).getWidth();
      String cellText = getCellText(value);

      // Use HTML hack so we can take the advantage of <div> for word wrapping a label text
      // @formatter:off
      String html = "<html><div style=\"width:" + colWidth + ";" + getAdditionalDivStyle() + "\">" +
            StringUtils.replaceWildcardWithHTMLStyle(StringEscapeUtils.escapeHtml4(StringUtils.replaceHTMLStyleWithWildcard(cellText))).replace("\n", "<br/>") +
            "</div></html>";
      // @formatter:on
      setText(html);

      return this;
   }

   /**
    * Override to return custom text format
    * @param value Current cell's value
    * @return The value you want to display
    */
   protected String getCellText(Object value) {
      return String.valueOf(value);
   }

   /**
    * @return Additional properties for div style, to be used when making the label an HTML one
    */
   protected String getAdditionalDivStyle() {
      return "";
   }


}
