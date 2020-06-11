package org.spa.controller.item;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.spa.model.ItemImpl;

/**
 * An interface that created to expose data of an item in the application<br/>
 * This interface created so we can implement it at the model layer without expose the model implementation to the view layer
 *
 * @author Haim Adrian
 * @since 11-Jun-20
 */
@JsonDeserialize(as = ItemImpl.class)
public interface Item {
   /**
    * @return Unique ID of this item
    */
   String getId();

   /**
    * @return Name of this item
    */
   String getName();

   /**
    * @return The category that this item belongs to. e.g. CPU
    */
   String getCategory();

   /**
    * @return Some description about this item. Can be long, multi-line string
    */
   String getDescription();

   /**
    * @return The price of this item. This is the original price that the store's management paid on the item
    */
   double getPrice();

   /**
    * @return Profit percentage - How much money, in percentage with respect to the price, the store's management earns
    */
   double getProfitPercent();

   /**
    * @return Discount percentage - How much money, in percentage with respect to the price+profit, the store's management gives
    */
   double getDiscountPercent();

   /**
    * @return How many occurrences of this item there are
    */
   int getCount();

   /**
    * @return The price after adding it the profit and discount values
    */
   default double getActualPrice() {
      return getPriceWithProfit() - getDiscountValue();
   }

   /**
    * @return The price after adding it the profit value
    */
   default double getPriceWithProfit() {
      return getPrice() + getProfitValue();
   }

   /**
    * @return The profit of this item
    */
   default double getProfitValue() {
      return getPrice() * (getProfitPercent() / 100.0);
   }

   /**
    * @return The discount of this item
    */
   default double getDiscountValue() {
      return getPriceWithProfit() * (getDiscountPercent() / 100.0);
   }
}
