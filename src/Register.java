import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Registration screen.
 * The new account is saved in data/users.txt (see DataStore), so it is still
 * there the next time the program is opened.
 */
public class Register extends JFrame implements ActionListener {

    private static final Image BACKGROUND =
            new ImageIcon("assets/backgrounds/registration_background.png").getImage();

    // Colors
    private static final Color RADIO_COLOR = new Color(214, 227, 133);   // light green
    private static final Color CREATE_COLOR = new Color(255, 146, 105);  // orange
    private static final Color BACK_COLOR = new Color(255, 241, 172);    // light yellow

    // Fonts
    private static final Font TITLE_FONT = new Font("Cambria", Font.PLAIN, 20);
    private static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 13);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 15);

    private JPanel panel;
    private JTextField emailTF, nameTF;
    private JPasswordField passwordTF, confirmPassTF;
    private JRadioButton maleBtn, femaleBtn;
    private ButtonGroup genderGroup;
    private JButton registerBtn, backBtn;

    public Register() {
        super("Registration Page");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(BACKGROUND, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);

        // ---- Title ----
        JLabel titleLabel = new JLabel("Registration Page");
        titleLabel.setBounds(520, 15, 300, 50);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(TITLE_FONT);
        panel.add(titleLabel);

        // ---- Text fields (label + box) ----
        addLabel("Email:", 470, 50, 40);
        emailTF = new JTextField();
        emailTF.setBounds(470, 80, 250, 30);
        panel.add(emailTF);

        addLabel("Name:", 470, 110, 30);
        nameTF = new JTextField();
        nameTF.setBounds(470, 135, 250, 30);
        panel.add(nameTF);

        addLabel("Password:", 470, 165, 30);
        passwordTF = new JPasswordField();
        passwordTF.setBounds(470, 190, 250, 30);
        panel.add(passwordTF);

        addLabel("Confirm Password:", 470, 220, 30);
        confirmPassTF = new JPasswordField();
        confirmPassTF.setBounds(470, 245, 250, 30);
        panel.add(confirmPassTF);

        // ---- Gender ----
        addLabel("Gender:", 470, 290, 30);
        maleBtn = createRadio("Male", 530);
        femaleBtn = createRadio("Female", 620);

        genderGroup = new ButtonGroup();
        genderGroup.add(maleBtn);
        genderGroup.add(femaleBtn);

        // ---- Buttons ----
        registerBtn = new JButton("Create account");
        registerBtn.setBounds(470, 345, 250, 30);
        registerBtn.setBackground(CREATE_COLOR);
        registerBtn.setForeground(Color.WHITE);
        registerBtn.setFont(BUTTON_FONT);
        registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerBtn.addActionListener(this);
        panel.add(registerBtn);

        backBtn = new JButton("Already have an account?");
        backBtn.setBounds(470, 390, 250, 30);
        backBtn.setBackground(BACK_COLOR);
        backBtn.setForeground(Color.BLACK);
        backBtn.setFont(SMALL_FONT);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(this);
        panel.add(backBtn);

        add(panel);
    }

    /** Adds a black text label at the given position (width is always 200). */
    private void addLabel(String text, int x, int y, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, height);
        label.setForeground(Color.BLACK);
        label.setFont(LABEL_FONT);
        panel.add(label);
    }

    /** Adds a gender radio button on the gender row. */
    private JRadioButton createRadio(String text, int x) {
        JRadioButton radio = new JRadioButton(text);
        radio.setBounds(x, 290, 70, 30);
        radio.setBackground(RADIO_COLOR);
        radio.setFont(SMALL_FONT);
        radio.setForeground(Color.BLACK);
        panel.add(radio);
        return radio;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == registerBtn) {
            createAccount();
        } else if (ae.getSource() == backBtn) {
            goToLogin();
        }
    }

    /** Checks every field, then saves the account to data/users.txt and goes back to Login. */
    private void createAccount() {
        String email = emailTF.getText().trim();
        String name = nameTF.getText().trim();
        String password = new String(passwordTF.getPassword()).trim();
        String confirmPassword = new String(confirmPassTF.getPassword()).trim();

        if (email.isEmpty()) {
            showWarning("Please enter your email.");
        } else if (!email.contains("@") || !email.contains(".")) {
            showWarning("Please enter a valid email (example: name@mail.com).");
        } else if (DataStore.userExists(email)) {
            showWarning("An account with this email already exists. Please Login.");
        } else if (name.isEmpty()) {
            showWarning("Please enter your name.");
        } else if (password.isEmpty()) {
            showWarning("Please enter your password.");
        } else if (confirmPassword.isEmpty()) {
            showWarning("Please confirm your password.");
        } else if (genderGroup.getSelection() == null) {
            showWarning("Please select your gender.");
        } else if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            passwordTF.setText("");
            confirmPassTF.setText("");
        } else {
            String gender = maleBtn.isSelected() ? "Male" : "Female";
            if (DataStore.saveUser(email, name, gender, password)) {
                JOptionPane.showMessageDialog(this, "Registration Successful!");
                goToLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Could not save your account. Please try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void goToLogin() {
        dispose();
        new Login().setVisible(true);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
