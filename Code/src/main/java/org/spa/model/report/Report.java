package org.spa.model.report;

import java.util.Random;

public class Report {
   private final String reportID;

   public Report() {
      Random rand = new Random();
      this.reportID = String.valueOf(rand.nextInt(9999999));
   }

   public String getReportID() {
      return reportID;
   }
}
