package gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgressOverViewWindow extends JFrame {

    public ProgressOverViewWindow(String studentName, int progressPercent) {
        setTitle("Progress Overview");
        setSize(300, 120);
        setLayout(new BorderLayout());

        JLabel label = new JLabel(studentName + " progress: " + progressPercent + "%", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(progressPercent);
        bar.setStringPainted(true);

        add(label, BorderLayout.NORTH);
        add(bar, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
