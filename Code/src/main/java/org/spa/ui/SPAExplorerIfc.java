package org.spa.ui;

import org.spa.controller.selection.SelectionModelManager;

import javax.swing.*;
import java.awt.*;

/**
 * An interface that represents a main work area in the application.
 * Use it to work among the different levels of the application, instead of passing a reference to JFrame.
 * @param <T> The components that this dialog works with. for example {@link org.spa.controller.item.WarehouseItem}
 * @author Haim Adrian
 * @since 13-May-20
 */
public interface SPAExplorerIfc<T> {
   /**
    * Displays this explorer
    */
   void show();

   /**
    * Closes this explorer
    */
   void close();

   /**
    * Sets the title of this explorer
    * @param newTitle The new title value
    */
   void updateTitle(String newTitle);

   /**
    * @return The selection model of this explorer, to get selected item
    */
   SelectionModelManager<T> getSelectionModel();

   /**
    * Displays some text in the status bar
    * @param text The text
    */
   void updateStatus(String text);

   /**
    * In order to allow explorer managers to customize their stuff, we expose an optional component that an explorer
    * can return when it wants that component to be used for navigating to it. (to the explorer)<br/>
    * For example, the shopping cart can expose a customized button that by clicking it, it replaces the view to
    * the shopping cart view, and the button can be customized by the shopping cart explorer. For example, displaying how
    * many items in cart.
    * @return An optional navigating component that a container can set under it in order to navigate to this explorer.
    */
   JComponent getNavigatingComponent();

   /**
    * Returns the main container component of this explorer<br/>
    * This method is used so we can get an explorer work area and present it under main explorer
    * @return Main work area of an explorer. Usually a panel
    */
   Container getMainContainer();

   /**
    * Retrieve the window containing this explorer, in order to be able to customize it
    * @return The dialog containing this explorer
    */
   Window getParentDialog();
}
