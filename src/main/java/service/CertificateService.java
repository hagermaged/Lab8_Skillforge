package service;

import db.*;
import model.*;
import java.util.List;

public class CertificateService {
    private CertificateManager certificateManager;
    private JsonDatabaseManager dbManager;

    public CertificateService() {
        this.dbManager = new JsonDatabaseManager();
        this.certificateManager = new CertificateManager(dbManager);
    }

    //generate a certificate (using certificate manager)
    public Certificate generateCertificate(String studentId, String courseId) {
        return certificateManager.generateCertificate(studentId, courseId);
    }

    //check if student has completed all lessons and is eligible for certificate
    
    public boolean isStudentEligibleForCertificate(String studentId, String courseId) {
        User user = dbManager.findUserById(studentId);
        Course course = dbManager.findCourseById(courseId);
        
        if (!(user instanceof Student) || course == null) {
            return false;
        }
        
        Student student = (Student) user;
        List<String> completedLessons = student.getCompletedLessons(course.getCourseId());
        int totalLessons = course.getLessons().size();
        
        return completedLessons.size() >= totalLessons;
    }

    //get list of certificates for a student
    public List<Certificate> getStudentCertificates(String studentId) {
        return certificateManager.getStudentCertificates(studentId);
    }

    //get specific certificate for a student and course
    public Certificate getStudentCertificateForCourse(String studentId, String courseId) {
        return certificateManager.getStudentCertificateForCourse(studentId, courseId);
    }

    //check if student has already a certificate
    public boolean hasCertificateForCourse(String studentId, String courseId) {
        return certificateManager.getStudentCertificateForCourse(studentId, courseId) != null;
    }

    //get certificate by id
    public Certificate getCertificateById(String certificateId) {
        return certificateManager.getCertificateById(certificateId);
    }
}