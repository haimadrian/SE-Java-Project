package org.spa.model.dal;

import org.spa.common.Repository;
import org.spa.model.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class OrderRepository implements Repository<Order> {
    List<Order> dummy;
    @Override
    public List<Order> selectAll() {

        dummy = new ArrayList<>();
        //TODO add dummy orders, ask what needed

        return dummy;
    }

    @Override
    public List<Order> select(Predicate<Order> filter) {
        return selectAll();
    }

    @Override
    public Order add(Order order) {

        return order;
    }

    @Override
    public List<Order> saveAll(List<Order> orders) {
        // TODO implement save
        return orders;
    }
}
