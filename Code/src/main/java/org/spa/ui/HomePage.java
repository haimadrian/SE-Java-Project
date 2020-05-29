package org.spa.ui;

import org.spa.common.Repository;
import org.spa.common.SPAApplication;
import org.spa.common.User;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.UserManagementService;
import org.spa.controller.UserManagementServiceObserver;
import org.spa.controller.action.ActionException;
import org.spa.controller.action.ActionManager;
import org.spa.controller.action.ActionType;
import org.spa.controller.cart.ShoppingCart;
import org.spa.controller.cart.ShoppingCartException;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.main.SPAMain;
import org.spa.model.Item;
import org.spa.model.user.Admin;
import org.spa.model.user.Customer;
import org.spa.model.user.SystemAdmin;
import org.spa.ui.cart.ShoppingCartView;
import org.spa.ui.LoginView;
import org.spa.ui.item.ItemColumn;
import org.spa.ui.item.ItemInfoDialog;
import org.spa.ui.item.ItemViewInfo;
import org.spa.ui.table.PopupAdapter;
import org.spa.ui.table.TableConfig;
import org.spa.ui.table.TableManager;
import org.spa.ui.util.Dialogs;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

import static org.spa.ui.item.ItemCopying.itemViewInfoToWarehouseItem;
import static org.spa.ui.item.ItemCopying.warehouseItemToItemViewInfo;

