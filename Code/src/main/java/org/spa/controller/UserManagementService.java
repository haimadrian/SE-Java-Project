package org.spa.controller;

import org.spa.common.Repository;
import org.spa.common.User;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;
import org.spa.model.dal.UserRepository;
import org.spa.model.user.Admin;
import org.spa.model.user.Customer;
import org.spa.model.user.Guest;
import org.spa.model.user.SystemAdmin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserManagementService implements Service {

   private static final Logger logger = LoggerFactory.getLogger(UserManagementService.class);
   private final Map<String, User> userMap;
   private final Repository<User> userRepository;
   private final Set<UserManagementServiceObserver> observers;
   // private User user;
   //  private dbAccess;
   private User loggedInUser;

   public UserManagementService() {

      userMap = new HashMap<>(1000); // Yeah sure...
      userRepository = new UserRepository();
      observers = new HashSet<>();
   }

   /**
    * Call this method to read data from storage
    */
   @Override
   public void start() {
      loggedInUser = new Guest();

      // Load data into memory
      userRepository.selectAll().forEach(user -> userMap.put(user.getUserId().toLowerCase(), user));
   }

   @Override
   public void stop() {
      //save data to storage
      userRepository.saveAll(userMap.values());
   }

   public Map<String, User> getUserMap() {
      return userMap;
   }

   public User login(String userId, String pass) {
      User u = userMap.get(userId.toLowerCase());
      if (u instanceof Customer) {
         if (((Customer) u).getPassword().equals(pass)) {
            this.loggedInUser = u;
            notifyUserLogin();
            logger.info(u.getUserId() + " Logged in");
            return u;
         }
      } else if (u instanceof SystemAdmin) {
         if (((SystemAdmin) u).getKey().equals(pass)) {
            this.loggedInUser = u;
            notifyUserLogin();
            logger.info(u.getUserId() + " Logged in");
            return u;
         }
      }
      return null;
   }

   public void logout() {
      loggedInUser = new Guest();
      notifyUserLogin();
   }

   public User getLoggedInUser() {
      return loggedInUser;
   }

   public UserType getLoggedInUserType() {
      if (loggedInUser instanceof SystemAdmin)
         return UserType.SysAdmin;
      else if (loggedInUser instanceof Admin)
         return UserType.Admin;
      else if (loggedInUser instanceof Customer)
         return UserType.Customer;
      else
         return UserType.Guest;
   }

   public void createUser(User user) {
      User u = userMap.get(user.getUserId().toLowerCase());
      if (u != null) {
         logger.warn("UserId Already Exist");
      } else {
         userMap.put(user.getUserId().toLowerCase(), user);
         userRepository.create(user);
         logger.info("User added to Users DB: " + user.getUserId());
      }
   }

   public void updateUser(User user, String pass) {
      User u = userMap.get(user.getUserId().toLowerCase());
      if (u instanceof Customer) {
         ((Customer) u).setPassword(pass);
      } else if (u instanceof SystemAdmin) {
         ((SystemAdmin) u).setKey(pass);
      }

      userRepository.update(u);
   }

   public User findUser(User user) {
      User u = userMap.get(user.getUserId().toLowerCase());
      if (u == null)
         logger.warn("User does not exist");
      return u;
   }

   public boolean isExist(String str) {
      return (userMap.containsKey(str.toLowerCase()));
   }

   public void deleteUser(User user) {
      userMap.remove(user.getUserId().toLowerCase());
      userRepository.delete(user);
   }

   public User getUser(String userId) {
      return userMap.get(userId.toLowerCase());
   }

   public void registerObserver(UserManagementServiceObserver observer) {
      observers.add(observer);
   }

   public void unregisterObserver(UserManagementServiceObserver observer) {
      observers.remove(observer);
   }

   private void notifyUserLogin() {
      for (UserManagementServiceObserver observer : observers) {
         observer.userLogin(loggedInUser);
      }
   }
}
