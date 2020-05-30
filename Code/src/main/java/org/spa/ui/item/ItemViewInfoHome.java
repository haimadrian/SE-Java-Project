package org.spa.ui.item;

/**
 * @author Haim Adrian
 * @since 31-May-20
 */
public class ItemViewInfoHome extends ItemViewInfo {
   public ItemViewInfoHome(String id, String category, String name, String description, double price, double profitPercent, double discountPercent, int count) {
      super(id, category, name, description, price, profitPercent, discountPercent, count);
   }

   public ItemViewInfoHome(ItemViewInfo item) {
      super(item.getId(), item.getCategory(), item.getName(), item.getDescription(),
            item.getPrice(), item.getProfitPercent(), item.getDiscountPercent(), item.getCount());
   }

   @Override
   protected String getPriceFormattedForTable() {
      double actualPrice = getActualPrice();

      String text = decimalFormat.format(actualPrice) + "$";

      if (getDiscountPercent() != 0) {
         double savings = getDiscountValue();
         text += System.lineSeparator() + "Instead of: " + decimalFormat.format(getPriceWithProfit()) + "$";
         text += System.lineSeparator() + "You save: " + decimalFormat.format(savings) + "$";
      }

      return text;
   }
}
