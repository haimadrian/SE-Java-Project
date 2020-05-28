package org.spa.common;

import org.spa.controller.order.OrderSystem;
import org.spa.model.dal.UserRepository;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.alert.AlertSystem;
import org.spa.controller.cart.ShoppingCart;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.ui.SPAExplorerIfc;
import org.spa.controller.UserManagementService;

/**
 * A singleton class that keeps a unique reference to all of the application's controllers.<br/>
 * Everybody can access this class in order to get to the different services in the project. The
 * services themselves use this class in order to communicate between themselves.
 * @author Haim Adrian
 * @since 16-May-20
 */
public class SPAApplication {
   private static final Logger logger = LoggerFactory.getLogger(SPAApplication.class);
   private final static SPAApplication instance = new SPAApplication();

   private final ItemsWarehouse itemsWarehouse;
   private final AlertSystem alertSystem;
   private final ShoppingCart shoppingCart;
   private final UserManagementService userManagementService;
   private final SelectionModelManager<SPAExplorerIfc<?>> selectionModel;
   private final OrderSystem orderSystem;

   //private final Repository<User> userRepository;
   // Disallow creation of this class from outside
   private SPAApplication() {
      itemsWarehouse = new ItemsWarehouse();
      alertSystem = new AlertSystem();
      shoppingCart = new ShoppingCart();
      userManagementService = new UserManagementService();
      selectionModel = new SelectionModelManager<>();
      orderSystem = new OrderSystem();
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
      userManagementService.start();
      orderSystem.start();
   }

   /**
    * Stop all services in the application
    */
   public void stop() {
      logger.info("Stopping services");
      alertSystem.stop();

      // Clear shopping cart before stopping warehouse, because it might update counts in warehouse.
      shoppingCart.clear(true);

      itemsWarehouse.stop();
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
    * @return A reference to {@link AlertSystem}
    */
   public AlertSystem getAlertSystem() {
      return alertSystem;
   }

   /**
    * @return A reference to {@link ShoppingCart}
    */
   public ShoppingCart getShoppingCart() {
      return shoppingCart;
   }

   public UserManagementService getUserManagementService() {
      return userManagementService;
   }

//   public Repository<User> getUserRepository() {
//      return userRepository;
//   }

   /**
    * @return A reference to the global {@link SelectionModelManager}
    */
   public SelectionModelManager<SPAExplorerIfc<?>> getSelectionModel() {
      return selectionModel;
   }}
