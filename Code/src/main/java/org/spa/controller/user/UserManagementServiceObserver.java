package org.spa.controller.user;

/**
 * Interface according to the Observer pattern to let modules get notified by
 * {@link UserManagementService} upon user logins
 * @author Idan Pollak
 */
public interface UserManagementServiceObserver {
   /**
    * This method is called when a user is logged in to the application.<br/>
    * For example we need to display the logged in user name in the home page.
    * @param loggedInUser The user that has just logged in
    */
   void onUserLoggedIn(User loggedInUser);
}
