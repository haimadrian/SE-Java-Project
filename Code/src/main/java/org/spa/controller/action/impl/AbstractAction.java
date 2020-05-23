package org.spa.controller.action.impl;

import org.spa.controller.action.Action;
import org.spa.controller.action.ActionContext;

/**
 * @author Haim Adrian
 * @since 23-May-20
 */
public abstract class AbstractAction<T> implements Action<T> {
   protected ActionContext context;

   @Override
   public void init(ActionContext context) {
      this.context = context;
   }
}
