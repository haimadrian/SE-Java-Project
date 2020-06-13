package org.spa.controller.report;

import org.junit.Before;
import org.junit.Test;
import org.spa.BaseTest;
import org.spa.controller.SPAApplication;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.model.report.StockReport;
import org.spa.util.DummyDataForItemsWarehouse;

import java.util.List;

public class ReportSystemTest extends BaseTest {
    private ItemsWarehouse itemsWarehouse;
    private String outputString;

    @Before
    public void init() {
        itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
        outputString =SPAApplication.getInstance().getReportSystem().generateStockReport();
        DummyDataForItemsWarehouse.fillInDummyData(true);
    }

    @Test
    public void TestGenerateStockReport(){
        StockReport stockReport = new StockReport();
        List<WarehouseItem> testingList=stockReport.getItems();
        assertEquals("Supposed to be the same amount of items",testingList.size(),itemsWarehouse.getItems().size());
        assertEquals("he","Item name:\tIntel Core i9-9900K Coffee Lake 8-Core, 16-Thread, 95W BX80684I99900K Desktop Processor\n" +
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
}