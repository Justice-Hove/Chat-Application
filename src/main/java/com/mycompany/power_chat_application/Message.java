/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.power_chat_application;


import java.util.regex.Pattern;


public class Message {
public static final int MAX_MESSAGE_LENGTH = 250;
    
    /*
     * Recipient cell number check, reusing the same South African 
    international-format approach as I used in the Part 1's Login.checkCellPhoneNumber(). (Rios, 2026)
     */
    private static final Pattern RECIPIENT_FORMAT_RULE = Pattern.compile("^\\+27\\d{9}$");
    private static final java.util.Random ID_GENERATOR = new java.util.Random();
    
    private final String msgId;
    private final String recipientNumber;
    private final String body;
    private String sender = "Unknown";
    private String messageHash;
    
    public Message(String messageId, String recipientCell, String messageText) {
        this.msgId = messageId;
        this.recipientNumber = recipientCell;
        this.body = messageText;
    }
    
       /* Generating a random 10-digit message ID. */
    public static String generateMessageId() {
        long generatedId = (long) (ID_GENERATOR.nextDouble() * 10_000_000_000L);
        return String.format("%010d", generatedId);
    }

    /* this ensures that the message ID is not more than ten characters. */
    public boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }

     /*
     * Ensuring the message does not over exceed 250 characters when returning the success of failure of message.
     */
 public String checkMessageLength(String message) {
    if (message != null && message.length() <= MAX_MESSAGE_LENGTH) {
        return "Message ready to send.";
    }
    int length = message == null ? 0 : message.length();
    int overLimitBy = length - MAX_MESSAGE_LENGTH;
    return "Message exceeds 250 characters by " + overLimitBy + "; please reduce the size.";
}

    /* Returns a message confirming that the message ID has been created. */
    public String getMessageIdStatus() {
        return "Message ID generated: " + msgId;
    }
    
    /*
     * this ensures the recipient cell number is no more than ten characters long
     and starts with an international code, returning the appropriate
     * user message.
     */
    
    
    public String checkRecipientCell(String cell) {
        if (isValidRecipientCell(cell)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number incorrectly formatted or does not contain international code. ";
    }

    
public static boolean isValidRecipientCell(String cell) {
    return cell != null && RECIPIENT_FORMAT_RULE.matcher(cell).matches();
}
    
    
    public String getMessageId() { return msgId; }
    public String getRecipientCell() { return recipientNumber; }
    public String getMessageText() { return body; }
    
    public String getSender() {
    return sender;
}

public void setSender(String sender) {
    this.sender = sender;
}
static String escape(String s) {
    if (s == null) {
        return "";
    }

    return s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
}
public String createMessageHash() {
    try {
        java.security.MessageDigest digest =
                java.security.MessageDigest.getInstance("SHA-256");

        String data = msgId + recipientNumber + body;

        byte[] hashBytes = digest.digest(
                data.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );

        StringBuilder hexString = new StringBuilder();

        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        messageHash = hexString.toString();
        return messageHash;

    } catch (java.security.NoSuchAlgorithmException e) {
        throw new IllegalStateException(e);
    }
}

public String getMessageHash() {
    return messageHash;
}
}

