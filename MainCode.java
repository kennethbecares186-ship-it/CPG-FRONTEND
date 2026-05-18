import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;


public class MainCode extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    Color PRIMARY = new Color(186, 55, 78);
    Color LIGHT_BG = new Color(248, 240, 240);
    Color WHITE = Color.WHITE;
    Color TEXT = new Color(70, 70, 70);

    public MainCode() {

        setTitle("MoveEat");

        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize immediately

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainContainer.add(createAuthPanel("LOGIN"), "LOGIN_PAGE");
        mainContainer.add(createAuthPanel("SIGNUP"), "SIGNUP_PAGE");

        add(mainContainer);

        // Force fullscreen (maximized) always, except when minimized
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                if ((getExtendedState() & JFrame.MAXIMIZED_BOTH) == 0) {
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            }

            @Override
            public void componentMoved(java.awt.event.ComponentEvent e) {}

            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {}

            @Override
            public void componentHidden(java.awt.event.ComponentEvent e) {}
        });

        addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(java.awt.event.WindowEvent e) {
                if ((e.getNewState() & JFrame.ICONIFIED) == 0) { // Not minimized
                    setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
            }
        });

        setVisible(true);
    }

    private JPanel createAuthPanel(String type) {

        // MAIN PANELL
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_BG);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(PRIMARY);
        leftPanel.setPreferredSize(new Dimension(700, 700));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        leftPanel.add(Box.createVerticalGlue());

        JLabel appTitle = new JLabel("MoveEat");
        appTitle.setForeground(Color.WHITE);
        appTitle.setFont(new Font("SansSerif", Font.BOLD, 55));
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Click, Move, Eat");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 22));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(appTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leftPanel.add(subtitle);

        leftPanel.add(Box.createVerticalGlue());

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(LIGHT_BG);

        // LOGIN CARD
        JPanel formCard = new JPanel();
        formCard.setPreferredSize(new Dimension(500, 600));
        formCard.setBackground(WHITE);
        formCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(
                type.equals("LOGIN") ? "Welcome!" : "Create Account");

        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(PRIMARY);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        formCard.add(title, gbc);

        JLabel smallText = new JLabel(
                type.equals("LOGIN")
                        ? "Login to continue"
                        : "Sign up to get started");

        smallText.setForeground(TEXT);

        gbc.gridy = 1;
        formCard.add(smallText, gbc);

        gbc.gridwidth = 1;

        // USERNAME
        gbc.gridx = 0;
        gbc.gridy = 2;

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        formCard.add(userLabel, gbc);

        JTextField userField = new JTextField(18);
        userField.setPreferredSize(new Dimension(250, 40));

        gbc.gridy = 3;
        gbc.gridwidth = 2;

        formCard.add(userField, gbc);

        // PASSWORD
        gbc.gridy = 4;
        gbc.gridwidth = 1;

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        formCard.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(18);
        passField.setPreferredSize(new Dimension(250, 40));

        gbc.gridy = 5;
        gbc.gridwidth = 2;

        formCard.add(passField, gbc);

        char defaultEchoChar = passField.getEchoChar();
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        showPassword.setForeground(TEXT);
        showPassword.setBackground(WHITE);
        showPassword.addActionListener(e -> passField.setEchoChar(showPassword.isSelected() ? (char)0 : defaultEchoChar));

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 20, 5, 20);
        formCard.add(showPassword, gbc);

        // BUTTON
        JButton submitBtn = new JButton(
                type.equals("LOGIN") ? "LOGIN" : "CREATE ACCOUNT");

        submitBtn.setBackground(PRIMARY);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        submitBtn.setPreferredSize(new Dimension(250, 45));

        gbc.gridy = 7;
        gbc.insets = new Insets(12, 20, 5, 20);
        formCard.add(submitBtn, gbc);

        // SWITCH BUTTON
        JButton switchBtn = new JButton(
                type.equals("LOGIN")
                        ? "Need an account? Sign Up"
                        : "Already have an account? Login");

        switchBtn.setBorderPainted(false);
        switchBtn.setContentAreaFilled(false);
        switchBtn.setForeground(PRIMARY);
        switchBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));

        gbc.gridy = 8;
        gbc.insets = new Insets(5, 20, 5, 20);
        formCard.add(switchBtn, gbc);

        // ADMIN CHECKBOX (only for LOGIN)
        JCheckBox adminCheck = null;
        if (type.equals("LOGIN")) {
            adminCheck = new JCheckBox("Login as Admin");
            adminCheck.setFont(new Font("SansSerif", Font.PLAIN, 14));
            adminCheck.setForeground(TEXT);
            adminCheck.setBackground(WHITE);

            gbc.gridy = 9;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(5, 20, 20, 20);
            formCard.add(adminCheck, gbc);
        }

        rightPanel.add(formCard);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);

        // Action Listeners
        switchBtn.addActionListener(e -> cardLayout.show(mainContainer, type.equals("LOGIN") ? "SIGNUP_PAGE" : "LOGIN_PAGE"));

        final JCheckBox finalAdminCheck = adminCheck;
        submitBtn.addActionListener(e -> {
            String name = userField.getText().trim();
            String pass = new String(passField.getPassword()); // gets the password text
            
            if (name.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Insert your username and password!", "Credential Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calls DatabaseConnection to connect with MySQL
            try (Connection connect = JDBC.getConnection()) {
                if (connect == null) {
                    JOptionPane.showMessageDialog(this, "Database offline! Check XAMPP.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                    return;                                     //May rename the string to "Server Down"
                }

                if (type.equals("LOGIN")) {
                    // LOGIN LOGIC: Checks account existence in DB
                    String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                    PreparedStatement pstmt = connect.prepareStatement(query);
                    pstmt.setString(1, name);
                    pstmt.setString(2, pass);
                    
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        // SUCCESS: Creates an object User based on the data from our db
                        String role = rs.getString("role");
                        boolean isNew = rs.getBoolean("is_new_user");
                        User sessionUser = new User(name, role, isNew);
                        
                        new HomePage(sessionUser); // OPENS DASHBOARD
                        this.dispose();            // CLOSES LOGIN FRAME/PAGE
                    } else {
                        JOptionPane.showMessageDialog(this, "Wrong username or password!", "Login Failed", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    // SIGNUP LOGIC: Saves new user to database
                    String role = (finalAdminCheck != null && finalAdminCheck.isSelected()) ? "ADMIN" : "CUSTOMER";
                    String query = "INSERT INTO users (username, password, role, is_new_user) VALUES (?, ?, ?, ?)";
                    
                    PreparedStatement pstmt = connect.prepareStatement(query);
                    pstmt.setString(1, name);
                    pstmt.setString(2, pass);
                    pstmt.setString(3, role);
                    pstmt.setBoolean(4, true);

                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this, "Account Created! Login again.");
                        cardLayout.show(mainContainer, "LOGIN_PAGE"); //Returns to login frame/page
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "SQL Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainCode::new);
    }
}



// --- Dynamic Home Page Window ---
