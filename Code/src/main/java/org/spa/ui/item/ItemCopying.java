package org.spa.ui.item;

import org.spa.controller.item.WarehouseItem;

/**
 * @author Haim Adrian
 * @since 23-May-20
 */
public class ItemCopying {
   private ItemCopying() {

   }

   /**
    * A converter from {@link WarehouseItem} to {@link ItemViewInfo}
    * @param item The item to convert
    * @return The conversion result
    */
   public static ItemViewInfo warehouseItemToItemViewInfo(WarehouseItem item) {
      if (item == null) {
         return null;
      }

      return new ItemViewInfo(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getProfitPercent(), item.getDiscountPercent(), item.getCount());
   }

   /**
    * A converter from {@link ItemViewInfo} to {@link WarehouseItem}
    * @param item The item to convert
    * @return The conversion result
    */
   public static WarehouseItem itemViewInfoToWarehouseItem(ItemViewInfo item) {
      if (item == null) {
         return null;
      }

      return new WarehouseItem(item.getId(), item.getName(), item.getDescription(), item.getPrice(), item.getProfitPercent(), item.getDiscountPercent(), item.getCount());
   }
}
