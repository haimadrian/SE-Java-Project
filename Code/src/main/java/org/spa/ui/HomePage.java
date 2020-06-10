package org.spa.ui;

import org.spa.common.SPAApplication;
import org.spa.common.User;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.UserManagementService;
import org.spa.controller.UserManagementServiceObserver;
import org.spa.controller.UserType;
import org.spa.controller.action.ActionException;
import org.spa.controller.action.ActionManager;
import org.spa.controller.action.ActionType;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.ItemsWarehouseObserver;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.model.user.Admin;
import org.spa.model.user.Customer;
import org.spa.model.user.SystemAdmin;
import org.spa.ui.alert.AlertsView;
import org.spa.ui.cart.ShoppingCartView;
import org.spa.ui.item.ItemColumn;
import org.spa.ui.item.ItemInfoDialog;
import org.spa.ui.item.ItemManagement;
import org.spa.ui.item.ItemViewInfoHome;
import org.spa.ui.login.LoginView;
import org.spa.ui.login.Registration;
import org.spa.ui.order.OrdersView;
import org.spa.ui.table.PopupAdapter;
import org.spa.ui.table.TableConfig;
import org.spa.ui.table.TableManager;
import org.spa.ui.util.Controls;
import org.spa.ui.util.Dialogs;
import org.spa.ui.util.Fonts;
import org.spa.ui.util.ImagesCache;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

import static org.spa.main.SPAMain.getMainLogo;
import static org.spa.ui.item.ItemCopying.itemViewInfoToWarehouseItem;
import static org.spa.ui.item.ItemCopying.warehouseItemToItemViewInfo;

public class HomePage extends JPanel implements SPAExplorerIfc<WarehouseItem>, UserManagementServiceObserver , ItemsWarehouseObserver {
    public static final int PAD = 10;
    public static final int HOME_PAGE_BUTTON_IMAGE_SIZE = 50;
    public static final int HOME_PAGE_BUTTON_SIZE = 65;
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    public static final ImageIcon MAGNIFYING_IMAGE = ImagesCache.getInstance().getImage("magnifying-icon.png");
    private JButton management;
    private JButton login;
    private JButton logout;
    private JButton orders;
    private JButton register;
    private JButton searchBtn;
    private JCheckBox darkMode;
    private CategoryTree categoryTree;
    private JTextField searchBar;
    private JFrame mainForm;
    private ShoppingCartView shoppingCart;
    private AlertsView alerts;
    private JLabel lblUsername;
    private ImageIcon spaLogo;
    private final UserManagementService userManagement;
    private ItemsWarehouse itemsWarehouse;
    private TableManager<ItemColumn, ItemViewInfoHome> tableManager;
    private ArrayList<String> itemsPick;
    private java.util.List<ItemViewInfoHome> tableModelList;

