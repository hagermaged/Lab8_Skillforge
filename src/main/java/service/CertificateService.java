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

    public boolean generatePDFCertificate(Certificate certificate, String filePath) {
        try {
            // Create a simple text-based PDF representation
            String pdfContent = createPDFContent(certificate);

            java.io.FileWriter writer = new java.io.FileWriter(filePath);
            writer.write(pdfContent);
            writer.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String createPDFContent(Certificate certificate) {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMMM dd, yyyy");
        String formattedDate = dateFormat.format(certificate.getIssueDate());

        return "%PDF-1.4\n"
                + "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n"
                + "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n"
                + "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R >>\nendobj\n"
                + "4 0 obj\n<< /Length 200 >>\nstream\n"
                + "BT\n/F1 24 Tf\n100 700 Td\n(CERTIFICATE OF COMPLETION) Tj\n"
                + "ET\n"
                + "BT\n/F1 18 Tf\n100 650 Td\n(This certifies that) Tj\n"
                + "ET\n"
                + "BT\n/F1 22 Tf\n100 600 Td\n(" + certificate.getStudentName() + ") Tj\n"
                + "ET\n"
                + "BT\n/F1 18 Tf\n100 550 Td\n(has successfully completed) Tj\n"
                + "ET\n"
                + "BT\n/F1 20 Tf\n100 500 Td\n(" + certificate.getCourseName() + ") Tj\n"
                + "ET\n"
                + "BT\n/F1 14 Tf\n100 400 Td\n(Issued on: " + formattedDate + ") Tj\n"
                + "ET\n"
                + "BT\n/F1 12 Tf\n100 350 Td\n(Certificate ID: " + certificate.getCertificateId() + ") Tj\n"
                + "ET\n"
                + "endstream\nendobj\n"
                + "xref\n0 5\n0000000000 65535 f \n0000000009 00000 n \n0000000058 00000 n \n0000000115 00000 n \n0000000234 00000 n \n"
                + "trailer\n<< /Size 5 /Root 1 0 R >>\n"
                + "startxref\n800\n%%EOF";
    }
}
