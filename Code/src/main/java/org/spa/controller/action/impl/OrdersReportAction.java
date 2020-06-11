package org.spa.controller.action.impl;

public class OrdersReportAction extends AbstractAction<String> {
   @Override
   public String execute() {
      return context.getReportSystem().generateOrdersReport
            (context.getValue("dateStart"), context.getValue("dateEnd"));
   }
}
