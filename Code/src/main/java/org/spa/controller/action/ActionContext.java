package org.spa.controller.action;

import org.spa.controller.UserManagementService;
import org.spa.common.SPAApplication;
import org.spa.controller.alert.AlertSystem;
import org.spa.controller.cart.ShoppingCart;
import org.spa.controller.item.ItemsWarehouse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Haim Adrian
 * @since 23-May-20
 */
public class ActionContext {
   // Keep references to the services here to ease the access and skip the "SPAApplication.getInstance()" prefix.
   private UserManagementService userManagement;
   private ItemsWarehouse itemsWarehouse;
   private ShoppingCart shoppingCart;
   private AlertSystem alertSystem;

   /**
    * A way to let users of this class to append any value that they need to pass from action invoker to the action they implement
    */
   private final Map<String, Object> additionalFields;

   /**
    * Constructs a new {@link ActionContext}
    */
   public ActionContext() {
      additionalFields = new HashMap<>();
      userManagement = SPAApplication.getInstance().getUserManagementService();
      itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
      shoppingCart = SPAApplication.getInstance().getShoppingCart();
      alertSystem = SPAApplication.getInstance().getAlertSystem();
   }

   /**
    * Add a custom parameter to pass to an action
    * @param key The key so the action can retrieve this parameter
    * @param value The value to map to the specified key
    * @return A reference to this
    */
   public ActionContext addParameter(String key, Object value) {
      additionalFields.put(key.toLowerCase(), value);
      return this;
   }

   /**
    * Gets a value that was previously set as an additional parameter for function.<br/>
    * Make sure you use the correct type, as this is a generic method.
    * @param key The key to get value for
    * @param <T> The type of the value
    * @return The value or <code>null</code> in case there is no value mapped to the specified key
    */
   public <T> T getValue(String key) {
      return (T)additionalFields.get(key.toLowerCase());
   }

   public UserManagementService getUserManagement() {
      return userManagement;
   }

   public ItemsWarehouse getItemsWarehouse() {
      return itemsWarehouse;
   }

   public ShoppingCart getShoppingCart() {
      return shoppingCart;
   }

   public AlertSystem getAlertSystem() {
      return alertSystem;
   }
}
