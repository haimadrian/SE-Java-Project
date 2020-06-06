package org.spa.controller.action.impl;

public class StockReportAction extends  AbstractAction<Void> {
    @Override
    public Void execute() {
        context.getItemsWarehouse().getItems();
        return null;
    }
}
