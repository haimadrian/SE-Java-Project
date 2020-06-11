package org.spa.ui.table;

import org.spa.ui.util.Controls;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * A class used to manage popup triggering over a component.<br/>
 * This adapter listens for a specified component mouse actions to detect when there is a popup triggering.
 *
 * @author Haim Adrian
 * @since 12-May-20
 */
public abstract class PopupAdapter extends MouseAdapter {
   private JComponent component;

   /**
    * Constructs a new {@link PopupAdapter}
    */
   public PopupAdapter() {
   }

   /**
    * Constructs a new {@link PopupAdapter}
    *
    * @param component A component to show a popup for. Can be <code>null</code> when passed to a manager.
    */
   public PopupAdapter(JComponent component) {
      this.component = component;
   }

   /**
    * @return The component that this popup opened for
    */
   public JComponent getComponent() {
      return component;
   }

   /**
    * A method to pass for managers so they can set a reference to the component they manage, and get this adapter from outside
    *
    * @param component A component to show a popup for
    */
   public void setComponent(JComponent component) {
      this.component = component;
   }

   @Override
   public void mouseReleased(MouseEvent e) {
      possiblyShowPopupMenu(e);
   }

   @Override
   public void mousePressed(MouseEvent e) {
      possiblyShowPopupMenu(e);
   }

   @Override
   public void mouseClicked(MouseEvent e) {
      possiblyShowPopupMenu(e);
   }

   private void possiblyShowPopupMenu(MouseEvent mouseEvent) {
      if (!mouseEvent.isConsumed() && mouseEvent.isPopupTrigger()) {
         List<JMenuItem> menuItemsForPopup = null;
         if (mouseEvent.getComponent().equals(component)) {
            menuItemsForPopup = getMenuItemsForPopup();
         } else {
            menuItemsForPopup = getBlankAreaMenuItemsForPopup();
         }

         if ((menuItemsForPopup != null) && (!menuItemsForPopup.isEmpty())) {
            JPopupMenu popup = new JPopupMenu();
            popup.setBackground(Controls.background);
            int displayIndex = 0;

            for (JMenuItem menuItem : menuItemsForPopup) {
               menuItem.setBackground(Controls.background);
               menuItem.setDisplayedMnemonicIndex(displayIndex++);
               popup.add(menuItem);
            }

            popup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
            mouseEvent.consume();
         }
      }
   }

   /**
    * Override this method to return items to display when right clicking a blank area
    * over the table
    *
    * @return Items for blank area. e.g. Create
    */
   protected List<JMenuItem> getBlankAreaMenuItemsForPopup() {
      return null;
   }

   /**
    * Implement this method to supply all menu items of a popup.
    *
    * @return The menu items to display in the popup when it is triggered
    */
   protected abstract java.util.List<JMenuItem> getMenuItemsForPopup();
}
