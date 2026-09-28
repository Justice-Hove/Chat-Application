/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.power_chat_application;

public class Login {
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
}
