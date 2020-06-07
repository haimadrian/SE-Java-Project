package org.spa.ui.report;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.item.WarehouseItem;
import org.spa.model.Order;
import org.spa.model.report.EconomicReport;
import org.spa.model.report.OrderReport;
import org.spa.model.report.StockReport;
import org.spa.ui.control.PrintSupport;
import org.spa.ui.util.DateLabelFormatter;
import org.spa.ui.util.ImagesCache;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static java.time.LocalDate.now;


public class ReportView {
    private static final Logger logger = LoggerFactory.getLogger(ReportView.class);
    JFrame frame;
    String kindOfReport;
    JTextArea reportText;
    JButton closeBtn;
    JButton printBtn;
    JLabel title;
    JLabel selectStartDateLbl;
    JLabel selectDayEndLbl;
    JPanel panel;
    JDatePickerImpl dateStart;
    JDatePickerImpl dateEnd;
    JDatePanelImpl datePanel1;
    JDatePanelImpl datePanel2;

    public ReportView(String kindOfReport) {
        this.kindOfReport = kindOfReport;
        frame = new JFrame("Report View");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(860, 600);
        frame.setLocationRelativeTo(null);
        panel = new JPanel();
        frame.add(panel);
        placeComponents();
        generateReport();
        frame.setVisible(true);
    }


    private void placeComponents() {
        boolean alreadyInUse = false;
        panel.setLayout(null);
        title = new JLabel();
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setBounds(20, 1, 300, 70);
        panel.add(title);
        reportText = new JTextArea();
        JScrollPane scrollBar = new JScrollPane(reportText);
        selectStartDateLbl = new JLabel("Select starting day:");
        selectDayEndLbl = new JLabel("Select ending day:");
        reportText.setEditable(false);
        panel.add(selectStartDateLbl);
        selectStartDateLbl.setBounds(20, 50, 200, 50);
        selectStartDateLbl.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(selectDayEndLbl);
        selectDayEndLbl.setBounds(20,275,200,50);
        selectDayEndLbl.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add((closeBtn = new JButton("Close")));
        closeBtn.setBounds(80, 500, 60, 50);
        panel.add(printBtn = new JButton(ImagesCache.getInstance().getImage("Printer.png")));
        printBtn.setBounds(160, 500, 60, 50);
        panel.add(reportText);
        panel.add(scrollBar);
        reportText.setBounds(310, 5, 520, 550);
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        UtilDateModel model2 = new UtilDateModel();
        model2.setSelected(true);
        Properties p2 = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel1 = new JDatePanelImpl(model, p);
        datePanel2 = new JDatePanelImpl(model2, p2);
        dateStart = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
        dateEnd = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
        panel.add(datePanel1);
        datePanel1.setBounds(20, 91, 260, 170);
        panel.add(datePanel2);
        datePanel2.setBounds(20, 315 , 260, 170);
        switch (kindOfReport) {
            case "Economic": {
                title.setText("Economic Report");
                break;
            }
            case "Order": {
                title.setText("Orders Report");
                break;
            }
            case "Stock":
                title.setText("Stock Report");
            default:
                break;
        }//Close form
        closeBtn.addActionListener(e -> {
            frame.dispose();
            frame = null;
        });
        //Print
        printBtn.addActionListener(e -> PrintSupport.printComponent(reportText));
    }

