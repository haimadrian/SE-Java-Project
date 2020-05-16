package org.spa.controller.alert;

import org.spa.model.Severity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hadrian
 * @since 16-May-20
 */
public class AlertConfig {
   private List<Threshold> thresholds;

   public AlertConfig() {
      thresholds = new ArrayList<>();
      thresholds.add(new Threshold(0, 2, Severity.HIGH));
      thresholds.add(new Threshold(2, 5, Severity.MEDIUM));
      thresholds.add(new Threshold(5, 10, Severity.LOW));
      thresholds.add(new Threshold(10, Double.MAX_VALUE, Severity.NORMAL));
   }

   public List<Threshold> getThresholds() {
      return thresholds;
   }

   public void setThresholds(List<Threshold> thresholds) {
      this.thresholds = thresholds;
   }

   /**
    * Finding a specific threshold for the specified value. Each threshold is an interval with closed beginning and open ending.
    * @param value The value to find matching threshold for
    * @return The threshold matched for the specified value
    */
   public Threshold findMatchingThreshold(double value) {
      // ez pz
      return thresholds.stream().filter(t -> value >= t.getMin() && value < t.getMax()).findFirst().orElse(Threshold.DISABLED);
   }
}
