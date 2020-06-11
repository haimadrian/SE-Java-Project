package org.spa.model.dal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.spa.controller.SPAApplication;
import org.spa.controller.order.Order;
import org.spa.controller.util.JsonUtils;
import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;
import org.spa.model.OrderImpl;
import org.spa.model.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Haim Adrian
 * @since 28-May-20
 */
public class OrderRepository implements Repository<Order> {
   private static final File FILE = new File(new File(SPAApplication.getWorkingDirectory(), "Repository"), "Orders.json");
   private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

   private final Map<String, Order> orders = new HashMap<>();

   public OrderRepository() {
      FILE.getParentFile().mkdirs();
   }

   @Override
   public List<? extends Order> selectAll() {
      if (orders.isEmpty()) {
         if (FILE.exists()) {
            logger.info("Reading orders from file");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(FILE))))) {
               OrdersList ordersFromFile = JsonUtils.readValue(reader, OrdersList.class);
               if (ordersFromFile != null) {
                  ordersFromFile.getOrders().forEach(order -> orders.put(order.getOrderId(), order));
                  logger.info(orders.size() + " orders have been read");
               }
            } catch (Exception e) {
               logger.error("Error has occurred while reading orders from file", e);
            }
         } else {
            logger.info("Orders file does not exist. Nothing to read");
         }
      }

      return new ArrayList<>(orders.values());
   }

   @Override
   public Order create(Order order) {
      orders.put(order.getOrderId(), new OrderImpl(order));
      return order;
   }

   @Override
   public Order update(Order order) {
      orders.put(order.getOrderId(), new OrderImpl(order));
      return order;
   }

   @Override
   public Order delete(Order order) {
      return orders.remove(order.getOrderId());
   }

   @Override
   public void saveAll(Iterable<? extends Order> orders) {
      orders.forEach(this::update);

      // Remove the dummy values so we will not store them to disk.
      this.orders.remove("#11111");
      this.orders.remove("#22222");

      if (!this.orders.isEmpty()) {
         logger.info("Saving " + this.orders.size() + " orders to file");
         try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(FILE))))) {
            JsonUtils.writeValue(writer, new OrdersList(new ArrayList<>(this.orders.values())));
            logger.info("Orders saved");
         } catch (Exception e) {
            logger.error("Error has occurred while writing orders to file", e);
         }
      }
   }

   public static class OrdersList {
      @JsonProperty
      private ArrayList<? extends Order> orders;

      public OrdersList() {
         orders = new ArrayList<>();
      }

      @JsonCreator
      public OrdersList(@JsonProperty(value = "orders") ArrayList<? extends Order> orders) {
         this.orders = orders;
      }

      public List<? extends Order> getOrders() {
         return orders;
      }
   }
}
