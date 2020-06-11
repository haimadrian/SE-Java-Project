package org.spa.model.report;

import org.spa.controller.SPAApplication;
import org.spa.controller.order.Order;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderReport extends Report {
   private Date dateStart;
   private Date dateEnd;

   public OrderReport(Date dateStart, Date dateEnd) {
      this.dateStart = dateStart;
      this.dateEnd = dateEnd;
   }

   public Map<String, Order> getOrders() {
      Map<String, Order> orderMap = new HashMap<>();
      SPAApplication.getInstance().getOrderSystem().getOrdersMap().values().forEach(order ->
      {
         Date convertedDate = new Date(order.getOrderTime());
         if (dateStart.before(convertedDate) && dateEnd.after(convertedDate))
            orderMap.put(order.getOrderId(), order);
      });
      return orderMap;
   }
}
