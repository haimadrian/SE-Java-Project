package org.spa.controller.item;

import java.util.Comparator;

public class SortbyPriceHighToLow implements Comparator<WarehouseItem>
{
    public int compare(WarehouseItem a, WarehouseItem b)
    {
        return Double.compare(b.getTotalPrice(), a.getTotalPrice());
    }
}
