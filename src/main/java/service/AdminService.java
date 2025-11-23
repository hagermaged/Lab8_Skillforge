/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import db.*;
import model.*;
import java.util.*;
/**
 *
 * @author orignal store
 */
public class AdminService {
      private JsonDatabaseManager db;
    
    public AdminService() {
        this.db = new JsonDatabaseManager();
    }
    
    public List<Course> getPendingCourses() {
        List<Course> allCourses = db.readCourses();
        List<Course> pendingCourses = new ArrayList<>();
        
        for (Course course : allCourses) {
            if ("PENDING".equals(course.getApprovalStatus())) {
                pendingCourses.add(course);
            }
        }
        
        return pendingCourses;
    }
    
    public boolean approveCourse(String courseId, String adminId, String notes) {
        List<Course> courses = db.readCourses();
        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                course.setApprovalStatus("APPROVED");
                course.setAdminReviewerId(adminId);
                course.setReviewNotes(notes);
                course.setReviewDate(java.time.LocalDateTime.now().toString());
                db.writeCourses(courses);
                return true;
            }
        }
        return false;
    }
    
    public boolean rejectCourse(String courseId, String adminId, String reason) {
        List<Course> courses = db.readCourses();
        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                course.setApprovalStatus("REJECTED");
                course.setAdminReviewerId(adminId);
                course.setReviewNotes(reason);
                course.setReviewDate(java.time.LocalDateTime.now().toString());
                db.writeCourses(courses);
                return true;
            }
        }
        return false;
    } 
}
