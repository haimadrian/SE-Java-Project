package org.spa.model.report;

import org.spa.common.SPAApplication;
import org.spa.controller.item.WarehouseItem;
import org.spa.model.Item;
import org.spa.model.Order;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EconomicReport extends Report {
    private double incoming;
    private double expenses;

    public EconomicReport(String reportID) {
        super(reportID);
    }

    public double getIncoming() {
        Map<String, Order> ordersMap = SPAApplication.getInstance().getOrderSystem().getOrdersMap();
        incoming = ordersMap.values().stream().flatMap(order -> order.getItems().stream()).mapToInt(item -> (int) item.getPrice()).sum();
        return incoming;
    }

    public double getExpenses() {
        SPAApplication.getInstance().getItemsWarehouse().getItems().forEach(warehouseItem ->
                expenses+=warehouseItem.getPrice());
        return expenses;
    }
    public double getTotalProfitPerItem() {
        double totalPrice = 0;
        double profitPerItem = 0;
/*
        SPAApplication.getInstance().getOrderSystem().getOrdersMap().forEach((s, order) ->
        {
        });*/
        List<WarehouseItem> warehouseItemList = SPAApplication.getInstance().getItemsWarehouse().getItems();
        for (WarehouseItem warehouseItem : warehouseItemList) {
            totalPrice = (warehouseItem.getPrice() * warehouseItem.getProfitPercent() / 100) + warehouseItem.getPrice();
            totalPrice += totalPrice - (totalPrice * warehouseItem.getDiscountPercent() / 100);
        }

        return profitPerItem;
    }

    public double calculateTotalProfit(){
        if(incoming > 0 && expenses > 0)
            return incoming - expenses;
        return 1;
    }
}
