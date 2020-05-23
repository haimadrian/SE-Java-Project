package org.spa.ui.cart;

import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.cart.ShoppingCart;
import org.spa.controller.cart.ShoppingCartObserver;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.ui.ButtonWithBadge;
import org.spa.ui.SPAExplorerIfc;
import org.spa.ui.item.ItemColumn;
import org.spa.ui.item.ItemInfoDialog;
import org.spa.ui.item.ItemViewInfo;
import org.spa.ui.table.PopupAdapter;
import org.spa.ui.table.TableConfig;
import org.spa.ui.table.TableManager;
import org.spa.ui.util.Controls;
import org.spa.ui.util.Dialogs;
import org.spa.ui.util.ImagesCache;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


   public ShoppingCartView(Window parent) {
      this.parent = parent;

      shoppingCart = SPAApplication.getInstance().getShoppingCart();
      shoppingCart.registerObserver(this);

      shoppingCartButton = new ButtonWithBadge(ImagesCache.getInstance().getImage("shopping-cart-icon.png"));
      shoppingCartButton.setSize(70, 70);
      shoppingCartButton.setCountForBadge(shoppingCart.count());

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
      continueButton = createButton(" Place Order ", e ->
         Dialogs.showInfoDialog(getParentDialog(), "Order has been placed.\nShop will contact you within few hours for payment details.", "Order completed"),
            true);

      clearCartButton = createButton(" Clear Cart ", e -> {
               if (Dialogs.showQuestionDialog(getParentDialog(), "This action will remove all items from cart.\nContinue?.", "Clear Cart")) {
                  shoppingCart.clear(true);
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
                        shoppingCart.remove(selection.getId());
                     }
                  } else {
                     Dialogs.showInfoDialog(getParentDialog(), "No selection. Nothing to show.\nPlease select a row first.", "No selection");
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
      workArea.setVisible(true);
      SPAApplication.getInstance().getSelectionModel().setSelection(this);
   }

   @Override
   public void close() {
      workArea.setVisible(false);
      SPAApplication.getInstance().getSelectionModel().setSelection(null);
   }

   @Override
   public void updateTitle(String newTitle) {
      title.setText(newTitle);
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
      return parent;
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
