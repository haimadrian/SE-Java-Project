package org.spa.ui.cart;

import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.UserType;
import org.spa.controller.action.ActionException;
import org.spa.controller.action.ActionManager;
import org.spa.controller.action.ActionType;
import org.spa.controller.cart.ShoppingCart;
import org.spa.controller.cart.ShoppingCartObserver;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.ui.LoginView;
import org.spa.ui.SPAExplorerIfc;
import org.spa.ui.control.ButtonWithBadge;
import org.spa.ui.item.ItemColumn;
import org.spa.ui.item.ItemInfoDialog;
import org.spa.ui.item.ItemViewInfo;
import org.spa.ui.table.PopupAdapter;
import org.spa.ui.table.TableConfig;
import org.spa.ui.table.TableManager;
import org.spa.ui.util.Controls;
import org.spa.ui.util.Dialogs;
import org.spa.ui.util.Fonts;
import org.spa.ui.util.ImagesCache;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.*;

import static org.spa.ui.item.ItemCopying.itemViewInfoToWarehouseItem;
import static org.spa.ui.item.ItemCopying.warehouseItemToItemViewInfo;
import static org.spa.ui.util.Controls.createButton;

/**
 * The main view of the shopping cart system<br/>
 * This view contains a {@link TableManager} that display the items there are in {@link ShoppingCart}. In addition, we manage
 * a button-with-a-badge here, in order to update its badge with the amount of items in the shopping cart.
 * @author Haim Adrian
 * @since 22-May-20
 */
public class ShoppingCartView implements SPAExplorerIfc<WarehouseItem>, ShoppingCartObserver {
   private static final Logger logger = LoggerFactory.getLogger(ShoppingCartView.class);

   private JDialog shoppingCartDialog;
   private final Window parent;
   private final ButtonWithBadge shoppingCartButton;
   private final ShoppingCart shoppingCart;

   private JPanel workArea;
   private JLabel title;
   private JButton continueButton;
   private JButton clearCartButton;

   /**
    * The table where we display items at
    */
   private TableManager<ItemColumn, ItemViewInfo> tableManager;

   /**
    * Hold the list of the table here so we can update it outside table manager and then call refresh table.
    */
   private List<ItemViewInfo> tableModelList;

   private final ActionListener loginActionListener;
   private final ActionListener placeOrderActionListener;

   public ShoppingCartView(Window parent) {
      this.parent = parent;
      shoppingCart = SPAApplication.getInstance().getShoppingCart();
      shoppingCart.registerObserver(this);

      shoppingCartButton = new ButtonWithBadge(ImagesCache.getInstance().getImage("shopping-cart-icon.png"));
      shoppingCartButton.setSize(70, 70);
      shoppingCartButton.setCountForBadge(shoppingCart.count());
      shoppingCartButton.addActionListener(e -> {
         logger.info("Opening Shopping Cart");
         show();
      });

      loginActionListener = e -> new LoginView(this.parent);
      placeOrderActionListener = e -> {
         if (shoppingCart.count() == 0) {
            Dialogs.showInfoDialog(getParentDialog(), "Cart is empty. Please go to home page and select some items", "Empty Cart");
            return;
         }

         Dialogs.executeWithWaitingDialog(() -> {
                  // Save order
                  try {
                     ActionManager.executeAction(ActionType.Purchase);
                     SwingUtilities.invokeLater(() -> {
                        Dialogs.showInfoDialog(getParentDialog(), "Order has been placed.\nShop will contact you within few hours for payment details.", "Order completed");
                        SwingUtilities.invokeLater(this::close);
                     });
                  } catch (Exception e1) {
                     logger.error("Error has occurred while saving order.", e1);
                     SwingUtilities.invokeLater(() -> Dialogs.showErrorDialog(getParentDialog(), "Error has occurred while placing order: " + e1.getMessage(), "Error"));
                  }
               },
               getParentDialog(),
               "Placing your order...");
      };

      SPAApplication.getInstance().getUserManagementService().registerObserver(loggedInUser -> {
         if (SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() == UserType.Guest) {
            continueButton.setText("Login");
            continueButton.removeActionListener(placeOrderActionListener);
            continueButton.removeActionListener(loginActionListener);
            continueButton.addActionListener(loginActionListener);
         } else {
            continueButton.setText("Place Order");
            continueButton.removeActionListener(placeOrderActionListener);
            continueButton.removeActionListener(loginActionListener);
            continueButton.addActionListener(placeOrderActionListener);
         }
      });

      initUI();
   }

   private void initUI() {
      title = Controls.createTitle("Shopping Cart");
      createItemsTable();
      JPanel buttons = createWorkAreaButtons();

      workArea = new JPanel();
      workArea.setLayout(new BoxLayout(workArea, BoxLayout.PAGE_AXIS));
      workArea.add(title);
      workArea.add(Box.createRigidArea(new Dimension(0,10)));
      workArea.add(tableManager.getMainPanel());
      workArea.add(Box.createRigidArea(new Dimension(0,5)));
      workArea.add(buttons);
   }

