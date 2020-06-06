package org.spa.controller.report;

import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.order.OrderSystem;
import org.spa.model.Order;
import java.util.List;
import java.util.Map;


public class ReportSystem {
    private static final Logger logger = LoggerFactory.getLogger(OrderSystem.class);
    private Map<String, Order> orderMap;
    private List<WarehouseItem> itemList;
    public ReportSystem() {
    }

    public void generateStockReport(Map<String, Order>orderMap){
        orderMap = SPAApplication.getInstance().getOrderSystem().getOrdersMap();
    }

}

