package org.spa.model.dal;

import org.spa.common.Repository;
import org.spa.common.SPAApplication;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.model.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Lior Shor
 * @since 16-May-20
 */
public class ItemRepository implements Repository<Item> {
   private static final File FILE = new File(new File(SPAApplication.getWorkingDirectory(), "Repository"), "data.txt");
   private static final Logger logger = LoggerFactory.getLogger(ItemRepository.class);

   @Override
   public List<Item> selectAll() {
      List<Item> items = new ArrayList<>();
      String id;
      String category;
      String name;
      String description;
      String price;
      String profitPercent;
      String discountPercent;
      String count;

      if (!FILE.exists()) {
         logger.info("Data file of items does not exist. Copying from resources");
         try {
            Files.copy(ItemRepository.class.getResourceAsStream("data.txt"), FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);
         } catch (Exception e) {
            logger.error("Error has occurred while copying data.txt from resources to working directory", e);
         }
      }

      if (FILE.exists()) {
         logger.info("Reading items from file");
         try (Scanner myReader = new Scanner(FILE)) {
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
               items.add(item);
            }

            logger.info(items.size() + " items have been read");
         } catch (Exception e) {
            logger.error("Failed reading items from file", e);
         }
      } else {
         logger.info("Items file does not exist. Nothing to read");
      }

      return items;
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
