package org.spa.controller.report;

import org.spa.controller.item.WarehouseItem;
import org.spa.controller.order.Order;
import org.spa.model.report.EconomicReport;
import org.spa.model.report.OrderReport;
import org.spa.model.report.StockReport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class ReportSystem {
   private static final DecimalFormat decimalFormat = new DecimalFormat("#.00");

   public ReportSystem() {
   }

   public String generateStockReport() {
      StockReport stockReport = new StockReport();
      StringBuilder reportString = new StringBuilder();
      for (WarehouseItem item : stockReport.getItems()) {
         reportString.append("Item name:\t").append(item.getName()).append("\n").append("Quantity:\t").append(item.getCount()).append("\n");
      }
      return reportString.toString();
   }

   public String generateEconomicReport() {
      EconomicReport economicReport = new EconomicReport();
      StringBuilder reportString = new StringBuilder();
      economicReport.getTotalProfitPerItem().forEach((itemName, profit) ->
            reportString.append("Item name: ").append(itemName).append("\t\tprofit: ").append(profit).append("\n\n"));
      reportString.append("\nTotal profit: ").append(decimalFormat.format(economicReport.getTotalProfit()));
      return reportString.toString();
   }

   public String generateOrdersReport(Date dateStart, Date dateEnd) {
      SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
      StringBuilder reportString = new StringBuilder();
      OrderReport orderReport = new OrderReport(dateStart, dateEnd);
      Map<String, Order> orders = orderReport.getOrders();
      orders.values().forEach(order -> {
         Date convertedDate = new Date(order.getOrderTime());
         reportString.append("Order ID: ").append(order.getOrderId()).append("\tOrder date: ").append(sdf.format(convertedDate)).append("\n");
         order.getItems().forEach(item -> {
            double discountPrice = item.getPrice() * item.getDiscountPercent() / 100;
            reportString.append("\tItem name: ").append(item.getName()).append("\tQuantity:\t").append(item.getCount()).append("\tTotal Price:\t").append(decimalFormat.format(item.getPrice() - discountPrice)).append("\n");
         });
         reportString.append("\n\n");
      });
      return reportString.toString();
   }
}

