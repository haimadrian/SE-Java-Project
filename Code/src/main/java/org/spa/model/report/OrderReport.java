package org.spa.model.report;

import org.spa.controller.SPAApplication;
import org.spa.controller.order.Order;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderReport extends Report {
   private final Date dateStart;
   private final Date dateEnd;

   public OrderReport(Date dateStart, Date dateEnd) {
      this.dateStart = dateStart;
      this.dateEnd = dateEnd;
   }

   public Map<String, Order> getOrders() {
      Map<String, Order> orderMap = new HashMap<>();
      Map<String, Order> finalOrderMap = orderMap;
      SPAApplication.getInstance().getOrderSystem().getOrdersMap().values().forEach(order ->
      {
         Date convertedDate = new Date(order.getOrderTime());
         if (dateStart.before(convertedDate) && dateEnd.after(convertedDate))
            finalOrderMap.put(order.getOrderId(), order);
      });
      orderMap = orderMap.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                  (oldValue, newValue) -> oldValue, LinkedHashMap::new));
      return orderMap;
   }
}
