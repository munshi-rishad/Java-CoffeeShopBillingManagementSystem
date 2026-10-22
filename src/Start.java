import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Entry point of the Coffee Shop Billing Management System.
 *
 * Flow of the app:
 *   Login  ->  Register (optional)  ->  FrameIntro (make your coffee)  ->  PaymentMethod
 *
 * Run this class from the project root folder, because the images are loaded
 * from the "assets" folder using a relative path.
 */
public class Start {

    public static void main(String[] args) {
        // Swing windows should always be created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            if (!new File("assets").isDirectory()) {
                JOptionPane.showMessageDialog(null,
                        "The 'assets' folder was not found.\n"
                        + "Please run the program from the project root folder\n"
                        + "(the folder that contains 'src' and 'assets').",
                        "Images not found", JOptionPane.WARNING_MESSAGE);
            }

            Login login = new Login();
            login.setVisible(true);
        });
    }
}