public class HomePage extends JPanel implements SPAExplorerIfc<WarehouseItem>, UserManagementServiceObserver {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private JTable table;
    private JButton management;
    private JButton login;
    private JButton logout;
    private JTree categoryTree;
    private JTextField searchBar;
    private DefaultTableModel model;
    private JFrame mainForm;
    private ShoppingCartView shoppingCart;
    private JLabel lblUsername;
    private ImageIcon spaLogo;
    private final UserManagementService userManagement;
    private ItemsWarehouse itemsWarehouse;
    private TableManager<ItemColumn, ItemViewInfo> tableManager;
    private ArrayList<String> itemsPick;
    private List<ItemViewInfo> tableModelList;
    public HomePage(JFrame parent) throws FileNotFoundException {
        itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
        userManagement = SPAApplication.getInstance().getUserManagementService();
        userManagement.registerObserver(this);
        final String path = new File("src\\main\\resources\\org\\spa\\ui\\homepagestuff").getAbsolutePath();
        mainForm = parent;
        spaLogo = new ImageIcon(path + "\\SPALOGO_transparent_Small.png", "The best electronic store money can buy");
        categoryTree = new JTree();
        management = new JButton("Management");
        shoppingCart = new ShoppingCartView(mainForm);
        logout = new JButton("Logout");
        logout.setVisible(false);
        login = new JButton("Login");
        itemsPick= new ArrayList<>();
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginView lv = new LoginView();
                lv.LoginView();
            }

        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SPAApplication.getInstance().getUserManagementService().logout();
                logout.setVisible(false);
                management.setVisible(false);
                login.setVisible(true);
                try {
                    clearCartBeforeLogout();
                } catch (ShoppingCartException e) {
                    e.printStackTrace();
                }
            }
        });

        String[] columnNames = {"Barcode", "Picture", "Item name", "Description", "Price", "Cart", "Delete"};
        WarehouseItem[][] data = new WarehouseItem[0][columnNames.length - 1];
        model = new DefaultTableModel(data, columnNames) {
            @Override
            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        table = new JTable(model);
        lblUsername = new JLabel("Hello guest.");
        searchBar = new JTextField("Search for product...", 40);
        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchBar.setText("");
            }
        });
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                DefaultTableModel table1 = (DefaultTableModel) table.getModel();
                String searchString = "(?i).*" + searchBar.getText() + ".*";
                TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(table1);
                table.setRowSorter(tr);
                tr.setRowFilter(RowFilter.regexFilter(searchString));
                logger.info(searchString);
            }
        });
        try {
            addRepoToTable(model, path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        tableConfiguration(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(tableManager.getMainPanel());
        add(shoppingCart.getNavigatingComponent());

        createItemsTable();
        add(login);
        add(logout);
        add(categoryTree);
        add(searchBar);
        add(lblUsername);
        add(management);
        management.setVisible(false);
        scrollPane.setPreferredSize(new Dimension(725, 400));
        JLabel imageContainer = new JLabel(spaLogo);
        add(imageContainer);
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        ComponentLocation(layout, this, shoppingCart.getNavigatingComponent(), login, searchBar, scrollPane, categoryTree, imageContainer, lblUsername, management, logout);
        add(scrollPane);
    }

    public void tableConfiguration(JTable table) {
        table.setRowHeight(80);
        //   table.setRowMargin(50);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(0).setWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setWidth(100);
        table.getColumnModel().getColumn(2).setMinWidth(100);
        table.getColumnModel().getColumn(2).setMaxWidth(100);
        table.getColumnModel().getColumn(3).setWidth(300);
        table.getColumnModel().getColumn(3).setMinWidth(300);
        table.getColumnModel().getColumn(3).setMaxWidth(300);
        table.getColumnModel().getColumn(5).setWidth(100);
        table.getColumnModel().getColumn(5).setMinWidth(100);
        table.getColumnModel().getColumn(5).setMaxWidth(100);

        //Makes the text be in the center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 2; i < 5; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        ButtonColumn cart = new ButtonColumn(table, null, 5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ShoppingCart shoppingCart = SPAApplication.getInstance().getShoppingCart();
                if (!table.getColumnName(table.getEditingColumn()).equalsIgnoreCase("cart"))
                    return;
                int modelRow = table.getSelectedRow();
                String itemId = (String) ((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0);
                try {
                    WarehouseItem shoppingCartItem = shoppingCart.getItems().stream().filter(item -> item.getId().equals(itemId)).findFirst().orElse(null);
                    shoppingCart.add(itemId, shoppingCartItem == null ? 1 : shoppingCartItem.getCount() + 1);
                    itemsPick.add(itemId);
                } catch (ShoppingCartException ex) {
                    SwingUtilities.invokeLater(() -> Dialogs.showSimpleErrorDialog(null, ex.getMessage(), "Error"));
                }
            }
        }
        );
        ButtonColumn delete = new ButtonColumn(table, null, 6, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ShoppingCart shoppingCart = SPAApplication.getInstance().getShoppingCart();
                if (!table.getColumnName(table.getEditingColumn()).equalsIgnoreCase("delete"))
                    return;

                int modelRow = table.getSelectedRow();
                String itemId = (String) ((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0);
                try {
                    shoppingCart.add(itemId, 0);

                } catch (ShoppingCartException ex) {
                    SwingUtilities.invokeLater(() -> Dialogs.showSimpleErrorDialog(null, ex.getMessage(), "Error"));
                }
                ((DefaultTableModel) table.getModel()).removeRow(modelRow);
            }
        }
        );
        delete.setMnemonic(KeyEvent.VK_D);
        table.getColumn("Cart").setCellRenderer(cart);
        table.getColumn("Delete").setCellRenderer(delete);
    }

    public void clearCartBeforeLogout() throws ShoppingCartException {
        Iterator<String> itr=itemsPick.iterator();
        ShoppingCart shoppingCart = SPAApplication.getInstance().getShoppingCart();
       while (itr.hasNext()){
           shoppingCart.add(itr.next(), 0);
        }
    }

    @Override
    public void close() {
        getParentDialog().dispatchEvent(new WindowEvent(getParentDialog(), WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void updateTitle(String newTitle) {
        mainForm.setTitle(newTitle);
    }

    @Override
    public SelectionModelManager<WarehouseItem> getSelectionModel() {
        return null;
    }

    @Override
    public void updateStatus(String text) {
    }

    @Override
    public JComponent getNavigatingComponent() {
        return null;
    }

    @Override
    public Container getMainContainer() {
        return this;
    }

    @Override
    public Window getParentDialog() {
        return mainForm;
    }

    @Override
    public void userLogin(User loggedInUser) {
        if ((loggedInUser instanceof SystemAdmin)
                || (loggedInUser instanceof Admin))
            management.setVisible(true);
        if ((loggedInUser instanceof SystemAdmin)
                || (loggedInUser instanceof Admin)
                || (loggedInUser instanceof Customer)) {
            lblUsername.setText("Hello " + loggedInUser.getUserId() + ".");
            login.setVisible(false);
            logout.setVisible(true);
        } else lblUsername.setText("Hello guest.");
    }

    private static class JTableButtonRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton) value;
            return button;
        }
    }

    public void ComponentLocation(SpringLayout layout, Container contentPane, Component cart, Component login, Component searchBar
            , Component table, Component categoryTree, Component imageContainer, Component lblUsername,
                                  Component management, Component logout) {
        layout.putConstraint(SpringLayout.NORTH, management, 70, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, management, 150, SpringLayout.EAST, searchBar);
        layout.putConstraint(SpringLayout.NORTH, login, 40, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, login, 200, SpringLayout.EAST, searchBar);
        layout.putConstraint(SpringLayout.NORTH, logout, 40, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, logout, 195, SpringLayout.EAST, searchBar);
        layout.putConstraint(SpringLayout.NORTH, cart, 80, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, cart, 360, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, searchBar, 135, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, searchBar, 500, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, table, 200, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, table, 300, SpringLayout.WEST, categoryTree);
        layout.putConstraint(SpringLayout.NORTH, categoryTree, 200, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, categoryTree, 60, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, imageContainer, 40, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, imageContainer, 60, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, lblUsername, 45, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, lblUsername, 890, SpringLayout.NORTH, searchBar);
    }

    public void addRepoToTable(DefaultTableModel model, String path) throws FileNotFoundException {
        String id="";
        String category="";
        String imgName="";
        String name="";
        String description="";
        double price= 0;
        double profitPercent= 0;
        double discountPercent= 0;
        int count =0;
        Icon cart = new ImageIcon(path + "\\AddToCart1.png");
        Icon delete = new ImageIcon(path + "\\garbage.png");
        ItemsWarehouse warehouse = SPAApplication.getInstance().getItemsWarehouse();
        List<WarehouseItem> idToItem = warehouse.getItems();
        Iterator<WarehouseItem> i = idToItem.iterator();
        while(i.hasNext()) {
/*            id = i.next().getId();
            name = i.next().getName();
            category = i.next().getCategory();
            description = i.next().getDescription();
            price = i.next().getPrice();
            profitPercent = i.next().getProfitPercent();
            discountPercent = i.next().getDiscountPercent();
            count = i.next().getCount();*/
        }
        Icon icon = new ImageIcon(path + "\\" + name +".png");
        Object[] object = {id, icon, name, description, price, cart, delete};
        model.addRow(object);
    }


    private void createItemsTable() {
        List<ItemColumn> itemCols = Arrays.asList(ItemColumn.Image, ItemColumn.Name, ItemColumn.Description, ItemColumn.Count, ItemColumn.Price);
        tableModelList = new ArrayList<>();
        TableConfig tableConfig = TableConfig.create().withLinesInRow(6).withEditable(true).withBorder(false).build();

        tableManager = new TableManager<>(itemCols, tableModelList, tableConfig);
        tableManager.setFocusedRowChangedListener((rowNumber, selectedModel) -> shoppingCart.getSelectionModel().setSelection(itemViewInfoToWarehouseItem(selectedModel)));
        tableManager.setPopupAdapter(new PopupAdapter() {
            @Override
            protected List<JMenuItem> getMenuItemsForPopup() {
                JMenuItem item = new JMenuItem("View More...");
                item.setDisplayedMnemonicIndex(0);
                item.addActionListener(e -> {
                    WarehouseItem selection = shoppingCart.getSelectionModel().getSelection();
                    SwingUtilities.invokeLater(() -> {
                        if (selection != null) {
                            new ItemInfoDialog(warehouseItemToItemViewInfo(selection)).init().setVisible(true);
                        } else {
                            Dialogs.showInfoDialog(getParentDialog(), "No selection. Nothing to show.\nPlease select a row first.", "No selection");
                        }
                    });
                });
                JMenuItem item2 = new JMenuItem("Remove from cart");
                item2.setDisplayedMnemonicIndex(1);
                item2.addActionListener(e -> {
                    WarehouseItem selection = shoppingCart.getSelectionModel().getSelection();
                    SwingUtilities.invokeLater(() -> {
                        if (selection != null) {
                            if (Dialogs.showQuestionDialog(getParentDialog(), "Are you sure you want to remove item from cart?", "Confirmation")) {
                                logger.info("Removing item from cart. Item: " + selection);
                                try {
                                    Map<String, Object> params = new HashMap<>();
                                    params.put("itemId", selection.getId());
                                    ActionManager.executeAction(ActionType.RemoveFromCart, params);
                                } catch (ActionException actionException) {
                                    SwingUtilities.invokeLater(() -> Dialogs.showSimpleErrorDialog(getParentDialog(), actionException.getMessage(), "Error"));
                                }
                            }
                        } else {
                            Dialogs.showInfoDialog(getParentDialog(), "No selection. Nothing to remove.\nPlease select a row first.", "No selection");
                        }
                    });
                });

                return Arrays.asList(item, item2);
            }
        });
        refreshTable();
    }
    private void refreshTable() {
        // First clear the list and then add all items from shopping cart as view info models
        tableModelList.clear();
        itemsWarehouse.getItems().forEach(item -> tableModelList.add(warehouseItemToItemViewInfo(item)));

        try {
            tableManager.refresh();
        } catch (Throwable t) {
            logger.error("Error has occurred while trying to refresh table.", t);
        }
    }

}


