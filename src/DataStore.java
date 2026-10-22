import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Very small "database" made of two text files inside the "data" folder:
 *
 *   data/users.txt   one line per account :  email | name | gender | passwordHash
 *   data/orders.txt  one line per order   :  date | email | base | type | brew | sugar | total | payment | location
 *
 * The folder and files are created automatically the first time they are needed.
 * Passwords are never written as plain text, only as a SHA-256 hash.
 * Card numbers, CVV and bKash PIN are never saved.
 */
public final class DataStore {

    private static final Path DATA_DIR = Paths.get("data");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.txt");
    private static final Path ORDERS_FILE = DATA_DIR.resolve("orders.txt");
    private static final String SEPARATOR = " | ";

    /** Email of the person who is logged in right now (used when saving an order). */
    private static String currentUserEmail = "";

    private DataStore() {}

    // ------------------------------------------------------------------
    //  Users
    // ------------------------------------------------------------------

    /** True if an account with this email is already saved. */
    public static boolean userExists(String email) {
        return findUserLine(email) != null;
    }

    /** Saves a new account. Returns false if the file could not be written. */
    public static boolean saveUser(String email, String name, String gender, String password) {
        String line = clean(email) + SEPARATOR + clean(name) + SEPARATOR
                + gender + SEPARATOR + hash(password);
        return appendLine(USERS_FILE, line);
    }

    /** True if the email exists and the password matches the saved one. */
    public static boolean checkLogin(String email, String password) {
        String[] user = findUserLine(email);
        return user != null && user.length >= 4 && user[3].equals(hash(password));
    }

    public static void setCurrentUser(String email) {
        currentUserEmail = email;
    }

    // ------------------------------------------------------------------
    //  Orders
    // ------------------------------------------------------------------

    /** Saves one finished order. Returns false if the file could not be written. */
    public static boolean saveOrder(Order order, String paymentType, String location) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String line = String.join(SEPARATOR,
                date, currentUserEmail, order.base, order.type, order.brew, order.sugar,
                String.format("%.2f Tk", order.total), paymentType, clean(location));
        return appendLine(ORDERS_FILE, line);
    }

    // ------------------------------------------------------------------
    //  File helpers
    // ------------------------------------------------------------------

    private static String[] findUserLine(String email) {
        if (!Files.exists(USERS_FILE)) {
            return null;
        }
        try {
            List<String> lines = Files.readAllLines(USERS_FILE, StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(" \\| ");
                if (parts.length > 0 && parts[0].equalsIgnoreCase(email.trim())) {
                    return parts;
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + USERS_FILE + ": " + e.getMessage());
        }
        return null;
    }

    private static boolean appendLine(Path file, String line) {
        try {
            Files.createDirectories(DATA_DIR);
            Files.write(file, (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            System.out.println("Could not write " + file + ": " + e.getMessage());
            return false;
        }
    }

    /** Removes the separator character and line breaks so one record stays on one line. */
    private static String clean(String text) {
        return text.replace("|", "/").replace("\n", " ").replace("\r", " ").trim();
    }

    /** SHA-256 hash of the password, written as hex text. */
    private static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }

    // ------------------------------------------------------------------
    //  The coffee the customer built on the "Make My Coffee" screen
    // ------------------------------------------------------------------

    public static class Order {
        final String base, type, brew, sugar;
        final double total;

        public Order(String base, String type, String brew, String sugar, double total) {
            this.base = base;
            this.type = type;
            this.brew = brew;
            this.sugar = sugar;
            this.total = total;
        }
    }
}
