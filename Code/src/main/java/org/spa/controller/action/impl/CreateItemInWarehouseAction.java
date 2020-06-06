package org.spa.controller.action.impl;

import java.util.concurrent.TimeUnit;

public class CreateItemInWarehouseAction extends AbstractAction<Void> {
    @Override
    public Void execute() {
        String userId = context.getUserManagement().getLoggedInUser().getUserId();
        context.getItemsWarehouse().addItem(userId, context.getValue("itemId"));

        return null;
    }
}