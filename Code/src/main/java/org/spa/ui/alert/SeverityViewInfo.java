package org.spa.ui.alert;

import org.spa.common.util.log.factory.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author hadrian
 * @since 15-May-20
 */
public enum SeverityViewInfo {
   DISABLED(), NORMAL(), LOW(), MEDIUM(), HIGH();

   private final Icon icon;

   SeverityViewInfo() {
      Icon icon1;
      if ("DISABLED".equalsIgnoreCase(name())) {
         icon1 = null;
      } else {
         try {
            // Use our class to resolve the same package
            icon1 = new ImageIcon(ImageIO.read(SeverityViewInfo.class.getResourceAsStream(name() + ".gif")));
         } catch (Exception e) {
            icon1 = null;
            LoggerFactory.getLogger(SeverityViewInfo.class).error("Could not find resource: " + name() + ".gif");
         }
      }

      icon = icon1;
   }

   public Icon getIcon() {
      return icon;
   }
}
