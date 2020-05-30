package org.spa.ui.control;

import org.spa.ui.util.Fonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * An image viewer which can be used when there is a need to stretch an image over its container's content
 * @author Haim Adrian
 * @since 16-May-20
 */
public class ImageViewer extends JPanel {
   private Image image;
   private int margin;
   private boolean isStretched;
   private int x = 0;
   private int y = 0;
   private Image scaledImage;
   private String ad;

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
    * Constructs an {@link ImageViewer} with custom margin and an ad
    * @param image The image to display
    * @param margin The space to take from image's container boundaries
    * @param ad An additional text that can be printed over the picture, at its top left corner - used for ads
    */
   public ImageViewer(Image image, int margin, String ad) {
      this(image, true, margin, ad);
   }

   /**
    * Constructs an {@link ImageViewer} with custom margin
    * @param image The image to display
    * @param isStretched Whether to stretch the image or not
    * @param margin The space to take from image's container boundaries
    */
   public ImageViewer(Image image, boolean isStretched, int margin) {
      this(image, isStretched, margin, "");
   }

   /**
    * Constructs an {@link ImageViewer} with custom margin
    * @param image The image to display
    * @param isStretched Whether to stretch the image or not
    * @param margin The space to take from image's container boundaries
    * @param ad An additional text that can be printed over the picture, at its top left corner - used for ads
    */
   public ImageViewer(Image image, boolean isStretched, int margin, String ad) {
      this.image = image;
      this.isStretched = isStretched;
      this.margin = margin;
      this.ad = ad;
      setOpaque(true);

      addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            // rescale image on every size event
            scaledImage = scaleImageIfNeeded();
         }
      });
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      if (image != null) {
         if (isStretched()) {
            if (scaledImage == null) {
               scaledImage = scaleImageIfNeeded();
            }

            g.drawImage(scaledImage, x + margin, y + margin,this);
         } else {
            g.drawImage(image, x + margin, y + margin, this);
         }
      }

      if (!ad.isEmpty()) {
         Color colorBackup = g.getColor();
         Font fontBackup = g.getFont();
         g.setFont(Fonts.PANEL_HEADING_FONT);
         g.setColor(Color.RED);
         int doubleMargin = margin*2;
         if (doubleMargin == 0) {
            doubleMargin = 10;
         }
         g.drawChars(ad.toCharArray(), 0, ad.length(), x + doubleMargin, y + doubleMargin + 10);
         g.setFont(fontBackup);
         g.setColor(colorBackup);
      }
   }

   /**
    * To avoid of scaling an image on every onPaint event, we cache it and scale it only in case there was a resize event.
    * @return The scaled image or <code>null</code> in case the image was not configured as stretched.
    */
   private Image scaleImageIfNeeded() {
      if (isStretched) {
         int doubleMargin = margin*2;
         int width = Math.max(getWidth() - doubleMargin, 1);
         int height = Math.max(getHeight() - doubleMargin, 1);
         return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
      } else {
         return null;
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

   public String getAd() {
      return ad;
   }

   public void setAd(String ad) {
      this.ad = ad;
   }
}
