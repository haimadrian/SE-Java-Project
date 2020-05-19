package org.spa.controller.item;

import java.util.Objects;

/**
 * To share item data between different layers in the project, we create this class which has the same fields as the DAL one,
 * but here we also keep the count of each item, without knowing how the DAL keeps this information
 * @author hadrian
 * @since 16-May-20
 */
public class WarehouseItem {
   private final String id;
   private final String name;
   private String description;
   private double price;
   private double profitPercent;
   private double discountPercent;
   private int count;

   /**
    * Constructs a new {@link WarehouseItem}
    * @param id
    * @param name
    * @param description
    * @param price
    * @param profitPercent
    * @param discountPercent
    * @param count
    */
   public WarehouseItem(String id, String name, String description, double price, double profitPercent, double discountPercent, int count) {
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

   // Protected so only items warehouse can update
   void setCount(int count) {
      this.count = count;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof WarehouseItem)) return false;
      WarehouseItem item = (WarehouseItem) o;
      return id.equals(item.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public String toString() {
      return "WarehouseItem{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", count=" + count +
            '}';
   }
}
