package org.spa.controller.item;

import org.spa.controller.Service;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;
import org.spa.model.Repository;
import org.spa.model.dal.ItemRepository;

import java.util.*;

/**
 * @author Haim Adrian
 * @since 16-May-20
 */
public class ItemsWarehouse implements Service {
   private static final Logger logger = LoggerFactory.getLogger(ItemsWarehouse.class);
   private final Map<String, WarehouseItem> idToItem;
   private final Repository<Item> itemRepository;
   private final SelectionModelManager<WarehouseItem> selectionModel;
   private final Set<ItemsWarehouseObserver> observers;

   public ItemsWarehouse() {
      idToItem = new HashMap<>(1000);
      itemRepository = new ItemRepository();
      selectionModel = new SelectionModelManager<>();
      observers = new HashSet<>();
   }

   public SelectionModelManager<WarehouseItem> getSelectionModel() {
      return selectionModel;
   }

   /**
    * Call this method to read data from storage
    */
   @Override
   public void start() {
      logger.info("Starting ItemsWarehouse - Select items from repository");

      // Load data into memory
      itemRepository.selectAll().forEach(item -> idToItem.put(item.getId(), new WarehouseItem(item)));
   }

   /**
    * Call this method when exiting the application, to save the in memory data to disk
    */
   @Override
   public void stop() {
      logger.info("Stopping ItemsWarehouse - Save items to repository");

      // Save data to repository
      itemRepository.saveAll(idToItem.values());
   }

   /**
    * Note that the result is a copy of the items in warehouse, so you cannot modify it by accessing this method. You mast use the
    * add/remove methods.
    *
    * @return All items in warehouse
    */
   public List<WarehouseItem> getItems() {
      // Disallow modifying items map outside the warehouse. The add/remove methods must be used
      // or we will have synchronization bugs.
      return new ArrayList<>(idToItem.values());
   }

   /**
    * Add a new item to the items warehouse. This will override any amount of this item in case it is already existing in the warehouse.
    *
    * @param id
    * @param name
    * @param category
    * @param description
    * @param price
    * @param profitPercent
    * @param discountPercent
    * @param amount
    */
   public void addItem(WarehouseItem warehouseItem) {
      idToItem.put(warehouseItem.getId(), warehouseItem);
      itemRepository.create(warehouseItem);
      logger.info("Item added to warehouse: " + warehouseItem);
      notifyItemAdded(warehouseItem);
   }

   /**
    * Completely remove an item from warehouse
    *
    * @param id The id of the item to remove
    * @return The removed item or <code>null</code> in case there is no item with the specified id
    */
   public WarehouseItem removeItem(String id) {
      WarehouseItem item = idToItem.remove(id);
      if (item != null) {
         itemRepository.delete(item);
         logger.info("Removed item from warehouse: " + item);
         notifyItemDeleted(item);
      }
      return item;
   }

   /**
    * Set new amount of an item in the warehouse
    *
    * @param id The id of the item to update
    * @param amount The new amount
    */
   public void updateAmountIfPresent(String id, int amount) {
      WarehouseItem item = idToItem.get(id);
      if (item != null) {
         int oldCount = item.getCount();
         if (oldCount == amount) {
            logger.debug(() -> "Tried to update amount of an item with the same value. oldCount=" + oldCount + ", newCount=" + amount);
         } else {
            item.setCount(amount);
            itemRepository.update(item);
            notifyItemUpdated(item);
            logger.info("Item count updated: " + item + ", old count was " + oldCount);
         }
      } else {
         logger.warn("Tried to update amount for item that does not exist in the warehouse.. id=" + id);
      }
   }

   public WarehouseItem updateItem(WarehouseItem warehouseItem) {
      WarehouseItem item = idToItem.get(warehouseItem.getId());
      if (item != null) {
         item.setCategory(warehouseItem.getCategory());
         item.setDescription(warehouseItem.getDescription());
         item.setPrice(warehouseItem.getPrice());
         item.setProfitPercent(warehouseItem.getProfitPercent());
         item.setDiscountPercent(warehouseItem.getDiscountPercent());
         item.setCount(warehouseItem.getCount());
         itemRepository.update(item);
         logger.info("item Updated in warehouse: " + item);
         notifyItemUpdated(item);
      }
      // else - if item doesn't exist - create a new item
      else {
         addItem(warehouseItem);
      }
      return warehouseItem;
   }

   /**
    * @param id ID of the item to retrieve
    * @return The item with the specified ID, or <code>null</code> if there is no such item in warehouse
    */
   public WarehouseItem getItem(String id) {
      // Return a copy of our item so no one can modify it outside the warehouse.
      return new WarehouseItem(idToItem.get(id));
   }

   public void registerObserver(ItemsWarehouseObserver observer) {
      observers.add(observer);
   }

   public void unregisterObserver(ItemsWarehouseObserver observer) {
      observers.remove(observer);
   }

   private void notifyItemDeleted(WarehouseItem item) {
      for (ItemsWarehouseObserver observer : observers) {
         observer.deleteItem(item);
      }
   }

   private void notifyItemUpdated(WarehouseItem item) {
      for (ItemsWarehouseObserver observer : observers) {
         observer.updateItem(item);
      }
   }

   private void notifyItemAdded(WarehouseItem item) {
      for (ItemsWarehouseObserver observer : observers) {
         observer.addItem(item);
      }
   }

}
