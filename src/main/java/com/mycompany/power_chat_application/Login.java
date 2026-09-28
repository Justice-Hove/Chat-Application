/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.power_chat_application;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class Login {
   
   private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
    
      /*
     * Makes sure that South African mobile number in international format: "+27" followed  by 9 digits.
     * Reference: <(Rios, 2026)>
     */
       private static final Pattern CELLPHONE_PATTERN = Pattern.compile("^\\+27\\d{9}$");

    private final Map<String, User> registeredUsers = new HashMap<>();
    /**
 *
 * this ensures that the username contains an underscore and is <= 5 characters
 */
    public boolean checkUserNsme(String username) {
        if (username == null){
            return false;
        }
        return username.length()<= 5 && username.contains("_");
    }
 /**
     * Ensures that a password is at least eight characters long and
     * contains a capital letter, a number and a special character.
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }


/** Validation, and, if valid, should register as new user
 * 
 */

 public String registerUser(String username, String password, String cellPhoneNumber,
                               String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
           

        User newUser = new User(username, password, cellPhoneNumber, firstName, lastName);
        registeredUsers.put(username, newUser);

        return "Username successfully captured. Password successfully captured. "
                + "Cell phone number successfully added. Registration successful!";
    }
 /* verifying that the provided username and password are matching the previous registered user
*/
 public boolean loginUser(String username, String password){
     if (username == null || password == null){
         return false;
     }
     User user = registeredUsers.get(username);
     return user != null && user.getPassword().equals(password);
 }
 
 /**
  * Returning the messaging for a success of failed login
  */
 public String returnLoginStatus(String username, String password){
     if (loginUser(username, password)) {
         User user = registeredUsers.get(username);
         return "Welcome " + user.getFirstName() + ", " + user.getLastName() + "it is great to see you.";
         
     }
     return "Username or password incorrect, please try again.";
 }
    private boolean checkCellPhoneNumber(String cellPhoneNumber) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private boolean checkUserName(String username) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}