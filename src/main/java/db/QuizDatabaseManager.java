package db;

import java.util.ArrayList;
import java.util.List;

import model.Course;
import model.Lesson;
import model.Quiz;
import model.QuizAttempt;
import model.Student;

/**
 * QuizDatabaseManager (Student C)
 *
 * Thin analytics-focused wrapper around the existing JsonDatabaseManager.
 * Uses only Lists + loops and calls JsonDatabaseManager methods exactly as implemented in your project.
 */
public class QuizDatabaseManager {
    private final JsonDatabaseManager jsonDb;

    public QuizDatabaseManager() {
        this.jsonDb = new JsonDatabaseManager();
    }

    // Return all quiz attempts (delegates)
    public List<QuizAttempt> readAllAttempts() {
        List<QuizAttempt> attempts = jsonDb.readQuizAttempts();
        return (attempts == null) ? new ArrayList<>() : attempts;
    }

    // Save attempt
    public boolean saveAttempt(QuizAttempt attempt) {
        return jsonDb.saveQuizAttempt(attempt);
    }

    // Get attempts by student (delegates to JsonDatabaseManager method name)
    public List<QuizAttempt> getAttemptsByStudent(String studentId) {
        return jsonDb.getQuizAttemptsByStudent(studentId);
    }

    // Get attempts by quiz (delegates)
    public List<QuizAttempt> getAttemptsByQuiz(String quizId) {
        return jsonDb.getQuizAttemptsByQuiz(quizId);
    }

    // Get attempts for specific student & quiz (delegates)
    public List<QuizAttempt> getAttempts(String studentId, String quizId) {
        return jsonDb.getQuizAttempts(studentId, quizId);
    }

    // Get attempts for a lesson (resolve quiz via JsonDatabaseManager.getQuizByLesson)
    public List<QuizAttempt> getAttemptsByLesson(String courseId, String lessonId) {
        List<QuizAttempt> result = new ArrayList<>();
        Quiz quiz = jsonDb.getQuizByLesson(courseId, lessonId);
        if (quiz == null || quiz.getQuizId() == null) return result;
        return getAttemptsByQuiz(quiz.getQuizId());
    }

    // Get attempts for a course: collect quizIds from course lessons then filter attempts
    public List<QuizAttempt> getAttemptsByCourse(String courseId) {
        List<QuizAttempt> result = new ArrayList<>();
        Course course = jsonDb.findCourseById(courseId);
        if (course == null) return result;

        // collect quizIds from lessons
        List<String> quizIds = new ArrayList<>();
        List<Lesson> lessons = course.getLessons();
        if (lessons != null) {
            for (Lesson l : lessons) {
                if (l != null && l.hasQuiz() && l.getQuiz() != null && l.getQuiz().getQuizId() != null) {
                    quizIds.add(l.getQuiz().getQuizId());
                }
            }
        }
        if (quizIds.isEmpty()) return result;

        // iterate all attempts and pick those with quizId in quizIds
        List<QuizAttempt> all = readAllAttempts();
        for (QuizAttempt a : all) {
            if (a == null || a.getQuizId() == null) continue;
            for (String qid : quizIds) {
                if (qid.equals(a.getQuizId())) {
                    result.add(a);
                    break;
                }
            }
        }
        return result;
    }

    // Return Quiz objects for the course (in lesson order)
    public List<Quiz> getQuizzesForCourse(String courseId) {
        List<Quiz> result = new ArrayList<>();
        Course course = jsonDb.findCourseById(courseId);
        if (course == null) return result;
        List<Lesson> lessons = course.getLessons();
        if (lessons == null) return result;
        for (Lesson l : lessons) {
            if (l != null && l.hasQuiz() && l.getQuiz() != null) result.add(l.getQuiz());
        }
        return result;
    }

    // Return enrolled student IDs for course
    public List<String> getEnrolledStudentIdsForCourse(String courseId) {
        List<String> result = new ArrayList<>();
        Course course = jsonDb.findCourseById(courseId);
        if (course == null) return result;
        List<Student> students = course.getStudents();
        if (students == null) return result;
        for (Student s : students) {
            if (s != null && s.getUserId() != null) result.add(s.getUserId());
        }
        return result;
    }

    // Mark lesson completed (uses existing method in JsonDatabaseManager)
    public boolean markLessonCompletedWithQuiz(String studentId, String courseId, String lessonId, boolean completed) {
        return jsonDb.markLessonAsCompletedWithQuiz(studentId, courseId, lessonId, completed);
    }
}
