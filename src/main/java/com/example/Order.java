package com.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single order item from the database.
 * Each Order represents one item ordered by a client.
 */
public class Order {
    // Status constants
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_CANCELLED = "Cancelled";

    // JavaFX properties for TableView automatic updates
    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty orderId;
    private final SimpleStringProperty clientName;
    private final SimpleStringProperty clientNumber;
    private final SimpleIntegerProperty tableNumber;
    private final SimpleStringProperty itemName;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty price;
    private final SimpleDoubleProperty totalPrice;
    private final SimpleStringProperty orderDate;
    private final SimpleStringProperty status;

    /**
     * Creates a new Order from database data.
     */
    public Order(int id, int orderId, String clientName, String clientNumber, int tableNumber,
                 String itemName, int quantity, double price, String orderDate, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.orderId = new SimpleIntegerProperty(orderId);
        this.clientName = new SimpleStringProperty(clientName);
        this.clientNumber = new SimpleStringProperty(clientNumber);
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
        this.itemName = new SimpleStringProperty(itemName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.totalPrice = new SimpleDoubleProperty(quantity * price);
        this.orderDate = new SimpleStringProperty(orderDate);
        this.status = new SimpleStringProperty(status);
    }

    /**
     * Backward compatibility constructor for OrderService
     */
    public Order(int orderId, int tableNumber, String orderDate, String status) {
        this(0, orderId, "", "", tableNumber, "", 0, 0.0, orderDate, status);
    }

    // Property getters for JavaFX TableView binding (PropertyValueFactory)
    public SimpleIntegerProperty idProperty() { return id; }
    public SimpleIntegerProperty orderIdProperty() { return orderId; }
    public SimpleStringProperty clientNameProperty() { return clientName; }
    public SimpleStringProperty clientNumberProperty() { return clientNumber; }
    public SimpleIntegerProperty tableNumberProperty() { return tableNumber; }
    public SimpleStringProperty itemNameProperty() { return itemName; }
    public SimpleIntegerProperty quantityProperty() { return quantity; }
    public SimpleDoubleProperty priceProperty() { return price; }
    public SimpleDoubleProperty totalPriceProperty() { return totalPrice; }
    public SimpleStringProperty orderDateProperty() { return orderDate; }
    public SimpleStringProperty statusProperty() { return status; }

    // Regular getters for business logic
    public int getId() { return id.get(); }
    public int getOrderId() { return orderId.get(); }
    public String getClientName() { return clientName.get(); }
    public String getClientNumber() { return clientNumber.get(); }
    public int getTableNumber() { return tableNumber.get(); }
    public String getItemName() { return itemName.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getPrice() { return price.get(); }
    public double getTotalPrice() { return totalPrice.get(); }
    public String getOrderDate() { return orderDate.get(); }
    public String getStatus() { return status.get(); }

    // Regular setters
    public void setId(int id) { this.id.set(id); }
    public void setOrderId(int orderId) { this.orderId.set(orderId); }
    public void setClientName(String clientName) { this.clientName.set(clientName); }
    public void setClientNumber(String clientNumber) { this.clientNumber.set(clientNumber); }
    public void setTableNumber(int tableNumber) { this.tableNumber.set(tableNumber); }
    public void setItemName(String itemName) { this.itemName.set(itemName); }
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        this.totalPrice.set(quantity * price.get());
    }
    public void setPrice(double price) {
        this.price.set(price);
        this.totalPrice.set(quantity.get() * price);
    }
    public void setOrderDate(String orderDate) { this.orderDate.set(orderDate); }
    public void setStatus(String status) { this.status.set(status); }

    /**
     * Utility method to get current timestamp for new orders.
     */
    public static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    // Additional methods for compatibility
    public javafx.collections.ObservableList<OrderItem> getOrderItems() { return javafx.collections.FXCollections.observableArrayList(); }
    public void addOrderItem(OrderItem item) { }
}
