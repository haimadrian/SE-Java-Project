package org.spa.controller.order;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spa.BaseTest;
import org.spa.controller.SPAApplication;
import org.spa.controller.item.WarehouseItem;
import org.spa.util.DummyDataForItemsWarehouse;
import org.spa.util.DummyOrders;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderSystemTest extends BaseTest {
    private OrderSystem orderSystem;
    List<WarehouseItem> items;
    Map<String, Order> ordersMap;

    @Before
    public void init() {
        DummyOrders.fillInDummyData(true);
        DummyDataForItemsWarehouse.fillInDummyData(true);
        items = SPAApplication.getInstance().getItemsWarehouse().getItems();
        orderSystem = SPAApplication.getInstance().getOrderSystem();
        ordersMap = orderSystem.getOrdersMap();
    }

    @After
    public void cleanup() {
        List<String> orderId = new ArrayList<>();
        ordersMap.values().forEach(order -> orderId.add(order.getOrderId()));
        orderId.forEach(order-> orderSystem.deleteOrder(order));
    }

    @Test
    public void TestCreateOrder() {
        int ordersTableSize = orderSystem.getOrdersMap().size();
        Order order = orderSystem.createOrder("A", new ArrayList<>(items));
        assertEquals("Check if OrdersMap size is plus 1 after creating order", orderSystem.getOrdersMap().size(),ordersTableSize + 1);
        orderSystem.deleteOrder(order.getOrderId());
    }

    @Test
    public void TestFindOrder() {
      Order order = orderSystem.createOrder("Moshe", new ArrayList<>(items));
      assertEquals("Find order by order id, order should be equals to the order that was found", order, orderSystem.findOrder(order.getOrderId()));
      orderSystem.deleteOrder(order.getOrderId());
    }

    @Test
    public void TestDeleteOrder() {
        Order order = orderSystem.createOrder("Tomer", new ArrayList<>(items));
        int ordersTableSize = orderSystem.getOrdersMap().size();
        orderSystem.deleteOrder(order.getOrderId());
        assertEquals("Check if OrdersMap size is minus 1 after deleting order", orderSystem.getOrdersMap().size(),ordersTableSize - 1);
    }

    @Test
    public void TestFindOrdersOfUser() {
        int count = 0;
        orderSystem.createOrder("1", new ArrayList<>(items));
        count++;
        orderSystem.createOrder("1", new ArrayList<>(items));
        count++;
        orderSystem.createOrder("1", new ArrayList<>(items));
        count++;

        List<Order> orders = orderSystem.findOrdersOfUser("1");
        assertEquals("Display orders for the user specified", count, orders.size());
    }
}