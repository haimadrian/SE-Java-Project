package org.spa.model.report;

import java.util.Random;

public class Report {
    private String reportID;
    public Report() {
        Random rand = new Random();
        Integer randomID = rand.nextInt(9999999);
        this.reportID = randomID.toString();
    }

    public String getReportID() {
        return reportID;
    }
}
