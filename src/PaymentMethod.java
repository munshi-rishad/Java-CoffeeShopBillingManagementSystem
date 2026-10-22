import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Payment screen.
 * The customer picks bKash, Credit/Debit card or Cash on Delivery, enters a
 * delivery location and presses the "Pay" button.
 * Only the fields of the chosen method are enabled.
 * When the order is confirmed it is saved to data/orders.txt and a receipt is shown.
 */
public class PaymentMethod extends JFrame implements ActionListener, MouseListener {

    private static final Image BACKGROUND =
            new ImageIcon("assets/backgrounds/payment_background.png").getImage();

    private static final ImageIcon BKASH_ICON    = new ImageIcon("assets/icons/bkash_icon.png");
    private static final ImageIcon CARD_ICON     = new ImageIcon("assets/icons/card_icon.png");
    private static final ImageIcon LOCATION_ICON = new ImageIcon("assets/icons/location_icon.png");
    private static final ImageIcon BACK_ICON     = new ImageIcon("assets/icons/back_icon.png");

    private static final Color ORANGE = new Color(255, 146, 105);
    private static final Color GREEN  = new Color(214, 227, 133);

    private static final Font OPTION_FONT = new Font("Cambria", Font.BOLD, 14);
    private static final Font TITLE_FONT  = new Font("Cambria", Font.BOLD, 17);
    private static final Font FIELD_FONT  = new Font("Arial", Font.BOLD, 12);

    private final DataStore.Order order;

    private JPanel panel;
    private JRadioButton cash, bkash, card;
    private JButton confirm, back;

    // Location (needed for every payment method)
    private JLabel locationLabel;
    private JTextField locationField;

    // bKash part (left side)
    private JLabel bkashTitle, mobileLabel, pinLabel;
    private JTextField mobileField;
    private JPasswordField pinField;

    // Card part (right side)
    private JLabel cardTitle, cardNumberLabel, expiryDateLabel, cvvLabel, nameOnCardLabel;
    private JTextField cardNumberField, expiryDateField, nameOnCardField;
    private JPasswordField cvvField;

