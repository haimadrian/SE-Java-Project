package org.spa.ui.util;

import org.apache.commons.text.StringEscapeUtils;
import org.spa.common.SPAApplication;
import org.spa.common.util.StringUtils;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caching images mapped to their path, to ease images loading and avoid of duplicating images
 * in the application.
 * @author hadrian
 * @since 16-May-20
 */
public class ImagesCache {
   private static final ImagesCache instance = new ImagesCache();
   private static final Logger logger = LoggerFactory.getLogger(ImagesCache.class);

   private final Map<String, ImageIcon> cache;
   private final File imagesDir;

   private ImagesCache() {
      cache = new ConcurrentHashMap<>();
      imagesDir = new File(SPAApplication.getInstance().getWorkingDirectory(), "images");
   }

   public static ImagesCache getInstance() {
      return instance;
   }

   /**
    * Use this method to find an image in the resources of the application or from disk. This method is
    * efficient as it will put in cache a new image in case we could find it, so next time you call it the
    * image will be retrieved from in-memory.
    * @param imageName The path to the image. Can be name of the image in case it sits as a resource with the same structure of calling class, or disk path.
    * @return The image or <code>null</code> in case it could not be found
    */
   public ImageIcon getImage(String imageName) {
      ImageIcon image = cache.get(imageName);
      if (image == null) {
         String legalImageName = StringUtils.toLegalFileName(imageName);
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
               image = new ImageIcon(ImageIO.read(resource));
               image.setDescription(imageName); // So we will use this description in tooltips
               cache.put(imageName, image);
            }
         } catch (Exception e) {
            image = null;
            logger.error("Could not find resource: " + imageName);
         }
      }

      return image;
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
}
