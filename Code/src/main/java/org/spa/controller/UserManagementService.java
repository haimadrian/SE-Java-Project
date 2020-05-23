package org.spa.controller;
import org.spa.model.user.Customer;
import org.spa.model.user.SystemAdmin;
import org.spa.common.User;
import org.spa.common.util.log.Logger;
import org.spa.common.util.log.factory.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class UserManagementService {

   // private User user;
    //  private dbAccess;
    private User loggedInUser;
//    private final List<User> usersList;

    private static final Logger logger = LoggerFactory.getLogger(UserManagementService.class);
    private final Map<String, User> userMap;


    public UserManagementService() {
//        this.user = user;
//        this.usersList = new ArrayList<>();
        userMap = new HashMap<>(1000); // Yeah sure...
    }

    /**
     * Call this method to read data from storage
     */
//    public void start() {
//        // Load data into memory
////        SPAApplication.getInstance().getUserRepository().selectAll().forEach(user -> userMap.put(user.getUserId(), TODO?);
//    }

    public User login(String userId, String pass) {
        User u = userMap.get(userId);
        if (u instanceof Customer) {
            if (((Customer) u).getPassword().equals(pass)) {
                this.loggedInUser = u;
                logger.info(u + " Logged in");
                return u;
            }
        } else if (u instanceof SystemAdmin) {
            this.loggedInUser = u;
            logger.info(u + " Logged in");
            return u;
        }
        return null;
    }

    public User getLoggedInUser(){
        return loggedInUser;
    }

//    public boolean isPermitted(Action ?) Todo

    // public ? get permittedFeatures Todo

    public void createUser(User user) {
        User u = userMap.get(user.getUserId());
        if (u != null) {
            logger.warn("UserId Already Exist");
        } else {
            userMap.put(user.getUserId(), user);
            logger.info("User added to Users DB: " + user);
        }
    }

    public void updateUser(User user) {
        // TODO
    }

    public User findUser(User user) {
        User u = userMap.get(user.getUserId());
        if(u == null)
            logger.warn("User does not exist");
        return u;
    }

    public void deleteUser(User user){
        this.userMap.remove(user.getUserId());
    }

    /**
     * @param id ID of the item to retrieve
     * @return The item with the specified ID, or <code>null</code> if there is no such item in warehouse
     */
    public User getUser(String userId) {
        return userMap.get(userId);
    }




}
