package org.spa.model.dal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.spa.controller.SPAApplication;
import org.spa.controller.item.Item;
import org.spa.controller.util.JsonUtils;
import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;
import org.spa.model.ItemImpl;
import org.spa.model.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Lior Shor, Idan Pollak
 * @since 16-May-20
 */
public class ItemRepository implements Repository<Item> {
   private static final File FILE = new File(new File(SPAApplication.getWorkingDirectory(), "Repository"), "Items.json");
   private static final Logger logger = LoggerFactory.getLogger(ItemRepository.class);
   private final Map<String, Item> items = new HashMap<>();

   public ItemRepository() {
      FILE.getParentFile().mkdirs();
   }

   @Override
   public List<? extends Item> selectAll() {
      if (items.isEmpty()) {
         if (FILE.exists()) {
            logger.info("Reading items from file");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(FILE))))) {
               ItemsList itemsFromFile = JsonUtils.readValue(reader, ItemsList.class);
               if (itemsFromFile != null) {
                  itemsFromFile.getItems().forEach(item -> items.put(item.getId(), item));
                  logger.info(items.size() + " items have been read");
               }
            } catch (Exception e) {
               logger.error("Error has occurred while reading items from file", e);
            }
         } else {
            logger.info("Items file does not exist. Nothing to read");
         }

         if (items.isEmpty()) {
            // If there is no order, generate some to play with
            generateDummyData();
         }
      }

      return new ArrayList<>(items.values());
   }

   @Override
   public Item create(Item item) {
      items.put(item.getId(), new ItemImpl(item));
      return item;
   }

   @Override
   public Item update(Item item) {
      items.put(item.getId(), new ItemImpl(item));
      return item;
   }

   @Override
   public Item delete(Item item) {
      return items.remove(item.getId());
   }

   @Override
   public void saveAll(Iterable<? extends Item> items) {
      items.forEach(this::update);

      // Remove the dummy values so we will not store them to disk.
      this.items.remove("1");
      this.items.remove("2");

      if (!this.items.isEmpty()) {
         logger.info("Saving " + this.items.size() + " items to file");
         try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(FILE))))) {
            JsonUtils.writeValue(writer, new ItemsList(new ArrayList<>(this.items.values())));
            logger.info("Items saved");
         } catch (Exception e) {
            logger.error("Error has occurred while writing items to file", e);
         }
      }
   }

   private void generateDummyData() {
      int idCounter = 1;
      ItemImpl dummy = new ItemImpl("" + idCounter++, "CPU",
            "Intel Core i9-9900K Coffee Lake 8-Core, 16-Thread, 95W BX80684I99900K Desktop Processor",
            "9th Gen Intel Processor\n" +
                  "Intel UHD Graphics 630\n" +
                  "Only Compatible with Intel 300 Series Motherboard\n" +
                  "Socket LGA 1151 (300 Series)\n" +
                  "3.6 GHz Max Turbo Frequency 5.0 GHz\n" +
                  "Unlocked Processor\n" +
                  "DDR4 Support\n" +
                  "Intel Optane Memory and SSD Supported\n" +
                  "Cooling device not included - Processor Only\n" +
                  "Intel Turbo Boost Technology 2.0 and Intel vPro technology offer pro-level performance for gaming, creating, and overall productivity",
            600,
            12,
            0,
            1);

      ItemImpl dummy2 = new ItemImpl("" + idCounter++, "SSD",
            "Crucial MX500 2.5\" 1TB SATA III 3D NAND Internal Solid State Drive (SSD) CT1000MX500SSD1",
            "Sequential reads/writes up to 560/510 MB/s and random reads/writes up to 95k/90k on all file types\n" +
                  "Accelerated by Micron 3D NAND technology\n" +
                  "Integrated Power Loss Immunity preserves all your saved work",
            114.9,
            17,
            2,
            1);

      items.put(dummy.getId(), dummy);
      items.put(dummy2.getId(), dummy2);
   }

   public static class ItemsList {
      @JsonProperty
      private ArrayList<? extends Item> items;

      public ItemsList() {
         items = new ArrayList<>();
      }

      @JsonCreator
      public ItemsList(@JsonProperty(value = "items") ArrayList<? extends Item> items) {
         this.items = items;
      }

      public List<? extends Item> getItems() {
         return items;
      }
   }
}
