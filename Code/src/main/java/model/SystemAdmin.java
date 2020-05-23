package model;

public class SystemAdmin extends AbstractUser implements User{

    private String key;

    public SystemAdmin(String key ) {
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
    public String getBirthDay() {
        return "";
    }

    @Override
    public String getRegistrationDate() {
        return "";
    }
}
