package org.spa.ui.order;

import org.spa.model.Item;
import org.spa.ui.table.TableCellValue;
import org.spa.ui.table.TableModelIfc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderViewInfo implements TableModelIfc {

    private final String orderId;
    private long orderTime;
    private List<Item> items;
    private String userId;

    public OrderViewInfo(String orderId, long orderTime, String userId, List<Item> items) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.items = items;
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public String getConvertedOrderTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date convertedDate = new Date(orderTime);
        return (sdf.format(convertedDate));
    }

    public String getUserId() {
        return userId;
    }

    public String getItemsToString() {
        String itemsToString = "";
        int count = 0;
        int sumPrice = 0;
        for (Item item : items) {
            count+= item.getCount();
            // TODO: calculate actual price before aggregating it
            sumPrice+= item.getPrice();
        }
        itemsToString = "Amount of items: " + count + System.lineSeparator() + "Total Price: " + sumPrice + "$";
        return itemsToString;
    }

    public List<Item> getItems(){
        return items;
    }

    @Override
    public Object getAttributeValue(String attributeName) {

        switch (OrderColumn.valueOf(attributeName)) {
            case OrderId: {
                return new TableCellValue<OrderViewInfo>(getOrderId(), this);
            }
            case OrderTime: {
                return new TableCellValue<OrderViewInfo>(getConvertedOrderTime(), this);
            }
            case Summary: {
                return new TableCellValue<OrderViewInfo>(getItemsToString(), this);
            }
            case UserId: {
                return new TableCellValue<OrderViewInfo>(getUserId(), this);
            }
            default:
                //do nothing
                return "";
        }
    }

    @Override
    public void setAttributeValue(String attributeName, Object value) {
    }

    @Override
    public String toString() {
        return "OrderViewInfo{" +
                "id='" + orderId + '}';
    }
}
