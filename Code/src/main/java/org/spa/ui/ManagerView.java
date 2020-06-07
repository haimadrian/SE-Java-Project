package org.spa.ui;

import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.order.OrderSystem;
import org.spa.model.Order;
import org.spa.ui.order.OrderColumn;
import org.spa.ui.order.OrderInfoDialog;
import org.spa.ui.order.OrderViewInfo;
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

import static org.spa.ui.order.OrderCopying.orderToOrderViewInfo;
import static org.spa.ui.order.OrderCopying.orderViewInfoToOrder;

public class ManagerView {
    private static final Logger logger = LoggerFactory.getLogger(ManagerView.class);
    JFrame frame;
  
    public ManagerView(Window parent) {

        frame = new JFrame("Management View");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(parent.getPreferredSize().width - 200, parent.getPreferredSize().height - 100));
        frame.add(new ManagementViewPane());
        frame.pack();
        Controls.centerDialog(parent, frame);
        frame.setVisible(true);
    }

    public class ManagementViewPane extends JPanel {

        private TitlePane title;
        private ReportsPane reportsPane;
        private OrdersPane ordersPane;


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
            add((reportsPane = new ReportsPane()), gbc);
            gbc.gridy++;
            gbc.weighty = 0.92;
            add((ordersPane = new OrdersPane()), gbc);
        }
    }

    public class TitlePane extends JPanel {


        public TitlePane() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;

            JLabel managerActions = new JLabel("Admin Actions");
            managerActions.setFont(new Font("Arial", Font.PLAIN, 30));
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(managerActions, gbc);
        }

    }

    public class ReportsPane extends JPanel {

        private JButton stockReport, orderReport, economicReport;
        private JLabel warehouse;

        public ReportsPane() {
            setLayout(new GridBagLayout());
            setBorder(new CompoundBorder(new TitledBorder("Reports"), new EmptyBorder(0, 0, 0, 0)));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;


            JPanel panel = new JPanel(new GridBagLayout());
            panel.add(new JLabel("Generate Report: "), gbc);
            gbc.gridx++;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 0, 0);
            panel.add((warehouse = new JLabel()), gbc);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(4, 4, 4, 4);
            add(panel, gbc);

            gbc.gridwidth = 1;
            gbc.weightx = 0.25;
            gbc.gridy++;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add((stockReport = new JButton("Stock Report")), gbc);
            gbc.gridx++;
            add((orderReport = new JButton("Order Report")), gbc);
            gbc.gridx++;
            add((economicReport = new JButton("Economic Report")), gbc);
        }
    }

    public class OrdersPane extends JPanel{
        private TableManager<OrderColumn, OrderViewInfo> tableManager;
        private List<OrderViewInfo> tableModelList;
        private OrderSystem orderSystem;
        JTextField searchBar;
        JButton findOrderBtn;

        public OrdersPane() {
            setLayout(new GridBagLayout());
            setBorder(new CompoundBorder(new TitledBorder("Orders"), new EmptyBorder(8, 0, 0, 0)));
            GridBagConstraints gbc = new GridBagConstraints();

            // OrderPanel Layout
            JPanel inner = new JPanel();
            inner.setLayout(new BoxLayout(inner, BoxLayout.X_AXIS));
            searchBar = new JTextField("Search by User/Order Id...");
            findOrderBtn = new JButton(ImagesCache.getInstance().getImage("Magnifying.png"));
            inner.add(searchBar);
            inner.add(findOrderBtn);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1000;
            gbc.weighty = 1;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            add(inner, gbc);

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
        private void refreshTable() {
            tableModelList.clear();
            orderSystem.getOrdersMap().values().stream().forEach(order -> tableModelList.add(orderToOrderViewInfo(order)));
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
