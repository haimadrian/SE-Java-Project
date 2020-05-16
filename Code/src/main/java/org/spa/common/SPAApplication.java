package org.spa.common;

import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.alert.AlertSystem;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.model.dal.ItemRepository;

/**
 * A singleton class that keeps a unique reference to all of the application's controllers.<br/>
 * Everybody can access this class in order to get to the different services in the project. The
 * services themselves use this class in order to communicate between themselves.
 * @author hadrian
 * @since 16-May-20
 */
public class SPAApplication {
   private static final Logger logger = LoggerFactory.getLogger(SPAApplication.class);
   private final static SPAApplication instance = new SPAApplication();

   private final ItemsWarehouse itemsWarehouse;
   private final ItemRepository itemRepository;
   private final AlertSystem alertSystem;

   // Disallow creation of this class from outside
   private SPAApplication() {
      itemsWarehouse = new ItemsWarehouse();
      itemRepository = new ItemRepository();
      alertSystem = new AlertSystem();
   }

   /**
    * @return The single instance of this class
    */
   public static SPAApplication getInstance() {
      return instance;
   }

   /**
    * Start all services in the application
    */
   public void start() {
      logger.info("Starting services");
      itemsWarehouse.start();
      alertSystem.start();
   }

   /**
    * Stop all services in the application
    */
   public void stop() {
      logger.info("Stopping services");
      alertSystem.stop();
   }

   /**
    * @return Working directory used for storing and loading files from disk
    */
   public String getWorkingDirectory() {
      return "C:\\temp\\SPAApp";
   }

   /**
    * @return A reference to {@link ItemsWarehouse}
    */
   public ItemsWarehouse getItemsWarehouse() {
      return itemsWarehouse;
   }

   /**
    * @return A reference to {@link ItemRepository}
    */
   public ItemRepository getItemRepository() {
      return itemRepository;
   }

   /**
    * @return A reference to {@link AlertSystem}
    */
   public AlertSystem getAlertSystem() {
      return alertSystem;
   }
}
