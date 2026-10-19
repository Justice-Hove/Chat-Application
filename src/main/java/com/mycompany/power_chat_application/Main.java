package com.mycompany.power_chat_application;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Login login = new Login();
        Scanner scanner = new Scanner(System.in);

        boolean loggedIn = false;

        System.out.println("=== Power Chat App ===");

        while (!loggedIn) {
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

                    if (login.loginUser(username, password)) {
                        System.out.println(login.returnLoginStatus(username, password));
                        loggedIn = true;
                    } else {
                        System.out.println(login.returnLoginStatus(username, password));
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

        // Part 2
        System.out.println("Welcome to QuickChat.");

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
            System.out.println("\n1) Send Messages\n2) Show recently sent messages\n3) Quit");
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

                    System.out.print("Enter your message: ");
                    String text = scanner.nextLine().trim();

                   Message message = new Message(
                   Message.generateMessageId(), recipient, text);
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

                    if (sendChoice == 1) {
                        System.out.println("Message ID: " + message.getMessageId()
                                + ", Message Hash: " + message.getMessageHash()
                                + ", Recipient: " + message.getRecipientCell()
                                + ", Message: " + message.getMessageText());
                        lastMessage = message;
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
                default -> System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
        }

        scanner.close();
    }
}