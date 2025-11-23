/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author Hajer1
 */
import model.*;
import db.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AuthService {

    private JsonDatabaseManager dbManager;
    private IdGenerator idGenerator;

    public AuthService() {
        this.dbManager = new JsonDatabaseManager();
        this.idGenerator = new IdGenerator();
    }

    // Email Validation
    private boolean validateEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.~]+@(.+)$");
    }

    // SHA-256 Password Hashing
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public boolean signUp(String username, String email, String password, String role) {
        if (username == null || email == null || password == null
                || username.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            return false;
        }
        if (!validateEmail(email)) {
            return false;
        }
        // Check if email exists
        List<User> users = dbManager.readUsers();
        if (findUserByEmail(email) != null) {
            return false; // Email already exists
        }
        // Create user id
        String userId = IdGenerator.generateUserId(users);
        // Hash password
        String passwordHash = hashPassword(password);
        // Create new user based on role
        User newUser;
        if ("instructor".equalsIgnoreCase(role)) {
            newUser = new Instructor(username, email, passwordHash, userId);
        } else {
            newUser = new Student(username, email, passwordHash, userId);
        }
        users.add(newUser);
        dbManager.writeUsers(users);
        return true;
    }

    public User login(String email, String password) {
        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }

        List<User> users = dbManager.readUsers();
        String passwordHash = hashPassword(password);

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) && user.getPasswordHash().equals(passwordHash)) {
                return user;
            }
        }
        return null;
    }

    public void logout() {
        System.out.println("User logged out");
    }

    // Find user by email
    public User findUserByEmail(String email) {
        List<User> users = dbManager.readUsers();
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }
        return null;
    }
    //find user by id

    public User getUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }

        try {
            List<User> users = dbManager.readUsers();

            for (User user : users) {
                if (userId.equals(user.getUserId())) {
                    // Return the specific type (Student or Instructor) with proper casting
                    if ("student".equals(user.getRole())) {
                        return (Student) user;
                    } else if ("instructor".equals(user.getRole())) {
                        return (Instructor) user;
                    } else {
                        return user; // Generic user
                    }
                }
            }
            return null; // User not found

        } catch (Exception e) {
            System.err.println("Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
