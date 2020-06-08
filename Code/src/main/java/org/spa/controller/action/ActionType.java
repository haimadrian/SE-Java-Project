package org.spa.controller.action;

import org.spa.controller.action.impl.*;
import org.spa.controller.action.impl.StockReportAction;
import org.spa.model.report.StockReport;

/**
 * @author Haim Adrian, Lior Shor, Idan Pollak
 * @since 23-May-20
 */
public enum ActionType {
   ClearCart(ClearCartAction.class),
   RemoveFromCart(RemoveFromCartAction.class),
   Purchase(PurchaseAction.class),
   GenerateStockReport(StockReportAction.class),
   CreateItemInWarehouse(CreateItemInWarehouseAction.class),
   DeleteItemFromWarehouse(DeleteItemFromWarehouseAction.class),
   UpdateItemInWarehouse(UpdateItemInWarehouseAction.class);


   final Class<? extends Action<?>> clazz;

   ActionType(Class<? extends Action<?>> clazz) {
      this.clazz = clazz;
   }

   public <R, T extends Action<R>> T newInstance() throws IllegalAccessException, InstantiationException {
      T action = (T)clazz.newInstance();
      return action;
   }
}
