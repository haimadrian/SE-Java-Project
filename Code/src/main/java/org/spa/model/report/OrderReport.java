package org.spa.model.report;

import org.spa.common.SPAApplication;
import org.spa.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderReport extends Report {
    private Date dateStart;
    private Date dateEnd;

    public OrderReport(String reportID, Date dateStart, Date dateEnd) {
        super(reportID);
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public Map<String, Order> getOrders() {
        Map<String, Order> orderMap = new HashMap<>();
        SPAApplication.getInstance().getOrderSystem().getOrdersMap().values().stream().forEach(order ->
        {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
            Date convertedDate = new Date(order.getOrderTime());
            if( dateStart.before(convertedDate) && dateEnd.after(convertedDate))
                orderMap.put(order.getOrderId(),order);
        });
        return orderMap;
    }
}
