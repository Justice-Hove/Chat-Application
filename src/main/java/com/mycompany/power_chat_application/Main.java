package com.mycompany.power_chat_application;
        import java.util.Scanner;
                
public class Main {
    
    public static void main(String[] args) {
        Login login = new Login();
        Scanner scanner = new Scanner(System.in);
        boolean running = true; 
        System.out.println("==== Chat Application ====");
        
       while (running) {
           System.out.println("\n1. Register\n2. Login\n3. Exit");
           System.out.println("Choose an Option:");
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

                    String status = login.returnLoginStatus(username, password);
                    System.out.println(status);
                }
                case "3" -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
       }
 
        scanner.close();
    }
}

    
