import java.awt.*;
import java.net.URL;
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

        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize immediately

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
            URL url = new URL("https://live.staticflickr.com/3132/2906289773_83c466e953_q.jpg"); // Derived direct image from Flickr link
            ImageIcon jollibeeIcon = new ImageIcon(url);
            jollibeeButton.setIcon(new ImageIcon(jollibeeIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH))); // Increased from 80x80 to 100x100
        } catch (Exception e) {
            jollibeeButton.setText("Logo Not Found");
        }
        jollibeeButton.setBackground(CARD);
        jollibeeButton.setFocusPainted(false);
        centerPanel.add(jollibeeButton);

        // McDo button with logo
        JButton mcdoButton = new JButton("McDo");
        mcdoButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        try {
            URL url = new URL("https://pngimg.com/uploads/mcdonalds/mcdonalds_PNG1.png"); // Direct from URL
            ImageIcon mcdoIcon = new ImageIcon(url);
            mcdoButton.setIcon(new ImageIcon(mcdoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH))); // Increased from 80x80 to 100x100
        } catch (Exception e) {
            mcdoButton.setText("Logo Not Found");
        }
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

            setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize immediately for restaurant frame

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
                URL logoUrl = restaurant.equals("Jollibee") ? new URL("https://live.staticflickr.com/3132/2906289773_83c466e953_q.jpg") : new URL("https://pngimg.com/uploads/mcdonalds/mcdonalds_PNG1.png");
                ImageIcon icon = new ImageIcon(logoUrl);
                restLogo.setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH))); // Increased from 60x60 to 80x80
            } catch (Exception e) {
                restLogo.setText("Logo");
            }
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
                foodPanel.add(createFoodCard("Chickenjoy", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSVSs2VmuBtUQYNPdT_SEMCq5snDSKig8y2nA&s", "₱99")); // Placeholder; replace with direct Chickenjoy URL
                foodPanel.add(createFoodCard("Jolly Spaghetti", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJOmN-B_Fiq1bx1leYjBNGkhLZ_S-IYSBmkQ&s", "₱79")); // Placeholder; replace with direct URL
                foodPanel.add(createFoodCard("Burger Steak", "https://www.jollibeefoods.com/_next/image?url=https%3A%2F%2Folo-images-live.imgix.net%2F02%2F023d7994bd1e46a2aac95c11c1833a73.jpg%3Fauto%3Dformat%252Ccompress%26q%3D60%26cs%3Dtinysrgb%26w%3D1200%26h%3D800%26fit%3Dfill%26fm%3Dpng32%26bg%3Dtransparent%26s%3D966c5b696ebf78b634eee98ce089c190&w=1920&q=75", "₱89")); // Direct from PNGEgg
            } else if (restaurant.equals("McDo")) {
                foodPanel.add(createFoodCard("Big Mac", "https://s7d1.scene7.com/is/image/mcdonalds/DC_202302_0005-999_BigMac_1564x1564-1:product-header-mobile?wid=1313&hei=1313&dpr=off", "₱149")); // Placeholder; replace with direct URL
                foodPanel.add(createFoodCard("McSpaghetti", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZRLGmYoD9kfyt10_KoCSymY9M7V2lnxh-Jg&s", "₱59")); // Placeholder; replace with direct URL
                foodPanel.add(createFoodCard("Fries", "https://s7d1.scene7.com/is/image/mcdonalds/mcdonalds-fries-medium:1-3-product-tile-desktop?wid=829&hei=515&dpr=off", "₱49")); // Placeholder; replace with direct URL
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

        private JPanel createFoodCard(String foodName, String imageUrl, String price) {
            JPanel card = new JPanel();
            card.setBackground(CARD);
            card.setLayout(new BorderLayout());
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

            JLabel imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            try {
                URL url = new URL(imageUrl);
                ImageIcon icon = new ImageIcon(url);
                imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH))); // Increased from 100x100 to 150x150
            } catch (Exception e) {
                imageLabel.setText("Image Not Found");
            }
            card.add(imageLabel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);
            JLabel nameLabel = new JLabel(foodName, SwingConstants.CENTER);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            bottomPanel.add(nameLabel, BorderLayout.NORTH);
            
            JLabel priceLabel = new JLabel(price, SwingConstants.CENTER);
            priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            priceLabel.setForeground(Color.GRAY);
            bottomPanel.add(priceLabel, BorderLayout.SOUTH);
            
            card.add(bottomPanel, BorderLayout.SOUTH);

            return card;
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        // For testing, pass a dummy user
        SwingUtilities.invokeLater(() -> new StoreList(new User("TestUser", "CUSTOMER")).setVisible(true));
    }
}