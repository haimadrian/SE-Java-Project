package org.spa.controller.action;

/**
 * @author Haim Adrian
 * @since 23-May-20
 */
public class ActionException extends Exception {
   public ActionException() {
   }

   public ActionException(String message) {
      super(message);
   }

   public ActionException(String message, Throwable cause) {
      super(message, cause);
   }

   public ActionException(Throwable cause) {
      super(cause);
   }

   public ActionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
      super(message, cause, enableSuppression, writableStackTrace);
   }
}
