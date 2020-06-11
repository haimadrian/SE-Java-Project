package org.spa.model.user;

import org.spa.controller.user.User;
import org.spa.controller.user.UserType;

import java.util.Date;

public class Guest extends AbstractUser implements User {


   @Override
   public String getUserId() {
      return "";
   }

   @Override
   public String getPhoneNumber() {
      return "";
   }

   @Override
   public Date getBirthDay() {
      return new Date(System.currentTimeMillis());
   }

   @Override
   public Date getRegistrationDate() {
      return new Date(System.currentTimeMillis());
   }

   @Override
   public UserType getUserType() {
      return UserType.Guest;
   }
}
