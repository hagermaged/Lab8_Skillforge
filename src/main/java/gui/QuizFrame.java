/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;
import model.*;
import service.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author orignal store
 */
public class QuizFrame extends javax.swing.JFrame {

    /**
     * Creates new form QuizFrame
     */
     private Quiz quiz;
    private Student student;
    private Course course;
    private Lesson lesson;
    private StudentService studentService;
    private javax.swing.ButtonGroup optionsGroup;
     // Quiz state
    private List<Integer> studentAnswers;
    private int currentQuestionIndex = 0;

    public QuizFrame(Student student, Course course, Lesson lesson) {
        initComponents();
         this.student = student;
        this.course = course;
        this.lesson = lesson;
        this.studentService = new StudentService();
        initializeQuiz();
    }

    private void initializeQuiz() {
          optionsGroup = new javax.swing.ButtonGroup();
    optionsGroup.add(jRadioButton3);
    optionsGroup.add(jRadioButton4);
    optionsGroup.add(jRadioButton5);
    optionsGroup.add(jRadioButton6);
      // Check if student can take the quiz
    if (!canTakeQuiz()) {
        dispose();
        return;
    }
    // Get quiz for the lesson
    this.quiz = studentService.getQuizForLesson(course, lesson);
    
    if (quiz == null) {
        JOptionPane.showMessageDialog(this, 
            "No quiz available for this lesson.", 
            "No Quiz", 
            JOptionPane.INFORMATION_MESSAGE);
        dispose();
        return;
    }
    
    // Initialize student answers list with -1 (no answer)
    studentAnswers = new ArrayList<>();
    for (int i = 0; i < quiz.getQuestions().size(); i++) {
        studentAnswers.add(-1);
    }
    
    displayQuestion(0);
    updateNavigationButtons();
    
    // Update title
    jLabel1.setText("Quiz - " + lesson.getLessonTitle());
}
    private void updateNavigationButtons() {
    jButton1.setEnabled(currentQuestionIndex > 0);
    
    if (currentQuestionIndex == quiz.getQuestions().size() - 1) {
        jButton2.setText("Submit Quiz");
    } else {
        jButton2.setText("Next Question");
    }
}

private void saveCurrentAnswer() {
    int selectedOption = -1;
    
    if (jRadioButton3.isSelected()) selectedOption = 0;
    else if (jRadioButton4.isSelected()) selectedOption = 1;
    else if (jRadioButton5.isSelected()) selectedOption = 2;
    else if (jRadioButton6.isSelected()) selectedOption = 3;
    
    studentAnswers.set(currentQuestionIndex, selectedOption);
}

private void displayQuestion(int questionIndex) {
    if (quiz == null || questionIndex < 0 || questionIndex >= quiz.getQuestions().size()) {
        return;
    }
    
    Question question = quiz.getQuestions().get(questionIndex);
    
    // Update question display
    jLabel1.setText("Question " + (questionIndex + 1) + " of " + quiz.getQuestions().size());
    jTextArea1.setText(question.getQuestionText());
    
    // Update options
    List<String> options = question.getOptions();
    jRadioButton3.setText(options.size() > 0 ? options.get(0) : "");
    jRadioButton4.setText(options.size() > 1 ? options.get(1) : "");
    jRadioButton5.setText(options.size() > 2 ? options.get(2) : "");
    jRadioButton6.setText(options.size() > 3 ? options.get(3) : "");
    
    // Clear selection
    optionsGroup.clearSelection();
    
    // Restore previous answer if exists
    int previousAnswer = studentAnswers.get(questionIndex);
    if (previousAnswer != -1) {
        switch (previousAnswer) {
            case 0: jRadioButton3.setSelected(true); break;
            case 1: jRadioButton4.setSelected(true); break;
            case 2: jRadioButton5.setSelected(true); break;
            case 3: jRadioButton6.setSelected(true); break;
        }
    }
    
    currentQuestionIndex = questionIndex;
    updateNavigationButtons();
}
private boolean canTakeQuiz() {
    // Check if lesson is already completed
    AuthService authService = new AuthService();
    Student currentStudent = (Student) authService.getUserById(student.getUserId());
    
    if (currentStudent != null) {
        List<String> completedLessons = currentStudent.getCompletedLessons(course.getCourseId());
        if (completedLessons != null && completedLessons.contains(lesson.getLessonId())) {
            JOptionPane.showMessageDialog(this,
                "This lesson is already completed. You cannot retake the quiz.",
                "Lesson Completed",
                JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
    }
    return true;
}
private void submitQuiz() {
    // Confirm submission
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to submit the quiz?",
        "Confirm Submission",
        JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        // Submit quiz - THIS AUTOMATICALLY HANDLES LESSON COMPLETION!
        QuizAttempt attempt = studentService.submitQuiz(student, course, lesson, studentAnswers);
        
        if (attempt != null) {
            // Show results
            String resultMessage = attempt.isPassed() ? 
                "Congratulations! You passed the quiz!\n" :
                "Try again! You didn't pass the quiz.\n";
            
            resultMessage += "Score: " + attempt.getScore() + "/" + quiz.getTotalPoints() + 
                           "\nPassing score: " + quiz.getPassingScore();
            
            JOptionPane.showMessageDialog(this,
                resultMessage,
                "Quiz Results",
                attempt.isPassed() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
            
            dispose(); // Close quiz window
        } else {
            JOptionPane.showMessageDialog(this,
                "Error submitting quiz.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(252, 250, 247));
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(800, 600));

        jLabel1.setBackground(new java.awt.Color(252, 250, 247));
        jLabel1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 30, 80));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("....");

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jRadioButton3.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jRadioButton3.setForeground(new java.awt.Color(0, 30, 80));
        jRadioButton3.setText("jRadioButton3");

        jRadioButton4.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jRadioButton4.setForeground(new java.awt.Color(0, 30, 80));
        jRadioButton4.setText("jRadioButton4");

        jRadioButton5.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jRadioButton5.setForeground(new java.awt.Color(0, 30, 80));
        jRadioButton5.setText("jRadioButton5");

        jRadioButton6.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 12)); // NOI18N
        jRadioButton6.setForeground(new java.awt.Color(0, 30, 80));
        jRadioButton6.setText("jRadioButton6");
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton6ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 30, 80));
        jButton1.setText("previous");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 0, 14)); // NOI18N
        jButton2.setText("next");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Yu Gothic UI Semibold", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 0, 0));
        jLabel2.setText("Select an answer :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(107, 107, 107)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(111, 111, 111)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jRadioButton6)
                    .addComponent(jRadioButton5)
                    .addComponent(jRadioButton4)
                    .addComponent(jRadioButton3)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton6)
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(113, 113, 113))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
            saveCurrentAnswer();
    displayQuestion(currentQuestionIndex - 1);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
            saveCurrentAnswer();
    
    if (currentQuestionIndex < quiz.getQuestions().size() - 1) {
        // Go to next question
        displayQuestion(currentQuestionIndex + 1);
    } else {
        // Submit quiz
        submitQuiz();
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
   public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(QuizFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // For testing purposes only - create dummy data
            try {
                // Create dummy objects for testing
                Student dummyStudent = new Student("TEST123", "Test Student", "test@email.com", "password");
                Course dummyCourse = new Course("COURSE001", "Test Course", "Test Description","INSTRUCTOR001");
                Lesson dummyLesson = new Lesson("LESSON001", "Test Lesson", "Test Content");
                
                // Create and show the frame with dummy data
                new QuizFrame(dummyStudent, dummyCourse, dummyLesson).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "This frame requires student, course, and lesson data.\n" +
                    "It should be opened from ViewCourseDetailsFrame.", 
                    "Info", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    });
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
