package org.spa.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.spa.controller.item.Item;
import org.spa.controller.order.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderImpl implements Order {
   @JsonProperty
   private String orderId;

   @JsonProperty
   private long orderTime;

   @JsonProperty
   private String userId;

   @JsonProperty
   private List<Item> items;

   //@formatter:off
    @JsonCreator()
    public OrderImpl(@JsonProperty(value = "orderId") String orderId,
                     @JsonProperty(value = "orderTime") long orderTime,
                     @JsonProperty(value = "userId") String userId,
                     @JsonProperty(value = "items") List<? extends Item> items) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.userId = userId;
        this.items = new ArrayList<>();
        if (items != null) {
           items.forEach(item -> this.items.add(new ItemImpl(item)));
        }
    }
    //@formatter:on

   public OrderImpl(Order copy) {
      this.orderId = copy.getOrderId();
      this.orderTime = copy.getOrderTime();
      this.userId = copy.getUserId();
      this.items = new ArrayList<>();
      if (copy.getItems() != null) {
         copy.getItems().forEach(item -> this.items.add(new ItemImpl(item)));
      }
   }

   @Override
   public String getOrderId() {
      return orderId;
   }

   @Override
   public long getOrderTime() {
      return orderTime;
   }

   @Override
   public String getUserId() {
      return userId;
   }

   @Override
   public List<? extends Item> getItems() {
      return items;
   }
}
