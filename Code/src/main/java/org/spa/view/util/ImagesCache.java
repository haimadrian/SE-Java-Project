package org.spa.view.util;

import org.spa.controller.SPAApplication;
import org.spa.controller.util.StringUtils;
import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Caching images mapped to their path, to ease images loading and avoid of duplicating images
 * in the application.<br/>
 * Note that this cache is persistent. Use the {@link #start()} to load stored images from local disk and use {@link #stop()}
 * to save non-existing images at the local disk
 *
 * @author Haim Adrian
 * @since 16-May-20
 */
public class ImagesCache {
   private static final ImagesCache instance = new ImagesCache();
   private static final String IMAGE_NOT_FOUND = "IMAGE_NOT_FOUND.png";
   private static Logger logger = LoggerFactory.getLogger(ImagesCache.class);

   private final Map<String, ImageIcon> cache;
   private File imagesDir;

   private ImagesCache() {
      cache = new ConcurrentHashMap<>();
      imagesDir = new File(SPAApplication.getWorkingDirectory(), "images");
   }

   public static ImagesCache getInstance() {
      return instance;
   }

   private static Class<?> getCallingClass() {
      try {
         // 1 - this method
         // 2 - a calling method inside this class
         // 3 - the caller to the calling method
         return Class.forName(Thread.currentThread().getStackTrace()[3].getClassName());
      } catch (ClassNotFoundException e) {
         logger.error("Could not find class.. This should never happen and still..", e);
         return null;
      }
   }

   /**
    * Use this method to find an image in the resources of the application or from disk. This method is
    * efficient as it will put in cache a new image in case we could find it, so next time you call it the
    * image will be retrieved from in-memory.
    *
    * @param imageName The path to the image. Can be name of the image in case it sits as a resource with the same structure of calling class, or disk path.
    * @return The image or <code>null</code> in case it could not be found
    */
   public ImageIcon getImage(String imageName) {
      String legalImageName = StringUtils.toLegalFileName(imageName);
      ImageIcon image = cache.get(legalImageName);
      if (image == null) {
         try {
            // Use our class to resolve the same package
            Class<?> callingClass = getCallingClass();
            InputStream resource = callingClass.getResourceAsStream(legalImageName);
            if (resource == null) {
               // Fallback to disk
               try {
                  resource = new FileInputStream(new File(imagesDir, legalImageName));
               } catch (FileNotFoundException e) {
                  logger.error("Image with path '" + legalImageName + "' cannot be found. Tried resource relative to class - " + callingClass + ", and also direct path from disk.");
                  resource = null;
               }
            }

            if (resource != null) {
               try {
                  image = new ImageIcon(ImageIO.read(resource));
                  image.setDescription(legalImageName); // So we will use this description in tooltips
                  cache.put(legalImageName, image);
               } finally {
                  resource.close();
               }
            }
         } catch (Exception e) {
            image = null;
            logger.error("Could not find resource: " + legalImageName, e);
         }
      }

      return image == null ? getImage(IMAGE_NOT_FOUND) : image;
   }

   /**
    * Load images from local disk
    */
   public void start() {
      if (imagesDir.exists()) {
         logger.info("Start loading images from disk");
         for (File image : imagesDir.listFiles()) {
            if (image.isFile()) {
               loadImageFromFile(image.getName(), image);
            }
         }
         logger.info("End loading images from disk. Loaded " + cache.size() + " images");
      }
   }

   /**
    * Save cached images to local disk
    */
   public void stop() {
      logger.info("Start storing missing images to disk. There are " + cache.size() + " images total");
      AtomicInteger imagesCount = new AtomicInteger();
      imagesDir.mkdirs();
      cache.forEach((imageName, image) -> {
         File currImageFile = new File(imagesDir, imageName);
         if (!currImageFile.exists()) {
            try {
               ImageIO.write((BufferedImage) image.getImage(), imageName.substring(imageName.lastIndexOf('.') + 1), currImageFile);
               imagesCount.incrementAndGet();
            } catch (Exception e) {
               logger.error("Error has occurred while saving image to local disk: " + currImageFile);
            }
         }
      });
      logger.info("End storing images to disk. Stored " + imagesCount.get() + " images");
   }

   /**
    * Use this method when you need to load an image from file and cache it in the {@link ImagesCache}
    *
    * @param imageNameForCache What name should we map this image to in the cache
    * @param file The image file to load.
    * @return the loaded image or null in case we have failed to load it
    */
   public ImageIcon loadImageFromFile(String imageNameForCache, File file) {
      ImageIcon result = null;

      try (FileInputStream fileInput = new FileInputStream(file)) {
         result = new ImageIcon(ImageIO.read(fileInput));
         cache.put(StringUtils.toLegalFileName(imageNameForCache), result);
      } catch (Exception e) {
         logger.error("Error has occurred while loading image from local disk: " + file, e);
         result = getImage(IMAGE_NOT_FOUND);
      }

      return result;
   }

   /**
    * Use this method when you need to load an image from file and cache it in the {@link ImagesCache}
    *
    * @param imageNameForCache What name should we map this image to in the cache
    * @param url The image url to download and load into cache.
    * @return the loaded image or null in case we have failed to load it
    */
   public ImageIcon loadImageFromURL(String imageNameForCache, URL url) {
      ImageIcon result = null;

      try {
         result = new ImageIcon(ImageIO.read(url));
         cache.put(StringUtils.toLegalFileName(imageNameForCache), result);
      } catch (Exception e) {
         logger.error("Error has occurred while downloading image from URL: " + url, e);
         result = getImage(IMAGE_NOT_FOUND);
      }

      return result;
   }

   /**
    * @return A reference for unit tests to let them modify this singleton such that we can execute several tests
    */
   ImagesCacheTestAccessor getTestAccessor() {
      return new ImagesCacheTestAccessor();
   }

   class ImagesCacheTestAccessor {
      public void setImagesDir(File imagesDir) {
         ImagesCache.this.imagesDir = imagesDir;
      }

      public void setLogger(Logger logger) {
         ImagesCache.logger = logger;
      }

      public void clearCache() {
         cache.clear();
      }

      public int cacheCount() {
         return cache.size();
      }
   }
}
