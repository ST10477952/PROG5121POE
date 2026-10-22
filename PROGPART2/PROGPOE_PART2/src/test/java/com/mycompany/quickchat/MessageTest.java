package com.mycompany.quickchat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MessageTest {

    // Test data from the brief
    private final Message message1 = new Message("0012345678", 0, "+27718693002",
            "Hi Mike, can you join us for dinner tonight?");
    private final Message message2 = new Message("0087654321", 1, "08575975889",
            "Hi Keegan, did you receive the payment?");

    @Test
    public void testMessageLengthSuccess() {
        assertEquals("Message ready to send.", message1.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        Message longMessage = new Message("1234567890", 0, "+27718693002", "a".repeat(260));
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                longMessage.checkMessageLength());
    }

    @Test
    public void testRecipientCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    public void testRecipientIncorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }

    @Test
    public void testMessageHash() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @Test
    public void testMessageHashesInLoop() {
        Message[] messages = {
            message1,
            message2,
            new Message("4512345678", 2, "+27834557896", "Did you get the cake?")
        };
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT", "45:2:DIDCAKE"};

        for (int i = 0; i < messages.length; i++) {
            assertEquals(expected[i], messages[i].createMessageHash());
        }
    }

    @Test
    public void testMessageIDCreated() {
        Message message = new Message(0, "+27718693002", "Hello there");
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
        assertEquals("Message ID generated: " + message.getMessageID(), message.printMessageID());
    }

    @Test
    public void testSendMessage() {
        Message message = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", message.sentMessage(1));
    }

    @Test
    public void testDisregardMessage() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    @Test
    public void testStoreMessage() {
        Message message = new Message(0, "+27718693002", "Store me for later please");
        assertEquals("Message successfully stored.", message.sentMessage(3));
    }

    @Test
    public void testTotalMessagesSent() {
        int before = Message.returnTotalMessages();
        Message first = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        Message second = new Message(1, "08575975889", "Hi Keegan, did you receive the payment?");

        first.sentMessage(1);   // send
        second.sentMessage(2);  // discard

        assertEquals(before + 1, Message.returnTotalMessages());
        assertTrue(Message.printMessages().contains(first.getMessageDetails()));
    }
}
