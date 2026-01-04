package com.example;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Represents an individual item within an order.
 * Uses JavaFX properties for automatic UI updates in TableView.
 * Automatically calculates subtotal when quantity or price changes.
 */
public class OrderItem {
    // JavaFX properties enable automatic TableView updates
    private final SimpleStringProperty itemName;
    private final SimpleIntegerProperty quantity;
    private final SimpleDoubleProperty price;
    private final SimpleDoubleProperty subtotal;

    /**
     * Creates a new order item with automatic subtotal calculation.
     * @param itemName The name of the menu item
     * @param quantity How many of this item
     * @param price Price per item
     */
    public OrderItem(String itemName, int quantity, double price) {
        this.itemName = new SimpleStringProperty(itemName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        // Calculate subtotal immediately: quantity × price
        this.subtotal = new SimpleDoubleProperty(quantity * price);
    }

    // Property getters for JavaFX TableView binding
    public SimpleStringProperty itemNameProperty() { return itemName; }
    public SimpleIntegerProperty quantityProperty() { return quantity; }
    public SimpleDoubleProperty priceProperty() { return price; }
    public SimpleDoubleProperty subtotalProperty() { return subtotal; }

    // Regular getters for business logic
    public String getItemName() { return itemName.get(); }
    public int getQuantity() { return quantity.get(); }
    public double getPrice() { return price.get(); }
    public double getSubtotal() { return subtotal.get(); }

    // Regular setters with automatic subtotal recalculation
    public void setItemName(String itemName) { this.itemName.set(itemName); }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
        updateSubtotal(); // Recalculate when quantity changes
    }

    public void setPrice(double price) {
        this.price.set(price);
        updateSubtotal(); // Recalculate when price changes
    }

    /**
     * Updates the subtotal when quantity or price changes.
     * Called automatically by setters.
     */
    private void updateSubtotal() {
        subtotal.set(quantity.get() * price.get());
    }
}