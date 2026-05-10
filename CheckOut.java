import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class CheckOut extends JFrame {

    private final Color PRIMARY = new Color(186, 55, 78);
    private final Color LIGHT_BG = new Color(248, 240, 240);
    private final Color CARD = Color.WHITE;
    private final Color SIDEBAR = new Color(120, 25, 45);
    private final Color TEXT = new Color(70, 70, 70);
    private final User user;
    private List<OrderItem> orderItems;
    private double totalAmount = 0;
    private JTextField nameField, phoneField, streetField, cityField, provinceField, postalField;
    private ButtonGroup paymentGroup;

    public CheckOut(User user) {
        this(user, getDefaultItems());
    }

    public CheckOut(User user, List<OrderItem> items) {
        this.user = user;
        this.orderItems = items != null ? items : getDefaultItems();
        calculateTotal();

        setTitle("MoveEat - Checkout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_BG);

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
        addNavButton(sidebar, "Store List");
        addNavButton(sidebar, "My Order History");
        addNavButton(sidebar, "Delivery Progress");
        sidebar.add(Box.createVerticalGlue());
        addNavButton(sidebar, "Logout");

        add(sidebar, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LIGHT_BG);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Checkout - Complete Your Order");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT);
        header.add(title, BorderLayout.WEST);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        navPanel.add(backButton);
        header.add(navPanel, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        // Order Summary 
        JPanel summaryCard = createOrderSummaryCard();
        contentPanel.add(summaryCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Customer Info 
        JPanel customerPanel = createCustomerInfoPanel();
        contentPanel.add(customerPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Payment Method 
        JPanel paymentPanel = createPaymentMethodPanel();
        contentPanel.add(paymentPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonPanel = createButtonPanel(backButton);
        contentPanel.add(buttonPanel);
        contentPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(LIGHT_BG);
        scrollPane.getViewport().setBackground(LIGHT_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        backButton.addActionListener(e -> this.dispose());

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createOrderSummaryCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        JLabel title = new JLabel("Order Summary");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(title, BorderLayout.WEST);
        card.add(headerPanel, BorderLayout.NORTH);

        // Items list
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setOpaque(false);

        for (OrderItem item : orderItems) {
            JPanel itemRow = new JPanel(new BorderLayout());
            itemRow.setOpaque(false);
            itemRow.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            JLabel itemName = new JLabel(item.quantity + "x " + item.name);
            itemName.setFont(new Font("SansSerif", Font.PLAIN, 14));

            JLabel itemPrice = new JLabel(String.format("₱%.2f", item.getTotal()));
            itemPrice.setFont(new Font("SansSerif", Font.BOLD, 14));
            itemPrice.setForeground(PRIMARY);

            itemRow.add(itemName, BorderLayout.WEST);
            itemRow.add(itemPrice, BorderLayout.EAST);
            itemsPanel.add(itemRow);
        }

        itemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        itemsPanel.add(separator);
        itemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);

        JLabel totalLabel = new JLabel("Total Amount:");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel totalValue = new JLabel(String.format("₱%.2f", totalAmount));
        totalValue.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalValue.setForeground(PRIMARY);

        totalRow.add(totalLabel, BorderLayout.WEST);
        totalRow.add(totalValue, BorderLayout.EAST);
        itemsPanel.add(totalRow);

        card.add(itemsPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createCustomerInfoPanel() {
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        JLabel title = new JLabel("Delivery Information");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(title, BorderLayout.WEST);
        card.add(headerPanel, BorderLayout.NORTH);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 20);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        formPanel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(user.username);
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        nameField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        formPanel.add(nameField, gbc);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        formPanel.add(phoneLabel, gbc);

        JTextField phoneField = new JTextField();
        phoneField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        phoneField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        formPanel.add(phoneField, gbc);

        JLabel streetLabel = new JLabel("Street Address:");
        streetLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        formPanel.add(streetLabel, gbc);

        JTextField streetField = new JTextField();
        streetField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        streetField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        formPanel.add(streetField, gbc);

        JLabel cityLabel = new JLabel("City:");
        cityLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        formPanel.add(cityLabel, gbc);

        JTextField cityField = new JTextField();
        cityField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cityField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        formPanel.add(cityField, gbc);

        JLabel provinceLabel = new JLabel("Province:");
        provinceLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.2;
        formPanel.add(provinceLabel, gbc);

        JTextField provinceField = new JTextField();
        provinceField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        formPanel.add(provinceField, gbc);

        JLabel postalLabel = new JLabel("Postal Code:");
        postalLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        formPanel.add(postalLabel, gbc);

        JTextField postalField = new JTextField();
        postalField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        gbc.gridx = 3;
        gbc.weightx = 0.2;
        formPanel.add(postalField, gbc);

        card.add(formPanel, BorderLayout.CENTER);

        // Store 
        this.nameField = nameField;
        this.phoneField = phoneField;
        this.streetField = streetField;
        this.cityField = cityField;
        this.provinceField = provinceField;
        this.postalField = postalField;

        return card;
    }

    private JPanel createPaymentMethodPanel() {
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel title = new JLabel("Payment Method");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(TEXT);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(title, BorderLayout.WEST);
        card.add(headerPanel, BorderLayout.NORTH);

        // Payment 
        JPanel optionsPanel = new JPanel();
        optionsPanel.setOpaque(false);
        optionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 50, 20));

        ButtonGroup group = new ButtonGroup();

        JRadioButton gcashOption = new JRadioButton("GCash");
        gcashOption.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gcashOption.setOpaque(false);
        gcashOption.setSelected(true);
        group.add(gcashOption);
        optionsPanel.add(gcashOption);

        JRadioButton codOption = new JRadioButton("Cash on Delivery (COD)");
        codOption.setFont(new Font("SansSerif", Font.PLAIN, 14));
        codOption.setOpaque(false);
        group.add(codOption);
        optionsPanel.add(codOption);

        card.add(optionsPanel, BorderLayout.CENTER);
        this.paymentGroup = group;

        return card;
    }

    private JPanel createButtonPanel(JButton backButton) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);

        JButton confirmButton = new JButton("Confirm & Pay");
        confirmButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        confirmButton.setBackground(PRIMARY);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setPreferredSize(new Dimension(180, 50));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelButton = new JButton("Cancel Order");
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        cancelButton.setBackground(new Color(200, 200, 200));
        cancelButton.setForeground(TEXT);
        cancelButton.setFocusPainted(false);
        cancelButton.setPreferredSize(new Dimension(180, 50));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.add(confirmButton);
        panel.add(cancelButton);

        confirmButton.addActionListener(e -> handleOrderConfirmation());
        cancelButton.addActionListener(e -> this.dispose());

        return panel;
    }

    private void handleOrderConfirmation() {
        String customerName = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String street = streetField.getText().trim();
        String city = cityField.getText().trim();
        String province = provinceField.getText().trim();
        String postal = postalField.getText().trim();
        String address = street + ", " + city + ", " + province + " " + postal;

        String paymentMethod = "GCash"; // default
        for (java.util.Enumeration<AbstractButton> buttons = paymentGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                paymentMethod = button.getText();
                break;
            }
        }

        if (customerName.isEmpty() || phone.isEmpty() || address.replace(", ,  ", "").trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all delivery information fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String orderId = "ORD" + System.currentTimeMillis();
        Order order = new Order(orderId, customerName, address, phone, orderItems, totalAmount, paymentMethod);
        user.orders.add(order);

        JOptionPane.showMessageDialog(this,
            "Order Confirmed!\n\n" +
            "Order ID: " + orderId + "\n" +
            "Total Amount: ₱" + String.format("%.2f", totalAmount) + "\n" +
            "Payment Method: " + paymentMethod + "\n\n" +
            "Your order has been placed successfully.\n" +
            "You will receive a notification soon.",
            "Order Confirmation",
            JOptionPane.INFORMATION_MESSAGE);

        new HomePage(user);
        this.dispose();
    }

    private void calculateTotal() {
        totalAmount = 0;
        for (OrderItem item : orderItems) {
            totalAmount += item.getTotal();
        }
    }

    private static List<OrderItem> getDefaultItems() {
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("Chickenjoy", 99.00, 1));
        items.add(new OrderItem("Jolly Spaghetti", 79.00, 1));
        items.add(new OrderItem("1L Coke", 45.00, 1));
        return items;
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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (text.equals("Dashboard")) {
            btn.addActionListener(e -> {
                try {
                    new CheckOut(user);
                    this.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error navigating to Dashboard", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else if (text.equals("Store List")) {
            btn.addActionListener(e -> {
                try {
                    new StoreList(user);
                    this.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error navigating to Store List", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else if (text.equals("Logout")) {
            btn.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Logged out successfully!");
                System.exit(0);
            });
        }

        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckOut(new User("TestUser", "CUSTOMER", false)).setVisible(true));
    }

}