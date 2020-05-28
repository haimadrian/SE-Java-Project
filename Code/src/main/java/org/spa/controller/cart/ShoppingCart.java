package org.spa.controller.cart;

import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.selection.SelectionModelManager;

import java.util.*;

/**
 * @author Haim Adrian
 * @since 22-May-20
 */
public class ShoppingCart {
   private static final Logger logger = LoggerFactory.getLogger(ShoppingCart.class);

   /**
    * We hold a different copy of warehouse items, so we can take advantage of the count property for an order, and not the count
    * of the warehouse which is global.
    */
   private final List<WarehouseItem> items;

   /**
    * To manage selected item in the shopping cart system
    */
   private final SelectionModelManager<WarehouseItem> selectionModel;

   private final Set<ShoppingCartObserver> observers;

   /**
    * Constructs a new {@link ShoppingCart}
    */
   public ShoppingCart() {
      items = new ArrayList<>();
      selectionModel = new SelectionModelManager<>();
      observers = new HashSet<>();
   }

   /**
    * Stop the shopping cart system and revert everything in it back to the warehouse.
    */
   public void stop() {
      clear(true);
   }

   /**
    * @return The {@link SelectionModelManager} of shopping cart, to get focused item in shopping cart
    */
   public SelectionModelManager<WarehouseItem> getSelectionModel() {
      return selectionModel;
   }

   /**
    * @return Items in shopping cart. The list is unmodifiable.
    */
   public List<WarehouseItem> getItems() {
      return Collections.unmodifiableList(items);
   }

   /**
    * Summarizes all counts of the items in cart
    * @return How many items in cart
    */
   public int count() {
      int count = 0;
      for (WarehouseItem item : items) {
         count += item.getCount();
      }
      return count;
   }

   /**
    * Clears the shopping cart
    * @param revertWarehouseCount Whether to add the count back to the items warehouse (cart cleared) or not (cart purchased)
    */
   public void clear(boolean revertWarehouseCount) {
      if (!items.isEmpty()) {
         // Check if we need to add the counts back to the warehouse.
         if (revertWarehouseCount) {
            logger.info("Reverting cart back to warehouse.");

            // This also notifies observer so it will update itself. We work on a copy because we should not modify a collection while iterating it
            new ArrayList<>(items).forEach(item -> remove(item.getId()));
         } else {
            new ArrayList<>(items).forEach(item -> {
               items.remove(item);
               notifyItemRemoved(item);
            });
         }

         items.clear();
         selectionModel.setSelection(null);
      }
   }

   /**
    * Add an item to shopping cart
    * @param itemId The item to add (its identifier)
    * @param count How many items of this kind
    * @throws ShoppingCartException In cae the identifier does not exist in warehouse, or if the count is bigger than the count in warehouse
    */
   public void add(String itemId, int count) throws ShoppingCartException {
      // First check if item already presents in shopping cart, so we will just update its count
      if (items.stream().anyMatch(item -> item.getId().equals(itemId))) {
         updateCount(itemId, count);
      } else {
         ItemsWarehouse itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
         WarehouseItem item = itemsWarehouse.getItem(itemId);
         if (item == null) {
            throw new ShoppingCartException("Item with ID " + itemId + " cannot be found.");
         }

         if (item.getCount() < count) {
            throw new ShoppingCartException("Requested count is above the allowed number. Current count in stock is: " + item.getCount());
         }

         // Reserve the count
         itemsWarehouse.updateAmountIfPresent(itemId, item.getCount() - count);

         // Override the count to reflect the amount of this item in the cart
         item.setCount(count);

         items.add(item);

         // Set last added item in focus
         selectionModel.setSelection(item);

         notifyItemAdded(item);
      }
   }

   /**
    * Remove an item from shopping cart
    * @param itemId Identifier of the item to remove
    */
   public void remove(String itemId) {
      logger.info("Removing item from cart. Item: " + itemId);
      Optional<WarehouseItem> item = items.stream().filter(it -> it.getId().equals(itemId)).findFirst();

      // Do not notify in case the item was not in cart in the first place
      if (item.isPresent()) {
         int index = items.indexOf(item.get());
         items.remove(item.get());

         // Add the count back to warehouse
         ItemsWarehouse itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
         WarehouseItem warehouseItem = itemsWarehouse.getItem(itemId);
         itemsWarehouse.updateAmountIfPresent(itemId, warehouseItem.getCount() + item.get().getCount());

         // Update the selection if the removed item was in focus.
         if (item.get().equals(selectionModel.getSelection())) {
            // Make sure we do not get out of bounds of the list.
            if (index >= items.size()) {
               index = items.size() - 1;
            }
            selectionModel.setSelection(index >= 0 ? items.get(index) : null);
         }

         notifyItemRemoved(item.get());
      }
   }

   /**
    * Update the count of an item which is in the cart to a new value. In case the item is not in cart, we will
    * redirect the method to {@link #add(String, int)}, as the item is first added to cart now. In case the new count is 0,
    * we will redirect the method to {@link #remove(String)}
    * @param itemId Identifier of the item to update
    * @param newCount The new value
    * @throws ShoppingCartException In case value was negative or if value was not in cart and the add method failed.
    */
   public void updateCount(String itemId, int newCount) throws ShoppingCartException {
      if (newCount < 0) {
         throw new ShoppingCartException("Count cannot be less than zero. Ignored value was: " + newCount);
      }

      // Zero means remove
      if (newCount == 0) {
         remove(itemId);
      } else {
         Optional<WarehouseItem> item = items.stream().filter(it -> it.getId().equals(itemId)).findFirst();
         if (!item.isPresent()) {
            // In case item is not in cart, add it. This is not an update.
            add(itemId, newCount);
         } else {
            int oldCount = item.get().getCount();

            ItemsWarehouse itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
            WarehouseItem warehouseItem = itemsWarehouse.getItem(itemId);
            if (warehouseItem == null) {
               throw new ShoppingCartException("Item with ID " + itemId + " cannot be found.");
            }

            int countInStock = warehouseItem.getCount() + oldCount;
            if (countInStock < newCount) {
               throw new ShoppingCartException("Requested count is above the allowed number. Current count in stock is: " + countInStock);
            }

            // Reserve the count
            itemsWarehouse.updateAmountIfPresent(itemId, countInStock - newCount);

            item.get().setCount(newCount);
            notifyItemUpdated(item.get(), oldCount);
         }
      }
   }

   /**
    * This method created in order to be able to get the id of an item in the cart by its row at the table
    * @param row The row index at the table
    * @return The item identifier at the specified row
    */
   public String getIdByRowIndex(int row) {
      return items.get(row).getId();
   }

   /**
    * Register yourself as an observer of the shopping cart, to get notified when there is any change in the shopping cart (item added/removed)
    * @param observer An observer to be notified upon changes
    * @see ShoppingCartObserver
    */
   public void registerObserver(ShoppingCartObserver observer) {
      observers.add(observer);
   }

   public void unregisterObserver(ShoppingCartObserver observer) {
      observers.remove(observer);
   }

   private void notifyItemAdded(WarehouseItem item) {
      for (ShoppingCartObserver observer : observers) {
         observer.itemAdded(this, item);
      }
   }

   private void notifyItemRemoved(WarehouseItem item) {
      for (ShoppingCartObserver observer : observers) {
         observer.itemRemoved(this, item);
      }
   }

   private void notifyItemUpdated(WarehouseItem item, int oldCount) {
      for (ShoppingCartObserver observer : observers) {
         observer.itemCountUpdated(this, item, oldCount, item.getCount());
      }
   }
}
