package db;

import model.*;
import java.util.*;
import java.util.regex.*;

public class IdGenerator {

    private static int extract(String id, String regex) {
        Matcher m = Pattern.compile(regex).matcher(id);
        if (m.matches()) {
            return Integer.parseInt(m.group(1));
        }
        return 0;
    }

    public static String generateUserId(List<User> users) {
        int max = 0;
        for (User u : users) {
            max = Math.max(max, extract(u.getUserId(), "^U(\\d+)$"));
        }
        return "U" + (max + 1);
    }

    public static String generateCourseId(List<Course> courses) {
        int max = 0;
        for (Course c : courses) {
            max = Math.max(max, extract(c.getCourseId(), "^C(\\d+)$"));
        }
        return "C" + (max + 1);
    }

    public static String generateLessonId(List<Course> courses) {
        int max = 0;
        for (Course c : courses) {
            for (Lesson l : c.getLessons()) {
                max = Math.max(max, extract(l.getLessonId(), "^L(\\d+)$"));
            }
        }
        return "L" + (max + 1);
    }

    public static String generateQuizId(List<Course> courses) {
        int max = 0;
        for (Course course : courses) {
            for (Lesson lesson : course.getLessons()) {
                if (lesson.getQuiz() != null) {
                    max = Math.max(max, extract(lesson.getQuiz().getQuizId(), "^Q(\\d+)$"));
                }
            }
        }
        return "Q" + (max + 1);
    }

    public static String generateQuestionId(List<Course> courses) {
        int max = 0;
        for (Course course : courses) {
            for (Lesson lesson : course.getLessons()) {
                if (lesson.getQuiz() != null) {
                    for (Question question : lesson.getQuiz().getQuestions()) {
                        max = Math.max(max, extract(question.getQuestionId(), "^QU(\\d+)$"));
                    }
                }
            }
        }
        return "QU" + (max + 1);
    }

    public static String generateAttemptId(List<QuizAttempt> attempts) {
        int max = 0;
        for (QuizAttempt attempt : attempts) {
            max = Math.max(max, extract(attempt.getAttemptId(), "^A(\\d+)$"));
        }
        return "A" + (max + 1);
    }

    public static String generateCertificateId(List<Certificate> certificates) {
        int max = 0;
        for (Certificate cert : certificates) {
            max = Math.max(max, extract(cert.getCertificateId(), "^CERT_(\\d+)$"));
        }
        return "CERT_" + (max + 1);
    }
}
