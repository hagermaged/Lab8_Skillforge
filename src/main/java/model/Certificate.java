package model;

import java.util.Date;

public class Certificate {
    private String certificateId;
    private String studentId;
    private String courseId;
    private final Date issueDate;
    private String studentName;
    private String courseName;

    // Constructor with all attributes
    public Certificate(String certificateId, String studentId, String courseId, Date issueDate, String studentName, String courseName) {
        if (certificateId == null || certificateId.trim().isEmpty()) {
            throw new IllegalArgumentException("Certificate ID cannot be null or empty");
        }
        this.certificateId = certificateId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.issueDate = issueDate;
        this.studentName = studentName;
        this.courseName = courseName;
    }

    // Constructor without names
    public Certificate(String certificateId, String studentId, String courseId, Date issueDate) {
        this.certificateId = certificateId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.issueDate = issueDate;
    }

    // Getters
    public String getCertificateId() {
        return this.certificateId;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Date getIssueDate() {
        return this.issueDate;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public String getCourseName() {
        return this.courseName;
    }

    // Setters for the names
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "certificateId='" + certificateId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", issueDate=" + issueDate +
                ", studentName='" + studentName + '\'' +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}