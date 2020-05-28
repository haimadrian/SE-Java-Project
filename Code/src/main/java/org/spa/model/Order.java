package org.spa.model;

import org.spa.common.SPAApplication;
import org.spa.controller.item.WarehouseItem;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private final String orderId;
    private final Date orderDate;
    private final String userId;
    private  Map<Integer, WarehouseItem> itemsMap; //TODO ask what should be here


    public Order(String orderId, Date orderDate, String userId, Map<String, WarehouseItem> idToItem) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.userId = userId;
        this.itemsMap = new HashMap<>(); //TODO ask what should be here
    }

    public String getOrderId() {
        return orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public String getUserId() {
        return userId;
    }
}
