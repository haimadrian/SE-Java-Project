package org.spa.ui.item;

import org.spa.ui.ImageViewer;
import org.spa.ui.util.Controls;
import org.spa.ui.util.Fonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static org.spa.ui.util.Controls.*;

/**
 * @author Haim Adrian
 * @since 23-May-20
 */
public class ItemInfoDialog extends JDialog {
   private final ItemViewInfo item;

   public ItemInfoDialog(ItemViewInfo item) {
      this.item = item;
   }

   public ItemInfoDialog init() {
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setPreferredSize(new Dimension((int)(screenSize.width / 1.5), screenSize.height / 2));
      setTitle("Item Info");

      // Lay out the label and text panes from top to bottom.
      JPanel fields = new JPanel();
      fields.setLayout(new BoxLayout(fields, BoxLayout.PAGE_AXIS));
      fields.setAlignmentX(0);

      // Lay out the members from left to right.
      JPanel namePanel = createMemberPanelWithLabel("Name:", item.getName());
      JPanel descPanel = createMemberPanelWithTextArea("Desc:", item.getDescription(), 200, screenSize.height / 2 - 50);
      JPanel pricePanel = createMemberPanelWithoutTextArea("Price: " + item.getPrice() + "$");

      fields.add(namePanel);
      fields.add(Box.createRigidArea(new Dimension(0, 25)));
      descPanel.setSize(250, 400);
      fields.add(descPanel);
      fields.add(Box.createRigidArea(new Dimension(0, 25)));
      fields.add(pricePanel);

      JLabel label = createTitle("Item Info");
      ImageViewer imageViewer = new ImageViewer(item.getImage().getImage(), true, 0);

      imageViewer.setSize(screenSize.width - 200, 300);
      imageViewer.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));

      // Lay out the label and scroll pane from top to bottom.
      JPanel detailsPanel = new JPanel();
      detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.PAGE_AXIS));

      detailsPanel.add(label);
      detailsPanel.add(Box.createRigidArea(new Dimension(0,5)));
      detailsPanel.add(fields);
      detailsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

      // Lay out the sections from left to right.
      JSplitPane splitPane = new JSplitPane();
      splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      splitPane.setLeftComponent(detailsPanel);
      splitPane.setRightComponent(imageViewer);
      splitPane.setDividerSize(15);
      splitPane.setDividerLocation(0.5);
      splitPane.setOpaque(true);
      splitPane.setContinuousLayout(true);
      splitPane.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            SwingUtilities.invokeLater(imageViewer::repaint);
         }

         @Override
         public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            SwingUtilities.invokeLater(imageViewer::repaint);
         }
      });

      setContentPane(splitPane);

      pack();
      Controls.centerDialog(this);

      return this;
   }

   private JPanel createMemberPanelWithLabel(String name, String value) {
      return createMemberPanel(name, value, 0, 0, true);
   }

   private JPanel createMemberPanelWithTextArea(String name, String value, int textAreaWidth, int textAreaHeight) {
      return createMemberPanel(name, value, textAreaWidth, textAreaHeight, false);
   }

   private JPanel createMemberPanelWithoutTextArea(String name) {
      return createMemberPanel(name, null, 0, 0, false);
   }

   private JPanel createMemberPanel(String name, String value, int textAreaWidth, int textAreaHeight, boolean isLabel) {
      JPanel memberPanel = new JPanel();
      memberPanel.setLayout(new BoxLayout(memberPanel, BoxLayout.Y_AXIS));
      JPanel inner = new JPanel();
      inner.setLayout(new BoxLayout(inner, BoxLayout.X_AXIS));
      JLabel label = createLabel(name, Fonts.PANEL_HEADING_FONT);
      label.setSize(40, 30);
      label.setVerticalAlignment(SwingConstants.TOP);

      inner.add(label);
      inner.add(Box.createHorizontalGlue());
      memberPanel.add(inner);
      memberPanel.add(Box.createRigidArea(new Dimension(0, 10)));

      if (value != null) {
         if (isLabel) {
            JLabel label2 = createLabel(value, Fonts.PLAIN_FONT);
            JPanel inner2 = new JPanel();
            inner2.setLayout(new BoxLayout(inner2, BoxLayout.X_AXIS));
            inner2.add(label2);
            inner2.add(Box.createHorizontalGlue());
            memberPanel.add(inner2);
         } else {
            JTextArea textArea = createTextArea(value, textAreaWidth, textAreaHeight);
            memberPanel.add(withScrollPane(textArea));
         }
      } else {
         memberPanel.add(Box.createVerticalGlue());
      }

      return memberPanel;
   }
}
