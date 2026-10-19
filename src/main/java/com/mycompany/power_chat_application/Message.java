/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.power_chat_application;


import java.util.regex.Pattern;


public class Message {

    
    /*
     * Recipient cell number check, reusing the same South African 
    international-format approach as I used in the Part 1's Login.checkCellPhoneNumber(). (Rios, 2026)
     */
    private static final Pattern RECIPIENT_FORMAT_RULE = Pattern.compile("^\\+27\\d{9}$");

    private final String msgId;
    private final String recipientNumber;
    private final String body;

    public Message(String messageId, String recipientCell, String messageText) {
        this.msgId = messageId;
        this.recipientNumber = recipientCell;
        this.body = messageText;
    }

    /** this ensures that the message ID is not more than ten characters. */
    public boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }

    /**
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

    
    private boolean isValidRecipientCell(String cell) {
        return cell != null && RECIPIENT_FORMAT_RULE.matcher(cell).matches();
    }

    
    
    public String getMessageId() { return msgId; }
    public String getRecipientCell() { return recipientNumber; }
    public String getMessageText() { return body; }
}
