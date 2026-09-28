/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.power_chat_application;
import java.util.regex.Pattern;
public class Login {
   
   private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
    
    
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
}
