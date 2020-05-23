package org.spa.ui.table.editor;

import org.spa.common.SPAApplication;
import org.spa.controller.cart.ShoppingCart;
import org.spa.controller.cart.ShoppingCartException;
import org.spa.ui.util.Dialogs;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * @author Haim Adrian
 * @since 22-May-20
 */
public class CountCellEditor extends DefaultCellEditor {
   private JSpinner spinner;
   private JTextField editor;
   private final Border focusBorder;
   private Color selectionForeground;
   private Color selectionBackground;

   public CountCellEditor() {
      super(new JTextField());

      // Get the focus border of the LAF we use
      focusBorder = (Border)UIManager.get("List.focusCellHighlightBorder");
      setClickCountToStart(1);
   }

   public CountCellEditor(javax.swing.text.Document doc) {
      super(new JTextField(doc, "", 0));

      // Get the focus border of the LAF we use
      focusBorder = (Border)UIManager.get("List.focusCellHighlightBorder");
      setClickCountToStart(1);
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      int count = Integer.parseInt(value.toString());

      selectionForeground = table.getSelectionForeground();
      selectionBackground = table.getSelectionBackground();

      spinner = new JSpinner();
      spinner.setOpaque(true);

      editor = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
      editor.setHorizontalAlignment(JTextField.CENTER);
      editor.setEditable(false);
      editor.addActionListener(actionEvent -> stopCellEditing());

      spinner.setValue(Integer.valueOf(count));
      spinner.setValue(Integer.valueOf(count));
      spinner.addChangeListener(e -> {
         ShoppingCart shoppingCart = SPAApplication.getInstance().getShoppingCart();
         int newCount = ((Integer) spinner.getValue()).intValue();
         if (newCount == 0) {
            if (!Dialogs.showQuestionDialog(null, "Count has been updated to zero.\nThis action will remove item from cart.\nContinue?", "Confirmation")) {
               spinner.setValue(Integer.valueOf(1));
               return;
            }
         }

         // Stop cell editing so we will see the up to date value
         stopCellEditing();

         // Do it after the event.
         final Integer prevValue = Integer.valueOf(editor.getText());
         SwingUtilities.invokeLater(() -> {
            try {
               shoppingCart.updateCount(shoppingCart.getIdByRowIndex(row), newCount);
            } catch (ShoppingCartException ex) {
               // Reset to previous value
               spinner.setValue(prevValue);
               Dialogs.showSimpleErrorDialog(null, "Error has occurred while updating shopping cart item's count:\n" + ex.getMessage(), "Error");
            }
         });
      });

      editor.setBorder(focusBorder);
      spinner.setBorder(focusBorder);
      spinner.setForeground(selectionForeground);
      spinner.setBackground(selectionBackground);
      editor.setForeground(selectionForeground);
      editor.setBackground(selectionBackground);

      return spinner;
   }

   @Override
   public Object getCellEditorValue() {
      return editor.getText();
   }
}
