package org.spa.controller.action.impl;

public class EconomicReportAction extends AbstractAction<String> {
   @Override
   public String execute() {
      return context.getReportSystem().generateEconomicReport();
   }
}
