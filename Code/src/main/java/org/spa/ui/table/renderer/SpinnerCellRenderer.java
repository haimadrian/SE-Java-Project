package org.spa.ui.table.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author Haim Adrian
 * @since 22-May-20
 */
public class SpinnerCellRenderer extends DefaultTableCellRenderer {
   public SpinnerCellRenderer() {
      setHorizontalAlignment(JLabel.CENTER);
      setVerticalAlignment(JLabel.TOP);
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
      // Set transparency if it is an odd row cause Nimbus L&F uses a different background color
      // for such rows. In addition, check if it is selected or focused cause it got a different background
      countSpinner.setOpaque(row % 2 == 1 || isSelected || isFocused);
      JTextField editor = ((JSpinner.DefaultEditor)countSpinner.getEditor()).getTextField();
      editor.setHorizontalAlignment(JTextField.CENTER);
      countSpinner.setValue(Integer.valueOf(count));

      if (isSelected) {
         countSpinner.setForeground(table.getSelectionForeground());
         countSpinner.setBackground(table.getSelectionBackground());
      }

      return countSpinner;
   }
}
