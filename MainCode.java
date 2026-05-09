import java.awt.*;
import javax.swing.*;

// --- Data Model ---
class User {
    String username;
    String role; // "ADMIN" or "CUSTOMER"
    boolean isNewUser; // True if created via signup

    User(String username, String role, boolean isNewUser) {
        this.username = username;
        this.role = role;
        this.isNewUser = isNewUser;
    }
}

// --- Main Application Window ---
public class MainCode extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);

    // Theme colors matching HomePage
    Color PRIMARY = new Color(186, 55, 78);
    Color LIGHT_BG = new Color(248, 240, 240);
    Color CARD = Color.WHITE;
    Color SIDEBAR = new Color(120, 25, 45);

    public MainCode() {
        setTitle("MoveEat");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Add Login and Sign-up panels to the container
        mainContainer.add(createAuthPanel("LOGIN"), "LOGIN_PAGE");
        mainContainer.add(createAuthPanel("SIGNUP"), "SIGNUP_PAGE");

        add(mainContainer);
        setVisible(true);
    }

    private JPanel createAuthPanel(String type) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(LIGHT_BG); // Match homepage background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Increased padding for better spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel(type.equals("LOGIN") ? "User Login" : "Register Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 24)); // Larger, matching homepage style
        title.setForeground(PRIMARY); // Use PRIMARY color for title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        // Input Fields
        gbc.gridwidth = 1;
        gbc.gridy = 1; panel.add(new JLabel("Username:"), gbc);
        JTextField userField = new JTextField(15);
        userField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        gbc.gridx = 1; panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Password:"), gbc);
        JPasswordField passField = new JPasswordField(15);
        passField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        gbc.gridx = 1; panel.add(passField, gbc);

        // Buttons
        JButton submitBtn = new JButton(type.equals("LOGIN") ? "Login" : "Create Account");
        submitBtn.setBackground(PRIMARY); // PRIMARY background
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        submitBtn.setFocusPainted(false);
        submitBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(submitBtn, gbc);

        JButton switchBtn = new JButton(type.equals("LOGIN") ? "Need an account? Sign Up" : "Back to Login");
        switchBtn.setBorderPainted(false);
        switchBtn.setContentAreaFilled(false);
        switchBtn.setForeground(Color.GRAY);
        switchBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        switchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4;
        panel.add(switchBtn, gbc);

        // Action Listeners
        switchBtn.addActionListener(e -> cardLayout.show(mainContainer, type.equals("LOGIN") ? "SIGNUP_PAGE" : "LOGIN_PAGE"));

        submitBtn.addActionListener(e -> {
            String name = userField.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username");
                return;
            }

            // SIMULATED AUTH LOGIC
            String role = name.toLowerCase().contains("admin") ? "ADMIN" : "CUSTOMER";
            boolean isNewUser = type.equals("SIGNUP"); // True for signup
            User sessionUser = new User(name, role, isNewUser);

            new HomePage(sessionUser);
            this.dispose();
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainCode::new);
    }
}

// --- Dynamic Home Page Window ---
class HomePage extends JFrame {
    private User user; // FIX: Added field to store user session

    Color PRIMARY = new Color(186, 55, 78);
    Color LIGHT_BG = new Color(248, 240, 240);
    Color CARD = Color.WHITE;
    Color SIDEBAR = new Color(120, 25, 45);

