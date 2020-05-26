package org.spa.controller.order;

import org.spa.common.Repository;
import org.spa.model.Order;
import org.spa.model.dal.OrderRepository;
import java.util.HashMap;
import java.util.Map;

public class OrderSystem {

    private final Map<String, Order> ordersMap;
    private final Repository<Order> orderRepository;

    public OrderSystem() {
        ordersMap = new HashMap<>(1000); // Yeah sure...
        orderRepository = new OrderRepository();
    }

    public void start() {
        // Load data into memory
        orderRepository.selectAll().forEach(order -> ordersMap.put(order.getUserId(),order));
    }

    public Map<String, Order> getOrdersMap() {
        return ordersMap;
    }

    public Order findOrder(Order order) {
        //return null if order isn't found
        return ordersMap.get(order.getOrderId());
    }

    public void dropAllOrders() {
        ordersMap.clear();
        //TODO clear repository
    }

    public Order updateOrder(Order order) {
        Order o = ordersMap.get(order.getOrderId());
        //TODO update, ask what needed
        return o;
    }

    public Order deleteOrder(Order order) {
        ordersMap.remove(order.getOrderId());
        // TODO delete from repository
        return order;
    }

    public Order createOrder(Order order) {
        ordersMap.put(order.getOrderId(),order);
        // TODO add to repository
        return order;
    }
}
