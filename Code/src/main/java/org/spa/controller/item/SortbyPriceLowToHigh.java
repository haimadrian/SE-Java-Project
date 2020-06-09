package org.spa.controller.item;

import java.util.Comparator;

public class SortbyPriceLowToHigh implements Comparator<WarehouseItem>
{
   public int compare(WarehouseItem a, WarehouseItem b)
   {
      return Double.compare(a.getTotalPrice() ,b.getTotalPrice());
   }
}
