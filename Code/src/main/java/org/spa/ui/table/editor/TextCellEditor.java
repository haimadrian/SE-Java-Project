package org.spa.ui.table.editor;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Editor component for strings - uses a text field
 * @author hadrian
 * @since 15-May-20
 */
public class TextCellEditor extends DefaultCellEditor implements FocusListener {
   public TextCellEditor() {
      super(new JTextField());
      editorComponent.addFocusListener(this);
   }

   public TextCellEditor(javax.swing.text.Document doc) {
      super(new JTextField(doc, "", 0));
      editorComponent.addFocusListener(this);
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
