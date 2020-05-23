package org.spa.model;

import java.util.Objects;

/**
 * @author Haim Adrian
 * @since 16-May-20
 */
public class Item {
   private final String id;
   private final String name;
   private String description;
   private double price;
   private double profitPercent;
   private double discountPercent;
   private int count;

   public Item(String id, String name, String description, double price, double profitPercent, double discountPercent, int count) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.price = price;
      this.profitPercent = profitPercent;
      this.discountPercent = discountPercent;
      this.count = count;
   }

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

   public double getPrice() {
      return price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

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
