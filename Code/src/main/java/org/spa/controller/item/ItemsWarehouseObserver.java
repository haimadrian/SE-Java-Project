package org.spa.controller.item;


public interface ItemsWarehouseObserver {
   void onItemDeleted(WarehouseItem item);

   void onItemUpdated(WarehouseItem item);

   void onItemAdded(WarehouseItem item);
}
