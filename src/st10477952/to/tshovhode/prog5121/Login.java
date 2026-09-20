package st10477952.to.tshovhode.prog5121;

public class Login {

    String registeredUser;
    String registeredPass;
    String registeredCell;
    String fName;
    String lName;

    // Validates the username format
    public boolean checkUserName(String user) {
        boolean hasUnderscore = user.contains("_");
        boolean shortEnough = user.length() <= 5;
        return hasUnderscore && shortEnough;
    }

    // Validates the password rules without regex
    public boolean checkPasswordComplexity(String pass) {
        if (pass.length() < 8) {
            return false;
        }

        boolean capitalPresent = false;
        boolean digitPresent = false;
        boolean specialPresent = false;

        for (int pos = 0; pos < pass.length(); pos++) {
            char letter = pass.charAt(pos);

            if (Character.isUpperCase(letter)) {
                capitalPresent = true;
            } else if (Character.isDigit(letter)) {
                digitPresent = true;
            } else if (!Character.isLetterOrDigit(letter)) {
                specialPresent = true;
            }
        }

        return capitalPresent && digitPresent && specialPresent;
    }

    /*
     * South African cell phone check inspired by a Stack Overflow regex answer.
     * Matches +27 followed by 9 digits.
     * Reference:
     * https://stackoverflow.com/questions/28131029/regex-for-south-african-phone-numbers
     * Author: Wiktor Stribiżew
     * Accessed: 2026
     */
    public boolean checkCellPhoneNumber(String cell) {
        return cell.matches("^\\+27[0-9]{9}$");
    }

    // Registers the user with validation messages
    public String registerUser(String user, String pass, String cell,
                               String first, String last) {

        if (!checkUserName(user)) {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(pass)) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, "
                    + "and a special character.";
        }

        if (!checkCellPhoneNumber(cell)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        registeredUser = user;
        registeredPass = pass;
        registeredCell = cell;
        fName = first;
        lName = last;

        return "Username successfully captured.\n"
                + "Password successfully captured.\n"
                + "Cell phone number successfully added.";
    }

    // Authenticates the user
    public boolean loginUser(String user, String pass) {
        if (registeredUser == null || registeredPass == null) {
            return false;
        }
        return registeredUser.equals(user) && registeredPass.equals(pass);
    }

    // Returns the login message
    public String returnLoginStatus(boolean status) {
        if (status) {
            return "Welcome " + fName + ", " + lName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}