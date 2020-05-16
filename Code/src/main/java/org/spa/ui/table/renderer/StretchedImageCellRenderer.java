package org.spa.ui.table.renderer;

import org.spa.ui.ImageViewer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A cell renderer to use for stretching images over the entire cell content
 *
 * @author hadrian
 * @since 16-May-20
 */
public class StretchedImageCellRenderer extends DefaultTableCellRenderer {
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int column) {
      Image image = ((ImageIcon)value).getImage();
      ImageViewer imageViewer = new ImageViewer(image);

      if (table.isRowSelected(row)) {
         imageViewer.setForeground(table.getSelectionForeground());
         imageViewer.setBackground(table.getSelectionBackground());
      } else {
         imageViewer.setForeground(getForeground());
         imageViewer.setBackground(getBackground());
      }

      return imageViewer;
   }
}
