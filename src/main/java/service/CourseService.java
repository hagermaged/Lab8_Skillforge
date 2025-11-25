/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author Hajer1
 */
import db.*;
import model.*;
import java.util.*;

public class CourseService {

    //attributes
    private JsonDatabaseManager db;
    private List<Course> courses = new ArrayList<>();

    //constructor
    public CourseService() {
        this.db = new JsonDatabaseManager();
        this.courses = db.readCourses();
    }

    public List<Course> getCourses() {
        return this.courses;
    }
 public List<Course> getApprovedCourses() {
        List<Course> approvedCourses = new ArrayList<>();
        for (Course course : courses) {
            if ("APPROVED".equals(course.getApprovalStatus())) {
                approvedCourses.add(course);
            }
        }
        return approvedCourses;
    }
 
    //course methods
    //method1 : create course
    public Course createCourse(String courseTitle, String courseDescription, String instructorId) {
        IdGenerator id = new IdGenerator();
        String courseId = id.generateCourseId(courses);
        Course course = new Course(courseId, courseTitle, courseDescription, instructorId);
        courses.add(course);
        db.writeCourses(courses);
        return course;
    }

    //method2 : edit course (given id -> edit new title, new description)
    public boolean editCourse(String courseId, String newTitle, String newDescription) {
        if (courseId == null || newTitle == null || newDescription == null) {
            return false;
        }

        try {
            // Find in instance list
            Course course = courses.stream()
                    .filter(c -> courseId.equals(c.getCourseId()))
                    .findFirst()
                    .orElse(null);

            if (course != null) {
                course.setCourseTitle(newTitle);
                course.setCourseDescription(newDescription);
                db.writeCourses(courses);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error editing course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //method3: delete course by id
    public boolean deleteCourse(String courseId) {
        try {
            Course course = courses.stream()
                    .filter(c -> courseId.equals(c.getCourseId()))
                    .findFirst()
                    .orElse(null);

            if (course != null) {
                courses.remove(course);
                db.writeCourses(courses);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting course: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //lesson methods
    //method1 : addLesson
    public boolean addLesson(String courseId, String lessonId, String title, String content, String lineResources) {
        try {
            Course course = courses.stream()
                    .filter(c -> courseId.equals(c.getCourseId()))
                    .findFirst()
                    .orElse(null);

            if (course != null) {
                Lesson newLesson = new Lesson(lessonId, title, content);
                addResources(newLesson, lineResources);
                course.addLesson(newLesson);
                db.writeCourses(courses);
                return true;
            }
            return false;

        } catch (Exception e) {
            System.err.println("Error adding lesson: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean editLesson(String courseId, String lessonId, String newLessonTitle) {
        try {
            Course course = courses.stream()
                    .filter(c -> courseId.equals(c.getCourseId()))
                    .findFirst()
                    .orElse(null);

            if (course != null) {
                for (Lesson lesson : course.getLessons()) {
                    if (lesson.getLessonId().equals(lessonId)) {
                        lesson.setLessonTitle(newLessonTitle);
                        db.writeCourses(courses);
                        return true;
                    }
                }
            }
            return false;

        } catch (Exception e) {
            System.err.println("Error updating lesson: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //get lessons for a course
    public List<Lesson> getLessonsForCourse(String courseId) {
        Course course = getCourseById(courseId);
        if (course != null) {
            return course.getLessons();
        }
        return new ArrayList<>();
    }

    public boolean deleteLesson(String courseId, String lessonId) {
        if (courseId == null || lessonId == null) {
            System.err.println("Course ID or Lesson ID cannot be null");
            return false;
        }

        try {
            Course course = courses.stream()
                    .filter(c -> courseId.equals(c.getCourseId()))
                    .findFirst()
                    .orElse(null);

            if (course != null) {
                Lesson lesson = findLessonInCourse(course, lessonId);
                if (lesson != null) {
                    course.getLessons().remove(lesson);
                    db.writeCourses(courses);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting lesson: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //additional method
    private Lesson findLessonInCourse(Course course, String lessonId) {
        if (course == null || course.getLessons() == null) {
            return null;
        }
        return course.getLessons().stream()
                .filter(l -> l != null && l.getLessonId().equals(lessonId))
                .findFirst()
                .orElse(null);
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    public Course getCourseById(String courseId) {
        return db.findCourseById(courseId);
    }

    //enroll student in course
     public boolean enrollStudent(String courseId, Student student) {
        if (student == null) {
            return false;
        }

        Course course = null;
        for (Course c : courses) {
            if (courseId.equals(c.getCourseId())) {
                course = c;
                break;
            }
        }

        
        if (course != null && "APPROVED".equals(course.getApprovalStatus())) {
            course.addStudent(student);
            db.writeCourses(courses);
            return true;
        }
        return false;
    }

    //get resources comma seperated
    public String getResourcesAsCommaSeparated(Lesson lesson) {
        var res = lesson.getResources();
        return (res == null || res.isEmpty()) ? "No resources" : String.join(", ", res);
    }

    //method to take a line of resources comma seperated and add them to lesson
    public void addResources(Lesson lesson, String resourcesLine) {
        if (resourcesLine == null || resourcesLine.trim().isEmpty()) {
            return;
        }
        String[] resourcesArray = resourcesLine.split(",");
        for (String resource : resourcesArray) {
            String trimmedResource = resource.trim();
            if (!trimmedResource.isEmpty()) {
                lesson.addResource(trimmedResource);
            }
        }
    }
    public Course getCourseFromInstance(String courseId) {
        return courses.stream()
                .filter(c -> courseId.equals(c.getCourseId()))
                .findFirst()
                .orElse(null);
    }
    // Add these methods to your existing CourseService class

public boolean addQuizToLesson(String courseId, String lessonId, Quiz quiz) {
    try {
        // Find course in the instance list, not from database
        Course course = courses.stream()
                .filter(c -> courseId.equals(c.getCourseId()))
                .findFirst()
                .orElse(null);
        
        if (course != null) {
            for (Lesson lesson : course.getLessons()) {
                if (lessonId.equals(lesson.getLessonId())) {
                    lesson.setQuiz(quiz);
                    // Write the updated courses list to file
                    db.writeCourses(courses);
                    return true;
                }
            }
        }
        return false;
    } catch (Exception e) {
        System.err.println("Error adding quiz to lesson: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

public boolean removeQuizFromLesson(String courseId, String lessonId) {
    try {
        Course course = getCourseById(courseId);
        if (course != null) {
            for (Lesson lesson : course.getLessons()) {
                if (lessonId.equals(lesson.getLessonId())) {
                    lesson.setQuiz(null);
                    db.writeCourses(courses);
                    return true;
                }
            }
        }
        return false;
    } catch (Exception e) {
        System.err.println("Error removing quiz from lesson: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}

public Quiz getQuizForLesson(String courseId, String lessonId) {
     Course course = courses.stream()
            .filter(c -> courseId.equals(c.getCourseId()))
            .findFirst()
            .orElse(null);
    
    if (course != null) {
        for (Lesson lesson : course.getLessons()) {
            if (lessonId.equals(lesson.getLessonId())) {
                return lesson.getQuiz();
            }
        }
    }
    return null;
}
}
