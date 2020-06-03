package org.spa.ui.alert;

import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.alert.AlertSystem;
import org.spa.controller.alert.AlertSystemObserver;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.model.Alert;
import org.spa.ui.SPAExplorerIfc;
import org.spa.ui.control.ButtonWithBadge;
import org.spa.ui.table.PopupAdapter;
import org.spa.ui.table.TableConfig;
import org.spa.ui.table.TableManager;
import org.spa.ui.util.Controls;
import org.spa.ui.util.ImagesCache;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.spa.ui.util.Controls.createButton;

/**
 * @author Haim Adrian
 * @since 28-May-20
 */
public class AlertsView implements SPAExplorerIfc<Alert> {
   private static final Logger logger = LoggerFactory.getLogger(AlertsView.class);

   private JDialog alertsDialog;
   private final Window parent;
   private final ButtonWithBadge alertsButton;
   private final AlertSystem alertSystem;

   private final JPanel workArea;
   private final JLabel title;
   private final JButton clearButton;

   /**
    * The table where we display alerts at
    */
   private TableManager<AlertColumn, AlertViewInfo> tableManager;

   /**
    * Hold the list of the table here so we can update it outside table manager and then call refresh table.
    */
   private List<AlertViewInfo> tableModelList;

   public AlertsView(Window parent) {
      this.parent = parent;
      alertSystem = SPAApplication.getInstance().getAlertSystem();

      alertsButton = new ButtonWithBadge(ImagesCache.getInstance().getImage("alert-icon.png"));
      alertsButton.setSize(70, 70);
      alertsButton.setCountForBadge(alertSystem.count());
      alertsButton.addActionListener(e -> {
         logger.info("Opening Alerts Dialog");
         show();
      });

      createAlertsTable();

      title = Controls.createTitle("Alerts");

      clearButton = createButton("Clear All", e -> alertSystem.clear(), true);

      JPanel buttonsPanel = new JPanel();
      buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
      buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      buttonsPanel.add(Box.createHorizontalGlue());
      buttonsPanel.add(clearButton);

      workArea = new JPanel();
      workArea.setBorder(BorderFactory.createLineBorder(Color.gray, 1, true));
      workArea.setLayout(new BoxLayout(workArea, BoxLayout.PAGE_AXIS));
      workArea.add(title);
      workArea.add(Box.createRigidArea(new Dimension(0,5)));
      workArea.add(tableManager.getMainPanel());
      workArea.add(Box.createRigidArea(new Dimension(0,5)));
      workArea.add(buttonsPanel);
   }

   private void createAlertsTable() {
      List<AlertColumn> alertCols = Arrays.asList(AlertColumn.Severity, AlertColumn.Message, AlertColumn.Date);
      tableModelList = new ArrayList<>();
      tableManager = new TableManager<>(alertCols, tableModelList, TableConfig.create().withRowHeight(64).build());
      tableManager.setPopupAdapter(new PopupAdapter() {
         @Override
         protected List<JMenuItem> getMenuItemsForPopup() {
            JMenuItem item = new JMenuItem("Acknowledge");
            item.setDisplayedMnemonicIndex(0);
            item.addActionListener(e -> {
               AlertViewInfo selectedModel = tableManager.getSelectedModel();
               if (selectedModel != null) {
                  alertSystem.acknowledge(selectedModel.getKey());
               }
            });
            return Arrays.asList(item);
         }
      });

      logger.info("Registering alert listener");
      SPAApplication.getInstance().getAlertSystem().registerAlertObserver(new AlertSystemObserver() {
         @Override
         public void onAlertTriggered(AlertSystem alertSystem, Alert alert) {
            alertsButton.setCountForBadge(alertSystem.count());
            SwingUtilities.invokeLater(() -> {
               try {
                  tableModelList.add(new AlertViewInfo(alert.getKey(), alert.getMessage(), alert.getDate(), alert.getSeverity().name()));
                  tableManager.refresh();
               } catch (Throwable t) {
                  logger.error("Error has occurred while trying to add alert to table. severity=" + alert.getSeverity(), t);
               }
            });
         }

         @Override
         public void onAlertAcknowledged(AlertSystem alertSystem, Alert alert) {
            alertsButton.setCountForBadge(alertSystem.count());
            SwingUtilities.invokeLater(() -> {
               try {
                  tableModelList.removeIf(alertViewInfo -> alertViewInfo.getKey().equals(alert.getKey()));
                  tableManager.refresh();
               } catch (Throwable t) {
                  logger.error("Error has occurred while trying to remove alert from table. severity=" + alert.getSeverity(), t);
               }
            });
         }
      });
   }

   @Override
   public void show() {
      SPAApplication.getInstance().getSelectionModel().setSelection(this);
      if (alertsDialog == null) {
         alertsDialog = new JDialog(parent, "Alerts");
         alertsDialog.setUndecorated(true);
         alertsDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         int width = 700, height = 450;
         alertsDialog.setPreferredSize(new Dimension(width, height));
         Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
         alertsDialog.setLocation(mouseLocation.x - width, mouseLocation.y);

         alertsDialog.setContentPane(getMainContainer());
         alertsDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               logger.info("Closing Alerts Dialog");
               alertsDialog.remove(getMainContainer());
               alertsDialog = null;
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
               close();
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
               close();
            }
         });
         alertsDialog.pack();
         alertsDialog.setVisible(true);
      }
   }

   @Override
   public void close() {
      if (alertsDialog != null) {
         alertsDialog.dispatchEvent(new WindowEvent(alertsDialog, WindowEvent.WINDOW_CLOSING));
      }
      SPAApplication.getInstance().getSelectionModel().setSelection(null);
   }

   @Override
   public void updateTitle(String newTitle) {
      title.setText(newTitle);
      if (alertsDialog != null) {
         alertsDialog.setTitle(newTitle);
      }
   }

   @Override
   public SelectionModelManager<Alert> getSelectionModel() {
      return null;
   }

   @Override
   public void updateStatus(String text) {

   }

   @Override
   public JComponent getNavigatingComponent() {
      return alertsButton;
   }

   @Override
   public Container getMainContainer() {
      return workArea;
   }

   @Override
   public Window getParentDialog() {
      return alertsDialog;
   }
}
