package org.spa.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.spa.controller.item.Item;

import java.util.Objects;

/**
 * @author Haim Adrian
 * @since 16-May-20
 */
public class ItemImpl implements Item {
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
   public ItemImpl(@JsonProperty(value = "id") String id,
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

   public ItemImpl(Item copy) {
      this(copy.getId(), copy.getCategory(), copy.getName(), copy.getDescription(), copy.getPrice(),
            copy.getProfitPercent(), copy.getDiscountPercent(), copy.getCount());
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
      if (!(o instanceof ItemImpl)) return false;
      ItemImpl item = (ItemImpl) o;
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
