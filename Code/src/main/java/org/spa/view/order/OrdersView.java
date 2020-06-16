package org.spa.view.order;

import org.spa.controller.SPAApplication;
import org.spa.controller.order.Order;
import org.spa.controller.order.OrderSystem;
import org.spa.controller.user.User;
import org.spa.controller.user.UserType;
import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;
import org.spa.view.table.PopupAdapter;
import org.spa.view.table.TableConfig;
import org.spa.view.table.TableManager;
import org.spa.view.util.Controls;
import org.spa.view.util.Dialogs;
import org.spa.view.util.ImagesCache;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.spa.driver.SPAMain.FRAME_ICON_NAME;
import static org.spa.view.HomePage.MAGNIFYING_IMAGE;

public class OrdersView {
   private static final Logger logger = LoggerFactory.getLogger(OrdersView.class);
   JFrame frame;

   public OrdersView(Window parent) {
      frame = new JFrame("Orders");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setIconImage(ImagesCache.getInstance().getImage(FRAME_ICON_NAME).getImage());
      frame.setPreferredSize(new Dimension(parent.getPreferredSize().width - 200, parent.getPreferredSize().height - 100));
      frame.add(new ManagementViewPane());
      frame.pack();
      Controls.centerDialog(parent, frame);
      frame.setVisible(true);
   }

   public static class TitlePane extends JPanel {

      public TitlePane() {
         setLayout(new GridBagLayout());
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridx = 0;
         gbc.gridy = 0;

         JLabel managerActions = new JLabel("Orders");
         managerActions.setFont(new Font("Arial", Font.PLAIN, 30));
         gbc.fill = GridBagConstraints.HORIZONTAL;
         add(managerActions, gbc);
      }
   }

   public class ManagementViewPane extends JPanel {
      private TitlePane title;
      private OrdersPanel ordersPanel;

      public ManagementViewPane() {
         setLayout(new GridBagLayout());
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.weightx = 1;
         gbc.weighty = 0.04;
         gbc.anchor = GridBagConstraints.WEST;
         gbc.fill = GridBagConstraints.BOTH;
         gbc.insets = new Insets(4, 4, 4, 4);
         add((title = new TitlePane()), gbc);
         gbc.gridy++;
         gbc.gridy++;
         gbc.weighty = 0.92;
         add((ordersPanel = new OrdersPanel()), gbc);
      }
   }

   public static OrderViewInfo orderToOrderViewInfo(Order order) {
      if (order == null) {
         return null;
      }

      return new OrderViewInfo(order.getOrderId(), order.getOrderTime(), order.getUserId(), order.getItems());
   }

   public class OrdersPanel extends JPanel {
      JTextField searchBar;
      JButton findOrderBtn;
      boolean isAdmin;
      User loggedInUser;
      private TableManager<OrderColumn, OrderViewInfo> tableManager;
      private List<OrderViewInfo> tableModelList;
      private OrderSystem orderSystem;

      public OrdersPanel() {
         loggedInUser = SPAApplication.getInstance().getUserManagementService().getLoggedInUser();
         isAdmin = SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() == UserType.Admin ||
               SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() == UserType.SysAdmin;

         setLayout(new GridBagLayout());
         setBorder(new CompoundBorder(new TitledBorder("Orders"), new EmptyBorder(8, 0, 0, 0)));
         GridBagConstraints gbc = new GridBagConstraints();

         if (isAdmin) {
            // OrderPanel Layout
            JPanel inner = new JPanel();
            inner.setLayout(new OverlayLayout(inner));

            searchBar = new JTextField("Search by User/Order Id...");
            searchBar.setPreferredSize(new Dimension(200, 30));
            Image scaledImage = MAGNIFYING_IMAGE.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            findOrderBtn = new JButton(new ImageIcon(scaledImage));
            Controls.setFlatStyle(findOrderBtn, false);
            findOrderBtn.setPreferredSize(new Dimension(30, 30));
            inner.add(findOrderBtn);
            inner.add(searchBar);

            searchBar.addComponentListener(new ComponentAdapter() {
               @Override
               public void componentResized(ComponentEvent e) {
                  // Do it later cause somehow the getSize() retrieves the preferred size, which differs from the actual size
                  // and it might bring the button to a wrong location..
                  SwingUtilities.invokeLater(() -> {
                     if (!searchBar.getSize().equals(searchBar.getPreferredSize())) {
                        findOrderBtn.setLocation(searchBar.getSize().width - findOrderBtn.getPreferredSize().width - 10, 0);
                     }
                  });
               }
            });

            searchBar.addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent e) {
                  if ("Search by User/Order Id...".equalsIgnoreCase(searchBar.getText())) {
                     searchBar.setText("");
                  }

                  refreshTable();
               }
            });

