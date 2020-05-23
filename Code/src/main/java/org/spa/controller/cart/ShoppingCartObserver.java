package org.spa.controller.cart;

import org.spa.controller.item.WarehouseItem;

/**
 * An observer that listens to changes in the {@link ShoppingCart}
 * @author Haim Adrian
 * @since 22-May-20
 */
public interface ShoppingCartObserver {
   /**
    * This event is raised when a new item is added to cart
    * @param cart A reference to {@link ShoppingCart} to let listener to get additional data from
    * @param item The item that was added to cart
    */
   void itemAdded(ShoppingCart cart, WarehouseItem item);

   /**
    * This event is raised when an existing item is removed from cart
    * @param cart A reference to {@link ShoppingCart} to let listener to get additional data from
    * @param item The item that was removed from cart
    */
   void itemRemoved(ShoppingCart cart, WarehouseItem item);

   /**
    * This event is raised when the count of an existing item in cart is updated
    * @param cart A reference to {@link ShoppingCart} to let listener to get additional data from
    * @param item The item whose its count has been modified
    * @param oldCount Value before update
    * @param newCount Value after update
    */
   void itemCountUpdated(ShoppingCart cart, WarehouseItem item, int oldCount, int newCount);
}