    public HomePage(User user) {
        this.user = user; // FIX: Initialize user session
        setTitle("MoveEat - " + user.role);
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_BG);

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 700));
        sidebar.setBackground(SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        JLabel logo = new JLabel("MoveEat");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 28));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        addNavButton(sidebar, "Dashboard");

        if (user.role.equals("ADMIN")) {
            addNavButton(sidebar, "Manage Deliveries");
            addNavButton(sidebar, "Fleet Tracking");
            addNavButton(sidebar, "User Management");
        } else {
            addNavButton(sidebar, "Store List");
            addNavButton(sidebar, "Order Food/Items");
            addNavButton(sidebar, "My Order History");
            addNavButton(sidebar, "Delivery Progress");
        }

        sidebar.add(Box.createVerticalGlue());
        addNavButton(sidebar, "Logout");

        // ================= MAIN AREA =================
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // ================= HEADER =================
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LIGHT_BG);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        String greeting = user.isNewUser ? "Hello, " + user.username : "Welcome back, " + user.username; // Change based on signup
        JLabel welcome = new JLabel(greeting);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel subtitle = new JLabel("Manage deliveries and orders easily");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(LIGHT_BG);
        textPanel.add(welcome);
        textPanel.add(subtitle);

        JTextField searchBar = new JTextField(" Search...");
        searchBar.setPreferredSize(new Dimension(220, 40));
        searchBar.setFont(new Font("SansSerif", Font.PLAIN, 14));

        header.add(textPanel, BorderLayout.WEST);
        header.add(searchBar, BorderLayout.EAST);

        // ================= DASHBOARD CONTENT =================
        JPanel content = new JPanel();
        content.setBackground(LIGHT_BG);
        content.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // ===== HERO CARD =====
        JPanel heroCard = new RoundedPanel(30);
        heroCard.setBackground(PRIMARY);
        heroCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        heroCard.setLayout(new BorderLayout());
        heroCard.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JPanel heroText = new JPanel();
        heroText.setOpaque(false);
        heroText.setLayout(new BoxLayout(heroText, BoxLayout.Y_AXIS));

        JLabel heroTitle = new JLabel("Fast & Reliable Delivery");
        heroTitle.setForeground(Color.WHITE);
        heroTitle.setFont(new Font("SansSerif", Font.BOLD, 32));

        JLabel heroDesc = new JLabel("Track orders and manage deliveries in real-time.");
        heroDesc.setForeground(Color.WHITE);
        heroDesc.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JButton heroBtn = new JButton("View Orders");
        heroBtn.setBackground(Color.WHITE);
        heroBtn.setForeground(PRIMARY);
        heroBtn.setFocusPainted(false);
        heroBtn.setPreferredSize(new Dimension(140, 40));

        heroText.add(heroTitle);
        heroText.add(Box.createRigidArea(new Dimension(0, 10)));
        heroText.add(heroDesc);
        heroText.add(Box.createRigidArea(new Dimension(0, 20)));
        heroText.add(heroBtn);

        heroCard.add(heroText, BorderLayout.WEST);
        content.add(heroCard);
        content.add(Box.createRigidArea(new Dimension(0, 25)));

        // ================= STATS CARDS =================
        if (user.role.equals("ADMIN")) {
            JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
            statsPanel.setBackground(LIGHT_BG);
            statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

            statsPanel.add(createStatCard("128", "Active Orders"));
            statsPanel.add(createStatCard("34", "Drivers Online"));
            statsPanel.add(createStatCard("96%", "Success Rate"));

            content.add(statsPanel);
            content.add(Box.createRigidArea(new Dimension(0, 25)));
        }

        // ================= TABLE CARD =================
        JPanel tableCard = new RoundedPanel(25);
        tableCard.setLayout(new BorderLayout());
        tableCard.setBackground(CARD);
        tableCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel tableTitle = new JLabel("Delivery Overview");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        String[] columns = {"Order ID", "Address", "Status", "Driver"};
        Object[][] data;

        if (user.role.equals("ADMIN")) {
            data = new Object[][]{
                {"#501", "Downtown - Central", "Pending", "John Doe"},
                {"#502", "North Park Ave", "In Transit", "Sarah Smith"},
                {"#503", "South Side Mall", "Delivered", "Mike Ross"}
            };
        } else {
            data = new Object[][]{
                {"#502", "North Park Ave (Home)", "In Transit", "Sarah Smith"}
            };
        }

        JTable table = new JTable(data, columns);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        content.add(tableCard);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(content, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void addNavButton(JPanel panel, String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(80, 80, 80));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));

        if (text.equals("Logout")) {
            btn.setBackground(new Color(255, 230, 230));
            btn.setForeground(Color.RED);
            btn.addActionListener(e -> {
                new MainCode();
                this.dispose();
            });
        } else if (text.equals("Store List")) {
            btn.addActionListener(e -> {
                // FIX: Successfully references this.user
                new StoreList(this.user).setVisible(true);
                this.dispose();
            });
        }

        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private JPanel createStatCard(String number, String label) {
        JPanel card = new RoundedPanel(25);
        card.setBackground(CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel num = new JLabel(number);
        num.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel text = new JLabel(label);
        text.setFont(new Font("SansSerif", Font.PLAIN, 14));
        text.setForeground(Color.GRAY);

        card.add(num);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(text);

        return card;
    }
}

class RoundedPanel extends JPanel {
    private int cornerRadius;

    public RoundedPanel(int radius) {
        super();
        cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
    }
}