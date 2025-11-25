package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import db.JsonDatabaseManager;
import model.Course;
import model.Lesson;
import util.ChartGenerator;

public class AnalyticsFrame extends JFrame {

    private JSplitPane splitPane;
    private JPanel leftPanel, coursePanel, lessonPanel, chartContainer;
    private JList<String> courseList, lessonList;
    private DefaultListModel<String> courseModel, lessonModel;

    private List<Course> courses;
    private JsonDatabaseManager dbManager;

    public AnalyticsFrame() {
        dbManager = new JsonDatabaseManager();
        courses = dbManager.readCourses();
        initComponents();
    }

    private void initComponents() {
        setTitle("Analytics Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);
        getContentPane().add(splitPane);

        // Left panel
        leftPanel = new JPanel(new GridLayout(2, 1));

        // Course panel
        coursePanel = new JPanel(new BorderLayout());
        coursePanel.add(new JLabel("Courses"), BorderLayout.NORTH);
        courseModel = new DefaultListModel<>();
        courseList = new JList<>(courseModel);
        coursePanel.add(new JScrollPane(courseList), BorderLayout.CENTER);
        leftPanel.add(coursePanel);

        // Lesson panel
        lessonPanel = new JPanel(new BorderLayout());
        lessonPanel.add(new JLabel("Lessons"), BorderLayout.NORTH);
        lessonModel = new DefaultListModel<>();
        lessonList = new JList<>(lessonModel);
        lessonPanel.add(new JScrollPane(lessonList), BorderLayout.CENTER);
        leftPanel.add(lessonPanel);

        splitPane.setLeftComponent(leftPanel);

        // Right panel: chart
        chartContainer = new JPanel(new BorderLayout());
        splitPane.setRightComponent(chartContainer);

        loadCourses();

        // Listeners
        courseList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadLessons();
        });

        lessonList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) updateChart();
        });
    }

    private void loadCourses() {
        courseModel.clear();
        if (courses != null) {
            for (Course c : courses) {
                courseModel.addElement(c.getCourseTitle());
            }
        }
    }

    private void loadLessons() {
        int courseIndex = courseList.getSelectedIndex();
        if (courseIndex >= 0) {
            Course selectedCourse = courses.get(courseIndex);
            lessonModel.clear();
            for (Lesson l : selectedCourse.getLessons()) {
                lessonModel.addElement(l.getLessonTitle());
            }
            updateChart(); // show chart for course when selected
        }
    }

    private void updateChart() {
        int courseIndex = courseList.getSelectedIndex();
        if (courseIndex >= 0) {
            Course selectedCourse = courses.get(courseIndex);
            List<String> lessonTitles = new ArrayList<>();
            List<Double> progressValues = new ArrayList<>();

            for (Lesson lesson : selectedCourse.getLessons()) {
                lessonTitles.add(lesson.getLessonTitle());
                progressValues.add(lesson.isCompleted() ? 100.0 : 0.0);
            }

            JPanel chartPanel = ChartGenerator.createBarChartPanel(
                    "Course Progress: " + selectedCourse.getCourseTitle(),
                    lessonTitles,
                    progressValues
            );

            chartContainer.removeAll();
            chartContainer.add(chartPanel, BorderLayout.CENTER);
            chartContainer.revalidate();
            chartContainer.repaint();
        }
    }
}
