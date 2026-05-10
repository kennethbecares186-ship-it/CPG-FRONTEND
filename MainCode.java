import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.List;
import java.util.ArrayList;

// LOGINN
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

        JLabel subtitle = new JLabel("Fast Delivery & Food Ordering");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 22));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(appTitle);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        leftPanel.add(subtitle);

        leftPanel.add(Box.createVerticalGlue());

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(LIGHT_BG);

        // LOGINB CARD
        JPanel formCard = new JPanel();
        formCard.setPreferredSize(new Dimension(420, 500));
        formCard.setBackground(WHITE);
        formCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 12, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(
                type.equals("LOGIN") ? "Welcome Back" : "Create Account");

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

        // ADMIN CHECKBOX (only for LOGIN)
        JCheckBox adminCheck = null;
        if (type.equals("LOGIN")) {
            adminCheck = new JCheckBox("Login as Admin");
            adminCheck.setFont(new Font("SansSerif", Font.PLAIN, 14));
            adminCheck.setForeground(TEXT);
            adminCheck.setBackground(WHITE);

            gbc.gridy = 6;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(10, 20, 10, 20);

            formCard.add(adminCheck, gbc);
        }

        // BUTTON
        JButton submitBtn = new JButton(
                type.equals("LOGIN") ? "LOGIN" : "CREATE ACCOUNT");

        submitBtn.setBackground(PRIMARY);
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFocusPainted(false);
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        submitBtn.setPreferredSize(new Dimension(250, 45));

        gbc.gridy = type.equals("LOGIN") ? 7 : 6;
        gbc.insets = new Insets(25, 20, 10, 20);

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

        gbc.gridy = type.equals("LOGIN") ? 8 : 7;
        gbc.insets = new Insets(5, 20, 20, 20);

        formCard.add(switchBtn, gbc);

        rightPanel.add(formCard);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);

        // Action Listeners
        switchBtn.addActionListener(e -> cardLayout.show(mainContainer, type.equals("LOGIN") ? "SIGNUP_PAGE" : "LOGIN_PAGE"));

        final JCheckBox finalAdminCheck = adminCheck;
        submitBtn.addActionListener(e -> {
            String name = userField.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username");
                return;
            }

            // SIMULATED AUTH LOGIC
            String role = (finalAdminCheck != null && finalAdminCheck.isSelected()) ? "ADMIN" : "CUSTOMER";
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

    private CardLayout contentLayout = new CardLayout();
    private JPanel contentContainer = new JPanel(contentLayout);

    public HomePage(User user) {
        this.user = user; // FIX: Initialize user session
        setTitle("MoveEat - " + user.role);

        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize immediately for homepage

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

        contentContainer.add(createDashboardPanel(), "DASHBOARD");

        if (user.role.equals("ADMIN")) {
            contentContainer.add(createManageDeliveriesPanel(), "MANAGE_DELIVERIES");
            contentContainer.add(createFleetTrackingPanel(), "FLEET_TRACKING");
            contentContainer.add(createUserManagementPanel(), "USER_MANAGEMENT");
        } else {
            contentContainer.add(createStoreListPanel(), "STORE_LIST");
            contentContainer.add(createOrderHistoryPanel(), "ORDER_HISTORY");
            contentContainer.add(createDeliveryProgressPanel(), "DELIVERY_PROGRESS");
        }

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(contentContainer, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createDashboardPanel() {
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
        content.add(Box.createRigidArea(new Dimension(0, 25)));

        // ================= MY ORDER HISTORY =================
        if (!user.role.equals("ADMIN")) {
            JPanel orderHistoryCard = new RoundedPanel(25);
            orderHistoryCard.setLayout(new BorderLayout());
            orderHistoryCard.setBackground(CARD);
            orderHistoryCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel historyTitle = new JLabel("My Order History");
            historyTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

            String[] orderColumns = {"Order ID", "Date", "Items", "Total", "Status"};
            Object[][] orderData;
            if (user.orders.isEmpty()) {
                orderData = new Object[][]{{"No orders yet", "", "", "", ""}};
            } else {
                orderData = new Object[user.orders.size()][];
                for (int i = 0; i < user.orders.size(); i++) {
                    Order order = user.orders.get(i);
                    orderData[i] = new Object[]{order.orderId, order.orderDate, getItemsSummary(order.items), "₱" + String.format("%.2f", order.totalAmount), order.status};
                }
            }

            JTable orderTable = new JTable(orderData, orderColumns);
            orderTable.setRowHeight(35);
            orderTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
            orderTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
            orderTable.getTableHeader().setBackground(PRIMARY);
            orderTable.getTableHeader().setForeground(Color.WHITE);

            JScrollPane orderScrollPane = new JScrollPane(orderTable);
            orderScrollPane.setBorder(BorderFactory.createEmptyBorder());

            orderHistoryCard.add(historyTitle, BorderLayout.NORTH);
            orderHistoryCard.add(orderScrollPane, BorderLayout.CENTER);

            content.add(orderHistoryCard);
        }

        return content;
    }

    private JPanel createOrderHistoryPanel() {
        JPanel content = new JPanel();
        content.setBackground(LIGHT_BG);
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel orderHistoryCard = new RoundedPanel(25);
        orderHistoryCard.setLayout(new BorderLayout());
        orderHistoryCard.setBackground(CARD);
        orderHistoryCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel historyTitle = new JLabel("My Order History");
        historyTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        String[] orderColumns = {"Order ID", "Date", "Items", "Total", "Status"};
        Object[][] orderData;
        if (user.orders.isEmpty()) {
            orderData = new Object[][]{{"No orders yet", "", "", "", ""}};
        } else {
            orderData = new Object[user.orders.size()][];
            for (int i = 0; i < user.orders.size(); i++) {
                Order order = user.orders.get(i);
                orderData[i] = new Object[]{order.orderId, order.orderDate, getItemsSummary(order.items), "₱" + String.format("%.2f", order.totalAmount), order.status};
            }
        }

        JTable orderTable = new JTable(orderData, orderColumns);
        orderTable.setRowHeight(35);
        orderTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        orderTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        orderTable.getTableHeader().setBackground(PRIMARY);
        orderTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderScrollPane.setBorder(BorderFactory.createEmptyBorder());

        orderHistoryCard.add(historyTitle, BorderLayout.NORTH);
        orderHistoryCard.add(orderScrollPane, BorderLayout.CENTER);

        content.add(orderHistoryCard);

        return content;
    }

    private JPanel createDeliveryProgressPanel() {
        JPanel content = new JPanel();
        content.setBackground(LIGHT_BG);
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel progressCard = new RoundedPanel(25);
        progressCard.setLayout(new BorderLayout());
        progressCard.setBackground(CARD);
        progressCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel progressTitle = new JLabel("Delivery Progress");
        progressTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        // Simple progress display
        JPanel progressPanel = new JPanel();
        progressPanel.setBackground(CARD);
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.Y_AXIS));

        JLabel orderLabel = new JLabel("Order #502 - North Park Ave (Home)");
        orderLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(75);
        progressBar.setStringPainted(true);
        progressBar.setString("In Transit - 75%");

        JLabel statusLabel = new JLabel("Driver: Sarah Smith | ETA: 15 mins");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statusLabel.setForeground(Color.GRAY);

        progressPanel.add(orderLabel);
        progressPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        progressPanel.add(progressBar);
        progressPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        progressPanel.add(statusLabel);

        progressCard.add(progressTitle, BorderLayout.NORTH);
        progressCard.add(progressPanel, BorderLayout.CENTER);

        content.add(progressCard);

        return content;
    }

    private JPanel createStoreListPanel() {
        JPanel content = new JPanel();
        content.setBackground(LIGHT_BG);
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel storeCard = new RoundedPanel(25);
        storeCard.setLayout(new BorderLayout());
        storeCard.setBackground(CARD);
        storeCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel storeTitle = new JLabel("Available Stores");
        storeTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        String[] storeColumns = {"Store Name", "Location", "Rating", "Status"};
        Object[][] storeData = {
            {"Burger Palace", "Downtown", "4.5", "Open"},
            {"Pizza Corner", "North Park", "4.2", "Open"},
            {"Taco Town", "South Side", "4.7", "Closed"}
        };

        JTable storeTable = new JTable(storeData, storeColumns);
        storeTable.setRowHeight(35);
        storeTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        storeTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        storeTable.getTableHeader().setBackground(PRIMARY);
        storeTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane storeScrollPane = new JScrollPane(storeTable);
        storeScrollPane.setBorder(BorderFactory.createEmptyBorder());

        storeCard.add(storeTitle, BorderLayout.NORTH);
        storeCard.add(storeScrollPane, BorderLayout.CENTER);

        content.add(storeCard);

        return content;
    }

    private JPanel createManageDeliveriesPanel() {
        JPanel content = new JPanel();
        content.setBackground(LIGHT_BG);
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel deliveriesCard = new RoundedPanel(25);
        deliveriesCard.setLayout(new BorderLayout());
        deliveriesCard.setBackground(CARD);
        deliveriesCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel deliveriesTitle = new JLabel("Manage Deliveries");
        deliveriesTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        String[] columns = {"Order ID", "Address", "Status", "Driver", "Actions"};
        Object[][] data = {
            {"#501", "Downtown - Central", "Pending", "John Doe", "Assign/Reassign"},
            {"#502", "North Park Ave", "In Transit", "Sarah Smith", "Update Status"},
            {"#503", "South Side Mall", "Delivered", "Mike Ross", "View Details"}
        };

        JTable table = new JTable(data, columns);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        deliveriesCard.add(deliveriesTitle, BorderLayout.NORTH);
        deliveriesCard.add(scrollPane, BorderLayout.CENTER);

        content.add(deliveriesCard);

        return content;
    }

    private JPanel createFleetTrackingPanel() {
        JPanel content = new JPanel();
        content.setBackground(LIGHT_BG);
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel fleetCard = new RoundedPanel(25);
        fleetCard.setLayout(new BorderLayout());
        fleetCard.setBackground(CARD);
        fleetCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel fleetTitle = new JLabel("Fleet Tracking");
        fleetTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        String[] columns = {"Vehicle ID", "Driver", "Location", "Status", "Last Update"};
        Object[][] data = {
            {"V001", "John Doe", "Downtown", "Active", "2 mins ago"},
            {"V002", "Sarah Smith", "North Park", "Active", "5 mins ago"},
            {"V003", "Mike Ross", "South Side", "Inactive", "1 hour ago"}
        };

        JTable table = new JTable(data, columns);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        fleetCard.add(fleetTitle, BorderLayout.NORTH);
        fleetCard.add(scrollPane, BorderLayout.CENTER);

        content.add(fleetCard);

        return content;
    }

    private JPanel createUserManagementPanel() {
        JPanel content = new JPanel();
        content.setBackground(LIGHT_BG);
        content.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel userCard = new RoundedPanel(25);
        userCard.setLayout(new BorderLayout());
        userCard.setBackground(CARD);
        userCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel userTitle = new JLabel("User Management");
        userTitle.setFont(new Font("SansSerif", Font.BOLD, 22));

        String[] columns = {"User ID", "Name", "Role", "Status", "Actions"};
        Object[][] data = {
            {"U001", "Alice Johnson", "Customer", "Active", "Edit/Delete"},
            {"U002", "Bob Smith", "Customer", "Active", "Edit/Delete"},
            {"U003", "Admin User", "Admin", "Active", "Edit/Delete"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        userCard.add(userTitle, BorderLayout.NORTH);
        userCard.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRow.setOpaque(false);
        JButton addDriverBtn = new JButton("Add Driver");
        addDriverBtn.setBackground(PRIMARY);
        addDriverBtn.setForeground(Color.WHITE);
        addDriverBtn.setFocusPainted(false);
        addDriverBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        addDriverBtn.addActionListener(e -> showAddDriverDialog(model));
        buttonRow.add(addDriverBtn);

        content.add(userCard);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(buttonRow);

        return content;
    }

    private void showAddDriverDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Add New Driver", true);
        dialog.setSize(420, 360);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(LIGHT_BG);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel nameLabel = new JLabel("Driver Name");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        form.add(nameLabel, gbc);

        gbc.gridy++;
        JTextField nameField = new JTextField(20);
        form.add(nameField, gbc);

        gbc.gridy++;
        JLabel phoneLabel = new JLabel("Phone Number");
        phoneLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        form.add(phoneLabel, gbc);

        gbc.gridy++;
        JTextField phoneField = new JTextField(20);
        form.add(phoneField, gbc);

        gbc.gridy++;
        JLabel licenseLabel = new JLabel("License / ID");
        licenseLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        form.add(licenseLabel, gbc);

        gbc.gridy++;
        JTextField licenseField = new JTextField(20);
        form.add(licenseField, gbc);

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionRow.setOpaque(false);
        JButton saveBtn = new JButton("Save Driver");
        saveBtn.setBackground(PRIMARY);
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveBtn.addActionListener(e -> {
            String driverName = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String license = licenseField.getText().trim();
            if (driverName.isEmpty() || phone.isEmpty() || license.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all driver details.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String driverId = "D" + (model.getRowCount() + 1);
            model.addRow(new Object[]{driverId, driverName, "Driver", "Active", "Edit/Delete"});
            JOptionPane.showMessageDialog(dialog, "Driver added successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(Color.LIGHT_GRAY);
        cancelBtn.setForeground(Color.DARK_GRAY);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelBtn.addActionListener(e -> dialog.dispose());

        actionRow.add(cancelBtn);
        actionRow.add(saveBtn);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(actionRow, BorderLayout.SOUTH);
        dialog.setVisible(true);
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
        } else if (text.equals("Dashboard")) {
            btn.addActionListener(e -> contentLayout.show(contentContainer, "DASHBOARD"));
        } else if (text.equals("Manage Deliveries")) {
            btn.addActionListener(e -> contentLayout.show(contentContainer, "MANAGE_DELIVERIES"));
        } else if (text.equals("Fleet Tracking")) {
            btn.addActionListener(e -> contentLayout.show(contentContainer, "FLEET_TRACKING"));
        } else if (text.equals("User Management")) {
            btn.addActionListener(e -> contentLayout.show(contentContainer, "USER_MANAGEMENT"));
        } else if (text.equals("My Order History")) {
            btn.addActionListener(e -> contentLayout.show(contentContainer, "ORDER_HISTORY"));
        } else if (text.equals("Delivery Progress")) {
            btn.addActionListener(e -> contentLayout.show(contentContainer, "DELIVERY_PROGRESS"));
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

    private String getItemsSummary(List<OrderItem> items) {
        StringBuilder sb = new StringBuilder();
        for (OrderItem item : items) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(item.name).append(" x").append(item.quantity);
        }
        return sb.toString();
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