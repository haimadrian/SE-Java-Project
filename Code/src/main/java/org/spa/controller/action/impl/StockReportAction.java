package org.spa.controller.action.impl;

public class StockReportAction extends AbstractAction<String> {
    @Override
    public String execute() {
        return context.getReportSystem().generateStockReport(context.getItemsWarehouse().getItems());
    }
}