    public PaymentMethod(DataStore.Order order) {
        super("Payment Method");
        this.order = order;

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

        // ---- Heading ----
        JLabel heading = new JLabel("PAYMENT OPTION", JLabel.CENTER);
        heading.setBounds(0, 8, 800, 70);
        heading.setForeground(ORANGE);
        heading.setFont(new Font("Cambria", Font.BOLD, 40));
        panel.add(heading);

        // ---- Payment options ----
        bkash = createOption("Bkash", 100);
        card  = createOption("Credit/Debit", 335);
        cash  = createOption("Cash on Delivery", 565);

        ButtonGroup options = new ButtonGroup();
        options.add(bkash);
        options.add(card);
        options.add(cash);

        // ---- Location (left, top) ----
        locationLabel = new JLabel("Set Location");
        locationLabel.setIcon(LOCATION_ICON);
        locationLabel.setFont(TITLE_FONT);
        locationLabel.setForeground(Color.BLACK);
        locationLabel.setBounds(130, 182, 350, 30);
        panel.add(locationLabel);

        locationField = new JTextField();
        locationField.setBounds(36, 220, 330, 40);
        panel.add(locationField);

        // ---- bKash (left, bottom) ----
        bkashTitle = new JLabel("bKash Payment");
        bkashTitle.setIcon(BKASH_ICON);
        bkashTitle.setFont(TITLE_FONT);
        bkashTitle.setForeground(new Color(204, 0, 0));
        bkashTitle.setBounds(100, 275, 350, 30);
        panel.add(bkashTitle);

        mobileLabel = createFieldLabel("Mobile Number:", 50, 323, 120);
        mobileField = new JTextField();
        mobileField.setBounds(180, 323, 140, 25);
        panel.add(mobileField);

        pinLabel = createFieldLabel("PIN:", 50, 363, 120);
        pinField = new JPasswordField();
        pinField.setBounds(180, 363, 140, 25);
        panel.add(pinField);

        // ---- Card (right) ----
        cardTitle = new JLabel("Credit Card Payment");
        cardTitle.setIcon(CARD_ICON);
        cardTitle.setFont(TITLE_FONT);
        cardTitle.setForeground(new Color(0, 102, 204));
        cardTitle.setBounds(480, 182, 400, 35);
        panel.add(cardTitle);

        cardNumberLabel = createFieldLabel("Card Number:", 425, 230, 120);
        cardNumberField = createCardField(230);

        expiryDateLabel = createFieldLabel("Expiry Date (MM/YY):", 425, 270, 140);
        expiryDateField = createCardField(270);

        cvvLabel = createFieldLabel("CVV:", 425, 310, 120);
        cvvField = new JPasswordField();
        cvvField.setBounds(550, 310, 160, 25);
        panel.add(cvvField);

        nameOnCardLabel = createFieldLabel("Name on Card:", 425, 350, 120);
        nameOnCardField = createCardField(350);

        // ---- Pay button (shows the amount to pay) ----
        confirm = new JButton(String.format("Pay %.2f Tk", order.total));
        confirm.setBounds(300, 405, 180, 40);
        confirm.setFont(new Font("Arial", Font.BOLD, 18));
        confirm.setBackground(GREEN);
        confirm.setForeground(Color.BLACK);
        confirm.setFocusable(false);
        confirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        confirm.addMouseListener(this);
        confirm.addActionListener(this);
        panel.add(confirm);

        // ---- Back arrow (top-left corner) ----
        back = new JButton(BACK_ICON);
        back.setBounds(0, 0, 25, 25);
        back.setBackground(ORANGE);
        back.setFocusable(false);
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.setToolTipText("Back to Make My Coffee");
        back.addActionListener(this);
        panel.add(back);

        add(panel);

        // Nothing is chosen yet, so every input starts disabled
        showFieldsFor(null);
    }

    // ------------------------------------------------------------------
    //  Small helpers that build the components
    // ------------------------------------------------------------------

    private JRadioButton createOption(String text, int x) {
        JRadioButton option = new JRadioButton(text);
        option.setBounds(x, 128, 200, 30);
        option.setFont(OPTION_FONT);
        option.setFocusable(false);
        option.setOpaque(false);
        option.setContentAreaFilled(false);
        option.setBorderPainted(false);
        option.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        option.addActionListener(this);
        panel.add(option);
        return option;
    }

