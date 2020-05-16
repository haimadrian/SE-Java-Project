package org.spa.ui;

import javax.swing.*;
import java.awt.*;

/**
 * An image viewer which can be used when there is a need to stretch an image over its container's content
 * @author hadrian
 * @since 16-May-20
 */
public class ImageViewer extends JPanel {
   private Image image;
   private int margin;
   private boolean isStretched;
   private int x = 0;
   private int y = 0;

   /**
    * Constructs an {@link ImageViewer} with margin=0
    * @param image The image to display
    */
   public ImageViewer(Image image) {
      this(image, true, 0);
   }

   /**
    * Constructs an {@link ImageViewer} with custom margin
    * @param image The image to display
    * @param margin The space to take from image's container boundaries
    */
   public ImageViewer(Image image, int margin) {
      this(image, true, margin);
   }

   /**
    * Constructs an {@link ImageViewer} with custom margin
    * @param image The image to display
    * @param isStretched Whether to stretch the image or not
    * @param margin The space to take from image's container boundaries
    */
   public ImageViewer(Image image, boolean isStretched, int margin) {
      this.image = image;
      this.isStretched = isStretched;
      this.margin = margin;
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      if (image != null) {
         if (isStretched()) {
            int doubleMargin = margin*2;
            Image scaledImage = image.getScaledInstance(getWidth() - doubleMargin, getHeight() - doubleMargin, Image.SCALE_SMOOTH);
            g.drawImage(scaledImage, x + margin, y + margin,this);
         } else {
            g.drawImage(image, x + margin, y + margin, this);
         }
      }
   }

   public Image getImage() {
      return image;
   }

   public void setImage(Image image) {
      this.image = image;
      repaint();
   }

   public int getMargin() {
      return margin;
   }

   public void setMargin(int margin) {
      this.margin = margin;
   }

   public boolean isStretched() {
      return isStretched;
   }

   public void setStretched(boolean stretched) {
      this.isStretched = stretched;
      repaint();
   }

   public int getImageX() {
      return x;
   }

   public void setImageX(int x) {
      this.x = x;
      repaint();
   }

   public int getImageY() {
      return y;
   }

   public void setImageY(int y) {
      this.y = y;
      repaint();
   }
}
