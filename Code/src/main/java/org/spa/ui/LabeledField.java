package org.spa.ui;

import org.spa.ui.util.Controls;

import javax.swing.*;

/**
 * A component that contains a text and some other component next to it.
 * Useful for when there is a need to a text box for example with a label at its left.
 * @author hadrian
 * @since 12-May-20
 */
public class LabeledField extends JPanel {
   private final JLabel label;
   private final JComponent field;

   /**
    * Constructs a new {@link LabeledField}
    * @param label The string to set as the label of this labeled field
    * @param field The field which is labeled
    * @see #LabeledField(String, int, JComponent) LabeledField(String label, int labelWidth, JComponent field) for defining custom label width
    */
   public LabeledField(String label, JComponent field) {
      this(label, -1, field);
   }

   /**
    * Constructs a new {@link LabeledField}
    * @param label The string to set as the label of this labeled field
    * @param labelWidth Custom width for the label. -1 for default width
    * @param field The field which is labeled
    */
   public LabeledField(String label, int labelWidth, JComponent field) {
      this.label = Controls.createLabel(label);
      this.field = field;

      setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
      add(Box.createRigidArea(Controls.DIM_5));

      if (labelWidth > 0) {
         Controls.setComponentSize(this.label, labelWidth, 25);
      }

      add(this.label);
      add(Box.createRigidArea(Controls.DIM_5));
      Controls.setComponentSize(field, 100, 25, 200, 25, Integer.MAX_VALUE, 25);
      add(field);
      add(Box.createRigidArea(Controls.DIM_5));
   }

   /**
    * @return A reference to the label of this labeled field, for customizations
    */
   public JLabel getLabel() {
      return label;
   }

   /**
    * @return A reference to the field of this labeled field, for customizations
    */
   public JComponent getField() {
      return field;
   }
}
