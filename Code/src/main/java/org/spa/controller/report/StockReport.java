package org.spa.controller.report;

import org.spa.controller.SPAApplication;
import org.spa.controller.item.WarehouseItem;

import java.util.List;

public class StockReport extends Report {
   public StockReport() {
      super();
   }

   public List<WarehouseItem> getItems() {
      return SPAApplication.getInstance().getItemsWarehouse().getItems();
   }
}
