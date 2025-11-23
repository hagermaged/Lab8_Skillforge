/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skillforgePackage;

/**
 *
 * @author Hajer1
 */
import db.JsonDatabaseManager;
import model.User;
import service.AuthService;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== User and Login Debug Test ===\n");
        
        JsonDatabaseManager dbManager = new JsonDatabaseManager();
        AuthService authService = new AuthService();
        
        // Test 1: Read and display all users with their password hashes
        System.out.println("1. All Users with Password Hashes:");
        List<User> users = dbManager.readUsers();
        for (User user : users) {
            System.out.println("   - " + user.getUsername() + 
                             " (" + user.getRole() + ") " +
                             "Email: " + user.getEmail() + 
                             " ID: " + user.getUserId());
            System.out.println("     Password Hash: " + user.getPasswordHash());
        }
        
        // Test 2: Test password hashing
        System.out.println("\n2. Password Hashing Test:");
        String testPassword = "password";
        String hashedTest = authService.hashPassword(testPassword);
        System.out.println("   Password 'password' hashes to: " + hashedTest);
        System.out.println("   Expected hash: 5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");
        System.out.println("   Hashes match: " + hashedTest.equals("5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"));
        
        // Test 3: Test login with prof_smith credentials
        System.out.println("\n3. Testing Login with prof_smith:");
        String email = "prof@example.com";
        String password = "password";
        System.out.println("   Email: " + email);
        System.out.println("   Password: " + password);
        
        User loggedInUser = authService.login(email, password);
        if (loggedInUser != null) {
            System.out.println("   ✓ LOGIN SUCCESSFUL: " + loggedInUser.getUsername());
            System.out.println("   Role: " + loggedInUser.getRole());
        } else {
            System.out.println("   ✗ LOGIN FAILED");
            
            // Debug why it failed
            System.out.println("\n4. Login Failure Debug:");
            User userByEmail = authService.findUserByEmail(email);
            if (userByEmail == null) {
                System.out.println("   - User not found with email: " + email);
            } else {
                System.out.println("   - User found: " + userByEmail.getUsername());
                System.out.println("   - Stored hash: " + userByEmail.getPasswordHash());
                System.out.println("   - Input hash: " + authService.hashPassword(password));
                System.out.println("   - Hashes match: " + userByEmail.getPasswordHash().equals(authService.hashPassword(password)));
            }
        }
        
        // Test 4: Test all possible password variations
        System.out.println("\n5. Testing Password Variations:");
        String[] testPasswords = {"password", "Password", "PASSWORD", " password", "password "};
        for (String pwd : testPasswords) {
            User testUser = authService.login(email, pwd);
            System.out.println("   Password '" + pwd + "': " + (testUser != null ? "SUCCESS" : "FAILED"));
        }
        
        // Test 5: Test email variations
        System.out.println("\n6. Testing Email Variations:");
        String[] testEmails = {"prof@example.com", "Prof@example.com", "PROF@example.com", " prof@example.com", "prof@example.com "};
        for (String testEmail : testEmails) {
            User testUser = authService.login(testEmail, password);
            System.out.println("   Email '" + testEmail + "': " + (testUser != null ? "SUCCESS" : "FAILED"));
        }
        
        System.out.println("\n=== Debug Complete ===");
    }
}