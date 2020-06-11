package org.spa.ui.item;

import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.controller.action.ActionException;
import org.spa.controller.action.ActionManager;
import org.spa.controller.action.ActionType;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.controller.item.WarehouseItem;
import org.spa.ui.util.Controls;
import org.spa.ui.util.Fonts;
import org.spa.ui.util.ImagesCache;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static javax.swing.JOptionPane.showMessageDialog;
import static org.spa.main.SPAMain.FRAME_ICON_NAME;
import static org.spa.ui.HomePage.MAGNIFYING_IMAGE;

public class ItemManagement extends JFrame implements ActionListener {
   private static final Logger logger = LoggerFactory.getLogger(ItemManagement.class);

   private WarehouseItem warehouseItem;
   private ItemsWarehouse itemsWarehouse;
   private String itemId;
   private Map<String, Object> params;
   private int y;
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
   private JLabel id;
   private JLabel idValue;

   public ItemManagement() {
      this(null);
   }

   public ItemManagement(WarehouseItem itemSelected) {
      warehouseItem = itemSelected;
      itemsWarehouse = SPAApplication.getInstance().getItemsWarehouse();
      itemId = "";
      params = new HashMap<>();

      if (itemSelected != null) {
         y = 120;
      } else {
         y = 75;
      }

      setTitle("Item Management");
      setSize(500, 650);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      setResizable(false);
      setIconImage(ImagesCache.getInstance().getImage(FRAME_ICON_NAME).getImage());

      container = getContentPane();
      container.setLayout(null);

      int textX = 160;
      int textWidth = 300;
      int textHeight = 30;
      int labelX = 40;
      int labelWidth = 115;

      title = new JLabel("Item Management");
      title.setFont(Fonts.HEADING_FONT);
      title.setSize(textWidth, 40);
      title.setLocation(130, 15);
      container.add(title);

      name = new JLabel("Name");
      name.setFont(Fonts.PANEL_HEADING_FONT);
      name.setSize(labelWidth, textHeight);
      name.setLocation(labelX, y);
      container.add(name);

      textName = new JTextField();
      textName.setFont(Fonts.PLAIN_FONT);
      textName.setSize(textWidth, textHeight);
      textName.setLocation(textX, y);
      container.add(textName);

      category = new JLabel("Category");
      category.setFont(Fonts.PANEL_HEADING_FONT);
      category.setSize(labelWidth, textHeight);
      category.setLocation(labelX, y + 50);
      container.add(category);

      textCategory = new JTextField();
      textCategory.setFont(Fonts.PLAIN_FONT);
      textCategory.setSize(textWidth, textHeight);
      textCategory.setLocation(textX, y + 50);
      container.add(textCategory);

      description = new JLabel("Description");
      description.setFont(Fonts.PANEL_HEADING_FONT);
      description.setSize(labelWidth, textHeight);
      description.setLocation(labelX, y + 100);
      container.add(description);

      textDescription = new JTextArea();
      textDescription.setFont(Fonts.PLAIN_FONT);
      textDescription.setLineWrap(true);
      JScrollPane scrollPane = new JScrollPane(textDescription);
      scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scrollPane.setSize(textWidth, 125);
      scrollPane.setLocation(textX, y + 100);
      container.add(scrollPane);

      price = new JLabel("Price");
      price.setFont(Fonts.PANEL_HEADING_FONT);
      price.setSize(labelWidth, textHeight);
      price.setLocation(labelX, y + 250);
      container.add(price);

      textPrice = new JTextField();
      textPrice.setFont(Fonts.PLAIN_FONT);
      textPrice.setSize(textWidth, textHeight);
      textPrice.setLocation(textX, y + 250);
      container.add(textPrice);

      profit = new JLabel("Profit%");
      profit.setFont(Fonts.PANEL_HEADING_FONT);
      profit.setSize(labelWidth, textHeight);
      profit.setLocation(labelX, y + 300);
      container.add(profit);

      textProfit = new JTextField();
      textProfit.setFont(Fonts.PLAIN_FONT);
      textProfit.setSize(textWidth, textHeight);
      textProfit.setLocation(textX, y + 300);
      container.add(textProfit);

      discount = new JLabel("Discount%");
      discount.setFont(Fonts.PANEL_HEADING_FONT);
      discount.setSize(labelWidth, textHeight);
      discount.setLocation(labelX, y + 350);
      container.add(discount);

      textDiscount = new JTextField();
      textDiscount.setFont(Fonts.PLAIN_FONT);
      textDiscount.setSize(textWidth, textHeight);
      textDiscount.setLocation(textX, y + 350);
      container.add(textDiscount);

      count = new JLabel("Count");
      count.setFont(Fonts.PANEL_HEADING_FONT);
      count.setSize(labelWidth, textHeight);
      count.setLocation(labelX, y + 400);
      container.add(count);

      textCount = new JTextField();
      textCount.setFont(Fonts.PLAIN_FONT);
      textCount.setSize(textWidth, textHeight);
      textCount.setLocation(textX, y + 400);
      container.add(textCount);

      image = new JLabel("Image");
      image.setFont(Fonts.PANEL_HEADING_FONT);
      image.setSize(labelWidth, textHeight);
      image.setLocation(labelX, y + 450);
      container.add(image);

      textImage = new JTextField();
      textImage.setFont(Fonts.PLAIN_FONT);
      textImage.setSize(textWidth, textHeight);
      textImage.setLocation(textX, y + 450);

      add = new JButton("Add");
      add.setBackground(Controls.acceptButtonColor);
      add.setFont(Fonts.PLAIN_FONT);
      add.setSize(100, 30);
      add.setLocation(200, y + 495);
      add.addActionListener(this);
      container.add(add);

      Image scaledImage = MAGNIFYING_IMAGE.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
      searchBtn = new JButton(new ImageIcon(scaledImage));
      searchBtn.setFont(Fonts.PLAIN_FONT);
      searchBtn.setSize(30, textHeight);
      searchBtn.setLocation(400, y + 450);
      searchBtn.addActionListener(this);
      searchBtn.setToolTipText("Open file");

      scaledImage = ImagesCache.getInstance().getImage("web-icon.png").getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
      imageBtn = new JButton(new ImageIcon(scaledImage));
      imageBtn.setFont(Fonts.PLAIN_FONT);
      imageBtn.setSize(30, textHeight);
      imageBtn.setLocation(430, y + 450);
      imageBtn.addActionListener(this);
      imageBtn.setToolTipText("Open browser");
      Controls.setFlatStyle(searchBtn, false);
      Controls.setFlatStyle(imageBtn, false);
      container.add(searchBtn);
      container.add(imageBtn);
      container.add(textImage);

      output = new JLabel("");
      output.setFont(Fonts.PANEL_HEADING_FONT);
      output.setSize(500, 20);
      output.setLocation(60, 50);
      container.add(output);

      id = new JLabel("");
      id.setFont(Fonts.PANEL_HEADING_FONT);
      id.setSize(500, 20);
      id.setLocation(labelX, 85);
      container.add(id);

      idValue = new JLabel("");
      idValue.setFont(Fonts.PANEL_HEADING_FONT);
      idValue.setSize(500, 20);
      idValue.setLocation(textX, 85);
      container.add(idValue);

      Controls.centerDialog(this);
      setVisible(true);

      if (itemSelected != null) {
         id.setText("ID");
         idValue.setText(itemSelected.getId());
         textName.setText(itemSelected.getName());
         textName.setCaretPosition(0);
         textCategory.setText(itemSelected.getCategory());
         textDescription.setText(itemSelected.getDescription());
         textDescription.setCaretPosition(0);
         textPrice.setText(String.valueOf(itemSelected.getPrice()));
         textProfit.setText(String.valueOf(itemSelected.getProfitPercent()));
         textDiscount.setText(String.valueOf(itemSelected.getDiscountPercent()));
         textCount.setText(String.valueOf(itemSelected.getCount()));

         image.setVisible(false);
         textImage.setVisible(false);
         textName.setEnabled(false);
         add.setText("Update");
         add.setLocation(200, 570);
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
         } else if (warehouseItem == null) {

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
         } else if (warehouseItem != null) {
            String convertedId = "";
            convertedId = idValue.getText();
            WarehouseItem warehouseItem = new WarehouseItem(convertedId,
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
      } else if (e.getSource() == searchBtn) {
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
      } else if (e.getSource() == imageBtn) {
         String url = "https://www.google.com/";
         try {
            url += "search?q=" + URLEncoder.encode(textName.getText(), StandardCharsets.UTF_8.name()) + "&tbm=isch";
         } catch (UnsupportedEncodingException e1) {
            url += "imghp";
            logger.warn("Error has occurred while trying to build a google images search query", e1);
         }

         try {
            Desktop.getDesktop().browse(new URI(url));
         } catch (Exception err) {
            logger.error("Error has occurred while trying to open browser", err);
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
