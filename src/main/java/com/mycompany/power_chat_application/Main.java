package com.mycompany.power_chat_application;

import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Login login = new Login();
        MessageStore store = new MessageStore();
        Scanner scanner = new Scanner(System.in);

        String loggedInUser = null;

        System.out.println("=== Power Chat App ===");

        // ---------- Part 1: register / log in ----------
        while (loggedInUser == null) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.print("Enter first name: ");
                    String firstName = scanner.nextLine().trim();
                    System.out.print("Enter last name: ");
                    String lastName = scanner.nextLine().trim();
                    System.out.print("Enter username (must contain '_' and be <= 5 characters): ");
                    String username = scanner.nextLine().trim();
                    System.out.print("Enter password (8+ chars, capital, number, special char): ");
                    String password = scanner.nextLine().trim();
                    System.out.print("Enter cell phone number (e.g. +27838968976): ");
                    String cell = scanner.nextLine().trim();

                    System.out.println(login.registerUser(username, password, cell, firstName, lastName));
                }
                case "2" -> {
                    System.out.print("Username: ");
                    String username = scanner.nextLine().trim();
                    System.out.print("Password: ");
                    String password = scanner.nextLine().trim();

                    System.out.println(login.returnLoginStatus(username, password));
                    if (login.loginUser(username, password)) {
                        loggedInUser = username;
                    }
                }
                case "3" -> {
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
        }

        // ---------- Part 2 and 3: QuickChat ----------
        System.out.println("Welcome to QuickChat.");

        // Part 3: read the stored JSON file into the arrays when the app starts
        int loadedCount = store.loadStoredMessages();
        if (loadedCount > 0) {
            System.out.println(loadedCount + " stored message(s) loaded from file.");
        }

        int maxMessages = 0;
        while (maxMessages <= 0) {
            System.out.print("How many messages would you like to enter? ");
            try {
                maxMessages = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }

        int messagesEntered = 0;
        Message lastMessage = null;
        boolean running = true;

        while (running) {
            System.out.println("\n1) Send Messages\n2) Show recently sent messages\n3) Quit\n4) Stored Messages");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> {
                    if (messagesEntered >= maxMessages) {
                        System.out.println("You have reached the maximum number of messages you can enter.");
                        break;
                    }

                    System.out.print("Enter recipient cell number (e.g. +27718693002): ");
                    String recipient = scanner.nextLine().trim();
                    if (!Message.isValidRecipientCell(recipient)) {
                        System.out.println("Cell phone number is incorrectly formatted or does not contain "
                                + "an international code. Please correct the number and try again.");
                        break;
                    }

                    System.out.print("Enter your message: ");
                    String text = scanner.nextLine().trim();
                    if (text.isEmpty() || text.length() > Message.MAX_MESSAGE_LENGTH) {
                        System.out.println(text.isEmpty()
                                ? "Please enter a message."
                                : "Message exceeds 250 characters by "
                                        + (text.length() - Message.MAX_MESSAGE_LENGTH)
                                        + "; please reduce the size.");
                        break;
                    }

                    Message message = new Message(recipient, text);
                    message.setSender(loggedInUser);
                    messagesEntered++;

                    System.out.println(message.checkRecipientCell(recipient));
                    System.out.println(message.checkMessageLength(text));

                    String hash = message.createMessageHash();
                    System.out.println(message.getMessageIdStatus());
                    System.out.println("Message Hash: " + hash);

                    System.out.println("\n1) Send Message\n2) Disregard Message\n3) Store Message to send later");
                    System.out.print("Choose an option: ");
                    int sendChoice;
                    try {
                        sendChoice = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        sendChoice = -1;
                    }
                    System.out.println(message.sentMessage(sendChoice));

                    // Part 3: put the message into the arrays that match its flag
                    if (sendChoice == 1) {
                        store.addMessage(message, MessageStore.FLAG_SENT);
                        lastMessage = message;
                        System.out.println("Message ID: " + message.getMessageId()
                                + ", Message Hash: " + message.getMessageHash()
                                + ", Recipient: " + message.getRecipientCell()
                                + ", Message: " + message.getMessageText());
                    } else if (sendChoice == 2) {
                        store.addMessage(message, MessageStore.FLAG_DISREGARD);
                    } else if (sendChoice == 3) {
                        store.addMessage(message, MessageStore.FLAG_STORED);
                    }
                }
                case "2" -> System.out.println("Coming Soon.");
                case "3" -> {
                    running = false;
                    if (lastMessage != null) {
                        System.out.println("Total number of messages sent: " + lastMessage.returnTotalMessagesSent());
                        System.out.println(lastMessage.printMessages());
                    } else {
                        System.out.println("Total number of messages sent: 0");
                    }
                    System.out.println("Goodbye!");
                }
                case "4" -> storedMessagesMenu(scanner, store);
                default -> System.out.println("Invalid option, please choose 1, 2, 3 or 4.");
            }
        }

        scanner.close();
    }

    /** Part 3: the fourth main menu option, "Stored Messages". */
    private static void storedMessagesMenu(Scanner scanner, MessageStore store) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n--- Stored Messages ---"
                    + "\na) Display the sender and recipient of all stored messages"
                    + "\nb) Display the longest stored message"
                    + "\nc) Search for a message ID"
                    + "\nd) Search for all messages stored for a recipient"
                    + "\ne) Delete a message using its message hash"
                    + "\nf) Display a report of all stored messages"
                    + "\ng) Back to the main menu");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim().toLowerCase();

            switch (option) {
                case "a" -> System.out.println(store.displaySendersAndRecipients());
                case "b" -> System.out.println(store.getLongestStoredMessage());
                case "c" -> {
                    System.out.print("Enter the message ID: ");
                    String id = scanner.nextLine().trim();
                    System.out.println(store.describeMessageById(id));
                }
                case "d" -> {
                    System.out.print("Enter the recipient's cell number: ");
                    String recipient = scanner.nextLine().trim();
                    List<String> found = store.searchByRecipient(recipient);
                    if (found.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        for (String text : found) {
                            System.out.println(text);
                        }
                    }
                }
                case "e" -> {
                    System.out.print("Enter the message hash: ");
                    String hash = scanner.nextLine().trim().toUpperCase();
                    System.out.println(store.deleteByHash(hash));
                }
                case "f" -> System.out.println(store.getStoredMessagesReport());
                case "g" -> inMenu = false;
                default -> System.out.println("Invalid option, please choose a to g.");
            }
        }
    }
}