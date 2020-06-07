package org.spa;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.spa.common.util.log.Level;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LogType;
import org.spa.common.util.log.factory.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Base class for all unit test classes in the application<br/>
 * This class exposes a logging functionality to let derived classes log easily, and also wrap the {@link Assert}
 * class method to perform assertions.<br/>
 * Another useful one is the {@link #getSandboxDir()} which prepares an empty test directory for derived class
 * where it can work with the file system. This directory is being cleared on every call to getSandboxDir, so
 * make sure you keep a reference in case you are within a single test case. Otherwise you will clear the data
 *
 * @author Haim Adrian
 * @since 05-Jun-20
 */
public abstract class BaseTest implements Logger {
   private static final FastDateFormat dateTimeFormatter = FastDateFormat.getInstance("dd/MM/yy HH:mm:ss.SSS");

   /**
    * Giving derived classes a managed sandbox directory to work with file system.<br/>
    * This directory is getting created and deleted by this class, so don't fucking mess with it.
    */
   private static File sandboxDir;

   private final Logger logger;

   /**
    * Constructs a {@link BaseTest}
    */
   public BaseTest() {
      logger = LoggerFactory.getLogger(getClass());
   }

   @BeforeClass
   public static void setupClass() {

   }

   @AfterClass
   public static void cleanupClass() {
      if (sandboxDir != null) {
         deleteDirectoryAndContents(sandboxDir);
         sandboxDir = null;
      }
   }


   /**
    * Creates a new temporary directory for unit tests, to act as a sandbox for working with file system.<br/>
    * In case the directory has already been created, we delete it and clear its contents, then recreating it.<br/>
    * Note that when cleaning up unit test class, we delete that directory and contents, so you do not have to take care of it.
    * @return The created directory.
    */
   protected static File getSandboxDir() {
      if (sandboxDir != null) {
         deleteDirectoryAndContents(sandboxDir);
      } else {
         String callerClass = Thread.currentThread().getStackTrace()[2].getClassName();
         callerClass = callerClass.substring(callerClass.lastIndexOf('.') + 1);

         try {
            sandboxDir = new File(new File("."), callerClass).getCanonicalFile();
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
      }

      sandboxDir.mkdirs();

      return sandboxDir;
   }

   /**
    * Completely deletes a directory and its sub-directories.
    * @param file The root directory to remove
    * @return true if the root directory was deleted
    */
   private static void deleteDirectoryAndContents(File file) {
      if (file.isDirectory()) {
         File[] files = file.listFiles();
         if (files != null) {
            for (int i = 0; i < files.length; i++) {
               deleteDirectoryAndContents(files[i]);
            }
         }
      }

      try {
         Files.deleteIfExists(file.toPath());
      } catch (IOException e) {
         LoggerFactory.getLogger(LogType.root).error("Error has occurred while trying to delete the file - " + file.getPath(), e);
      }
   }

   @Override
   public void log(Level level, Supplier<CharSequence> messageSupplier) {
      logger.log(level, messageSupplier);
   }

   @Override
   public void log(Level level, Supplier<CharSequence> messageSupplier, Throwable thrown) {
      logger.log(level, messageSupplier, thrown);
   }

   @Override
   public void log(Level level, CharSequence message) {
      logger.log(level, message);
   }

   @Override
   public void log(Level level, CharSequence message, Throwable thrown) {
      logger.log(level, message, thrown);
   }

   /**
    * Assert that two primitive booleans are equal
    * @param message The message to fail with in case they differ
    * @param expected Expected value
    * @param actual Actual value
    */
   protected static void assertEquals(String message, boolean expected, boolean actual) {
      Assert.assertEquals(message, Boolean.valueOf(expected), Boolean.valueOf(actual));
   }

   /**
    * Assert that two times (represented by millis since epoch) are equal
    * @param message The message to fail with in case they differ
    * @param expected Expected value
    * @param actual Actual value
    */
   protected static void assertTimeEquals(String message, long expected, long actual) {
      Assert.assertEquals(message + " : expected " + dateTimeFormatter.format(expected) + " got " + dateTimeFormatter.format(actual), expected, actual);

   }

   /**
    * Assert that two primitive bytes are equal
    * @param message The message to fail with in case they differ
    * @param expected Expected value
    * @param actual Actual value
    */
   protected static void assertEquals(String message, byte expected, byte actual) {
      Assert.assertEquals(message, Byte.valueOf(expected), Byte.valueOf(actual));
   }

   /**
    * Assert that two primitive integers are equal
    * @param message The message to fail with in case they differ
    * @param expected Expected value
    * @param actual Actual value
    */
   protected static void assertEquals(String message, int expected, int actual) {
      Assert.assertEquals(message, Integer.valueOf(expected), Integer.valueOf(actual));
   }

   /**
    * Assert that two primitive long integers are equal
    * @param message The message to fail with in case they differ
    * @param expected Expected value
    * @param actual Actual value
    */
   protected static void assertEquals(String message, long expected, long actual) {
      Assert.assertEquals(message, Long.valueOf(expected), Long.valueOf(actual));
   }

   /**
    * Assert that two primitive doubles are equal, within a positive delta
    * @param message The message to fail with in case they differ
    * @param expected Expected value
    * @param actual Actual value
    * @param delta the maximum delta between <code>expected</code> and <code>actual</code> for which both numbers are still considered equal
    */
   protected static void assertEquals(String message, double expected, double actual, double delta) {
      Assert.assertEquals(message, expected, actual, delta);
   }

   /**
    * Assert that two objects are equal. This uses Object.equals, and not check that the references are the same.
    * See {@link #assertSame(String, Object, Object)}
    * @param message The message to fail with in case they differ
    * @param expected Expected value
    * @param actual Actual value
    */
   protected static void assertEquals(String message, Object expected, Object actual) {
      Assert.assertEquals(message, expected, actual);
   }

   /**
    * Asserts that a condition is true
    * @param message The message to fail with if it is not true
    * @param condition The condition to check
    */
   protected static void assertTrue(String message, boolean condition) {
      Assert.assertTrue(message, condition);
   }

   /**
    * Asserts that a condition is false
    * @param message The message to fail with if it is not false
    * @param condition The condition to check
    */
   protected static void assertFalse(String message, boolean condition) {
      Assert.assertFalse(message, condition);
   }

   /**
    * Asserts that an object refers to null
    * @param message The message to fail with if object differs from null
    * @param object The object to test
    */
   protected static void assertNull(String message, Object object) {
      Assert.assertNull(message, object);
   }

   /**
    * Asserts that an object does not refer to null
    * @param message The message to fail with if object refers to null
    * @param object The object to test
    */
   protected static void assertNotNull(String message, Object object) {
      Assert.assertNotNull(message, object);
   }

   /**
    * Asserts that two objects refer to the same object (==)
    * @param message The message to fail with if not
    * @param expected The expected object
    * @param actual The object to compare to the expected one
    */
   protected static void assertSame(String message, Object expected, Object actual) {
      Assert.assertSame(message, expected, actual);
   }

   /**
    * Asserts that two objects do no refer to the same object (!=)
    * @param message The message to fail with if they do refer to the same object
    * @param expected The object you do not expect
    * @param actual The object to compare to the expected one
    */
   protected static void assertNotSame(String message, Object unexpected, Object actual) {
      Assert.assertNotSame(message, unexpected, actual);
   }

   /**
    * Asserts that a collection is empty
    * @param message The message to fail with in case it is not empty
    * @param collection The collection to check
    */
   protected static void assertEmpty(String message, Collection<?> collection) {
      assertTrue(message, collection.isEmpty());
   }

   /**
    * Asserts that a collection is not empty
    * @param message The message to fail with in case it is empty
    * @param collection The collection to check
    */
   protected static void assertNotEmpty(String message, Collection<?> collection) {
      assertFalse(message, collection.isEmpty());
   }
}
