package org.spa.ui.table.renderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Haim Adrian
 * @since 22-May-20
 */
public class SpinnerCellRenderer extends DefaultTableCellRenderer {
   private final Border originalBorder;
   private final Border focusBorder;

   public SpinnerCellRenderer() {
      setHorizontalAlignment(JLabel.CENTER);
      setVerticalAlignment(JLabel.TOP);

      originalBorder = getBorder();

      // Get the focus border of the LAF we use
      focusBorder = (Border)UIManager.get("List.focusCellHighlightBorder");
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int column) {
      int count;

      try {
         count = Integer.parseInt(value.toString());
      } catch (NumberFormatException e) {
         count = 0;
      }

      JSpinner countSpinner = new JSpinner();
      countSpinner.setOpaque(true);
      JTextField editor = ((JSpinner.DefaultEditor)countSpinner.getEditor()).getTextField();
      editor.setHorizontalAlignment(JTextField.CENTER);
      countSpinner.setValue(Integer.valueOf(count));

      if (isSelected) {
         editor.setForeground(table.getSelectionForeground());
         editor.setBackground(table.getSelectionBackground());
         countSpinner.setForeground(table.getSelectionForeground());
         countSpinner.setBackground(table.getSelectionBackground());
      } else {
         editor.setForeground(table.getForeground());
         editor.setBackground(table.getBackground());
         countSpinner.setForeground(table.getForeground());
         countSpinner.setBackground(table.getBackground());
      }

      if (isFocused) {
         countSpinner.setBorder(focusBorder);
      } else {
         countSpinner.setBorder(originalBorder);
      }

      return countSpinner;
   }
}
