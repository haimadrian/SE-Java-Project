package org.spa.view.report;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.spa.controller.action.ActionException;
import org.spa.controller.action.ActionManager;
import org.spa.controller.action.ActionType;
import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;
import org.spa.view.control.PrintSupport;
import org.spa.view.util.Controls;
import org.spa.view.util.DateLabelFormatter;
import org.spa.view.util.Fonts;
import org.spa.view.util.ImagesCache;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.spa.driver.SPAMain.FRAME_ICON_NAME;


public class ReportView {
   public static final int PAD = 10;
   private static final Logger logger = LoggerFactory.getLogger(ReportView.class);
   JFrame frame;
   String kindOfReport;
   JTextArea reportText;
   JButton closeBtn;
   JButton printBtn;
   JButton stockReport;
   JButton economicReport;
   JButton orderReport;
   JLabel title;
   JLabel selectStartDateLbl;
   JLabel selectDayEndLbl;
   JPanel panel;
   JPanel buttonsPanel;
   JScrollPane scrollBar;
   JDatePickerImpl dateStart;
   JDatePickerImpl dateEnd;
   JDatePanelImpl datePanel1;
   JDatePanelImpl datePanel2;

   public ReportView(Window parent, String kindOfReport) {

      this.kindOfReport = kindOfReport;
      frame = new JFrame("Report View");
      SpringLayout layout = new SpringLayout();
      frame.setLayout(layout);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setSize(868, 600);
      frame.setIconImage(ImagesCache.getInstance().getImage(FRAME_ICON_NAME).getImage());
      Controls.centerDialog(parent, frame);
      panel = new JPanel();
      buttonsPanel = new JPanel();
      buttonsPanel.setBorder(BorderFactory.createTitledBorder("Reports:"));
      Container contentPane = frame.getContentPane();
      creatingComponents(layout, contentPane);
      generateReport(layout, contentPane);
      frame.getContentPane().add(buttonsPanel);
      frame.getContentPane().add(panel);
      frame.setResizable(false);
      frame.setVisible(true);
   }

   private void creatingComponents(SpringLayout layout, Container contentPane) {
      panel.setLayout(layout);
      panel.setVisible(true);
      UtilDateModel model = new UtilDateModel();
      model.setSelected(true);
      Properties p = new Properties();
      p.put("text.today", "Today");
      p.put("text.month", "Month");
      p.put("text.year", "Year");
      UtilDateModel model2 = new UtilDateModel();
      model2.setSelected(true);
      Properties p2 = new Properties();
      p2.put("text.today", "Today");
      p2.put("text.month", "Month");
      p2.put("text.year", "Year");
      datePanel1 = new JDatePanelImpl(model, p);
      datePanel2 = new JDatePanelImpl(model2, p2);
      dateStart = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
      dateEnd = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
      stockReport = new JButton("Stock Report");
      economicReport = new JButton("Economic Report");
      orderReport = new JButton("Orders Report");
      title = new JLabel();
      reportText = new JTextArea();
      reportText.setEditable(false);
      scrollBar = new JScrollPane(reportText);
      selectStartDateLbl = new JLabel("Select starting day:");
      selectDayEndLbl = new JLabel("Select ending day:");
      title.setFont(Fonts.HEADING_FONT);
      selectStartDateLbl.setFont(Fonts.PLAIN_FONT);
      selectDayEndLbl.setFont(Fonts.PLAIN_FONT);
      contentPane.add(title);
      contentPane.add(scrollBar);
      contentPane.add(selectStartDateLbl);
      contentPane.add((closeBtn = new JButton("Close")));
      contentPane.add(selectDayEndLbl);
      contentPane.add(printBtn = new JButton(ImagesCache.getInstance().getImage("Printer.png")));
      contentPane.add(datePanel1);
      title.setText(kindOfReport + " Report");
      closeBtn.addActionListener(e -> {
         frame.dispose();
         frame = null;
      });
      printBtn.addActionListener(e -> PrintSupport.printComponent(reportText));
      componentLocation(layout, contentPane);
      contentPane.add(datePanel2);
      orderReport.setEnabled(false);
      buttonsPanel.add(stockReport);
      stockReport.addActionListener(actionEvent -> {
         kindOfReport = ("Stock");
         economicReport.setEnabled(true);
         orderReport.setEnabled(true);
         stockReport.setEnabled(false);
         title.setText(kindOfReport + " Report");
         reportText.setText("");

         generateReport(layout, contentPane);
      });
      buttonsPanel.add(orderReport);
      orderReport.addActionListener(actionEvent -> {
         kindOfReport = ("Order");
         economicReport.setEnabled(true);
         orderReport.setEnabled(false);
         stockReport.setEnabled(true);
         title.setText(kindOfReport + " Report");
         reportText.setText("");

         componentLocation(layout, contentPane);
      });
      buttonsPanel.add(economicReport);
      economicReport.addActionListener(actionEvent -> {
         kindOfReport = "Economic";
         economicReport.setEnabled(false);
         orderReport.setEnabled(true);
         stockReport.setEnabled(true);
         title.setText(kindOfReport + " Report");
         reportText.setText("");
         generateReport(layout, contentPane);
      });
   }

