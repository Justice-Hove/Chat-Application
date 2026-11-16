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

/**
 * Part 3: holds the message arrays and every "Stored Messages" feature.
 *
 * The first six lists are PARALLEL arrays: index i in each of them describes
 * the same message, so a search in one list gives the matching entry in the
 * others. sentMessages, disregardedMessages and storedMessages then hold the
 * message text for each flag.
 */
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

    // Commits 3 to 7 add their methods here, above the final closing brace
}