package org.spa.ui.order;
import org.spa.model.Order;

public class OrderCopying {
    private OrderCopying() {

    }

    public static OrderViewInfo orderToOrderViewInfo(Order order) {
        if (order == null) {
            return null;
        }

        return new OrderViewInfo(order.getOrderId(),order.getOrderTime(), order.getUserId(), order.getItems());
    }

    /**
     * A converter from {@link OrderViewInfo} to {@link Order}
     * @param order The item to convert
     * @return The conversion result
     */
    public static Order orderViewInfoToOrder(OrderViewInfo order) {
        if (order == null) {
            return null;
        }

        return new Order(order.getOrderId(),order.getOrderTime(), order.getUserId(), order.getItems());
    }
}
