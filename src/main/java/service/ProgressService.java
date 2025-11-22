package service;

import db.JsonDatabaseManager;
import model.*;
import java.util.*;

public class ProgressService {
    private final JsonDatabaseManager db;

    public ProgressService(JsonDatabaseManager db) {
        this.db = db;
    }

    public ProgressService() {
        this.db = new JsonDatabaseManager();
    }

    public void initializeProgress(String userId, String courseId) {
        List<User> users = db.readUsers();

        for (User u : users) {
            if (u instanceof Student && u.getUserId().equals(userId)) {
                Student s = (Student) u;
                s.enrollInCourse(courseId); // This now initializes progress tracking
                db.writeUsers(users);
                return;
            }
        }
    }

   public void completeLesson(String userId, String courseId, String lessonId) {
    List<User> users = db.readUsers();
    
    for (User u : users) {
        if (u instanceof Student && u.getUserId().equals(userId)) {
            Student s = (Student) u;
            
            // ADD THIS CHECK:
            if (!s.getEnrolledCourses().contains(courseId)) {
                System.out.println("Student not enrolled in course: " + courseId);
                return;
            }
            
            s.completeLesson(courseId, lessonId);
                
                // Update overall progress
                updateOverallProgress(s);
                
                db.writeUsers(users);
                System.out.println("Lesson " + lessonId + " completed for student " + userId);
                return;
            }
        }
        System.out.println("Student not found: " + userId);
    }

    public int getProgress(String userId, String courseId) {
        Course c = db.findCourseById(courseId);
        if (c == null) return 0;

        List<User> users = db.readUsers();
        for (User u : users) {
            if (u instanceof Student && u.getUserId().equals(userId)) {
                Student s = (Student) u;
                return s.getCourseProgressPercentage(courseId, c.getLessons().size());
            }
        }
        return 0;
    }
    
    public int getOverallProgress(String userId) {
        List<User> users = db.readUsers();
        for (User u : users) {
            if (u instanceof Student && u.getUserId().equals(userId)) {
                Student s = (Student) u;
                return s.getProgress();
            }
        }
        return 0;
    }
    
    public boolean enrollInCourse(String userId, String courseId) {
    List<User> users = db.readUsers();
    Course course = db.findCourseById(courseId);
    
    if (course == null) {
        System.out.println("Course not found: " + courseId);
        return false;
    }
    
    for (User u : users) {
        if (u instanceof Student && u.getUserId().equals(userId)) {
            Student s = (Student) u;
            
            // Check both: user thinks they're enrolled AND course confirms it
            boolean userThinksEnrolled = s.getEnrolledCourses().contains(courseId);
            boolean courseConfirmsEnrolled = isStudentInCourse(course, userId);
            
            if (!userThinksEnrolled || !courseConfirmsEnrolled) {
                // Fix the data inconsistency
                if (!userThinksEnrolled) {
                    s.enrollInCourse(courseId);
                    System.out.println("Added course " + courseId + " to student's enrolled courses");
                }
                if (!courseConfirmsEnrolled) {
                    course.addStudent(s);
                    System.out.println("Added student " + userId + " to course " + courseId + " student list");
                }
                
                // Update both databases
                db.writeUsers(users);
                
                // Update courses database
                List<Course> courses = db.readCourses();
                for (Course c : courses) {
                    if (c.getCourseId().equals(courseId)) {
                        // Ensure student is in the course's student list
                        boolean studentExistsInCourse = false;
                        for (Student student : c.getStudents()) {
                            if (student.getUserId().equals(userId)) {
                                studentExistsInCourse = true;
                                break;
                            }
                        }
                        if (!studentExistsInCourse) {
                            c.addStudent(s);
                        }
                        break;
                    }
                }
                db.writeCourses(courses);
                
                System.out.println("Student " + userId + " enrolled in course " + courseId);
                return true;
            } else {
                System.out.println("Student already enrolled in course");
                return false;
            }
        }
    }
    System.out.println("Student not found: " + userId);
    return false;
}

// Helper method to check if student is in course's student list
private boolean isStudentInCourse(Course course, String userId) {
    for (Student student : course.getStudents()) {
        if (student.getUserId().equals(userId)) {
            return true;
        }
    }
    return false;
}
 public List<Course> getEnrolledCourses(String userId) {
    List<Course> enrolledCourses = new ArrayList<>();
    List<User> users = db.readUsers();
    
    for (User u : users) {
        if (u instanceof Student && u.getUserId().equals(userId)) {
            Student s = (Student) u;
            List<Course> allCourses = db.readCourses();
            
            for (Course course : allCourses) {
                // Check both: student thinks they're enrolled AND course confirms it
                if (s.getEnrolledCourses().contains(course.getCourseId()) && 
                    isStudentInCourse(course, userId)) {
                    enrolledCourses.add(course);
                }
            }
            break;
        }
    }
    return enrolledCourses;
}


    
    public Student getStudent(String userId) {
        List<User> users = db.readUsers();
        for (User u : users) {
            if (u instanceof Student && u.getUserId().equals(userId)) {
                return (Student) u;
            }
        }
        return null;
    }
    
    private void updateOverallProgress(Student student) {
        List<Course> allCourses = db.readCourses();
        int totalLessons = 0;
        int completedLessons = 0;
        
        for (String courseId : student.getEnrolledCourses()) {
            Course course = db.findCourseById(courseId);
            if (course != null) {
                totalLessons += course.getLessons().size();
                completedLessons += student.getCompletedLessons(courseId).size();
            }
        }
        
        if (totalLessons > 0) {
            int overallProgress = (completedLessons * 100) / totalLessons;
            student.setProgress(overallProgress);
        }
    }
}
