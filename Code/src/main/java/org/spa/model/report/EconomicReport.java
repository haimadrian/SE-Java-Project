package org.spa.model.report;

import org.spa.common.SPAApplication;
import org.spa.controller.item.WarehouseItem;
import org.spa.model.Item;
import org.spa.model.Order;

import java.util.Date;
import java.util.Map;

public class EconomicReport extends Report {
    private double incoming;
    private double expenses;
    private Date dateStart;
    private Date dateEnd;

    public EconomicReport(String reportID, Date dateStart, Date dateEnd) {
        super(reportID);
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public double getIncoming() {

        int sum = 0;
        Map<String, Order> ordersMap =SPAApplication.getInstance().getOrderSystem().getOrdersMap();
         /*       for(Map.E<String, Order> order : ordersMap)
                {
                    for(Item item : order.getItems())
                        sum += item.getPrice();
                    });
                    return sum;
                }

        }*/return incoming;
    }

    public double getExpenses() {
        return expenses;
    }

    public double calculateTotalProfit(){
        return incoming - expenses;
    }
}
