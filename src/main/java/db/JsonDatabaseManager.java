/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class JsonDatabaseManager {

    private final Path usersPath = Paths.get("users.json");
    private final Path coursesPath = Paths.get("courses.json");
    private final Path quizAttemptsPath = Paths.get("quiz_attempts.json");
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(User.class, new UserDeserializer())
            .create();

    // Custom deserializer for User polymorphism
    private static class UserDeserializer implements JsonDeserializer<User> {

        @Override
        public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String role = jsonObject.get("role").getAsString();

            if (User.ROLE_STUDENT.equals(role)) {
                return context.deserialize(json, Student.class);
            } else if (User.ROLE_INSTRUCTOR.equals(role)) {
                return context.deserialize(json, Instructor.class);
            } else if(User.ROLE_ADMIN.equals(role)) {
                  return context.deserialize(json, Admin.class);
            }
            else {
                return context.deserialize(json, User.class);
            }
        }
    }

    public List<User> readUsers() {
        try {
            if (!Files.exists(usersPath)) {
                return new ArrayList<>();
            }
            Reader r = Files.newBufferedReader(usersPath);
            Type type = new TypeToken<List<User>>() {
            }.getType();
            return gson.fromJson(r, type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void writeUsers(List<User> users) {
        try (Writer w = Files.newBufferedWriter(usersPath)) {
            gson.toJson(users, w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Course> readCourses() {
        try {
            if (!Files.exists(coursesPath)) {
                return new ArrayList<>();
            }
            Reader r = Files.newBufferedReader(coursesPath);
            Type type = new TypeToken<List<Course>>() {
            }.getType();
            return gson.fromJson(r, type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void writeCourses(List<Course> courses) {
        try (Writer w = Files.newBufferedWriter(coursesPath)) {
            gson.toJson(courses, w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User findUserById(String id) {
        return readUsers().stream().filter(u -> u.getUserId().equals(id)).findFirst().orElse(null);
    }

    public Course findCourseById(String id) {
        return readCourses().stream().filter(c -> c.getCourseId().equals(id)).findFirst().orElse(null);
    }

    public boolean isUserIdUnique(String id) {
        return findUserById(id) == null;
    }

    public boolean isCourseIdUnique(String id) {
        return findCourseById(id) == null;
    }
    // NEW METHOD: Mark lesson as completed for a student

    public boolean markLessonAsCompleted(String studentId, String courseId, String lessonId) {
        try {
            List<User> users = readUsers();
            List<Course> courses = readCourses();

            Student student = null;
            for (User user : users) {
                if (user instanceof Student && user.getUserId().equals(studentId)) {
                    student = (Student) user;
                    break;
                }
            }

            if (student == null) {
                return false;
            }

            Course course = findCourseById(courseId);
            if (course == null) {
                return false;
            }

            boolean lessonExists = course.getLessons().stream()
                    .anyMatch(lesson -> lesson.getLessonId().equals(lessonId));
            if (!lessonExists) {
                return false;
            }

            student.completeLesson(courseId, lessonId);
            updateStudentOverallProgress(student, courses);
            writeUsers(users);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateStudentOverallProgress(Student student, List<Course> courses) {
        int totalLessons = 0;
        int completedLessons = 0;

        for (String enrolledCourseId : student.getEnrolledCourses()) {
            Course enrolledCourse = courses.stream()
                    .filter(c -> c.getCourseId().equals(enrolledCourseId))
                    .findFirst()
                    .orElse(null);

            if (enrolledCourse != null) {
                totalLessons += enrolledCourse.getLessons().size();
                completedLessons += student.getCompletedLessons(enrolledCourseId).size();
            }
        }

        if (totalLessons > 0) {
            int overallProgress = (completedLessons * 100) / totalLessons;
            student.setProgress(overallProgress);
        }
    }
        public List<QuizAttempt> readQuizAttempts() {
        try {
            if (!Files.exists(quizAttemptsPath)) {
                return new ArrayList<>();
            }
            Reader r = Files.newBufferedReader(quizAttemptsPath);
            Type type = new TypeToken<List<QuizAttempt>>() {}.getType();
            return gson.fromJson(r, type);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void writeQuizAttempts(List<QuizAttempt> attempts) {
        try (Writer w = Files.newBufferedWriter(quizAttemptsPath)) {
            gson.toJson(attempts, w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean saveQuizAttempt(QuizAttempt attempt) {
        try {
            List<QuizAttempt> attempts = readQuizAttempts();
            attempts.add(attempt);
            writeQuizAttempts(attempts);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<QuizAttempt> getQuizAttemptsByStudent(String studentId) {
        List<QuizAttempt> allAttempts = readQuizAttempts();
        List<QuizAttempt> studentAttempts = new ArrayList<>();
        
        for (QuizAttempt attempt : allAttempts) {
            if (studentId.equals(attempt.getStudentId())) {
                studentAttempts.add(attempt);
            }
        }
        return studentAttempts;
    }

    public List<QuizAttempt> getQuizAttemptsByQuiz(String quizId) {
        List<QuizAttempt> allAttempts = readQuizAttempts();
        List<QuizAttempt> quizAttempts = new ArrayList<>();
        
        for (QuizAttempt attempt : allAttempts) {
            if (quizId.equals(attempt.getQuizId())) {
                quizAttempts.add(attempt);
            }
        }
        return quizAttempts;
    }

        public List<QuizAttempt> getQuizAttempts(String studentId, String quizId) {
        List<QuizAttempt> allAttempts = readQuizAttempts();
        List<QuizAttempt> result = new ArrayList<>();
        
        for (QuizAttempt attempt : allAttempts) {
            if (studentId.equals(attempt.getStudentId()) && quizId.equals(attempt.getQuizId())) {
                result.add(attempt);
            }
        }
        return result;
    }
    public QuizAttempt findQuizAttemptById(String attemptId) {
        List<QuizAttempt> attempts = readQuizAttempts();
        for (QuizAttempt attempt : attempts) {
            if (attemptId.equals(attempt.getAttemptId())) {
                return attempt;
            }
        }
        return null;
    }

    // ==== STUDENT B: COURSE/LESSON QUIZ METHODS ====
    public boolean addQuizToLesson(String courseId, String lessonId, Quiz quiz) {
        try {
            List<Course> courses = readCourses();
            
            for (Course course : courses) {
                if (courseId.equals(course.getCourseId())) {
                    for (Lesson lesson : course.getLessons()) {
                        if (lessonId.equals(lesson.getLessonId())) {
                            lesson.setQuiz(quiz);
                            writeCourses(courses);
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Quiz getQuizByLesson(String courseId, String lessonId) {
        List<Course> courses = readCourses();
        
        for (Course course : courses) {
            if (courseId.equals(course.getCourseId())) {
                for (Lesson lesson : course.getLessons()) {
                    if (lessonId.equals(lesson.getLessonId()) && lesson.getQuiz() != null) {
                        return lesson.getQuiz();
                    }
                }
            }
        }
        return null;
    }

    public boolean markLessonAsCompletedWithQuiz(String studentId, String courseId, String lessonId, boolean completed) {
        try {
            List<User> users = readUsers();
            List<Course> courses = readCourses();

            Student student = null;
            for (User user : users) {
                if (user instanceof Student && user.getUserId().equals(studentId)) {
                    student = (Student) user;
                    break;
                }
            }

            if (student == null) {
                return false;
            }

            // Update lesson completion in course
            for (Course course : courses) {
                if (courseId.equals(course.getCourseId())) {
                    for (Lesson lesson : course.getLessons()) {
                        if (lessonId.equals(lesson.getLessonId())) {
                            lesson.setCompleted(completed);
                            break;
                        }
                    }
                    break;
                }
            }

            // Also update student progress (existing method)
            if (completed) {
                student.completeLesson(courseId, lessonId);
                updateStudentOverallProgress(student, courses);
            }

            writeUsers(users);
            writeCourses(courses);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
