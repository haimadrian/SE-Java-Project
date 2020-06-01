package org.spa.controller.item;

import org.spa.model.Item;

public interface ItemsWarehouseObserver {
    void deleteItem(WarehouseItem item);
    void updateItem(WarehouseItem item);
    void addItem(WarehouseItem item);
}
