package service;

import java.util.ArrayList;
import java.util.List;
import model.*;
import db.*;

public class StudentService {
    
    private JsonDatabaseManager jsonDb;
    
    public StudentService() {
        this.jsonDb = new JsonDatabaseManager();
    }
    
    public List<Course> browseAllCourses() {
        return jsonDb.readCourses();
    }
    
    public List<Course> getEnrolledCourses(Student s) {
        List<Course> all = browseAllCourses();
        List<Course> result = new ArrayList<>();
        
        for(Course c : all) {
            if(s.getEnrolledCourses().contains(c.getCourseId())) {
                result.add(c);
            }
        }
        
        return result;
    }
    
    public boolean enrollInCourse(Student s, Course c) {
        if(!s.getEnrolledCourses().contains(c.getCourseId())) {
            s.enrollInCourse(c.getCourseId());
            
            // Add student to course's student list
            c.addStudent(s);
            
            // Update both users and courses in database
            List<User> users = jsonDb.readUsers();
            List<Course> courses = jsonDb.readCourses();
            
            // Update the student in users list
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserId().equals(s.getUserId())) {
                    users.set(i, s);
                    break;
                }
            }
            
            // Update the course in courses list
            for (int i = 0; i < courses.size(); i++) {
                if (courses.get(i).getCourseId().equals(c.getCourseId())) {
                    courses.set(i, c);
                    break;
                }
            }
            
            jsonDb.writeUsers(users);
            jsonDb.writeCourses(courses);
            return true;
        }
        return false;
    }
    
    public List<Lesson> getCourseLessons(Course c) {
        return c.getLessons();
    }
    
    public boolean completeLesson(Student s, Course c, Lesson l) {
        if (!s.getEnrolledCourses().contains(c.getCourseId())) {
            return false; // Student not enrolled in this course
        }
        
        s.completeLesson(c.getCourseId(), l.getLessonId());
        return jsonDb.markLessonAsCompleted(s.getUserId(), c.getCourseId(), l.getLessonId());
    }
    
    public int getCourseProgressPercentage(Student s, Course c) {
        if (!s.getEnrolledCourses().contains(c.getCourseId())) {
            return 0;
        }
        List<String> completedLessons = s.getCompletedLessons(c.getCourseId());
        int totalLessons = c.getLessons().size();
        
        if (totalLessons == 0) return 0;
        return (completedLessons.size() * 100) / totalLessons;
    }
    
    public List<Lesson> getCompletedLessons(Student s, Course c) {
        List<Lesson> completed = new ArrayList<>();
        if (!s.getEnrolledCourses().contains(c.getCourseId())) {
            return completed;
        }
        
        List<String> completedLessonIds = s.getCompletedLessons(c.getCourseId());
        for (Lesson lesson : c.getLessons()) {
            if (completedLessonIds.contains(lesson.getLessonId())) {
                completed.add(lesson);
            }
        }
        return completed;
    }
    
    public List<Lesson> getRemainingLessons(Student s, Course c) {
        List<Lesson> remaining = new ArrayList<>();
        if (!s.getEnrolledCourses().contains(c.getCourseId())) {
            return remaining;
        }
        
        List<String> completedLessonIds = s.getCompletedLessons(c.getCourseId());
        for (Lesson lesson : c.getLessons()) {
            if (!completedLessonIds.contains(lesson.getLessonId())) {
                remaining.add(lesson);
            }
        }
        return remaining;
    }
}