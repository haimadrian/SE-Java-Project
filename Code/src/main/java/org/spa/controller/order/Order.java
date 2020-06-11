package org.spa.controller.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.spa.controller.item.Item;
import org.spa.model.OrderImpl;

import java.util.List;

/**
 * @author Haim Adrian
 * @since 11-Jun-20
 */
@JsonDeserialize(as = OrderImpl.class)
public interface Order {
   String getOrderId();
   long getOrderTime();
   String getUserId();
   List<? extends Item> getItems();
}
