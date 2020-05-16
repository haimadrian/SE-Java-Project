package org.spa.ui.table.renderer;

import javax.swing.*;

/**
 * @author hadrian
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

      return text;
   }

   @Override
   protected String getAdditionalDivStyle() {
      return "text-align: center;vertical-align: middle;";
   }
}