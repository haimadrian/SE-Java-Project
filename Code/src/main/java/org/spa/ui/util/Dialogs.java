package org.spa.ui.util;

import org.spa.ui.CustomGridBagConstraints;
import org.spa.ui.SPAExplorerIfc;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;

/**
 * Contain some helper methods to show different time of message dialogs.
 *
 * @author Haim Adrian
 * @since 12-May-20
 */
public class Dialogs {
   /**
    * File filter for directories
    */
   public static final FileFilter DIR_FILE_FILTER = new FileFilter() {
      /**
       * Whether the given file is accepted by this filter.
       *
       * @param f The file
       * @return Accepted?
       */
      @Override
      public boolean accept(File f) {
         return f.isDirectory();
      }

      /**
       * The description of this filter. For example: "JPG and GIF Images"
       *
       * @see FileView#getName
       * @return String
       */
      @Override
      public String getDescription() {
         return "Directory";
      }
   };
   /**
    * File filter all files
    */
   public static final FileFilter ALL_FILE_FILTER = new FileFilter() {
      /**
       * Whether the given file is accepted by this filter.
       *
       * @param f The file
       * @return Accepted?
       */
      @Override
      public boolean accept(File f) {
         return true;
      }

      /**
       * The description of this filter. For example: "JPG and GIF Images"
       *
       * @see FileView#getName
       * @return String
       */
      @Override
      public String getDescription() {
         return "All Files (*.*)";
      }
   };

   // UI for the please wait... dialog
   /**
    * File filter for ZIP files
    */
   public static final FileFilter ZIP_FILE_FILTER = new FileFilter() {
      @Override
      public boolean accept(File f) {
         return f.getName().toLowerCase().endsWith(".zip") || f.isDirectory();
      }

      @Override
      public String getDescription() {
         return "Zip Files (*.zip)";
      }
   };
   /**
    * File filter for any XML file
    */
   public static final FileFilter XML_FILE_FILTER = new FileFilter() {
      @Override
      public boolean accept(File f) {
         return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
      }

      @Override
      public String getDescription() {
         return "XML Files (*.xml)";
      }
   };
   /**
    * File filter for any text file
    */
   public static final FileFilter TEXT_FILE_FILTER = new FileFilter() {
      @Override
      public boolean accept(File f) {
         return f.getName().toLowerCase().endsWith(".txt") || f.isDirectory();
      }

      @Override
      public String getDescription() {
         return "Text Files (*.txt)";
      }
   };
   /**
    * Used to notify any threads that have called hideWaitingDialog() that they now may proceed.
    */
   protected static final Object waitingDialogLock = new Object();
   private static final Logger logger = LoggerFactory.getLogger(Dialogs.class);
   private static final String DEFAULT_WAITINGDIALOG_MESSAGE = "Thinking...";
   protected static JDialog waitingDialog = null;
   protected static JLabel waitingDialogLabel = null;
   /**
    * Indicates that a call has been made to showWaitingDialog() and that it's pending display
    */
   protected static volatile boolean waitingDialogPending = false;
   /**
    * Indicates waiting dialog has been completely shown - can't use isVisible()/isShowning() here.
    */
   protected static volatile boolean waitingDialogShown = false;
   protected static JPanel waitingCancel;
   protected static CancelListener cancelNotifier = null;
   private static volatile boolean errorDialogShown = false;
   private static Component mainComponent;

