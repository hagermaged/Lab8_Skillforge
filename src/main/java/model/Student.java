/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {

    private List<String> enrolledCourses;
    private List<String> certificateIds;
    private int progress; // Overall progress percentage
    private Map<String, List<String>> courseProgress; // Course-specific completed lessons

    public Student() {
        super();
        this.enrolledCourses = new ArrayList<>();
        this.progress = 0;
        this.courseProgress = new HashMap<>();
    }

    public Student(String username, String email, String passwordHash, String userId) {
        super(username, email, User.ROLE_STUDENT, userId, passwordHash);
        this.enrolledCourses = new ArrayList<>();
        this.progress = 0;
        this.courseProgress = new HashMap<>();
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Map<String, List<String>> getCourseProgress() {
        return courseProgress;
    }

    public void setCourseProgress(Map<String, List<String>> courseProgress) {
        this.courseProgress = courseProgress;
    }

    public String getUserType() {
        return User.ROLE_STUDENT;
    }

    // Helper methods
    public void enrollInCourse(String courseId) {
        if (!enrolledCourses.contains(courseId)) {
            enrolledCourses.add(courseId);
            // Initialize progress tracking for this course
            courseProgress.putIfAbsent(courseId, new ArrayList<>());
        }
    }

    public void unenrollFromCourse(String courseId) {
        enrolledCourses.remove(courseId);
        courseProgress.remove(courseId);
    }

    public void completeLesson(String courseId, String lessonId) {
        List<String> completedLessons = courseProgress.getOrDefault(courseId, new ArrayList<>());
        if (!completedLessons.contains(lessonId)) {
            completedLessons.add(lessonId);
            courseProgress.put(courseId, completedLessons);
        }
    }

    public List<String> getCompletedLessons(String courseId) {
        return courseProgress.getOrDefault(courseId, new ArrayList<>());
    }

    public int getCourseProgressPercentage(String courseId, int totalLessons) {
        if (totalLessons == 0) {
            return 0;
        }
        List<String> completed = getCompletedLessons(courseId);
        return (completed.size() * 100) / totalLessons;
    }

    public void addCertificate(String certificateId) {
        if (!certificateIds.contains(certificateId)) {
            certificateIds.add(certificateId);
        }
    }

    public boolean removeCertificate(String certificateId) {
        return certificateIds.remove(certificateId);
    }

    public List<String> getCertificateIds() {
        return certificateIds;
    }
}
