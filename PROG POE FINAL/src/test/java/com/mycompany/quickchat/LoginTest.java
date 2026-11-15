package com.mycompany.quickchat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoginTest {

    private Login login;

    @BeforeEach
    public void setUp() {
        login = new Login();
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
    }

    // ---------- assertEquals ----------

    @Test
    public void testUsernameCorrectlyFormatted() {
        assertEquals("Username successfully captured.", login.usernameMessage("kyl_1"));
        assertEquals("Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus(login.loginUser("kyl_1", "Ch&&sec@ke99!")));
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                login.usernameMessage("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordMeetsComplexity() {
        assertEquals("Password successfully captured.", login.passwordMessage("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                login.passwordMessage("password"));
    }

    @Test
    public void testCellNumberCorrectlyFormatted() {
        assertEquals("Cell number successfully captured.", login.cellNumberMessage("+27838968976"));
    }

    @Test
    public void testCellNumberIncorrectlyFormatted() {
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
                login.cellNumberMessage("08966553"));
    }

    @Test
    public void testRegisterUserFailsOnBadUsername() {
        Login newLogin = new Login();
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                newLogin.registerUser("Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976"));
    }

    // ---------- assertTrue / assertFalse ----------

    @Test
    public void testLoginSuccessful() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        assertFalse(login.loginUser("kyl_1", "wrongPassword1!"));
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(false));
    }

    @Test
    public void testUsernameTrue() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameFalse() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void testPasswordTrue() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPasswordFalse() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testCellNumberTrue() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellNumberFalse() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}
