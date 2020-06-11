package org.spa.controller;

import org.spa.common.User;

public interface UserManagementServiceObserver {

   void userLogin(User loggedInUser);
}
