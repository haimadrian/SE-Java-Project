package org.spa.model.user;

import org.spa.common.User;

import java.util.Date;

public class Customer extends AbstractUser implements User {

    private String password;
    private String phoneNumber;
    private Date birthDate;
    private Date registrationDate;
    private String secretQuestion;
    private String secretAnswer;

    public Customer(String userId, String password, String phoneNumber, Date birthDate, Date registrationDate, String secretQuestion, String secretAnswer) {
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
}
