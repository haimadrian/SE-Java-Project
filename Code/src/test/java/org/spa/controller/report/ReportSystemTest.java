package org.spa.controller.report;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spa.BaseTest;
import org.spa.controller.SPAApplication;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.order.Order;
import org.spa.controller.order.OrderSystem;
import org.spa.model.report.EconomicReport;
import org.spa.model.report.OrderReport;
import org.spa.model.report.StockReport;
import org.spa.util.DummyDataForItemsWarehouse;
import org.spa.util.DummyOrders;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ReportSystemTest extends BaseTest {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.00");
    private ItemsWarehouse itemsWarehouse;
    private OrderSystem orderSystem;
    private Map<String, Order> ordersMap;

    private String outputString;
    private Date dateStart;
    private Date dateEnd;
    @Before
    public void init() {
        itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
        outputString =SPAApplication.getInstance().getReportSystem().generateStockReport();
        orderSystem = SPAApplication.getInstance().getOrderSystem();
        dateStart = new GregorianCalendar(2020, Calendar.MAY, 11).getTime();
        dateEnd =  new GregorianCalendar(2020, Calendar.AUGUST, 11).getTime();
        ordersMap = orderSystem.getOrdersMap();

        DummyDataForItemsWarehouse.fillInDummyData(true);
        DummyOrders.fillInDummyData(true);
    }

    @After
    public void cleanup() {
        List<String> orderId = new ArrayList<>();
        ordersMap.values().forEach(order -> orderId.add(order.getOrderId()));
        orderId.forEach(order-> orderSystem.deleteOrder(order));
    }
    @Test
    public void TestGenerateStockReport(){
        StockReport stockReport = new StockReport();
        List<WarehouseItem> testingList=stockReport.getItems();
        assertEquals("Supposed to be the same amount of items",testingList.size(),itemsWarehouse.getItems().size());
        assertEquals("The output string should be the same","Item name:\tIntel Core i9-9900K Coffee Lake 8-Core, 16-Thread, 95W BX80684I99900K Desktop Processor\n" +
                "Quantity:\t10\n" +
                "Item name:\tASUS ROG STRIX Z490-F GAMING LGA 1200 (Intel 10th Gen)\n" +
                "Quantity:\t5\n" +
                "Item name:\tCrucial MX500 2.5\" 1TB SATA III 3D NAND Internal Solid State Drive (SSD) CT1000MX500SSD1\n" +
                "Quantity:\t1\n" +
                "Item name:\tEVGA GeForce RTX 2060 KO ULTRA GAMING Video Card, 06G-P4-2068-KR, 6GB GDDR6, Dual Fans, Metal Backplate\n" +
                "Quantity:\t20\n" +
                "Item name:\tEVGA GeForce RTX 2080 SUPER XC ULTRA GAMING Video Card, 08G-P4-3183-KR, 8GB GDDR6, RGB LED, Metal Backplate\n" +
                "Quantity:\t15\n" +
                "Item name:\tEVGA GeForce RTX 2080 Ti GAMING Video Card, 11G-P4-2380-KR, 11GB GDDR6, RGB LED Logo, Metal Backplate\n" +
                "Quantity:\t6\n" +
                "Item name:\tMSI GeForce RTX 2080 TI GAMING X TRIO Video Card\n" +
                "Quantity:\t2\n",outputString);
    }
    @Test
    public void TestGenerateEconomicReport(){
        EconomicReport economicReport = new EconomicReport();
        double expenses = economicReport.getExpenses();
        double profit = economicReport.getIncoming();
        //assertEquals("Expenses should be ",);

    }
    @Test
    public void TestGenerateOrdersReport(){
        StringBuilder testingString= new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        StringBuilder generatedReportString = new StringBuilder();
        OrderReport orderReport1 = new OrderReport(dateStart,dateEnd);
        assertEquals("Should see X Orders between those dates",5,orderReport1.getOrders().size());
        generatedReportString.append(SPAApplication.getInstance().getReportSystem().generateOrdersReport(dateStart,dateEnd));
        ordersMap = orderReport1.getOrders().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        ordersMap.values().forEach(order ->
        {
            Order order1 = orderSystem.findOrder(order.getOrderId());
            Date convertedDate = new Date(order1.getOrderTime());
            testingString.append("Order ID: ").append(order1.getOrderId()).append("\tOrder date: ").append(sdf.format(convertedDate)).append("\n");
            order1.getItems().forEach(item ->{
                double discountPrice = item.getPrice() * item.getDiscountPercent() / 100;
                testingString.append("\tItem name: ").append(item.getName()).append("\tQuantity:\t").append(item.getCount()).append("\tTotal Price:\t").append(decimalFormat.format(item.getPrice() - discountPrice)).append("\n");
            });
            testingString.append("\n\n");
        });
        assertEquals("Faile",generatedReportString.toString(),testingString.toString());
       OrderReport orderReport2 = new OrderReport(dateEnd,dateStart);
        ordersMap = orderReport2.getOrders();
        assertEquals("We should not see any orders",0,ordersMap.size());

    }
}