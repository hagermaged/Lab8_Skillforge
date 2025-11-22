/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author orignal store
 */
public class Admin extends User {
    public Admin() {
        super();
    }
    
    public Admin(String username, String email, String passwordHash, String userId) {
        super(username, email, User.ROLE_ADMIN, userId, passwordHash);
    }
    public String getUserType() {
        return User.ROLE_ADMIN;
    }
    
}
