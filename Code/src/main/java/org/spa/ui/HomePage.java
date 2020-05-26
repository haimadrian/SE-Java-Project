package org.spa.ui;

import org.spa.common.SPAApplication;
import org.spa.common.User;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.UserManagementService;
import org.spa.controller.UserManagementServiceObserver;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.main.SPAMain;
import org.spa.model.user.Admin;
import org.spa.model.user.Customer;
import org.spa.model.user.SystemAdmin;
import org.spa.ui.cart.ShoppingCartView;
import org.spa.ui.LoginView;
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
    private JLabel lblUsername;
    private ImageIcon  spaLogo;
    private final UserManagementService userManagement;
    private final ItemsWarehouse itemsWarehouse;

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
        logout = new JButton("Logout");
        logout.setVisible(false);
        login = new JButton("Login");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginView lv= new LoginView();
                lv.LoginView();
            }

        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SPAApplication.getInstance().getUserManagementService().logout();
                logout.setVisible(false);
                management.setVisible(false);
                login.setVisible(true);
            }
        });
        String[] columnNames = {"Picture", "Item name","Description","Price","Cart","Delete"};
        WarehouseItem[][] data = new WarehouseItem[0][6];
        model = new DefaultTableModel(data, columnNames)
        {
            @Override
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
            public void addRow(String barcode,ImageIcon icon,String title,String description) {
                //super.addRow();
              /*  Object[] rowData = {barcode,icon,title,description};
                super.addRow(rowData);*/
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
        add(login);
        add(logout);
        add(categoryTree);
        add(searchBar);
        add(lblUsername);
        add(management);
        management.setVisible(false);
        scrollPane.setPreferredSize(new Dimension(725, 400));
        JLabel imageContainer = new JLabel(spaLogo);
        add(imageContainer);
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        ComponentLocation(layout, this, shoppingCart.getNavigatingComponent(), login, searchBar, scrollPane, categoryTree,imageContainer,lblUsername,management,logout);
        add(scrollPane);
    }

    private static TableModel createTableModel() {
        Vector<String> columns = new Vector<>(Arrays.asList("Picture", "Item name","Description","Price","Cart","Delete"));
        Vector<Vector<Object>> rows = new Vector<>();
           /* Vector<Object> v = new Vector<>();
            rows.add(v);*/
            DefaultTableModel model = new DefaultTableModel(rows, columns) {
            @Override
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
        return model;
    }
    public void tableConfiguration(JTable table){
        table.setRowHeight(80);
        table.setRowMargin(50);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.getTableHeader().setReorderingAllowed(false);
        //For user - we hide the delete button
/*        if(isAdmin()) {
            table.getColumnModel().getColumn(5).setWidth(100);
            table.getColumnModel().getColumn(5).setMinWidth(100);
            table.getColumnModel().getColumn(5).setMaxWidth(100);
        }
        else{
            table.getColumnModel().getColumn(5).setWidth(0);
            table.getColumnModel().getColumn(5).setMinWidth(0);
            table.getColumnModel().getColumn(5).setMaxWidth(0);
        }*/
        table.setAutoCreateRowSorter(true);
        table.getColumnModel().getColumn(1).setWidth(100);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(1).setMaxWidth(100);
        table.getColumnModel().getColumn(2).setWidth(300);
        table.getColumnModel().getColumn(2).setMinWidth(300);
        table.getColumnModel().getColumn(2).setMaxWidth(300);
        table.getColumnModel().getColumn(4).setWidth(100);
        table.getColumnModel().getColumn(4).setMinWidth(100);
        table.getColumnModel().getColumn(4).setMaxWidth(100);

        //Makes the text be in the center
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i < 4; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        ButtonColumn cart = new ButtonColumn(table,null,4);
        ButtonColumn delete = new ButtonColumn(table,null,5);
        table.getColumn("Cart").setCellRenderer(cart);
        table.getColumn("Delete").setCellRenderer(delete);
    }
    private boolean DEBUG = false;

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
    public void updateStatus(String text) {

    }

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
                || (loggedInUser instanceof Admin))
        management.setVisible(true);
        if((loggedInUser instanceof SystemAdmin)
                || (loggedInUser instanceof Admin)
                || (loggedInUser instanceof Customer))
        {
            lblUsername.setText("Hello " + loggedInUser.getUserId() + ".");
            login.setVisible(false);
            logout.setVisible(true);
        }
        else lblUsername.setText("Hello guest.");
    }

    private static class JTableButtonRenderer implements TableCellRenderer {
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton)value;
            return button;
        }
    }

    public void frameComponent(JFrame frame) {
        SpringLayout layout = new SpringLayout();

        //searchBar.setPreferredSize(new Dimension(100, 30)); //NOT WORKING


    }
    public void ComponentLocation(SpringLayout layout,Container contentPane,Component cart,Component login,Component searchBar
                        ,Component table,Component categoryTree,Component imageContainer,Component lblUsername,Component management,Component logout)    {
        layout.putConstraint(SpringLayout.NORTH,management,70,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,management,150,SpringLayout.EAST,searchBar);
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
        String profitPrecent;
        String discountPercent;
        String count;
        String cart="Add to cart";
        Scanner myReader = new Scanner(data);
        while (myReader.hasNextLine()) {
            imgName = myReader.nextLine();
            name = myReader.nextLine();
            description = myReader.nextLine();
            price = myReader.nextLine();
            Icon icon = new ImageIcon(path+"\\"+imgName);
//            SPAApplication.getInstance().getItemsWarehouse().addItem();
            Object[] object = {icon, name, description, price, cart, "Delete"};
            model.addRow(object);
        }
        myReader.close();
    }
}

