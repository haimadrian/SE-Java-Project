package org.spa.ui.table.editor;

import org.spa.ui.table.TableCellValue;
import org.spa.ui.util.Controls;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.function.Consumer;

/**
 * The ButtonColumn class provides a renderer and an editor that looks like a
 * JButton. The renderer and editor will then be used for a specified column
 * in the table. The TableModel will contain the String to be displayed on
 * the button.
 * <p>
 * The button can be invoked by a mouse click or by pressing the space bar
 * when the cell has focus. Optionally a mnemonic can be set to invoke the
 * button. When the button is invoked the provided Action is invoked. The
 * source of the Action will be the table. The action command will contain
 * the model row number of the button that was clicked.
 *
 * @author Lior Shor
 */

public class ButtonColumn extends DefaultCellEditor implements TableCellRenderer, TableCellEditor {
   private int mnemonic;
   private Border originalBorder;
   private Border focusBorder;
   private JButton renderButton;
   private JButton editButton;
   private Object editorValue;
   private JTable table;

   /**
    * Create the ButtonColumn to be used as a renderer and editor. The
    * renderer and editor will automatically be installed on the TableColumn
    * of the specified column.
    */
   public ButtonColumn(Consumer<JTable> listener) {
      super(new JTextField(""));
      renderButton = new JButton();
      Controls.setFlatStyle(renderButton);
      editButton = new JButton();
      Controls.setFlatStyle(editButton);
      editButton.addActionListener(actionEvent -> {
         this.stopCellEditing();
         listener.accept(table);
      });

      // Get the focus border of the LAF we use
      focusBorder = (Border) UIManager.get("List.focusCellHighlightBorder");
      originalBorder = new DefaultTableCellRenderer().getBorder();

      setClickCountToStart(1);
   }


   /**
    * Get foreground color of the button when the cell has focus
    *
    * @return the foreground color
    */
   public Border getFocusBorder() {
      return focusBorder;
   }

   /**
    * The foreground color of the button when the cell has focus
    *
    * @param focusBorder the foreground color
    */
   public void setFocusBorder(Border focusBorder) {
      this.focusBorder = focusBorder;
      editButton.setBorder(focusBorder);
   }

   public int getMnemonic() {
      return mnemonic;
   }

   /**
    * The mnemonic to activate the button when the cell has focus
    *
    * @param mnemonic the mnemonic
    */
   public void setMnemonic(int mnemonic) {
      this.mnemonic = mnemonic;
      renderButton.setMnemonic(mnemonic);
      editButton.setMnemonic(mnemonic);
   }

   @Override
   public Component getTableCellEditorComponent(
         JTable table, Object value, boolean isSelected, int row, int column) {
      this.table = table;

      handleValueForButton(value, editButton);

      editButton.setOpaque(true);

      if (isSelected) {
         editButton.setForeground(table.getSelectionForeground());
         editButton.setBackground(table.getSelectionBackground());
         editButton.setBorder(focusBorder);
      } else {
         editButton.setBackground(table.getBackground());
         editButton.setForeground(table.getForeground());
         editButton.setBorder(originalBorder);
      }

      this.editorValue = value;
      return editButton;
   }

   @Override
   public Object getCellEditorValue() {
      if (editorValue == null) {
         return "";
      } else if (editorValue instanceof Icon) {
         return editorValue;
      } else if (editorValue instanceof TableCellValue) {
         return ((TableCellValue<?>) editorValue).getValue();
      } else {
         return editorValue;
      }
   }

   //
//  Implement TableCellRenderer interface
//
   public Component getTableCellRendererComponent(
         JTable table, Object value, boolean isSelected, boolean isFocused, int row, int column) {
      this.table = table;

      renderButton.setOpaque(row % 2 == 1 || isSelected || isFocused);

      if (isSelected) {
         renderButton.setForeground(table.getSelectionForeground());
         renderButton.setBackground(table.getSelectionBackground());
      } else {
         renderButton.setBackground(table.getBackground());
         renderButton.setForeground(table.getForeground());
      }

      if (isFocused) {
         renderButton.setBorder(focusBorder);
      } else {
         renderButton.setBorder(originalBorder);
      }

      handleValueForButton(value, renderButton);

      return renderButton;
   }

   private void handleValueForButton(Object value, JButton renderButton) {
      if (value == null) {
         renderButton.setText("");
         renderButton.setIcon(null);
      } else if (value instanceof Icon) {
         renderButton.setText("");
         renderButton.setIcon((Icon) value);
      } else if (value instanceof TableCellValue) {
         renderButton.setText("");
         renderButton.setIcon((Icon) ((TableCellValue<?>) value).getValue());
      } else {
         renderButton.setText("");
         renderButton.setIcon(null);
      }
   }

}
