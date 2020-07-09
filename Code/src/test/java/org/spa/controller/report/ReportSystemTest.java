package org.spa.controller.report;

import org.junit.Before;
import org.junit.Test;
import org.spa.BaseTest;
import org.spa.controller.SPAApplication;
import org.spa.controller.item.Item;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.order.DummyOrders;
import org.spa.controller.order.Order;
import org.spa.controller.order.OrderSystem;
import org.spa.model.report.EconomicReport;
import org.spa.model.report.OrderReport;
import org.spa.model.report.StockReport;
import org.spa.util.DummyDataForItemsWarehouse;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

      DummyDataForItemsWarehouse.fillInDummyData(true);
      DummyOrders.fillInDummyData(true);
      itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
      outputString = SPAApplication.getInstance().getReportSystem().generateStockReport();
      orderSystem = SPAApplication.getInstance().getOrderSystem();
      dateStart = new GregorianCalendar(2020, Calendar.MAY, 11).getTime();
      dateEnd = new GregorianCalendar(2020, Calendar.AUGUST, 11).getTime();
      ordersMap = orderSystem.getOrdersMap();

   }

   @Test
   public void TestGenerateStockReport() {
      StockReport stockReport = new StockReport();
      List<WarehouseItem> testingList = stockReport.getItems();
      assertEquals("Supposed to be the same amount of items", testingList.size(), itemsWarehouse.getItems().size());
      assertEquals("The output string should be the same", "Item name: Intel Core i9-9900K Coffee Lake 8-Core, 16-Thread, 95W BX80684I99900K Desktop Processor\n" +
              "Quantity: 10\n" +
              "\n" +
              "-----------------------------------------------------------------------------------------------------------------------------------------------------\n" +
              "\n" +
              "Item name: ASUS ROG STRIX Z490-F GAMING LGA 1200 (Intel 10th Gen)\n" +
              "Quantity: 5\n" +
              "\n" +
              "-----------------------------------------------------------------------------------------------------------------------------------------------------\n" +
              "\n" +
              "Item name: Crucial MX500 2.5\" 1TB SATA III 3D NAND Internal Solid State Drive (SSD) CT1000MX500SSD1\n" +
              "Quantity: 1\n" +
              "\n" +
              "-----------------------------------------------------------------------------------------------------------------------------------------------------\n" +
              "\n" +
              "Item name: EVGA GeForce RTX 2060 KO ULTRA GAMING Video Card, 06G-P4-2068-KR, 6GB GDDR6, Dual Fans, Metal Backplate\n" +
              "Quantity: 20\n" +
              "\n" +
              "-----------------------------------------------------------------------------------------------------------------------------------------------------\n" +
              "\n" +
              "Item name: EVGA GeForce RTX 2080 SUPER XC ULTRA GAMING Video Card, 08G-P4-3183-KR, 8GB GDDR6, RGB LED, Metal Backplate\n" +
              "Quantity: 15\n" +
              "\n" +
              "-----------------------------------------------------------------------------------------------------------------------------------------------------\n" +
              "\n" +
              "Item name: EVGA GeForce RTX 2080 Ti GAMING Video Card, 11G-P4-2380-KR, 11GB GDDR6, RGB LED Logo, Metal Backplate\n" +
              "Quantity: 6\n" +
              "\n" +
              "-----------------------------------------------------------------------------------------------------------------------------------------------------\n" +
              "\n" +
              "Item name: MSI GeForce RTX 2080 TI GAMING X TRIO Video Card\n" +
              "Quantity: 2\n" +
              "\n" +
              "-----------------------------------------------------------------------------------------------------------------------------------------------------\n" +
              "\n", outputString);
   }

   @Test
   public void TestGenerateEconomicReport() {
      String orderId = ordersMap.keySet().stream().findFirst().get();
      EconomicReport economicReport = new EconomicReport();
      Map<String, Double> test = economicReport.getTotalProfitPerItem();
      Map<String, Double> profitPerItem = new HashMap<>();
      Order order = orderSystem.findOrder(orderId);
      profitPerItem = economicReport.getProfitPerItem(profitPerItem);
      Item item = order.getItems().get(0);
      assertNotSame("Expenses should be different from profit ", economicReport.getExpenses(), economicReport.getIncoming());
      assertEquals("Profit of this item should be the same - 2 orders", item.getActualPrice() * item.getCount() * 2, profitPerItem.get(item.getName()));
      profitPerItem = economicReport.getExpensesPerItem(profitPerItem);
      item = order.getItems().get(0);
      assertEquals("Total profit from the item should be the same", test.values().stream().findFirst(), profitPerItem.values().stream().findFirst());
      profitPerItem.clear();
      profitPerItem = economicReport.getExpensesPerItem(profitPerItem);
      assertEquals("Expenses should have the same value", -profitPerItem.get(item.getName()), (item.getPrice() * item.getCount()));
   }

   @Test
   public void TestGenerateOrdersReport() {
      StringBuilder testingString = new StringBuilder();
      SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
      StringBuilder generatedReportString = new StringBuilder();
      OrderReport orderReport1 = new OrderReport(dateStart, dateEnd);
      assertEquals("Should see 1 Orders between those dates", 1, orderReport1.getOrders().size());
      generatedReportString.append(SPAApplication.getInstance().getReportSystem().generateOrdersReport(dateStart, dateEnd));
      ordersMap.values().forEach(order ->
      {
         Order order1 = orderSystem.findOrder("11111");
         Date convertedDate = new Date(order1.getOrderTime());
         testingString.append("Order ID: ").append(order1.getOrderId()).append("\nOrder date: ").append(sdf.format(convertedDate)).append("\n");
         order1.getItems().forEach(item -> {
            double discountPrice = item.getPrice() * item.getDiscountPercent() / 100;
            testingString.append("Item name: ").append(item.getName()).append("\nQuantity: ").append(item.getCount()).append("\nTotal Price: ").append(decimalFormat.format(item.getPrice() - discountPrice)).append("\n");
         });
         testingString.append("\n\n--------------------------------------------------------------------------------------------------------------------\n\n");
      });
      assertEquals("String output check", generatedReportString.toString(), testingString.toString());
      OrderReport orderReport2 = new OrderReport(dateEnd, dateStart);
      ordersMap = orderReport2.getOrders();
      assertEquals("We should not see any orders if End shows before start", 0, ordersMap.size());

   }
}
