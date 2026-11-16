/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.power_chat_application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageStore {

    public static final String DEFAULT_FILE = "stored_messages.json";
    public static final String FLAG_SENT = "Sent";
    public static final String FLAG_STORED = "Stored";
    public static final String FLAG_DISREGARD = "Disregard";

    // ---------- parallel arrays (one entry per message, whatever its flag) ----------
    private final ArrayList<String> messageIds = new ArrayList<>();
    private final ArrayList<String> messageHashes = new ArrayList<>();
    private final ArrayList<String> senders = new ArrayList<>();
    private final ArrayList<String> recipients = new ArrayList<>();
    private final ArrayList<String> messageTexts = new ArrayList<>();
    private final ArrayList<String> flags = new ArrayList<>();

    // ---------- arrays required by the brief ----------
    private final ArrayList<String> sentMessages = new ArrayList<>();
    private final ArrayList<String> disregardedMessages = new ArrayList<>();
    private final ArrayList<String> storedMessages = new ArrayList<>();

    private final String storageFile;

    public MessageStore() {
        this(DEFAULT_FILE);
    }

    /** allows a unit test point the store at a temporary file. */
    public MessageStore(String storageFile) {
        this.storageFile = storageFile;
    }

    /** Adds a Message object to the arrays under the given flag (Sent, Stored or Disregard). */
    public void addMessage(Message message, String flag) {
        String hash = message.getMessageHash() != null
                ? message.getMessageHash()
                : message.createMessageHash();
        addEntry(message.getMessageId(), hash, message.getSender(),
                message.getRecipientCell(), message.getMessageText(), flag);
    }

    private void addEntry(String id, String hash, String sender, String recipient,
                          String text, String flag) {
        messageIds.add(id);
        messageHashes.add(hash);
        senders.add(sender);
        recipients.add(recipient);
        messageTexts.add(text);
        flags.add(flag);

        if (FLAG_SENT.equals(flag)) {
            sentMessages.add(text);
        } else if (FLAG_DISREGARD.equals(flag)) {
            disregardedMessages.add(text);
        } else if (FLAG_STORED.equals(flag)) {
            storedMessages.add(text);
        }
    }

    public List<String> getSentMessages() { return new ArrayList<>(sentMessages); }
    public List<String> getDisregardedMessages() { return new ArrayList<>(disregardedMessages); }
    public List<String> getStoredMessages() { return new ArrayList<>(storedMessages); }
    public List<String> getMessageHashes() { return new ArrayList<>(messageHashes); }
    public List<String> getMessageIds() { return new ArrayList<>(messageIds); }


    public int loadStoredMessages() {
        int loaded = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String json = cleanJsonLine(line);
                if (!json.startsWith("{")) {
                    continue;
                }
                String id = readJsonString(json, "messageId");
                if (id.isEmpty() || messageIds.contains(id)) {
                    continue;
                }
                String sender = readJsonString(json, "sender");
                addEntry(id,
                        readJsonString(json, "messageHash"),
                        sender.isEmpty() ? "Unknown" : sender,
                        readJsonString(json, "recipientCell"),
                        readJsonString(json, "message"),
                        FLAG_STORED);
                loaded++;
            }
        } catch (IOException e) {
            // no file yet means nothing has been stored, so there is nothing to load
        }
        return loaded;
    }

    /** Removes the brackets and commas a JSON array would put around a line. */
    private String cleanJsonLine(String line) {
        String cleaned = line.trim();
        if (cleaned.startsWith("[")) {
            cleaned = cleaned.substring(1).trim();
        }
        if (cleaned.endsWith("]") || cleaned.endsWith(",")) {
            cleaned = cleaned.substring(0, cleaned.length() - 1).trim();
        }
        return cleaned;
    }

    /** Reads the text value of one key from a single-line JSON object, undoing the escapes. */
    private String readJsonString(String json, String key) {
        String marker = "\"" + key + "\":\"";
        int start = json.indexOf(marker);
        if (start < 0) {
            return "";
        }
        StringBuilder value = new StringBuilder();
        for (int i = start + marker.length(); i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                char next = json.charAt(++i);
                switch (next) {
                    case 'n': value.append('\n'); break;
                    case 'r': value.append('\r'); break;
                    case 't': value.append('\t'); break;
                    default: value.append(next);
                }
            } else if (c == '"') {
                break;
            } else {
                value.append(c);
            }
        }
        return value.toString();
    }
    /** Menu option a: the sender and recipient of every stored message. */