    public HomePage(JFrame parent) {
        itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
        userManagement = SPAApplication.getInstance().getUserManagementService();
        userManagement.registerObserver(this);
        createItemsTable();
        mainForm = parent;
        ImageIcon image = getMainLogo();
        Image scaledImage = image.getImage().getScaledInstance(225, 102, Image.SCALE_SMOOTH);
        spaLogo = new ImageIcon(scaledImage);
        JLabel imageContainer = new JLabel(spaLogo);
        imageContainer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        categoryTree = new CategoryTree();
        categoryTree.getCategoryTree().setBorder(((JComponent)tableManager.getMainPanel().getComponent(0)).getBorder());
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
            }
        });

        image = ImagesCache.getInstance().getImage("register-icon.png");
        scaledImage = image.getImage().getScaledInstance(HOME_PAGE_BUTTON_IMAGE_SIZE, HOME_PAGE_BUTTON_IMAGE_SIZE, Image.SCALE_SMOOTH);
        register = new JButton(new ImageIcon(scaledImage));
        Controls.setComponentSize(register, HOME_PAGE_BUTTON_SIZE, HOME_PAGE_BUTTON_SIZE);
        Controls.setFlatStyle(register);
        image = ImagesCache.getInstance().getImage("management-icon.png");
        scaledImage = image.getImage().getScaledInstance(HOME_PAGE_BUTTON_IMAGE_SIZE, HOME_PAGE_BUTTON_IMAGE_SIZE, Image.SCALE_SMOOTH);
        management = new JButton(new ImageIcon(scaledImage));
        Controls.setComponentSize(management, HOME_PAGE_BUTTON_SIZE, HOME_PAGE_BUTTON_SIZE);
        Controls.setFlatStyle(management);
        image = ImagesCache.getInstance().getImage("order-history-icon.png");
        scaledImage = image.getImage().getScaledInstance(HOME_PAGE_BUTTON_IMAGE_SIZE, HOME_PAGE_BUTTON_IMAGE_SIZE, Image.SCALE_SMOOTH);
        orders = new JButton(new ImageIcon(scaledImage));
        Controls.setComponentSize(orders, HOME_PAGE_BUTTON_SIZE, HOME_PAGE_BUTTON_SIZE);
        Controls.setFlatStyle(orders);
        image = ImagesCache.getInstance().getImage("logout-icon.png");
        scaledImage = image.getImage().getScaledInstance(HOME_PAGE_BUTTON_IMAGE_SIZE, HOME_PAGE_BUTTON_IMAGE_SIZE, Image.SCALE_SMOOTH);
        logout = new JButton(new ImageIcon(scaledImage));
        Controls.setComponentSize(logout, HOME_PAGE_BUTTON_SIZE + 2, HOME_PAGE_BUTTON_SIZE);
        Controls.setFlatStyle(logout);
        logout.setVisible(false);
        image = ImagesCache.getInstance().getImage("login-icon.png");
        scaledImage = image.getImage().getScaledInstance(HOME_PAGE_BUTTON_IMAGE_SIZE, HOME_PAGE_BUTTON_IMAGE_SIZE, Image.SCALE_SMOOTH);
        login = new JButton(new ImageIcon(scaledImage));
        Controls.setComponentSize(login, HOME_PAGE_BUTTON_SIZE + 2, HOME_PAGE_BUTTON_SIZE);
        Controls.setFlatStyle(login);
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
                orders.setVisible(false);
                register.setVisible(false);
            }
        });

        management.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new ManagerView(mainForm);
            }
        });

        orders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new OrdersView(mainForm);
            }
        });

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Registration(mainForm);
            }
        });

        shoppingCart = new ShoppingCartView(mainForm);
        alerts = new AlertsView(mainForm);
        Controls.setComponentSize(shoppingCart.getNavigatingComponent(), HOME_PAGE_BUTTON_SIZE, HOME_PAGE_BUTTON_SIZE);
        Controls.setComponentSize(alerts.getNavigatingComponent(), HOME_PAGE_BUTTON_SIZE, HOME_PAGE_BUTTON_SIZE);

        lblUsername = Controls.createLabel("Hello guest     ", Fonts.PANEL_HEADING_FONT);
        searchBar = new JTextField("Search for product...", 40);
        searchBar.setFont(Fonts.PLAIN_FONT);
        searchBar.setPreferredSize(new Dimension(40, 40));
        scaledImage = MAGNIFYING_IMAGE.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
        searchBtn = new JButton(new ImageIcon(scaledImage));
        Controls.setFlatStyle(searchBtn, false);
        searchBtn.setPreferredSize(new Dimension(40, 40));
        ActionListener searchActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = "(?i).*" + searchBar.getText() + ".*";
                List<WarehouseItem> searchedItems = new ArrayList<>();
                itemsWarehouse.getItems().forEach(item -> {
                    if ((item.getName().matches(searchString)) || (item.getDescription().matches(searchString))) {
                        searchedItems.add(item);
                    }
                });
                tableModelList.clear();
                searchedItems.forEach(item -> tableModelList.add(new ItemViewInfoHome(warehouseItemToItemViewInfo(item))));
                tableManager.refresh();
            }
        };
        searchBtn.addActionListener(searchActionListener);
        searchBar.addActionListener(searchActionListener);
        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchBar.setText("");
                refreshTable();
            }
        });

        darkMode = new JCheckBox("Dark Mode");
        darkMode.setSelected(Controls.isDarkMode());
        darkMode.setToolTipText("You must restart the application so changes will take effect");
        darkMode.addChangeListener(e -> Controls.setIsDarkMode(darkMode.isSelected()));

        itemsWarehouse.registerObserver(this);
        add(login);
        add(logout);
        add(lblUsername);
        add(darkMode);
        add(management);
        add(orders);
        add(register);
        add(shoppingCart.getNavigatingComponent());
        add(alerts.getNavigatingComponent());
        add(categoryTree.getCategoryTree());
        add(tableManager.getMainPanel());
        add(searchBtn);
        add(searchBar);
        alerts.getNavigatingComponent().setVisible(false);
        management.setVisible(false);
        orders.setVisible(false);
        register.setVisible(false);
        add(imageContainer);
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        ComponentLocation(layout, this, shoppingCart.getNavigatingComponent(),
                alerts.getNavigatingComponent(),imageContainer);
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
            if(loggedInUser instanceof SystemAdmin) {
                register.setVisible(true);
            }
        }
        if((loggedInUser instanceof SystemAdmin)
                || (loggedInUser instanceof Admin)
                || (loggedInUser instanceof Customer)) {
            lblUsername.setText("Hello " + loggedInUser.getUserId());
            login.setVisible(false);
            logout.setVisible(true);
            orders.setVisible(true);
        }
        else {
            alerts.getNavigatingComponent().setVisible(false);
            lblUsername.setText("Hello guest     ");
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

    public void ComponentLocation(SpringLayout layout, Container contentPane, Component cart, Component alerts, Component imageContainer) {
        layout.putConstraint(SpringLayout.NORTH, imageContainer, PAD, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, imageContainer, PAD, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, login, 0, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, login, -login.getPreferredSize().width, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, logout, 0, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, logout, -logout.getPreferredSize().width, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, cart, 0, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, cart, -cart.getPreferredSize().width, SpringLayout.WEST, login);
        layout.putConstraint(SpringLayout.NORTH, orders, 0, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, orders, -orders.getPreferredSize().width, SpringLayout.WEST, cart);
        layout.putConstraint(SpringLayout.NORTH, management, 0, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, management, -management.getPreferredSize().width, SpringLayout.WEST, orders);
        layout.putConstraint(SpringLayout.NORTH, alerts, 0, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, alerts, -alerts.getPreferredSize().width, SpringLayout.WEST, management);
        layout.putConstraint(SpringLayout.NORTH, register, 0, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, register, -register.getPreferredSize().width, SpringLayout.WEST, alerts);
        layout.putConstraint(SpringLayout.NORTH, lblUsername, 5, SpringLayout.SOUTH, login);
        layout.putConstraint(SpringLayout.WEST, lblUsername, -lblUsername.getPreferredSize().width, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, darkMode, 5, SpringLayout.SOUTH, lblUsername);
        layout.putConstraint(SpringLayout.WEST, darkMode, 0, SpringLayout.WEST, lblUsername);
        layout.putConstraint(SpringLayout.NORTH, categoryTree.getCategoryTree(), PAD, SpringLayout.SOUTH, imageContainer);
        layout.putConstraint(SpringLayout.WEST, categoryTree.getCategoryTree(), 0, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.EAST, categoryTree.getCategoryTree(), 250, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, categoryTree.getCategoryTree(), 0, SpringLayout.SOUTH, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, searchBar, 0, SpringLayout.SOUTH, imageContainer);
        layout.putConstraint(SpringLayout.WEST, searchBar, 0, SpringLayout.EAST, categoryTree.getCategoryTree());
        layout.putConstraint(SpringLayout.EAST, searchBar, 0, SpringLayout.WEST, alerts);
        layout.putConstraint(SpringLayout.NORTH, searchBtn, 0, SpringLayout.NORTH, searchBar);
        layout.putConstraint(SpringLayout.WEST, searchBtn, -searchBtn.getPreferredSize().width, SpringLayout.EAST, searchBar);
        layout.putConstraint(SpringLayout.SOUTH, searchBtn, 0, SpringLayout.SOUTH, searchBar);
        layout.putConstraint(SpringLayout.NORTH, tableManager.getMainPanel(), PAD, SpringLayout.SOUTH, imageContainer);
        layout.putConstraint(SpringLayout.WEST, tableManager.getMainPanel(), 0, SpringLayout.EAST, categoryTree.getCategoryTree());
        layout.putConstraint(SpringLayout.EAST, tableManager.getMainPanel(), 0, SpringLayout.EAST, contentPane);
        layout.putConstraint(SpringLayout.SOUTH, tableManager.getMainPanel(), 0, SpringLayout.SOUTH, contentPane);
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
            protected List<JMenuItem> getBlankAreaMenuItemsForPopup() {
                if(isManager()) {
                    JMenuItem addItem = new JMenuItem("Add Item");
                    addItem.setDisplayedMnemonicIndex(0);
                    addItem.setFont(Fonts.PLAIN_FONT);
                    addItem.addActionListener(e -> {
                        new ItemManagement();
                        logger.info("Adding item to itemswarehouse. Item: ");
                    });
                    return Arrays.asList(addItem);
                }
                return null;
            }
            @Override
            protected java.util.List<JMenuItem> getMenuItemsForPopup() {
                JMenuItem item = new JMenuItem("View More...");
                item.setDisplayedMnemonicIndex(0);
                item.addActionListener(e -> {
                    WarehouseItem selection = itemsWarehouse.getSelectionModel().getSelection();
                    SwingUtilities.invokeLater(() -> {
                        if (selection != null) {
                            new ItemInfoDialog(new ItemViewInfoHome(warehouseItemToItemViewInfo(selection))).init().setVisible(true);
                        } else {
                            Dialogs.showInfoDialog(getParentDialog(), "No selection. Nothing to show.\nPlease select a row first.", "No selection");
                        }
                    });
                });
                if(isManager()) {
                    JMenuItem item2 = new JMenuItem("Remove item");
                    JMenuItem item3 = new JMenuItem("Update item");
                    JMenuItem item4 = new JMenuItem("Add item");
                    item2.setDisplayedMnemonicIndex(1);
                    item3.setDisplayedMnemonicIndex(2);
                    item4.setDisplayedMnemonicIndex(3);
                    item2.setFont(Fonts.PLAIN_FONT);
                    item3.setFont(Fonts.PLAIN_FONT);
                    item4.setFont(Fonts.PLAIN_FONT);
                    item2.addActionListener(e -> {
                        WarehouseItem selection = itemsWarehouse.getSelectionModel().getSelection();
                        SwingUtilities.invokeLater(() -> {
                            if (selection != null) {
                                if (Dialogs.showQuestionDialog(getParentDialog(), "Are you sure you want to remove item from shop?", "Confirmation")) {
                                    logger.info("Removing item from itemswarehouse. Item: " + selection);
                                    try {
                                        Map<String, Object> params = new HashMap<>();
                                        params.put("itemId", selection.getId());
                                        ActionManager.executeAction(ActionType.DeleteItemFromWarehouse, params);
                                    } catch (ActionException actionException) {
                                        SwingUtilities.invokeLater(() -> Dialogs.showErrorDialog(getParentDialog(), actionException.getMessage(), "Error"));
                                }
                                }
                            } else {
                                Dialogs.showInfoDialog(getParentDialog(), "No selection. Nothing to remove.\nPlease select a row first.", "No selection");
                            }
                        });
                    });
                    item3.addActionListener(e -> {
                        WarehouseItem selection = itemsWarehouse.getSelectionModel().getSelection();
                        SwingUtilities.invokeLater(() -> {
                            if (selection != null) {
                                new ItemManagement(selection);
                                logger.info("Updating item in itemswarehouse. Item: " + selection);
                            }else {
                                Dialogs.showInfoDialog(getParentDialog(), "No selection. Nothing to remove.\nPlease select a row first.", "No selection");
                }
                        });
                    });
                    item4.addActionListener(e -> {
                        WarehouseItem selection = itemsWarehouse.getSelectionModel().getSelection();
                        SwingUtilities.invokeLater(() -> {
                            if (selection != null) {
                                new ItemManagement();
                                logger.info("Adding item from itemswarehouse. Item: " + selection);

                            } else {
                                Dialogs.showInfoDialog(getParentDialog(), "No selection. Nothing to remove.\nPlease select a row first.", "No selection");
                            }
                        });
                    });
                    return Arrays.asList(item, item2, item3, item4);
                }
                else
                    return Arrays.asList(item);
            }
        });
        refreshTable();
    }

    private void refreshTable() {
        tableModelList.clear();
        itemsWarehouse.getItems().forEach(item -> tableModelList.add(new ItemViewInfoHome(warehouseItemToItemViewInfo(item))));
        try {
            tableManager.refresh();
        } catch (Throwable t) {
            logger.error("Error has occurred while trying to refresh table.", t);
        }
    }

    public boolean isManager(){
        return (userManagement.getLoggedInUserType() == UserType.Admin || userManagement.getLoggedInUserType()==UserType.SysAdmin );
    }
}


