package org.spa.controller.item;


public interface ItemsWarehouseObserver {
   void deleteItem(WarehouseItem item);

   void updateItem(WarehouseItem item);

   void addItem(WarehouseItem item);
}
