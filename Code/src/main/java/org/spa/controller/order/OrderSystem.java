package org.spa.controller.order;

import org.spa.controller.Service;
import org.spa.controller.item.Item;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;
import org.spa.model.Repository;
import org.spa.model.dal.OrderRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderSystem implements Service {
   private static final Logger logger = LoggerFactory.getLogger(OrderSystem.class);
   private final Map<String, Order> ordersMap;
   private final Repository<Order> orderRepository;
   private final SelectionModelManager<Order> selectionModel;

   public OrderSystem() {
      ordersMap = new HashMap<>(1000);
      orderRepository = new OrderRepository();
      selectionModel = new SelectionModelManager<>();
   }

   public SelectionModelManager<Order> getSelectionModel() {
      return selectionModel;
   }

   @Override
   public void start() {
      logger.info("Starting OrderSystem - Select orders from repository");

      // Load data into memory
      orderRepository.selectAll().forEach(order -> ordersMap.put(order.getOrderId(), order));
   }

   @Override
   public void stop() {
      logger.info("Stopping OrderSystem - Save orders to repository");

      // Save data to storage
      orderRepository.saveAll(ordersMap.values());
   }

   public Map<String, Order> getOrdersMap() {
      return ordersMap;
   }

   public Order findOrder(String orderId) {
      return ordersMap.get(orderId);
   }

   public List<Order> findOrdersOfUser(String userId) {
      List<Order> orders = ordersMap.values().stream().filter(order -> userId.equalsIgnoreCase(order.getUserId())).collect(Collectors.toList());
      logger.info("Find orders for userId '" + userId + "' returned " + orders.size() + " orders");
      return orders;
   }

   public Order deleteOrder(String orderId) {
      logger.info("Deleting order - " + orderId);
      return ordersMap.remove(orderId);
   }

   public Order createOrder(String userId, List<Item> items) {
      Order order = orderRepository.create(new Order() {
         @Override
         public String getOrderId() {
            return UUID.randomUUID().toString();
         }

         @Override
         public long getOrderTime() {
            return System.currentTimeMillis();
         }

         @Override
         public String getUserId() {
            return userId;
         }

         @Override
         public List<? extends Item> getItems() {
            return items;
         }
      });

      logger.info("Creating order: " + order.getOrderId() + " for user '" + userId + "'");
      ordersMap.put(order.getOrderId(), order);
      return order;
   }
   OrderSystemTestAccessor getTestAccessor() {return new OrderSystemTestAccessor();}
   class OrderSystemTestAccessor {
      public void createOrder(Order order) {
         OrderSystem.this.ordersMap.put("111111",order);
      }
   }
}
