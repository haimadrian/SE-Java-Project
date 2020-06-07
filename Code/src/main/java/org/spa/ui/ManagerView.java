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
    JPanel mainPanel;
    private JLabel managerActions;
    private JButton stockReport, orderReport, economicReport;
    private TableManager<OrderColumn, OrderViewInfo> tableManager;
    private List<OrderViewInfo> tableModelList;
    private OrderSystem orderSystem;
    private JTextField searchBar;
    private JButton findOrderBtn;

    public ManagerView() {
        frame = new JFrame("Management View");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(new Dimension(1000, 650));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.add(mainPanel);
        panelsLayout();
    }

    public void panelsLayout() {
        JPanel titlePanel = new JPanel();
        managerActions = new JLabel("Admin Actions");
        managerActions.setFont(new Font("Arial", Font.PLAIN, 30));
        titlePanel.add(managerActions);
        mainPanel.add(titlePanel);

        // reportsPanel Layout
        JPanel reportsPanel = new JPanel();
        stockReport = new JButton("Stock Report");
        reportsPanel.add(stockReport);
        orderReport = new JButton("Order Report");
        reportsPanel.add(orderReport);
        economicReport = new JButton("Economic Report");
        reportsPanel.add(economicReport);
        reportsPanel.setBorder(new CompoundBorder(new TitledBorder("Reports"), new EmptyBorder(0, 0, 0, 0)));
        mainPanel.add(reportsPanel);


        // ordersPanel Layout
        JPanel ordersPanel = new JPanel();
        ordersPanel.setBorder(new CompoundBorder(new TitledBorder("Orders"), new EmptyBorder(0, 0, 0, 0)));
        findOrderBtn = new JButton(ImagesCache.getInstance().getImage("Magnifying.png"));
        ordersPanel.add(findOrderBtn);

        searchBar = new JTextField("Search by User/Order Id...");
        ordersPanel.add(searchBar);
        // add Orders table
        createTable();

        ordersPanel.add(tableManager.getMainPanel());
        mainPanel.add(ordersPanel);

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
        tableManager.getMainPanel().setPreferredSize(new Dimension(frame.getWidth() - 100, frame.getHeight() - 300));
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


