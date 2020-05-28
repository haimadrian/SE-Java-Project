package org.spa.ui.item;

import org.spa.ui.table.TableModelIfc;
import org.spa.ui.util.ImagesCache;

import javax.swing.*;
import java.text.DecimalFormat;

/**
 * @author Haim Adrian
 * @since 16-May-20
 */
public class ItemViewInfo implements TableModelIfc {
   private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
   private final String id;
   private String category;
   private String name;
   private String description;
   private double price;
   private double profitPercent;
   private double discountPercent;
   private int count;

   public ItemViewInfo(String id, String category, String name, String description, double price, double profitPercent, double discountPercent, int count) {
      this.id = id;
      this.category = category;
      this.name = name;
      this.description = description;
      this.price = price;
      this.profitPercent = profitPercent;
      this.discountPercent = discountPercent;
      this.count = count;
   }

   public String getCategory() {return category;}

   public void setCategory(String category) {this.category = category;}

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
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

   public ImageIcon getImage() {
      return ImagesCache.getInstance().getImage(getName() + ".png");
   }

   /**
    * Price can be displayed in two different tables. One is the warehouse table where we display the price of each
    * item, and another is at the shopping cart, where we calculate the price based on count. Hence we use this method, to
    * be able to separate between two item view infos, such that one can derive from another and modify the format.
    * @return The price, formatted for JTable as text
    */
   protected String getPriceFormattedForTable() {
      String text = "" + decimalFormat.format(getPrice() * getCount()) + "$" + System.lineSeparator() +
            Double.valueOf(getPrice()).toString() + "$ each";
      return text;
   }

   @Override
   public Object getAttributeValue(String attributeName) {
      switch (ItemColumn.valueOf(attributeName)) {
         case Image: {
            return getImage();
         }
         case Name: {
            return getName();
         }
         case Description: {
            return getDescription();
         }
         case Price: {
            return getPriceFormattedForTable();
         }
         case Count: {
            return Integer.valueOf(getCount());
         }
         default:
            return null;
      }
   }

   @Override
   public void setAttributeValue(String attributeName, Object value) {
      switch (ItemColumn.valueOf(attributeName)) {
         case Name: {
            setName(String.valueOf(value));
            break;
         }
         case Description: {
            setDescription(String.valueOf(value));
            break;
         }
         case Price: {
            setPrice(Double.parseDouble(String.valueOf(value).replace("$", "")));
            break;
         }
         case Count: {
            setCount(Integer.parseInt(String.valueOf(value)));
            break;
         }
         default:
            // Do nothing
      }
   }

   @Override
   public String toString() {
      return "ItemViewInfo{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            '}';
   }
}
