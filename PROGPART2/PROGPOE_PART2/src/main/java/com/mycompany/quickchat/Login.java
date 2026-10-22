package com.mycompany.quickchat;

/**
 * Handles registering a new user and logging them in.
 */
public class Login {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellNumber;

    // Username must contain an underscore and be 5 characters or less
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // Password needs 8+ characters, a capital letter, a number and a special character
    public boolean checkPasswordComplexity(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetter(c)) {
                hasSpecial = true;
            }
        }
        return password.length() >= 8 && hasCapital && hasNumber && hasSpecial;
    }

    /*
     * Checks for the +27 code followed by 9 digits.
     * Regex adapted from: Oracle. 2021. Pattern (Java SE 17 & JDK 17).
     * [Online] Available at:
     * https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/regex/Pattern.html
     */
    public boolean checkCellPhoneNumber(String cellNumber) {
        return cellNumber.matches("^\\+27\\d{9}$");
    }

    public String usernameMessage(String username) {
        if (checkUserName(username)) {
            return "Username successfully captured.";
        }
        return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    }

    public String passwordMessage(String password) {
        if (checkPasswordComplexity(password)) {
            return "Password successfully captured.";
        }
        return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    }

    public String cellNumberMessage(String cellNumber) {
        if (checkCellPhoneNumber(cellNumber)) {
            return "Cell number successfully captured.";
        }
        return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
    }

    // Only saves the details if everything is valid
    public String registerUser(String firstName, String lastName, String username, String password, String cellNumber) {
        if (!checkUserName(username)) {
            return usernameMessage(username);
        }
        if (!checkPasswordComplexity(password)) {
            return passwordMessage(password);
        }
        if (!checkCellPhoneNumber(cellNumber)) {
            return cellNumberMessage(cellNumber);
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellNumber = cellNumber;
        return "User has been registered successfully.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return username != null
                && username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public String getCellNumber() {
        return cellNumber;
    }
}
