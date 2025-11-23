/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Hajer1
 */
import java.util.*; //for lists

public class Course {
      private String courseId;
    private String courseTitle;
    private String courseDescription;
    private String instructorId;
    List<Student> students = new ArrayList<>();
    List<Lesson> lessons = new ArrayList<>();
    
    // ==== STUDENT A: ADDED ADMIN APPROVAL FIELDS ====
    private String approvalStatus = "PENDING"; // PENDING, APPROVED, REJECTED
    private String adminReviewerId;
    private String reviewNotes;
    private String reviewDate;
    // ================================================

    //empty constructor
    public Course() {
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.approvalStatus = "PENDING";
    }

    //constructor with parameters
    public Course(String courseId, String courseTitle, String courseDescription, String instructorId, List<Student> students, List<Lesson> lessons) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.instructorId = instructorId;
        this.students = students;
        this.lessons = lessons;
        this.approvalStatus = "PENDING";
    }

    //constructor without students and lessons
    public Course(String courseId, String courseTitle, String courseDescription, String instructorId) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.instructorId = instructorId;
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
        this.approvalStatus = "PENDING";
    }

    //setters and getters
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseTitle() {
        return this.courseTitle;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseDescription() {
        return this.courseDescription;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorId() {
        return this.instructorId;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Student> getStudents() {
        return this.students;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Lesson> getLessons() {
        return this.lessons;
    }

    // ==== STUDENT A: ADDED GETTERS/SETTERS FOR APPROVAL FIELDS ====
    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getAdminReviewerId() {
        return adminReviewerId;
    }

    public void setAdminReviewerId(String adminReviewerId) {
        this.adminReviewerId = adminReviewerId;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }

    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }
    // ==============================================================

    //add/ remove student
    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    //add/remove lesson
    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
    }

    public void removeLesson(Lesson lesson) {
        this.lessons.remove(lesson);
    }
    
    // ==== STUDENT A: OPTIONAL - HELPER METHODS ====
    public boolean isApproved() {
        return "APPROVED".equals(this.approvalStatus);
    }
    
    public boolean isPending() {
        return "PENDING".equals(this.approvalStatus);
    }
    
    public boolean isRejected() {
        return "REJECTED".equals(this.approvalStatus);
    }
    
    // Helper method to approve course
    public void approve(String adminId, String notes) {
        this.approvalStatus = "APPROVED";
        this.adminReviewerId = adminId;
        this.reviewNotes = notes;
        this.reviewDate = java.time.LocalDateTime.now().toString();
    }
    
    // Helper method to reject course
    public void reject(String adminId, String reason) {
        this.approvalStatus = "REJECTED";
        this.adminReviewerId = adminId;
        this.reviewNotes = reason;
        this.reviewDate = java.time.LocalDateTime.now().toString();
    }
}
