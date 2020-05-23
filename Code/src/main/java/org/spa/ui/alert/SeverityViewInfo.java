package org.spa.ui.alert;

import org.spa.ui.util.ImagesCache;

import javax.swing.*;

/**
 * @author Haim Adrian
 * @since 15-May-20
 */
public enum SeverityViewInfo {
   DISABLED(), NORMAL(), LOW(), MEDIUM(), HIGH();

   private final Icon icon;

   SeverityViewInfo() {
      ImageIcon icon1;
      if ("DISABLED".equalsIgnoreCase(name())) {
         icon1 = null;
      } else {
         icon1 = ImagesCache.getInstance().getImage(name() + ".gif");
      }

      icon = icon1;
   }

   public Icon getIcon() {
      return icon;
   }
}
