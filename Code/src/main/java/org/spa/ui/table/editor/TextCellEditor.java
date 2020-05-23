package org.spa.ui.table.editor;

import org.spa.ui.util.Fonts;

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
      textArea = new JTextArea();
      textArea.setWrapStyleWord(true);
      textArea.setLineWrap(true);
      textArea.setFont(Fonts.PLAIN_FONT);
      textArea.addFocusListener(this);
      editorComponent = textArea;
   }

   private void initScrollPane() {
      scrollPane = new JScrollPane(textArea);
      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      scrollPane.setMinimumSize(new Dimension(100, Integer.MAX_VALUE));
      scrollPane.setPreferredSize(new Dimension(20, Integer.MAX_VALUE));
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      super.getTableCellEditorComponent(table, value, isSelected, row, column);

      scrollPane.getViewport().setBackground(table.getBackground());
      scrollPane.getViewport().setForeground(table.getForeground());

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
