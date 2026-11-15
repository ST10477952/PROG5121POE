package com.mycompany.quickchat;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Part 3 - keeps every captured message in parallel arrays so they can be
 * searched, deleted and reported on from the "Stored Messages" menu.
 *
 * The same index in each array belongs to the same message, e.g.
 * messageIDs[2], messageHashes[2], recipients[2], messageTexts[2] and
 * messageFlags[2] all describe the third message.
 */
public class MessageManager {

    public static final String FLAG_SENT = "Sent";
    public static final String FLAG_STORED = "Stored";
    public static final String FLAG_DISREGARD = "Disregard";

    private final String senderCell;

    // Parallel arrays
    private String[] messageIDs;
    private String[] messageHashes;
    private String[] recipients;
    private String[] messageTexts;
    private String[] messageFlags;
    private int messageCount = 0;

    // Messages read back in from the JSON file
    private Message[] storedMessages = new Message[0];

    public MessageManager(String senderCell, int startingSize) {
        this.senderCell = senderCell;
        int size = Math.max(startingSize, 1);
        messageIDs = new String[size];
        messageHashes = new String[size];
        recipients = new String[size];
        messageTexts = new String[size];
        messageFlags = new String[size];
    }

    // Adds a message to the end of every parallel array
    public void addMessage(Message message, String flag) {
        if (messageCount == messageIDs.length) {
            growArrays();
        }
        messageIDs[messageCount] = message.getMessageID();
        messageHashes[messageCount] = message.getMessageHash();
        recipients[messageCount] = message.getRecipient();
        messageTexts[messageCount] = message.getMessageText();
        messageFlags[messageCount] = flag;
        messageCount++;
    }

    // Converts the menu choice used in Message.sentMessage() to a flag
    public static String flagForOption(int option) {
        switch (option) {
            case 1:
                return FLAG_SENT;
            case 3:
                return FLAG_STORED;
            default:
                return FLAG_DISREGARD;
        }
    }

    // Doubles the size of the arrays when they are full
    private void growArrays() {
        int newSize = messageIDs.length * 2;
        messageIDs = Arrays.copyOf(messageIDs, newSize);
        messageHashes = Arrays.copyOf(messageHashes, newSize);
        recipients = Arrays.copyOf(recipients, newSize);
        messageTexts = Arrays.copyOf(messageTexts, newSize);
        messageFlags = Arrays.copyOf(messageFlags, newSize);
    }

    // Sent and stored messages are kept; disregarded ones are not
    private boolean isKept(int index) {
        return !FLAG_DISREGARD.equals(messageFlags[index]);
    }

    // Returns the text of every message that has the given flag
    private String[] messagesWithFlag(String flag) {
        int matches = 0;
        for (int i = 0; i < messageCount; i++) {
            if (flag.equals(messageFlags[i])) {
                matches++;
            }
        }

        String[] result = new String[matches];
        int position = 0;
        for (int i = 0; i < messageCount; i++) {
            if (flag.equals(messageFlags[i])) {
                result[position++] = messageTexts[i];
            }
        }
        return result;
    }

    public String[] getSentMessages() {
        return messagesWithFlag(FLAG_SENT);
    }

    public String[] getDisregardedMessages() {
        return messagesWithFlag(FLAG_DISREGARD);
    }

    public String[] getMessageHashes() {
        return Arrays.copyOf(messageHashes, messageCount);
    }

    public String[] getMessageIDs() {
        return Arrays.copyOf(messageIDs, messageCount);
    }

    /*
     * Reads the stored messages JSON file (written by Message.storeMessage())
     * back into an array.
     * Adapted from: Google. 2023. Gson User Guide. [Online] Available at:
     * https://github.com/google/gson/blob/main/UserGuide.md#array-examples
     */
    public Message[] readStoredMessages(String fileName) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(fileName)) {
            Message[] fromFile = gson.fromJson(reader, Message[].class);
            storedMessages = (fromFile == null) ? new Message[0] : fromFile;
        } catch (IOException e) {
            storedMessages = new Message[0];
        }
        return storedMessages;
    }

    public String displayStoredMessagesFromJson() {
        if (storedMessages.length == 0) {
            return "No stored messages were found in the JSON file.";
        }
        StringBuilder output = new StringBuilder("Stored messages read from JSON:\n");
        for (Message message : storedMessages) {
            output.append("\n").append(message.getMessageDetails()).append("\n");
        }
        return output.toString().trim();
    }

    // a. Sender and recipient of every sent/stored message
    public String displaySenderAndRecipients() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < messageCount; i++) {
            if (isKept(i)) {
                output.append("Sender: ").append(senderCell)
                        .append(" | Recipient: ").append(recipients[i]).append("\n");
            }
        }
        return output.length() == 0 ? "There are no messages to display." : output.toString().trim();
    }

    // b. Longest sent/stored message
    public String getLongestMessage() {
        String longest = "";
        for (int i = 0; i < messageCount; i++) {
            if (isKept(i) && messageTexts[i].length() > longest.length()) {
                longest = messageTexts[i];
            }
        }
        return longest.isEmpty() ? "There are no messages to display." : longest;
    }

    // c. Search for a message ID and show the recipient and message
    public String searchByMessageID(String messageID) {
        for (int i = 0; i < messageCount; i++) {
            if (isKept(i) && messageIDs[i].equals(messageID)) {
                return "Recipient: " + recipients[i] + "\nMessage: " + messageTexts[i];
            }
        }
        return "Message ID " + messageID + " was not found.";
    }

    // d. All sent/stored messages for one recipient
    public String[] searchByRecipient(String recipient) {
        int matches = 0;
        for (int i = 0; i < messageCount; i++) {
            if (isKept(i) && recipients[i].equals(recipient)) {
                matches++;
            }
        }

        String[] result = new String[matches];
        int position = 0;
        for (int i = 0; i < messageCount; i++) {
            if (isKept(i) && recipients[i].equals(recipient)) {
                result[position++] = messageTexts[i];
            }
        }
        return result;
    }

    // e. Delete a message using its hash - shifts everything after it left by one
    public String deleteByMessageHash(String messageHash) {
        for (int i = 0; i < messageCount; i++) {
            if (messageHashes[i].equalsIgnoreCase(messageHash)) {
                String deletedText = messageTexts[i];
                for (int j = i; j < messageCount - 1; j++) {
                    messageIDs[j] = messageIDs[j + 1];
                    messageHashes[j] = messageHashes[j + 1];
                    recipients[j] = recipients[j + 1];
                    messageTexts[j] = messageTexts[j + 1];
                    messageFlags[j] = messageFlags[j + 1];
                }
                messageCount--;
                messageIDs[messageCount] = null;
                messageHashes[messageCount] = null;
                recipients[messageCount] = null;
                messageTexts[messageCount] = null;
                messageFlags[messageCount] = null;
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }
        return "Message hash " + messageHash + " was not found.";
    }

    // f. Full report of every sent/stored message
    public String displayReport() {
        StringBuilder report = new StringBuilder("===== Message Report =====\n");
        boolean found = false;
        for (int i = 0; i < messageCount; i++) {
            if (isKept(i)) {
                found = true;
                report.append("\nMessage ID: ").append(messageIDs[i])
                        .append("\nMessage Hash: ").append(messageHashes[i])
                        .append("\nRecipient: ").append(recipients[i])
                        .append("\nMessage: ").append(messageTexts[i])
                        .append("\nStatus: ").append(messageFlags[i])
                        .append("\n");
            }
        }
        if (!found) {
            report.append("\nThere are no messages to report.");
        }
        return report.toString().trim();
    }
}
