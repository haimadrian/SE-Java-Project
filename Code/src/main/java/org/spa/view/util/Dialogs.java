package org.spa.view.util;

import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;

import javax.swing.*;
import java.awt.*;

import static org.spa.driver.SPAMain.FRAME_ICON_NAME;
import static org.spa.view.util.Controls.centerDialog;
import static org.spa.view.util.Controls.setFontToComponents;

/**
 * Contain some helper methods to show different dialogs
 *
 * @author Haim Adrian
 * @since 12-May-20
 */
public class Dialogs {
   private static final Logger logger = LoggerFactory.getLogger(Dialogs.class);

   private static JDialog waitingDialog = null;
   private static volatile boolean dialogShown = false;

   public static void hideWaitingDialog() {
      logger.info("Hide waiting dialog");

      if (waitingDialog != null) {
         waitingDialog.setVisible(false);
         waitingDialog.dispose();
         waitingDialog = null;
      }
   }

   /**
    * Show the waiting dialog with a progress bar and custom text.
    *
    * @param owner Owner window
    * @param text Text to display
    * @param listener Get notified when the cancel button is clicked
    * @see CancelListener
    */
   public static void showWaitingDialog(Window owner, String text, CancelListener listener) {
      logger.info("Show waiting dialog");

      if (waitingDialog == null) {
         waitingDialog = new JDialog(owner, "Please Wait", Dialog.ModalityType.APPLICATION_MODAL);
         JProgressBar waitBar = new JProgressBar();
         waitBar.setIndeterminate(true);
         waitingDialog.setUndecorated(true);
         waitingDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
         waitingDialog.setPreferredSize(new Dimension(owner.getPreferredSize().width / 2, 130));
         waitingDialog.getContentPane().setLayout(new BorderLayout());
         waitingDialog.setIconImage(ImagesCache.getInstance().getImage(FRAME_ICON_NAME).getImage());
         JLabel waitingDialogLabel = new JLabel(text);
         waitingDialogLabel.setFont(Fonts.PANEL_HEADING_FONT);
         waitingDialogLabel.setHorizontalAlignment(SwingConstants.CENTER);
         waitingDialogLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

         waitingDialog.getContentPane().add(waitingDialogLabel, BorderLayout.NORTH);

         JButton btnWaitingDialogCancel = new JButton("Cancel");
         btnWaitingDialogCancel.addActionListener(e -> {
            if ((listener == null) || listener.waitingCancelled()) {
               hideWaitingDialog();
            }
         });
         btnWaitingDialogCancel.setMaximumSize(btnWaitingDialogCancel.getPreferredSize());

         waitingDialog.getContentPane().add(waitBar, BorderLayout.CENTER);
         waitingDialog.getContentPane().add(btnWaitingDialogCancel, BorderLayout.SOUTH);
         waitingDialog.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         ((JComponent) waitingDialog.getContentPane()).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(10, 20, 10, 20)));

         waitingDialog.pack();
         waitingDialog.setResizable(false);
         centerDialog(owner, waitingDialog);

         waitingDialog.setVisible(true);
      }
   }

   /**
    * Shows a waiting dialog
    *
    * @param owner the owning window
    * @param message the message to be displayed in the dialog
    */
   public static void showWaitingDialog(Window owner, String message) {
      showWaitingDialog(owner, message, null);
   }

   /**
    * Execute a Runnable, while showing a waiting dialog. The dialog will hide once the job is complete.
    *
    * @param runnable The job to run
    * @param owner The window to show the waiting dialog on
    * @param message to show in the dialog
    */
   public static boolean executeWithWaitingDialog(final Runnable runnable, Window workArea, String message) {
      return executeWithWaitingDialog(runnable, workArea, message, null);
   }

   /**
    * Execute a Runnable, while showing a waiting dialog. The dialog will hide once the job is complete.
    *
    * @param runnable The job to run
    * @param owner The window to show the waiting dialog on
    * @param message to show in the dialog
    * @param listener to notify when everything is done
    */
   public static boolean executeWithWaitingDialog(final Runnable runnable, Window owner, String message, CancelListener listener) {
      if (waitingDialog != null) {
         return false;
      }

      owner.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      new Thread(() -> {
         try {
            // ensure show dialog was executed before hide dialog
            Thread.sleep(500);
            runnable.run();
         } catch (InterruptedException e) {
            logger.error("Sleep was interrupted", e);
         } finally {
            hideWaitingDialog();
         }
      }, message).start();

      showWaitingDialog(owner, message, listener);
      owner.setCursor(Cursor.getDefaultCursor());

      return true;
   }

   /**
    * Shows an error dialog
    *
    * @param parent The parent component
    * @param text The text to show in the dialog
    * @param title The title of the dialog
    */
   public static void showErrorDialog(Component parent, String text, String title) {
      doShowDialog(parent, title, text, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION);
   }

   /**
    * Shows an information dialog
    *
    * @param parent The parent component
    * @param text The text to show in the dialog
    * @param title The title of the dialog
    */
   public static void showInfoDialog(Component parent, String text, String title) {
      doShowDialog(parent, title, text, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION);
   }

   /**
    * Shows a warning dialog
    *
    * @param parent The parent component
    * @param text The text to show in the dialog
    * @param title The title of the dialog
    */
   public static void showWarningDialog(Component parent, String text, String title) {
      doShowDialog(parent, title, text, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION);
   }

   /**
    * Shows a YES/NO question dialog
    *
    * @param parent The parent component
    * @param text The text to show in the dialog
    * @param title The title of the dialog
    * @return true If the user answered YES to the question
    */
   public static boolean showQuestionDialog(Component parent, String text, String title) {
      return doShowDialog(parent, title, text, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, JOptionPane.YES_OPTION);
   }

   /**
    * Shows an OK/CANCEL or YES/NO question dialog
    *
    * @param parent The parent component
    * @param text The text to show in the dialog
    * @param title The title of the dialog
    * @param okCancelOption true if OK/CANCEL, false if YES/NO
    * @return true If the user answered YES/OK to the question
    */
   public static boolean showQuestionDialog(Component parent, String text, String title, boolean okCancelOption) {
      if (!okCancelOption) {
         return showQuestionDialog(parent, text, title);
      }

      return doShowDialog(parent, title, text, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, JOptionPane.OK_OPTION);
   }

   private static boolean doShowDialog(Component parent, String title, String text, int messageType, int optionType, int acceptOption) {
      if (dialogShown) {
         return false;
      }

      dialogShown = true;
      JOptionPane opt = new JOptionPane(text, messageType, optionType) {
         @Override
         public int getMaxCharactersPerLineCount() {
            return 80;
         }
      };
      // Set font to all of the components in the option pane because we calculate font based on
      // screen resolution and we need the option pane to calculate the correct size of text inside it.
      setFontToComponents(opt, Fonts.PLAIN_FONT);
      JDialog dlg = opt.createDialog(parent, title);
      Controls.centerDialog(dlg);
      dlg.setResizable(false);
      dlg.setVisible(true);
      dlg.dispose();
      dialogShown = false;

      if (opt.getValue() == null || !(opt.getValue() instanceof Integer) || ((Integer) opt.getValue()).intValue() != acceptOption) {
         return false;
      }

      return true;
   }

   /**
    * Implement this interface when you want to control the waiting dialog cancel button.<br/>
    * You can ignore user clicks on the cancel button by returning false from this method. This
    * event is raised when the Cancel button of waiting dialog is clicked.
    */
   public interface CancelListener {
      default boolean waitingCancelled() {
         return true;
      }
   }
}
