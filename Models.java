import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

// New class for the Timeline history
class TrackingUpdate {
    public String status;
    public String time;
    public String location;

    public TrackingUpdate(String status, String location) {
        this.status = status;
        this.location = location;
        this.time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
    }
}

class Order {
    public String orderId;
    public String trackingNumber; 
    public String customerName;
    public String address;
    public String phone;
    public List<OrderItem> items;
    public double totalAmount;
    public String paymentMethod;
    public String status; // "Order placed", "Preparing", "Shipped", etc.
    public String orderDate;
    
    // New fields for Delivery Progress
    public String estimatedDelivery;
    public String courierName;
    public String courierPhone;
    public List<TrackingUpdate> timeline;

    public Order(String orderId, String customerName, String address, String phone,
                 List<OrderItem> items, double totalAmount, String paymentMethod) {
        this.orderId = orderId;
        // Generates a random tracking number like SHP-1234
        this.trackingNumber = "SHP-" + (int)(Math.random() * 9000 + 1000); 
        this.customerName = customerName;
        this.address = address;
        this.phone = phone;
        this.items = items;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = "Order placed"; 
        this.orderDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        
        // Defaults for progress page
        this.estimatedDelivery = LocalDateTime.now().plusDays(3).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
        this.courierName = "Kenneth Rider";
        this.courierPhone = "0912-345-6789";
        
        this.timeline = new ArrayList<>();
        this.timeline.add(new TrackingUpdate("Order placed", "Warehouse"));
    }

    // Call this method to move the progress forward
    public void updateStatus(String newStatus, String location) {
        this.status = newStatus;
        this.timeline.add(0, new TrackingUpdate(newStatus, location)); 
    }
}