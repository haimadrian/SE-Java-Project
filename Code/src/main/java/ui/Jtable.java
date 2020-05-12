package ui;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
public class Jtable extends  JPanel {

    private boolean DEBUG = false;

    public Jtable() {
        super(new GridLayout(1, 0));
        JButton button = new JButton();
        Icon aboutIcon = new ImageIcon("C:\\Users\\liors\\Documents\\SE-Java-Project\\Code\\src\\main\\java\\ui\\image1.gif");
        ImageIcon copyIcon = new ImageIcon("");

        String[] columnNames = {"Picture", "Description","Testing"};
        Object[][] data =
                {
                        {aboutIcon, "About",aboutIcon},
                        {aboutIcon, "Add",aboutIcon},
                        {aboutIcon, "Copy",aboutIcon},
                };
        DefaultTableModel model = new DefaultTableModel(data, columnNames)
        {
            @Override
            //  Returning the Class of each column will allow different
            //  renderers to be used based on Class
            public Class getColumnClass(int column)
            {
                return getValueAt(0, column).getClass();
            }
        };
        JTable table = new JTable( model );
        table.setRowHeight(250);
        //table.sizeColumnsToFit(4);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane( table );
        add( scrollPane );
    }

    private static void createAndShowGUI()
    {
        JFrame frame = new JFrame("Table Icon");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Jtable());

        frame.setLocationByPlatform( true );
        frame.pack();
        frame.setVisible( true );
    }

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }

}