            ActionListener searchActionListener = new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  String searchString = "(?i).*" + searchBar.getText() + ".*";
                  List<Order> searchedItems = new ArrayList<>();
                  orderSystem.getOrdersMap().values().forEach(order -> {
                     if ((order.getOrderId().matches(searchString)) || (order.getUserId().matches(searchString))) {
                        searchedItems.add(order);
                     }
                  });
                  tableModelList.clear();
                  searchedItems.forEach(order -> tableModelList.add(orderToOrderViewInfo(order)));
                  tableManager.refresh();
               }
            };
            findOrderBtn.addActionListener(searchActionListener);
            searchBar.addActionListener(searchActionListener);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1000;
            gbc.weighty = 1;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            add(inner, gbc);
         }
         // add Orders table
         createTable();
         gbc.anchor = GridBagConstraints.NORTHWEST;
         gbc.fill = GridBagConstraints.BOTH;
         gbc.weightx = 1000;
         gbc.weighty = 1000;
         gbc.gridwidth = 1;
         gbc.gridheight = 1;
         gbc.gridy++;
         add(tableManager.getMainPanel(), gbc);
      }

      private void refreshTable() {
         tableModelList.clear();
         if (isAdmin) {
            orderSystem.getOrdersMap().values().forEach(order -> tableModelList.add(orderToOrderViewInfo(order)));
         } else {
            List<Order> orders = SPAApplication.getInstance().getOrderSystem().findOrdersOfUser(loggedInUser.getUserId());
            orders.stream().forEach(order -> tableModelList.add(orderToOrderViewInfo(order)));
         }
         try {
            tableManager.refresh();
         } catch (Throwable t) {
            logger.error("Error has occurred while trying to refresh table.", t);
         }
      }

      public void createTable() {
         orderSystem = SPAApplication.getInstance().getOrderSystem();
         List<OrderColumn> orderCols = Arrays.asList(OrderColumn.OrderId, OrderColumn.OrderTime, OrderColumn.Summary, OrderColumn.UserId);
         TableConfig tableConfig = TableConfig.create().withLinesInRow(4).withEditable(true).withBorder(true).withColumnReordering(true).withColumnResizing(false).build();
         tableModelList = new ArrayList<>();
         tableManager = new TableManager<>(orderCols, tableModelList, tableConfig);
         tableManager.setFocusedRowChangedListener((rowNumber, selectedModel) -> {
            logger.info("Selected model is: " + selectedModel);
            orderSystem.getSelectionModel().setSelection(selectedModel);
         });
         tableManager.setPopupAdapter(new PopupAdapter() {
            @Override
            protected java.util.List<JMenuItem> getMenuItemsForPopup() {
               JMenuItem item = new JMenuItem("View More...");
               item.setDisplayedMnemonicIndex(0);
               item.addActionListener(e -> {
                  Order selection = orderSystem.getSelectionModel().getSelection();
                  SwingUtilities.invokeLater(() -> {
                     if (selection != null) {
                        new OrderInfoDialog(orderToOrderViewInfo(selection)).init().setVisible(true);
                     } else {
                        Dialogs.showInfoDialog(frame, "No selection. Nothing to show.\nPlease select a row first.", "No selection");
                     }
                  });
               });
               return Collections.singletonList(item);
            }
         });
         refreshTable();
      }
   }
}
