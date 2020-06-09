package org.spa.ui;

import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.order.OrderSystem;
import org.spa.model.Order;
import org.spa.ui.login.Registration;
import org.spa.ui.order.OrderColumn;
import org.spa.ui.order.OrderInfoDialog;
import org.spa.ui.order.OrderViewInfo;
import org.spa.ui.report.ReportView;
import org.spa.ui.table.PopupAdapter;
import org.spa.ui.table.TableConfig;
import org.spa.ui.table.TableManager;
import org.spa.ui.util.Controls;
import org.spa.ui.util.Dialogs;
import org.spa.ui.util.ImagesCache;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.spa.ui.order.OrderCopying.orderToOrderViewInfo;
import static org.spa.ui.order.OrderCopying.orderViewInfoToOrder;

public class ManagerView {
    private static final Logger logger = LoggerFactory.getLogger(ManagerView.class);
    JFrame frame;

    public ManagerView(Window parent) {
        frame = new JFrame("Management View");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setPreferredSize(new Dimension(parent.getPreferredSize().width - 200, parent.getPreferredSize().height - 100));
        frame.add(new ManagementViewPane());
        frame.pack();
        Controls.centerDialog(parent, frame);
        frame.setVisible(true);
    }

    public class ManagementViewPane extends JPanel {

        private TitlePane title;
        private ReportsPane reportsPane;


        public ManagementViewPane() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 0.04;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.insets = new Insets(4, 4, 4, 4);

            add((title = new TitlePane()), gbc);
            gbc.gridy++;
            add((reportsPane = new ReportsPane()), gbc);
            gbc.gridy++;
            gbc.weighty = 0.92;
        }
    }

    public class TitlePane extends JPanel {


        public TitlePane() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;

            JLabel managerActions = new JLabel("Admin Actions");
            managerActions.setFont(new Font("Arial", Font.PLAIN, 30));
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add(managerActions, gbc);
        }

    }

    public class ReportsPane extends JPanel {

        private JButton stockReport, orderReport, economicReport;
        private JLabel warehouse;

        public ReportsPane() {
            setLayout(new GridBagLayout());
            setBorder(new CompoundBorder(new TitledBorder("Reports"), new EmptyBorder(0, 0, 0, 0)));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.WEST;


            JPanel panel = new JPanel(new GridBagLayout());
            panel.add(new JLabel("Generate Report: "), gbc);
            gbc.gridx++;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 0, 0, 0);
            panel.add((warehouse = new JLabel()), gbc);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(4, 4, 4, 4);
            add(panel, gbc);

            gbc.gridwidth = 1;
            gbc.weightx = 0.25;
            gbc.gridy++;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            add((stockReport = new JButton("Stock Report")), gbc);
            stockReport.addActionListener(actionEvent -> {
                new ReportView("Stock");
            });
            gbc.gridx++;
            add((orderReport = new JButton("Order Report")), gbc);
            orderReport.addActionListener(actionEvent -> {
                new ReportView("Order");
            });
            gbc.gridx++;
            add((economicReport = new JButton("Economic Report")), gbc);
            economicReport.addActionListener(actionEvent -> {
                new ReportView("Economic");
            });
        }
    }
}
