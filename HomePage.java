import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

class HomePage extends JFrame {
    private User user; // FIX: Added field to store user session

    Color PRIMARY = new Color(186, 55, 78);
    Color LIGHT_BG = new Color(248, 240, 240);
    Color CARD = Color.WHITE;
    Color SIDEBAR = new Color(120, 25, 45);

    private String lastRestaurant = null;
    private JPanel cartItemsPanel;
    private final List<OrderItem> cart = new ArrayList<>();
    private JLabel sectionIndicator;

    private CardLayout contentLayout = new CardLayout();
    private JPanel contentContainer = new JPanel(contentLayout);

    public HomePage(User user) {
        this.user = user; 
        setTitle("MoveEat - " + user.role);

        setExtendedState(JFrame.MAXIMIZED_BOTH); 

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_BG);

        // SIDEBAR 
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

        String greeting = user.isNewUser ? "Hello, " + user.username : "Hello, " + user.username; // Change based on signup
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

        header.add(textPanel, BorderLayout.WEST);

        contentContainer.add(createDashboardPanel(), "DASHBOARD");

        if (user.role.equals("ADMIN")) {
            contentContainer.add(createManageDeliveriesPanel(), "MANAGE_DELIVERIES");
            contentContainer.add(createFleetTrackingPanel(), "FLEET_TRACKING");
            contentContainer.add(createUserManagementPanel(), "USER_MANAGEMENT");
            // Set Manage Deliveries as default for admins if no drivers added
            if (user.drivers.isEmpty()) {
                contentLayout.show(contentContainer, "MANAGE_DELIVERIES");
            }
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

        heroText.add(heroTitle);
        heroText.add(Box.createRigidArea(new Dimension(0, 10)));
        heroText.add(heroDesc);

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

        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
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

            DefaultTableModel orderTableModel = new DefaultTableModel(orderData, orderColumns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable orderTable = new JTable(orderTableModel);
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

        DefaultTableModel orderTableModel = new DefaultTableModel(orderData, orderColumns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable orderTable = new JTable(orderTableModel);
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
        content.setBackground(new Color(242, 240, 238));
        content.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        content.setLayout(new BorderLayout(0, 20));

        JLabel pageTitle = new JLabel("Track Your Order");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        pageTitle.setForeground(new Color(33, 33, 33));
        content.add(pageTitle, BorderLayout.NORTH);

        if (user.orders.isEmpty()) {
            JPanel emptyCard = new RoundedPanel(25);
            emptyCard.setLayout(new BorderLayout());
            emptyCard.setBackground(Color.WHITE);
            emptyCard.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));
            emptyCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
            
            JLabel emptyLabel = new JLabel("No active orders yet");
            emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            emptyCard.add(emptyLabel, BorderLayout.CENTER);
            content.add(emptyCard, BorderLayout.CENTER);
        } else {
            // Display the most recent order
            Order order = user.orders.get(user.orders.size() - 1);

            JPanel mainContainer = new JPanel(new BorderLayout(0, 20));
            mainContainer.setOpaque(false);

            JPanel statusCard = new RoundedPanel(20);
            statusCard.setBackground(Color.WHITE);
            statusCard.setLayout(new BorderLayout());
            statusCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));

            JPanel statusHeader = new JPanel(new BorderLayout());
            statusHeader.setOpaque(false);
            JLabel orderId = new JLabel("Order #" + order.orderId);
            orderId.setFont(new Font("SansSerif", Font.BOLD, 18));
            JLabel eta = new JLabel("<html><font color='gray'>ETA:</font> <font color='#911010'>" + order.estimatedDelivery + "</font></html>");
            eta.setFont(new Font("SansSerif", Font.BOLD, 14));
            statusHeader.add(orderId, BorderLayout.NORTH);
            statusHeader.add(eta, BorderLayout.SOUTH);
            statusCard.add(statusHeader, BorderLayout.NORTH);

            statusCard.add(createTimelineComponent(order.status), BorderLayout.CENTER);

            mainContainer.add(statusCard, BorderLayout.NORTH);

