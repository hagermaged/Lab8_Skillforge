import javax.swing.SwingUtilities;

import gui.AnalyticsFrame;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AnalyticsFrame frame = new AnalyticsFrame();
            frame.setVisible(true);
        });
    }
}
