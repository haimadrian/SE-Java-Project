package org.spa.controller.alert;

import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.item.WarehouseItem;
import org.spa.model.Severity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class responsible for scheduling a task that runs every 5 minutes and checks if there
 * is any item which is out of stock or about to be out of stock
 * @author Haim Adrian
 * @since 16-May-20
 */
public class AlertSystem {
   private static final Logger logger = LoggerFactory.getLogger(AlertSystem.class);
   private static final long ALERT_SYSTEM_CHECK_RATE_MINUTES = 5;

   private AlertConfig alertConfig;

   private final List<AlertSystemObserver> observers;
   private ScheduledExecutorService alertSystemCheck;
   private ExecutorService notifier;

   /**
    * Constructs a new {@link AlertSystem}
    */
   public AlertSystem() {
      observers = new ArrayList<>();
      alertConfig = new AlertConfig();
   }

   /**
    * Call this method to start the scheduler
    */
   public void start() {
      logger.info("Starting AlertSystem");

      // Use a custom thread factory so we can give a name with meaning, which we can see later in log and jconsole
      notifier = Executors.newCachedThreadPool(r -> {
         Thread t = Executors.defaultThreadFactory().newThread(r);
         t.setName("AlertSystemNotifier#" + t.getId());
         return t;
      });

      alertSystemCheck = Executors.newSingleThreadScheduledExecutor(r -> {
         Thread t = Executors.defaultThreadFactory().newThread(r);
         t.setName("AlertSystemCheck");
         return t;
      });

      // Schedule our job to run within 2 seconds and then every 5 minutes
      logger.info("Scheduling AlertSystemCheckJob to run every " + ALERT_SYSTEM_CHECK_RATE_MINUTES + " minute/s");
      alertSystemCheck.scheduleAtFixedRate(new AlertSystemCheckJob(), 2, ALERT_SYSTEM_CHECK_RATE_MINUTES*60, TimeUnit.SECONDS);
   }

   /**
    * Call this method to stop the scheduler
    */
   public void stop() {
      logger.info("Stopping AlertSystem");

      if (alertSystemCheck != null) {
         try {
            alertSystemCheck.shutdownNow();
         } catch (Exception e) {
            logger.error("Error has occurred while shutting down alert system check scheduler", e);
         }

         try {
            notifier.shutdownNow();
         } catch (Exception e) {
            logger.error("Error has occurred while shutting down alert system notifier", e);
         }
      }
   }

   public AlertConfig getAlertConfig() {
      return alertConfig;
   }

   public void updateAlertConfig(AlertConfig newAlertConfig) {
      this.alertConfig = newAlertConfig;
   }

   /**
    * When you need to listen to events (when we generate an alert), register yourself using this method
    * @param observer The listener. See {@link AlertSystemObserver}
    */
   public void registerAlertObserver(AlertSystemObserver observer) {
      observers.add(observer);
   }

   /**
    * When you do not which to get notified when alerts are generated, you use this method to unregister yourself
    * @param observer A listener who has previously registered
    */
   public void unregisterAlertObserver(AlertSystemObserver observer) {
      observers.remove(observer);
   }

   /**
    * Use this method to notify observers asynchronously when there is a need to raise alert
    * @param item The item to alert about
    * @param matchingThreshold A threshold to use for the alert
    */
   // TODO haim: AlertSystem should manage alerts in a map and the UI will ask for them when we call it to refresh itself.
   //  This is so we can update existing alerts instead of showing them over and over
   private void raiseAlert(WarehouseItem item, Threshold matchingThreshold) {
      String key = item.getId();
      String message = "<b>Only " + item.getCount() + " left</b> in stock for '" + item.getName() + "' <b>(Item ID=" + item.getId() + ")</b>";
      String severity = matchingThreshold.getSeverity().name();
      Date date = new Date(System.currentTimeMillis());

      List<Callable<Void>> notificationWorkers = new ArrayList<>(observers.size());
      for (AlertSystemObserver observer : observers) {
         notificationWorkers.add(() -> {
            try {
               logger.info("Raising alert: " + message);
               observer.onAlertTriggered(key, message, severity, date);
            } catch (Exception e) {
               logger.error("Error has occurred while notifying observer about alert: " + message);
            }

            return null;
         });
      }

      if (!notificationWorkers.isEmpty()) {
         logger.info("Using executor to notify listeners about alert");

         try {
            notifier.invokeAll(notificationWorkers);
         } catch (Exception e) {
            logger.error("Error has occurred while notifying observers about alert");
         }
      }
   }

   /**
    * The job that we schedule to run in background every some minutes, to check the stock
    */
   private class AlertSystemCheckJob implements Runnable {
      @Override
      public void run() {
         logger.debug(() -> "Running stock check to see if there is a need to alert about it");
         List<WarehouseItem> items = SPAApplication.getInstance().getItemsWarehouse().getItems();

         for (WarehouseItem item : items) {
            Threshold matchingThreshold = alertConfig.findMatchingThreshold(item.getCount());
            if (matchingThreshold.getSeverity() != Severity.DISABLED) {
               raiseAlert(item, matchingThreshold);
            }
         }
      }
   }
}