            JPanel bottomRow = new JPanel(new GridLayout(1, 2, 20, 0));
            bottomRow.setOpaque(false);

            //Rider
            JPanel riderCard = new RoundedPanel(20);
            riderCard.setBackground(Color.WHITE);
            riderCard.setLayout(new BorderLayout(0, 15));
            riderCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel riderTitle = new JLabel("Rider Information");
            riderTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
            
            JPanel riderProfile = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            riderProfile.setOpaque(false);
            JPanel riderAvatar = new RoundedPanel(60);
            riderAvatar.setBackground(new Color(218, 37, 28));
            riderAvatar.setPreferredSize(new Dimension(60, 60));
            
            JPanel riderText = new JPanel(new GridLayout(4, 1, 0, 2));
            riderText.setOpaque(false);
            JLabel rName = new JLabel(order.courierName);
            rName.setFont(new Font("SansSerif", Font.BOLD, 15));
            JLabel rRating = new JLabel("⭐ 4.9");
            rRating.setFont(new Font("SansSerif", Font.PLAIN, 12));
            rRating.setForeground(Color.GRAY);
            JLabel rPhone = new JLabel("📞 " + order.courierPhone);
            rPhone.setFont(new Font("SansSerif", Font.PLAIN, 12));
            JLabel rVehicle = new JLabel("🏍️ Yamaha NMAX 155 | 1234ABC");
            rVehicle.setFont(new Font("SansSerif", Font.PLAIN, 12));
            
            riderText.add(rName);
            riderText.add(rRating);
            riderText.add(rPhone);
            riderText.add(rVehicle);
            riderProfile.add(riderAvatar);
            riderProfile.add(riderText);

            JButton contactBtn = new JButton("📞 Contact Rider");
            stylePrimaryButton(contactBtn);

            riderCard.add(riderTitle, BorderLayout.NORTH);
            riderCard.add(riderProfile, BorderLayout.CENTER);
            riderCard.add(contactBtn, BorderLayout.SOUTH);

            //ORDER SUMMARY
            JPanel summaryCard = new RoundedPanel(20);
            summaryCard.setBackground(Color.WHITE);
            summaryCard.setLayout(new BorderLayout(0, 10));
            summaryCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel summaryTitle = new JLabel("Order Summary");
            summaryTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
            
            JPanel itemsList = new JPanel();
            itemsList.setLayout(new BoxLayout(itemsList, BoxLayout.Y_AXIS));
            itemsList.setOpaque(false);
            
            for (OrderItem item : order.items) {
                itemsList.add(createSummaryItem(item.name, "x " + item.quantity, "P" + String.format("%.2f", item.getTotal())));
            }

            JPanel totalRow = new JPanel(new BorderLayout());
            totalRow.setOpaque(false);
            totalRow.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
            JLabel totalText = new JLabel("Total");
            totalText.setFont(new Font("SansSerif", Font.BOLD, 16));
            JLabel totalPrice = new JLabel("P" + String.format("%.2f", order.totalAmount));
            totalPrice.setFont(new Font("SansSerif", Font.BOLD, 18));
            totalPrice.setForeground(new Color(218, 37, 28));
            totalRow.add(totalText, BorderLayout.WEST);
            totalRow.add(totalPrice, BorderLayout.EAST);

            summaryCard.add(summaryTitle, BorderLayout.NORTH);
            summaryCard.add(itemsList, BorderLayout.CENTER);
            summaryCard.add(totalRow, BorderLayout.SOUTH);

            bottomRow.add(riderCard);
            bottomRow.add(summaryCard);
            mainContainer.add(bottomRow, BorderLayout.CENTER);

            // 4. BACK HOME BUTTON
            JButton backBtn = new JButton("← Back to Home");
            backBtn.setContentAreaFilled(false);
            backBtn.setBorderPainted(false);
            backBtn.setForeground(Color.GRAY);
            backBtn.addActionListener(e -> contentLayout.show(contentContainer, "DASHBOARD"));
            
