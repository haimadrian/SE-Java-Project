package org.spa.controller.action.impl;

public class CreateItemInWarehouseAction extends AbstractAction<Void> {
    @Override
    public Void execute() {
        context.getItemsWarehouse().addItem(context.getValue("item"));
        return null;
    }
}