package org.spa.controller.selection;

import java.util.HashSet;
import java.util.Set;

/**
 * This class created in order to keep the focused element in the application, so we can share it among different
 * systems in the application. For example, assume the user presses an item in the shopping cart, then the selection model will hold the
 * focused item, so the actions in the application (e.g. remove from cart) can get the focused item and act accordingly.
 * <p>
 * It is possible to listen to selection model events and respond to them. Let's take for example a status bar that tells
 * what component is in focus, so the status bar expects to get updates when a selected component is changed to another component,
 * for this we create the Observer mechanism such that the status bar can register itself to observe selection changes.
 * </p>
 *
 * @author Haim Adrian
 * @since 22-May-20
 */
public class SelectionModelManager<T> {
   private final Set<SelectionModelObserver<T>> observers;
   private T currentSelection;
   private T previousSelection;

   /**
    * Constructs a new {@link SelectionModelManager}
    */
   public SelectionModelManager() {
      observers = new HashSet<>();
   }

   /**
    * @return The current selected component in this selection model manager. May refer to <code>null</code> when there is nothing in focus
    */
   public T getSelection() {
      return currentSelection;
   }

   /**
    * Set the active (selected) component as the current selection of this selection model listener
    *
    * @param selection The selection to set
    */
   public void setSelection(T selection) {
      previousSelection = currentSelection;
      currentSelection = selection;
      notifyObservers();
   }

   /**
    * @return The previous selected component in this selection model manager. May refer to <code>null</code> when there was nothing in focus
    */
   public T getPreviousSelection() {
      return previousSelection;
   }

   /**
    * Register yourself as an observer of this selection model manager, to get notified when there is a selection change
    *
    * @param observer An observer to be notified upon selection changes
    * @see SelectionModelObserver
    */
   public void registerObserver(SelectionModelObserver<T> observer) {
      observers.add(observer);
   }

   public void unregisterObserver(SelectionModelObserver<T> observer) {
      observers.remove(observer);
   }

   private void notifyObservers() {
      for (SelectionModelObserver<T> observer : observers) {
         observer.onSelectionChanged(previousSelection, currentSelection);
      }
   }
}
