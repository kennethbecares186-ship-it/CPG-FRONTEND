import java.awt.*;
import javax.swing.*;

public class StoreList extends JFrame {
    // Colors and fonts from MainCode
    private final Color PRIMARY = new Color(186, 55, 78);
    private final Color LIGHT_BG = new Color(248, 240, 240);
    private final Color CARD = Color.WHITE;
    private final Color SIDEBAR = new Color(120, 25, 45);
    private final User user;

    public StoreList(User user) {
        this.user = user;
        setTitle("MoveEat - Store List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_BG);

        // Sidebar (like HomePage)
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
        addNavButton(sidebar, "Order Food/Items");
        addNavButton(sidebar, "My Order History");
        addNavButton(sidebar, "Delivery Progress");
        sidebar.add(Box.createVerticalGlue());
        addNavButton(sidebar, "Logout");

        add(sidebar, BorderLayout.WEST);

        // Main area
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LIGHT_BG);
        header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("Select a Restaurant");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        header.add(title, BorderLayout.WEST);

        // Home and Back buttons at the top right
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navPanel.setOpaque(false);
        JButton homeButton = new JButton("Home");
        JButton backButton = new JButton("Back");
        navPanel.add(homeButton);
        navPanel.add(backButton);
        header.add(navPanel, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // Restaurant choices (center)
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(LIGHT_BG);
        centerPanel.setLayout(new GridLayout(1, 2, 40, 40));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        // Jollibee button with logo
        JButton jollibeeButton = new JButton("Jollibee");
        jollibeeButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        try {
            ImageIcon jollibeeIcon = new ImageIcon("GUIBuilder/resources/jollibee_logo.png");
            jollibeeButton.setIcon(new ImageIcon(jollibeeIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        } catch (Exception e) {}
        jollibeeButton.setBackground(CARD);
        jollibeeButton.setFocusPainted(false);
        centerPanel.add(jollibeeButton);

        // McDo button with logo
        JButton mcdoButton = new JButton("McDo");
        mcdoButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        try {
            ImageIcon mcdoIcon = new ImageIcon("GUIBuilder/resources/mcdo_logo.png");
            mcdoButton.setIcon(new ImageIcon(mcdoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        } catch (Exception e) {}
        mcdoButton.setBackground(CARD);
        mcdoButton.setFocusPainted(false);
        centerPanel.add(mcdoButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Action listeners for restaurant buttons
        jollibeeButton.addActionListener(e -> new RestaurantFrame(user, "Jollibee").setVisible(true));
        mcdoButton.addActionListener(e -> new RestaurantFrame(user, "McDo").setVisible(true));

        // Home and Back button actions
        homeButton.addActionListener(e -> {
            new HomePage(user);
            this.dispose();
        });
        backButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Back clicked!"));

        add(mainPanel, BorderLayout.CENTER);
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
        if (text.equals("Dashboard")) {
            btn.addActionListener(e -> {
                new HomePage(user);
                this.dispose();
            });
        }
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    // RestaurantFrame inner class
    class RestaurantFrame extends JFrame {
        private final User user;
        public RestaurantFrame(User user, String restaurant) {
            this.user = user;
            setTitle("MoveEat - " + restaurant + " Menu");
            setSize(1200, 700);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());
            getContentPane().setBackground(LIGHT_BG);

            // Sidebar (reuse design)
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
            addNavButton(sidebar, "Order Food/Items");
            addNavButton(sidebar, "My Order History");
            addNavButton(sidebar, "Delivery Progress");
            sidebar.add(Box.createVerticalGlue());
            addNavButton(sidebar, "Logout");

            add(sidebar, BorderLayout.WEST);

            // Main area
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(LIGHT_BG);

            // Header with restaurant logo and name
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(LIGHT_BG);
            header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
            leftHeader.setOpaque(false);
            JLabel restLogo = new JLabel();
            try {
                String logoPath = restaurant.equals("Jollibee") ? "GUIBuilder/resources/jollibee_logo.png" : "GUIBuilder/resources/mcdo_logo.png";
                ImageIcon icon = new ImageIcon(logoPath);
                restLogo.setIcon(new ImageIcon(icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
            } catch (Exception e) {}
            JLabel restName = new JLabel(restaurant + " Menu");
            restName.setFont(new Font("SansSerif", Font.BOLD, 28));
            leftHeader.add(restLogo);
            leftHeader.add(restName);
            header.add(leftHeader, BorderLayout.WEST);

            // Home and Back buttons at the top right
            JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            navPanel.setOpaque(false);
            JButton homeButton = new JButton("Home");
            JButton backButton = new JButton("Back");
            navPanel.add(homeButton);
            navPanel.add(backButton);
            header.add(navPanel, BorderLayout.EAST);

            mainPanel.add(header, BorderLayout.NORTH);

            // Food items in card panels
            JPanel foodPanel = new JPanel(new GridLayout(1, 3, 40, 40));
            foodPanel.setBackground(LIGHT_BG);
            foodPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

            if (restaurant.equals("Jollibee")) {
                foodPanel.add(createFoodCard("Chickenjoy", "GUIBuilder/resources/chickenjoy.png"));
                foodPanel.add(createFoodCard("Jolly Spaghetti", "GUIBuilder/resources/jolly_spaghetti.png"));
                foodPanel.add(createFoodCard("Burger Steak", "GUIBuilder/resources/burger_steak.png"));
            } else if (restaurant.equals("McDo")) {
                foodPanel.add(createFoodCard("Big Mac", "GUIBuilder/resources/big_mac.png"));
                foodPanel.add(createFoodCard("McSpaghetti", "GUIBuilder/resources/mcspaghetti.png"));
                foodPanel.add(createFoodCard("Fries", "GUIBuilder/resources/fries.png"));
            }

            mainPanel.add(foodPanel, BorderLayout.CENTER);

            // Home and Back button actions
            homeButton.addActionListener(e -> {
                new HomePage(user);
                this.dispose();
            });
            backButton.addActionListener(e -> dispose());

            add(mainPanel, BorderLayout.CENTER);
        }

        private JPanel createFoodCard(String foodName, String imagePath) {
            JPanel card = new JPanel();
            card.setBackground(CARD);
            card.setLayout(new BorderLayout());
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            try {
                ImageIcon icon = new ImageIcon(imagePath);
                imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
            } catch (Exception e) {}
            card.add(imageLabel, BorderLayout.CENTER);

            JLabel nameLabel = new JLabel(foodName, SwingConstants.CENTER);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
            card.add(nameLabel, BorderLayout.SOUTH);

            return card;
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        // For testing, pass a dummy user
        SwingUtilities.invokeLater(() -> new StoreList(new User("TestUser", "CUSTOMER")).setVisible(true));
    }
}
