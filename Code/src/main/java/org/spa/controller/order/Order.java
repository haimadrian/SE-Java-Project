package org.spa.controller.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.spa.controller.item.Item;
import org.spa.model.OrderImpl;

import java.util.List;

/**
 * An interface created to represent order in the application
 *
 * @author Haim Adrian
 * @since 11-Jun-20
 */
@JsonDeserialize(as = OrderImpl.class)
public interface Order {
   /**
    * @return Unique ID of this order
    */
   String getOrderId();

   /**
    * @return When the order created (PurchaseAction)
    */
   long getOrderTime();

   /**
    * @return ID of the user that this order belongs to
    */
   String getUserId();

   /**
    * @return All of the items that are part of this order
    */
   List<? extends Item> getItems();
}
