package org.spa.ui.table.renderer;

import org.spa.ui.util.Controls;
import org.spa.ui.util.Fonts;

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

      JSpinner spinner = new JSpinner();
      // Set transparency if it is an odd row cause Nimbus L&F uses a different background color
      // for such rows. In addition, check if it is selected or focused cause it got a different background
      spinner.setOpaque(row % 2 == 1 || isSelected || isFocused);
      JTextField editor = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
      editor.setHorizontalAlignment(JTextField.CENTER);
      editor.setFont(Fonts.PLAIN_FONT);
      spinner.setValue(Integer.valueOf(count));
      Controls.doubleComponentWidth(spinner, JButton.class);

      if (isSelected) {
         spinner.setForeground(table.getSelectionForeground());
         spinner.setBackground(table.getSelectionBackground());
      }

      return spinner;
   }
}
