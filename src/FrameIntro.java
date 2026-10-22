import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * "Make My Coffee" screen.
 * The customer chooses a base, a type, a brew and sugar.
 * "Price" shows the total, "Proceed" opens the payment screen with this order,
 * "Back" goes to Login.
 */
public class FrameIntro extends JFrame implements MouseListener, ActionListener {

    private static final Image BACKGROUND =
            new ImageIcon("assets/backgrounds/coffee_order_background.png").getImage();

    // ---- Menu: what is shown in the drop-downs and what each one costs (Tk) ----
    // Index 0 is the "Select" placeholder, so it has price 0.
    // *_NAMES are the short names written on the receipt and in data/orders.txt.
    private static final String[] BASE_ITEMS  = {"Select", "Water 0.00Tk", "Milk 15.00 Tk", "Vegan Milk 25.00 Tk"};
    private static final String[] BASE_NAMES  = {"", "Water", "Milk", "Vegan Milk"};
    private static final double[] BASE_PRICES = {0.00, 0.00, 15.00, 25.00};

    private static final String[] TYPE_ITEMS = {"Select", "Hot", "Cold", "Iced"};

    private static final String[] BREW_ITEMS  = {"Select", "Espresso 25.00Tk", "Cappuccino 35.00Tk",
                                                 "Americano 15.00Tk", "Latte 55.00Tk"};
    private static final String[] BREW_NAMES  = {"", "Espresso", "Cappuccino", "Americano", "Latte"};
    private static final double[] BREW_PRICES = {0.00, 25.00, 35.00, 15.00, 55.00};

    // ---- Colors ----
    private static final Color GREEN_BUTTON = new Color(214, 227, 133);
    private static final Color PRICE_BUTTON = new Color(255, 241, 172);
    private static final Color PRICE_HOVER  = new Color(255, 255, 0);
    private static final Color SUGAR_COLOR  = new Color(255, 146, 105);

    // ---- Fonts ----
    private static final Font LABEL_FONT   = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT  = new Font("Arial", Font.BOLD, 14);
    private static final Font HEADER_FONT  = new Font("Algerian", Font.BOLD, 35);
    // Kept exactly as in the original design (the name with the space falls back
    // to Java's default italic font, which is how "Slurp Slurp" looks in the screenshots).
    private static final Font SUB_FONT     = new Font("Calibri Light ", Font.ITALIC, 25);
    private static final Font TOTAL_FONT   = new Font("Cambria", Font.PLAIN, 30);

    private JPanel panel;
    private JLabel tpLabel;
    private JButton exitBtn, priceBtn, proceedBtn;
    private JRadioButton sugarYes, sugarNo;
    private ButtonGroup sugarGroup;
    private JComboBox<String> baseCombo, typeCombo, brewCombo;
    private double totalPrice;

    public FrameIntro() {
        super("Make My Coffee");
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
        panel.setOpaque(false);
        panel.setBounds(0, 0, 800, 500);

        // ---- Headings ----
        JLabel headLabel = new JLabel("<html><u>Make My Coffee</u></html>");
        headLabel.setBounds(120, 5, 391, 59);
        headLabel.setForeground(Color.BLACK);
        headLabel.setFont(HEADER_FONT);
        panel.add(headLabel);

        JLabel subLabel = new JLabel("Slurp  Slurp ");
        subLabel.setBounds(387, 45, 551, 41);
        subLabel.setForeground(Color.BLACK);
        subLabel.setFont(SUB_FONT);
        panel.add(subLabel);

        // ---- Left side labels ----
        addLabel("Base:", 103);
        addLabel("Type:", 152);
        addLabel("Brew Type:", 205);
        addLabel("Sugar:", 245);

        // ---- Drop-downs ----
        baseCombo = createCombo(BASE_ITEMS, 105);
        typeCombo = createCombo(TYPE_ITEMS, 153);
        brewCombo = createCombo(BREW_ITEMS, 207);

        // ---- Sugar Yes / No ----
        sugarYes = createSugarRadio("Yes", 245);
        sugarNo = createSugarRadio("NO", 317);
        sugarGroup = new ButtonGroup();
        sugarGroup.add(sugarYes);
        sugarGroup.add(sugarNo);

        // ---- Total price ----
        tpLabel = new JLabel("Total:  0.00Tk");
        tpLabel.setBounds(470, 353, 230, 50);
        tpLabel.setForeground(Color.BLACK);
        tpLabel.setFont(TOTAL_FONT);
        panel.add(tpLabel);

        // ---- Buttons ----
        priceBtn = new JButton("Price");
        priceBtn.setBounds(200, 305, 150, 30);
        priceBtn.setBackground(PRICE_BUTTON);
        priceBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        priceBtn.addActionListener(this);
        priceBtn.addMouseListener(this);
        panel.add(priceBtn);

        exitBtn = createBottomButton("Back", 155);
        proceedBtn = createBottomButton("Proceed", 270);

        add(panel);
    }

