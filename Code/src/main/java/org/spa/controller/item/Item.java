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
   String getId();
   String getName();
   String getCategory();
   String getDescription();
   double getPrice();
   double getProfitPercent();
   double getDiscountPercent();
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
