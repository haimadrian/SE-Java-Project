package org.spa.ui.report;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.model.Order;
import org.spa.model.report.EconomicReport;
import org.spa.model.report.OrderReport;
import org.spa.model.report.StockReport;
import org.spa.ui.ManagerView;
import org.spa.ui.control.PrintSupport;
import org.spa.ui.util.DateLabelFormatter;
import org.spa.ui.util.ImagesCache;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.time.LocalDate.now;


public class ReportView {
    private static final Logger logger = LoggerFactory.getLogger(ReportView.class);
    JFrame frame;
    String kindOfReport;
    JTextArea reportText;
    JButton closeBtn;
    JButton printBtn;
    JLabel title;
    JLabel datetxt;
    JPanel panel;
    JDatePickerImpl dateStart;
    JDatePickerImpl dateEnd;
    JDatePanelImpl datePanel1;
    JDatePanelImpl datePanel2;

    public ReportView(String kindOfReport) {
        this.kindOfReport = kindOfReport;
        frame = new JFrame("Report View");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
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
        reportText = new JTextArea();
        datetxt = new JLabel();
        reportText.setEditable(false);
        panel.add(title);
        panel.add(datetxt);
        panel.add(reportText);
        panel.add((closeBtn = new JButton("Close")));
        closeBtn.setBounds(100, 600, 60, 50);
        panel.add(printBtn = new JButton(ImagesCache.getInstance().getImage("Printer.png")));
        printBtn.setBounds(180, 600, 60, 50);
        title.setBounds(350, 2, 200, 70);
        reportText.setBounds(330, 100, 500, 500);
        UtilDateModel model = new UtilDateModel();
        model.setSelected(true);
        Properties p = new Properties();
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
        datePanel1.setBounds(40, 150, 260, 200);
        panel.add(datePanel2);
        datePanel2.setBounds(40, 400, 260, 200);
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
      /*  //Calander change
        datePanel1.getModel().addChangeListener(changeEvent -> {
        });
        datePanel2.getModel().addChangeListener(changeEvent -> {

        });*/
    }

    private void generateReport() {
        Random rand = new Random();
        if(datePanel1.getModel().getDay() != 0 &&datePanel2.getModel().getDay() != 0 ) {
            Date dateStart = new Date(datePanel1.getModel().getYear(), datePanel1.getModel().getMonth(), datePanel1.getModel().getDay());
            Date dateEnd = new Date(datePanel2.getModel().getYear(), datePanel2.getModel().getMonth(), datePanel2.getModel().getDay());
            if (!dateStart.after(dateEnd)) {
                switch (kindOfReport) {
                    case "Stock": {
                        int randomID = rand.nextInt(9999999);
                        StockReport stockReport = new StockReport(String.valueOf(randomID));
                        List<WarehouseItem> items = stockReport.getItems();
                        items.forEach(item -> {
                            reportText.setText(reportText.getText() + "Item name:\t" + item.getName() + "\n" + "Quantity:\t" + item.getCount() + "\t\t\t\n\n");
                        });
                        break;
                    }
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
                                reportText.setText(reportText.getText() + "\tItem name: " + item.getName() + "\tQuantity:\t" + item.getCount() + (item.getPrice() - discountPrice) + "\n");
                            });
                        });
                        break;
                    }
                    case "Economic": {
                        int randomID = rand.nextInt(9999999);
//                    EconomicReport economicReport = new EconomicReport(String.valueOf(randomID), dateStart, dateEnd);
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }
}


