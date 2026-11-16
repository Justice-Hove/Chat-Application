# Power Chat Application

## Project Description

Power Chat is a Java-based console messaging application developed using NetBeans and Maven.

The application allows users to register, log in, send messages, store messages for later, and manage stored message records.

## Features

### Part 1: User Registration and Login
- Register using a first name, last name, username, password and South African cell number.
- Validate usernames, passwords and cell phone numbers.
- Log in using registered credentials.

### Part 2: Messaging
- Send messages to valid recipients.
- Validate message length (maximum 250 characters).
- Generate unique message IDs and message hashes.
- Choose to send, disregard or store messages.
- Display sent-message information.

### Part 3: Stored Messages
- Store messages in a JSON file.
- Display the sender and recipient of stored messages.
- Find the longest stored message.
- Search for messages using their IDs or recipient numbers.
- Delete messages using their message hashes.
- Generate sent-message and stored-message reports.
- Reload stored messages when the application starts.

## Technologies Used
- Java
- Apache Maven
- NetBeans IDE
- JUnit 5
- Git and GitHub
- GitHub Actions

## How to Run the Application

1. Clone or download the repository.
2. Open the project in NetBeans.
3. Allow Maven to load the project dependencies.
4. Locate `Main.java` under Source Packages.
5. Right-click the project and select **Run**.
6. Follow the console instructions to register, log in and use the messaging features.

## How to Run Unit Tests

Using NetBeans:
1. Right-click the project.
2. Select **Test**.
3. Wait for Maven to execute the unit tests.
4. Check the output for successful test results.

Alternatively, run the following command from the project directory:

```bash
mvn test
```

## Message Storage

Stored messages are saved in `stored_messages.json` in the project working directory. The application loads previously stored messages when it starts.

The JSON file is excluded from Git tracking through `.gitignore` so that locally stored message data is not committed to the repository.

## Author
Developed as part of the Power Chat messaging application project.

Reference:

Rios, N. (2026) 5 ways to validate phone numbers using Regex, 5 Ways to Validate Phone Numbers Using Regex. Available at: https://www.abstractapi.com/guides/api-functions/phone-number-validation-in-regex (Accessed: 28 September 2026). 

