package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import db.JsonDatabaseManager;
import model.Course;
import model.Lesson;
import model.Student;
import service.CourseService;
import service.StudentService;
import util.ChartGenerator;

public class AnalyticsFrame extends JFrame {

    private JSplitPane splitPane;
    private JPanel leftPanel, coursePanel, studentPanel, chartContainer;
    private JList<String> courseList, studentList;
    private DefaultListModel<String> courseModel, studentModel;
    private JButton backButton;

    private List<Course> courses;
    private JsonDatabaseManager dbManager;
    private String instructorId;
    private CourseService courseService;
    private StudentService studentService;

    // Store students for each course
    private Map<String, List<Student>> courseStudentsMap;

    public AnalyticsFrame(String instructorId) {
        this.instructorId = instructorId;
        dbManager = new JsonDatabaseManager();
        courseService = new CourseService();
        studentService = new StudentService();
        courseStudentsMap = new HashMap<>();
        
        // Load only courses for this instructor
        courses = courseService.getCoursesByInstructor(instructorId);
        loadStudentData();
        initComponents();
    }

    private void loadStudentData() {
        // For each course, get enrolled students
        for (Course course : courses) {
            String courseId = course.getCourseId();
            
            // Get students enrolled in this course
            List<Student> enrolledStudents = getStudentsByCourse(courseId);
            courseStudentsMap.put(courseId, enrolledStudents);
        }
    }

    private List<Student> getStudentsByCourse(String courseId) {
        List<Student> allStudents = studentService.getAllStudents();
        List<Student> courseStudents = new ArrayList<>();
        
        for (Student student : allStudents) {
            if (student.getEnrolledCourses() != null && student.getEnrolledCourses().contains(courseId)) {
                courseStudents.add(student);
            }
        }
        return courseStudents;
    }

    private void initComponents() {
        setTitle("Analytics Dashboard - Student Progress");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        // Left panel
        leftPanel = new JPanel(new BorderLayout());

        // Back button
        backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> {
            InstructorDashboardFrame dashboard = new InstructorDashboardFrame(instructorId);
            dashboard.setVisible(true);
            this.dispose();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(backButton, BorderLayout.NORTH);

        // Course and student panels
        JPanel selectionPanel = new JPanel(new GridLayout(2, 1));

        // Course panel
        coursePanel = new JPanel(new BorderLayout());
        coursePanel.add(new JLabel("Your Courses"), BorderLayout.NORTH);
        courseModel = new DefaultListModel<>();
        courseList = new JList<>(courseModel);
        coursePanel.add(new JScrollPane(courseList), BorderLayout.CENTER);
        selectionPanel.add(coursePanel);

        // Student panel
        studentPanel = new JPanel(new BorderLayout());
        studentPanel.add(new JLabel("Enrolled Students"), BorderLayout.NORTH);
        studentModel = new DefaultListModel<>();
        studentList = new JList<>(studentModel);
        studentPanel.add(new JScrollPane(studentList), BorderLayout.CENTER);
        selectionPanel.add(studentPanel);

        topPanel.add(selectionPanel, BorderLayout.CENTER);
        leftPanel.add(topPanel, BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);

        // Right panel: chart
        chartContainer = new JPanel(new BorderLayout());
        splitPane.setRightComponent(chartContainer);

        loadCourses();

        // Listeners
        courseList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadStudentsForCourse();
        });

        studentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) updateStudentProgressChart();
        });
    }

    private void loadCourses() {
        courseModel.clear();
        if (courses != null) {
            for (Course c : courses) {
                courseModel.addElement(c.getCourseTitle() + " (ID: " + c.getCourseId() + ")");
            }
        }
    }

    private void loadStudentsForCourse() {
        int courseIndex = courseList.getSelectedIndex();
        if (courseIndex >= 0) {
            Course selectedCourse = courses.get(courseIndex);
            String courseId = selectedCourse.getCourseId();
            
            studentModel.clear();
            List<Student> students = courseStudentsMap.get(courseId);
            if (students != null) {
                for (Student student : students) {
                    studentModel.addElement(student.getFirstName() + " " + student.getLastName());
                }
            }
            
            // Show overall course statistics when a course is selected
            showCourseOverview(selectedCourse);
        }
    }

    private void showCourseOverview(Course course) {
        String courseId = course.getCourseId();
        List<Student> students = courseStudentsMap.get(courseId);
        
        if (students == null || students.isEmpty()) {
            displayMessage("No students enrolled in this course yet.");
            return;
        }

        // Calculate overall statistics
        int totalStudents = students.size();
        int totalLessons = course.getLessons().size();
        double overallProgress = 0;
        int completedStudents = 0;

        for (Student student : students) {
            double studentProgress = calculateStudentProgress(student, courseId, totalLessons);
            overallProgress += studentProgress;
            if (studentProgress == 100.0) {
                completedStudents++;
            }
        }

        overallProgress = totalStudents > 0 ? overallProgress / totalStudents : 0;

        // Create overview panel
        JPanel overviewPanel = new JPanel(new BorderLayout());
        JLabel overviewLabel = new JLabel(
            "<html><div style='text-align: center;'>" +
            "<h2>Course Overview: " + course.getCourseTitle() + "</h2>" +
            "Total Students: " + totalStudents + "<br>" +
            "Total Lessons: " + totalLessons + "<br>" +
            "Average Progress: " + String.format("%.1f", overallProgress) + "%<br>" +
            "Completed Course: " + completedStudents + " students<br>" +
            "</div></html>"
        );
        overviewLabel.setHorizontalAlignment(JLabel.CENTER);
        overviewPanel.add(overviewLabel, BorderLayout.NORTH);

        // Show progress distribution chart
        List<String> studentNames = new ArrayList<>();
        List<Double> progressValues = new ArrayList<>();

        for (Student student : students) {
            studentNames.add(student.getFirstName() + " " + student.getLastName());
            progressValues.add(calculateStudentProgress(student, courseId, totalLessons));
        }

        JPanel chartPanel = ChartGenerator.createBarChartPanel(
            "Student Progress Distribution - " + course.getCourseTitle(),
            studentNames,
            progressValues
        );

        overviewPanel.add(chartPanel, BorderLayout.CENTER);
        
        chartContainer.removeAll();
        chartContainer.add(overviewPanel, BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private void updateStudentProgressChart() {
        int courseIndex = courseList.getSelectedIndex();
        int studentIndex = studentList.getSelectedIndex();
        
        if (courseIndex >= 0 && studentIndex >= 0) {
            Course selectedCourse = courses.get(courseIndex);
            String courseId = selectedCourse.getCourseId();
            List<Student> students = courseStudentsMap.get(courseId);
            
            if (students != null && studentIndex < students.size()) {
                Student selectedStudent = students.get(studentIndex);
                showIndividualStudentProgress(selectedStudent, selectedCourse);
            }
        }
    }

    private void showIndividualStudentProgress(Student student, Course course) {
        String courseId = course.getCourseId();
        int totalLessons = course.getLessons().size();
        double overallProgress = calculateStudentProgress(student, courseId, totalLessons);
        
        // Create lesson-by-lesson progress
        List<String> lessonTitles = new ArrayList<>();
        List<Double> lessonProgress = new ArrayList<>();
        
        List<String> completedLessons = student.getCompletedLessons(courseId);
        
        for (Lesson lesson : course.getLessons()) {
            lessonTitles.add(lesson.getLessonTitle());
            boolean completed = completedLessons.contains(lesson.getLessonId());
            lessonProgress.add(completed ? 100.0 : 0.0);
        }

        JPanel studentPanel = new JPanel(new BorderLayout());
        
        // Student info
        JLabel studentInfo = new JLabel(
            "<html><div style='text-align: center;'>" +
            "<h2>Student: " + student.getFirstName() + " " + student.getLastName() + "</h2>" +
            "<h3>Course: " + course.getCourseTitle() + "</h3>" +
            "Overall Progress: " + String.format("%.1f", overallProgress) + "%<br>" +
            "Completed Lessons: " + completedLessons.size() + "/" + totalLessons + "<br>" +
            "Email: " + student.getEmail() + "<br>" +
            "</div></html>"
        );
        studentInfo.setHorizontalAlignment(JLabel.CENTER);
        studentPanel.add(studentInfo, BorderLayout.NORTH);

        // Lesson progress chart
        JPanel chartPanel = ChartGenerator.createBarChartPanel(
            "Lesson Completion Progress",
            lessonTitles,
            lessonProgress
        );
        studentPanel.add(chartPanel, BorderLayout.CENTER);
        
        chartContainer.removeAll();
        chartContainer.add(studentPanel, BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }

    private double calculateStudentProgress(Student student, String courseId, int totalLessons) {
        if (totalLessons == 0) return 0.0;
        List<String> completedLessons = student.getCompletedLessons(courseId);
        return ((double) completedLessons.size() / totalLessons) * 100.0;
    }

    private void displayMessage(String message) {
        chartContainer.removeAll();
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'><h3>" + message + "</h3></div></html>");
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        chartContainer.add(messageLabel, BorderLayout.CENTER);
        chartContainer.revalidate();
        chartContainer.repaint();
    }
}