import java.awt.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class StoreList extends JFrame {
    // Colors and fonts from MainCode
    private final Color PRIMARY = new Color(186, 55, 78);
    private final Color LIGHT_BG = new Color(248, 240, 240);
    private final Color CARD = Color.WHITE;
    private final Color SIDEBAR = new Color(120, 25, 45);
    private final User user;
    private String lastRestaurant = null;
    private final List<OrderItem> cart = new ArrayList<>();

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

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Select a Restaurant");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JLabel categoryHeader = new JLabel("Fast Food & Beverages");
        categoryHeader.setFont(new Font("SansSerif", Font.PLAIN, 16));
        categoryHeader.setForeground(new Color(100, 100, 100));

        JLabel selectedHeader = new JLabel("Selected: None");
        selectedHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        selectedHeader.setForeground(PRIMARY);


        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 6)));
        titlePanel.add(categoryHeader);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 8)));
        titlePanel.add(selectedHeader);


        header.add(titlePanel, BorderLayout.WEST);


        // Back button at the top right
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        navPanel.setOpaque(false);
        JButton backButton = new JButton("Back");
        navPanel.add(backButton);
        header.add(navPanel, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // Restaurant choices (center)
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(LIGHT_BG);
        centerPanel.setLayout(new GridLayout(2, 2, 40, 40));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        JButton jollibeeButton = createRestaurantButton("Jollibee", "https://live.staticflickr.com/3132/2906289773_83c466e953_q.jpg", "Fast Foods");
        JButton mcdoButton = createRestaurantButton("McDo", "https://pngimg.com/uploads/mcdonalds/mcdonalds_PNG1.png", "Fast Foods");
        JButton starbucksButton = createRestaurantButton("Starbucks", "https://mcdonough.com/wp-content/uploads/2020/09/starbucks-logo-png-transparent.png", "Beverages & Desserts");
        JButton dunkinButton = createRestaurantButton("Dunkin'", "https://vectorseek.com/wp-content/uploads/2025/01/Dunkin-Donuts-Badge-Logo-Vector.svg--300x300.png", "Beverages & Desserts");


        centerPanel.add(jollibeeButton);
        centerPanel.add(mcdoButton);
        centerPanel.add(starbucksButton);
        centerPanel.add(dunkinButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        jollibeeButton.addActionListener(e -> {
            lastRestaurant = "Jollibee";
            selectedHeader.setText("Selected: Jollibee (Fast Foods)");
            new RestaurantFrame(user, "Jollibee", cart).setVisible(true);
        });
        mcdoButton.addActionListener(e -> {
            lastRestaurant = "McDo";
            selectedHeader.setText("Selected: McDo (Fast Foods)");
            new RestaurantFrame(user, "McDo", cart).setVisible(true);
        });
        starbucksButton.addActionListener(e -> {
            lastRestaurant = "Starbucks";
            selectedHeader.setText("Selected: Starbucks (Beverages & Desserts)");
            new RestaurantFrame(user, "Starbucks", cart).setVisible(true);
        });
        dunkinButton.addActionListener(e -> {
            lastRestaurant = "Dunkin'";
            selectedHeader.setText("Selected: Dunkin' (Beverages & Desserts)");
            new RestaurantFrame(user, "Dunkin'", cart).setVisible(true);
        });


        backButton.addActionListener(e -> {
            if (lastRestaurant != null) {
                new RestaurantFrame(user, lastRestaurant, cart).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a restaurant first.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createRestaurantButton(String restaurantName, String imageUrl, String categoryLabel) {
        JButton button = new JButton();

        button.setLayout(new BorderLayout());
        button.setFont(new Font("SansSerif", Font.BOLD, 24));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD);
        panel.setOpaque(false);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            URL url = new URL(imageUrl);
            ImageIcon icon = new ImageIcon(url);
            imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            imageLabel.setText("Logo Not Found");
        }

        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel categoryLabelView = new JLabel(categoryLabel);
        categoryLabelView.setFont(new Font("SansSerif", Font.BOLD, 14));
        categoryLabelView.setForeground(new Color(120, 25, 45));
        categoryLabelView.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(imageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(categoryLabelView);
        panel.add(Box.createVerticalGlue());


        button.add(panel, BorderLayout.CENTER);
        button.setBackground(CARD);
        button.setFocusPainted(false);
        return button;
    }

    private void addNavButton(JPanel panel, String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(200, 50));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(Color.WHITE);
        btn.setForeground(text.equals("Logout") ? Color.RED : new Color(80, 80, 80));
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

    private double extractPrice(String priceString) {
        try {
            return Double.parseDouble(priceString.replace("\u20b1", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // RestaurantFrame inner class
    class RestaurantFrame extends JFrame {
        private final User user;
        private final List<OrderItem> cart;
        private JPanel cartItemsPanel;

        public RestaurantFrame(User user, String restaurant, List<OrderItem> cart) {
            this.user = user;
            this.cart = cart;
            setTitle("MoveEat - " + restaurant + " Menu");

            setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize immediately for restaurant frame

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

            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(LIGHT_BG);
            header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
            leftHeader.setOpaque(false);
            JLabel restLogo = new JLabel();
            try {
                URL logoUrl;
                if (restaurant.equals("Jollibee")) {
                    logoUrl = new URL("https://images.seeklogo.com/logo-png/7/1/jollibee-logo-png_seeklogo-75962.png");
                } else if (restaurant.equals("McDo")) {
                    logoUrl = new URL("https://pngimg.com/uploads/mcdonalds/mcdonalds_PNG1.png");
                } else if (restaurant.equals("Starbucks")) {
                    logoUrl = new URL("https://mcdonough.com/wp-content/uploads/2020/09/starbucks-logo-png-transparent.png");
                } else {
                    logoUrl = new URL("https://vectorseek.com/wp-content/uploads/2025/01/Dunkin-Donuts-Badge-Logo-Vector.svg--300x300.png");
                }
                ImageIcon icon = new ImageIcon(logoUrl);
                restLogo.setIcon(new ImageIcon(icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH))); // Increased from 60x60 to 80x80
            } catch (Exception e) {
                restLogo.setText("Logo");
            }
            JLabel restName = new JLabel((restaurant.equals("McDo") ? "McDonald's" : restaurant) + " Menu");
            restName.setFont(new Font("SansSerif", Font.BOLD, 28));

            JLabel restMeta = new JLabel(getCategoryLabelForRestaurant(restaurant));

            restMeta.setFont(new Font("SansSerif", Font.PLAIN, 14));
            restMeta.setForeground(new Color(100, 100, 100));

            JPanel metaPanel = new JPanel();
            metaPanel.setLayout(new BoxLayout(metaPanel, BoxLayout.Y_AXIS));
            metaPanel.setOpaque(false);
            metaPanel.add(restName);
            metaPanel.add(restMeta);

            leftHeader.add(restLogo);
            leftHeader.add(metaPanel);
            header.add(leftHeader, BorderLayout.WEST);


            JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            navPanel.setOpaque(false);
            JButton backButton = new JButton("Back");
            navPanel.add(backButton);
            header.add(navPanel, BorderLayout.EAST);

            mainPanel.add(header, BorderLayout.NORTH);

            JPanel foodPanel = new JPanel(new GridLayout(1, 3, 40, 40));
            foodPanel.setBackground(LIGHT_BG);
            foodPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

            if (restaurant.equals("Jollibee")) {
                foodPanel.add(createFoodCard("Chickenjoy", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSVSs2VmuBtUQYNPdT_SEMCq5snDSKig8y2nA&s", "\u20b199"));
                foodPanel.add(createFoodCard("Jolly Spaghetti", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJOmN-B_Fiq1bx1leYjBNGkhLZ_S-IYSBmkQ&s", "\u20b179"));
                foodPanel.add(createFoodCard("Burger Steak", "https://www.jollibeefoods.com/_next/image?url=https%3A%2F%2Folo-images-live.imgix.net%2F02%2F023d7994bd1e46a2aac95c11c1833a73.jpg%3Fauto%3Dformat%252Ccompress%26q%3D60%26cs%3Dtinysrgb%26w%3D1200%26h%3D800%26fit%3Dfill%26fm%3Dpng32%26bg%3Dtransparent%26s%3D966c5b696ebf78b634eee98ce089c190&w=1920&q=75", "\u20b189"));
            } else if (restaurant.equals("McDo")) {
                foodPanel.add(createFoodCard("Big Mac", "https://s7d1.scene7.com/is/image/mcdonalds/DC_202302_0005-999_BigMac_1564x1564-1:product-header-mobile?wid=1313&hei=1313&dpr=off", "\u20b1149"));
                foodPanel.add(createFoodCard("McSpaghetti", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZRLGmYoD9kfyt10_KoCSymY9M7V2lnxh-Jg&s", "\u20b159"));
                foodPanel.add(createFoodCard("Fries", "https://s7d1.scene7.com/is/image/mcdonalds/mcdonalds-fries-medium:1-3-product-tile-desktop?wid=829&hei=515&dpr=off", "\u20b149"));
            } else if (restaurant.equals("Starbucks")) {
                foodPanel.add(createFoodCard("Caffe Latte", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSnyIy7r7LywDZ6bJjuRteCs7La2oxbjP3aSA&s", "\u20b1180"));
                foodPanel.add(createFoodCard("Caramel Macchiato", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSZgO2y7szDOa9Tq5qROs9k5M5cf2HoMrIwww&s", "\u20b1210"));
                foodPanel.add(createFoodCard("Blueberry Muffin", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSNL9uCLD9W6NJwxpNC-5LtFXMktrYH-dLgTQ&s", "\u20b1120"));
            } else {
                foodPanel.add(createFoodCard("Donut", "https://w7.pngwing.com/pngs/371/702/png-transparent-stack-of-assorted-flavored-doughnuts-dunkin-donuts-coffee-and-doughnuts-iced-coffee-krispy-kreme-donuts-baked-goods-food-fast-food-restaurant-thumbnail.png", "\u20b190"));
                foodPanel.add(createFoodCard("Coffee", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTdN9R5-RLIu1zJoD0GD0t7cz6fDs5oWY-k7w&s", "\u20b1150"));
                foodPanel.add(createFoodCard("Bagel", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2o7UrQ7ZynsYNf08tvR9149iaP2TE2rnHtw&s", "\u20b1110"));
            }

            mainPanel.add(foodPanel, BorderLayout.CENTER);

            JPanel cartPanel = new JPanel(new BorderLayout());
            cartPanel.setBackground(LIGHT_BG);
            cartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel cartTitle = new JLabel("Your Cart");
            cartTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
            cartPanel.add(cartTitle, BorderLayout.NORTH);

            cartItemsPanel = new JPanel();
            // Keep items tightly packed; we control per-row spacing in updateCartDisplay()
            cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
            cartItemsPanel.setBackground(Color.WHITE);
            cartItemsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            JScrollPane cartScroll = new JScrollPane(cartItemsPanel);
            cartScroll.setPreferredSize(new Dimension(0, 200)); // Set height
            cartScroll.setBorder(BorderFactory.createEmptyBorder());
            cartPanel.add(cartScroll, BorderLayout.CENTER);


            JPanel cartBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton proceedButton = new JButton("Proceed to Checkout");
            proceedButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            proceedButton.setBackground(PRIMARY);
            proceedButton.setForeground(Color.WHITE);
            proceedButton.setFocusPainted(false);
            proceedButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            proceedButton.addActionListener(e -> {
                if (cart.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Cart is empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                new CheckOut(user, cart).setVisible(true);
                RestaurantFrame.this.dispose();
            });
            cartBottom.add(proceedButton);
            cartPanel.add(cartBottom, BorderLayout.SOUTH);

            mainPanel.add(cartPanel, BorderLayout.SOUTH);

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
                imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH)));
            } catch (Exception e) {
                imageLabel.setText("Image Not Found");
            }
            card.add(imageLabel, BorderLayout.CENTER);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);
            JLabel nameLabel = new JLabel(foodName, SwingConstants.CENTER);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            bottomPanel.add(nameLabel, BorderLayout.NORTH);
            
            JPanel priceAndButtonPanel = new JPanel(new BorderLayout());
            priceAndButtonPanel.setOpaque(false);
            
            JLabel priceLabel = new JLabel(price, SwingConstants.CENTER);
            priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            priceLabel.setForeground(Color.GRAY);
            priceAndButtonPanel.add(priceLabel, BorderLayout.NORTH);
            
            JPanel qtyPanel = new JPanel(new FlowLayout());
            qtyPanel.setOpaque(false);
            qtyPanel.add(new JLabel("Qty:"));
            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            qtyPanel.add(quantitySpinner);
            JButton addToCartButton = new JButton("Add to Cart");
            addToCartButton.setFont(new Font("SansSerif", Font.BOLD, 12));
            addToCartButton.setBackground(PRIMARY);
            addToCartButton.setForeground(Color.WHITE);
            addToCartButton.setFocusPainted(false);
            addToCartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addToCartButton.addActionListener(e -> {
                double itemPrice = extractPrice(price);
                int qty = (Integer) quantitySpinner.getValue();
                boolean found = false;
                for (OrderItem item : cart) {
                    if (item.name.equals(foodName)) {
                        item.quantity += qty;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    cart.add(new OrderItem(foodName, itemPrice, qty));
                }
                updateCartDisplay();
            });
            qtyPanel.add(addToCartButton);
            priceAndButtonPanel.add(qtyPanel, BorderLayout.SOUTH);
            
            bottomPanel.add(priceAndButtonPanel, BorderLayout.SOUTH);
            card.add(bottomPanel, BorderLayout.SOUTH);

            return card;
        }

        private void updateCartDisplay() {
            cartItemsPanel.removeAll();
            for (OrderItem item : cart) {
                JPanel row = new JPanel(new BorderLayout());
                // Tight spacing between cart rows (helps when picking the first few items)
                row.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                row.setBackground(Color.WHITE);
                JLabel name = new JLabel(item.name);

                name.setFont(new Font("SansSerif", Font.BOLD, 14));

                JLabel qty = new JLabel("Qty: " + item.quantity);
                qty.setFont(new Font("SansSerif", Font.PLAIN, 12));

                JLabel price = new JLabel("\u20b1" + String.format("%.2f", item.price));
                price.setFont(new Font("SansSerif", Font.PLAIN, 12));

                JLabel total = new JLabel("\u20b1" + String.format("%.2f", item.getTotal()));
                total.setFont(new Font("SansSerif", Font.BOLD, 14));
                total.setForeground(PRIMARY);

                JButton removeBtn = new JButton("Remove");
                removeBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
                removeBtn.setBackground(new Color(255, 100, 100));
                removeBtn.setForeground(Color.WHITE);
                removeBtn.setFocusPainted(false);
                removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                removeBtn.addActionListener(e -> {
                    cart.remove(item);
                    updateCartDisplay();
                });

                JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                left.setOpaque(false);
                left.add(name);
                left.add(qty);

                JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                right.setOpaque(false);
                right.add(price);
                right.add(total);
                right.add(removeBtn);

                row.add(left, BorderLayout.WEST);
                row.add(right, BorderLayout.EAST);
                cartItemsPanel.add(row);
            }
            cartItemsPanel.revalidate();
            cartItemsPanel.repaint();
        }
    }

    private String getCategoryLabelForRestaurant(String restaurant) {
        switch (restaurant) {
            case "Jollibee":
            case "McDo":
                return "Category: Fast Foods";
            case "Starbucks":
            case "Dunkin'":
                return "Category: Beverages & Desserts";
            default:
                return "Category: Restaurant";
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        // For testing, pass a dummy user
        SwingUtilities.invokeLater(() -> new StoreList(new User("TestUser", "CUSTOMER", false)).setVisible(true));
    }

}