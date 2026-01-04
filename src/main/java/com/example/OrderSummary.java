package com.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.util.List;

/**
 * Represents a grouped order summary for admin view.
 * Contains all items for one order session.
 */
public class OrderSummary {
    // Status constants
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_CANCELLED = "Cancelled";

    // JavaFX properties for TableView
    private final SimpleIntegerProperty orderId;
    private final SimpleIntegerProperty tableNumber;
    private final SimpleStringProperty date;
    private final SimpleDoubleProperty total;
    private final SimpleStringProperty status;

    // Business data
    private List<Order> orderItems;
    private String clientName;
    private String clientNumber;

    public OrderSummary(int orderId, int tableNumber, String date, double total, String status,
                       String clientName, String clientNumber, List<Order> orderItems) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.tableNumber = new SimpleIntegerProperty(tableNumber);
        this.date = new SimpleStringProperty(date);
        this.total = new SimpleDoubleProperty(total);
        this.status = new SimpleStringProperty(status);
        this.clientName = clientName;
        this.clientNumber = clientNumber;
        this.orderItems = orderItems;
    }

    // Property getters for JavaFX TableView binding
    public SimpleIntegerProperty orderIdProperty() { return orderId; }
    public SimpleIntegerProperty tableNumberProperty() { return tableNumber; }
    public SimpleStringProperty dateProperty() { return date; }
    public SimpleDoubleProperty totalProperty() { return total; }
    public SimpleStringProperty statusProperty() { return status; }

    // Regular getters
    public int getOrderId() { return orderId.get(); }
    public int getTableNumber() { return tableNumber.get(); }
    public String getDate() { return date.get(); }
    public double getTotal() { return total.get(); }
    public String getStatus() { return status.get(); }
    public String getClientName() { return clientName; }
    public String getClientNumber() { return clientNumber; }
    public List<Order> getOrderItems() { return orderItems; }

    // Regular setters
    public void setOrderId(int orderId) { this.orderId.set(orderId); }
    public void setTableNumber(int tableNumber) { this.tableNumber.set(tableNumber); }
    public void setDate(String date) { this.date.set(date); }
    public void setTotal(double total) { this.total.set(total); }
    public void setStatus(String status) { this.status.set(status); }
    public void setOrderItems(List<Order> orderItems) { this.orderItems = orderItems; }
}