    private void stockReportrelocation()
    {
        datePanel1.setVisible(false);
        datePanel2.setVisible(false);
        selectStartDateLbl.setVisible(false);
        selectDayEndLbl.setVisible(false);
        reportText.setBounds(20, 55, 520, 440);
        closeBtn.setBounds(220, 500, 60, 50);
        printBtn.setBounds(280, 500, 60, 50);
        frame.setSize(565,600);
    }
    private void generateReport() {
        Random rand = new Random();
        if (kindOfReport.equals("Stock")){
            stockReportrelocation();
            int randomID = rand.nextInt(9999999);
            StockReport stockReport = new StockReport(String.valueOf(randomID));
            List<WarehouseItem> items = stockReport.getItems();
            items.forEach(item -> {
                reportText.setText(reportText.getText() + "Item name:\t" + item.getName() + "\n" + "Quantity:\t" + item.getCount() + "\n");
            });
            return;
        }
        datePanel1.setVisible(true);
        datePanel2.setVisible(true);
        datePanel1.getModel().addChangeListener(changeEvent -> {
            reportText.setText("");
            Date dateStart = new Date(datePanel1.getModel().getYear()-1900, datePanel1.getModel().getMonth(), datePanel1.getModel().getDay());
            Date dateEnd = new Date(datePanel2.getModel().getYear()-1900, datePanel2.getModel().getMonth(), datePanel2.getModel().getDay());
            if (!dateStart.after(dateEnd)) {
                switch (kindOfReport) {
                    case "Order": {
                        int randomID = rand.nextInt(9999999);
                        OrderReport stockReport = new OrderReport(String.valueOf(randomID), dateStart, dateEnd);
                        Map<String, Order> orders = stockReport.getOrders();
                        orders.values().stream().forEach(order -> {
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                            Date convertedDate = new Date(order.getOrderTime());
                            reportText.setText(reportText.getText() + "Order ID: " + order.getOrderId() + "\tOrder date: " + sdf.format(convertedDate) + "\n");
                            order.getItems().forEach(item -> {
                                double discountPrice = item.getPrice() * item.getDiscountPercent() / 100;
                                reportText.setText(reportText.getText() + "\tItem name: " + item.getName() + "\tQuantity:\t" + item.getCount()+ "\tTotal Price:\t" + (item.getPrice() - discountPrice) + "\n");
                            });
                            reportText.setText(reportText.getText()+"\n\n");
                        });
                        break;
                    }
                    case "Economic": {
                        int randomID = rand.nextInt(9999999);
                        EconomicReport economicReport = new EconomicReport(String.valueOf(randomID));
                        reportText.setText("Total expenses:\t" +economicReport.getExpenses()+"\n"+
                                "Total incoming:\t" +economicReport.getIncoming()+"\n"+
                                "Total profit:\t" +economicReport.calculateTotalProfit());
                        break;
                    }
                    default:
                        break;
                }
            }
        });
        datePanel2.getModel().addChangeListener(changeEvent -> {
        Date dateStart = new Date(datePanel1.getModel().getYear()-1900, datePanel1.getModel().getMonth(), datePanel1.getModel().getDay());
        Date dateEnd = new Date(datePanel2.getModel().getYear()-1900, datePanel2.getModel().getMonth(), datePanel2.getModel().getDay());
        reportText.setText("");
        if (!dateStart.after(dateEnd)) {
            switch (kindOfReport) {
                case "Order": {
                    logger.info(""+dateStart);
                    logger.info(""+dateEnd);
                    int randomID = rand.nextInt(9999999);
                    OrderReport stockReport = new OrderReport(String.valueOf(randomID), dateStart, dateEnd);
                    Map<String, Order> orders = stockReport.getOrders();
                    orders.values().stream().forEach(order -> {
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                        Date convertedDate = new Date(order.getOrderTime());
                        reportText.setText(reportText.getText() + "Order ID: " + order.getOrderId() + "\tOrder date: " + sdf.format(convertedDate) + "\n");
                        order.getItems().forEach(item -> {
                            double discountPrice = item.getPrice() * item.getDiscountPercent() / 100;
                            reportText.setText(reportText.getText() + "\tItem name: " + item.getName() + "\tQuantity:\t" + item.getCount()+ "\tTotal Price:\t" + (item.getPrice() - discountPrice) + "\n");
                        });
                        reportText.setText(reportText.getText()+"\n\n");
                    });
                    break;
                }
                case "Economic": {
                    int randomID = rand.nextInt(9999999);
                    EconomicReport economicReport = new EconomicReport(String.valueOf(randomID));
                    reportText.setText("Total expenses:\t" +economicReport.getExpenses()+"\n"+
                            "Total incoming:\t" +economicReport.getIncoming()+"\n"+
                            "Total profit:\t" +economicReport.calculateTotalProfit());
                    break;
                }
                default:
                    break;
            }
        }
        });
    }
}


