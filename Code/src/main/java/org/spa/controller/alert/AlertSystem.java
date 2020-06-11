package org.spa.controller.alert;

import org.spa.controller.SPAApplication;
import org.spa.controller.Service;
import org.spa.controller.item.ItemsWarehouseObserver;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiConsumer;

/**
 * This class responsible for scheduling a task that runs every 5 minutes and checks if there
 * is any item which is out of stock or about to be out of stock
 *
 * @author Haim Adrian
 * @since 16-May-20
 */
public class AlertSystem implements Service, ItemsWarehouseObserver {
   static final int INITIAL_DELAY_SECONDS = 2;
   private static final Logger logger = LoggerFactory.getLogger(AlertSystem.class);
   private static final long ALERT_SYSTEM_CHECK_RATE_MINUTES = 5;
   private final List<AlertSystemObserver> observers;
   /**
    * Map between alert's key (The Item ID) to the alert
    */
   private final Map<String, Alert> alerts;
   private AlertConfig alertConfig;
   private ScheduledExecutorService alertSystemCheck;
   private ExecutorService notifier;

   /**
    * Constructs a new {@link AlertSystem}
    */
   public AlertSystem() {
      observers = new ArrayList<>();
      alertConfig = new AlertConfig();
      alerts = new HashMap<>();
   }

   /**
    * Call this method to start the scheduler
    */
   @Override
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
      logger.info("Scheduling AlertSystemCheckJob to run every " + ALERT_SYSTEM_CHECK_RATE_MINUTES + " minutes");
      alertSystemCheck.scheduleAtFixedRate(new AlertSystemCheckJob(), INITIAL_DELAY_SECONDS, ALERT_SYSTEM_CHECK_RATE_MINUTES * 60, TimeUnit.SECONDS);

      // Register for events about items, to get notified if they are deleted or added
      SPAApplication.getInstance().getItemsWarehouse().registerObserver(this);
   }

   /**
    * Call this method to stop the scheduler
    */
   @Override
   public void stop() {
      logger.info("Stopping AlertSystem");

      SPAApplication.getInstance().getItemsWarehouse().unregisterObserver(this);

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

   /**
    * @return how many alerts there are currently
    */
   public int count() {
      return alerts.size();
   }

   /**
    * Acknowledge an alert (remove it from alert system)
    *
    * @param alertKey The key of the alert to acknowledge
    */
   public void acknowledge(String alertKey) {
      Alert alert = alerts.remove(alertKey);
      if (alert != null) {
         notifyAboutAlert(alert, (observer, theAlert) -> observer.onAlertAcknowledged(this, theAlert));
      }
   }

   /**
    * Remove all alerts from alert system
    */
   public void clear() {
      new ArrayList<>(getAlerts()).forEach(alert -> acknowledge(alert.getKey()));
   }

   /**
    * @return The alerts there are currently in alert system
    */
   public Collection<Alert> getAlerts() {
      return Collections.unmodifiableCollection(alerts.values());
   }

   public AlertConfig getAlertConfig() {
      return alertConfig;
   }

   public void updateAlertConfig(AlertConfig newAlertConfig) {
      this.alertConfig = newAlertConfig;
   }

   /**
    * When you need to listen to events (when we generate an alert), register yourself using this method
    *
    * @param observer The listener. See {@link AlertSystemObserver}
    */
   public void registerAlertObserver(AlertSystemObserver observer) {
      observers.add(observer);
   }

   /**
    * When you do not which to get notified when alerts are generated, you use this method to unregister yourself
    *
    * @param observer A listener who has previously registered
    */
   public void unregisterAlertObserver(AlertSystemObserver observer) {
      observers.remove(observer);
   }

   /**
    * Use this method to notify observers asynchronously when there is a need to raise alert
    *
    * @param item The item to alert about
    * @param matchingThreshold A threshold to use for the alert
    */
   private void raiseAlert(WarehouseItem item, Threshold matchingThreshold) {
      String key = item.getId();
      String message = "Only " + item.getCount() + " left in stock for '" + item.getName() + "' (Item ID=" + item.getId() + ")";
      String severity = matchingThreshold.getSeverity().name();
      Date date = new Date(System.currentTimeMillis());

      Alert alert = new Alert(key, message, date.getTime(), Severity.valueOf(severity));
      alerts.put(key, alert);

      notifyAboutAlert(alert, (observer, theAlert) -> observer.onAlertTriggered(this, theAlert));
   }

   private void notifyAboutAlert(Alert alert, BiConsumer<AlertSystemObserver, Alert> func) {
      List<Callable<Void>> notificationWorkers = new ArrayList<>(observers.size());
      for (AlertSystemObserver observer : observers) {
         notificationWorkers.add(() -> {
            try {
               func.accept(observer, alert);
            } catch (Exception e) {
               logger.error("Error has occurred while notifying observer about alert: " + alert.getMessage());
            }

            return null;
         });
      }

      if (!notificationWorkers.isEmpty()) {
         logger.debug(() -> "Using executor to notify listeners about alert");

         try {
            notifier.invokeAll(notificationWorkers);
         } catch (Exception e) {
            logger.error("Error has occurred while notifying observers about alert");
         }
      }
   }

   @Override
   public void deleteItem(WarehouseItem item) {
      acknowledge(item.getId());
   }

   @Override
   public void updateItem(WarehouseItem item) {
      // Won't do
   }

   @Override
   public void addItem(WarehouseItem item) {
      raiseAlertIfNeeded(item);
   }

   private void raiseAlertIfNeeded(WarehouseItem item) {
      Threshold matchingThreshold = alertConfig.findMatchingThreshold(item.getCount());
      if (matchingThreshold.getSeverity() != Severity.DISABLED) {
         raiseAlert(item, matchingThreshold);
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
            raiseAlertIfNeeded(item);
         }
      }
   }
}
