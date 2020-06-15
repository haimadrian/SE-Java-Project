package org.spa.controller.user;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spa.BaseTest;
import org.spa.controller.SPAApplication;
import org.spa.model.user.Admin;
import org.spa.model.user.Customer;
import org.spa.model.user.SystemAdmin;
import java.util.Date;

public class UserManagementServiceTest extends BaseTest {
    private UserManagementService userManagementService;
    private Customer customer;
    private Admin admin;
    private SystemAdmin sysAdmin;

    @Before
    public void init() {
        userManagementService = SPAApplication.getInstance().getUserManagementService();
        customer = new Customer("Rony", "123", "054",
                new Date("1995" + "/" + "05" + "/" + "20"),
                new Date(System.currentTimeMillis()), "What is your favourite color ?", "red");
        admin = new Admin("Tomer", "1", "0534564",
                new Date("1995" + "/" + "05" + "/" + "29"),
                new Date(System.currentTimeMillis()), "What is your favourite color ?", "blue", 15000, 100);
        sysAdmin = new SystemAdmin("King", "0");
        userManagementService.createUser(customer);
        userManagementService.createUser(admin);
        userManagementService.createUser(sysAdmin);
    }

    @After
    public void cleanup() {
        userManagementService.deleteUser(customer);
        userManagementService.deleteUser(admin);
        userManagementService.deleteUser(sysAdmin);
    }

    @Test
    public void TestLogin_UserIdNotExist_LoginReturnNull() {
        assertNull("User with wrong userId trying to Login,should return null", userManagementService.login("Ronyy", customer.getPassword()));
    }

    @Test
    public void TestLogin_UserIdExistWithWrongPassword_LoginReturnNull() {
        assertNull("User with wrong password trying to Login,should return null", userManagementService.login(customer.getUserId(), "123456"));
    }

    @Test
    public void TestLogin_CustomerUserIdExistWithMatchedPassword_LoginReturnLoggedInCustomer() {
        assertNotNull("Customer with matched id and pass trying to Login,should not return null", userManagementService.login(customer.getUserId(), customer.getPassword()));
        assertEquals("LoggedIn user type returned should be Customer", customer.getUserType(), UserType.Customer);
    }

    @Test
    public void TestLogin_AdminUserIdExistWithMatchedPassword_LoginReturnLoggedInAdmin() {
        assertNotNull("Admin with matched id and pass trying to Login,should not return null", userManagementService.login(admin.getUserId(), admin.getPassword()));
        assertEquals("loggedIn user type returned should be Admin", admin.getUserType(), UserType.Admin);
    }

    @Test
    public void TestLogin_SysAdminUserIdExistWithMatchedPassword_LoginReturnLoggedInSysAdmin() {
        assertNotNull("System Admin with matched id and pass trying to Login,should not return null", userManagementService.login(sysAdmin.getUserId(), sysAdmin.getKey()));
        assertEquals("LoggedIn user type returned should be SysAdmin", sysAdmin.getUserType(), UserType.SysAdmin);
    }

    @Test
    public void TestRegistration_UserAlreadyExist_IsExistReturnTrue() {
        assertTrue("On Registration if username already exist, create user should fail and isExist should return true", userManagementService.isExist(customer.getUserId()));
        assertFalse("On Registration if username does not exist, create user should work fine and isExist should return false", userManagementService.isExist("AAA"));
    }

    @Test
    public void TestRegistration_GuestRegistrationWithExistingUserName_ShouldReturnFalse() {
        assertFalse("On Registration if username already exist, register should return false", userManagementService.register(customer.getUserId(), customer.getPassword(), customer.getPhoneNumber(),
                customer.getBirthDay(), customer.getRegistrationDate(), customer.getSecretQuestion(), customer.getSecretAnswer(), UserType.Guest, null));
    }

    @Test
    public void TestRegistration_GuestRegistrationWithMissingField_ShouldReturnFalse() {
        assertFalse("On Guest Registration if any field is empty, register should return false", userManagementService.register("Lior", customer.getPassword(), customer.getPhoneNumber(),
                customer.getBirthDay(), customer.getRegistrationDate(), customer.getSecretQuestion(), "", UserType.Guest, null));
    }

    @Test
    public void TestRegistration_SysAdminRegistrationWithMissingField_ShouldReturnFalse() {
        assertFalse("On SysAdmin Registration if any field is empty, register should return false", userManagementService.register("Haim", customer.getPassword(), customer.getPhoneNumber(),
                customer.getBirthDay(), customer.getRegistrationDate(), customer.getSecretQuestion(), "", UserType.Guest, null));
    }

    @Test
    public void TestRegistration_GuestRegistration_ShouldReturnTrue() {
        assertTrue("On Guest Registration, register should return true", userManagementService.register("Lior", customer.getPassword(), customer.getPhoneNumber(),
                customer.getBirthDay(), customer.getRegistrationDate(), customer.getSecretQuestion(), customer.getSecretAnswer(), UserType.Guest, null));
        Customer dummy = new Customer("Lior", customer.getPassword(), customer.getPhoneNumber(),
                customer.getBirthDay(), customer.getRegistrationDate(), customer.getSecretQuestion(), customer.getSecretAnswer());
        userManagementService.deleteUser(dummy);
    }

    @Test
    public void TestRegistration_SysAdminRegistration() {
        assertTrue("On SysAdmin Registration, register should return true", userManagementService.register("Moshe", "123", "054",
                customer.getBirthDay(), customer.getRegistrationDate(), customer.getSecretQuestion(), "blabla", UserType.SysAdmin, "Admin"));
        Customer dummy = new Customer("Moshe", customer.getPassword(), customer.getPhoneNumber(),
                customer.getBirthDay(), customer.getRegistrationDate(), customer.getSecretQuestion(), customer.getSecretAnswer());
        userManagementService.deleteUser(dummy);
    }

    @Test
    public void TestForgotPasswordDisplayQuestion() {
        assertEquals("On Forgot Password when userId inserted, check if the User's question is displayed", userManagementService.forgotPasswordDisplayQuestion(customer.getUserId()), "What is your favourite color ?");
    }

    @Test
    public void TestForgotPasswordCheckAnswer() {
        assertTrue("On forgot password, if it's the right secret answer, forgotPasswordCheckAnswer return true", userManagementService.forgotPasswordCheckAnswer(customer.getUserId(), customer.getSecretAnswer()));
        assertFalse("On forgot password, if it's a wrong secret answer, forgotPasswordCheckAnswer return false", userManagementService.forgotPasswordCheckAnswer(customer.getUserId(), "Gold"));
    }

    @Test
    public void TestResetPassword() {
        assertTrue("On reset password, if password equals the reEnter password, return true", userManagementService.resetPassword("123","123",customer.getUserId()));
        assertFalse("On reset password, if there is an empty field, return false",userManagementService.resetPassword("","123",customer.getUserId()));
    }

    @Test
    public void TestResetPassword_CompareOldToNewPassword() {
        String oldPassword = customer.getPassword();
        userManagementService.resetPassword("000","000",customer.getUserId());
        assertEquals("Compare old password to new, should not be the same", oldPassword, customer.getPassword());
    }
}
