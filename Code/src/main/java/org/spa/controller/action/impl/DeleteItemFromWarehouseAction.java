package org.spa.controller.action.impl;

public class DeleteItemFromWarehouseAction extends AbstractAction<Void> {
   @Override
   public Void execute() {
      context.getItemsWarehouse().removeItem(context.getValue("itemId"));
      return null;
   }
}
