package org.spa.controller.action;

/**
 * All actions in the application implement this interface to support a generic invocation through {@link ActionManager}.
 *
 * @param <T> The result type of an implementing action. Can be Void if there is no result.
 * @author Haim Adrian
 * @since 23-May-20
 */
public interface Action<T> {
   void init(ActionContext context);

   T execute();
}
