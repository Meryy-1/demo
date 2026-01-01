package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private int id;
    private String clientName;
    private String clientNumber;
    private String itemName;
    private int quantity;
    private double price;
    private String orderDate;
    private double totalPrice;

    public Order(int id, String clientName, String clientNumber, String itemName,
            int quantity, double price, String orderDate) {
        this.id = id;
        this.clientName = clientName;
        this.clientNumber = clientNumber;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.orderDate = orderDate;
        this.totalPrice = quantity * price;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientNumber() {
        return clientNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    // Static method to get current timestamp
    public static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
