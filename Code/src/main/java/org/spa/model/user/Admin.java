package org.spa.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.spa.controller.user.UserType;

import java.util.Date;

public class Admin extends Customer {
   @JsonProperty
   private double salary;

   @JsonProperty
   private double positionPercentage;

   @JsonCreator
   public Admin(@JsonProperty(value = "userId") String userId,
                @JsonProperty(value = "password") String password,
                @JsonProperty(value = "phoneNumber") String phoneNumber,
                @JsonProperty(value = "birthDate") Date birthDate,
                @JsonProperty(value = "registrationDate") Date registrationDate,
                @JsonProperty(value = "secretQuestion") String secretQuestion,
                @JsonProperty(value = "secretAnswer") String secretAnswer,
                @JsonProperty(value = "salary") double salary,
                @JsonProperty(value = "positionPercentage") double positionPercentage) {
      super(userId, password, phoneNumber, birthDate, registrationDate, secretQuestion, secretAnswer);
      this.salary = salary;
      this.positionPercentage = positionPercentage;
   }

   public double getSalary() {
      return salary;
   }

   public void setSalary(double salary) {
      this.salary = salary;
   }

   public double getPositionPercentage() {
      return positionPercentage;
   }

   public void setPositionPercentage(double positionPercentage) {
      this.positionPercentage = positionPercentage;
   }

   @Override
   public UserType getUserType() {
      return UserType.Admin;
   }
}
