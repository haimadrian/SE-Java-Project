package org.spa.ui.table.editor;

import org.spa.ui.util.Controls;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Editor component for strings - uses a text field
 *
 * @author Haim Adrian
 * @since 15-May-20
 */
public class TextCellEditor extends DefaultCellEditor implements FocusListener {
   private final Border originalBorder;
   private final Border focusBorder;
   private final boolean isReadOnly;
   private JScrollPane scrollPane;
   private JTextArea textArea;

   public TextCellEditor() {
      this(false);
   }

   public TextCellEditor(boolean isReadOnly) {
      super(new JTextField());
      this.isReadOnly = isReadOnly;

      setClickCountToStart(1);
      initTextArea();
      initScrollPane();

      // Get the focus border of the LAF we use
      focusBorder = (Border) UIManager.get("List.focusCellHighlightBorder");
      originalBorder = BorderFactory.createEmptyBorder();
   }

   private void initTextArea() {
      textArea = Controls.createTextArea("", !isReadOnly);
      textArea.addFocusListener(this);
      editorComponent = textArea;
   }

   private void initScrollPane() {
      scrollPane = Controls.withScrollPane(textArea, 200, Integer.MAX_VALUE);
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      super.getTableCellEditorComponent(table, value, isSelected, row, column);

      textArea.setText(String.valueOf(value));
      textArea.setCaretPosition(0);

      if (isSelected) {
         textArea.setForeground(table.getSelectionForeground());
         textArea.setBackground(table.getSelectionBackground());
         textArea.setBorder(focusBorder);
         scrollPane.setBackground(table.getSelectionBackground());
      } else {
         textArea.setBackground(table.getBackground());
         textArea.setForeground(table.getForeground());
         textArea.setBorder(originalBorder);
         scrollPane.setBackground(table.getBackground());
      }

      return scrollPane;
   }

   @Override
   public Object getCellEditorValue() {
      return textArea.getText();
   }

   @Override
   public void focusGained(FocusEvent e) {
   }

   @Override
   public void focusLost(FocusEvent e) {
      // Stop editing cell when textfield loses focus
      super.stopCellEditing();
   }
}
