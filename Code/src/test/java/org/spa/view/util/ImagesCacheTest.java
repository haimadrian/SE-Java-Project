package org.spa.view.util;

import org.junit.Before;
import org.junit.Test;
import org.spa.BaseTest;
import org.spa.controller.util.log.factory.LogType;
import org.spa.controller.util.log.factory.LoggerFactory;
import org.spa.util.ErrorLogHandler;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A test class for {@link ImagesCache}
 *
 * @author Haim Adrian
 * @since 05-Jun-20
 */
public class ImagesCacheTest extends BaseTest {
   private final String testImageName = "imageName.png";
   /**
    * We use it in order to redirect errors from ImagesCache into our error log such that we can
    * know that there was a problem
    */
   private ErrorLogHandler errorLogHandler;
   private File sandboxDir;

   @Before
   public void init() {
      errorLogHandler = new ErrorLogHandler(this);
      sandboxDir = getSandboxDir();

      ImagesCache.ImagesCacheTestAccessor testAccessor = ImagesCache.getInstance().getTestAccessor();
      testAccessor.setLogger(LoggerFactory.getLogger(LogType.component, errorLogHandler));
      testAccessor.setImagesDir(sandboxDir);
   }

   @Test
   public void TestLoadImageFromFile_FileDoesNotExist_FailureExpected() {
      // Act
      ImageIcon image = ImagesCache.getInstance().loadImageFromFile(testImageName, new File("DoYouKnowRoni?.png"));

      // Assert
      assertTrue("An error supposed to occur because we tried to load non existing image", errorLogHandler.hasError());
      assertEquals("Default image should be: \"IMAGE_NOT_FOUND.png\"", "IMAGE_NOT_FOUND.png", image.getDescription());
   }

   @Test
   public void TestLoadImageFromFile_FileExists_FileShouldBeCached() throws URISyntaxException {
      // Arrange
      URL defaultImage = ImagesCacheTest.class.getResource("ImageForTest.png");
      File imageFile = new File(defaultImage.toURI());

      // Act
      ImagesCache.getInstance().loadImageFromFile(testImageName, imageFile);

      // Assert
      assertFalse("No error supposed to occur because we tried to load an existing image", errorLogHandler.hasError());
      assertNotNull("Image supposed to be cached", ImagesCache.getInstance().getImage(testImageName));
   }

   @Test
   public void TestLoadImageFromURL_URLDoesNotExist_FailureExpected() throws MalformedURLException {
      // Act
      ImagesCache.getInstance().loadImageFromURL(testImageName, new URL("https://do.you.know.roni?!?!.png"));

      // Assert
      assertTrue("An error supposed to occur because we tried to load non existing image", errorLogHandler.hasError());
   }

   @Test
   public void TestLoadImageFromURL_URLExists_ImageShouldBeCached() throws MalformedURLException {
      // Arrange
      URL imageURL = new URL("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png");

      // Act
      ImagesCache.getInstance().loadImageFromURL(testImageName, imageURL);

      // Assert
      assertFalse("No error supposed to occur because we tried to load an existing image", errorLogHandler.hasError());
      assertNotNull("Image supposed to be cached", ImagesCache.getInstance().getImage(testImageName));
   }

   @Test
   public void TestImagesCachePersistent_SaveImagesToDisk_ImagesShouldBeFoundOnDisk() throws URISyntaxException {
      // Arrange
      // Call the other test so we will have an image to store
      TestLoadImageFromFile_FileExists_FileShouldBeCached();

      // Act
      ImagesCache.getInstance().stop();

      // Assert
      assertTrue(testImageName + " supposed to be stored to disk", new File(sandboxDir, testImageName).exists());
   }

   @Test
   public void TestImagesCachePersistent_LoadImagesFromDisk_ImagesShouldBeAvailableInCache() throws URISyntaxException {
      // Arrange
      ImagesCache.ImagesCacheTestAccessor testAccessor = ImagesCache.getInstance().getTestAccessor();

      // Call the other test so we will have image file on the disk
      TestImagesCachePersistent_SaveImagesToDisk_ImagesShouldBeFoundOnDisk();
      testAccessor.clearCache();
      assertEquals("Nothing should be available in cache after clear", 0, testAccessor.cacheCount());

      // Act
      ImagesCache.getInstance().start();

      // Assert
      assertNotNull("Image supposed to be available in cache", ImagesCache.getInstance().getImage(testImageName));
   }
}
