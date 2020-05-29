package org.spa.ui.table.editor;

import org.spa.ui.util.Controls;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Editor component for strings - uses a text field
 * @author Haim Adrian
 * @since 15-May-20
 */
public class TextCellEditor extends DefaultCellEditor implements FocusListener {
   private JScrollPane scrollPane;
   private JTextArea textArea;
   private final Border originalBorder;
   private final Border focusBorder;

   public TextCellEditor() {
      super(new JTextField());
      initTextArea();
      initScrollPane();
      originalBorder = BorderFactory.createEmptyBorder();

      // Get the focus border of the LAF we use
      focusBorder = (Border)UIManager.get("List.focusCellHighlightBorder");
   }

   public TextCellEditor(javax.swing.text.Document doc) {
      super(new JTextField(doc, "", 0));
      initTextArea();
      initScrollPane();
      originalBorder = BorderFactory.createEmptyBorder();

      // Get the focus border of the LAF we use
      focusBorder = (Border)UIManager.get("List.focusCellHighlightBorder");
   }

   private void initTextArea() {
      textArea = Controls.createTextArea("", true);
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

      if (isSelected) {
         textArea.setForeground(table.getSelectionForeground());
         textArea.setBackground(table.getSelectionBackground());
         textArea.setBorder(focusBorder);
      } else {
         textArea.setBackground(table.getBackground());
         textArea.setForeground(table.getForeground());
         textArea.setBorder(originalBorder);
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
