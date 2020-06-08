package org.spa.ui.order;

import org.spa.common.SPAApplication;
import org.spa.common.User;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.UserType;
import org.spa.controller.order.OrderSystem;
import org.spa.model.Order;
import org.spa.ui.table.PopupAdapter;
import org.spa.ui.table.TableConfig;
import org.spa.ui.table.TableManager;
import org.spa.ui.util.Controls;
import org.spa.ui.util.Dialogs;
import org.spa.ui.util.ImagesCache;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.spa.main.SPAMain.FRAME_ICON_NAME;
import static org.spa.ui.HomePage.MAGNIFYING_IMAGE;
import static org.spa.ui.order.OrderCopying.orderToOrderViewInfo;
import static org.spa.ui.order.OrderCopying.orderViewInfoToOrder;

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

    public class TitlePane extends JPanel {

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

    public class OrdersPanel extends JPanel{
        private TableManager<OrderColumn, OrderViewInfo> tableManager;
        private List<OrderViewInfo> tableModelList;
        private OrderSystem orderSystem;
        JTextField searchBar;
        JButton findOrderBtn;
        boolean isAdmin;
        User loggedInUser;
        public OrdersPanel() {
            loggedInUser = SPAApplication.getInstance().getUserManagementService().getLoggedInUser();
            isAdmin=(SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() == UserType.Admin ||
                    SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() == UserType.SysAdmin) ? true : false;

            setLayout(new GridBagLayout());
            setBorder(new CompoundBorder(new TitledBorder("Orders"), new EmptyBorder(8, 0, 0, 0)));
            GridBagConstraints gbc = new GridBagConstraints();
            // OrderPanel Layout
            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner, BoxLayout.X_AXIS));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1000;
            gbc.weighty = 1;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            add(inner, gbc);

            if (isAdmin) {
                searchBar = new JTextField("Search by User/Order Id...");
                Image scaledImage = MAGNIFYING_IMAGE.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                findOrderBtn= new JButton(new ImageIcon(scaledImage));
                findOrderBtn.setPreferredSize(new Dimension(30, 30));
                inner.add(searchBar);
                inner.add(findOrderBtn);
                searchBar.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        searchBar.setText("");
                        refreshTable();
                    }
                });
                findOrderBtn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String searchString = "(?i).*" + searchBar.getText() + ".*";
                        List<Order> searchedItems = new ArrayList<>();
                        orderSystem.getOrdersMap().values().stream().forEach(order -> {
                            if ((order.getOrderId().matches(searchString)) || (order.getUserId().matches(searchString))) {
                                searchedItems.add(order);
                            }
                        });
                        tableModelList.clear();
                        searchedItems.forEach(order -> tableModelList.add(orderToOrderViewInfo(order)));
                        tableManager.refresh();
                    }
                });
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
            if(isAdmin) {
                orderSystem.getOrdersMap().values().stream().forEach(order -> tableModelList.add(orderToOrderViewInfo(order)));
            }
            else {
                for (Order order  : orderSystem.getOrdersMap().values()) {
                    if(order.getUserId().equals(loggedInUser.getUserId())) {
                        tableModelList.add(orderToOrderViewInfo(order));
                    }
                }
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
                orderSystem.getSelectionModel().setSelection(orderViewInfoToOrder(selectedModel));
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
