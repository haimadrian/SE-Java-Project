package org.spa.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Haim Adrian
 * @since 16-May-20
 */
public class Item {
   @JsonProperty
   private String id;

   @JsonProperty
   private String category;

   @JsonProperty
   private String name;

   @JsonProperty
   private String description;

   @JsonProperty
   private double price;

   @JsonProperty
   private double profitPercent;

   @JsonProperty
   private double discountPercent;

   @JsonProperty
   private int count;

   //@formatter:off
   @JsonCreator
   public Item(@JsonProperty(value = "id") String id,
               @JsonProperty(value = "category") String category,
               @JsonProperty(value = "name") String name,
               @JsonProperty(value = "description") String description,
               @JsonProperty(value = "price") double price,
               @JsonProperty(value = "profitPercent") double profitPercent,
               @JsonProperty(value = "discountPercent") double discountPercent,
               @JsonProperty(value = "count") int count) {
      this.id = id;
      this.category = category;
      this.name = name;
      this.description = description;
      this.price = price;
      this.profitPercent = profitPercent;
      this.discountPercent = discountPercent;
      this.count = count;
   }
   //@formatter:on

   /**
    * @return The price after adding it the profit and discount values
    */
   public double getActualPrice() {
      return getPriceWithProfit() - getDiscountValue();
   }

   /**
    * @return The price after adding it the profit value
    */
   public double getPriceWithProfit() {
      return getPrice() + getProfitValue();
   }

   /**
    * @return The profit of this item
    */
   public double getProfitValue() {
      return getPrice() * (getProfitPercent() / 100.0);
   }

   /**
    * @return The discount of this item
    */
   public double getDiscountValue() {
      return getPriceWithProfit() * (getDiscountPercent() / 100.0);
   }

   public String getCategory() {return category;}

   public void setCategory(String category){this.category= category;}

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public double getPrice() { return price; }

   public void setPrice(double price) { this.price = price; }

   public double getProfitPercent() {
      return profitPercent;
   }

   public void setProfitPercent(double profitPercent) {
      this.profitPercent = profitPercent;
   }

   public double getDiscountPercent() {
      return discountPercent;
   }

   public void setDiscountPercent(double discountPercent) {
      this.discountPercent = discountPercent;
   }

   public int getCount() {
      return count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Item)) return false;
      Item item = (Item) o;
      return id.equals(item.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public String toString() {
      return "Item{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", profitPercent=" + profitPercent +
            ", discountPercent=" + discountPercent +
            '}';
   }
}
