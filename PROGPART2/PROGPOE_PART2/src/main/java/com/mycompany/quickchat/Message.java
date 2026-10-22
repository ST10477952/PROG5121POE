package com.mycompany.quickchat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * A single chat message plus the methods to validate, send and store it.
 */
public class Message {

    private static final String JSON_FILE = "messages.json";
    private static final ArrayList<Message> sentMessages = new ArrayList<>();
    private static final ArrayList<Message> storedMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    public Message(int messageNumber, String recipient, String messageText) {
        this(generateMessageID(), messageNumber, recipient, messageText);
    }

    // Used by the tests so the ID is known
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Builds a random 10 digit ID one digit at a time
    private static String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String printMessageID() {
        return "Message ID generated: " + messageID;
    }

    // Reuses the cell number check from Login
    public String checkRecipientCell() {
        Login login = new Login();
        if (login.checkCellPhoneNumber(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int over = messageText.length() - 250;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    // e.g. 00:0:HITONIGHT -> first 2 digits of ID, message number, first + last word
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

        String hash = messageID.substring(0, 2) + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }

    // 1 = send, 2 = disregard, 3 = store
    public String sentMessage(int option) {
        switch (option) {
            case 1:
                sentMessages.add(this);
                totalMessagesSent++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    public String getMessageDetails() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        StringBuilder output = new StringBuilder();
        for (Message message : sentMessages) {
            output.append(message.getMessageDetails()).append("\n\n");
        }
        return output.toString().trim();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    /*
     * Writes all stored messages to messages.json.
     * Adapted from: Google. 2023. Gson User Guide. [Online] Available at:
     * https://github.com/google/gson/blob/main/UserGuide.md
     */
    public void storeMessage() {
        storedMessages.add(this);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(storedMessages, writer);
        } catch (IOException e) {
            System.out.println("Could not save message: " + e.getMessage());
        }
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }
}
