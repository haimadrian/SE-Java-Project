package org.spa.controller.cart;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spa.BaseTest;
import org.spa.controller.SPAApplication;
import org.spa.controller.item.Item;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.util.DummyDataForItemsWarehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Test class for {@link ShoppingCart}
 *
 * @author Haim Adrian
 * @since 05-Jun-20
 */
public class ShoppingCartTest extends BaseTest {
   private ItemsWarehouse itemsWarehouse;
   private ShoppingCart shoppingCart;
   private ShoppingCartObserverTest scObserver;

   @Before
   public void init() {
      itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
      scObserver = new ShoppingCartObserverTest();
      shoppingCart = SPAApplication.getInstance().getShoppingCart();
      shoppingCart.registerObserver(scObserver);

      DummyDataForItemsWarehouse.fillInDummyData(true);
   }

   @After
   public void cleanup() {
      if (scObserver != null) {
         shoppingCart.unregisterObserver(scObserver);
      }

      shoppingCart.clear(false);
   }

   @Test
   public void TestAddItemToCart_CartWasEmpty_CartShouldHaveAddedAnItem() throws ShoppingCartException {
      // Arrange
      int itemCountInWarehouseBeforeAddingToCart = itemsWarehouse.getItem("1").getCount();

      // Act
      shoppingCart.add("1", 2);

      // Assert
      assertEquals("There supposed to be an item in cart", 1, shoppingCart.getItems().size());
      assertEquals("Cart supposed to update listeners about item that was added", 1, scObserver.addedItems.size());
      assertEquals("Cart supposed to update listeners about item with ID=1 that was added", "1", scObserver.addedItems.get(0).getId());
      assertEquals("Cart supposed to have an item with count=2", 2, shoppingCart.getItems().get(0).getCount());
      int newCountInWarehouse = itemsWarehouse.getItem("1").getCount();
      assertEquals("Item count in warehouse supposed to be updated when adding an item to cart", itemCountInWarehouseBeforeAddingToCart - 2, newCountInWarehouse);
   }

   @Test
   public void TestAddItemToCart_NotEnoughItemsInWarehouse_ExceptionShouldBeThrown() {
      // Arrange
      Throwable t = null;

      // Act
      try {
         shoppingCart.add("1", itemsWarehouse.getItem("1").getCount() + 1);
      } catch (ShoppingCartException e) {
         t = e;
      }

      // Assert
      assertNotNull("There supposed to be an exception because there are not enough occurrences of item in warehouse", t);
      assertEquals("Nothing should be added to cart", 0, scObserver.addedItems.size());
      assertEquals("Nothing should be updated to cart", 0, scObserver.updatedItems.size());
      assertEquals("Nothing should be removed from cart", 0, scObserver.removedItems.size());
   }

   @Test
   public void TestRemoveItemFromCart_UpdateCountToZero_ItemShouldBeRemoved() throws ShoppingCartException {
      // Arrange
      int itemCountInWarehouseBeforeAddingToCart = itemsWarehouse.getItem("1").getCount();
      shoppingCart.add("1", 2);
      scObserver.addedItems.clear();

      // Act
      shoppingCart.add("1", 0);

      // Assert
      assertEquals("Nothing should be added to cart", 0, scObserver.addedItems.size());
      assertEquals("Nothing should be updated to cart", 0, scObserver.updatedItems.size());
      assertEquals("We supposed to have item removed from cart", 1, scObserver.removedItems.size());
      assertEquals("We supposed to have item #1 removed from cart", "1", scObserver.removedItems.get(0).getId());
      int newCountInWarehouse = itemsWarehouse.getItem("1").getCount();
      assertEquals("Item count in warehouse supposed to be reverted to its origin value due to removal of item from cart", itemCountInWarehouseBeforeAddingToCart, newCountInWarehouse);
   }

