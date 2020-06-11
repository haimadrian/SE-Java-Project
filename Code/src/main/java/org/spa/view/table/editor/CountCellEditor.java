package org.spa.view.table.editor;

import org.spa.controller.SPAApplication;
import org.spa.controller.cart.ShoppingCart;
import org.spa.controller.cart.ShoppingCartException;
import org.spa.view.util.Controls;
import org.spa.view.util.Dialogs;
import org.spa.view.util.Fonts;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @author Haim Adrian
 * @since 22-May-20
 */
public class CountCellEditor extends DefaultCellEditor {
   private final Border originalBorder;
   private final Border focusBorder;
   private JSpinner spinner;
   private JTextField editor;

   public CountCellEditor() {
      super(new JTextField());
      setClickCountToStart(1);

      // Get the focus border of the LAF we use
      focusBorder = (Border) UIManager.get("List.focusCellHighlightBorder");
      originalBorder = BorderFactory.createEmptyBorder();
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      int count = Integer.parseInt(value.toString());

      spinner = new JSpinner();
      spinner.setOpaque(true);

      editor = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
      editor.setBorder(BorderFactory.createEmptyBorder());
      editor.setHorizontalAlignment(JTextField.CENTER);
      editor.setEditable(false);
      editor.setFont(Fonts.PLAIN_FONT);
      editor.addActionListener(actionEvent -> stopCellEditing());

      Controls.increaseComponentWidth(spinner, JButton.class, 1.5);
      spinner.setValue(Integer.valueOf(count));
      spinner.addChangeListener(new CountChangeListener(row));

      if (isSelected) {
         spinner.setForeground(table.getSelectionForeground());
         spinner.setBackground(table.getSelectionBackground());
         spinner.setBorder(focusBorder);
         editor.setBackground(table.getSelectionBackground());
      } else {
         spinner.setForeground(table.getForeground());
         spinner.setBackground(table.getBackground());
         spinner.setBorder(originalBorder);
         editor.setBackground(table.getBackground());
      }

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
         shoppingCart.setIsEditing(true);
         try {
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
         } finally {
            shoppingCart.setIsEditing(false);
         }
      }
   }
}
