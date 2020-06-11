package org.spa.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.spa.controller.user.User;
import org.spa.controller.user.UserType;

import java.util.Date;

public class Customer extends AbstractUser implements User {
   @JsonProperty
   private String password;

   @JsonProperty
   private String phoneNumber;

   @JsonProperty
   private Date birthDate;

   @JsonProperty
   private Date registrationDate;

   @JsonProperty
   private String secretQuestion;

   @JsonProperty
   private String secretAnswer;

   @JsonCreator
   public Customer(@JsonProperty(value = "userId") String userId,
                   @JsonProperty(value = "password") String password,
                   @JsonProperty(value = "phoneNumber") String phoneNumber,
                   @JsonProperty(value = "birthDate") Date birthDate,
                   @JsonProperty(value = "registrationDate") Date registrationDate,
                   @JsonProperty(value = "secretQuestion") String secretQuestion,
                   @JsonProperty(value = "secretAnswer") String secretAnswer) {
      this.userId = userId;
      this.password = password;
      this.phoneNumber = phoneNumber;
      this.birthDate = birthDate;
      this.registrationDate = registrationDate;
      this.secretQuestion = secretQuestion;
      this.secretAnswer = secretAnswer;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getSecretQuestion() {
      return secretQuestion;
   }

   public void setSecretQuestion(String secretQuestion) {
      this.secretQuestion = secretQuestion;
   }

   public String getSecretAnswer() {
      return secretAnswer;
   }

   public void setSecretAnswer(String secretAnswer) {
      this.secretAnswer = secretAnswer;
   }

   @Override
   public String getUserId() {
      return userId;
   }

   @Override
   public String getPhoneNumber() {
      return phoneNumber;
   }

   @Override
   public Date getBirthDay() {
      return birthDate;
   }

   @Override
   public Date getRegistrationDate() {
      return registrationDate;
   }

   @Override
   public UserType getUserType() {
      return UserType.Customer;
   }
}
