package st10477952.to.tshovhode.prog5121;

import java.util.Scanner;


public class ST10477952TOTSHOVHODEPROG5121 {


    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        Login login = new Login();

        System.out.println("=== Registration ===");

        System.out.print("Enter first name: ");
        String first = reader.nextLine();

        System.out.print("Enter last name: ");
        String last = reader.nextLine();

        System.out.print("Enter username: ");
        String user = reader.nextLine();

        if (login.checkUserName(user)) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.");
        }

        System.out.print("Enter password: ");
        String pass = reader.nextLine();

        if (login.checkPasswordComplexity(pass)) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, "
                    + "and a special character.");
        }

        System.out.print("Enter cell phone number (e.g. +27831234567): ");
        String cell = reader.nextLine();

        if (login.checkCellPhoneNumber(cell)) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }

        String message = login.registerUser(user, pass, cell, first, last);
        System.out.println(message);

        System.out.println("\n=== Login ===");

        System.out.print("Enter username: ");
        String userTry = reader.nextLine();

        System.out.print("Enter password: ");
        String passTry = reader.nextLine();

        boolean loggedIn = login.loginUser(userTry, passTry);
        System.out.println(login.returnLoginStatus(loggedIn));

    }
    
}