    private JLabel createFieldLabel(String text, int x, int y, int width) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, 25);
        label.setFont(FIELD_FONT);
        panel.add(label);
        return label;
    }

    private JTextField createCardField(int y) {
        JTextField field = new JTextField();
        field.setBounds(550, y, 160, 25);
        panel.add(field);
        return field;
    }

    // ------------------------------------------------------------------
    //  Enable only the fields of the chosen payment method
    // ------------------------------------------------------------------

    /** method is "bkash", "card", "cash" or null (nothing chosen yet). */
    private void showFieldsFor(String method) {
        boolean isBkash = "bkash".equals(method);
        boolean isCard = "card".equals(method);

        setEnabled(isBkash, bkashTitle, mobileLabel, mobileField, pinLabel, pinField);
        setEnabled(isCard, cardTitle, cardNumberLabel, cardNumberField, expiryDateLabel,
                expiryDateField, cvvLabel, cvvField, nameOnCardLabel, nameOnCardField);
        setEnabled(method != null, locationLabel, locationField, confirm);

        // Clear the details of the methods that are not in use
        if (!isBkash) {
            mobileField.setText("");
            pinField.setText("");
        }
        if (!isCard) {
            cardNumberField.setText("");
            expiryDateField.setText("");
            cvvField.setText("");
            nameOnCardField.setText("");
        }
    }

    private static void setEnabled(boolean enabled, JComponent... components) {
        for (JComponent component : components) {
            component.setEnabled(enabled);
        }
    }

    // ------------------------------------------------------------------
    //  Hover color for the Pay button
    // ------------------------------------------------------------------

    @Override public void mouseClicked(MouseEvent me) {}
    @Override public void mousePressed(MouseEvent me) {}
    @Override public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {
        if (me.getSource() == confirm && confirm.isEnabled()) {
            confirm.setBackground(ORANGE);
        }
    }

    @Override
    public void mouseExited(MouseEvent me) {
        if (me.getSource() == confirm) {
            confirm.setBackground(GREEN);
        }
    }

    // ------------------------------------------------------------------
    //  Clicks
    // ------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == back) {
            dispose();
            new FrameIntro().setVisible(true);
        } else if (source == bkash) {
            showFieldsFor("bkash");
        } else if (source == card) {
            showFieldsFor("card");
        } else if (source == cash) {
            showFieldsFor("cash");
        } else if (source == confirm) {
            confirmOrder();
        }
    }

    /** Checks the fields of the chosen method, then saves the order and shows a receipt. */
    private void confirmOrder() {
        String location = locationField.getText().trim();
        String paymentType;

        if (location.isEmpty()) {
            showError("Please enter your delivery location.");
            return;
        }

        if (bkash.isSelected()) {
            String mobile = mobileField.getText().trim();
            String pin = new String(pinField.getPassword());
            if (mobile.isEmpty() || pin.isEmpty()) {
                showError("Please fill in all the fields for bKash payment.");
                return;
            }
            if (!mobile.matches("01\\d{9}")) {
                showError("Mobile number must be 11 digits and start with 01.");
                return;
            }
            paymentType = "bKash (" + mobile + ")";

        } else if (card.isSelected()) {
            String number = cardNumberField.getText().replace(" ", "");
            String expiry = expiryDateField.getText().trim();
            String cvv = new String(cvvField.getPassword());
            String name = nameOnCardField.getText().trim();
            if (number.isEmpty() || expiry.isEmpty() || cvv.isEmpty() || name.isEmpty()) {
                showError("Please fill in all the fields for Card payment.");
                return;
            }
            if (!number.matches("\\d{12,19}")) {
                showError("Card number should contain only digits (12 to 19 digits).");
                return;
            }
            if (!expiry.matches("(0[1-9]|1[0-2])/\\d{2}")) {
                showError("Expiry date must look like MM/YY, for example 08/27.");
                return;
            }
            if (!cvv.matches("\\d{3,4}")) {
                showError("CVV must be 3 or 4 digits.");
                return;
            }
            // Only the last 4 digits are kept, never the full card number
            paymentType = "Card (**** " + number.substring(number.length() - 4) + ")";

        } else {
            paymentType = "Cash on Delivery";
        }

        int answer = JOptionPane.showConfirmDialog(this, "Do you want to confirm order?",
                "Confirm!!", JOptionPane.OK_CANCEL_OPTION);
        if (answer != JOptionPane.OK_OPTION) {
            return;
        }

        if (!DataStore.saveOrder(order, paymentType, location)) {
            showError("The order could not be saved to data/orders.txt, but it is confirmed.");
        }
        showReceipt(paymentType, location);
    }

    /** Shows a short receipt, then lets the customer order again or close the app. */
    private void showReceipt(String paymentType, String location) {
        String receipt = String.format(
                "Thank you! Your order is confirmed.%n%n"
                + "Coffee   : %s (%s)%n"
                + "Base     : %s%n"
                + "Sugar    : %s%n"
                + "Total    : %.2f Tk%n"
                + "Payment  : %s%n"
                + "Deliver to: %s%n%n"
                + "Do you want to make another coffee?",
                order.brew, order.type, order.base, order.sugar, order.total, paymentType, location);

        int again = JOptionPane.showConfirmDialog(this, receipt, "Receipt",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (again == JOptionPane.YES_OPTION) {
            dispose();
            new FrameIntro().setVisible(true);
        } else {
            System.exit(0);
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
