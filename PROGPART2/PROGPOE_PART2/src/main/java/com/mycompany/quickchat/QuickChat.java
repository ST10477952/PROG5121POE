package com.mycompany.quickchat;

import java.util.Scanner;

/**
 * Console app for QuickChat - register, login, then send messages.
 */
public class QuickChat {

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Login login = new Login();

        // ---------- Registration ----------
        System.out.println("=== Register ===");
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        String username;
        do {
            System.out.print("Enter a username: ");
            username = input.nextLine();
            System.out.println(login.usernameMessage(username));
        } while (!login.checkUserName(username));

        String password;
        do {
            System.out.print("Enter a password: ");
            password = input.nextLine();
            System.out.println(login.passwordMessage(password));
        } while (!login.checkPasswordComplexity(password));

        String cellNumber;
        do {
            System.out.print("Enter your cell number (e.g. +27831234567): ");
            cellNumber = input.nextLine();
            System.out.println(login.cellNumberMessage(cellNumber));
        } while (!login.checkCellPhoneNumber(cellNumber));

        System.out.println(login.registerUser(firstName, lastName, username, password, cellNumber));

        // ---------- Login (3 tries) ----------
        System.out.println("\n=== Login ===");
        boolean loggedIn = false;
        int attempts = 0;
        while (!loggedIn && attempts < 3) {
            System.out.print("Username: ");
            String enteredUsername = input.nextLine();
            System.out.print("Password: ");
            String enteredPassword = input.nextLine();

            loggedIn = login.loginUser(enteredUsername, enteredPassword);
            System.out.println(login.returnLoginStatus(loggedIn));
            attempts++;
        }

        if (!loggedIn) {
            System.out.println("Too many failed attempts. Goodbye.");
            return;
        }

        // ---------- QuickChat ----------
        System.out.println("\nWelcome to QuickChat.");
        int maxMessages = readNumber("How many messages would you like to send? ");
        while (maxMessages <= 0) {
            maxMessages = readNumber("Please enter a number bigger than 0: ");
        }

        int messagesEntered = 0;
        boolean running = true;

        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            int option = readNumber("Choose an option: ");

            switch (option) {
                case 1:
                    if (messagesEntered >= maxMessages) {
                        System.out.println("You have already entered all " + maxMessages + " messages.");
                        break;
                    }
                    for (int i = messagesEntered; i < maxMessages; i++) {
                        System.out.println("\n--- Message " + (i + 1) + " of " + maxMessages + " ---");
                        sendOneMessage(i);
                        messagesEntered++;
                    }
                    System.out.println("\nTotal number of messages sent: " + Message.returnTotalMessages());
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
        }
    }

    // Captures one message and lets the user send, disregard or store it
    private static void sendOneMessage(int messageNumber) {
        Login login = new Login();
        String recipient;
        do {
            System.out.print("Enter the recipient's cell number: ");
            recipient = input.nextLine();
            if (!login.checkCellPhoneNumber(recipient)) {
                System.out.println("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.");
            }
        } while (!login.checkCellPhoneNumber(recipient));

        String text;
        do {
            System.out.print("Enter your message: ");
            text = input.nextLine();
            if (text.isBlank() || text.length() > 250) {
                System.out.println("Please enter a message of less than 250 characters.");
            }
        } while (text.isBlank() || text.length() > 250);

        Message message = new Message(messageNumber, recipient, text);
        System.out.println(message.checkRecipientCell());
        System.out.println(message.checkMessageLength());

        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int choice = readNumber("Choose an option: ");
        while (choice < 1 || choice > 3) {
            choice = readNumber("Please choose 1, 2 or 3: ");
        }

        System.out.println(message.sentMessage(choice));

        if (choice == 1) {
            System.out.println(message.getMessageDetails());
        } else if (choice == 2) {
            while (readNumber("") != 0) {
                System.out.println("Press 0 to delete the message.");
            }
            System.out.println("Message deleted.");
        }
    }

    // Keeps asking until a whole number is entered
    private static int readNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
