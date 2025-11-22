/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import db.JsonDatabaseManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author KimoStore
 */
public class AdminRole extends User {

    private List<String> newcourses;
    private Instructor instructor;
    private Course course;

    public AdminRole() {
        super();
        this.newcourses = new ArrayList<>();
        this.instructor = new Instructor();
        this.course = new Course();
    }

    public AdminRole(String username, String email, String role, String userId, String passwordHash) {
        super(username, email, "Admin", userId, passwordHash);
        this.newcourses = new ArrayList<>();
        this.instructor = new Instructor();
        this.course = new Course();
    }

    public List<String> getNewcourses() {
        return newcourses;
    }

    public void setNewcourses(List<String> newcourses) {
        this.newcourses = newcourses;
    }

    public String getUserType() {
        return "Admin";
    }

    public List<String> viewnewcourses(Instructor i) {
        return i.getCreatedCourses();
    }

    public void approvecourse(Course c, JsonDatabaseManager dbManager) {
        c.setStatus("Approved");
        dbManager.updateCourse(c);
    }

    public void rejectcourse(Course c, JsonDatabaseManager dbManager) {
        c.setStatus("Rejected");
        dbManager.updateCourse(c);
    }

}
