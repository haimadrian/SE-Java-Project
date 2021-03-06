package org.spa.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.spa.controller.user.User;
import org.spa.controller.user.UserType;

import java.util.Date;

public class SystemAdmin extends AbstractUser implements User {

   @JsonProperty
   private String key;

   @JsonCreator()
   public SystemAdmin(@JsonProperty(value = "userId") String userId, @JsonProperty(value = "key") String key) {
      this.userId = userId;
      this.key = key;
   }

   public String getKey() {
      return key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   @Override
   public String getUserId() {
      return userId;
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
      return UserType.SysAdmin;
   }
}
