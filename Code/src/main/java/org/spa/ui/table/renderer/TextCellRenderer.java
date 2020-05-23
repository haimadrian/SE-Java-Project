package org.spa.ui.table.renderer;

import org.spa.ui.util.Fonts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A text cell renderer used to wrap text when it is too long to fit in table cell.
 * @author Haim Adrian
 * @since 16-May-20
 */
public class TextCellRenderer extends DefaultTableCellRenderer {
   private final JTextArea textArea;
   private final Border originalBorder;
   private final Border focusBorder;

   public TextCellRenderer() {
      setHorizontalAlignment(JLabel.LEFT);
      setVerticalAlignment(JLabel.TOP);
      textArea = new JTextArea();
      textArea.setWrapStyleWord(true);
      textArea.setLineWrap(true);
      textArea.setFont(Fonts.PLAIN_FONT);
      originalBorder = getBorder();

      // Get the focus border of the LAF we use
      focusBorder = (Border)UIManager.get("List.focusCellHighlightBorder");
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, isFocused, row, column);

      textArea.setText(String.valueOf(value));
      textArea.setBackground(table.getBackground());
      textArea.setForeground(table.getForeground());

      if (isSelected) {
         textArea.setForeground(table.getSelectionForeground());
         textArea.setBackground(table.getSelectionBackground());
      } else {
         textArea.setBackground(table.getBackground());
         textArea.setForeground(table.getForeground());
      }

      if (isFocused) {
         textArea.setBorder(focusBorder);
      } else {
         textArea.setBorder(originalBorder);
      }

      /*int colWidth = table.getColumn(Integer.valueOf(column)).getWidth();
      String cellText = getCellText(value);

      // Use HTML hack so we can take the advantage of <div> for word wrapping a label text
      // @formatter:off
      String html = "<html><div style=\"width:" + colWidth + ";" + getAdditionalDivStyle() + "\">" +
            StringUtils.replaceWildcardWithHTMLStyle(StringEscapeUtils.escapeHtml4(StringUtils.replaceHTMLStyleWithWildcard(cellText))).replace("\n", "<br/>") +
            "</div></html>";
      // @formatter:on
      setText(html);*/

      return textArea;
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
