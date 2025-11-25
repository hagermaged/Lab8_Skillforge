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
public class Quiz {
      private String quizId;
    private String lessonId;
    private List<Question> questions;
    private int passingScore; 
    private int maxAttempts;

    public Quiz(String quizId, String lessonId, List<Question> questions, int passingScore, int maxAttempts) {
        this.quizId = quizId;
        this.lessonId = lessonId;
        this.questions = questions;
        this.passingScore = passingScore;
        this.maxAttempts = maxAttempts;
    }

    public Quiz() {
         this.questions = new ArrayList<>();
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public List<Question> getQuestions() {
           return questions != null ? questions : new ArrayList<>();
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
    //adds a question 
    public void addQuestion(Question question) {
        this.questions.add(question);
    }
    //get total points of all questions 
    public int getTotalPoints() {
        int total = 0;
        for (Question question : questions) {
            total += question.getPoints();
        }
        return total;
    }
}
