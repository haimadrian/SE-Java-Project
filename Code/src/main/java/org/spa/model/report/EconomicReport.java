package org.spa.model.report;

import org.spa.common.SPAApplication;
import org.spa.model.Order;

import java.util.HashMap;
import java.util.Map;

public class EconomicReport extends Report {
    private double incoming;
    private double expenses;

    public EconomicReport() {
        super();
    }

    public double getIncoming() {
        Map<String, Order> ordersMap = SPAApplication.getInstance().getOrderSystem().getOrdersMap();
        incoming = ordersMap.values().stream().flatMap(order -> order.getItems().stream()).mapToDouble(item -> item.getPriceWithProfit() *item.getCount()).sum();
        return incoming;
    }

    public double getExpenses() {// From the actual price we subtract the profit value to get the actual expense with discount included multiplied by the items count
        SPAApplication.getInstance().getItemsWarehouse().getItems().forEach(item ->
                expenses+=(item.getActualPrice()-item.getProfitValue())*item.getCount());
        //We also need to add the expenses of items that are now in the Orders
        expenses += SPAApplication.getInstance().getOrderSystem().getOrdersMap().values().stream().flatMap(order -> order.getItems().stream()).mapToDouble
                (item -> (item.getActualPrice()-item.getProfitValue()) *item.getCount()).sum();
        return expenses;
    }
    public Map<String, Double> getExpensesPerItem() {
        Map<String, Double> expensesPerItem= new HashMap<>();
        SPAApplication.getInstance().getOrderSystem().getOrdersMap().values().stream().forEach(order -> order.getItems().stream().forEach(
                (item -> expensesPerItem.put(item.getName(),(-1)*(item.getActualPrice()-item.getProfitValue())*item.getCount()))));
        SPAApplication.getInstance().getItemsWarehouse().getItems().forEach(item -> expensesPerItem.put(item.getName(),((-1)*item.getActualPrice()-item.getProfitValue())*item.getCount()));
    return expensesPerItem;
    }
    public Map<String, Double> getProfitPerItem() {
        Map<String, Double> profitPerItem= new HashMap<>();
        SPAApplication.getInstance().getOrderSystem().getOrdersMap().values().stream().forEach(order -> order.getItems().stream().forEach(
                (item -> profitPerItem.put(item.getName(),item.getPriceWithProfit()*item.getCount()))));
        SPAApplication.getInstance().getItemsWarehouse().getItems().forEach(item -> profitPerItem.put(item.getName(),(item.getPriceWithProfit()*item.getCount())));
        return profitPerItem;
    }

    public double getTotalProfit(){
        return getIncoming() - getExpenses();
    }

    public Map<String, Double> getTotalProfitPerItem(){
        Map<String, Double> profitPerItem= getProfitPerItem();
        Map<String, Double> expensesPerItem= getExpensesPerItem();
        profitPerItem.putAll(expensesPerItem);
        return profitPerItem;
    }
}
