package org.spa.controller.action.impl;

import java.util.concurrent.TimeUnit;

/**
 * @author Haim Adrian
 * @since 23-May-20
 */
public class PurchaseAction extends AbstractAction<Void> {
   @Override
   public Void execute() {
      String userId = context.getUserManagement().getLoggedInUser().getUserId();
      context.getOrderSystem().createOrder(userId, context.getShoppingCart().getItems());
      try {
         Thread.sleep(TimeUnit.SECONDS.toMillis(3));
      } catch (InterruptedException ignore) {
      }
      context.getShoppingCart().clear(false);

      return null;
   }
}
