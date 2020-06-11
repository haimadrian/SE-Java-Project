package org.spa.controller.alert;

/**
 * @author Haim Adrian
 * @since 16-May-20
 */
public class Threshold {
   public static final Threshold DISABLED = new Threshold(Double.MIN_VALUE, Double.MAX_VALUE, Severity.DISABLED);
   private final double min;
   private final double max;
   private final Severity severity;

   public Threshold(double min, double max, Severity severity) {
      this.min = min;
      this.max = max;
      this.severity = severity;
   }

   public double getMin() {
      return min;
   }

   public double getMax() {
      return max;
   }

   public Severity getSeverity() {
      return severity;
   }
}
