package org.spa.controller.item;

import org.spa.common.Repository;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.model.Item;
import org.spa.model.dal.ItemRepository;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Haim Adrian
 * @since 16-May-20
 */
public class ItemsWarehouse {
   private static final Logger logger = LoggerFactory.getLogger(ItemsWarehouse.class);
   private final Map<String, WarehouseItem> idToItem;
   private final Repository<Item> itemRepository;

   public ItemsWarehouse() {
      idToItem = new HashMap<>(1000); // Yeah sure...
      itemRepository = new ItemRepository();
   }

   /**
    * Call this method to read data from storage
    */
   public void start() throws FileNotFoundException {
      // Load data into memory
      itemRepository.selectAll().forEach(item -> idToItem.put(item.getId(), itemToWarehouseItem(item)));
   }

   /**
    * Call this method when exiting the application, to save the in memory data to disk
    */
   public void stop() {
      // Save data to disk
      itemRepository.saveAll(idToItem.values().stream().map(ItemsWarehouse::warehouseItemToItem).collect(Collectors.toList()));
   }

   /**
    * Note that the result is a copy of the items in warehouse, so you cannot modify it by accessing this method. You mast use the
    * add/remove methods.
    * @return All items in warehouse
    */
   public List<WarehouseItem> getItems() {
      // Disallow modifying items map outside the warehouse. The add/remove methods must be used
      // or we will have synchronization bugs.
      return new ArrayList<>(idToItem.values());
   }

   /**
    * Add a new item to the items warehouse. This will override any amount of this item in case it is already existing in the warehouse.
    * @param id
    * @param name
    * @param category
    * @param description
    * @param price
    * @param profitPercent
    * @param discountPercent
    * @param amount
    */
   public void addItem(String id,String category, String name, String description, double price, double profitPercent, double discountPercent, int amount) {
      WarehouseItem item = new WarehouseItem(id,category, name, description, price, profitPercent, discountPercent, amount);
      idToItem.put(item.getId(), item);
      logger.info("Item added to warehouse: " + item);
   }

   /**
    * Completely remove an item from warehouse
    * @param id The id of the item to remove
    * @return The removed item or <code>null</code> in case there is no item with the specified id
    */
   public WarehouseItem removeItem(String id) {
      WarehouseItem item =  idToItem.remove(id);
      if (item != null) {
         logger.info("Removed item from warehouse: " + item);
      }

      return item;
   }

   /**
    * Set new amount of an item in the warehouse
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
            logger.info("Item count updated: " + item + ", old count was " + oldCount);
         }
      } else {
         logger.warn("Tried to update amount for item that does not exist in the warehouse.. id=" + id);
      }
   }

   /**
    * @param id ID of the item to retrieve
    * @return The item with the specified ID, or <code>null</code> if there is no such item in warehouse
    */
   public WarehouseItem getItem(String id) {
      // Return a copy of our item so no one can modify it outside the warehouse.
      return new WarehouseItem(idToItem.get(id));
   }

   private static WarehouseItem itemToWarehouseItem(Item item) {
      return new WarehouseItem(item.getId(),item.getCategory(), item.getName(), item.getDescription(), item.getPrice(), item.getProfitPercent(), item.getDiscountPercent(), item.getCount());
   }

   private static Item warehouseItemToItem(WarehouseItem item) {
      return new Item(item.getId(),item.getCategory(), item.getName(), item.getDescription(), item.getPrice(), item.getProfitPercent(), item.getDiscountPercent(), item.getCount());
   }
}
