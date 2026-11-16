/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.power_chat_application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Part 3 unit tests, using the five test messages from the brief.
 * Each test gets its own empty store and its own temporary JSON file,
 * so nothing is written to the real stored_messages.json.
 */
class MessageStoreTest {

    private static final String LONGEST = "Where are you? You are late! I have asked you to be on time.";

    @TempDir
    Path tempDir;

    private MessageStore store;
    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @BeforeEach
    void setUp() {
        Message.resetMessageLog();
        store = new MessageStore(tempDir.resolve("stored_messages.json").toString());

        // Test Data Message 1 - Sent
        message1 = new Message("1234567890", "+27834557896", "Did you get the cake?");
        store.addMessage(message1, MessageStore.FLAG_SENT);

        // Test Data Message 2 - Stored
        message2 = new Message("2345678901", "+27838884567", LONGEST);
        store.addMessage(message2, MessageStore.FLAG_STORED);

        // Test Data Message 3 - Disregard
        message3 = new Message("3456789012", "+27834484567", "Yohoooo, I am at your gate.");
        store.addMessage(message3, MessageStore.FLAG_DISREGARD);

        // Test Data Message 4 - Sent (developer entry, ID 0838884567)
        message4 = new Message("0838884567", "0838884567", "It is dinner time!");
        store.addMessage(message4, MessageStore.FLAG_SENT);

        // Test Data Message 5 - Stored
        message5 = new Message("5678901234", "+27838884567", "Ok, I am leaving without you.");
        store.addMessage(message5, MessageStore.FLAG_STORED);
    }

    // ---------- Sent Messages array correctly populated ----------

    @Test
    void testSentMessagesArray_containsMessages1And4() {
        assertEquals(List.of("Did you get the cake?", "It is dinner time!"), store.getSentMessages());
    }

    @Test
    void testDisregardedMessagesArray_containsMessage3() {
        assertEquals(List.of("Yohoooo, I am at your gate."), store.getDisregardedMessages());
    }

    @Test
    void testStoredMessagesArray_containsMessages2And5() {
        assertEquals(List.of(LONGEST, "Ok, I am leaving without you."), store.getStoredMessages());
    }

    @Test
    void testMessageIdArray_containsAllFiveIds() {
        assertEquals(List.of("1234567890", "2345678901", "3456789012", "0838884567", "5678901234"),
                store.getMessageIds());
    }

    @Test
    void testMessageHashArray_containsAllFiveHashes() {
        assertEquals(List.of(message1.getMessageHash(), message2.getMessageHash(),
                message3.getMessageHash(), message4.getMessageHash(), message5.getMessageHash()),
                store.getMessageHashes());
    }

    // ---------- Display the longest message ----------

    @Test
    void testLongestStoredMessage_returnsMessage2() {
        assertEquals(LONGEST, store.getLongestStoredMessage());
    }

    // ---------- Search for messageID ----------

    @Test
    void testSearchByMessageId_message4_returnsDinnerMessage() {
        assertEquals("It is dinner time!", store.searchByMessageId("0838884567"));
    }

    @Test
    void testSearchByMessageId_unknownId_returnsNotFound() {
        assertEquals("Message ID not found.", store.searchByMessageId("9999999999"));
    }

    @Test
    void testDescribeMessageById_showsRecipientAndMessage() {
        assertEquals("Recipient: 0838884567, Message: It is dinner time!",
                store.describeMessageById("0838884567"));
    }

    // ---------- Search all messages sent or stored for a recipient ----------

    @Test
    void testSearchByRecipient_returnsMessages2And5() {
        assertEquals(List.of(LONGEST, "Ok, I am leaving without you."),
                store.searchByRecipient("+27838884567"));
    }

    @Test
    void testSearchByRecipient_ignoresDisregardedMessages() {
        assertTrue(store.searchByRecipient("+27834484567").isEmpty());
    }

    // ---------- Delete a message using a message hash ----------

