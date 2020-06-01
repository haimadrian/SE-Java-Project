package org.spa.ui.table.editor;

import org.spa.common.SPAApplication;
import org.spa.controller.cart.ShoppingCart;
import org.spa.controller.cart.ShoppingCartException;
import org.spa.ui.util.Controls;
import org.spa.ui.util.Dialogs;
import org.spa.ui.util.Fonts;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author Haim Adrian
 * @since 22-May-20
 */
public class CountCellEditor extends DefaultCellEditor {
   private JSpinner spinner;
   private JTextField editor;
   private Color selectionBackground;

   public CountCellEditor() {
      super(new JTextField());
      setClickCountToStart(1);
   }

   public CountCellEditor(javax.swing.text.Document doc) {
      super(new JTextField(doc, "", 0));
      setClickCountToStart(1);
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      int count = Integer.parseInt(value.toString());

      selectionBackground = table.getSelectionBackground();

      spinner = new JSpinner();
      spinner.setOpaque(true);

      editor = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
      editor.setHorizontalAlignment(JTextField.CENTER);
      editor.setEditable(false);
      editor.setFont(Fonts.PLAIN_FONT);
      editor.addActionListener(actionEvent -> stopCellEditing());

      Controls.increaseComponentWidth(spinner, JButton.class, 1.5);
      spinner.setValue(Integer.valueOf(count));
      spinner.addChangeListener(new CountChangeListener(row));

      spinner.setBackground(selectionBackground);

      return spinner;
   }

   @Override
   public Object getCellEditorValue() {
      return String.valueOf(editor.getText());
   }

   private class CountChangeListener implements ChangeListener {
      private final int selectedRow;
      private boolean isChangingValue;
      private int ignoredValue = -1;

      public CountChangeListener(int selectedRow) {
         this.selectedRow = selectedRow;
      }

      @Override
      public void stateChanged(ChangeEvent e) {
         // Ignore change events that caused by us.
         if (isChangingValue) {
            return;
         }

         int newCount = ((Integer) spinner.getValue()).intValue();
         final String prevValue = editor.getText();

         // There is a bug of swing where it keeps raising the event over and over when
         // we ignore a value. So we just want to exit the event in this case.
         if (ignoredValue == newCount) {
            return;
         }

         // Stop cell editing so we will see the up to date value
         stopCellEditing();

         ShoppingCart shoppingCart = SPAApplication.getInstance().getShoppingCart();
         if (newCount <= 0) {
            if (!Dialogs.showQuestionDialog(null, "Count has been updated to zero.\nThis action will remove item from cart.\nContinue?", "Confirmation")) {
               isChangingValue = true;
               ignoredValue = newCount;

               // This might raise state change event again
               editor.setText(prevValue);
               spinner.setValue(Integer.valueOf(prevValue));

               SwingUtilities.invokeLater(() -> {
                  // Update it after the fire state changed caused by our change
                  isChangingValue = false;
               });

               return;
            }
         }

         try {
            shoppingCart.updateCount(shoppingCart.getIdByRowIndex(selectedRow), Math.max(0, newCount));
         } catch (ShoppingCartException ex) {
            // Reset to previous value
            isChangingValue = true;
            ignoredValue = newCount;

            // This might raise state change event again
            editor.setText(prevValue);
            spinner.setValue(Integer.valueOf(prevValue));

            Dialogs.showErrorDialog(null, "Error has occurred while updating shopping cart item's count:\n" + ex.getMessage(), "Error");
            SwingUtilities.invokeLater(() -> {
               // Update it after the fire state changed caused by our change
               isChangingValue = false;
            });
         }
      }
   }
}
