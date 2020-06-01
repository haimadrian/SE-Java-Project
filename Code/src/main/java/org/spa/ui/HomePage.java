package org.spa.ui;

import org.spa.common.SPAApplication;
import org.spa.common.User;
import org.spa.common.util.log.Logger;
import org.spa.controller.UserType;
import org.spa.controller.action.ActionException;
import org.spa.controller.action.ActionManager;
import org.spa.controller.action.ActionType;
import org.spa.controller.item.ItemsWarehouseObserver;
import org.spa.ui.util.Dialogs;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.UserManagementService;
import org.spa.controller.UserManagementServiceObserver;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.model.user.Admin;
import org.spa.model.user.Customer;
import org.spa.model.user.SystemAdmin;
import org.spa.ui.alert.AlertsView;
import org.spa.ui.cart.ShoppingCartView;
import org.spa.ui.item.ItemColumn;
import org.spa.ui.item.ItemInfoDialog;
import org.spa.ui.item.ItemViewInfo;
import org.spa.ui.table.PopupAdapter;
import org.spa.ui.table.TableConfig;
import org.spa.ui.table.TableManager;
import org.spa.ui.util.Fonts;
import org.spa.ui.util.ImagesCache;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;
import static org.spa.ui.item.ItemCopying.itemViewInfoToWarehouseItem;
import static org.spa.ui.item.ItemCopying.warehouseItemToItemViewInfo;

