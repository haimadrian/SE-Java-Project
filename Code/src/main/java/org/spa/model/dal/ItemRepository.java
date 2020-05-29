package org.spa.model.dal;

import org.spa.common.Repository;
import org.spa.common.SPAApplication;
import org.spa.controller.item.ItemsWarehouse;
import org.spa.model.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * @author Haim Adrian
 * @since 16-May-20
 */
public class ItemRepository implements Repository<Item> {
   @Override
   public List<Item> selectAll() throws FileNotFoundException {
      int idCounter = 1;
      List<Item> dummy = new ArrayList<>();
      String id;
      String category;
      String imgName;
      String name;
      String description;
      String price;
      String profitPercent;
      String discountPercent;
      String count;
      String path = new File("src\\main\\resources\\org\\spa\\ui\\homepagestuff").getAbsolutePath();
      File  read = new File(path+"\\data.txt");
      Scanner myReader = new Scanner(read);
      while (myReader.hasNextLine()) {
         id = myReader.nextLine();
         category = myReader.nextLine();
         name = myReader.nextLine();
         description = myReader.nextLine();
         price = myReader.nextLine();
         profitPercent = myReader.nextLine();
         discountPercent = myReader.nextLine();
         count = myReader.nextLine();
         Item item = new Item(id, category, name, description, Double.parseDouble(price), Double.parseDouble(profitPercent), Double.parseDouble(discountPercent), Integer.parseInt(count));
         dummy.add(item);
      }
        return dummy;
   }
   @Override
   public List<Item> select(Predicate<Item> filter) throws FileNotFoundException {
      return selectAll();
   }

   @Override
   public Item create(Item item) {
      // TODO: implement it
      return null;
   }

   @Override
   public Item update(Item item) {
      // TODO: implement it
      return item;
   }

   @Override
   public Item delete(Item item) {
      // TODO: implement it
      return item;
   }

   @Override
   public void saveAll(Iterable<Item> items) {
      // TODO: implement save
   }
}
