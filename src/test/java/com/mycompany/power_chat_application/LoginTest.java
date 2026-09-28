/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.power_chat_application;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the Login class
 */
class LoginTest {

      private final Login login = new Login();


    // ---------- checkUserName() ----------

    @Test
    void testCheckUserName_correctlyFormatted_returnsTrue() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void testCheckUserName_incorrectlyFormatted_returnsFalse() {
        assertFalse(login.checkUserName("kyle!!!!!!"));
    }

    // ---------- checkPasswordComplexity() ----------

    @Test
    void testCheckPasswordComplexity_meetsRequirements_returnsTrue() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void testCheckPasswordComplexity_doesNotMeetRequirements_returnsFalse() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    // ---------- checkCellPhoneNumber() ----------

    @Test
    void testCheckCellPhoneNumber_correctlyFormatted_returnsTrue() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void testCheckCellPhoneNumber_incorrectlyFormatted_returnsFalse() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    // ---------- registerUser() ----------

    @Test
    void testRegisterUser_allFieldsValid_returnsSuccessMessage() {
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals("Username successfully captured. Password successfully captured. "
                + "Cell phone number successfully added. Registration successful!", result);
    }

    @Test
    void testRegisterUser_invalidUsername_returnsUsernameErrorMessage() {
        String result = login.registerUser("kyle!!!!!!", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals("Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.", result);
    }

    @Test
    void testRegisterUser_invalidPassword_returnsPasswordErrorMessage() {
        String result = login.registerUser("kyl_1", "password", "+27838968976", "Kyle", "Smith");
        assertEquals("Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, and a special character.", result);
    }

    @Test
    void testRegisterUser_invalidCellPhone_returnsCellPhoneErrorMessage() {
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "08966553", "Kyle", "Smith");
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.", result);
    }

    // ---------- loginUser() ----------

    @Test
    void testLoginUser_correctCredentials_returnsTrue() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testLoginUser_incorrectCredentials_returnsFalse() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertFalse(login.loginUser("kyl_1", "wrongPassword1!"));
    }

    // ---------- returnLoginStatus() ----------

    @Test
    void testReturnLoginStatus_successfulLogin_returnsWelcomeMessage() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals("Welcome Kyle, Smith it is great to see you.",
                login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testReturnLoginStatus_failedLogin_returnsErrorMessage() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus("kyl_1", "wrongPassword1!"));
    }
}