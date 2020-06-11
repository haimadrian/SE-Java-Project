package org.spa.view.order;

import org.spa.controller.item.Item;
import org.spa.controller.order.Order;
import org.spa.view.table.TableCellValue;
import org.spa.view.table.TableModelIfc;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderViewInfo implements TableModelIfc, Order {

   private final String orderId;
   private long orderTime;
   private List<? extends Item> items;
   private String userId;

   public OrderViewInfo(String orderId, long orderTime, String userId, List<? extends Item> items) {
      this.orderId = orderId;
      this.orderTime = orderTime;
      this.items = items;
      this.userId = userId;
   }

   @Override
   public String getOrderId() {
      return orderId;
   }

   @Override
   public long getOrderTime() {
      return orderTime;
   }

   public String getConvertedOrderTime() {
      SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
      Date convertedDate = new Date(orderTime);
      return (sdf.format(convertedDate));
   }

   @Override
   public String getUserId() {
      return userId;
   }

   public String getItemsToString() {
      String itemsToString = "";
      int count = 0;
      double sumTotalPrice = 0;
      double sumItemPrice = 0;
      for (Item item : items) {
         count += item.getCount();
         sumItemPrice = item.getActualPrice();
         sumTotalPrice += sumItemPrice;
      }
      itemsToString = "Amount of items: " + count + System.lineSeparator() + "Total Price: " + new DecimalFormat("##.##").format(sumTotalPrice) + "$";
      return itemsToString;
   }

   @Override
   public List<? extends Item> getItems() {
      return items;
   }

   @Override
   public Object getAttributeValue(String attributeName) {

      switch (OrderColumn.valueOf(attributeName)) {
         case OrderId: {
            return new TableCellValue<OrderViewInfo>(getOrderId(), this);
         }
         case OrderTime: {
            return new TableCellValue<OrderViewInfo>(getConvertedOrderTime(), this);
         }
         case Summary: {
            return new TableCellValue<OrderViewInfo>(getItemsToString(), this);
         }
         case UserId: {
            return new TableCellValue<OrderViewInfo>(getUserId(), this);
         }
         default:
            //do nothing
            return "";
      }
   }

   @Override
   public void setAttributeValue(String attributeName, Object value) {
   }

   @Override
   public String toString() {
      return "OrderViewInfo{" +
            "id='" + orderId + '}';
   }
}
