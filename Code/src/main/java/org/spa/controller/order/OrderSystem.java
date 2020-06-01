package org.spa.controller.order;

import org.spa.common.Repository;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.item.WarehouseItem;
import org.spa.model.Item;
import org.spa.model.Order;
import org.spa.model.dal.OrderRepository;

import java.util.*;
import java.util.stream.Collectors;

public class OrderSystem {
    private static final Logger logger = LoggerFactory.getLogger(OrderSystem.class);
    private final Map<String, Order> ordersMap;
    private final Repository<Order> orderRepository;
    private final SelectionModelManager<Order> selectionModel;

    public OrderSystem() {
        ordersMap = new HashMap<>(1000);
        orderRepository = new OrderRepository();
        selectionModel = new SelectionModelManager<>();
    }
    public SelectionModelManager<Order> getSelectionModel() {
        return selectionModel;
    }

    public void start() {
        logger.info("Starting OrderSystem - Select orders from repository");

        // Load data into memory
        orderRepository.selectAll().forEach(order -> ordersMap.put(order.getOrderId(), order));
    }

    public void stop() {
        logger.info("Stopping OrderSystem - Save orders to repository");

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
        List<Order> orders = ordersMap.values().stream().filter(order -> userId.equalsIgnoreCase(order.getUserId())).collect(Collectors.toList());
        logger.info("Find orders for userId '" + userId + "' returned " + orders.size() + " orders");
        return orders;
    }

    public Order deleteOrder(String orderId) {
        logger.info("Deleting order - " + orderId);
        return ordersMap.remove(orderId);
    }

    public Order createOrder(String userId, Iterable<WarehouseItem> items) {
        String orderId = UUID.randomUUID().toString();
        List<Item> modelItems = new ArrayList<>();
        items.forEach(item -> modelItems.add(warehouseItemToItem(item)));

        logger.info("Creating order: " + orderId + " for user '" + userId + "'");

        Order order = new Order(orderId, System.currentTimeMillis(), userId, modelItems);
        ordersMap.put(orderId, order);
        return order;
    }

    private static Item warehouseItemToItem(WarehouseItem item) {
        return new Item(item.getId(),item.getCategory(), item.getName(), item.getDescription(), item.getPrice(), item.getProfitPercent(), item.getDiscountPercent(), item.getCount());
    }
}
