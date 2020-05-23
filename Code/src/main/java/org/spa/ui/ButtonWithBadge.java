package org.spa.ui;

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
         int badgeWidth = 30;
         int badgeHeight = 30;

         Color colorBackup = g.getColor();
         Font fontBackup = g.getFont();
         g.setFont(Fonts.BOLD_FONT);

         g.setColor(Color.WHITE);
         g.fillOval(0, 0, badgeWidth, badgeHeight);
         g.setColor(Color.RED);
         g.fillOval(2, 2, badgeWidth - 4, badgeHeight - 4);
         g.setColor(Color.WHITE);
         g.drawChars(countStr.toCharArray(), 0, countStr.length(), badgeWidth / 2 + 6 - (countStr.length() * 8), badgeHeight / 2 + 6);

         g.setFont(fontBackup);
         g.setColor(colorBackup);
      }
   }

   public void setCountForBadge(int countForBadge) {
      this.countForBadge = countForBadge;
      repaint();
   }
}
