package org.spa.view.util;

import java.awt.*;

/**
 * @author Haim Adrian
 * @since 12-May-20
 */
public class Fonts {
   public static final int DEFAULT_FONT_SIZE = Math.max(14, Toolkit.getDefaultToolkit().getScreenResolution() / 9);
   public static final int PROPORTIONAL_FONT_SIZE_CHANGE = (int) (0.2 * DEFAULT_FONT_SIZE);
   public static final String DEFAULT_FONT_FACE = "Dialog";
   public static final String DEFAULT_MONOSPACED_FONT_FACE = "Courier New";

   public static final Font PANEL_HEADING_FONT = new Font(DEFAULT_FONT_FACE, Font.BOLD, DEFAULT_FONT_SIZE + PROPORTIONAL_FONT_SIZE_CHANGE);
   public static final Font HEADING_FONT = new Font(DEFAULT_FONT_FACE, Font.BOLD, DEFAULT_FONT_SIZE * 2);
   public static final Font BOLD_FONT = new Font(DEFAULT_FONT_FACE, Font.BOLD, DEFAULT_FONT_SIZE);
   public static final Font PLAIN_FONT = new Font(DEFAULT_FONT_FACE, Font.PLAIN, DEFAULT_FONT_SIZE);
   public static final Font SMALL_FONT = new Font(DEFAULT_FONT_FACE, Font.PLAIN, DEFAULT_FONT_SIZE - PROPORTIONAL_FONT_SIZE_CHANGE);
   public static final Font BIG_FONT = new Font(DEFAULT_FONT_FACE, Font.PLAIN, DEFAULT_FONT_SIZE + PROPORTIONAL_FONT_SIZE_CHANGE);
   public static final Font ITALIC_FONT = new Font(DEFAULT_FONT_FACE, Font.ITALIC, DEFAULT_FONT_SIZE);
   public static final Font BOLD_ITALIC_FONT = new Font(DEFAULT_FONT_FACE, Font.BOLD | Font.ITALIC, DEFAULT_FONT_SIZE);
   public static final Font MONOSPACED_FONT = new Font(DEFAULT_MONOSPACED_FONT_FACE, Font.PLAIN, DEFAULT_FONT_SIZE);

   private Fonts() {
      // Disallow instantiation of this class
   }
}
