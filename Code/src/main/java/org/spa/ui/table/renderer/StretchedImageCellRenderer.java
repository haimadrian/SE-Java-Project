package org.spa.ui.table.renderer;

import org.spa.ui.ImageViewer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * A cell renderer to use for stretching images over the entire cell content
 *
 * @author hadrian
 * @since 16-May-20
 */
public class StretchedImageCellRenderer extends DefaultTableCellRenderer {
   private final int margin;
   private final Border originalBorder;
   private final Border focusBorder;

   public StretchedImageCellRenderer() {
      this(0);
   }

   public StretchedImageCellRenderer(int margin) {
      this.margin = margin;
      originalBorder = getBorder();

      // Get the focus border of the LAF we use
      focusBorder = (Border)UIManager.get("List.focusCellHighlightBorder");
   }

   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int column) {
      if (value == null) {
         return this;
      }

      Image image = ((ImageIcon)value).getImage();
      if (image == null) {
         return this;
      }

      ImageViewer imageViewer = new ImageViewer(image, margin);

      if (isSelected) {
         imageViewer.setForeground(table.getSelectionForeground());
         imageViewer.setBackground(table.getSelectionBackground());
      } else {
         imageViewer.setForeground(getForeground());
         imageViewer.setBackground(getBackground());
      }

      if (isFocused) {
         imageViewer.setBorder(focusBorder);
      } else {
         imageViewer.setBorder(originalBorder);
      }

      return imageViewer;
   }
}
