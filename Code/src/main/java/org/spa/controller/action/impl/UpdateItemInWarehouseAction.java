package org.spa.controller.action.impl;

public class UpdateItemInWarehouseAction extends AbstractAction<Void> {
    @Override
    public Void execute() {
        context.getItemsWarehouse().updateItem(context.getValue("item"));
        return null;
    }
}