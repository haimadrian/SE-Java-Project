package org.spa.view.item;

import org.junit.Before;
import org.junit.Test;
import org.spa.BaseTest;
import org.spa.view.util.ImagesCache;
import org.spa.view.util.ImagesCacheTest;
import org.spa.view.util.ImagesCacheTestUtils;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Test class for {@link ItemViewInfo}
 *
 * @author Haim Adrian
 * @since 05-Jun-20
 */
public class ItemViewInfoTest extends BaseTest {
   private File sandboxDir;

   private static ItemViewInfo createItemWithPricing(double price, double profit, double discount, int count) {
      return new ItemViewInfo("#1234, ", "cat", "item", "desc", price, profit, discount, count);
   }

   @Before
   public void init() {
      sandboxDir = getSandboxDir();
      ImagesCacheTestUtils.setImagesDirectory(sandboxDir);
   }

   @Test
   public void TestItemImage_ItemImageExists_MakeSureItIsAvailableForItemViewInfo() throws URISyntaxException {
      // Arrange
      String itemName = "testItem";
      URL defaultImage = ImagesCacheTest.class.getResource("ImageForTest.png");
      File imageFile = new File(defaultImage.toURI());
      ImagesCache.getInstance().loadImageFromFile(itemName + ".png", imageFile);
      ItemViewInfo item = new ItemViewInfo("#1234, ", "cat", itemName, "desc", 10, 5, 2, 5);

      // Act
      ImageIcon itemImage = item.getImage();

      // Assert
      assertNotNull("Item image supposed to be retrieved as we loaded ImageForTest.png under the item name: " + itemName, itemImage);
   }

   @Test
   public void TestActualPrice_SetPriceOnly_ActualPriceEqualsToPrice() {
      // Arrange
      ItemViewInfo item = createItemWithPricing(10, 0, 0, 1);

      // Act
      double actualPrice = item.getActualPrice();

      // Assert
      assertEquals("Actual price supposed to be equal to price", item.getPrice(), actualPrice, 0);
   }

   @Test
   public void TestActualPrice_SetPriceAndDiscountOnly_ActualPriceShouldConsiderDiscount() {
      // Arrange
      ItemViewInfo item = createItemWithPricing(10, 0, 2, 1);

      // Act
      double actualPrice = item.getActualPrice();

      // Assert
      assertEquals("Discount value expected to be price * (discount/100)", (item.getDiscountPercent() / 100.0) * item.getPrice(), item.getDiscountValue(), 0);
      assertEquals("Actual price supposed to be equal to price - discount", item.getPrice() - item.getDiscountValue(), actualPrice, 0);
   }

   @Test
   public void TestActualPrice_SetPriceAndProfitOnly_ActualPriceShouldConsiderProfit() {
      // Arrange
      ItemViewInfo item = createItemWithPricing(10, 2, 0, 1);

      // Act
      double actualPrice = item.getActualPrice();

      // Assert
      assertEquals("Profit value expected to be price * (profit/100)", (item.getProfitPercent() / 100.0) * item.getPrice(), item.getProfitValue(), 0);
      assertEquals("Actual price supposed to be equal to price + profit", item.getPrice() + item.getProfitValue(), actualPrice, 0);
   }

   @Test
   public void TestActualPrice_SetPriceAndDiscountAndProfit_ActualPriceShouldConsiderDiscountAndProfit() {
      // Arrange
      ItemViewInfo item = createItemWithPricing(10, 5, 2, 1);

      // Act
      double actualPrice = item.getActualPrice();

      // Assert
      assertEquals("Actual price supposed to be equal to price + profit - discount", item.getPrice() + item.getProfitValue() - item.getDiscountValue(), actualPrice, 0);
   }

   @Test
   public void TestPriceFormattedForTable_SetPriceAndDiscountAndProfitAndCount_ActualPriceShouldConsiderDiscountAndProfitAndCount() {
      // Arrange
      ItemViewInfo item = createItemWithPricing(10, 35, 20, 5);
      String expected = "54$" + System.lineSeparator() +
            "13.5$ each" + System.lineSeparator() +
            "You saved: 13.5$";

      // Act
      String priceForShoppingCartTable = item.getPriceFormattedForTable();

      // Assert
      assertEquals("Actual price supposed to be equal to (price + profit - discount) * count", expected, priceForShoppingCartTable);
   }
}
