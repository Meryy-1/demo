package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OrderService {
    // Static ObservableList to store all orders
    private static final ObservableList<Order> allOrders = FXCollections.observableArrayList();

    // Static initializer to add some sample data
    static {
        initializeSampleData();
    }

    /**
     * Get all orders
     * @return ObservableList of all orders
     */
    public static ObservableList<Order> getAllOrders() {
        return allOrders;
    }

    /**
     * Get order items for a specific order
     * @param order The order to get items for
     * @return ObservableList of order items
     */
    public static ObservableList<OrderItem> getItemsForOrder(Order order) {
        return order.getOrderItems();
    }

    /**
     * Mark an order as completed
     * @param order The order to mark as completed
     */
    public static void markOrderCompleted(Order order) {
        order.setStatus(Order.STATUS_COMPLETED);
    }

    /**
     * Cancel an order
     * @param order The order to cancel
     */
    public static void cancelOrder(Order order) {
        order.setStatus(Order.STATUS_CANCELLED);
    }

    /**
     * Add a new order to the service
     * @param order The order to add
     */
    public static void addOrder(Order order) {
        allOrders.add(order);
    }

    /**
     * Remove an order from the service
     * @param order The order to remove
     */
    public static void removeOrder(Order order) {
        allOrders.remove(order);
    }

    /**
     * Get an order by ID
     * @param orderId The order ID to search for
     * @return The order if found, null otherwise
     */
    public static Order getOrderById(int orderId) {
        return allOrders.stream()
                .filter(order -> order.getOrderId() == orderId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Initialize sample data for demonstration
     */
    private static void initializeSampleData() {
        // Sample menu items with realistic prices
        String[] menuItems = {
            "Grilled Salmon", "Ribeye Steak", "Chicken Parmesan", "Vegetarian Pasta",
            "Caesar Salad", "French Fries", "Mashed Potatoes", "Garlic Bread",
            "Chocolate Cake", "Cheesecake", "Tiramisu", "Ice Cream",
            "Coffee", "Tea", "Soda", "Beer", "Wine"
        };

        double[] prices = {
            24.99, 32.99, 18.99, 16.99,
            12.99, 6.99, 7.99, 5.99,
            8.99, 7.99, 9.99, 6.99,
            3.99, 2.99, 2.49, 5.99, 8.99
        };

        // Create 6 sample orders with varied items and statuses
        Order order1 = new Order(1, 5, "2024-01-15 12:30:00", Order.STATUS_COMPLETED);
        order1.addOrderItem(new OrderItem("Ribeye Steak", 1, 32.99));
        order1.addOrderItem(new OrderItem("Caesar Salad", 1, 12.99));
        order1.addOrderItem(new OrderItem("Garlic Bread", 2, 5.99));
        order1.addOrderItem(new OrderItem("Chocolate Cake", 1, 8.99));

        Order order2 = new Order(2, 3, "2024-01-15 13:15:00", Order.STATUS_PENDING);
        order2.addOrderItem(new OrderItem("Grilled Salmon", 1, 24.99));
        order2.addOrderItem(new OrderItem("French Fries", 1, 6.99));
        order2.addOrderItem(new OrderItem("Soda", 2, 2.49));

        Order order3 = new Order(3, 7, "2024-01-15 14:00:00", Order.STATUS_COMPLETED);
        order3.addOrderItem(new OrderItem("Chicken Parmesan", 1, 18.99));
        order3.addOrderItem(new OrderItem("Mashed Potatoes", 1, 7.99));
        order3.addOrderItem(new OrderItem("Ice Cream", 1, 6.99));
        order3.addOrderItem(new OrderItem("Coffee", 1, 3.99));

        Order order4 = new Order(4, 2, "2024-01-15 14:45:00", Order.STATUS_PENDING);
        order4.addOrderItem(new OrderItem("Vegetarian Pasta", 1, 16.99));
        order4.addOrderItem(new OrderItem("Garlic Bread", 1, 5.99));
        order4.addOrderItem(new OrderItem("Cheesecake", 1, 7.99));

        Order order5 = new Order(5, 8, "2024-01-15 15:30:00", Order.STATUS_COMPLETED);
        order5.addOrderItem(new OrderItem("Ribeye Steak", 2, 32.99));
        order5.addOrderItem(new OrderItem("Caesar Salad", 2, 12.99));
        order5.addOrderItem(new OrderItem("Wine", 1, 8.99));
        order5.addOrderItem(new OrderItem("Tiramisu", 2, 9.99));

        Order order6 = new Order(6, 1, "2024-01-15 16:00:00", Order.STATUS_PENDING);
        order6.addOrderItem(new OrderItem("Grilled Salmon", 1, 24.99));
        order6.addOrderItem(new OrderItem("French Fries", 1, 6.99));
        order6.addOrderItem(new OrderItem("Beer", 2, 5.99));

        Order order7 = new Order(7, 6, "2024-01-15 16:30:00", Order.STATUS_COMPLETED);
        order7.addOrderItem(new OrderItem("Chicken Parmesan", 1, 18.99));
        order7.addOrderItem(new OrderItem("Mashed Potatoes", 1, 7.99));
        order7.addOrderItem(new OrderItem("Tea", 1, 2.99));

        Order order8 = new Order(8, 4, "2024-01-15 17:00:00", Order.STATUS_PENDING);
        order8.addOrderItem(new OrderItem("Vegetarian Pasta", 1, 16.99));
        order8.addOrderItem(new OrderItem("Caesar Salad", 1, 12.99));
        order8.addOrderItem(new OrderItem("Soda", 1, 2.49));
        order8.addOrderItem(new OrderItem("Ice Cream", 1, 6.99));
        order8.addOrderItem(new OrderItem("Coffee", 1, 3.99));

        allOrders.addAll(order1, order2, order3, order4, order5, order6, order7, order8);
    }
}