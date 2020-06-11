package org.spa.controller.item;


import java.util.Objects;

/**
 * To share item data between different layers in the project, we create this class which has the same fields as the DAL one,
 * but here we also keep the count of each item, without knowing how the DAL keeps this information
 *
 * @author Haim Adrian
 * @since 16-May-20
 */
public class WarehouseItem implements Item {
   private final String id;
   private final String name;
   private String category;
   private String description;
   private double price;
   private double profitPercent;
   private double discountPercent;
   private int count;

   /**
    * Constructs a new {@link WarehouseItem}
    *
    * @param id
    * @param category
    * @param name
    * @param description
    * @param price
    * @param profitPercent
    * @param discountPercent
    * @param count
    */
   public WarehouseItem(String id, String category, String name, String description, double price, double profitPercent, double discountPercent, int count) {
      this.id = id;
      this.category = category;
      this.name = name;
      this.description = description;
      this.price = price;
      this.profitPercent = profitPercent;
      this.discountPercent = discountPercent;
      this.count = count;
   }

   /**
    * Constructs a copy of an {@link Item}
    *
    * @param another The item to copy data from
    */
   public WarehouseItem(Item another) {
      this.id = another.getId();
      this.category = another.getCategory();
      this.name = another.getName();
      this.description = another.getDescription();
      this.price = another.getPrice();
      this.profitPercent = another.getProfitPercent();
      this.discountPercent = another.getDiscountPercent();
      this.count = another.getCount();
   }

   @Override
   public String getCategory() {
      return category;
   }

   public void setCategory(String category) {
      this.category = category;
   }

   @Override
   public String getId() {
      return id;
   }

   @Override
   public String getName() {
      return name;
   }

   @Override
   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   @Override
   public double getPrice() {
      return price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

   @Override
   public double getProfitPercent() {
      return profitPercent;
   }

   public void setProfitPercent(double profitPercent) {
      this.profitPercent = profitPercent;
   }

   @Override
   public double getDiscountPercent() {
      return discountPercent;
   }

   public void setDiscountPercent(double discountPercent) {
      this.discountPercent = discountPercent;
   }

   @Override
   public int getCount() {
      return count;
   }

   public void setCount(int count) {
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