    // ------------------------------------------------------------------
    //  Small helpers that build the components
    // ------------------------------------------------------------------

    private void addLabel(String text, int y) {
        JLabel label = new JLabel(text);
        label.setBounds(169, y, 100, 30);
        label.setForeground(Color.BLACK);
        label.setFont(LABEL_FONT);
        panel.add(label);
    }

    private JComboBox<String> createCombo(String[] items, int y) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBounds(245, y, 125, 30);
        combo.setSelectedIndex(0);
        combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(combo);
        return combo;
    }

    private JRadioButton createSugarRadio(String text, int x) {
        JRadioButton radio = new JRadioButton(text);
        radio.setBounds(x, 253, 55, 20);
        radio.setBackground(SUGAR_COLOR);
        radio.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel.add(radio);
        return radio;
    }

    private JButton createBottomButton(String text, int x) {
        JButton button = new JButton(text);
        button.setBounds(x, 395, 95, 30);
        button.setBackground(GREEN_BUTTON);
        button.setFont(BUTTON_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(this);
        button.addActionListener(this);
        panel.add(button);
        return button;
    }

    // ------------------------------------------------------------------
    //  Mouse hover colors
    // ------------------------------------------------------------------

    @Override public void mouseClicked(MouseEvent me) {}
    @Override public void mousePressed(MouseEvent me) {}
    @Override public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {
        if (me.getSource() == proceedBtn) {
            proceedBtn.setBackground(Color.GREEN);
            proceedBtn.setForeground(Color.BLACK);
        } else if (me.getSource() == exitBtn) {
            exitBtn.setBackground(Color.RED);
            exitBtn.setForeground(Color.BLACK);
        } else if (me.getSource() == priceBtn) {
            priceBtn.setBackground(PRICE_HOVER);
            priceBtn.setForeground(Color.BLACK);
        }
    }

    @Override
    public void mouseExited(MouseEvent me) {
        if (me.getSource() == proceedBtn) {
            proceedBtn.setBackground(GREEN_BUTTON);
            proceedBtn.setForeground(Color.BLACK);
        } else if (me.getSource() == exitBtn) {
            exitBtn.setBackground(GREEN_BUTTON);
            exitBtn.setForeground(Color.BLACK);
        } else if (me.getSource() == priceBtn) {
            priceBtn.setBackground(PRICE_BUTTON);
            priceBtn.setForeground(Color.BLACK);
        }
    }

    // ------------------------------------------------------------------
    //  Button clicks
    // ------------------------------------------------------------------

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == priceBtn) {
            if (isOrderComplete()) {
                updateTotal();
            }
        } else if (e.getSource() == exitBtn) {
            dispose();
            new Login().setVisible(true);
        } else if (e.getSource() == proceedBtn) {
            if (isOrderComplete()) {
                updateTotal();
                dispose();
                new PaymentMethod(buildOrder()).setVisible(true);
            }
        }
    }

    /** Returns true when base, sugar, type and brew are all chosen. Otherwise shows a warning. */
    private boolean isOrderComplete() {
        boolean sugarChosen = sugarYes.isSelected() || sugarNo.isSelected();
        boolean allChosen = baseCombo.getSelectedIndex() != 0
                && typeCombo.getSelectedIndex() != 0
                && brewCombo.getSelectedIndex() != 0
                && sugarChosen;

        if (!allChosen) {
            JOptionPane.showMessageDialog(this, "Oops, Customization Incomplete!",
                    "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
        return allChosen;
    }

    /** Total = base price + brew price. */
    private void updateTotal() {
        totalPrice = BASE_PRICES[baseCombo.getSelectedIndex()]
                   + BREW_PRICES[brewCombo.getSelectedIndex()];
        tpLabel.setText(String.format("Total: %.2fTk", totalPrice));
    }

    /** Packs the current choices into an Order for the payment screen. */
    private DataStore.Order buildOrder() {
        return new DataStore.Order(
                BASE_NAMES[baseCombo.getSelectedIndex()],
                (String) typeCombo.getSelectedItem(),
                BREW_NAMES[brewCombo.getSelectedIndex()],
                sugarYes.isSelected() ? "Sugar" : "No Sugar",
                totalPrice);
    }
}
