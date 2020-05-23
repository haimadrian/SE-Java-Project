package org.spa.controller.selection;

/**
 * Whoever may need to respond to selection changes in the application that correspond to the explorer that holds a
 * {@link SelectionModelManager}, can implement this interface and register itself as an observer of the selection model manager.
 * @author Haim Adrian
 * @since 22-May-20
 */
public interface SelectionModelObserver<T> {
   /**
    * When listening to the {@link SelectionModelManager}, this event is raised when there is a selection change
    * @param previousSelection Previous selected component, may refer to <code>null</code> in case there was nothing focused before
    * @param currentSelection Current selected component, may refer to <code>null</code> when resetting focus
    */
   void onSelectionChanged(T previousSelection, T currentSelection);
}
