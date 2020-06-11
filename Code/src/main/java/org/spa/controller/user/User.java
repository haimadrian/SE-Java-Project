package org.spa.controller.user;

import java.util.Date;

public interface User {
   String getUserId();

   String getPhoneNumber();

   Date getBirthDay();

   Date getRegistrationDate();

   UserType getUserType();
}
