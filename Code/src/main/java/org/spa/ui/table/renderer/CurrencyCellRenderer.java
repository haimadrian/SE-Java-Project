package org.spa.ui.table.renderer;

import javax.swing.*;

/**
 * @author Haim Adrian
 * @since 16-May-20
 */
public class CurrencyCellRenderer extends TextCellRenderer {
   public CurrencyCellRenderer() {
      setHorizontalAlignment(JLabel.CENTER);
   }

   @Override
   protected String getCellText(Object value) {
      String text = super.getCellText(value);
      if (!text.contains("$")) {
         text += "$";
      }

      return "<b>" + text + "</b>";
   }

   @Override
   protected String getAdditionalDivStyle() {
      return "text-align: center;vertical-align: middle;";
   }
}