public class HomePage extends JPanel implements SPAExplorerIfc<WarehouseItem>, UserManagementServiceObserver , ItemsWarehouseObserver {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private JButton management;
    private JButton login;
    private JButton logout;
    private JButton searchBtn;
    private CategoryTree categoryTree;
    private JTextField searchBar;
    private JFrame mainForm;
    private ShoppingCartView shoppingCart;
    private AlertsView alerts;
    private JLabel lblUsername;
    private ImageIcon spaLogo;
    private final UserManagementService userManagement;
    private ItemsWarehouse itemsWarehouse;
    private TableManager<ItemColumn, ItemViewInfo> tableManager;
    private ArrayList<String> itemsPick;
    private java.util.List<ItemViewInfo> tableModelList;
    public HomePage(JFrame parent) {
        itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
        userManagement = SPAApplication.getInstance().getUserManagementService();
        userManagement.registerObserver(this);
        createItemsTable();
        mainForm = parent;
        spaLogo = ImagesCache.getInstance().getImage("SPALOGO_transparent_Small.png");
        JLabel imageContainer = new JLabel(spaLogo);
        imageContainer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        categoryTree = new CategoryTree(mainForm);
        imageContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                categoryTree.clear();
                refreshTable();
            }
        });

        categoryTree.getCategoryTree().addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                String node = evt.getNewLeadSelectionPath().getLastPathComponent().toString();
                //TODO filter table by node
                TreePath newLeadSelectionPath = evt.getNewLeadSelectionPath();
                if (newLeadSelectionPath != null) {
                    String node = newLeadSelectionPath.getLastPathComponent().toString();
                    //TODO filter table by node
                    List<WarehouseItem> selectedCategory = new ArrayList<>();
                    itemsWarehouse.getItems().stream().forEach(item -> {
                        if (item.getCategory().matches(node)) {
                            selectedCategory.add(item);
                        }
                    });
                    tableModelList.clear();
                    selectedCategory.forEach(item -> tableModelList.add(new ItemViewInfoHome(warehouseItemToItemViewInfo(item))));
                    tableManager.refresh();
                }
                List<WarehouseItem> selectedCategory = new ArrayList<>();
                itemsWarehouse.getItems().stream().forEach(item -> {
                    if (item.getCategory().matches(node)) {
                        selectedCategory.add(item);
                    }
                });
                tableModelList.clear();
                selectedCategory.forEach(item -> tableModelList.add(warehouseItemToItemViewInfo(item)));
                tableManager.refresh();
            }
        });
        management = new JButton("Management");
        shoppingCart = new ShoppingCartView(mainForm);
        alerts = new AlertsView(mainForm);
        logout = new JButton("Logout");
        logout.setVisible(false);
        login = new JButton("Login");
        itemsPick= new ArrayList<>();
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginView(parent);
            }

        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SPAApplication.getInstance().getUserManagementService().logout();
                logout.setVisible(false);
                management.setVisible(false);
                login.setVisible(true);
            }
        });
        lblUsername = new JLabel("Hello guest.");
        searchBar = new JTextField("Search for product...", 40);
        searchBtn = new JButton(ImagesCache.getInstance().getImage("Magnifying.png"));
        searchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchString = "(?i).*" + searchBar.getText() + ".*";
                List<WarehouseItem> searchedItems = new ArrayList<>();
                itemsWarehouse.getItems().stream().forEach(item -> {
                    if ((item.getName().matches(searchString)) || (item.getDescription().matches(searchString))) {
                        searchedItems.add(item);
                    }
                });
                tableModelList.clear();
                searchedItems.forEach(item -> tableModelList.add(warehouseItemToItemViewInfo(item)));
                tableManager.refresh();
            }
        });
        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchBar.setText("");
                refreshTable();
            }
        });
        itemsWarehouse.registerObserver(this);
        add(tableManager.getMainPanel());
        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchBar.setText("");
                refreshTable();
            }
        });

        add(tableManager.getMainPanel());
        add(shoppingCart.getNavigatingComponent());
        add(alerts.getNavigatingComponent());
        add(searchBtn);
        add(login);
        add(logout);
        add(categoryTree.getCategoryTree());
        add(searchBar);
        add(lblUsername);
        add(lblUsername);
        add(management);
        alerts.getNavigatingComponent().setVisible(false);
        management.setVisible(false);
        add(imageContainer);
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        ComponentLocation(layout, this, shoppingCart.getNavigatingComponent(),
                alerts.getNavigatingComponent(),imageContainer);
        int tableWidth = mainForm.getPreferredSize().width - 60 - 40 - categoryTree.getCategoryTree().getPreferredSize().width;
        tableManager.getMainPanel().setPreferredSize(new Dimension(tableWidth,mainForm.getPreferredSize().height-250));
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
        return itemsWarehouse.getSelectionModel();
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
                || (loggedInUser instanceof Admin)) {
            alerts.getNavigatingComponent().setVisible(true);
            management.setVisible(true);
        }
        if((loggedInUser instanceof SystemAdmin)
                || (loggedInUser instanceof Admin)
                || (loggedInUser instanceof Customer)) {
            lblUsername.setText("Hello " + loggedInUser.getUserId() + ".");
            login.setVisible(false);
            logout.setVisible(true);
        }
        else {
            alerts.getNavigatingComponent().setVisible(false);
            lblUsername.setText("Hello guest.");
        }
    }

    @Override
    public void deleteItem(WarehouseItem item) {
        refreshTable();
    }

    @Override
    public void updateItem(WarehouseItem item) {
        refreshTable();
    }

    @Override
    public void addItem(WarehouseItem item) {
        refreshTable();
    }

    public void ComponentLocation(SpringLayout layout, Container contentPane, Component cart, Component alerts, Component imageContainer)    {
        layout.putConstraint(SpringLayout.NORTH,management,70,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,management,150,SpringLayout.EAST,searchBar);
        layout.putConstraint(SpringLayout.NORTH,alerts,5,SpringLayout.SOUTH, management);
        layout.putConstraint(SpringLayout.WEST,alerts,0, SpringLayout.WEST, management);
        layout.putConstraint(SpringLayout.NORTH,login,40,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,login,200,SpringLayout.EAST,searchBar);
        layout.putConstraint(SpringLayout.NORTH,logout,40,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,logout,195,SpringLayout.EAST,searchBar);
        layout.putConstraint(SpringLayout.NORTH,cart,80,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,cart,360,SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH,searchBar,135,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,searchBar,500,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.NORTH,searchBtn,137,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,searchBtn,0,SpringLayout.EAST,searchBar);
        layout.putConstraint(SpringLayout.NORTH, tableManager.getMainPanel(),200, SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST, tableManager.getMainPanel(),10, SpringLayout.EAST, categoryTree.getCategoryTree());
        layout.putConstraint(SpringLayout.NORTH, categoryTree.getCategoryTree(),200,SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, categoryTree.getCategoryTree(),10,SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, imageContainer,40,SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, imageContainer,60,SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, lblUsername,45,SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, lblUsername,890,SpringLayout.NORTH, searchBar);
    }

    private void createItemsTable() {
        List<ItemColumn> itemCols = Arrays.asList(ItemColumn.Image, ItemColumn.Name, ItemColumn.Description, ItemColumn.Price, ItemColumn.Cart);
        tableModelList = new ArrayList<>();
        TableConfig tableConfig = TableConfig.create().withLinesInRow(6).withEditable(true).withBorder(true).build();
        tableManager = new TableManager<>(itemCols, tableModelList, tableConfig);
        tableManager.setFocusedRowChangedListener((rowNumber, selectedModel) -> {
            logger.info("Selected model is: " + selectedModel);
            itemsWarehouse.getSelectionModel().setSelection(itemViewInfoToWarehouseItem(selectedModel));
        });
        tableManager.setPopupAdapter(new PopupAdapter() {
            @Override
            protected java.util.List<JMenuItem> getMenuItemsForPopup() {
                JMenuItem item = new JMenuItem("View More...");
                item.setDisplayedMnemonicIndex(0);
                item.addActionListener(e -> {
                    WarehouseItem selection = itemsWarehouse.getSelectionModel().getSelection();
                    SwingUtilities.invokeLater(() -> {
                        if (selection != null) {
                            new ItemInfoDialog(warehouseItemToItemViewInfo(selection)).init().setVisible(true);
                        } else {
                            Dialogs.showInfoDialog(getParentDialog(), "No selection. Nothing to show.\nPlease select a row first.", "No selection");
                        }
                    });
                });
                if(userManagement.getLoggedInUserType() == UserType.Admin || userManagement.getLoggedInUserType()==UserType.SysAdmin ) {
                    JMenuItem item2 = new JMenuItem("Remove item");
                    item2.setDisplayedMnemonicIndex(1);
                    item2.setFont(Fonts.PLAIN_FONT);
                    item2.addActionListener(e -> {
                        WarehouseItem selection = itemsWarehouse.getSelectionModel().getSelection();
                        SwingUtilities.invokeLater(() -> {
                            if (selection != null) {
                                if (Dialogs.showQuestionDialog(getParentDialog(), "Are you sure you want to remove item from shop?", "Confirmation")) {
                                    logger.info("Removing item from itemswarehouse. Item: " + selection);
                                    itemsWarehouse.removeItem(selection.getId());
                                }
                            } else {
                                Dialogs.showInfoDialog(getParentDialog(), "No selection. Nothing to remove.\nPlease select a row first.", "No selection");
                            }
                        });
                    });
                    return Arrays.asList(item, item2);
                }
                else
                    return Arrays.asList(item);
            }
        });
        refreshTable();
    }

    private void refreshTable() {
        tableModelList.clear();
        itemsWarehouse.getItems().forEach(item -> tableModelList.add(warehouseItemToItemViewInfo(item)));
        try {
            tableManager.refresh();
        } catch (Throwable t) {
            logger.error("Error has occurred while trying to refresh table.", t);
        }
    }
}


