/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.power_chat_application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageTest {


    @Test
    void testCheckMessageLength_under250Characters_returnsReadyMessage() {
        Message message = new Message("0012345678", "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", message.checkMessageLength(message.getMessageText()));
    }

    @Test
    void testCheckMessageLength_over250Characters_returnsExcessMessage() {
        Message message = new Message("0012345678", "+27718693002", "x");
        String longMessage = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                message.checkMessageLength(longMessage));
    }

    @Test
    void testCheckRecipientCell_correctlyFormatted_returnsSuccessMessage() {
        Message message = new Message("0012345678", "+27718693002", "Hi Mike!");
        assertEquals("Cell phone number successfully captured.",
                message.checkRecipientCell("+27718693002"));
    }

    @Test
    void testCheckRecipientCell_incorrectlyFormatted_returnsFailureMessage() {
        Message message = new Message("0098765432", "08575975889", "Hi Keegan!");
        assertEquals("Cell phone number incorrectly formatted or does not contain international code. "
                + "Please correct the number and try again.", message.checkRecipientCell("08575975889"));
    }

    @Test
    void testCreateMessageHash_testCase1_returnsExpectedHash() {
        Message message = new Message("0012345678", "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    void testCreateMessageHash_loopsCorrectlyForSecondMessage() {
        Message first = new Message("0012345678", "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        first.createMessageHash();

        Message second = new Message("0098765432", "08575975889",
                "Hi Keegan, did you receive the payment?");
        assertEquals("00:1:HIPAYMENT", second.createMessageHash());
    }

    @Test
    void testCheckMessageID_tenCharactersOrFewer_returnsTrue() {
        Message message = new Message("0012345678", "+27718693002", "Hi Mike!");
        assertTrue(message.checkMessageID("0012345678"));
    }

    @Test
    void testGetMessageIdStatus_returnsGeneratedMessage() {
        Message message = new Message("0012345678", "+27718693002", "Hi Mike!");
        assertEquals("Message ID generated: 0012345678", message.getMessageIdStatus());
    }

    @Test
    void testSentMessage_userSelectsSend_returnsSuccessMessage() {
        Message message = new Message("0012345678", "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", message.sentMessage(1));
    }

    @Test
    void testSentMessage_userSelectsDisregard_returnsPressZeroMessage() {
        Message message = new Message("0098765432", "08575975889",
                "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", message.sentMessage(2));
    }

    @Test
    void testSentMessage_userSelectsStore_returnsStoredMessage() {
        Message message = new Message("0098765432", "08575975889",
                "Hi Keegan, did you receive the payment?");
        assertEquals("Message successfully stored.", message.sentMessage(3));
    }

    @Test
    void testReturnTotalMessagesSent_afterOneSend_returnsOne() {
        Message message = new Message("0012345678", "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        message.sentMessage(1);
        assertEquals(1, message.returnTotalMessagesSent());
    }

    @Test
    void testReturnTotalMessagesSent_discardAndStoreDoNotCount() {
        Message discarded = new Message("0098765432", "08575975889", "Hi Keegan!");
        discarded.sentMessage(2);
        Message stored = new Message("0011122233", "+27718693002", "Hi again!");
        stored.sentMessage(3);
        assertEquals(0, stored.returnTotalMessagesSent());
    }
}