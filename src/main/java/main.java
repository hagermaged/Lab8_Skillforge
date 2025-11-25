import gui.LoginFrame;
/**
 * Main class to start the Learning Management System
 */
public class main {
    public static void main(String[] args) {
        // Start the application with the Login Frame
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}