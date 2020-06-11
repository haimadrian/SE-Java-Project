package org.spa.controller.report;

import org.spa.controller.item.WarehouseItem;
import org.spa.model.Order;
import org.spa.model.report.EconomicReport;
import org.spa.model.report.OrderReport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class ReportSystem {
   private static final DecimalFormat decimalFormat = new DecimalFormat("#.00");

   public ReportSystem() {
   }

   public String generateStockReport(List<WarehouseItem> items) {
      StringBuilder reportString = new StringBuilder();
      for (WarehouseItem item : items) {
         reportString.append("Item name:\t").append(item.getName()).append("\n").append("Quantity:\t").append(item.getCount()).append("\n");
      }
      return reportString.toString();
   }

   public String generateEconomicReport() {
      EconomicReport economicReport = new EconomicReport();
      StringBuilder reportString = new StringBuilder();
      economicReport.getTotalProfitPerItem().forEach((itemName, profit) ->
            reportString.append("Item name: ").append(itemName).append("\tprofit ").append(profit).append("\n\n"));
      reportString.append("\nTotal profit: ").append(economicReport.getTotalProfit());
      return reportString.toString();

   }

   public String generateOrdersReport(Date dateStart, Date dateEnd) {
      StringBuilder reportString = new StringBuilder();
      OrderReport orderReport = new OrderReport(dateStart, dateEnd);
      Map<String, Order> orders = orderReport.getOrders();
      orders.values().forEach(order -> {
         SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
         Date convertedDate = new Date(order.getOrderTime());
         reportString.append("Order ID: ").append(order.getOrderId()).append("\tOrder date: ").append(sdf.format(convertedDate)).append("\n");
         order.getItems().forEach(item -> {
            double discountPrice = item.getPrice() * item.getDiscountPercent() / 100;
            reportString.append("\tItem name: " + item.getName() + "\tQuantity:\t" + item.getCount() + "\tTotal Price:\t" + (item.getPrice() - discountPrice) + "\n");
         });
         reportString.append("\n\n");
      });
      return reportString.toString();
   }
}

