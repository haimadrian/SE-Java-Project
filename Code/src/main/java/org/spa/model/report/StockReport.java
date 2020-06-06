package org.spa.model.report;

import org.spa.common.SPAApplication;
import org.spa.controller.item.WarehouseItem;

import java.util.Date;
import java.util.List;

public class StockReport extends Report {
    public StockReport(String reportID) {
        super(reportID);
    }

    public List<WarehouseItem> getItems() {
       return SPAApplication.getInstance().getItemsWarehouse().getItems();
    }
}
