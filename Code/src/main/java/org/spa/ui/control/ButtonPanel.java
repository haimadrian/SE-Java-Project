package org.spa.ui.control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A horizontal button panel where the buttons are centered and separated by <code>gapSize</code> pixels
 * @author Haim Adrian
 * @since 12-May-20
 */
public class ButtonPanel extends JPanel {
   private final List<JButton> buttons;

   public ButtonPanel(int gapSize, JButton... buttons) {
      this(null, gapSize, buttons);
   }

   public ButtonPanel(ActionListener listener, int gapSize, JButton... buttons) {
      setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
      Dimension gap = new Dimension(gapSize, gapSize);
      add(Box.createGlue());
      add(Box.createRigidArea(gap));

      this.buttons = new ArrayList<>(buttons.length);
      for (int i = 0; i < buttons.length; ++i) {
         if (i > 0) {
            add(Box.createRigidArea(gap));
         }

         this.buttons.add(buttons[i]);
         add(buttons[i]);

         if (listener != null) {
            buttons[i].addActionListener(listener);
         }
      }

      add(Box.createRigidArea(gap));
      add(Box.createGlue());
   }

   /**
    * Gets a button by its addition index (when the button panel constructed)
    * @param index The index of the button to retrieve
    * @return The requested button
    */
   public JButton getButtonByIndex(int index) {
      return buttons.get(index);
   }

   /**
    * Gets a button by its addition index (when the button panel constructed)
    * @param index The index of the button to retrieve
    * @return The requested button
    */
   public JButton getButtonByText(String text) {
      return buttons.stream().filter(button -> button.getText().equals(text)).findFirst().orElse(null);
   }

   /**
    * @return All buttons that were added to this button panel (read-only list)
    */
   public List<JButton> getButtons() {
      return Collections.unmodifiableList(buttons);
   }
}