public String displaySendersAndRecipients() {
    StringBuilder output = new StringBuilder();

    for (int i = 0; i < flags.size(); i++) {
        if (FLAG_STORED.equals(flags.get(i))) {
            output.append("Sender: ").append(senders.get(i))
                    .append(", Recipient: ").append(recipients.get(i))
                    .append(System.lineSeparator());
        }
    }

    return output.length() == 0
            ? "No stored messages."
            : output.toString().trim();
}
/** Menu option b: the longest stored message. */
public String getLongestStoredMessage() {
    if (storedMessages.isEmpty()) {
        return "No stored messages.";
    }

    String longest = storedMessages.get(0);

    for (String candidate : storedMessages) {
        if (candidate.length() > longest.length()) {
            longest = candidate;
        }
    }

    return longest;
}
/**option c (used by the unit test): the message text that belongs to the ID. */
public String searchByMessageId(String id) {
    int position = messageIds.indexOf(id);

    if (position < 0) {
        return "Message ID not found.";
    }

    return messageTexts.get(position);
}
public String describeMessageById(String id) {
    int position = messageIds.indexOf(id);

    if (position == -1) {
        return "Message ID not found.";
    }

    return "Recipient: " + recipients.get(position)
            + ", Message: " + messageTexts.get(position);
}


/** Menu option d: all messages sent or stored for one recipient. */
public List<String> searchByRecipient(String recipient) {
    List<String> found = new ArrayList<>();

    for (int i = 0; i < recipients.size(); i++) {
        boolean sameRecipient = recipients.get(i).equals(recipient);
        boolean notDisregarded = !FLAG_DISREGARD.equals(flags.get(i));

        if (sameRecipient && notDisregarded) {
            found.add(messageTexts.get(i));
        }
    }

    return found;
}
/** Menu option e: removes the message from every array and, if it was stored, from the JSON file. */
public String deleteByHash(String hash) {
    int position = messageHashes.indexOf(hash);

    if (position < 0) {
        return "Message hash not found.";
    }

    String text = messageTexts.get(position);
    String flag = flags.get(position);

    messageIds.remove(position);
    messageHashes.remove(position);
    senders.remove(position);
    recipients.remove(position);
    messageTexts.remove(position);
    flags.remove(position);

    if (FLAG_SENT.equals(flag)) {
        sentMessages.remove(text);
    } else if (FLAG_DISREGARD.equals(flag)) {
        disregardedMessages.remove(text);
    } else if (FLAG_STORED.equals(flag)) {
        storedMessages.remove(text);
        rewriteStorageFile();
    }

    return "Message: \"" + text + "\" successfully deleted.";
}
/** Rewrites the JSON file so it only holds the messages that are still stored. */
private void rewriteStorageFile() {
    try (FileWriter writer = new FileWriter(storageFile, false)) {
        for (int i = 0; i < flags.size(); i++) {
            if (FLAG_STORED.equals(flags.get(i))) {
                writer.write("{\"messageId\":\"" + Message.escape(messageIds.get(i)) + "\","
                        + "\"sender\":\"" + Message.escape(senders.get(i)) + "\","
                        + "\"recipientCell\":\"" + Message.escape(recipients.get(i)) + "\","
                        + "\"message\":\"" + Message.escape(messageTexts.get(i)) + "\","
                        + "\"messageHash\":\"" + Message.escape(messageHashes.get(i)) + "\"}");
                writer.write(System.lineSeparator());
            }
        }
    } catch (IOException e) {
        System.out.println("Could not update the stored messages file: " + e.getMessage());
    }
}

/** Report of every sent message: Message Hash, Recipient and Message. */
public String getSentMessagesReport() {
    StringBuilder report = new StringBuilder();
    for (int i = 0; i < flags.size(); i++) {
        if (FLAG_SENT.equals(flags.get(i))) {
            report.append("Message Hash: ").append(messageHashes.get(i))
                    .append(", Recipient: ").append(recipients.get(i))
                    .append(", Message: ").append(messageTexts.get(i))
                    .append(System.lineSeparator());
        }
    }
    return report.length() == 0 ? "No sent messages." : report.toString().trim();
}

/** Menu option f: the full details of every stored message. */
public String getStoredMessagesReport() {
    StringBuilder report = new StringBuilder();
    for (int i = 0; i < flags.size(); i++) {
        if (FLAG_STORED.equals(flags.get(i))) {
            report.append("Message ID: ").append(messageIds.get(i))
                    .append(", Message Hash: ").append(messageHashes.get(i))
                    .append(", Sender: ").append(senders.get(i))
                    .append(", Recipient: ").append(recipients.get(i))
                    .append(", Message: ").append(messageTexts.get(i))
                    .append(System.lineSeparator());
        }
    }
    return report.length() == 0 ? "No stored messages." : report.toString().trim();
}

}
