package org.spa.ui;

import org.spa.common.SPAApplication;
import org.spa.common.User;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.UserManagementService;
import org.spa.controller.UserManagementServiceObserver;
import org.spa.controller.cart.ShoppingCart;
import org.spa.controller.cart.ShoppingCartException;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.main.SPAMain;
import org.spa.model.Item;
import org.spa.model.user.Admin;
import org.spa.model.user.Customer;
import org.spa.model.user.SystemAdmin;
import org.spa.ui.alert.AlertsView;
import org.spa.ui.cart.ShoppingCartView;
import org.spa.ui.LoginView;
import org.spa.ui.util.Dialogs;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class HomePage extends JPanel implements SPAExplorerIfc<WarehouseItem>, UserManagementServiceObserver {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private JTable table;
    private JButton management;
    private JButton login;
    private JButton logout;
    private JTree categoryTree;
    private JTextField searchBar;
    private DefaultTableModel model;
    private JFrame mainForm;
    private ShoppingCartView shoppingCart;
    private AlertsView alerts;
    private JLabel lblUsername;
    private ImageIcon  spaLogo;
    private final UserManagementService userManagement;
    final ItemsWarehouse itemsWarehouse;

    public HomePage(JFrame parent) throws FileNotFoundException {
        itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
        userManagement = SPAApplication.getInstance().getUserManagementService();
        userManagement.registerObserver(this);
        final String path = new File("src\\main\\resources\\org\\spa\\ui\\homepagestuff").getAbsolutePath();
        mainForm = parent;
        File  read = new File(path+"\\data.txt");
        spaLogo = new ImageIcon(path+"\\SPALOGO_transparent_Small.png","The best electronic store money can buy");
        categoryTree = new JTree();
        management = new JButton("Management");
        shoppingCart = new ShoppingCartView(mainForm);
        alerts = new AlertsView(mainForm);
        logout = new JButton("Logout");
        logout.setVisible(false);
        login = new JButton("Login");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginView(parent);
            }

        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SPAApplication.getInstance().getUserManagementService().logout();
                //TODO clear cart
                logout.setVisible(false);
                management.setVisible(false);
                login.setVisible(true);
            }
        });
        String[] columnNames = {"Barcode","Picture", "Item name","Description","Price","Cart","Delete"};
        WarehouseItem[][] data = new WarehouseItem[0][columnNames.length-1];
        model = new DefaultTableModel(data, columnNames)
        {
            @Override
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
        table = new JTable( model );
        lblUsername = new JLabel("Hello guest.");
        searchBar = new JTextField("Search for product...",40);
        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchBar.setText("");
            }
        });
        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyTyped(e);
                DefaultTableModel table1 = (DefaultTableModel)table.getModel();
                String searchString = "(?i).*" + searchBar.getText() + ".*";
                TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(table1);
                table.setRowSorter(tr);
                tr.setRowFilter(RowFilter.regexFilter(searchString));
                logger.info(searchString);
            }
        });
        try {
            readFromFile(model,read,path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        tableConfiguration(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(shoppingCart.getNavigatingComponent());
        add(alerts.getNavigatingComponent());
        add(login);
        add(logout);
        add(categoryTree);
        add(searchBar);
        add(lblUsername);
        add(management);
        alerts.getNavigatingComponent().setVisible(false);
        management.setVisible(false);
        scrollPane.setPreferredSize(new Dimension(725, 400));
        JLabel imageContainer = new JLabel(spaLogo);
        add(imageContainer);
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        ComponentLocation(layout, this, shoppingCart.getNavigatingComponent(), alerts.getNavigatingComponent(), login, searchBar, scrollPane, categoryTree,imageContainer,lblUsername,management,logout);
        add(scrollPane);
    }
    public void tableConfiguration(JTable table){
        table.setRowHeight(80);
     //   table.setRowMargin(50);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(0).setWidth(0);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(2).setWidth(100);
        table.getColumnModel().getColumn(2).setMinWidth(100);
        table.getColumnModel().getColumn(2).setMaxWidth(100);
        table.getColumnModel().getColumn(3).setWidth(300);
        table.getColumnModel().getColumn(3).setMinWidth(300);
        table.getColumnModel().getColumn(3).setMaxWidth(300);
        table.getColumnModel().getColumn(5).setWidth(100);
        table.getColumnModel().getColumn(5).setMinWidth(100);
        table.getColumnModel().getColumn(5).setMaxWidth(100);

        //Makes the text be in the center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 2; i < 5; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        ButtonColumn cart = new ButtonColumn(table,null,5,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ShoppingCart shoppingCart = SPAApplication.getInstance().getShoppingCart();
                if(!table.getColumnName(table.getEditingColumn()).equalsIgnoreCase("cart"))
                    return;
                int modelRow =table.getSelectedRow();
//                int modelRow = Integer.parseInt(e.getActionCommand());
                String itemId = (String) ((DefaultTableModel) table.getModel()).getValueAt(modelRow, 0);
                try {
                    WarehouseItem shoppingCartItem = shoppingCart.getItems().stream().filter(item -> item.getId().equals(itemId)).findFirst().orElse(null);
                    shoppingCart.add(itemId, shoppingCartItem == null ? 1 : shoppingCartItem.getCount() + 1);
                } catch (ShoppingCartException ex) {
                    SwingUtilities.invokeLater(() -> Dialogs.showSimpleErrorDialog(null, ex.getMessage(), "Error"));
                }
            }
        }
        );
        ButtonColumn delete = new ButtonColumn(table,null,6,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!table.getColumnName(table.getEditingColumn()).equalsIgnoreCase("delete"))
                    return;
                int modelRow =table.getSelectedRow();
                ((DefaultTableModel)table.getModel()).removeRow(modelRow);
            }
        }
        );
        delete.setMnemonic(KeyEvent.VK_D);
        table.getColumn("Cart").setCellRenderer(cart);
        table.getColumn("Delete").setCellRenderer(delete);
    }

    @Override
    public void close() {
        getParentDialog().dispatchEvent(new WindowEvent(getParentDialog(), WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void updateTitle(String newTitle) {
        mainForm.setTitle(newTitle);
    }

    @Override
    public SelectionModelManager<WarehouseItem> getSelectionModel() {
        return null;
    }

    @Override
    public void updateStatus(String text) {}

    @Override
    public JComponent getNavigatingComponent() {
        return null;
    }

    @Override
    public Container getMainContainer() {
        return this;
    }

    @Override
    public Window getParentDialog() {
        return mainForm;
    }

    @Override
    public void userLogin(User loggedInUser) {
        if((loggedInUser instanceof SystemAdmin)
                || (loggedInUser instanceof Admin)) {
            alerts.getNavigatingComponent().setVisible(true);
            management.setVisible(true);
        }
        if((loggedInUser instanceof SystemAdmin)
                || (loggedInUser instanceof Admin)
                || (loggedInUser instanceof Customer))
        {
            lblUsername.setText("Hello " + loggedInUser.getUserId() + ".");
            login.setVisible(false);
            logout.setVisible(true);
        }
        else {
            alerts.getNavigatingComponent().setVisible(false);
            lblUsername.setText("Hello guest.");
        }
    }

    private static class JTableButtonRenderer implements TableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton)value;
            return button;
        }
    }

    public void ComponentLocation(SpringLayout layout,Container contentPane,Component cart,Component alerts,Component login,Component searchBar
                                 ,Component table,Component categoryTree,Component imageContainer,Component lblUsername,
                                  Component management,Component logout)    {
        layout.putConstraint(SpringLayout.NORTH,management,70,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,management,150,SpringLayout.EAST,searchBar);
        layout.putConstraint(SpringLayout.NORTH,alerts,5,SpringLayout.SOUTH, management);
        layout.putConstraint(SpringLayout.WEST,alerts,0, SpringLayout.WEST, management);
        layout.putConstraint(SpringLayout.NORTH,login,40,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,login,200,SpringLayout.EAST,searchBar);
        layout.putConstraint(SpringLayout.NORTH,logout,40,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,logout,195,SpringLayout.EAST,searchBar);
        layout.putConstraint(SpringLayout.NORTH,cart,80,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,cart,360,SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH,searchBar,135,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,searchBar,500,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.NORTH,table,200,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,table,300,SpringLayout.WEST, categoryTree);
        layout.putConstraint(SpringLayout.NORTH, categoryTree,200,SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, categoryTree,60,SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, imageContainer,40,SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, imageContainer,60,SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.NORTH, lblUsername,45,SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, lblUsername,890,SpringLayout.NORTH, searchBar);
    }

    public void readFromFile(DefaultTableModel model,File data,String path) throws FileNotFoundException {
        String id;
        String category;
        String imgName;
        String name;
        String description;
        String price;
        String profitPercent;
        String discountPercent;
        String count;
        String cart="Add to cart";
        Icon delete = new ImageIcon(path+"\\garbage.png");
        Scanner myReader = new Scanner(data);
        while (myReader.hasNextLine()) {
            imgName = myReader.nextLine();
            id = myReader.nextLine();
            category = myReader.nextLine();
            name = myReader.nextLine();
            description = myReader.nextLine();
            price = myReader.nextLine();
            profitPercent = myReader.nextLine();
            discountPercent = myReader.nextLine();
            count = myReader.nextLine();
            Icon icon = new ImageIcon(path+"\\"+imgName);
            itemsWarehouse.addItem(id,category,name,description,Double.parseDouble(price)
                    ,Double.parseDouble(profitPercent),Double.parseDouble(discountPercent),Integer.parseInt(count));
            Object[] object = {id,icon, name, description, price, cart,delete};
            model.addRow(object);
        }
        myReader.close();
    }
}

