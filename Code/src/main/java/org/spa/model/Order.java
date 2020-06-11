package org.spa.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Order {
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
    public Order(@JsonProperty(value = "orderId") String orderId,
                 @JsonProperty(value = "orderTime") long orderTime,
                 @JsonProperty(value = "userId") String userId,
                 @JsonProperty(value = "items") List<Item> items) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.userId = userId;
        this.items = items;
    }
    //@formatter:on

   public Order(Order copy) {
      this.orderId = copy.getOrderId();
      this.orderTime = copy.getOrderTime();
      this.userId = copy.getUserId();
      this.items = new ArrayList<>(copy.getItems());
   }

   public String getOrderId() {
      return orderId;
   }

   public long getOrderTime() {
      return orderTime;
   }

   public String getUserId() {
      return userId;
   }

   public List<Item> getItems() {
      return items;
   }
}
