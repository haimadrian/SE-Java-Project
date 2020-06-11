package org.spa.controller.alert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spa.BaseTest;
import org.spa.controller.SPAApplication;
import org.spa.util.DummyDataForItemsWarehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Test class for {@link AlertSystem}
 *
 * @author Haim Adrian
 * @since 05-Jun-20
 */
public class AlertSystemTest extends BaseTest {
   private AlertSystem alertSystem;
   private AlertSystemObserverTest alertSystemObserver;

   @Before
   public void init() throws InterruptedException {
      alertSystem = SPAApplication.getInstance().getAlertSystem();
      alertSystemObserver = new AlertSystemObserverTest();
      alertSystem.registerAlertObserver(alertSystemObserver);

      DummyDataForItemsWarehouse.fillInDummyData(true);

      // Start alert system so it will launch its thread for scanning the warehouse
      alertSystem.start();

      // Wait for alert system to perform its first scan
      Thread.sleep(TimeUnit.SECONDS.toMillis(AlertSystem.INITIAL_DELAY_SECONDS + 1));
   }

   @After
   public void cleanup() {
      if (alertSystemObserver != null) {
         alertSystem.unregisterAlertObserver(alertSystemObserver);
      }
   }

   @Test
   public void TestAlertsTriggered_AlertsShouldBeTriggeredDueToDummyData() {
      // Assert
      assertNotEmpty("There supposed to be some alerts due to dummy data in warehouse", alertSystemObserver.triggeredAlerts);
      assertEmpty("No alerts supposed to be acknowledged", alertSystemObserver.acknowledgedAlerts);
   }

   @Test
   public void TestAcknowledgeAlert_AcknowledgedAlertShouldBeRemoved() {
      // Arrange
      Alert alert1 = alertSystemObserver.triggeredAlerts.get(0);
      alertSystemObserver.triggeredAlerts.clear();

      // Act
      alertSystem.acknowledge(alert1.getKey());

      // Assert
      assertEmpty("No alerts supposed to be triggered when we acknowledged an alert", alertSystemObserver.triggeredAlerts);
      assertNotEmpty("There supposed to be an acknowledged alert notification", alertSystemObserver.acknowledgedAlerts);
      assertEquals("Acknowledged alert supposed to have key: " + alert1.getKey(), alert1.getKey(), alertSystemObserver.acknowledgedAlerts.get(0).getKey());
      int count = (int) alertSystem.getAlerts().stream().filter(alert -> alert.getKey().equals(alert1.getKey())).count();
      assertEquals("Acknowledged alert supposed to be removed from alert system", 0, count);
   }

   static class AlertSystemObserverTest implements AlertSystemObserver {
      private final List<Alert> triggeredAlerts = new ArrayList<>();
      private final List<Alert> acknowledgedAlerts = new ArrayList<>();

      @Override
      public void onAlertTriggered(AlertSystem alertSystem, Alert alert) {
         triggeredAlerts.add(alert);
      }

      @Override
      public void onAlertAcknowledged(AlertSystem alertSystem, Alert alert) {
         acknowledgedAlerts.add(alert);
      }
   }
}
