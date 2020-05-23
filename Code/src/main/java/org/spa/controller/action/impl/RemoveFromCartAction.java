package org.spa.controller.action.impl;

/**
 * @author Haim Adrian
 * @since 23-May-20
 */
public class RemoveFromCartAction extends AbstractAction<Void> {
   @Override
   public Void execute() {
      context.getShoppingCart().remove(context.getValue("itemId"));
      return null;
   }
}
