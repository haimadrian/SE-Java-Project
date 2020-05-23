package model;

public class Customer extends AbstractUser implements User{

    private String password;
    private String phoneNumber;
    private String birthDate;
    private String registrationDate;
    private String secretQuestion;
    private String secretAnswer;

    public Customer(String password, String phoneNumber, String birthDate, String registrationDate, String secretQuestion, String secretAnswer) {
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
    public String getBirthDay() {
        return birthDate;
    }

    @Override
    public String getRegistrationDate() {
        return registrationDate;
    }
}
