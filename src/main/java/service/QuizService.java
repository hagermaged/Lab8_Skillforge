/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
    import db.JsonDatabaseManager;
import db.IdGenerator; 
import model.*;
import java.util.*;
/**
 *
 * @author orignal store
 */
public class QuizService {
  private JsonDatabaseManager db;
    
    public QuizService() {
        this.db = new JsonDatabaseManager();
    }
    
    // Submit quiz and calculate score
    public QuizAttempt submitQuiz(Quiz quiz, List<Integer> studentAnswers, String studentId) {
        QuizAttempt attempt = new QuizAttempt();
        
        // Use IdGenerator for attempt ID
        List<QuizAttempt> allAttempts = db.readQuizAttempts();
        attempt.setAttemptId(IdGenerator.generateAttemptId(allAttempts));
        
        attempt.setStudentId(studentId);
        attempt.setQuizId(quiz.getQuizId());
        attempt.setStudentAnswers(new ArrayList<>(studentAnswers));
        
        // Calculate score
        int score = calculateScore(quiz, studentAnswers);
        attempt.setScore(score);
        
        // Check if passed
        boolean passed = score >= quiz.getPassingScore();
        attempt.setPassed(passed);
        
        // Save attempt using database manager
        db.saveQuizAttempt(attempt);
        
        return attempt;
    }
        private int calculateScore(Quiz quiz, List<Integer> studentAnswers) {
        int totalScore = 0;
        List<Question> questions = quiz.getQuestions();
        
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            
            // Check if student answered this question and if it's correct
            if (i < studentAnswers.size()) {
                int studentAnswer = studentAnswers.get(i);
                if (studentAnswer != -1 && question.isCorrectAnswer(studentAnswer)) {
                    totalScore += question.getPoints();
                }
            }
        }
        
        return totalScore;
    }
           // Check if student can take quiz (based on max attempts)
    public boolean canTakeQuiz(String studentId, String quizId, int maxAttempts) {
        List<QuizAttempt> attempts = db.getQuizAttempts(studentId, quizId);
        return attempts.size() < maxAttempts;
    }
    
    // Helper method to get student's best score for a quiz
    public int getBestScore(String studentId, String quizId) {
        List<QuizAttempt> attempts = db.getQuizAttempts(studentId, quizId);
        int bestScore = 0;
        
        for (QuizAttempt attempt : attempts) {
            if (attempt.getScore() > bestScore) {
                bestScore = attempt.getScore();
            }
        }
        
        return bestScore;
    }
    
    // Check if student has passed a quiz
    public boolean hasPassedQuiz(String studentId, String quizId, int passingScore) {
        List<QuizAttempt> attempts = db.getQuizAttempts(studentId, quizId);
        
        for (QuizAttempt attempt : attempts) {
            if (attempt.isPassed()) {
                return true;
            }
        }
        
        return false;
    }
        // Mark lesson as completed after passing quiz
    public boolean completeLessonAfterQuiz(String studentId, String courseId, String lessonId, boolean quizPassed) {
        if (quizPassed) {
            return db.markLessonAsCompletedWithQuiz(studentId, courseId, lessonId, true);
        }
        return false;
    }
}
