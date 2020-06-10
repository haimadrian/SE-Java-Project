package org.spa.ui.table.renderer;

import org.spa.ui.util.Controls;
import org.spa.ui.util.Fonts;

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

      // Get the focus border of the LAF we use
      focusBorder = (Border)UIManager.get("List.focusCellHighlightBorder");
      originalBorder = BorderFactory.createEmptyBorder();
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
      boolean isOpaque = row % 2 == 1 || isSelected || isFocused;
      spinner.setOpaque(isOpaque);
      JTextField editor = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
      editor.setHorizontalAlignment(JTextField.CENTER);
      editor.setFont(Fonts.PLAIN_FONT);
      editor.setBorder(BorderFactory.createEmptyBorder());
      spinner.setValue(Integer.valueOf(count));
      Controls.increaseComponentWidth(spinner, JButton.class, 1.5);

      if (isSelected) {
         spinner.setForeground(table.getSelectionForeground());
         spinner.setBackground(table.getSelectionBackground());
         editor.setBackground(table.getSelectionBackground());
      } else {
         spinner.setForeground(table.getForeground());
         spinner.setBackground(table.getBackground());
         editor.setBackground(table.getBackground());
      }

      if (isFocused) {
         spinner.setBorder(focusBorder);
      } else {
         spinner.setBorder(originalBorder);
      }

      return spinner;
   }
}
