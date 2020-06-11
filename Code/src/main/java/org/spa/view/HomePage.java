package org.spa.view;

import org.spa.controller.SPAApplication;
import org.spa.controller.action.ActionException;
import org.spa.controller.action.ActionManager;
import org.spa.controller.action.ActionType;
import org.spa.controller.item.*;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.controller.user.User;
import org.spa.controller.user.UserManagementService;
import org.spa.controller.user.UserManagementServiceObserver;
import org.spa.controller.user.UserType;
import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;
import org.spa.view.alert.AlertsView;
import org.spa.view.cart.ShoppingCartView;
import org.spa.view.item.*;
import org.spa.view.login.LoginView;
import org.spa.view.login.Registration;
import org.spa.view.order.OrdersView;
import org.spa.view.report.ReportView;
import org.spa.view.table.PopupAdapter;
import org.spa.view.table.TableConfig;
import org.spa.view.table.TableManager;
import org.spa.view.util.Controls;
import org.spa.view.util.Dialogs;
import org.spa.view.util.Fonts;
import org.spa.view.util.ImagesCache;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.*;

import static org.spa.driver.SPAMain.getMainLogo;

public class HomePage extends JPanel implements SPAExplorerIfc<WarehouseItem>, UserManagementServiceObserver, ItemsWarehouseObserver {
   public static final int PAD = 10;
   public static final int HOME_PAGE_BUTTON_IMAGE_SIZE = 50;
   public static final int HOME_PAGE_BUTTON_SIZE = 65;
   public static final ImageIcon MAGNIFYING_IMAGE = ImagesCache.getInstance().getImage("magnifying-icon.png");
   private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
   private final UserManagementService userManagement;
   private final JButton reports;
   private final JButton login;
   private final JButton logout;
   private final JButton orders;
   private final JButton register;
   private final JButton searchBtn;
   private final JCheckBox darkMode;
   private final JComboBox<String> sortTable;
   private final CategoryTree categoryTree;
   private final JTextField searchBar;
   private final JFrame mainForm;
   private final ShoppingCartView shoppingCart;
   private final AlertsView alerts;
   private final JLabel lblUsername;
   private final ImageIcon spaLogo;
   private final ItemsWarehouse itemsWarehouse;
   private final List<WarehouseItem> modifiedItemList;
   private TableManager<ItemColumn, ItemViewInfoHome> tableManager;
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
      categoryTree.getCategoryTree().setBorder(((JComponent) tableManager.getMainPanel().getComponent(0)).getBorder());
      modifiedItemList = new ArrayList<>();
      imageContainer.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            categoryTree.clear();
            modifiedItemList.clear();
            refreshTable();
         }
      });
      categoryTree.getCategoryTree().addTreeSelectionListener(evt -> {
         modifiedItemList.clear();
         TreePath newLeadSelectionPath = evt.getNewLeadSelectionPath();
         if (newLeadSelectionPath != null) {
            String node = newLeadSelectionPath.getLastPathComponent().toString();
            itemsWarehouse.getItems().forEach(item -> {
               if (item.getCategory().matches(node)) {
                  modifiedItemList.add(item);
               }
            });
            tableModelList.clear();
            modifiedItemList.forEach(item -> tableModelList.add(new ItemViewInfoHome(new ItemViewInfo(item))));
            tableManager.refresh();
         }
      });

      image = ImagesCache.getInstance().getImage("register-icon.png");
      scaledImage = image.getImage().getScaledInstance(HOME_PAGE_BUTTON_IMAGE_SIZE, HOME_PAGE_BUTTON_IMAGE_SIZE, Image.SCALE_SMOOTH);
      register = new JButton(new ImageIcon(scaledImage));
      register.setToolTipText("Add user");
      Controls.setComponentSize(register, HOME_PAGE_BUTTON_SIZE, HOME_PAGE_BUTTON_SIZE);
      Controls.setFlatStyle(register);
      image = ImagesCache.getInstance().getImage("reports-icon.png");
      scaledImage = image.getImage().getScaledInstance(HOME_PAGE_BUTTON_IMAGE_SIZE, HOME_PAGE_BUTTON_IMAGE_SIZE, Image.SCALE_SMOOTH);
      reports = new JButton(new ImageIcon(scaledImage));
      reports.setToolTipText("View reports");
      Controls.setComponentSize(reports, HOME_PAGE_BUTTON_SIZE, HOME_PAGE_BUTTON_SIZE);
      Controls.setFlatStyle(reports);
      image = ImagesCache.getInstance().getImage("order-history-icon.png");
      scaledImage = image.getImage().getScaledInstance(HOME_PAGE_BUTTON_IMAGE_SIZE, HOME_PAGE_BUTTON_IMAGE_SIZE, Image.SCALE_SMOOTH);
      orders = new JButton(new ImageIcon(scaledImage));
      orders.setToolTipText("View orders");
      Controls.setComponentSize(orders, HOME_PAGE_BUTTON_SIZE, HOME_PAGE_BUTTON_SIZE);
      Controls.setFlatStyle(orders);
      image = ImagesCache.getInstance().getImage("logout-icon.png");
      scaledImage = image.getImage().getScaledInstance(HOME_PAGE_BUTTON_IMAGE_SIZE, HOME_PAGE_BUTTON_IMAGE_SIZE, Image.SCALE_SMOOTH);
      logout = new JButton(new ImageIcon(scaledImage));
      logout.setToolTipText("Logout");
      Controls.setComponentSize(logout, HOME_PAGE_BUTTON_SIZE + 2, HOME_PAGE_BUTTON_SIZE);
      Controls.setFlatStyle(logout);
      logout.setVisible(false);
      image = ImagesCache.getInstance().getImage("login-icon.png");
      scaledImage = image.getImage().getScaledInstance(HOME_PAGE_BUTTON_IMAGE_SIZE, HOME_PAGE_BUTTON_IMAGE_SIZE, Image.SCALE_SMOOTH);
      login = new JButton(new ImageIcon(scaledImage));
      login.setToolTipText("Login");
      Controls.setComponentSize(login, HOME_PAGE_BUTTON_SIZE + 2, HOME_PAGE_BUTTON_SIZE);
      Controls.setFlatStyle(login);
      login.addActionListener(e -> new LoginView(parent));
      logout.addActionListener(actionEvent -> {
         SPAApplication.getInstance().getUserManagementService().logout();
         logout.setVisible(false);
         reports.setVisible(false);
         login.setVisible(true);
         orders.setVisible(false);
         register.setVisible(false);
      });

      reports.addActionListener(actionEvent -> new ReportView(mainForm, "Order"));

      orders.addActionListener(actionEvent -> new OrdersView(mainForm));

      register.addActionListener(actionEvent -> new Registration(mainForm));

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
      ActionListener searchActionListener = e -> {
         modifiedItemList.clear();
         String searchString = "(?i).*" + searchBar.getText() + ".*";
         itemsWarehouse.getItems().forEach(item -> {
            if ((item.getName().matches(searchString)) || (item.getDescription().matches(searchString))) {
               modifiedItemList.add(item);
            }
         });
         tableModelList.clear();
         modifiedItemList.forEach(item -> tableModelList.add(new ItemViewInfoHome(new ItemViewInfo(item))));
         tableManager.refresh();
      };
      searchBtn.addActionListener(searchActionListener);
      searchBar.addActionListener(searchActionListener);
      searchBar.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            searchBar.setText("");
            modifiedItemList.clear();
            sortTable.setSelectedIndex(0);
         }
      });

      darkMode = new JCheckBox("Dark Mode");
      darkMode.setSelected(Controls.isDarkMode());
      darkMode.setToolTipText("You must restart the application so changes will take effect");
      darkMode.addChangeListener(e -> Controls.setIsDarkMode(darkMode.isSelected()));

      String[] sortArray = { "Sort by: None", "Price: Low to high", "Price: High to low", "Name: Alphabetically" };
      sortTable = new JComboBox<>(sortArray);
      if (Controls.isDarkMode()) {
         sortTable.setBackground(Color.black);
         sortTable.setForeground(Color.white);
      }
      sortTable.setFont(Fonts.PLAIN_FONT);
      sortTable.addActionListener(e -> {
         tableModelList.clear();
         if (modifiedItemList.size() == 0)
            modifiedItemList.addAll(itemsWarehouse.getItems());
         switch (sortTable.getSelectedIndex()) {
            case 1: { //Low to high
               modifiedItemList.sort(new SortbyPriceLowToHigh());
            }
            break;

            case 2: { //High to low
               modifiedItemList.sort(new SortbyPriceHighToLow());
               break;
            }
            case 3: { //Alphabetic
               modifiedItemList.sort((item1, item2) -> item1.getName().compareToIgnoreCase(item2.getName()));
               break;
            }
            default: {
               break;
            }
         }
         modifiedItemList.forEach(item -> tableModelList.add(new ItemViewInfoHome(new ItemViewInfo(item))));
         tableManager.refresh();
      });
      itemsWarehouse.registerObserver(this);
      add(sortTable);
      add(login);
      add(logout);
      add(lblUsername);
      add(darkMode);
      add(reports);
      add(orders);
      add(register);
      add(shoppingCart.getNavigatingComponent());
      add(alerts.getNavigatingComponent());
      add(categoryTree.getCategoryTree());
      add(tableManager.getMainPanel());
      add(searchBtn);
      add(searchBar);
      alerts.getNavigatingComponent().setVisible(false);
      reports.setVisible(false);
      orders.setVisible(false);
      register.setVisible(false);
      add(imageContainer);
      SpringLayout layout = new SpringLayout();
      this.setLayout(layout);
      componentLocation(layout, this, shoppingCart.getNavigatingComponent(),
            alerts.getNavigatingComponent(), imageContainer);
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
      boolean isAdmin = (loggedInUser.getUserType() == UserType.SysAdmin) || (loggedInUser.getUserType() == UserType.Admin);
      if (isAdmin) {
         alerts.getNavigatingComponent().setVisible(true);
         reports.setVisible(true);

         if (loggedInUser.getUserType() == UserType.SysAdmin) {
            register.setVisible(true);
         }
      }

      if (isAdmin || (loggedInUser.getUserType() == UserType.Customer)) {
         lblUsername.setText("Hello " + loggedInUser.getUserId());
         login.setVisible(false);
         logout.setVisible(true);
         orders.setVisible(true);
      } else {
         alerts.getNavigatingComponent().setVisible(false);
         lblUsername.setText("Hello guest     ");
      }
   }

   @Override
   public void deleteItem(WarehouseItem item) {
      modifiedItemList.clear();
      sortTable.setSelectedIndex(0);
   }

   @Override
   public void updateItem(WarehouseItem item) {
      modifiedItemList.clear();
      sortTable.setSelectedIndex(0);
   }

   @Override
   public void addItem(WarehouseItem item) {
      modifiedItemList.clear();
      sortTable.setSelectedIndex(0);
   }

   public void componentLocation(SpringLayout layout, Container contentPane, Component cart, Component alerts, Component imageContainer) {
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
      layout.putConstraint(SpringLayout.NORTH, reports, 0, SpringLayout.NORTH, contentPane);
      layout.putConstraint(SpringLayout.WEST, reports, -reports.getPreferredSize().width, SpringLayout.WEST, orders);
      layout.putConstraint(SpringLayout.NORTH, alerts, 0, SpringLayout.NORTH, contentPane);
      layout.putConstraint(SpringLayout.WEST, alerts, -alerts.getPreferredSize().width, SpringLayout.WEST, reports);
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
      layout.putConstraint(SpringLayout.NORTH, sortTable, 0, SpringLayout.NORTH, searchBar);
      layout.putConstraint(SpringLayout.WEST, sortTable, PAD, SpringLayout.EAST, searchBar);
      layout.putConstraint(SpringLayout.SOUTH, sortTable, 0, SpringLayout.SOUTH, searchBar);
   }

   private void createItemsTable() {
      List<ItemColumn> itemCols = Arrays.asList(ItemColumn.Image, ItemColumn.Name, ItemColumn.Description, ItemColumn.Price, ItemColumn.Cart);
      tableModelList = new ArrayList<>();
      TableConfig tableConfig = TableConfig.create().withLinesInRow(6).withEditable(true).withBorder(true).build();
      tableManager = new TableManager<>(itemCols, tableModelList, tableConfig);
      tableManager.setFocusedRowChangedListener((rowNumber, selectedModel) -> {
         logger.info("Selected model is: " + selectedModel);
         itemsWarehouse.getSelectionModel().setSelection(new WarehouseItem(selectedModel));
      });
      tableManager.setPopupAdapter(new PopupAdapter() {
         @Override
         protected List<JMenuItem> getBlankAreaMenuItemsForPopup() {
            if (isManager()) {
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
                     new ItemInfoDialog(new ItemViewInfoHome(new ItemViewInfo(selection))).init().setVisible(true);
                  } else {
                     Dialogs.showInfoDialog(getParentDialog(), "No selection. Nothing to show.\nPlease select a row first.", "No selection");
                  }
               });
            });
            if (isManager()) {
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
                     } else {
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
            } else
               return Arrays.asList(item);
         }
      });
      refreshTable();
   }

   private void refreshTable() {
      tableModelList.clear();
      itemsWarehouse.getItems().forEach(item -> tableModelList.add(new ItemViewInfoHome(new ItemViewInfo(item))));
      try {
         tableManager.refresh();
      } catch (Throwable t) {
         logger.error("Error has occurred while trying to refresh table.", t);
      }
   }

   public boolean isManager() {
      return (userManagement.getLoggedInUserType() == UserType.Admin || userManagement.getLoggedInUserType() == UserType.SysAdmin);
   }
}
