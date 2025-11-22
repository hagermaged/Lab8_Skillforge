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

public class Lesson {
    //attributes
    private String lessonId;
    private String lessonTitle;
    private String content;
    private List<String> resources;
    private Quiz quiz;
    private boolean isCompleted;
    
    //constructor 1 - without resources
    public Lesson(String lessonId, String lessonTitle, String content){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.content = content;
        this.resources = new ArrayList<>(); //empty list
    }
    
    //constructor 2 - with resources
    public Lesson(String lessonId, String lessonTitle, String content, List<String> resources){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.content = content;
        this.resources = resources;
    }
    //constructor 3 - with quiz
    public Lesson(String lessonId, String lessonTitle, String content, Quiz quiz){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.content = content;
        this.resources = new ArrayList<>();
        this.quiz = quiz; // Set the quiz
        this.isCompleted = false; // Not completed by default
    }
    
    //constructor 4 - with resources and quiz
    public Lesson(String lessonId, String lessonTitle, String content, List<String> resources, Quiz quiz){
        this.lessonId = lessonId;
        this.lessonTitle = lessonTitle;
        this.content = content;
        this.resources = resources;
        this.quiz = quiz; // Set the quiz
        this.isCompleted = false; // Not completed by default
    }
    //setters and getters
    public void setLessonId(String lessonId){
        this.lessonId = lessonId;
    }
    public String getLessonId(){
        return this.lessonId;
    }
    public void setLessonTitle(String lessonTitle){
        this.lessonTitle = lessonTitle;
    }
    public String getLessonTitle(){
        return this.lessonTitle;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
    public void setResources(List<String> resources){
        this.resources = resources;
    }
    public List<String> getResources(){
        return this.resources;
    }
    public Quiz getQuiz() {
        return quiz;
    }
    
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
    
    public boolean isCompleted() {
        return isCompleted;
    }
    
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    //add or remove resource
    public void addResource(String resource){
        this.resources.add(resource);
    }
    public void removeResource(String resource){
        this.resources.remove(resource);
    }
      public boolean hasQuiz() {
        return quiz != null;
    }
}
