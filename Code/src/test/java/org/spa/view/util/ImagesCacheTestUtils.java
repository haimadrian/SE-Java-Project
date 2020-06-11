package org.spa.view.util;

import java.io.File;

/**
 * @author Haim Adrian
 * @since 05-Jun-20
 */
public class ImagesCacheTestUtils {
   /**
    * Give test classes the ability to modify the images cache directory, as the ImageCache's test accessor is package private.
    *
    * @param imagesDir The directory to set
    */
   public static void setImagesDirectory(File imagesDir) {
      ImagesCache.getInstance().getTestAccessor().setImagesDir(imagesDir);
   }
}
