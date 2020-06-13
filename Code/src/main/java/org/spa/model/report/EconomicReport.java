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
      ordersMap.values().forEach(order -> order.getItems().forEach(item -> incoming+=item.getActualPrice() * item.getCount()));
      return incoming;
   }

   public double getExpenses() {// From the actual price we subtract the profit value to get the actual expense with discount included multiplied by the items count
      SPAApplication.getInstance().getItemsWarehouse().getItems().forEach(item ->
            expenses += (item.getPrice()  - item.getProfitValue()+item.getDiscountValue()) * item.getCount());
      //Expenses of items that are in the cart
      SPAApplication.getInstance().getShoppingCart().getItems().forEach(item ->
              expenses+= (item.getPrice()  - item.getProfitValue()+item.getDiscountValue()) * item.getCount());
      return expenses;
   }

   public Map<String, Double> getExpensesPerItem(Map<String, Double> profitPerItem) {
      SPAApplication.getInstance().getItemsWarehouse().getItems().forEach(item -> {
                  if (profitPerItem.get(item.getName()) != null)
                     profitPerItem.put(item.getName(), (item.getPrice() - item.getProfitValue() + item.getDiscountValue()) * item.getCount() * (-1) + profitPerItem.get(item.getName()));
                  else
                     profitPerItem.put(item.getName(), (item.getPrice() - item.getProfitValue() + item.getDiscountValue()) * item.getCount() * (-1));
              });
      SPAApplication.getInstance().getShoppingCart().getItems().forEach(item ->{
         if(profitPerItem.get(item.getName())!=null)
            profitPerItem.put(item.getName(),(item.getPrice() - item.getProfitValue()+item.getDiscountValue()) * item.getCount()*(-1)+profitPerItem.get(item.getName()));
         else
            profitPerItem.put(item.getName(),(item.getPrice()  - item.getProfitValue()+item.getDiscountValue()) * item.getCount()*(-1));
      });
      return profitPerItem;
   }

   public Map<String, Double> getProfitPerItem(Map<String, Double> profitPerItem) {
      SPAApplication.getInstance().getOrderSystem().getOrdersMap().values().forEach(order -> order.getItems().forEach(
            (item -> {if(profitPerItem.get(item.getName())!=null)
               profitPerItem.put(item.getName(), item.getActualPrice() * item.getCount() + profitPerItem.get(item.getName()));
            else
               profitPerItem.put(item.getName(), item.getActualPrice() * item.getCount());
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
/*      expensesPerItem.forEach((item, price) -> {
         if (profitPerItem.get(item)!=null)
            expensesPerItem.put(item,expensesPerItem.get(item)+profitPerItem.get(item));
      });*/
      return profitPerItem;
   }
}
