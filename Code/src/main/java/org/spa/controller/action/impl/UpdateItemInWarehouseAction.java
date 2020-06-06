package org.spa.controller.action.impl;

import java.util.concurrent.TimeUnit;

public class UpdateItemInWarehouseAction extends AbstractAction<Void> {
    @Override
    public Void execute() {
        String userId = context.getUserManagement().getLoggedInUser().getUserId();
        context.getItemsWarehouse().updateItem(userId, context.getValue("itemId"));

        return null;
    }
}