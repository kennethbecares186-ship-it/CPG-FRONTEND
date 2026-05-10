import java.util.*;

// --- Data Model ---
class User {
    String username;
    String role; // "ADMIN" or "CUSTOMER"
    boolean isNewUser; // True if created via signup
    List<Order> orders;

    User(String username, String role, boolean isNewUser) {
        this.username = username;
        this.role = role;
        this.isNewUser = isNewUser;
        this.orders = new ArrayList<>();
    }
}

// Order Item class
class OrderItem {
    public String name;
    public double price;
    public int quantity;

    public OrderItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public double getTotal() {
        return price * quantity;
    }
}

// Order class
class Order {
    public String orderId;
    public String customerName;
    public String address;
    public String phone;
    public List<OrderItem> items;
    public double totalAmount;
    public String paymentMethod;
    public String status;
    public String orderDate;

    public Order(String orderId, String customerName, String address, String phone,
                 List<OrderItem> items, double totalAmount, String paymentMethod) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = "Delivered"; // For history, assume delivered
        this.orderDate = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }
}