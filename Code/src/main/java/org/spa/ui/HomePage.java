package org.spa.ui;

import org.spa.controller.item.WarehouseItem;
import org.spa.controller.selection.SelectionModelManager;
import org.spa.ui.cart.ShoppingCartView;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class HomePage extends JPanel implements SPAExplorerIfc<WarehouseItem> {
    private JTable table;
    private TableModel tableModel;
    private JButton login;
    private JTree categoryTree;
    private JTextField searchBar;
    private DefaultTableModel model;
    private JFrame mainForm;
    private ShoppingCartView shoppingCart;


    public HomePage(JFrame parent) {
        mainForm = parent;
        shoppingCart = new ShoppingCartView(mainForm);
        login = new JButton("Login");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginView.showLoginView();
            }
        });

        categoryTree = new JTree();
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
                Object[] rowData = {barcode,icon,title,description};
                super.addRow(rowData);
            }
        };

//        tableModel = createTableModel();
        table = new JTable( model );
        searchBar = new JTextField("Search for product..."); /*RowFilterUtil.createRowFilter(table);*/
        searchBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                searchBar.setText("");
            }
        });

/*        searchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });*/

        File myObj = new File("C:\\Users\\liors\\Documents\\SE-Java-Project\\Code\\src\\main\\java\\ui\\data.txt");
        try {
            readFromFile(model,myObj);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        tableConfiguration(table);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        //add(shoppingCart.getMainContainer());
        add(shoppingCart.getNavigatingComponent());
        add(login);
        add(categoryTree);
        add(searchBar);
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        ComponentLocation(layout, this, shoppingCart.getNavigatingComponent(), login, searchBar, scrollPane, categoryTree);
//        frameComponent(frame);

        //frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - frame.getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - frame.getHeight()/2); // Put frame in the middle

    }
    private void search (KeyEvent evt)
    {
        DefaultTableModel table1 = (DefaultTableModel)table.getModel();
        String searchString =searchBar.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(table1);
        table.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter(searchString));
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
        table.setPreferredSize(new Dimension(725, 400));
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
    public void ComponentLocation(SpringLayout layout,Container contentPane,Component cart,Component login,Component searchBar,Component table,Component categoryTree)    {
        layout.putConstraint(SpringLayout.NORTH,login,40,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,login,255,SpringLayout.EAST,searchBar);
        layout.putConstraint(SpringLayout.WEST,cart,275,SpringLayout.WEST, contentPane);
        layout.putConstraint(SpringLayout.NORTH,cart,40,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.NORTH,searchBar,40,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,searchBar,450,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.WEST,table,170,SpringLayout.WEST, categoryTree);
        layout.putConstraint(SpringLayout.NORTH,table,100,SpringLayout.NORTH,contentPane);
        layout.putConstraint(SpringLayout.NORTH, categoryTree,100,SpringLayout.NORTH, contentPane);
        layout.putConstraint(SpringLayout.WEST, categoryTree,100,SpringLayout.NORTH, contentPane);
    }

    public void readFromFile(DefaultTableModel model,File data) throws FileNotFoundException {

        String url;
        String title;
        String description;
        String price;
        String cart="Add to cart";
        Scanner myReader = new Scanner(data);
        while (myReader.hasNextLine()) {
            url = myReader.nextLine();
            title = myReader.nextLine();
            description = myReader.nextLine();
            price = myReader.nextLine();
            Icon icon = new ImageIcon(url);
            Object[] object = {icon, title, description, price, cart, "Delete"};
            model.addRow(object);
        }
        myReader.close();
    }


}
