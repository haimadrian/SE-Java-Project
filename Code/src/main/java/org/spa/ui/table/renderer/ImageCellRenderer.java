package org.spa.ui.table.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author hadrian
 * @since 16-May-20
 */
public class ImageCellRenderer extends DefaultTableCellRenderer {
   public ImageCellRenderer() {
      setHorizontalAlignment(JLabel.CENTER);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

      if (value instanceof ImageIcon) {
         setText("");
         setIcon((ImageIcon)value);
      } else {
         setText(value.toString());
      }

      return this;
   }
}
