package org.spa.ui.table;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A class used to manage popup triggering over a component.<br/>
 * This adapter listens for a specified component mouse actions to detect when there is a popup triggering.
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
    * @param component A component to show a popup for. Can be <code>null</code> when passed to a manager.
    */
   public PopupAdapter(JComponent component) {
      this.component = component;
   }

   /**
    * A method to pass for managers so they can set a reference to the component they manage, and get this adapter from outside
    * @param component A component to show a popup for
    */
   public void setComponent(JComponent component) {
      this.component = component;
   }

   /**
    * @return The component that this popup opened for
    */
   public JComponent getComponent() {
      return component;
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
         JPopupMenu popup = new JPopupMenu();
         int displayIndex = 0;
         for (JMenuItem menuItem : getMenuItemsForPopup()) {
            menuItem.setDisplayedMnemonicIndex(displayIndex++);
            popup.add(menuItem);
         }
         popup.show(component, mouseEvent.getX(), mouseEvent.getY());
         mouseEvent.consume();
      }
   }

   /**
    * Implement this method to supply all menu items of a popup.
    * @return The menu items to display in the popup when it is triggered
    */
   protected abstract java.util.List<JMenuItem> getMenuItemsForPopup();
}
