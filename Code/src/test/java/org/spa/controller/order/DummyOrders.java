package org.spa.controller.order;

import org.spa.controller.SPAApplication;
import org.spa.controller.item.Item;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.util.DummyDataForItemsWarehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyOrders {

   public static void fillInDummyData(boolean shouldClearBefore) {
      DummyDataForItemsWarehouse.fillInDummyData(true);
      ItemsWarehouse itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
      OrderSystem orderSystem = SPAApplication.getInstance().getOrderSystem();
      List<Item> items1 = new ArrayList<>();
      if (shouldClearBefore) {
         Map<String, Order> ordersMap = new HashMap<>(orderSystem.getOrdersMap());
         ordersMap.values().forEach(order -> orderSystem.deleteOrder(order.getOrderId()));
      }
      OrderSystem.OrderSystemTestAccessor testAccessor = SPAApplication.getInstance().getOrderSystem().getTestAccessor();
      items1.add(itemsWarehouse.getItems().get(0));
      Order order = new Order() {
         @Override
         public String getOrderId() {
            return "11111";
         }

         @Override
         public long getOrderTime() {
            return System.currentTimeMillis();
         }

         @Override
         public String getUserId() {
            return "Lior";
         }

         @Override
         public List<? extends Item> getItems() {
            return items1;
         }
      };
      testAccessor.createOrder(order);
      items1.add(itemsWarehouse.getItems().get(0));
      items1.add(itemsWarehouse.getItems().get(1));
      items1.add(itemsWarehouse.getItems().get(2));
   }
}
