package db;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.*;
import service.*;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;

public class CertificateManager {

    private final Path certificatesPath = Paths.get("certificates.json");
    private final JsonDatabaseManager dbManager;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public CertificateManager(JsonDatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    //generate certificate id from IdGenerator class
    public String generateCertificateId() {
        List<Certificate> allCertificates = readAllCertificates();
        return IdGenerator.generateCertificateId(allCertificates);
    }

    //generate the class
    public Certificate generateCertificate(String studentId, String courseId) {
        try {
            User user = dbManager.findUserById(studentId);
            Course course = dbManager.findCourseById(courseId);
            if (!(user instanceof Student) || course == null) {
                return null;
            }
            Student student = (Student) user;
            if (!hasCompletedAllLessons(student, course)) {
                return null;
            }
            if (getStudentCertificateForCourse(studentId, courseId) != null) {
                return null; // certificate already exists
            }
            //creating the certificate
            String certificateId = generateCertificateId();
            Certificate certificate = new Certificate(certificateId, studentId, courseId, new Date(), student.getUsername(), course.getCourseTitle());
            //save certificate
            if (saveCertificate(certificate)) {
                // Update student's certificates list
                updateStudentCertificates(studentId, certificateId);
                return certificate;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //check  if student has completed all lessons and passed all lessons
    private boolean hasCompletedAllLessons(Student student, Course course) {
        List<Lesson> lessons = course.getLessons();

        for (Lesson lesson : lessons) {
            if (lesson.hasQuiz()) {
                String quizId = lesson.getQuiz().getQuizId();
                // check if student passed this quiz
                if (!hasPassedQuiz(student.getUserId(), quizId)) {
                    return false;
                }
            }
        }
        return true;
    }

    //check if student has passed a quiz
    private boolean hasPassedQuiz(String studentId, String quizId) {
        QuizService quizService = new QuizService();
        return quizService.hasPassedQuiz(studentId, quizId, 0);
    }

    //save certificate to the file
    public boolean saveCertificate(Certificate certificate) {
        try {
            List<Certificate> certificates = readAllCertificates();
            certificates.add(certificate);

            try (Writer writer = Files.newBufferedWriter(certificatesPath)) {
                gson.toJson(certificates, writer);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //read all certificates
    public List<Certificate> readAllCertificates() {
        try {
            if (!Files.exists(certificatesPath)) {
                return new ArrayList<>();
            }

            Reader reader = Files.newBufferedReader(certificatesPath);
            Type type = new TypeToken<List<Certificate>>() {
            }.getType();
            List<Certificate> certificates = gson.fromJson(reader, type);

            return certificates != null ? certificates : new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //get certificates for a student
    public List<Certificate> getStudentCertificates(String studentId) {
        List<Certificate> allCertificates = readAllCertificates();
        List<Certificate> studentCertificates = new ArrayList<>();

        for (Certificate cert : allCertificates) {
            if (cert.getStudentId().equals(studentId)) {
                studentCertificates.add(cert);
            }
        }

        return studentCertificates;
    }

    //get specific certificate
    public Certificate getStudentCertificateForCourse(String studentId, String courseId) {
        List<Certificate> studentCertificates = getStudentCertificates(studentId);

        for (Certificate cert : studentCertificates) {
            if (cert.getCourseId().equals(courseId)) {
                return cert;
            }
        }

        return null;
    }

    //update student's certificates
    private void updateStudentCertificates(String studentId, String certificateId) {
        try {
            List<User> users = dbManager.readUsers();

            for (User user : users) {
                if (user.getUserId().equals(studentId) && user instanceof Student) {
                    Student student = (Student) user;
                    student.addCertificate(certificateId);
                    break;
                }
            }

            dbManager.writeUsers(users);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //delete a certificate
    public boolean deleteCertificate(String certificateId) {
        try {
            List<Certificate> certificates = readAllCertificates();
            boolean removed = certificates.removeIf(cert -> cert.getCertificateId().equals(certificateId));

            if (removed) {
                try (Writer writer = Files.newBufferedWriter(certificatesPath)) {
                    gson.toJson(certificates, writer);
                }
                // Also remove from student's certificate list
                removeCertificateFromStudent(certificateId);
            }

            return removed;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //remove a certificate from student
    private void removeCertificateFromStudent(String certificateId) {
        try {
            List<User> users = dbManager.readUsers();
            boolean updated = false;

            for (User user : users) {
                if (user instanceof Student) {
                    Student student = (Student) user;
                    if (student.removeCertificate(certificateId)) {
                        updated = true;
                    }
                }
            }

            if (updated) {
                dbManager.writeUsers(users);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get certificate from the id
    public Certificate getCertificateById(String certificateId) {
        List<Certificate> certificates = readAllCertificates();

        for (Certificate cert : certificates) {
            if (cert.getCertificateId().equals(certificateId)) {
                return cert;
            }
        }

        return null;
    }
}
