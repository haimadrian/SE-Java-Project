package org.spa.controller.user;

import org.spa.controller.Service;
import org.spa.controller.util.log.Logger;
import org.spa.controller.util.log.factory.LoggerFactory;
import org.spa.model.Repository;
import org.spa.model.dal.UserRepository;
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
      if (u != null) {
         switch (u.getUserType()) {
            case Admin:
            case Customer: {
               if (((Customer)u).getPassword().equals(pass)) {
                  this.loggedInUser = u;
                  notifyUserLogin();
                  logger.info(u.getUserId() + " Logged in");
                  return u;
               }

               break;
            }
            case SysAdmin: {
               if (((SystemAdmin) u).getKey().equals(pass)) {
                  this.loggedInUser = u;
                  notifyUserLogin();
                  logger.info(u.getUserId() + " Logged in");
                  return u;
               }

               break;
            }
            default:
               // do Nothing
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
      return loggedInUser != null ? loggedInUser.getUserType() : UserType.Guest;
   }

   public void createUser(User user) {
      if (isExist(user.getUserId())) {
         logger.warn("UserId Already Exist");
      } else {
         User createdUser = userRepository.create(user);
         userMap.put(createdUser.getUserId().toLowerCase(), createdUser);
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
      userMap.put(user.getUserId().toLowerCase(), userRepository.update(u));
   }

   public boolean isExist(String userId) {
      return (userMap.containsKey(userId.toLowerCase()));
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
