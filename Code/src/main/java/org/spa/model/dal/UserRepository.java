package org.spa.model.dal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.spa.controller.SPAApplication;
import org.spa.controller.user.User;
import org.spa.controller.util.JsonUtils;
import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;
import org.spa.model.Repository;
import org.spa.model.user.Admin;
import org.spa.model.user.Customer;
import org.spa.model.user.SystemAdmin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class UserRepository implements Repository<User> {
   private static final File CUSTOMER_FILE = new File(new File(SPAApplication.getWorkingDirectory(), "Repository"), "Customers.json");
   private static final File ADMIN_FILE = new File(new File(SPAApplication.getWorkingDirectory(), "Repository"), "Admins.json");
   private static final File SYSADMIN_FILE = new File(new File(SPAApplication.getWorkingDirectory(), "Repository"), "SysAdmins.json");
   private static final Logger logger = LoggerFactory.getLogger(OrderRepository.class);

   private final Map<String, User> users = new HashMap<>();

   public UserRepository() {
      CUSTOMER_FILE.getParentFile().mkdirs();
   }

   @Override
   public List<? extends User> selectAll() {
      if (users.isEmpty()) {
         readUsersFromFile(CUSTOMER_FILE, CustomerList.class);
         readUsersFromFile(ADMIN_FILE, AdminList.class);
         readUsersFromFile(SYSADMIN_FILE, SysAdminList.class);

         createSysAdminIfMissing();

         if (!users.isEmpty()) {
            logger.info(users.size() + " users have been read");
         }
      }

      return new ArrayList<>(users.values());
   }

   private void createSysAdminIfMissing() {
      if (users.values().stream().noneMatch(SystemAdmin.class::isInstance)) {
         users.put("Idan", new SystemAdmin("Idan", "1234"));
         users.put("Lior", new SystemAdmin("Lior", "1234"));
         users.put("Haim", new SystemAdmin("Haim", "1234"));
      }
   }

   private <T extends UserList<? extends User>> void readUsersFromFile(File currFile, Class<T> customerListClass) {
      if (currFile.exists()) {
         logger.info("Reading users from file: " + currFile);
         try (BufferedReader reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(currFile))))) {
            T usersFromFile = JsonUtils.readValue(reader, customerListClass);
            if (usersFromFile != null) {
               usersFromFile.getUsers().forEach(user -> users.put(user.getUserId(), user));
            }
         } catch (Exception e) {
            logger.error("Error has occurred while reading users from file: " + currFile, e);
         }
      } else {
         logger.info("Users file does not exist. Nothing to read: " + currFile);
      }
   }

   @Override
   public User create(User user) {
      if (user instanceof Admin) {
         Admin admin = (Admin) user;
         users.put(admin.getUserId(), new Admin(admin.getUserId(), admin.getPassword(),
               admin.getPhoneNumber(), admin.getBirthDay(), admin.getRegistrationDate(),
               admin.getSecretQuestion(), admin.getSecretAnswer(), admin.getSalary(), admin.getPositionPercentage()));
      } else if (user instanceof Customer) {
         Customer customer = (Customer) user;
         users.put(customer.getUserId(), new Customer(customer.getUserId(), customer.getPassword(),
               customer.getPhoneNumber(), customer.getBirthDay(), customer.getRegistrationDate(),
               customer.getSecretQuestion(), customer.getSecretAnswer()));
      } else if (user instanceof SystemAdmin) {
         users.put(user.getUserId(), new SystemAdmin(user.getUserId(), ((SystemAdmin) user).getKey()));
      }

      return user;
   }

   @Override
   public User update(User item) {
      users.put(item.getUserId(), item);
      return item;
   }

   @Override
   public User delete(User item) {
      return users.remove(item.getUserId());
   }

   @Override
   public void saveAll(Iterable<? extends User> users) {
      users.forEach(this::update);

      ArrayList<Admin> admins = new ArrayList<>();
      ArrayList<Customer> customers = new ArrayList<>();
      ArrayList<SystemAdmin> sysadmins = new ArrayList<>();

      if (!this.users.isEmpty()) {
         logger.info("Saving " + this.users.size() + " users to file");

         for (User user : this.users.values()) {
            if (user instanceof Admin) {
               admins.add((Admin) user);
            } else if (user instanceof Customer) {
               customers.add((Customer) user);
            } else if (user instanceof SystemAdmin) {
               sysadmins.add((SystemAdmin) user);
            }
         }

         if (!admins.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(ADMIN_FILE))))) {
               JsonUtils.writeValue(writer, new AdminList(admins));
            } catch (Exception e) {
               logger.error("Error has occurred while writing admins to file", e);
            }
         }

         if (!customers.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(CUSTOMER_FILE))))) {
               JsonUtils.writeValue(writer, new CustomerList(customers));
            } catch (Exception e) {
               logger.error("Error has occurred while writing customers to file", e);
            }
         }

         if (!sysadmins.isEmpty()) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(SYSADMIN_FILE))))) {
               JsonUtils.writeValue(writer, new SysAdminList(sysadmins));
            } catch (Exception e) {
               logger.error("Error has occurred while writing sys-admins to file", e);
            }
         }

         logger.info("Users saved");
      }
   }

   public interface UserList<T> {
      List<T> getUsers();
   }

   public static class CustomerList implements UserList<Customer> {
      @JsonProperty
      private ArrayList<Customer> customers;

      public CustomerList() {
         customers = new ArrayList<>();
      }

      @JsonCreator
      public CustomerList(@JsonProperty(value = "customers") ArrayList<Customer> customers) {
         this.customers = customers;
      }

      @Override
      public List<Customer> getUsers() {
         return customers;
      }
   }

   public static class AdminList implements UserList<Admin> {
      @JsonProperty
      private ArrayList<Admin> admins;

      public AdminList() {
         admins = new ArrayList<>();
      }

      @JsonCreator
      public AdminList(@JsonProperty(value = "admins") ArrayList<Admin> admins) {
         this.admins = admins;
      }

      @Override
      public List<Admin> getUsers() {
         return admins;
      }
   }

   public static class SysAdminList implements UserList<SystemAdmin> {
      @JsonProperty
      private ArrayList<SystemAdmin> sysadmins;

      public SysAdminList() {
         sysadmins = new ArrayList<>();
      }

      @JsonCreator
      public SysAdminList(@JsonProperty(value = "sysadmins") ArrayList<SystemAdmin> sysadmins) {
         this.sysadmins = sysadmins;
      }

      @Override
      public List<SystemAdmin> getUsers() {
         return sysadmins;
      }
   }
}
