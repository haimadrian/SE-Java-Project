package org.spa.ui.item;

import org.spa.ui.control.CustomGridBagConstraints;
import org.spa.ui.control.ImageViewer;
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
public class ItemInfoDialog extends JFrame {
   private final ItemViewInfo item;

   public ItemInfoDialog(ItemViewInfo item) {
      this.item = item;
   }

   public ItemInfoDialog init() {
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setPreferredSize(new Dimension((int)(screenSize.width / 1.5), (int)(screenSize.height / 2)));

      // These fucking Swing panels insist on not working as expected.. Hence I do this voodoo in order to repaint he window with
      // a different height after it has been displayed. Somehow it works and the "Price" row is displayed. Otherwise the price is
      // not displayed and the dialog must be resized by user
      SwingUtilities.invokeLater(() -> setPreferredSize(new Dimension((int)(screenSize.width / 1.5), (int)(screenSize.height / 1.8))));

      setTitle("Item Info");

      // Lay out the label and text panes from top to bottom.
      JPanel fields = new JPanel();
      fields.setLayout(new BoxLayout(fields, BoxLayout.PAGE_AXIS));
      fields.setAlignmentX(0);

      // Lay out the members from left to right.
      JPanel namePanel = createMemberPanelWithLabel("Name:", item.getName());
      JPanel descPanel = createMemberPanelWithTextArea("Desc:", item.getDescription(), 200, 600);
      JPanel pricePanel = createMemberPanelWithoutTextArea("Price: " + item.getPrice() + "$");

      fields.add(namePanel);
      fields.add(Box.createRigidArea(new Dimension(0, 25)));
      fields.add(descPanel);
      fields.add(Box.createRigidArea(new Dimension(0, 25)));
      fields.add(pricePanel);

      JLabel label = createTitle("Item Info");
      ImageViewer imageViewer = new ImageViewer(item.getImage().getImage(), true, 0);

      imageViewer.setSize(300, 300);
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
      //splitPane.setDividerSize(15);
      splitPane.setDividerLocation((int)(getPreferredSize().width * 0.6));
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

      JButton buttonPanel = createButton(" Close ", e -> SwingUtilities.invokeLater(() -> {
         this.setVisible(false);
         this.dispose();
      }), true);
      this.getRootPane().setDefaultButton(buttonPanel);

      // Lay out the label and scroll pane from top to bottom.
      JPanel contentPane = new JPanel();
      contentPane.setLayout(new GridBagLayout());
      CustomGridBagConstraints constraints = new CustomGridBagConstraints();
      contentPane.add(splitPane, constraints.constrainFillBoth().nextY());
      //constraints.addXYFiller(contentPane, 1);
      contentPane.add(buttonPanel, constraints.constrainLabel().nextY());

      setContentPane(contentPane);

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
            memberPanel.add(withScrollPane(createTextArea(value, false), textAreaWidth, textAreaHeight));
         }
      } else {
         memberPanel.add(Box.createVerticalGlue());
      }

      return memberPanel;
   }
}
