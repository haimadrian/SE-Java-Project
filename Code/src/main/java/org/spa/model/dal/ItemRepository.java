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
   private static final String FILE_NAME = "Items.json";
   private static final File DIR = new File(new File(SPAApplication.getWorkingDirectory(), "Repository"), "Items");
   private static final Logger logger = LoggerFactory.getLogger(ItemRepository.class);
   private final Map<String, Item> items = new HashMap<>();

   public ItemRepository() {
      DIR.mkdirs();
   }

   @Override
   public List<? extends Item> selectAll() {
      if (items.isEmpty()) {
         for (File currFile : DIR.listFiles((dir, name) -> name.endsWith("json"))) {
            logger.info("Reading items from file: " + currFile);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(currFile))))) {
               ItemsList itemsFromFile = JsonUtils.readValue(reader, ItemsList.class);
               if (itemsFromFile != null) {
                  itemsFromFile.getItems().forEach(item -> items.put(item.getId(), item));
                  logger.info(itemsFromFile.getItems().size() + " items have been read");
               }
            } catch (Exception e) {
               logger.error("Error has occurred while reading items from file", e);
            }
         }
      }

      return new ArrayList<>(items.values());
   }

   @Override
   public Item create(Item item) {
      ItemImpl value = new ItemImpl(item);
      items.put(item.getId(), value);
      return value;
   }

   @Override
   public Item update(Item item) {
      ItemImpl value = new ItemImpl(item);
      items.put(item.getId(), value);
      return value;
   }

   @Override
   public Item delete(Item item) {
      return items.remove(item.getId());
   }

   @Override
   public void saveAll(Iterable<? extends Item> items) {
      items.forEach(this::update);

      if (!this.items.isEmpty()) {
         logger.info("Saving " + this.items.size() + " items to file");
         File outFile = new File(DIR, FILE_NAME);
         try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(outFile))))) {
            JsonUtils.writeValue(writer, new ItemsList(new ArrayList<>(this.items.values())));
            logger.info("Items saved");
         } catch (Exception e) {
            logger.error("Error has occurred while writing items to file", e);
         }
      }
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
