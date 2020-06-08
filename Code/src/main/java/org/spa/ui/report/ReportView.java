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
    public static final int PAD = 10;
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
    JScrollPane scrollBar;
    JDatePickerImpl dateStart;
    JDatePickerImpl dateEnd;
    JDatePanelImpl datePanel1;
    JDatePanelImpl datePanel2;

    public ReportView(String kindOfReport) {
        this.kindOfReport = kindOfReport;
        frame = new JFrame("Report View");
        SpringLayout layout = new SpringLayout();
        frame.setLayout(layout);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(865, 600);
        frame.setLocationRelativeTo(null);
        panel = new JPanel();
        Container contentPane = frame.getContentPane();
        creatingComponents(layout,contentPane);
        generateReport( layout, contentPane);
        frame.getContentPane().add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void creatingComponents(SpringLayout layout,Container contentPane) {
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
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        datePanel1 = new JDatePanelImpl(model, p);
        datePanel2 = new JDatePanelImpl(model2, p2);
        dateStart = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
        dateEnd = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
        title = new JLabel();
        reportText = new JTextArea();
        reportText.setEditable(false);
        scrollBar = new JScrollPane(reportText);
        selectStartDateLbl = new JLabel("Select starting day:");
        selectDayEndLbl = new JLabel("Select ending day:");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        selectStartDateLbl.setFont(new Font("Arial", Font.PLAIN, 20));
        selectDayEndLbl.setFont(new Font("Arial", Font.PLAIN, 20));
        contentPane.add(title);
        //contentPane.add(reportText);
        contentPane.add(scrollBar);
        contentPane.add(selectStartDateLbl);
        contentPane.add((closeBtn = new JButton("Close")));
        contentPane.add(selectDayEndLbl);
        contentPane.add(printBtn = new JButton(ImagesCache.getInstance().getImage("Printer.png")));
        contentPane.add(datePanel1);
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
        }
        closeBtn.addActionListener(e -> {
            frame.dispose();
            frame = null;
        });
        printBtn.addActionListener(e -> PrintSupport.printComponent(reportText));
        componentLocation(layout,contentPane);
        contentPane.add(datePanel2);
    }
    private void componentLocation(SpringLayout layout,Container contentPane){
        closeBtn.setPreferredSize(printBtn.getPreferredSize());
        scrollBar.setPreferredSize(new Dimension(615,540));
        layout.putConstraint(SpringLayout.NORTH, title, PAD, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, title, PAD, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, selectStartDateLbl, title.getPreferredSize().height+PAD, SpringLayout.NORTH, title);
        layout.putConstraint(SpringLayout.WEST, selectStartDateLbl, PAD, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, datePanel1, 25, SpringLayout.NORTH, selectStartDateLbl);
        layout.putConstraint(SpringLayout.WEST, datePanel1, PAD, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, selectDayEndLbl, datePanel1.getPreferredSize().height+75, SpringLayout.NORTH, title);
        layout.putConstraint(SpringLayout.WEST, selectDayEndLbl, PAD, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, datePanel2,25, SpringLayout.NORTH, selectDayEndLbl);
        layout.putConstraint(SpringLayout.WEST, datePanel2, PAD, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, scrollBar, PAD, SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, scrollBar, PAD, SpringLayout.EAST, datePanel1);
        layout.putConstraint(SpringLayout.NORTH, printBtn,datePanel1.getPreferredSize().height+30 , SpringLayout.NORTH, datePanel2);
        layout.putConstraint(SpringLayout.WEST, printBtn, 40, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, closeBtn,datePanel1.getPreferredSize().height+30 , SpringLayout.NORTH, datePanel2);
        layout.putConstraint(SpringLayout.WEST, closeBtn, PAD, SpringLayout.EAST, printBtn);
    }
    private void stockReportRelocation(SpringLayout layout,Container contentPane)
    {
        datePanel1.setVisible(false);
        datePanel2.setVisible(false);
        selectStartDateLbl.setVisible(false);
        selectDayEndLbl.setVisible(false);
        scrollBar.setPreferredSize(new Dimension(630,485));
        layout.putConstraint(SpringLayout.NORTH, scrollBar,title.getPreferredSize().height, SpringLayout.SOUTH, title);
        layout.putConstraint(SpringLayout.WEST, scrollBar, PAD, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, printBtn,20, SpringLayout.SOUTH, scrollBar);
        layout.putConstraint(SpringLayout.WEST, printBtn, 242, SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH, closeBtn,20 , SpringLayout.SOUTH, scrollBar);
        layout.putConstraint(SpringLayout.WEST, closeBtn, PAD, SpringLayout.EAST, printBtn);
        frame.setSize(660,700);
    }
    private void generateReport(SpringLayout layout,Container contentPane) {
        Random rand = new Random();
        if (kindOfReport.equals("Stock")){
            stockReportRelocation( layout, contentPane);
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


