package org.spa.view.table.renderer;

import org.spa.view.util.Fonts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A text cell renderer used to wrap text when it is too long to fit in table cell.
 *
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

      // Get the focus border of the LAF we use
      focusBorder = (Border) UIManager.get("List.focusCellHighlightBorder");
      originalBorder = getBorder();
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, isFocused, row, column);

      textArea.setText(getCellText(value));
      textArea.setCaretPosition(0);

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

      return textArea;
   }

   /**
    * Expose this method so derived renderers can customize their values before we print them
    *
    * @param value The value to customize
    * @return The value to present
    */
   protected String getCellText(Object value) {
      return String.valueOf(value);
   }
}
