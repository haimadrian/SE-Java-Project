package org.spa.model.report;

import org.spa.controller.SPAApplication;
import org.spa.controller.order.Order;

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
      ordersMap.values().forEach(order -> order.getItems().forEach(item -> incoming += item.getActualPrice() * item.getCount()));
      return incoming;
   }

   public double getExpenses() {
      SPAApplication.getInstance().getItemsWarehouse().getItems().forEach(item ->
            expenses += (item.getPrice() * item.getCount()));
      SPAApplication.getInstance().getShoppingCart().getItems().forEach(item ->
            expenses += (item.getPrice() * item.getCount()));
      return expenses;
   }

   public Map<String, Double> getExpensesPerItem(Map<String, Double> expensesPerItem) {
      SPAApplication.getInstance().getItemsWarehouse().getItems().forEach(item -> {
         Double currItemExpense = item.getPrice() * item.getCount();
         expensesPerItem.merge(item.getName(), currItemExpense, Double::sum);
      });
      SPAApplication.getInstance().getShoppingCart().getItems().forEach(item -> {
         Double currItemExpense = item.getPrice() * item.getCount();
         expensesPerItem.merge(item.getName(), currItemExpense, Double::sum);
      });
      return expensesPerItem;
   }

   public Map<String, Double> getProfitPerItem(Map<String, Double> profitPerItem) {
      SPAApplication.getInstance().getOrderSystem().getOrdersMap().values().forEach(order -> order.getItems().forEach((item -> {
         Double currItemExpense = item.getActualPrice() * item.getCount();
         profitPerItem.merge(item.getName(), currItemExpense, Double::sum);
      })));
      return profitPerItem;
   }

   public double getTotalProfit() {
      return getIncoming() - getExpenses();
   }

   public Map<String, Double> getTotalProfitPerItem() {
      Map<String, Double> profitPerItem = new HashMap<>();
      profitPerItem = getExpensesPerItem(profitPerItem);
      profitPerItem = getProfitPerItem(profitPerItem);
      return profitPerItem;
   }
}
