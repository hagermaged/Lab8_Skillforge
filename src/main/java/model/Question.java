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
public class Question {
   private String questionId;
    private String questionText;
    private List<String> options; // mcq options
    private int correctOptionIndex; // 0-based index (0, 1, 2, 3)
    private int points;
  public Question() {
        this.options = new ArrayList<>();
    }
  
    public Question(String questionId, String questionText, List<String> options, int correctOptionIndex, int points) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.points = points;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        this.correctOptionIndex = correctOptionIndex;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    //Helper method to check if answer is correct
     public boolean isCorrectAnswer(int selectedIndex) {
        return selectedIndex == correctOptionIndex;
    }
}
