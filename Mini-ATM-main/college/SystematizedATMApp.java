/*import java.util.Scanner;

public class MiniATM {
    public static void main(String[] args) {
        double balance = 1000.0; // Initial balance
        Scanner scanner = new Scanner(System.in);
        int correctPin = 1234;
        int enteredPin;
        int attempts = 0;
        int maxAttempts = 3;

        // PIN authentication loop with max 3 attempts
        while (attempts < maxAttempts) {
            System.out.print("Enter your 4-digit PIN: ");
            enteredPin = scanner.nextInt();
            if (enteredPin == correctPin) {
                break;
            } else {
                attempts++;
                if (attempts < maxAttempts) {
                    System.out.println("Incorrect PIN. Please try again.\n");
                } else {
                    System.out.println("Incorrect PIN entered 3 times. Access denied.");
                    scanner.close();
                    return;
                }
            }
        }

        while (true) {
            System.out.println("\n=== Welcome to Mini ATM ===");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Your Balance: ₹" + balance);
                    break;

                case 2:
                    System.out.print("Enter amount to deposit: ₹");
                    double deposit = scanner.nextDouble();
                    if (deposit > 0) {
                        balance += deposit;
                        System.out.println("Deposited ₹" + deposit);
                    } else {
                        System.out.println("Invalid amount.");
                    }
                    break;

                case 3:
                    System.out.print("Enter amount to withdraw: ₹");
                    double withdraw = scanner.nextDouble();
                    if (withdraw > 0 && withdraw <= balance) {
                        balance -= withdraw;
                        System.out.println("Withdrawn ₹" + withdraw);
                    } else {
                        System.out.println("Invalid or Insufficient balance.");
                    }
                    break;

                case 4:
                    System.out.println("Thank you for using the ATM!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystematizedATMApp {

    static final String DB_URL = "jdbc:mysql://localhost:3306/atmdb";
    static final String DB_USER = "root"; // Change to your DB username
    static final String DB_PASS = "himanshu"; // Change to your DB password

    static class User {
        String name;
        int pin;
        double balance;

        User(String name, int pin, double balance) {
            this.name = name;
            this.pin = pin;
            this.balance = balance;
        }

        void addTransaction(String message) {
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            String fullMessage = message + " at " + time;
            saveTransactionToDB(fullMessage);
        }

        void saveTransactionToDB(String message) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String sql = "INSERT INTO transactions (pin, message, time) VALUES (?, ?, NOW())";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, pin);
                stmt.setString(2, message);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static User currentUser = null;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ATM Machine");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        CardLayout layout = new CardLayout();
        JPanel mainPanel = new JPanel(layout);

        // Registration Panel
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
        registerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel regTitle = new JLabel("Register New User", JLabel.CENTER);
        regTitle.setFont(new Font("Arial", Font.BOLD, 20));
        regTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField(15);
        JPasswordField pinFieldReg = new JPasswordField(15);
        JTextField balanceField = new JTextField(15);

        JButton registerBtn = new JButton("Register");
        JButton goToLogin = new JButton("Already have an account? Login");

        registerPanel.add(regTitle);
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(createFormField("Name:", nameField));
        registerPanel.add(createFormField("4-digit PIN:", pinFieldReg));
        registerPanel.add(createFormField("Initial Balance:", balanceField));
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(registerBtn);
        registerPanel.add(Box.createVerticalStrut(10));
        registerPanel.add(goToLogin);

        // Login Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel loginTitle = new JLabel("Login", JLabel.CENTER);
        loginTitle.setFont(new Font("Arial", Font.BOLD, 20));
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPasswordField pinFieldLogin = new JPasswordField(15);
        JButton loginBtn = new JButton("Login");
        JButton goToRegister = new JButton("New user? Register");

        loginPanel.add(loginTitle);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(createFormField("Enter 4-digit PIN:", pinFieldLogin));
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(loginBtn);
        loginPanel.add(Box.createVerticalStrut(10));
        loginPanel.add(goToRegister);

        // ATM Panel
        JPanel atmPanel = new JPanel(new BorderLayout(10, 10));
        atmPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel balanceLabel = new JLabel("", JLabel.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel actionPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton checkBtn = new JButton("Check Balance");
        JButton depositBtn = new JButton("Deposit");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton historyBtn = new JButton("View History");
        JButton logoutBtn = new JButton("Logout");

        actionPanel.add(checkBtn);
        actionPanel.add(depositBtn);
        actionPanel.add(withdrawBtn);
        actionPanel.add(historyBtn);
        actionPanel.add(logoutBtn);

        atmPanel.add(welcomeLabel, BorderLayout.NORTH);
        atmPanel.add(balanceLabel, BorderLayout.CENTER);
        atmPanel.add(actionPanel, BorderLayout.SOUTH);

        mainPanel.add(registerPanel, "Register");
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(atmPanel, "ATM");

        frame.add(mainPanel);
        frame.setVisible(true);

        registerBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String pinText = new String(pinFieldReg.getPassword()).trim();
                String balanceText = balanceField.getText().trim();

                if (name.isEmpty() || pinText.isEmpty() || balanceText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required.");
                    return;
                }

                if (!pinText.matches("\\d{4}")) {
                    JOptionPane.showMessageDialog(frame, "PIN must be a 4-digit number.");
                    return;
                }

                int pin = Integer.parseInt(pinText);
                double bal = Double.parseDouble(balanceText);

                if (getUserByPin(pin) != null) {
                    JOptionPane.showMessageDialog(frame, "PIN already exists.");
                } else {
                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                        String sql = "INSERT INTO users (pin, username, balance) VALUES (?, ?, ?)";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setInt(1, pin);
                        stmt.setString(2, name);
                        stmt.setDouble(3, bal);
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Registration successful!");
                        nameField.setText("");
                        pinFieldReg.setText("");
                        balanceField.setText("");
                        layout.show(mainPanel, "Login");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error during registration:\n" + ex.getMessage());
            }
        });

        goToLogin.addActionListener(e -> layout.show(mainPanel, "Login"));
        goToRegister.addActionListener(e -> layout.show(mainPanel, "Register"));

        loginBtn.addActionListener(e -> {
            try {
                int pin = Integer.parseInt(new String(pinFieldLogin.getPassword()));
                currentUser = getUserByPin(pin);
                if (currentUser != null) {
                    welcomeLabel.setText("Welcome, " + currentUser.name + "!");
                    balanceLabel.setText("");
                    layout.show(mainPanel, "ATM");
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid PIN.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        checkBtn.addActionListener(e -> balanceLabel.setText("Balance: ₹" + currentUser.balance));

        depositBtn.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog("Enter deposit amount:");
                double amt = Double.parseDouble(input);
                if (amt > 0) {
                    currentUser.balance += amt;
                    currentUser.addTransaction("Deposited ₹" + amt);
                    updateBalance(currentUser);
                    JOptionPane.showMessageDialog(frame, "Amount deposited successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Amount must be positive.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
            }
        });

        withdrawBtn.addActionListener(e -> {
            try {
                String input = JOptionPane.showInputDialog("Enter withdrawal amount:");
                double amt = Double.parseDouble(input);
                if (amt > 0 && amt <= currentUser.balance) {
                    currentUser.balance -= amt;
                    currentUser.addTransaction("Withdrew ₹" + amt);
                    updateBalance(currentUser);
                    JOptionPane.showMessageDialog(frame, "Withdrawal successful.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient balance or invalid amount.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
            }
        });

        historyBtn.addActionListener(e -> {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String sql = "SELECT message, time FROM transactions WHERE pin = ? ORDER BY time ASC";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, currentUser.pin);
                ResultSet rs = stmt.executeQuery();

                StringBuilder historyBuilder = new StringBuilder();
                while (rs.next()) {
                    String message = rs.getString("message");
                    Timestamp time = rs.getTimestamp("time");
                    historyBuilder.append(message).append(" (").append(time.toString()).append(")").append("\n");
                }

                if (historyBuilder.length() == 0) {
                    JOptionPane.showMessageDialog(frame, "No transactions found.");
                } else {
                    JTextArea textArea = new JTextArea(historyBuilder.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(300, 200));
                    JOptionPane.showMessageDialog(frame, scrollPane, "Transaction History", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error fetching transaction history.");
            }
        });

        logoutBtn.addActionListener(e -> {
            currentUser = null;
            pinFieldLogin.setText("");
            balanceLabel.setText("");
            layout.show(mainPanel, "Login");
        });
    }

    private static JPanel createFormField(String label, JComponent input) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(150, 25));
        input.setPreferredSize(new Dimension(200, 25));
        panel.add(jLabel);
        panel.add(input);
        return panel;
    }

    private static User getUserByPin(int pin) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String query = "SELECT * FROM users WHERE pin = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, pin);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("username");
                double balance = rs.getDouble("balance");
                return new User(name, pin, balance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void updateBalance(User user) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE users SET balance = ? WHERE pin = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, user.balance);
            stmt.setInt(2, user.pin);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
