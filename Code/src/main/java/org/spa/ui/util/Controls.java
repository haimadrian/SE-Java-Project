package org.spa.ui.util;

import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.ui.control.LabeledField;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.spa.ui.util.Fonts.PLAIN_FONT;

/**
 * Some utilities to create controls, tweak the look and feel, center dialogs, etc.
 * @author Haim Adrian
 * @since 12-May-20
 */
public class Controls {
   public static final Dimension DIM_1 = new Dimension(1, 1);
   public static final Dimension DIM_5 = new Dimension(5, 5);
   public static final Dimension DIM_10 = new Dimension(10, 10);

   public static final Border RED_LINE_BORDER = BorderFactory.createLineBorder(Color.RED, 5);
   public static final int SEPARATOR_THICKNESS = 2;

   private static final Logger logger = LoggerFactory.getLogger(Controls.class);
   public static Color background;
   private static Color tooltipBack;
   public static Color textColor;
   public static final Color acceptButtonColor = new Color(54, 88, 128);

   private Controls() {
      // Disallow instantiation of this class
   }

   /**
    * Makes a BoxLayout panel of the specified orientation containing the supplied components.
    *
    * @param layoutType Type of BoxLayout. the axis to lay out components along. Can be one of: BoxLayout.X_AXIS, BoxLayout.Y_AXIS, BoxLayout.LINE_AXIS or BoxLayout.PAGE_AXIS
    * @param comps Component[] to add
    * @param separateComponents A small space will be added between components if this is <code>true</code>.
    * @return A panel containing all of the components.
    */
   public static JPanel createPanel(int layoutType, boolean separateComponents, Component... comps) {
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, layoutType));
      for (int i = 0; i < comps.length; i++) {
         if (i > 0 && separateComponents) {
            p.add(Box.createRigidArea(DIM_5));
         }
         p.add(comps[i]);
      }
      return p;
   }

   public static JPanel createPanel(int layoutType, Component... comps) {
      return createPanel(layoutType, false, comps);
   }

   /**
    * Utility to add common action listener to each button in the group.
    *
    * @param actionListener
    * @param buttons
    */
   public static void addActionListener(ActionListener actionListener, JButton... buttons) {
      for (JButton button : buttons) {
         button.addActionListener(actionListener);
      }
   }

   /**
    * Make a horizontal button panel where the buttons are centered and separated by <code>gapSize</code> pixels.
    *
    * @param listener The action listener for the buttons.
    * @param gapSize The inter-button gap size.
    * @param buttons The array of buttons to add to the panel.
    * @return The button panel
    */
   public static JPanel createButtonPanel(ActionListener listener, int gapSize, JButton... buttons) {
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));

      Dimension gap = new Dimension(gapSize, gapSize);

      p.add(Box.createGlue());
      p.add(Box.createRigidArea(gap));
      for (int n = 0; n < buttons.length; n++) {
         if (n > 0) {
            p.add(Box.createRigidArea(gap));
         }
         p.add(buttons[n]);

         if (listener != null) {
            buttons[n].addActionListener(listener);
         }
      }
      p.add(Box.createRigidArea(gap));
      p.add(Box.createGlue());

      return p;
   }

   /**
    * Makes a line-oriented BoxLayout panel specifically for a labeled field
    *
    * @param label A textual label the supplied component
    * @param labelWidth The width of the label
    * @param component The Swing Component to add
    * @return A panel containing the label and component
    * @see LabeledField
    */
   public static LabeledField LabeledField(String label, int labelWidth, JComponent component) {
      return new LabeledField(label, labelWidth, component);
   }

   /**
    * Creates a <code>JLabel</code> instance with the specified text.
    * The label is aligned against the leading edge of its display area,
    * and centered vertically.
    *
    * @param text  The text to be displayed by the label.
    */
   public static JLabel createLabel(String text) {
      return createLabel(text, null, SwingConstants.LEADING);
   }

   /**
    * Creates a <code>JLabel</code> instance with the specified text.
    * The label is aligned against the leading edge of its display area,
    * and centered vertically.
    * The text is on the trailing edge of the image.
    *
    * @param text  The text to be displayed by the label.
    * @param icon  The image to be displayed by the label.
    */
   public static JLabel createLabel(String text, Icon icon) {
      return createLabel(text, icon, SwingConstants.LEFT);
   }

   /**
    * Creates a <code>JLabel</code> instance with the specified
    * text, image, and horizontal alignment.
    * The label is centered vertically in its display area.
    * The text is on the trailing edge of the image.
    *
    * @param text  The text to be displayed by the label.
    * @param icon  The image to be displayed by the label.
    * @param horizontalAlignment  One of the following constants
    *           defined in <code>SwingConstants</code>:
    *           <code>LEFT</code>,
    *           <code>CENTER</code>,
    *           <code>RIGHT</code>,
    *           <code>LEADING</code> or
    *           <code>TRAILING</code>.
    */
   public static JLabel createLabel(String text, Icon icon, int horizontalAlignment) {
      return createLabel(text, icon, horizontalAlignment, PLAIN_FONT);
   }

   /**
    * Creates a <code>JLabel</code> instance with the specified
    * text, image, and horizontal alignment.
    * The label is centered vertically in its display area.
    * The text is on the trailing edge of the image.
    *
    * @param text  The text to be displayed by the label.
    * @param icon  The image to be displayed by the label.
    * @param horizontalAlignment  One of the following constants
    *           defined in <code>SwingConstants</code>:
    *           <code>LEFT</code>,
    *           <code>CENTER</code>,
    *           <code>RIGHT</code>,
    *           <code>LEADING</code> or
    *           <code>TRAILING</code>.
    * @param font The font to set to the label. See {@link Fonts}
    */
   public static JLabel createLabel(String text, Icon icon, int horizontalAlignment, Font font) {
      JLabel label = new JLabel(text, icon, horizontalAlignment);
      label.setFont(font);
      return label;
   }

   public static JLabel createLabel(String text, Font font) {
      return createLabel(text, null, SwingConstants.LEADING, font);
   }

   /**
    * Creates a <code>JLabel</code> instance with the specified text.
    * The label is aligned against the leading edge of its display area,
    * and centered vertically.
    *
    * @param text  The text to be displayed by the label.
    */
   public static JLabel createTitle(String text) {
      return createTitle(text, null, SwingConstants.LEADING);
   }

   /**
    * Creates a <code>JLabel</code> instance with the specified text.
    * The label is aligned against the leading edge of its display area,
    * and centered vertically.
    * The text is on the trailing edge of the image.
    *
    * @param text  The text to be displayed by the label.
    * @param icon  The image to be displayed by the label.
    */
   public static JLabel createTitle(String text, Icon icon) {
      return createTitle(text, icon, SwingConstants.LEFT);
   }

   /**
    * Creates a <code>JLabel</code> instance with the specified
    * text, image, and horizontal alignment.
    * The label is centered vertically in its display area.
    * The text is on the trailing edge of the image.
    *
    * @param text  The text to be displayed by the label.
    * @param icon  The image to be displayed by the label.
    * @param horizontalAlignment  One of the following constants
    *           defined in <code>SwingConstants</code>:
    *           <code>LEFT</code>,
    *           <code>CENTER</code>,
    *           <code>RIGHT</code>,
    *           <code>LEADING</code> or
    *           <code>TRAILING</code>.
    */
   public static JLabel createTitle(String text, Icon icon, int horizontalAlignment) {
      JLabel label = new JLabel(text, icon, horizontalAlignment);
      label.setFont(Fonts.HEADING_FONT);
      return label;
   }

   public static JTextArea createTextArea(String text, boolean isEditable) {
      JTextArea textArea = new JTextArea(text);
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setEditable(isEditable);
      textArea.setFont(Fonts.PLAIN_FONT);
      return textArea;
   }

   public static JScrollPane withScrollPane(Component comp, int width, int height) {
      JScrollPane scroll = new JScrollPane(comp);
      scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
      scroll.setMinimumSize(new Dimension(width / 5, height == Integer.MAX_VALUE ? height : height / 3));
      scroll.setPreferredSize(new Dimension(width, height));
      scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      //scroll.getViewport().add(comp);
      return scroll;
   }

   public static JButton createButton(String caption, ActionListener listener, boolean isDefault) {
      JButton button = new JButton(caption);
      button.addActionListener(listener);
      button.setDefaultCapable(isDefault);
      button.setFont(Fonts.BOLD_FONT);
      return button;
   }

   /**
    * Make a flat style for button with a highlight when hovering over it
    * @param button The button to stylize
    */
   public static void setFlatStyle(AbstractButton button) {
      setFlatStyle(button, true);
   }

   /**
    * Make a flat style for button where you can control whether to set a highlight when hovering over it or not
    * @param button The button to stylize
    * @param putHoverEffect Set a mouse hover highlight effect?
    */
   public static void setFlatStyle(AbstractButton button, boolean putHoverEffect) {
      button.setBorderPainted(false);
      button.setFocusPainted(false);
      button.setContentAreaFilled(false);
      button.setBackground(background.brighter());

      if (putHoverEffect) {
         button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
               // Show background so there will be an highlight
               button.setOpaque(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
               // Transparent background
               button.setOpaque(false);
            }
         });
      }
   }

   /**
    * @return A nice-looking and resizable horizontal separator, preferably for use in box layouts
    */
   public static JSeparator createHorizontalSeparator() {
      JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
      sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, SEPARATOR_THICKNESS));
      sep.setMinimumSize(new Dimension(0, SEPARATOR_THICKNESS));
      return sep;
   }

   /**
    * @return A nice-looking and resizable vertical separator, preferably for use in box layouts
    */
   public static JSeparator createVerticalSeparator() {
      JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
      sep.setMaximumSize(new Dimension(SEPARATOR_THICKNESS, Integer.MAX_VALUE));
      sep.setMinimumSize(new Dimension(SEPARATOR_THICKNESS, 5));
      return sep;
   }

   /**
    * Places a dialog on the screen relative to its parent
    *
    * @param parent parent component
    * @param dialog dialog to move
    */
   public static void centerDialog(Component parent, Window dialog) {
      int x = parent.getLocationOnScreen().x + (parent.getSize().width - dialog.getSize().width) / 2;
      if (x < 0) {
         x = 0;
      }

      int y = parent.getLocationOnScreen().y + (parent.getSize().height - dialog.getSize().height) / 2;
      if (y < 0) {
         y = 0;
      }

      dialog.setLocation(x, y);
   }

   /**
    * Places a dialog in the center of the screen
    *
    * @param dialog dialog to move
    */
   public static void centerDialog(Window dialog) {
      Frame f = JOptionPane.getFrameForComponent(dialog);
      if (f.isShowing()) {
         centerDialog(f, dialog);
      } else {
         Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
         int x = (size.width - dialog.getSize().width) / 2;
         if (x < 0) {
            x = 0;
         }

         int y = (size.height - dialog.getSize().height) / 2;
         if (y < 0) {
            y = 0;
         }

         dialog.setLocation(x, y);
      }
   }

   /**
    * Goes through all the data in the table columns, and make sure they fit as much visible space as possible.
    *
    * @param table A table to fit the columns to its data
    * @param maxWidth Maximum width for any column
    */
   public static void fitTableColumnsToData(JTable table, int maxWidth) {
      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      TableColumnModel tcm = table.getColumnModel();
      for (int col = 0; col < tcm.getColumnCount(); col++) {
         TableColumn tc = tcm.getColumn(col);
         int width = 0;
         Object headerValue = tc.getHeaderValue();
         Component rendererComponent = null;

         for (int row = 0; row < table.getRowCount(); row++) {
            Object data = table.getValueAt(row, tc.getModelIndex());
            if (data != null) {
               rendererComponent = table.getCellRenderer(row, tc.getModelIndex()).getTableCellRendererComponent(table, data, false, false, row, tc.getModelIndex());
               width = Math.max(width, rendererComponent.getPreferredSize().width + 4);
            }
         }

         if (headerValue != null && rendererComponent != null) {
            width = Math.max(width, rendererComponent.getFontMetrics(rendererComponent.getFont()).stringWidth(headerValue.toString()) + 10);
         }

         width = Math.min(width, maxWidth);
         tc.setWidth(width);
         tc.setPreferredWidth(width);
      }

      table.revalidate();
   }

   /**
    * Given a table column model, generate a map of headers to columns
    *
    * @param model A table model to create map of header-to-column for
    * @return map of headers to columns
    */
   public static Map<Object, TableColumn> getMapOfHeadersToColumns(TableColumnModel model) {
      Map<Object, TableColumn> map = new HashMap<Object, TableColumn>();
      if (model == null) {
         return map;
      }

      for (int pos = 0; pos < model.getColumnCount(); pos++) {
         TableColumn oldCol = model.getColumn(pos);
         TableColumn newCol = new TableColumn(pos, oldCol.getWidth());
         map.put(oldCol.getHeaderValue(), newCol);
      }

      return map;
   }

   /**
    * Set the minimum, preferred and maximum size of the given component to the given width and
    * height and preferred width and height and maximum width and height
    *
    * @param comp The component whose minimum, preferred and maximum size are to be set
    * @param minWidth
    * @param minHeight
    * @param prefWidth
    * @param prefHeight
    * @param maxWidth
    * @param maxHeight
    */
   public static void setComponentSize(JComponent comp, int minWidth, int minHeight, int prefWidth, int prefHeight, int maxWidth, int maxHeight) {
      comp.setMinimumSize(new Dimension(minWidth, minHeight));
      comp.setMaximumSize(new Dimension(maxWidth, maxHeight));
      comp.setPreferredSize(new Dimension(prefWidth, prefHeight));
   }

   /**
    * Set the minimum, preferred and maximum size of the given component to the given min, max, and preferred sizes;
    *
    * @param comp The component whose minimum, preferred and maximum size are to be set
    * @param minSize The minimum size
    * @param prefSize The preferred size
    * @param maxSize The maximum size
    */
   public static void setComponentSize(JComponent comp, Dimension minSize, Dimension prefSize, Dimension maxSize) {
      comp.setMinimumSize(minSize);
      comp.setMaximumSize(maxSize);
      comp.setPreferredSize(prefSize);
   }

   /**
    * Set the minimum, preferred and maximum size of the given component to the given width and height
    *
    * @param comp The component whose minimum, preferred and maximum size are to be set
    * @param width The width
    * @param height The height
    */
   public static void setComponentSize(JComponent comp, int width, int height) {
      setComponentSize(comp, width, height, width, height, width, height);
   }

   /**
    * Set the minimum, preferred and maximum size of the given component to the given <code>size</code>.
    *
    * @param comp The component whose minimum, preferred and maximum size are to be set
    * @param size The size
    */
   public static void setComponentSize(JComponent comp, Dimension size) {
      setComponentSize(comp, size, size, size);
   }

   /**
    * Set the enabled state of the given Container and all of its children recursively
    *
    * @param parent The parent container
    * @param enable The state to set
    */
   public static void setEnabledRecursively(Container parent, boolean enable) {
      parent.setEnabled(enable);
      for (Component component : parent.getComponents()) {
         if (component instanceof Container) {
            setEnabledRecursively((Container) component, enable);
         }
      }
   }

   public static void setFontToComponents(Container comp, Font font) {
      comp.setFont(font);
      for (Component child : comp.getComponents()) {
         if (child instanceof Container) {
            setFontToComponents((Container)child, font);
         } else if (child instanceof JComponent) {
            setFontToComponents((JComponent)child, font);
         }
      }
   }

   public static void setFontToComponents(JComponent comp, Font font) {
      comp.setFont(font);
      for (Component child : comp.getComponents()) {
         if (child instanceof Container) {
            setFontToComponents((Container)child, font);
         } else if (child instanceof JComponent) {
            setFontToComponents((JComponent)child, font);
         }
      }
   }

   public static void setBackgroundToComponents(Container comp, Color color) {
      comp.setBackground(color);
      for (Component child : comp.getComponents()) {
         if (child instanceof Container) {
            setBackgroundToComponents((Container)child, color);
         } else if (child instanceof JComponent) {
            setBackgroundToComponents((JComponent)child, color);
         }
      }
   }

   public static void setBackgroundToComponents(JComponent comp, Color color) {
      comp.setBackground(color);
      for (Component child : comp.getComponents()) {
         if (child instanceof Container) {
            setBackgroundToComponents((Container)child, color);
         } else if (child instanceof JComponent) {
            setBackgroundToComponents((JComponent)child, color);
         }
      }
   }

   public static void increaseComponentWidth(Container comp, Class<?> typeToSearch, double proportion) {
      if (typeToSearch.isAssignableFrom(comp.getClass())) {
         setComponentSize((JComponent) comp, (int)(comp.getPreferredSize().width * proportion), comp.getPreferredSize().height);
         ((JComponent)comp).setFont(Fonts.BOLD_FONT);
      }

      for (Component child : comp.getComponents()) {
         if (child instanceof Container) {
            increaseComponentWidth((Container)child, typeToSearch, proportion);
         } else if (child instanceof JComponent) {
            increaseComponentWidth((JComponent)child, typeToSearch, proportion);
         }
      }
   }

   public static void increaseComponentWidth(JComponent comp, Class<?> typeToSearch, double proportion) {
      if (typeToSearch.isAssignableFrom(comp.getClass())) {
         setComponentSize((JComponent) comp, (int)(comp.getPreferredSize().width * proportion), comp.getPreferredSize().height);
         ((JComponent)comp).setFont(Fonts.BOLD_FONT);
      }

      for (Component child : comp.getComponents()) {
         if (child instanceof Container) {
            increaseComponentWidth((Container)child, typeToSearch, proportion);
         } else if (child instanceof JComponent) {
            increaseComponentWidth((JComponent)child, typeToSearch, proportion);
         }
      }
   }

   /**
    * Make some tweaks to the default Look-and-Feel. This is because the default Metal LAF is pretty dodgy, so
    * we try to make it a bit more Windows-like.
    */
   public static void tweakPLAF() {
      // Set up Look & Feel to default for current OS

      /*
       * Other L&F options: UIManager.getSystemLookAndFeelClassName() - default for current OS
       * UIManager.getCrossPlatformLookAndFeelClassName() - metal L&F
       * "javax.swing.plaf.metal.MetalLookAndFeel" - metal L&F
       * "com.sun.java.swing.plaf.windows.WindowsLookAndFeel" - windows L&F
       * "com.sun.java.swing.plaf.gtk.GTKLookAndFeel" - GTK+ L&F "com.sun.java.swing.plaf.mac.MacLookAndFeel" -
       * Mac L&F "com.sun.java.swing.plaf.motif.MotifLookAndFeel" - Motif L&F
       */
      //String className = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
      String className = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
      try {
         UIManager.setLookAndFeel(className);
      } catch (Exception e) {
         logger.error("Failed setting NimbusLookAndFeel. Defaulting to system L&F");

         className = UIManager.getSystemLookAndFeelClassName();
         try {
            UIManager.setLookAndFeel(className);
         } catch (Exception classNotFoundException) {
            logger.error("Failed setting SystemLookAndFeel.. FML");
         }
      }

      // Override info because tooltip uses "info" background instead of tooltip.background.. WTF dude?
      tooltipBack = UIManager.getColor("ToolTip.background");
      background = UIManager.getColor("background");
      UIManager.put("info", tooltipBack);
      UIManager.put("ToolTip.font", PLAIN_FONT);
      UIManager.put("Tree.background", tooltipBack);
      // See all options here: https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/_nimbusDefaults.html

      if (isDarkMode()) {
         darkness();
      }
   }

   public static void setIsDarkMode(boolean isDarkMode) {
      if (isDarkMode) {
         try {
            new File(SPAApplication.getWorkingDirectory(), "dark.flag").createNewFile();
         } catch (IOException e) {
            logger.error("Failed to store dark mode state", e);
         }
      } else {
         new File(SPAApplication.getWorkingDirectory(), "dark.flag").delete();
      }
   }

   public static boolean isDarkMode() {
      return new File(SPAApplication.getWorkingDirectory(), "dark.flag").exists();
   }

   private static void darkness() {
      background = new Color(60, 63, 65);
      Color editorBackground = new Color(43, 43, 43);
      UIManager.put("ToolTip.font", PLAIN_FONT);
      UIManager.put("background", background);
      UIManager.put("nimbusBlueGrey", editorBackground);
      UIManager.put("nimbusLightBackground", editorBackground);
      UIManager.put("nimbusSelectedText", Color.white);
      UIManager.put("nimbusBorder", editorBackground);
      UIManager.put("scrollbar", editorBackground);
      UIManager.put("DesktopIcon.background", editorBackground);
      UIManager.put("control", background);
      textColor = Color.white.darker();
      UIManager.put("text", textColor);
      UIManager.put("activeCaption", background);
      UIManager.put("inactiveCaption", background);
      UIManager.put("menu", background);
      UIManager.put("controlLHighlight", background);
      UIManager.put("controlHighlight", background);
      UIManager.put("controlDkShadow", background);
      UIManager.put("textHighlightText", new Color(169, 169, 169));
      UIManager.put("Button.background", editorBackground);
      UIManager.put("MenuBar.background", background);
      UIManager.put("ComboBox.background", background);
      UIManager.put("EditorPane.background", background);
      UIManager.put("CheckBox.background", background);
      UIManager.put("ToggleButton.background", background);
      UIManager.put("TabbedPane.background", background);
      UIManager.put("TableHeader.background", background);
      UIManager.put("PopupMenu.background", background);
      UIManager.put("Separator.background", background);
      UIManager.put("ToolTip.background", background);
      UIManager.put("ToolBar.background", background);
      UIManager.put("ScrollPane.background", background);
      UIManager.put("CheckBoxMenuItem.background", background);
      UIManager.put("Viewport.background", background);
      UIManager.put("SplitPane.background", background);
      UIManager.put("Spinner.background", background);
      UIManager.put("MenuItem.background", background);
      UIManager.put("ScrollBar.background", background);
      UIManager.put("ScrollBarThumb.background", background);
      UIManager.put("ScrollBarTrack.background", background);
      UIManager.put("SliderThumb.background", background);
      UIManager.put("SliderTrack.background", background);
      UIManager.put("info", UIManager.get("ToolTip.background"));
   }
}
