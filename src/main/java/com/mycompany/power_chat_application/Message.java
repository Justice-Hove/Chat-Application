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
    private static final java.util.Random ID_GENERATOR = new java.util.Random();
    
     private static int messageSequence = 0;
    
         private static final java.util.List<Message> sentHistory = new java.util.ArrayList<>();
    private static int sendCount = 0;
     
    private final String msgId;
    private final String recipientNumber;
    private final String body;

    
     private final int messageNumber;
    private String messageHash;
    
    public Message(String messageId, String recipientCell, String messageText) {
        this.msgId = messageId;
        this.recipientNumber = recipientCell;
        this.body = messageText;
        this.messageNumber = messageSequence;   
        messageSequence++;  
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
        if (message != null && message.length() <= 250) {
            return "Message ready to send.";
        }
        int length = message == null ? 0 : message.length();
        int overLimitBy = length - 250;
        return "Message exceeds 250 characters by " + overLimitBy + "; please reduce the size.";
    }

    /* A return message comes confirming that the message ID has been created. */
    public String getMessageIdStatus() {
        return "Message ID generated: " + msgId;
    }
    
     /*
     * Lets the user choose to send, disregard, or store the message.
     * choice: 1 = Send, 2 = Disregard, 3 = Store.
     */
    public String sentMessage(int userSelection) {
        switch (userSelection) {
            case 1:
                sendCount++;
                sentHistory.add(this);
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

    
    private boolean isValidRecipientCell(String cell) {
        return cell != null && RECIPIENT_FORMAT_RULE.matcher(cell).matches();
    }

     /*
     * Creating and returning Message Hash: the first two digits of the
     * message ID, a colon, the message number, a colon, and the first and
     * last words of the message (punctuation stripped).
     */
    public String createMessageHash() {
        String idPrefix = msgId.length() >= 2 ? msgId.substring(0, 2) : msgId;
        String[] words = body.trim().split("\\s+");
        String firstWord = stripPunctuation(words[0]);
        String lastWord = stripPunctuation(words[words.length - 1]);

        this.messageHash = (idPrefix + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return this.messageHash;
    }

    private String stripPunctuation(String word) {
        return word.replaceAll("[^a-zA-Z0-9]", "");
    }
    
    public String getMessageId() { return msgId; }
    public String getRecipientCell() { return recipientNumber; }
    public String getMessageText() { return body; }
    public int getMessageNumber() { return messageNumber; }
    public String getMessageHash() { return messageHash; }

    
    
    /* this stores the message in JSON format in a file named store_messages.json.
    
    */
   public void storeMessage() {
    try (java.io.FileWriter writer =
            new java.io.FileWriter("stored_messages.json", true)) {
        writer.write(toJson());
        writer.write(System.lineSeparator());
    } catch (java.io.IOException e) {
        System.out.println("Could not store message: " + e.getMessage());
    }
}
/*converting the message details into a JSON string
   
   */
private String toJson() {
    return "{\"messageId\":\"" + escape(msgId) + "\","
            + "\"messageNumber\":" + messageNumber + ","
            + "\"recipientCell\":\"" + escape(recipientNumber) + "\","
            + "\"message\":\"" + escape(body) + "\","
            + "\"messageHash\":\"" + escape(messageHash) + "\"}";
}
/*escapes special characters so that strings will b4e safely allowed in JSON output

*/
private static String escape(String s) {
    if (s == null) {
        return "";
    }
    return s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
}
/*
 * Returns all messages sen8t while the program is running,
 * displaying the Message ID, Message Hashh, Recipient,
 * and Message in that order.
 */
public String printMessages() {
    StringBuilder sb = new StringBuilder();

    
    
    for (Message m : sentHistory) {
        sb.append("Message ID: ").append(m.msgId)
                .append(", Message Hash: ").append(m.messageHash)
                .append(", Recipient: ").append(m.recipientNumber)
                .append(", Message: ").append(m.body)
                .append(System.lineSeparator());
    }

    return sb.toString();
}


/* Returns the total number of messages sent so far. */
public int returnTotalMessagesSent() {
    return sendCount;
}


/*
 * Resets the message history and message counter.
 */
public static void resetMessageLog() {
    sentHistory.clear();
    sendCount = 0;
    messageSequence = 0;
}
}