   private JPanel createWorkAreaButtons() {
      if (SPAApplication.getInstance().getUserManagementService().getLoggedInUserType() == UserType.Guest) {
         continueButton = createButton("Login", loginActionListener, true);
      } else {
         continueButton = createButton("Place Order", placeOrderActionListener, true);
      }

      clearCartButton = createButton("Clear Cart", e -> {
               if (shoppingCart.count() == 0) {
                  return;
               }

               if (Dialogs.showQuestionDialog(getParentDialog(), "This action will remove all items from cart.\nContinue?.", "Clear Cart")) {
                  try {
                     ActionManager.executeAction(ActionType.ClearCart);
                  } catch (ActionException actionException) {
                     SwingUtilities.invokeLater(() -> Dialogs.showErrorDialog(getParentDialog(), actionException.getMessage(), "Error"));
                  }
               }
            },
            false);

      JPanel buttonsPanel = new JPanel();
      buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
      buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      buttonsPanel.add(Box.createHorizontalGlue());
      buttonsPanel.add(clearCartButton);
      buttonsPanel.add(Box.createRigidArea(new Dimension(10,0)));
      buttonsPanel.add(continueButton);

      return buttonsPanel;
   }

   private void createItemsTable() {
      List<ItemColumn> itemCols = Arrays.asList(ItemColumn.Image, ItemColumn.Name, ItemColumn.Description, ItemColumn.Price, ItemColumn.Count);
      tableModelList = new ArrayList<>();
      TableConfig tableConfig = TableConfig.create().withLinesInRow(6).withEditable(true).withBorder(true).withColumnReordering(true).withColumnResizing(true).build();

      tableManager = new TableManager<>(itemCols, tableModelList, tableConfig);
      tableManager.setFocusedRowChangedListener((rowNumber, selectedModel) -> shoppingCart.getSelectionModel().setSelection(itemViewInfoToWarehouseItem(selectedModel)));
      tableManager.setPopupAdapter(new PopupAdapter() {
         @Override
         protected List<JMenuItem> getMenuItemsForPopup() {
            JMenuItem item = new JMenuItem("View More...");
            item.setDisplayedMnemonicIndex(0);
            item.setFont(Fonts.PLAIN_FONT);
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
            item2.setFont(Fonts.PLAIN_FONT);
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
                           SwingUtilities.invokeLater(() -> Dialogs.showErrorDialog(getParentDialog(), actionException.getMessage(), "Error"));
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
      shoppingCart.getItems().forEach(item -> tableModelList.add(warehouseItemToItemViewInfo(item)));

      try {
         tableManager.refresh();
      } catch (Throwable t) {
         logger.error("Error has occurred while trying to refresh table.", t);
      }
   }

   @Override
   public void show() {
      SPAApplication.getInstance().getSelectionModel().setSelection(this);
      if (shoppingCartDialog == null) {
         shoppingCartDialog = new JDialog(parent, "Shopping Cart");
         shoppingCartDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         Dimension parentSize = parent.getPreferredSize();
         shoppingCartDialog.setPreferredSize(new Dimension(parentSize.width - 200, parentSize.height - 100));
         SwingUtilities.invokeLater(() -> shoppingCartDialog.setPreferredSize(new Dimension(parentSize.width - 150, parentSize.height - 100)));

         shoppingCartDialog.setContentPane(getMainContainer());
         shoppingCartDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               logger.info("Closing Shopping Cart");
               shoppingCartDialog.remove(getMainContainer());
               shoppingCartDialog = null;
            }
         });
         shoppingCartDialog.pack();
         Controls.centerDialog(shoppingCartDialog);
         shoppingCartDialog.setVisible(true);
      }
   }

   @Override
   public void close() {
      if (shoppingCartDialog != null) {
         shoppingCartDialog.dispatchEvent(new WindowEvent(shoppingCartDialog, WindowEvent.WINDOW_CLOSING));
      }
      SPAApplication.getInstance().getSelectionModel().setSelection(null);
   }

   @Override
   public void updateTitle(String newTitle) {
      title.setText(newTitle);
      if (shoppingCartDialog != null) {
         shoppingCartDialog.setTitle(newTitle);
      }
   }

   @Override
   public SelectionModelManager<WarehouseItem> getSelectionModel() {
      return shoppingCart.getSelectionModel();
   }

   @Override
   public void updateStatus(String text) {

   }

   @Override
   public JComponent getNavigatingComponent() {
      return shoppingCartButton;
   }

   @Override
   public Container getMainContainer() {
      return workArea;
   }

   @Override
   public Window getParentDialog() {
      return shoppingCartDialog;
   }

   @Override
   public void itemAdded(ShoppingCart cart, WarehouseItem item) {
      shoppingCartButton.setCountForBadge(cart.count());
      refreshTable();
   }

   @Override
   public void itemRemoved(ShoppingCart cart, WarehouseItem item) {
      shoppingCartButton.setCountForBadge(cart.count());
      refreshTable();
   }

   @Override
   public void itemCountUpdated(ShoppingCart cart, WarehouseItem item, int oldCount, int newCount) {
      shoppingCartButton.setCountForBadge(cart.count());
      refreshTable();
   }
}
