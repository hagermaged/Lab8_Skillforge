/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author orignal store
 */
public class QuizAttempt {
    private String attemptId;
    private String studentId;
    private String quizId;
    private LocalDateTime attemptDate;
    private List<Integer> studentAnswers; // Stores selected option index for each question
    private int score;
    private boolean passed;

     public QuizAttempt() {
        this.studentAnswers = new ArrayList<>();
        this.attemptDate = LocalDateTime.now();
    }
       public QuizAttempt(String attemptId, String studentId, String quizId) {
        this();
        this.attemptId = attemptId;
        this.studentId = studentId;
        this.quizId = quizId;
    }

    public String getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(String attemptId) {
        this.attemptId = attemptId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public LocalDateTime getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(LocalDateTime attemptDate) {
        this.attemptDate = attemptDate;
    }

    public List<Integer> getStudentAnswers() {
        return studentAnswers;
    }

    public void setStudentAnswers(List<Integer> studentAnswers) {
        this.studentAnswers = studentAnswers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
       // Helper method to add an answer
    public void addAnswer(int selectedOptionIndex) {
        this.studentAnswers.add(selectedOptionIndex);
    }
    
    // Helper method to get answer for a specific question
    public int getAnswerForQuestion(int questionIndex) {
        if (questionIndex >= 0 && questionIndex < studentAnswers.size()) {
            return studentAnswers.get(questionIndex);
        }
        return -1; // -1 means no answer
    }
}