   private void componentLocation(SpringLayout layout, Container contentPane) {
      datePanel1.setVisible(true);
      datePanel2.setVisible(true);
      selectStartDateLbl.setVisible(true);
      selectDayEndLbl.setVisible(true);
      stockReport.setPreferredSize(economicReport.getPreferredSize());
      orderReport.setPreferredSize(economicReport.getPreferredSize());
      scrollBar.setPreferredSize(new Dimension(500, title.getPreferredSize().height * 2 + datePanel1.getPreferredSize().height * 2));
      frame.setSize(750, 600);
      layout.putConstraint(SpringLayout.NORTH, title, 0, SpringLayout.SOUTH, buttonsPanel);
      layout.putConstraint(SpringLayout.WEST, title, PAD, SpringLayout.WEST, contentPane);
      layout.putConstraint(SpringLayout.NORTH, selectStartDateLbl, title.getPreferredSize().height + PAD, SpringLayout.NORTH, title);
      layout.putConstraint(SpringLayout.WEST, selectStartDateLbl, PAD, SpringLayout.WEST, contentPane);
      layout.putConstraint(SpringLayout.NORTH, datePanel1, 25, SpringLayout.NORTH, selectStartDateLbl);
      layout.putConstraint(SpringLayout.WEST, datePanel1, PAD, SpringLayout.WEST, contentPane);
      layout.putConstraint(SpringLayout.NORTH, selectDayEndLbl, datePanel1.getPreferredSize().height + 75, SpringLayout.NORTH, title);
      layout.putConstraint(SpringLayout.WEST, selectDayEndLbl, PAD, SpringLayout.WEST, contentPane);
      layout.putConstraint(SpringLayout.NORTH, datePanel2, 25, SpringLayout.NORTH, selectDayEndLbl);
      layout.putConstraint(SpringLayout.WEST, datePanel2, PAD, SpringLayout.WEST, contentPane);
      layout.putConstraint(SpringLayout.NORTH, scrollBar, PAD, SpringLayout.SOUTH, buttonsPanel);
      layout.putConstraint(SpringLayout.WEST, scrollBar, PAD, SpringLayout.EAST, datePanel1);
      layout.putConstraint(SpringLayout.NORTH, printBtn, 2 * PAD, SpringLayout.NORTH, contentPane);
      layout.putConstraint(SpringLayout.WEST, printBtn, PAD, SpringLayout.EAST, buttonsPanel);
      layout.putConstraint(SpringLayout.NORTH, closeBtn, 2 * PAD, SpringLayout.NORTH, contentPane);
      layout.putConstraint(SpringLayout.EAST, closeBtn, -PAD, SpringLayout.EAST, contentPane);
      closeBtn.setPreferredSize(printBtn.getPreferredSize());
   }

   private void stockEconomicReportsRelocation(SpringLayout layout, Container contentPane) {
      datePanel1.setVisible(false);
      datePanel2.setVisible(false);
      selectStartDateLbl.setVisible(false);
      selectDayEndLbl.setVisible(false);
      scrollBar.setPreferredSize(new Dimension(630, 485));
      layout.putConstraint(SpringLayout.NORTH, scrollBar, 0, SpringLayout.SOUTH, title);
      layout.putConstraint(SpringLayout.WEST, scrollBar, PAD, SpringLayout.WEST, contentPane);
      layout.putConstraint(SpringLayout.NORTH, printBtn, PAD, SpringLayout.SOUTH, scrollBar);
      layout.putConstraint(SpringLayout.WEST, printBtn, 242, SpringLayout.WEST, contentPane);
      layout.putConstraint(SpringLayout.NORTH, closeBtn, PAD, SpringLayout.SOUTH, scrollBar);
      layout.putConstraint(SpringLayout.WEST, closeBtn, PAD, SpringLayout.EAST, printBtn);
      frame.setSize(660, 710);
   }

   private void generateReport(SpringLayout layout, Container contentPane) {
      if (kindOfReport.equals("Order")) {
         datePanel1.setVisible(true);
         datePanel2.setVisible(true);
         reportText.setText("");

         ChangeListener dateChangeListener = changeEvent -> {
            Date dateStart = (Date) datePanel1.getModel().getValue();
            Date dateEnd = (Date) datePanel2.getModel().getValue();
            if ((dateStart != null && dateEnd != null) && (!dateStart.after(dateEnd))) {
               try {
                  Map<String, Object> params = new HashMap<>();
                  params.put("dateStart", dateStart);
                  params.put("dateEnd", dateEnd);
                  reportText.setText(ActionManager.executeAction(ActionType.GenerateOrdersReport, params));
               } catch (ActionException e) {
                  logger.error("Error has occurred:", e);
               }
            }
         };

         //If user change the first date panel
         datePanel1.getModel().addChangeListener(dateChangeListener);

         //If user change the second date panel
         datePanel2.getModel().addChangeListener(dateChangeListener);
      }
      switch (kindOfReport) {
         case "Stock": {
            stockEconomicReportsRelocation(layout, contentPane);
            try {
               reportText.setText(ActionManager.executeAction(ActionType.GenerateStockReport));
            } catch (ActionException e) {
               logger.error("Error has occurred:", e);
            }
            break;
         }
         case "Economic": {
            stockEconomicReportsRelocation(layout, contentPane);
            try {
               reportText.setText(ActionManager.executeAction(ActionType.GenerateEconomicReport));
            } catch (ActionException e) {
               logger.error("Error has occurred:", e);
            }
            break;
         }
         default:
            break;
      }
   }
}



