package org.spa.model.user;

import org.spa.common.User;

import java.util.Date;

public class SystemAdmin extends AbstractUser implements User {

    private String key;

    public SystemAdmin(String userId, String key ) {
        this.userId = userId;
        this.key = key;
    }

    public String getKey() {
        return key;
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
}
