import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Login screen.
 * The user types the email and password that were saved on the Register screen
 * (accounts are kept in data/users.txt, see DataStore).
 * If they match, the coffee order screen (FrameIntro) opens.
 */
public class Login extends JFrame implements ActionListener {

    // Background image is loaded once, not on every repaint
    private static final Image BACKGROUND =
            new ImageIcon("assets/backgrounds/login_background.png").getImage();

    // Colors and fonts used on this screen
    private static final Color BUTTON_COLOR = new Color(255, 146, 105);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    private JPanel panel;
    private JLabel emailLabel, passwordLabel;
    private JTextField emailTF;
    private JPasswordField passwordTF;
    private JButton loginBtn, registerBtn;

    public Login() {
        super("Login");
        setSize(800, 500);
        setLocationRelativeTo(null);   // open in the middle of the screen
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel that draws the background picture
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(BACKGROUND, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);   // we place every component by x, y position

        // ---- Email ----
        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(230, 150, 100, 30);
        emailLabel.setForeground(Color.BLACK);
        emailLabel.setFont(LABEL_FONT);
        panel.add(emailLabel);

        emailTF = new JTextField();
        emailTF.setBounds(300, 150, 200, 30);
        panel.add(emailTF);

        // ---- Password ----
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(230, 200, 100, 30);
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setFont(LABEL_FONT);
        panel.add(passwordLabel);

        passwordTF = new JPasswordField();
        passwordTF.setBounds(300, 200, 200, 30);
        panel.add(passwordTF);

        // ---- Buttons ----
        loginBtn = createButton("Login", 300, 260, 90);
        registerBtn = createButton("Register", 400, 260, 100);
        panel.add(loginBtn);
        panel.add(registerBtn);

        add(panel);

        // Pressing Enter in the password box works like clicking Login
        getRootPane().setDefaultButton(loginBtn);
    }

    /** Makes an orange button with white bold text. */
    private JButton createButton(String text, int x, int y, int width) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, 30);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == loginBtn) {
            checkLogin();
        } else if (ae.getSource() == registerBtn) {
            dispose();
            new Register().setVisible(true);
        }
    }

    /** Checks what the user typed against the accounts saved in data/users.txt. */
    private void checkLogin() {
        String email = emailTF.getText().trim();
        String password = new String(passwordTF.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            showError("Please Enter your email and password!");
        } else if (!DataStore.userExists(email)) {
            showError("No account found with this email. Please Register first!");
        } else if (!DataStore.checkLogin(email, password)) {
            showError("Incorrect Password!");
            passwordTF.setText("");
        } else {
            DataStore.setCurrentUser(email);
            JOptionPane.showMessageDialog(this, "Login Successful!");
            dispose();
            new FrameIntro().setVisible(true);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
