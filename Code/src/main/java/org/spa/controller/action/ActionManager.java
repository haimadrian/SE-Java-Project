package org.spa.controller.action;

import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;

import java.util.Map;

/**
 * Use this class to execute actions in the application from anywhere you need.
 *
 * @author Haim Adrian
 * @since 23-May-20
 */
public class ActionManager {
   private static final Logger logger = LoggerFactory.getLogger(ActionManager.class);

   /**
    * Execute a new action based on the specified {@link ActionType}
    *
    * @param actionType The action to execute
    * @param <T> Response type of the action. May be Void if the action has no response
    * @return The response of action execution
    * @throws ActionException In case create or execute failed
    */
   public static <T> T executeAction(ActionType actionType) throws ActionException {
      return executeAction(actionType, null);
   }

   /**
    * Execute a new action based on the specified {@link ActionType}
    *
    * @param actionType The action to execute
    * @param params Additional parameters to pass to the command
    * @param <T> Response type of the action. May be Void if the action has no response
    * @return The response of action execution
    * @throws ActionException In case create or execute failed
    */
   public static <T> T executeAction(ActionType actionType, Map<String, Object> params) throws ActionException {
      T response = null;

      try {
         Action<T> action = actionType.newInstance();
         ActionContext context = new ActionContext();
         if (params != null) {
            params.forEach(context::addParameter);
         }
         action.init(context);
         response = action.execute();
      } catch (Exception e) {
         logger.error("Failed to execute a new action. actionType=" + actionType, e);
         throw new ActionException("Could not execute action. Reason: " + e.getMessage(), e);
      }

      return response;
   }
}
