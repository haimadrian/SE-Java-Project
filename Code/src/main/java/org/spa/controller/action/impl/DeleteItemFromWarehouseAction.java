package org.spa.controller.action.impl;

import java.util.concurrent.TimeUnit;

public class DeleteItemFromWarehouseAction extends AbstractAction<Void> {
    @Override
    public Void execute() {
        String userId = context.getUserManagement().getLoggedInUser().getUserId();
        context.getItemsWarehouse().removeItem(userId, context.getValue("itemId"));
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        } catch (InterruptedException ignore) {
        }

        return null;
    }
}