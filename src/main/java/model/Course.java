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
    
    //empty constructor
    public Course() {
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }
    
    //constructor with parameters
    public Course(String courseId, String courseTitle, String courseDescription, String instructorId, List<Student> students, List<Lesson> lessons){
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.instructorId = instructorId;
        this.students = students;
        this.lessons = lessons;
    }
    
    //constructor without students and lessons
    //constructor with parameters
    public Course(String courseId, String courseTitle, String courseDescription, String instructorId){
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.instructorId = instructorId;
        this.students = new ArrayList<>();
        this.lessons = new ArrayList<>();
    }
    
    //setters and getters
    public void setCourseId(String courseId){
        this.courseId = courseId;
    }
    public String getCourseId(){
        return this.courseId;
    }
    public void setCourseTitle(String courseTitle){
        this.courseTitle = courseTitle;
    }
    public String getCourseTitle(){
        return this.courseTitle;
    }
    public void setCourseDescription(String courseDescription){
        this.courseDescription = courseDescription;
    }
    public String getCourseDescription(){
        return this.courseDescription;
    }
    public void setInstructorId(String instructorId){
        this.instructorId = instructorId;
    }
    public String getInstructorId(){
        return this.instructorId;
    }
    public void setStudents(List<Student> students){
        this.students = students;
    }
    public List<Student> getStudents(){
        return this.students;
    }
    public void setLessons(List<Lesson> lessons){
        this.lessons = lessons;
    }
    public List<Lesson> getLessons(){
        return this.lessons;
    }
    
    //add/ remove student
    public void addStudent(Student student){
        this.students.add(student);
    }
    public void removeStudent(Student student){
        this.students.remove(student);
    }
    
    //add/remove lesson
    public void addLesson(Lesson lesson){
        this.lessons.add(lesson);
    }
    public void removeLesson(Lesson lesson){
        this.lessons.remove(lesson);
    }
}