            content.add(mainContainer, BorderLayout.CENTER);
            content.add(backBtn, BorderLayout.SOUTH);
        }

        return content;
    }

    private JPanel createTimelineComponent(String orderStatus) {
        JPanel container = new JPanel(new GridLayout(1, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setStroke(new BasicStroke(2));
                
                int y = 45;
                int width = getWidth() / 5;
                int completedSteps = getCompletedSteps(orderStatus);
                
                g2.setColor(new Color(218, 37, 28));
                if (completedSteps > 0) {
                    g2.drawLine(width/2, y, width + (width * completedSteps), y);
                }
                
                g2.setColor(Color.LIGHT_GRAY);
                if (completedSteps < 5) {
                    g2.drawLine(width + (width * completedSteps), y, getWidth() - (width/2), y);
                }
            }
        };
        container.setOpaque(false);
        container.setPreferredSize(new Dimension(0, 100));

        container.add(createTimelineNode("Order Placed", orderStatus.equals("Order placed") || isOrderAfterStatus(orderStatus, "Order placed"), "✓"));
        container.add(createTimelineNode("Preparing Order", orderStatus.equals("Preparing") || isOrderAfterStatus(orderStatus, "Preparing"), "✓"));
        container.add(createTimelineNode("Out for Delivery", orderStatus.equals("Shipped") || orderStatus.equals("Out for Delivery") || isOrderAfterStatus(orderStatus, "Shipped"), "3"));
        container.add(createTimelineNode("Arriving Soon", orderStatus.equals("Arriving") || isOrderAfterStatus(orderStatus, "Arriving"), "4"));
        container.add(createTimelineNode("Delivered", orderStatus.equals("Delivered"), "5"));

        return container;
    }

    private int getCompletedSteps(String status) {
        switch (status) {
            case "Order placed":
                return 1;
            case "Preparing":
                return 2;
            case "Shipped":
            case "Out for Delivery":
                return 3;
            case "Arriving":
                return 4;
            case "Delivered":
                return 5;
            default:
                return 0;
        }
    }

    private boolean isOrderAfterStatus(String current, String reference) {
        int currentSteps = getCompletedSteps(current);
        int refSteps = getCompletedSteps(reference);
        return currentSteps > refSteps;
    }

    private JPanel createTimelineNode(String text, boolean completed, String step) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        
        JLabel circle = new JLabel(step, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(completed ? new Color(218, 37, 28) : Color.WHITE);
                g2.fillOval(0, 0, 30, 30);
                if(!completed) {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawOval(0, 0, 29, 29);
                }
                super.paintComponent(g);
            }
        };
        circle.setPreferredSize(new Dimension(30, 30));
        circle.setMaximumSize(new Dimension(30, 30));
        circle.setForeground(completed ? Color.WHITE : Color.LIGHT_GRAY);
        circle.setFont(new Font("SansSerif", Font.BOLD, 12));
        circle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 11));
        label.setForeground(completed ? Color.BLACK : Color.LIGHT_GRAY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(Box.createVerticalGlue());
        p.add(circle);
        p.add(Box.createRigidArea(new Dimension(0, 8)));
        p.add(label);
        p.add(Box.createVerticalGlue());
        
        return p;
    }

    private JPanel createSummaryItem(String name, String qty, String price) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setOpaque(false);
        item.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JLabel nameLabel = new JLabel(name);
        JLabel qtyLabel = new JLabel(qty);
        qtyLabel.setForeground(Color.GRAY);
        JLabel priceLabel = new JLabel(price);
        
        item.add(nameLabel, BorderLayout.WEST);
        item.add(qtyLabel, BorderLayout.CENTER);
        item.add(priceLabel, BorderLayout.EAST);
        
        return item;
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setBackground(new Color(218, 37, 28));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(0, 45));
    }

    private JPanel createStoreListPanel() {
        JPanel storePanel = new JPanel(new CardLayout());
        storePanel.setBackground(LIGHT_BG);

        // Selection Panel
        JPanel selectionPanel = new JPanel();
        selectionPanel.setBackground(LIGHT_BG);
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("Select a Restaurant");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        sectionIndicator = new JLabel("Category: Fast Foods / Coffee Shops");
        sectionIndicator.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sectionIndicator.setForeground(new Color(100, 100, 100));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.add(title);
        titlePanel.add(sectionIndicator);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        selectionPanel.add(titlePanel);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel fastFoodLabel = new JLabel("Fast Foods");
        fastFoodLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        fastFoodLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectionPanel.add(fastFoodLabel);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel fastFoodRow = new JPanel(new GridLayout(1, 2, 40, 0));
        fastFoodRow.setOpaque(false);
        fastFoodRow.add(createCategoryButton("Jollibee", "https://live.staticflickr.com/3132/2906289773_83c466e953_q.jpg", "Fast Foods", storePanel));
        fastFoodRow.add(createCategoryButton("McDo", "https://pngimg.com/uploads/mcdonalds/mcdonalds_PNG1.png", "Fast Foods", storePanel));
        selectionPanel.add(fastFoodRow);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel coffeeLabel = new JLabel("Coffee Shops");
        coffeeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        coffeeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        selectionPanel.add(coffeeLabel);
        selectionPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel coffeeRow = new JPanel(new GridLayout(1, 2, 40, 0));
        coffeeRow.setOpaque(false);
        coffeeRow.add(createCategoryButton("Starbucks", "https://upload.wikimedia.org/wikipedia/sco/thumb/4/45/Starbucks_Coffee_Logo.svg/1200px-Starbucks_Coffee_Logo.svg.png", "Coffee Shops", storePanel));
        coffeeRow.add(createCategoryButton("Dunkin'", "https://upload.wikimedia.org/wikipedia/commons/thumb/3/35/Dunkin%27_%28logo%29.svg/2560px-Dunkin%27_%28logo%29.svg.png", "Coffee Shops", storePanel));
        selectionPanel.add(coffeeRow);

        storePanel.add(selectionPanel, "SELECTION");

        // Menu Panel (placeholder for now)
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(LIGHT_BG);
        menuPanel.add(new JLabel("Menu for selected restaurant - to be implemented"));
        storePanel.add(menuPanel, "MENU");

        return storePanel;
    }

    private JButton createCategoryButton(String restaurantName, String imageUrl, String section, JPanel storePanel) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        button.setFocusPainted(false);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            URL url = new URL(imageUrl);
            ImageIcon icon = new ImageIcon(url);
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            imageLabel.setText("Logo Not Found");
        }

        JLabel nameLabel = new JLabel(restaurantName, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        button.add(imageLabel, BorderLayout.CENTER);
        button.add(nameLabel, BorderLayout.SOUTH);
        button.addActionListener(e -> showRestaurantMenu(restaurantName, section, storePanel));
        return button;
    }

    private void showRestaurantMenu(String restaurant, String section, JPanel storePanel) {
        lastRestaurant = restaurant;
        if (sectionIndicator != null) {
            sectionIndicator.setText("Category: " + section);
        }

        JPanel menuPanel = createMenuPanel(restaurant, section, storePanel);
        storePanel.add(menuPanel, "MENU");
        ((CardLayout) storePanel.getLayout()).show(storePanel, "MENU");
    }

    private JPanel createMenuPanel(String restaurant, String section, JPanel storePanel) {
        JPanel menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(LIGHT_BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JPanel leftHeader = new JPanel();
        leftHeader.setOpaque(false);
        leftHeader.setLayout(new BoxLayout(leftHeader, BoxLayout.Y_AXIS));
        JLabel restName = new JLabel((restaurant.equals("McDo") ? "McDonald's" : restaurant) + " Menu");
        restName.setFont(new Font("SansSerif", Font.BOLD, 28));
        JLabel categoryLabel = new JLabel(section);
        categoryLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        categoryLabel.setForeground(new Color(100, 100, 100));
        leftHeader.add(restName);
        leftHeader.add(categoryLabel);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> ((CardLayout) storePanel.getLayout()).show(storePanel, "SELECTION"));
        navPanel.add(backButton);

        header.add(leftHeader, BorderLayout.WEST);
        header.add(navPanel, BorderLayout.EAST);

        menuPanel.add(header, BorderLayout.NORTH);

        // Main content: Food grid and cart
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        // Food items grid
        JPanel foodPanel = new JPanel();
        foodPanel.setLayout(new BoxLayout(foodPanel, BoxLayout.Y_AXIS));
        foodPanel.setBackground(LIGHT_BG);
        foodPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Sample food items based on restaurant
        List<OrderItem> foodItems = getFoodItemsForRestaurant(restaurant);
        JPanel foodGrid = new JPanel(new GridLayout(0, 3, 20, 20)); // 3 columns
        foodGrid.setOpaque(false);

        for (OrderItem item : foodItems) {
            foodGrid.add(createFoodCard(item));
        }

        JScrollPane foodScroll = new JScrollPane(foodGrid);
        foodScroll.setOpaque(false);
        foodScroll.getViewport().setOpaque(false);
        foodScroll.setBorder(BorderFactory.createEmptyBorder());
        foodScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        foodScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        foodPanel.add(foodScroll);

        // Cart panel
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(CARD);
        cartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        cartPanel.setPreferredSize(new Dimension(300, 0));

        JLabel cartTitle = new JLabel("Your Cart");
        cartTitle.setFont(new Font("SansSerif", Font.BOLD, 18));

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new GridLayout(0, 1, 0, 0)); // No spacing
        cartItemsPanel.setOpaque(false);

        JScrollPane cartScroll = new JScrollPane(cartItemsPanel);
        cartScroll.setOpaque(false);
        cartScroll.getViewport().setOpaque(false);
        cartScroll.setBorder(BorderFactory.createEmptyBorder());
        cartScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JButton checkoutBtn = new JButton("Proceed to Checkout");
        checkoutBtn.setBackground(PRIMARY);
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFocusPainted(false);
        checkoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        checkoutBtn.setPreferredSize(new Dimension(0, 45));
        checkoutBtn.addActionListener(e -> proceedToCheckout());

        cartPanel.add(cartTitle, BorderLayout.NORTH);
        cartPanel.add(cartScroll, BorderLayout.CENTER);
        cartPanel.add(checkoutBtn, BorderLayout.SOUTH);

        mainContent.add(foodPanel, BorderLayout.CENTER);
        mainContent.add(cartPanel, BorderLayout.EAST);

        menuPanel.add(mainContent, BorderLayout.CENTER);

        updateCartDisplay();

        return menuPanel;
    }

    private List<OrderItem> getFoodItemsForRestaurant(String restaurant) {
        List<OrderItem> items = new ArrayList<>();
        switch (restaurant) {
            case "Jollibee":
                items.add(new OrderItem("Chicken Joy", 2.5, 1));
                items.add(new OrderItem("Tuna Pie", 1.5, 1));
                items.add(new OrderItem("Jolly Hotdog", 1.0, 1));
                break;
            case "McDo":
                items.add(new OrderItem("Big Mac", 5.0, 1));
                items.add(new OrderItem("Fries", 2.0, 1));
                items.add(new OrderItem("Coke", 1.5, 1));
                break;
            case "Starbucks":
                items.add(new OrderItem("Latte", 4.0, 1));
                items.add(new OrderItem("Croissant", 3.0, 1));
                items.add(new OrderItem("Muffin", 2.5, 1));
                break;
            case "Dunkin'":
                items.add(new OrderItem("Donut", 1.5, 1));
                items.add(new OrderItem("Coffee", 2.0, 1));
                items.add(new OrderItem("Bagel", 2.5, 1));
                break;
        }
        return items;
    }

    private JPanel createFoodCard(OrderItem item) {
        JPanel card = new RoundedPanel(15);
        card.setLayout(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setPreferredSize(new Dimension(200, 250));

        JLabel nameLabel = new JLabel(item.name);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel priceLabel = new JLabel("₱" + String.format("%.2f", item.price));
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        priceLabel.setForeground(Color.GRAY);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel quantityPanel = new JPanel(new FlowLayout());
        quantityPanel.setOpaque(false);
        JButton minusBtn = new JButton("-");
        minusBtn.setPreferredSize(new Dimension(30, 30));
        minusBtn.addActionListener(e -> adjustQuantity(item, -1));

        JLabel qtyLabel = new JLabel(String.valueOf(item.quantity));
        qtyLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        qtyLabel.setPreferredSize(new Dimension(30, 30));
        qtyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton plusBtn = new JButton("+");
        plusBtn.setPreferredSize(new Dimension(30, 30));
        plusBtn.addActionListener(e -> adjustQuantity(item, 1));

        quantityPanel.add(minusBtn);
        quantityPanel.add(qtyLabel);
        quantityPanel.add(plusBtn);

        JButton addToCartBtn = new JButton("Add to Cart");
        addToCartBtn.setBackground(PRIMARY);
        addToCartBtn.setForeground(Color.WHITE);
        addToCartBtn.setFocusPainted(false);
        addToCartBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        addToCartBtn.addActionListener(e -> addToCart(item));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(quantityPanel, BorderLayout.NORTH);
        bottomPanel.add(addToCartBtn, BorderLayout.SOUTH);

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(priceLabel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    private void adjustQuantity(OrderItem item, int delta) {
        item.quantity = Math.max(1, item.quantity + delta);
        // Update display if needed
    }

    private void addToCart(OrderItem item) {
        // Check if already in cart
        for (OrderItem cartItem : cart) {
            if (cartItem.name.equals(item.name)) {
                cartItem.quantity += item.quantity;
                updateCartDisplay();
                return;
            }
        }
        cart.add(new OrderItem(item.name, item.price, item.quantity));
        updateCartDisplay();
    }

    private void updateCartDisplay() {
        if (cartItemsPanel != null) {
            cartItemsPanel.removeAll();
            for (OrderItem item : cart) {
                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setOpaque(false);
                itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

                JLabel nameQty = new JLabel(item.name + " x" + item.quantity);
                nameQty.setFont(new Font("SansSerif", Font.PLAIN, 12));

                JLabel price = new JLabel("₱" + String.format("%.2f", item.getTotal()));
                price.setFont(new Font("SansSerif", Font.BOLD, 12));

                itemPanel.add(nameQty, BorderLayout.WEST);
                itemPanel.add(price, BorderLayout.EAST);

                cartItemsPanel.add(itemPanel);
            }
            cartItemsPanel.revalidate();
            cartItemsPanel.repaint();
        }
    }

    private void proceedToCheckout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }
        double total = cart.stream().mapToDouble(OrderItem::getTotal).sum();
        Order newOrder = new Order("ORD" + (user.orders.size() + 1), user.username, "Default Address", "0912-345-6789", cart, total, "Cash");
        user.orders.add(newOrder);
        cart.clear();
        updateCartDisplay();
        JOptionPane.showMessageDialog(this, "Order placed successfully!");
        // Switch back to dashboard or order history
        contentLayout.show(contentContainer, "ORDER_HISTORY");
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

        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
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

        DefaultTableModel tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
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

        String[] columns = {"User ID", "Name", "Role", "Status"};
        Object[][] data = {
            {"U001", "Alice Johnson", "Customer", "Active"},
            {"U002", "Bob Smith", "Customer", "Active"},
            {"U003", "Admin User", "Admin", "Active"}
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
        
        JButton deleteDriverBtn = new JButton("Delete Driver");
        deleteDriverBtn.setBackground(new Color(200, 50, 50));
        deleteDriverBtn.setForeground(Color.WHITE);
        deleteDriverBtn.setFocusPainted(false);
        deleteDriverBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        deleteDriverBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a driver to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this driver?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String driverId = (String) model.getValueAt(selectedRow, 0);
                user.drivers.remove(driverId);
                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Driver deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        JButton addDriverBtn = new JButton("Add Driver");
        addDriverBtn.setBackground(PRIMARY);
        addDriverBtn.setForeground(Color.WHITE);
        addDriverBtn.setFocusPainted(false);
        addDriverBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        addDriverBtn.addActionListener(e -> showAddDriverDialog(model));
        
        buttonRow.add(deleteDriverBtn);
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
            // Track driver in user's drivers list
            user.drivers.add(driverId);
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
            btn.addActionListener(e -> contentLayout.show(contentContainer, "STORE_LIST"));
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
