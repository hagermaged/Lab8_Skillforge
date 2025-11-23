/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author orignal store
 */
public class Instructor extends User {
    private List<String>createdCourses;
    public Instructor()
    {
        super();
        this.createdCourses=new ArrayList<>();
    }
    public Instructor(String username,String email,String passwordHash,String userId)
    {
         super(username,email,User.ROLE_INSTRUCTOR,userId,passwordHash);
         this.createdCourses= new ArrayList<>();
    }

    public List<String> getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(List<String> createdCourses) {
        this.createdCourses = createdCourses;
    }
     
    public String getUserType()
    {
        return User.ROLE_INSTRUCTOR;
    }
    // Helper methods
    public void addCreatedCourse(String courseId) {
        if (!createdCourses.contains(courseId)) {
            createdCourses.add(courseId);
        }
    }

    public void removeCreatedCourse(String courseId) {
        createdCourses.remove(courseId);
    }
}
