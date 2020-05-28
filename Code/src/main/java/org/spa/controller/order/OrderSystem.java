package org.spa.controller.order;

import org.spa.common.Repository;
import org.spa.controller.item.WarehouseItem;
import org.spa.model.Item;
import org.spa.model.Order;
import org.spa.model.dal.OrderRepository;

import java.util.*;
import java.util.stream.Collectors;

public class OrderSystem {
    private final Map<String, Order> ordersMap;
    private final Repository<Order> orderRepository;

    public OrderSystem() {
        ordersMap = new HashMap<>(1000);
        orderRepository = new OrderRepository();
    }

    public void start() {
        // Load data into memory
        orderRepository.selectAll().forEach(order -> ordersMap.put(order.getOrderId(), order));
    }

    public void stop() {
        // Save data to storage
        orderRepository.saveAll(ordersMap.values());
    }

    public Map<String, Order> getOrdersMap() {
        return ordersMap;
    }

    public Order findOrder(String orderId) {
        return ordersMap.get(orderId);
    }

    public List<Order> findOrdersOfUser(String userId) {
        return ordersMap.values().stream().filter(order -> userId.equalsIgnoreCase(order.getUserId())).collect(Collectors.toList());
    }

    public Order deleteOrder(String orderId) {
        return ordersMap.remove(orderId);
    }

    public Order createOrder(String userId, Iterable<WarehouseItem> items) {
        String orderId = UUID.randomUUID().toString();
        List<Item> modelItems = new ArrayList<>();
        items.forEach(item -> modelItems.add(warehouseItemToItem(item)));

        Order order = new Order(orderId, System.currentTimeMillis(), userId, modelItems);
        ordersMap.put(orderId, order);
        return order;
    }

    private static Item warehouseItemToItem(WarehouseItem item) {
        return new Item(item.getId(),item.getCategory(), item.getName(), item.getDescription(), item.getPrice(), item.getProfitPercent(), item.getDiscountPercent(), item.getCount());
    }
}
