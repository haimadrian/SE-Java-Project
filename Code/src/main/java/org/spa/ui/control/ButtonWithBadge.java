package org.spa.ui.control;

import org.spa.ui.util.Fonts;

import javax.swing.*;
import java.awt.*;

/**
 * @author Haim Adrian
 * @since 22-May-20
 */
public class ButtonWithBadge extends JButton {
   private int countForBadge;

   public ButtonWithBadge() {
      init();
   }

   public ButtonWithBadge(Icon icon) {
      super(icon);
      init();
   }

   public ButtonWithBadge(String text) {
      super(text);
      init();
   }

   public ButtonWithBadge(Action a) {
      super(a);
      init();
   }

   public ButtonWithBadge(String text, Icon icon) {
      super(text, icon);
      init();
   }

   private void init() {
      setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
      setFont(Fonts.BOLD_FONT);
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      if (countForBadge > 0) {
         String countStr = String.valueOf(countForBadge);
         int badgeDiameter = (int)(getWidth() / 2.5);

         Color colorBackup = g.getColor();
         Font fontBackup = g.getFont();
         g.setFont(Fonts.BOLD_FONT);

         g.setColor(Color.WHITE);
         g.fillOval(0, 0, badgeDiameter, badgeDiameter);
         g.setColor(Color.RED);
         g.fillOval(2, 2, badgeDiameter - 4, badgeDiameter - 4);
         g.setColor(Color.WHITE);
         int halfFontSize = Fonts.PLAIN_FONT.getSize() / 2;
         int xAdjustment = countStr.length() == 1 ? 2 : (countStr.length()-1) * halfFontSize;
         int yAdjustment = halfFontSize - (halfFontSize % 2);
         g.drawChars(countStr.toCharArray(), 0, countStr.length(), badgeDiameter / 2 - xAdjustment, badgeDiameter / 2 + yAdjustment);

         g.setFont(fontBackup);
         g.setColor(colorBackup);
      }
   }

   public void setCountForBadge(int countForBadge) {
      this.countForBadge = countForBadge;
      repaint();
   }
}
