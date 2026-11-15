package com.mycompany.quickchat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageManagerTest {

    private MessageManager manager;
    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    // Part 3 test data from the brief
    @BeforeEach
    public void setUp() {
        manager = new MessageManager("+27838968976", 5);

        message1 = new Message(0, "+27834557896", "Did you get the cake?");
        message2 = new Message(1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        message3 = new Message(2, "+27834484567", "Yohoooo, I am at your gate.");
        message4 = new Message(3, "0838884567", "It is dinner time!");
        message5 = new Message(4, "+27838884567", "Ok, I am leaving without you.");

        manager.addMessage(message1, MessageManager.FLAG_SENT);
        manager.addMessage(message2, MessageManager.FLAG_STORED);
        manager.addMessage(message3, MessageManager.FLAG_DISREGARD);
        manager.addMessage(message4, MessageManager.FLAG_SENT);
        manager.addMessage(message5, MessageManager.FLAG_STORED);
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        String[] expected = {"Did you get the cake?", "It is dinner time!"};
        assertArrayEquals(expected, manager.getSentMessages());
    }

    @Test
    public void testDisregardedMessagesArray() {
        assertArrayEquals(new String[]{"Yohoooo, I am at your gate."}, manager.getDisregardedMessages());
    }

    @Test
    public void testMessageHashAndIDArraysPopulated() {
        String[] hashes = manager.getMessageHashes();
        String[] ids = manager.getMessageIDs();
        assertEquals(5, hashes.length);
        assertEquals(5, ids.length);
        assertEquals(message2.getMessageHash(), hashes[1]);
        assertEquals(message4.getMessageID(), ids[3]);
    }

    @Test
    public void testDisplayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                manager.getLongestMessage());
    }

    @Test
    public void testSearchForMessageID() {
        assertEquals("Recipient: 0838884567\nMessage: It is dinner time!",
                manager.searchByMessageID(message4.getMessageID()));
    }

    @Test
    public void testSearchForMessageIDNotFound() {
        assertEquals("Message ID 0000000000 was not found.", manager.searchByMessageID("0000000000"));
    }

    @Test
    public void testSearchMessagesForRecipient() {
        String[] expected = {
            "Where are you? You are late! I have asked you to be on time.",
            "Ok, I am leaving without you."
        };
        assertArrayEquals(expected, manager.searchByRecipient("+27838884567"));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                manager.deleteByMessageHash(message2.getMessageHash()));
        // message 2 is gone, so only message 5 is left for that recipient
        assertArrayEquals(new String[]{"Ok, I am leaving without you."}, manager.searchByRecipient("+27838884567"));
        assertEquals(4, manager.getMessageHashes().length);
    }

    @Test
    public void testDeleteMessageHashNotFound() {
        assertEquals("Message hash 99:9:NOTHERE was not found.", manager.deleteByMessageHash("99:9:NOTHERE"));
    }

    @Test
    public void testDisplaySenderAndRecipients() {
        String output = manager.displaySenderAndRecipients();
        assertTrue(output.contains("Sender: +27838968976 | Recipient: +27834557896"));
        assertTrue(output.contains("Sender: +27838968976 | Recipient: +27838884567"));
        assertFalse(output.contains("+27834484567")); // disregarded message is left out
    }

    @Test
    public void testDisplayReport() {
        String report = manager.displayReport();
        Message[] sent = {message1, message4};
        for (Message message : sent) {
            assertTrue(report.contains("Message Hash: " + message.getMessageHash()));
            assertTrue(report.contains("Recipient: " + message.getRecipient()));
            assertTrue(report.contains("Message: " + message.getMessageText()));
        }
        assertFalse(report.contains("Yohoooo, I am at your gate."));
    }

    @Test
    public void testReadStoredMessagesFromJson() {
        Message toStore = new Message(0, "+27838884567", "Part 3 JSON read test message");
        toStore.sentMessage(3); // writes the JSON file

        Message[] fromFile = manager.readStoredMessages(Message.JSON_FILE);
        boolean found = false;
        for (Message message : fromFile) {
            if (message.getMessageID().equals(toStore.getMessageID())) {
                assertEquals(toStore.getMessageText(), message.getMessageText());
                assertEquals(toStore.getMessageHash(), message.getMessageHash());
                found = true;
            }
        }
        assertTrue(found);
    }
}
