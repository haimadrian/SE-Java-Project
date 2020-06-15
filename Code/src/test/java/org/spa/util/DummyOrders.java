package org.spa.util;

import org.spa.controller.SPAApplication;
import org.spa.controller.item.Item;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.order.Order;
import org.spa.controller.order.OrderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DummyOrders {

    public static void fillInDummyData(boolean shouldClearBefore) {
        DummyDataForItemsWarehouse.fillInDummyData(true);
        ItemsWarehouse itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
        OrderSystem orderSystem = SPAApplication.getInstance().getOrderSystem();
        List<Item> items1 = new ArrayList<>();
        List<Item> items2 = new ArrayList<>();
        List<Item> items3 = new ArrayList<>();
        if (shouldClearBefore) {
            Map<String, Order> ordersMap = orderSystem.getOrdersMap();
            ordersMap.values().forEach(order -> orderSystem.deleteOrder(order.getOrderId()));
        }

        items1.add(itemsWarehouse.getItems().get(0));
        items1.add(itemsWarehouse.getItems().get(1));
        items1.add(itemsWarehouse.getItems().get(2));

        items2.add(itemsWarehouse.getItems().get(3));
        items2.add(itemsWarehouse.getItems().get(4));

        items3.add(itemsWarehouse.getItems().get(4));
        items3.add(itemsWarehouse.getItems().get(5));
        items3.add(itemsWarehouse.getItems().get(6));

        orderSystem.createOrder("Idan", items1);
        orderSystem.createOrder("Lior", items1);
        orderSystem.createOrder("Haim", items2);
        orderSystem.createOrder("David", items3);
        orderSystem.createOrder("Ben", items2);
    }
}