   /**
    * Initialises the waiting dialog
    *
    * @param workArea The SPAExplorerIfc
    */
   protected static void initWaitingDialog(final SPAExplorerIfc<?> workArea) {
      waitingDialog = new JDialog((JFrame) workArea, "Please Wait", true);
      JProgressBar waitBar = new JProgressBar();
      waitBar.setIndeterminate(true);

      waitingDialog.setUndecorated(true);
      waitingDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      waitingDialog.getContentPane().setLayout(new BorderLayout());

      waitingDialogLabel = new JLabel(DEFAULT_WAITINGDIALOG_MESSAGE);
      waitingDialogLabel.setHorizontalAlignment(SwingConstants.CENTER);
      waitingDialogLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

      waitingDialog.getContentPane().add(waitingDialogLabel, BorderLayout.NORTH);

      JButton btnWaitingDialogCancel = new JButton("Cancel");
      btnWaitingDialogCancel.addActionListener(e -> {
         if (cancelNotifier != null) {
            cancelNotifier.waitingCancelled();
         }
      });

      btnWaitingDialogCancel.setMaximumSize(btnWaitingDialogCancel.getPreferredSize());

      waitingCancel = Controls.createPanel(BoxLayout.Y_AXIS, new Component[] { Box.createVerticalStrut(8), Controls.createPanel(BoxLayout.X_AXIS, new Component[] { Box.createHorizontalGlue(), btnWaitingDialogCancel,
            Box.createHorizontalGlue() }), Box.createVerticalStrut(5) });
      waitingDialog.getContentPane().add(waitBar, BorderLayout.CENTER);
      waitingDialog.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      ((JComponent) waitingDialog.getContentPane()).setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(10, 20, 10, 20)));

      waitingDialog.pack();
      waitingDialog.setResizable(false);

      // Centre the dialog when it's shown and wake up any threads blocked in hideWaitingDialog()
      waitingDialog.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentShown(ComponentEvent e) {
            waitingDialog.setLocation(((JFrame) workArea).getLocation().x + (((JFrame) workArea).getSize().width - waitingDialog.getSize().width) / 2,
                  ((JFrame) workArea).getLocation().y + (((JFrame) workArea).getSize().height - waitingDialog.getSize().height) / 2);
            synchronized (waitingDialogLock) {
               logger.info("componentShown(): waitingDialogShown = true");
               waitingDialogShown = true; // the waiting dialog is completely shown
               waitingDialogLock.notifyAll();
            }
         }

         @Override
         public void componentHidden(ComponentEvent e) {
            synchronized (waitingDialogLock) {
               logger.info("componentHidden(): waitingDialogShown = false");
               waitingDialogShown = false; // reset for next time
            }
         }
      });
   }

   /**
    * @param workArea the SPAExplorerIfc
    * @param pMessage the message to be displayed in the dialog
    * @param listener Will show a "Cancel" button on the dialog, upon which press the listener will be
    * notified.
    * @return A please wait... modal dialog
    */
   protected static Dialog getWaitingDialog(final SPAExplorerIfc<?> workArea, final String pMessage, final CancelListener listener) {
      String message = pMessage;
      if (waitingDialog == null) {
         initWaitingDialog(workArea);
      }

      if (waitingDialogLabel != null) {
         if (!message.endsWith(".")) {
            message = message + "...";
         }
         waitingDialogLabel.setText(message);
         waitingDialogLabel.invalidate();
         waitingDialog.pack();
      }

      cancelNotifier = listener;

      if (cancelNotifier != null) {
         waitingDialog.getContentPane().add(waitingCancel, BorderLayout.SOUTH);
      } else {
         waitingDialog.getContentPane().remove(waitingCancel);
      }

      waitingDialog.pack();
      return waitingDialog;
   }

   /**
    * Shows a waiting dialog
    *
    * @param workArea the SPAExplorerIfc
    */
   public static void showWaitingDialog(SPAExplorerIfc<?> workArea) {
      showWaitingDialog(workArea, DEFAULT_WAITINGDIALOG_MESSAGE);
   }

   /**
    * Shows a waiting dialog
    *
    * @param workArea the SPAExplorerIfc
    * @param message the message to be displayed in the dialog
    */
   public static void showWaitingDialog(SPAExplorerIfc<?> workArea, String message) {
      showWaitingDialog(workArea, message, null);
   }

   /**
    * Shows a waiting dialog, with a cancel button
    *
    * @param workArea the SPAExplorerIfc
    * @param message the message to be displayed in the dialog
    * @param listener CancelListener to get notified
    */
   public static void showWaitingDialog(SPAExplorerIfc<?> workArea, String message, CancelListener listener) {
      logger.info("showWaitingDialog() - start");

      synchronized (waitingDialogLock) {
         logger.info("showWaitingDialog(): waitingDialogPending = true");
         waitingDialogPending = true;
      }

      getWaitingDialog(workArea, message, listener).setVisible(true);
      logger.info("showWaitingDialog() - finish");
   }

   /**
    * @param workArea The SPAExplorerIfc
    * @return The main dialog for creating error frames on top of, this will either be the waiting dialog, of
    * the main frame, or potentially another modal form.
    */
   public static Window getMainDialog(SPAExplorerIfc<?> workArea) {
      if (waitingDialog != null && waitingDialog.isVisible()) {
         return waitingDialog;
      }

      return workArea.getParentDialog();
   }

   /**
    * Hides the waiting dialog but first waits for it to be completely shown if not already so
    */
   public static void hideWaitingDialog() {
      logger.info("hideWaitingDialog() - start");

      synchronized (waitingDialogLock) {
         if (waitingDialogPending) {
            if (!waitingDialogShown) {
               try {
                  logger.info("hideWaitingDialog(): before wait");
                  waitingDialogLock.wait();
                  logger.info("hideWaitingDialog(): after wait");
               } catch (InterruptedException ex) {
                  logger.debug(() -> "hideWaitingDialog(): interrupted");
               }
            }

            waitingDialog.setVisible(false);
            waitingDialogPending = false;
            logger.info("hideWaitingDialog(): waitingDialogPending = false");
         }
      }

      logger.info("hideWaitingDialog() - finish");
   }

   /**
    * Execute a Runnable, while showing a waiting dialog. The dialog will hide once the job is complete.
    *
    * @param runnable The job to run
    * @param workArea The SPAExplorerIfc to show the waiting dialog on
    * @param message to show in the dialog
    */
   public static void executeWithWaitingDialog(final Runnable runnable, SPAExplorerIfc<?> workArea, String message) {
      executeWithWaitingDialog(runnable, workArea, message, null);
   }

   /**
    * Execute a Runnable, while showing a waiting dialog. The dialog will hide once the job is complete.
    *
    * @param runnable The job to run
    * @param workArea The SPAExplorerIfc to show the waiting dialog on
    * @param message to show in the dialog
    * @param listener to notify when everything is done
    */
   public static void executeWithWaitingDialog(final Runnable runnable, SPAExplorerIfc<?> workArea, String message, CancelListener listener) {
      synchronized (waitingDialogLock) {
         logger.info("executeWithWaitingDialog(): waitingDialogPending = true");
         waitingDialogPending = true;
      }

      workArea.getParentDialog().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
      new Thread(() -> {
         try {
            // ensure show dialog was executed before hide dialog
            Thread.sleep(1000);
            runnable.run();
         } catch (InterruptedException e) {
            logger.error("Sleep was interrupted", e);
         } finally {
            hideWaitingDialog();
         }
      }, message).start();

      showWaitingDialog(workArea, message, listener);
      workArea.getParentDialog().setCursor(Cursor.getDefaultCursor());
   }

   /**
    * Shows an error dialog for an error caused by an exception
    *
    * @param parent The parent component
    * @param text The text to show in the dialog
    * @param detailedErrorText The detailed error text associated with the error
    * @param title The title of the dialog
    */
   public static void showErrorDialog(Component parent, String text, String detailedErrorText, String title) {
      if (errorDialogShown) {
         return;
      }

      hideWaitingDialog();

      JTextArea detailedErrorJTextArea = null;
      JScrollPane detailedErrorJScrollPane = null;


      // get the proper parent
      final JDialog dialog = getDialog(parent, title, true);
      JPanel contentPane = (JPanel) dialog.getContentPane();
      contentPane.setLayout(new GridBagLayout());
      CustomGridBagConstraints constraints = new CustomGridBagConstraints();

      if ((detailedErrorText != null) && (detailedErrorText.length() > 0)) {
         detailedErrorJTextArea = new JTextArea(detailedErrorText);
         detailedErrorJTextArea.setLineWrap(true);
         detailedErrorJTextArea.setWrapStyleWord(true);
         detailedErrorJTextArea.setEditable(false);
         detailedErrorJTextArea.setToolTipText(detailedErrorJTextArea.getText());
         detailedErrorJTextArea.setFont(Fonts.PLAIN_FONT);
         detailedErrorJTextArea.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
         detailedErrorJTextArea.setMinimumSize(new java.awt.Dimension(450, 44));
         detailedErrorJTextArea.setPreferredSize(new java.awt.Dimension(450, 44));
         detailedErrorJScrollPane = new JScrollPane(detailedErrorJTextArea);
         detailedErrorJScrollPane.setMinimumSize(new java.awt.Dimension(450, 30));
         detailedErrorJScrollPane.setPreferredSize(new java.awt.Dimension(450, Integer.MAX_VALUE));
      }

      JTextArea errorMsg = new JTextArea();
      errorMsg.setFont(Fonts.BOLD_FONT);
      errorMsg.setText(text);
      errorMsg.setBorder(new EmptyBorder(3, 3, 3, 3));
      errorMsg.setEditable(false);
      errorMsg.setToolTipText(errorMsg.getText());
      errorMsg.setFont(Fonts.BOLD_FONT);
      //errorMsg.setBackground(SystemColor.control);
      errorMsg.setMinimumSize(new java.awt.Dimension(450, 30));
      errorMsg.setPreferredSize(new java.awt.Dimension(450, 30));
      errorMsg.setLineWrap(true);
      errorMsg.setWrapStyleWord(true);
      errorMsg.setFocusable(false);
      errorMsg.setRequestFocusEnabled(false);

      JPanel buttonPanel = new JPanel();
      JButton okButton = new JButton(" OK ");
      okButton.addActionListener(e -> {
         dialog.setVisible(false);
         dialog.dispose();
      });
      okButton.setDefaultCapable(true);
      dialog.getRootPane().setDefaultButton(okButton);
      buttonPanel.add(okButton);

      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosed(WindowEvent windowEvent) {
            errorDialogShown = false;
         }
      });

      contentPane.add(errorMsg, constraints.constrainFillBoth().setWeight(40));
      if (detailedErrorJScrollPane == null) {
         dialog.setMinimumSize(new Dimension(600, 150));
      } else {
         contentPane.add(detailedErrorJScrollPane, constraints.constrainFillBoth().nextY().setInsets("L=10,R=10"));
         dialog.setMinimumSize(new Dimension(800, 200));
      }
      constraints.addXYFiller(contentPane, 1);
      contentPane.add(buttonPanel, constraints.constrainLabel().nextY());
      dialog.pack();
      dialog.setResizable(true);
      Controls.centerDialog(dialog);
      okButton.requestFocus();
      errorDialogShown = true;
      dialog.setVisible(true);
   }

   /**
    * Shows an error dialog for an error contained within an ADK Message
    *
    * @param parent The parent component
    * @param text The text to show in the dialog
    * @param title The title of the dialog
    */
   public static void showErrorDialog(Component parent, String text, String title) {
      showErrorDialog(parent, text, "", title);
   }

   /**
    * Shows an error dialog with the given text and exception. The dialog is parented by the current main component
    *
    * @param text
    * @param exception
    */
   public static void showErrorDialog(String text, Throwable exception) {
      showErrorDialog(mainComponent, text, exception, "Error");
   }

   /**
    * Shows an error dialog with the given text. The dialog is parented by the current main component
    *
    * @param text
    */
   public static void showErrorDialog(String text) {
      showErrorDialog(mainComponent, text, "Error");
   }

   /**
    * Shows an error dialog for an error caused by an exception
    *
    * @param parent The parent component
    * @param pText The text to show in the dialog
    * @param exception The exception associated with the error or nil if there is no exception
    * @param title The title of the dialog
    */
   public static void showErrorDialog(Component parent, String pText, Throwable exception, String title) {
      String text = pText;
      Throwable ex = exception;
      if (ex == null) {
         showErrorDialog(parent, text, "", title);
         return;
      }

      if (ex instanceof InvocationTargetException) {
         ex = ((InvocationTargetException) ex).getTargetException();
      }

      StringBuilder buf = new StringBuilder();
      if (ex.getCause() == null) {
         StringWriter sw = new StringWriter();
         ex.printStackTrace(new PrintWriter(sw));
         buf.append(sw);
      } else {
         if (ex.getMessage() != null) {
            buf.append(ex.getMessage());
         }
         Throwable tex = ex.getCause();
         while (tex != null) {
            buf.append("\n   caused by\n").append(tex.getMessage());
            tex = tex.getCause();
         }
      }

      showErrorDialog(parent, text, buf.toString(), title);
   }

   /**
    * Shows an information dialog
    *
    * @param parent The parent component
    * @param text The text to show in the dialog
    * @param title The title of the dialog
    */
   public static void showSimpleErrorDialog(Component parent, String text, String title) {
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
    * Shows an information dialog
    *
    * @param parent The parent component
    * @param text The text to show in the dialog
    * @param title The title of the dialog
    */
   public static void showWarningDialog(Component parent, String text, String title) {
      doShowDialog(parent, title, text, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION);
   }

   /**
    * Shows a YES/NO question dialog with warning icon.
    *
    * @param parent The parent component
    * @param text The text to show in the dialog
    * @param title The title of the dialog
    * @return true If the user answered YES to the question
    */
   public static boolean showWarningQuestionDialog(Component parent, String text, String title) {
      return doShowDialog(parent, title, text, JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION, JOptionPane.YES_OPTION);
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

   /**
    * Shows a simple model dialog with the contents of the panel, and a Close button
    *
    * @param parent The parent window
    * @param title The new dialog title
    * @param comp The contents of the pop-up dialog
    */
   public static void showPanelDialog(JFrame parent, String title, Component comp) {
      JDialog dlg = new JDialog(parent, title, true);
      doShowPanelDialog(parent, dlg, comp);
   }

   /**
    * Shows a simple model dialog with the contents of the panel, and a Close button
    *
    * @param parent The parent window
    * @param title The new dialog title
    * @param comp The contents of the pop-up dialog
    */
   public static void showPanelDialog(JDialog parent, String title, Component comp) {
      JDialog dlg = new JDialog(parent, title, true);
      doShowPanelDialog(parent, dlg, comp);
   }

   private static void doShowPanelDialog(Component parent, final JDialog dlg, Component comp) {
      dlg.getContentPane().setLayout(new BorderLayout());
      dlg.getContentPane().add(comp);

      JButton btnClose = new JButton("Close");
      btnClose.addActionListener(e -> {
         dlg.setVisible(false);
         dlg.dispose();
      });

      dlg.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

      dlg.getContentPane().add(Controls.createPanel(BoxLayout.X_AXIS, new Component[] { Box.createHorizontalGlue(), btnClose }), BorderLayout.SOUTH);
      dlg.pack();

      if (dlg.getSize().width > parent.getSize().width) {
         dlg.setSize(parent.getSize().width, dlg.getSize().height);
      }
      if (dlg.getSize().height > parent.getSize().height) {
         dlg.setSize(dlg.getSize().width, parent.getSize().height);
      }

      dlg.setLocation(parent.getLocationOnScreen().x + (parent.getSize().width - dlg.getSize().width) / 2, parent.getLocationOnScreen().y + (parent.getSize().height - dlg.getSize().height) / 2);
      dlg.setVisible(true);
   }

   private static boolean doShowDialog(Component parent, String title, String text, int messageType, int optionType, int acceptOption) {
      JOptionPane opt = new JOptionPane(text, messageType, optionType) {
         @Override
         public int getMaxCharactersPerLineCount() {
            return 80;
         }
      };
      JDialog dlg = opt.createDialog(parent, title);
      Controls.centerDialog(dlg);
      dlg.setResizable(false);
      dlg.setVisible(true);
      dlg.dispose();

      if (opt.getValue() == null || !(opt.getValue() instanceof Integer) || ((Integer)opt.getValue()).intValue() != acceptOption) {
         return false;
      }

      return true;
   }

   /**
    * Displays a message dialog to edit a string
    *
    * @param parent The parent component
    * @param title The title of the dialog
    * @param prompt The prompt displayed next to the edit box
    * @param initial Initial value
    * @return User-defined value
    */
   public static String showEditDialog(Component parent, String title, String prompt, String initial) {
      JOptionPane pane = new JOptionPane(prompt, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      pane.setWantsInput(true);
      pane.setInitialValue(initial);
      pane.setInitialSelectionValue(initial);
      JDialog dlg = pane.createDialog(parent, title);
      dlg.setResizable(false);
      dlg.setVisible(true);
      dlg.dispose();
      if (pane.getInputValue() instanceof String && !pane.getInputValue().equals(JOptionPane.UNINITIALIZED_VALUE)) {
         return (String)pane.getInputValue();
      }

      return null;
   }

   /**
    * Displays a message dialog to edit a string
    *
    * @param parent The parent component
    * @param title The title of the dialog
    * @param prompt The prompt displayed next to the edit box
    * @param initial Initial value
    * @param componentSupplier Used to define special components for each field. Can be <code>null</code> to ignore and use {@link JTextField} only. This bi function
    * will get the index of current prompt field, and a string which is the value at that index in the <code>prompt</code> parameter.
    * @return User-defined value
    */
   public static String[] showEditDialog(Component parent, String title, String[] prompt, String[] initial, BiFunction<Integer, String, JComponent> componentSupplier) {
      Object[] message = new Object[prompt.length * 2];

      for (int i = 0; i < prompt.length; i++) {
         JComponent editor;
         if (componentSupplier == null) {
            editor = new JTextField(initial[i]);
         } else {
            editor = componentSupplier.apply(Integer.valueOf(i), prompt[i]);
         }

         message[i * 2] = prompt[i];
         message[i * 2 + 1] = editor;
      }

      ((JTextField) message[1]).setSelectionStart(0);
      ((JTextField) message[1]).setSelectionEnd(initial[0].length());

      JOptionPane pane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
      JDialog dlg = pane.createDialog(parent, title);
      dlg.setResizable(false);
      dlg.setVisible(true);
      dlg.dispose();

      Object option = pane.getValue();
      if ((option == null) || ((option instanceof Integer) && (((Integer) option).intValue() != JOptionPane.OK_OPTION))) {
         return null;
      }

      String[] response = new String[prompt.length];
      for (int i = 0; i < prompt.length; i++) {
         Object comp = message[i * 2 + 1];

         if (comp instanceof JTextComponent) {
            response[i] = ((JTextComponent) comp).getText();
         } else if (comp instanceof JSpinner) {
            response[i] = ((JSpinner) comp).getValue().toString();
         }
      }

      return response;
   }

   private static JDialog getDialog(Component parentComponent, String title, boolean modal) {
      JDialog dialog = null;
      Window window = getWindowForComponent(parentComponent);
      if (window instanceof Frame) {
         dialog = new JDialog((Frame) window, title, modal);
      } else if (window instanceof Dialog) {
         dialog = new JDialog((Dialog) window, title, modal);
      }

      return dialog;
   }

   private static Window getWindowForComponent(Component comp) {
      if (comp == null) {
         return JOptionPane.getRootFrame();
      } else if (comp instanceof Frame || comp instanceof Dialog) {
         return (Window) comp;
      } else {
         return getWindowForComponent(comp.getParent());
      }
   }

   /**
    * Set the component that will own Dialogs that shown
    *
    * @param pMainComponent The main component that will own dialogs
    */
   public static void setMainComponent(Component pMainComponent) {
      mainComponent = pMainComponent;
   }

   /**
    * Interface to something which wants to get notified when a cancel button is pressed on the waiting dialog
    */
   public interface CancelListener {
      void waitingCancelled();
   }

}