    @Test
    void testDeleteByHash_message2_returnsDeletedMessage() {
        assertEquals("Message: \"" + LONGEST + "\" successfully deleted.",
                store.deleteByHash(message2.getMessageHash()));
        assertEquals(List.of("Ok, I am leaving without you."), store.getStoredMessages());
    }

    @Test
    void testDeleteByHash_unknownHash_returnsNotFound() {
        assertEquals("Message hash not found.", store.deleteByHash("00:0:NOPE"));
    }

    // ---------- Display report ----------

    @Test
    void testSentMessagesReport_showsHashRecipientAndMessage() {
        String expected = "Message Hash: " + message1.getMessageHash()
                + ", Recipient: +27834557896, Message: Did you get the cake?"
                + System.lineSeparator()
                + "Message Hash: " + message4.getMessageHash()
                + ", Recipient: 0838884567, Message: It is dinner time!";
        assertEquals(expected, store.getSentMessagesReport());
    }

    @Test
    void testStoredMessagesReport_showsFullDetails() {
        String expected = "Message ID: 2345678901, Message Hash: " + message2.getMessageHash()
                + ", Sender: Unknown, Recipient: +27838884567, Message: " + LONGEST
                + System.lineSeparator()
                + "Message ID: 5678901234, Message Hash: " + message5.getMessageHash()
                + ", Sender: Unknown, Recipient: +27838884567, Message: Ok, I am leaving without you.";
        assertEquals(expected, store.getStoredMessagesReport());
    }

    @Test
    void testSendersAndRecipients_listsStoredMessagesOnly() {
        message2.setSender("Developer");
        MessageStore fresh = new MessageStore(tempDir.resolve("other.json").toString());
        fresh.addMessage(message2, MessageStore.FLAG_STORED);
        assertEquals("Sender: Developer, Recipient: +27838884567", fresh.displaySendersAndRecipients());
    }

    // ---------- Read the JSON file into an array ----------

    @Test
    void testLoadStoredMessages_readsJsonFileIntoArray() throws IOException {
        Path file = tempDir.resolve("saved.json");
        Files.writeString(file,
                "{\"messageId\":\"1111111111\",\"messageNumber\":0,\"sender\":\"Ann\","
                        + "\"recipientCell\":\"+27834557896\",\"message\":\"Did you get the cake?\","
                        + "\"messageHash\":\"11:0:DIDCAKE\"}" + System.lineSeparator()
                        + "{\"messageId\":\"2222222222\",\"messageNumber\":1,\"sender\":\"Ann\","
                        + "\"recipientCell\":\"+27838884567\",\"message\":\"Ok, I am leaving without you.\","
                        + "\"messageHash\":\"22:1:OKYOU\"}" + System.lineSeparator());

        MessageStore loaded = new MessageStore(file.toString());
        assertEquals(2, loaded.loadStoredMessages());
        assertEquals(List.of("Did you get the cake?", "Ok, I am leaving without you."),
                loaded.getStoredMessages());
        assertEquals(List.of("11:0:DIDCAKE", "22:1:OKYOU"), loaded.getMessageHashes());
    }

    @Test
    void testLoadStoredMessages_missingFile_loadsNothing() {
        MessageStore empty = new MessageStore(tempDir.resolve("does_not_exist.json").toString());
        assertEquals(0, empty.loadStoredMessages());
    }

    @Test
    void testDeleteThenReload_fileKeepsOnlyRemainingMessages_andQuotesSurvive() {
        Message quoted = new Message("7777777777", "+27834557896", "He said \"hi\"\nthen left");
        store.addMessage(quoted, MessageStore.FLAG_STORED);

        store.deleteByHash(message2.getMessageHash());   // rewrites the JSON file

        MessageStore reloaded = new MessageStore(tempDir.resolve("stored_messages.json").toString());
        assertEquals(2, reloaded.loadStoredMessages());
        assertEquals(List.of("Ok, I am leaving without you.", "He said \"hi\"\nthen left"),
                reloaded.getStoredMessages());
    }
}

