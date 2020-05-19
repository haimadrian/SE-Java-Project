package org.spa.ui;

import javax.swing.*;

/**
 * An interface that represents a main work area in the application.
 * Use it to work among the different levels of the application, instead of passing a reference to JFrame.
 * @author hadrian
 * @since 13-May-20
 */
public interface SPAExplorerIfc {
   /**
    * Listener which gets called when an editor dialog is closed
    */
   interface ClosureListener {
      /**
       * The editor panel defined by main has been closed
       *
       * @param main The dialog that was just closed
       */
      void dialogClosed(JPanel main);
   }

   /**
    * Panels which implement ClosureVetoable will get queried before being switched away from
    */
   interface ClosureVetoable {
      /**
       * @return Whether the current panel can be closed
       */
      boolean canClose();
   }

   /**
    * Displays an editing dialog in the main work area
    *
    * @param main The component to show in the dialog
    * @param icn An icon to show in dialog corner
    * @param title The dialog's title
    */
   void displayDialog(JPanel main, Icon icn, String title);

   /**
    * Adds a listener to be notified when the current dialog closes
    *
    * @param list The listener to add
    */
   void addClosureListener(ClosureListener list);

   /**
    * Removes a listener to be notified when the current dialog closes
    *
    * @param list The listener to remove
    */
   void removeClosureListener(ClosureListener list);

   /**
    * Sets the title of the work area to a new value
    *
    * @param newTitle The new title value
    */
   void updateTitle(String newTitle);

   /**
    * Closes the current active dialog in the work area
    */
   void closeDialog();

   /**
    * @return A JPanel - the current dialog in the work area
    */
   JPanel getCurrent();

   /**
    * Displays some text in the status bar
    *
    * @param text The text
    */
   void displayStatus(String text);

   /**
    * @return parent for popup dialogs
    */
   JFrame getDialogParent();

   /**
    * Save the visible state of the tree ( ie. expansion/collapse of nodes ) for restoration later
    */
   void saveTreeState();

   /**
    * Restore the visible state of the tree from the last time save was called
    */
   void restoreTreeState();
}
