package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * ChartFrame - simple JFrame for showing chart panels
 */
public class ChartFrame extends JFrame {
    public ChartFrame(String title, JPanel chartPanel) {
        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
        setSize(800, 520);
        setLocationRelativeTo(null);
    }

    public void showFrame() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }
}
