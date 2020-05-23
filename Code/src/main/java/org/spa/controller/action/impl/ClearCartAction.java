package org.spa.controller.action.impl;

/**
 * @author Haim Adrian
 * @since 23-May-20
 */
public class ClearCartAction extends AbstractAction<Void> {
   @Override
   public Void execute() {
      context.getShoppingCart().clear(true);
      return null;
   }
}
