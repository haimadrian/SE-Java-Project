package org.spa.ui.util;

import java.awt.*;

/**
 * @author hadrian
 * @since 12-May-20
 */
public class Fonts {
   public static final int DEFAULT_FONT_SIZE = 16;
   public static final String DEFAULT_FONT_FACE = "Dialog";
   public static final String DEFAULT_MONOSPACED_FONT_FACE = "Courier New";

   public static final Font PANEL_HEADING_FONT = new Font("tahoma", Font.BOLD, DEFAULT_FONT_SIZE + 2);
   public static final Font BOLD_FONT = new Font(DEFAULT_FONT_FACE, Font.BOLD, DEFAULT_FONT_SIZE);
   public static final Font PLAIN_FONT = new Font(DEFAULT_FONT_FACE, Font.PLAIN, DEFAULT_FONT_SIZE);
   public static final Font SMALL_FONT = new Font(DEFAULT_FONT_FACE, Font.PLAIN, DEFAULT_FONT_SIZE - 2);
   public static final Font ITALIC_FONT = new Font(DEFAULT_FONT_FACE, Font.ITALIC, DEFAULT_FONT_SIZE);
   public static final Font BOLD_ITALIC_FONT = new Font(DEFAULT_FONT_FACE, Font.BOLD | Font.ITALIC, DEFAULT_FONT_SIZE);
   public static final Font MONOSPACED_FONT = new Font(DEFAULT_MONOSPACED_FONT_FACE, Font.PLAIN, DEFAULT_FONT_SIZE);

   private Fonts() {
      // Disallow instantiation of this class
   }
}
