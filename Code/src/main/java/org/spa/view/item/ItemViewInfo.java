package org.spa.view.item;

import org.spa.controller.item.Item;
import org.spa.view.table.TableCellValue;
import org.spa.view.table.TableModelIfc;
import org.spa.view.util.ImagesCache;

import javax.swing.*;
import java.text.DecimalFormat;

/**
 * @author Haim Adrian
 * @since 16-May-20
 */
public class ItemViewInfo implements TableModelIfc, Item {
   /**
    * In order to win the ability to draw ads over item's image, we expose a custom attribute which is not an
    * item column. We use this at ItemColumn.Image
    */
   public static final String ADS_ATTRIBUTE_NAME = "ads";
   protected static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
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

   public ItemViewInfo(Item copy) {
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

   public void setName(String name) {
      this.name = name;
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

   public ImageIcon getImage() {
      return ImagesCache.getInstance().getImage(getName() + ".png");
   }

   /**
    * Price can be displayed in two different tables. One is the warehouse table where we display the price of each
    * item, and another is at the shopping cart, where we calculate the price based on count. Hence we use this method, to
    * be able to separate between two item view infos, such that one can derive from another and modify the format.
    *
    * @return The price, formatted for JTable as text
    */
   protected String getPriceFormattedForTable() {
      double actualPrice = getActualPrice();

      String text = decimalFormat.format(actualPrice * getCount()) + "$" + System.lineSeparator() +
            decimalFormat.format(getPriceWithProfit()) + "$ each";

      if (getDiscountPercent() != 0) {
         double savings = getDiscountValue();
         text += System.lineSeparator() + "You saved: " + decimalFormat.format(savings * getCount()) + "$";
      }

      return text;
   }

   @Override
   public Object getAttributeValue(String attributeName) {
      try {
         switch (ItemColumn.valueOf(attributeName)) {
            case Image: {
               return new TableCellValue<ItemViewInfo>(getImage(), this);
            }
            case Name: {
               return new TableCellValue<ItemViewInfo>(getName(), this);
            }
            case Description: {
               return new TableCellValue<ItemViewInfo>(getDescription(), this);
            }
            case Price: {
               return new TableCellValue<ItemViewInfo>(getPriceFormattedForTable(), this);
            }
            case Count: {
               return new TableCellValue<ItemViewInfo>(Integer.valueOf(getCount()), this);
            }
            case Cart: {
               return new TableCellValue<ItemViewInfo>(ImagesCache.getInstance().getImage("Add To Cart.png"), this);
            }
            default:
               return handleDefault(attributeName);
         }
      } catch (IllegalArgumentException e) {
         // When it is a custom cell which is not listed as enum, we get here.
         return handleDefault(attributeName);
      }
   }

   private Object handleDefault(String attributeName) {
      if (ADS_ATTRIBUTE_NAME.equalsIgnoreCase(attributeName)) {
         String ads = "";
         if (discountPercent > 0) {
            ads = decimalFormat.format(discountPercent) + "% off!";
         }
         return ads;
      }
      return null;
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