   @Test
   public void TestRemoveItemFromCart_UseTheRemoveMethod_ItemShouldBeRemoved() throws ShoppingCartException {
      // Arrange
      int itemCountInWarehouseBeforeAddingToCart = itemsWarehouse.getItem("1").getCount();
      shoppingCart.add("1", 2);
      scObserver.addedItems.clear();

      // Act
      shoppingCart.remove("1");

      // Assert
      assertEquals("Nothing should be added to cart", 0, scObserver.addedItems.size());
      assertEquals("Nothing should be updated to cart", 0, scObserver.updatedItems.size());
      assertEquals("We supposed to have item removed from cart", 1, scObserver.removedItems.size());
      assertEquals("We supposed to have item #1 removed from cart", "1", scObserver.removedItems.get(0).getId());
      int newCountInWarehouse = itemsWarehouse.getItem("1").getCount();
      assertEquals("Item count in warehouse supposed to be reverted to its origin value due to removal of item from cart", itemCountInWarehouseBeforeAddingToCart, newCountInWarehouse);
   }

   @Test
   public void TestUpdateItemInCart_CartWasNotEmpty_CartShouldBeUpdatedWithoutOverridingItems() throws ShoppingCartException {
      // Arrange
      shoppingCart.add("1", 2);
      shoppingCart.add("2", 1);
      scObserver.addedItems.clear();

      // Act
      shoppingCart.add("1", 1);

      // Assert
      assertEquals("There supposed to be 2 items in cart", 2, shoppingCart.getItems().size());
      assertEquals("Cart supposed to update listeners about item that was updated", 1, scObserver.updatedItems.size());
      assertEquals("Cart was not supposed to update listeners about item that was added", 0, scObserver.addedItems.size());
      assertEquals("Cart supposed to update listeners about item with ID=1 that was updated", "1", scObserver.updatedItems.get(0).getId());
      Item itemWithId1 = shoppingCart.getItems().stream().filter(item -> item.getId().equals("1")).collect(Collectors.toList()).get(0);
      assertEquals("Cart supposed to have an item with count=1 as we have overridden 2 with new count: 1", 1, itemWithId1.getCount());
   }

   @Test
   public void TestUpdateItemInCart_NotEnoughItemsInWarehouse_ExceptionShouldBeThrown() throws ShoppingCartException {
      // Arrange
      Throwable t = null;
      shoppingCart.add("1", 2);
      shoppingCart.add("2", 1);
      scObserver.addedItems.clear();
      int countInWarehouseBeforeIllegalUpdate = itemsWarehouse.getItem("1").getCount();

      // Act
      try {
         // +2 is because we keep 2 in cart, and another +1 is to exceed the amount in stock
         shoppingCart.add("1", countInWarehouseBeforeIllegalUpdate + 3);
      } catch (ShoppingCartException e) {
         t = e;
      }

      // Assert
      assertNotNull("There supposed to be an exception because there are not enough occurrences of item in warehouse", t);
      assertEquals("Nothing should be added to cart", 0, scObserver.addedItems.size());
      assertEquals("Nothing should be updated to cart", 0, scObserver.updatedItems.size());
      assertEquals("Nothing should be removed from cart", 0, scObserver.removedItems.size());
      Item itemWithId1 = shoppingCart.getItems().stream().filter(item -> item.getId().equals("1")).collect(Collectors.toList()).get(0);
      assertEquals("Item with ID #1 wasn't supposed to be affected by the exception", 2, itemWithId1.getCount());
      int newCountInWarehouse = itemsWarehouse.getItem("1").getCount();
      assertEquals("Item count in warehouse supposed to stay the same as before the illegal update", countInWarehouseBeforeIllegalUpdate, newCountInWarehouse);
   }

   static class ShoppingCartObserverTest implements ShoppingCartObserver {
      private final List<WarehouseItem> addedItems = new ArrayList<>();
      private final List<WarehouseItem> removedItems = new ArrayList<>();
      private final List<WarehouseItem> updatedItems = new ArrayList<>();

      @Override
      public void itemAdded(ShoppingCart cart, WarehouseItem item) {
         addedItems.add(item);
      }

      @Override
      public void itemRemoved(ShoppingCart cart, WarehouseItem item) {
         removedItems.add(item);
      }

      @Override
      public void itemCountUpdated(ShoppingCart cart, WarehouseItem item, int oldCount, int newCount) {
         updatedItems.add(item);
      }
   }
}
