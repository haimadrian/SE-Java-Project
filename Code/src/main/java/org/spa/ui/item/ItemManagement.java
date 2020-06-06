package org.spa.ui.item;

import org.spa.common.SPAApplication;
import org.spa.controller.action.ActionException;
import org.spa.controller.action.ActionManager;
import org.spa.controller.action.ActionType;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.ui.util.ImagesCache;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static javax.swing.JOptionPane.showMessageDialog;

public class ItemManagement extends JFrame implements ActionListener {

    private WarehouseItem warehouseItem;
    public enum actionType{Add,Update};
    actionType actionType = null;
    private ItemsWarehouse itemsWarehouse;
    private String itemId;
    private Map<String, Object> params;
    private Container container;
    private JLabel title;
    private JLabel name;
    private JTextField textName;
    private JLabel image;
    private JTextField textImage;
    private JLabel category;
    private JTextField textCategory;
    private JLabel description;
    private JTextArea textDescription;
    private JLabel price;
    private JTextField textPrice;
    private JLabel profit;
    private JTextField textProfit;
    private JLabel discount;
    private JTextField textDiscount;
    private JLabel count;
    private JTextField textCount;
    private JButton add;
    private JButton searchBtn;
    private JButton imageBtn;
    private JLabel output;

    public ItemManagement(WarehouseItem itemSelected, actionType actionType) {
        warehouseItem = itemSelected;
        this.actionType = actionType;
        itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
        itemId = "";
        params = new HashMap<>();

        setTitle("Item Management");
        setBounds(600, 200, 500, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        container = getContentPane();
        container.setLayout(null);

        title = new JLabel("Item Management");
        title.setFont(new Font("Arial", Font.PLAIN, 30));
        title.setSize(300, 30);
        title.setLocation(130, 15);
        container.add(title);

        name = new JLabel("Name");
        name.setFont(new Font("Arial", Font.PLAIN, 20));
        name.setSize(100, 25);
        name.setLocation(60, 75);
        container.add(name);

        textName = new JTextField();
        textName.setFont(new Font("Arial", Font.PLAIN, 15));
        textName.setSize(250, 25);
        textName.setLocation(160, 75);
        container.add(textName);

        category = new JLabel("Category");
        category.setFont(new Font("Arial", Font.PLAIN, 20));
        category.setSize(100, 25);
        category.setLocation(60, 125);
        container.add(category);

        textCategory = new JTextField();
        textCategory.setFont(new Font("Arial", Font.PLAIN, 15));
        textCategory.setSize(250, 25);
        textCategory.setLocation(160, 125);
        container.add(textCategory);

        description = new JLabel("Description");
        description.setFont(new Font("Arial", Font.PLAIN, 20));
        description.setSize(100, 25);
        description.setLocation(60, 175);
        container.add(description);

        textDescription = new JTextArea();
        textDescription.setFont(new Font("Arial", Font.PLAIN, 15));
        textDescription.setSize(300, 125);
        textDescription.setLocation(160, 175);
        textDescription.setLineWrap(true);
        container.add(textDescription);

        price = new JLabel("Price");
        price.setFont(new Font("Arial", Font.PLAIN, 20));
        price.setSize(100, 25);
        price.setLocation(60, 325);
        container.add(price);

        textPrice = new JTextField();
        textPrice.setFont(new Font("Arial", Font.PLAIN, 15));
        textPrice.setSize(250, 25);
        textPrice.setLocation(160, 325);
        container.add(textPrice);

        profit = new JLabel("Profit%");
        profit.setFont(new Font("Arial", Font.PLAIN, 20));
        profit.setSize(100, 25);
        profit.setLocation(60, 375);
        container.add(profit);

        textProfit = new JTextField();
        textProfit.setFont(new Font("Arial", Font.PLAIN, 15));
        textProfit.setSize(250, 25);
        textProfit.setLocation(160, 375);
        container.add(textProfit);

        discount = new JLabel("Discount%");
        discount.setFont(new Font("Arial", Font.PLAIN, 20));
        discount.setSize(100, 25);
        discount.setLocation(60, 425);
        container.add(discount);

        textDiscount = new JTextField();
        textDiscount.setFont(new Font("Arial", Font.PLAIN, 15));
        textDiscount.setSize(250, 25);
        textDiscount.setLocation(160, 425);
        container.add(textDiscount);

        count = new JLabel("Count");
        count.setFont(new Font("Arial", Font.PLAIN, 20));
        count.setSize(100, 25);
        count.setLocation(60, 475);
        container.add(count);

        textCount = new JTextField();
        textCount.setFont(new Font("Arial", Font.PLAIN, 15));
        textCount.setSize(250, 25);
        textCount.setLocation(160, 475);
        container.add(textCount);

        image = new JLabel("Image");
        image.setFont(new Font("Arial", Font.PLAIN, 20));
        image.setSize(100, 25);
        image.setLocation(60, 525);
        container.add(image);

        textImage = new JTextField();
        textImage.setFont(new Font("Arial", Font.PLAIN, 15));
        textImage.setSize(250, 25);
        textImage.setLocation(160, 525);
        container.add(textImage);

        add = new JButton("Add");
        add.setFont(new Font("Arial", Font.PLAIN, 15));
        add.setSize(100, 30);
        add.setLocation(200, 570);
        add.addActionListener(this);
        container.add(add);

        searchBtn= new JButton(ImagesCache.getInstance().getImage("Magnifying.png"));
        searchBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        searchBtn.setSize(35, 25);
        searchBtn.setLocation(415, 525);
        searchBtn.addActionListener(this);
        container.add(searchBtn);

        imageBtn = new JButton(ImagesCache.getInstance().getImage("DefaultBrowser.png"));
        imageBtn.setFont(new Font("Arial", Font.PLAIN, 15));
        imageBtn.setSize(35, 25);
        imageBtn.setLocation(455, 525);
        imageBtn.addActionListener(this);
        container.add(imageBtn);

        output = new JLabel("");
        output.setFont(new Font("Arial", Font.PLAIN, 20));
        output.setSize(500, 20);
        output.setLocation(60, 50);
        container.add(output);

        setVisible(true);

        if(actionType == actionType.Update){
            textImage.setText(itemSelected.getId());
            textName.setText(itemSelected.getName());
            textCategory.setText(itemSelected.getCategory());
            textDescription.setText(itemSelected.getDescription());
            textPrice.setText(String.valueOf(itemSelected.getPrice()));
            textProfit.setText(String.valueOf(itemSelected.getProfitPercent()));
            textDiscount.setText(String.valueOf(itemSelected.getDiscountPercent()));
            textCount.setText(String.valueOf(itemSelected.getCount()));

            textImage.setEnabled(false);
            textName.setEnabled(false);
            add.setText("Update");
            image.setText("ID");
            searchBtn.setVisible(false);
            imageBtn.setVisible(false);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        boolean isItemNameAlreadyExist = false;
        if (e.getSource() == add) {
            itemId = UUID.randomUUID().toString();
            if (areExistEmptyFields()) {
                output.setText("Please fill the empty fields");
            } else if (actionType == actionType.Add) {

                for (WarehouseItem item : itemsWarehouse.getItems()) {
                    if (textName.getText().equals(item.getName())) {
                        output.setText("Name Already Exist");
                        isItemNameAlreadyExist = true;
                        break;
                    }
                }

                if (!textImage.getText().isEmpty()) {
                    if (!isItemNameAlreadyExist) {
                        if (textImage.getText().contains("http")) {
                            try {
                                URL url = new URL(textImage.getText());
                                ImagesCache.getInstance().loadImageFromURL(textName.getText() + ".png", url);
                            } catch (MalformedURLException malformedURLException) {
                                malformedURLException.printStackTrace();
                            }
                        } else {
                            ImagesCache.getInstance().loadImageFromFile(textName.getText() + ".png", new File(textImage.getText()));
                        }
                    }
                }

                if (!isItemNameAlreadyExist) {
                    WarehouseItem warehouseItem = new WarehouseItem(itemId,
                            textCategory.getText(),
                            textName.getText(),
                            textDescription.getText(),
                            Double.parseDouble(textPrice.getText()),
                            Double.parseDouble(textProfit.getText()),
                            Double.parseDouble(textDiscount.getText()),
                            Integer.parseInt(textCount.getText()));
                    params.put("item", warehouseItem);
                    try {
                        ActionManager.executeAction(ActionType.CreateItemInWarehouse, params);
                    } catch (ActionException actionException) {
                        actionException.printStackTrace();
                    }
                    showMessageDialog(null, "Item added successfully");
                    dispose();
                }
            } else if (actionType == actionType.Update) {
                WarehouseItem warehouseItem = new WarehouseItem(textImage.getText(),
                        textCategory.getText(),
                        textName.getText(),
                        textDescription.getText(),
                        Double.parseDouble(textPrice.getText()),
                        Double.parseDouble(textProfit.getText()),
                        Double.parseDouble(textDiscount.getText()),
                        Integer.parseInt(textCount.getText()));
                params.put("item", warehouseItem);
                try {
                    ActionManager.executeAction(ActionType.UpdateItemInWarehouse, params);
                } catch (ActionException actionException) {
                    actionException.printStackTrace();
                }
                showMessageDialog(null, "Item updated successfully");
                dispose();
            }
        }

        else if (e.getSource() == searchBtn)
        {
            // create an object of JFileChooser class
            JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setDialogTitle("Select an image for item");
            fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setApproveButtonText("Select");
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files (*.PNG, *.JPG, *.JPEG, *.GIF, *.BMP)",
                    "PNG", "JPG", "JPEG", "GIF", "BMP", "WBMP"));

            // invoke the showsSaveDialog function to show the save dialog
            int r = fileChooser.showOpenDialog(null);

            // if the user selects a file
            if (r == JFileChooser.APPROVE_OPTION) {
                // set the label to the path of the selected file
                textImage.setText(fileChooser.getSelectedFile().getAbsolutePath());
                output.setText("Image selected successfully!");
            }
            // if the user cancelled the operation
            else
                output.setText("the user cancelled the operation");
        }
        else if (e.getSource() == imageBtn)
        {
            String googleImagesPath = "https://www.google.co.il/imghp?hl=iw&tab=wi&authuser=0&ogbl";
            try {
                Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + googleImagesPath);
                p.waitFor();
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }
    public boolean areExistEmptyFields() {
        return (textName.getText().isEmpty() || textCategory.getText().isEmpty()
                || textDescription.getText().isEmpty() ||
                textPrice.getText().isEmpty() || textProfit.getText().isEmpty()
                || textDiscount.getText().isEmpty() || textCount.getText().isEmpty());
    }
}
