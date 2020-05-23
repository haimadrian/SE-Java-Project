package org.spa.model.user;

import java.util.Date;

public class Manager extends Customer{
    private double salary;
    private double positionPercentage;

    public Manager(String password, String phoneNumber, Date birthDate, Date registrationDate, String secretQuestion, String secretAnswer, double salary, double positionPercentage) {
        super(password, phoneNumber, birthDate, registrationDate, secretQuestion, secretAnswer);
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


}
