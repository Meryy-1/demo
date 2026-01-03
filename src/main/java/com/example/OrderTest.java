package com.example;

import java.util.List;

import java.util.List;

public class OrderTest {
    public static void main(String[] args) {
        System.out.println("=== Order and Reservation Test ===\n");

        // Initialize database
        DatabaseManager.initialize();

        String clientName = "John Doe";
        String clientNumber = "12345";

        // Test 1: Add reservation
        System.out.println("Test 1: Adding reservation for " + clientName + " " + clientNumber + " at table 1");
        boolean reservationAdded = DatabaseManager.addReservation(clientName, clientNumber, 1, 4);
        System.out.println("Reservation added: " + (reservationAdded ? "Success" : "Failed"));

        // Test 2: Check table number retrieval
        System.out.println("\nTest 2: Checking table number for " + clientName + " " + clientNumber);
        // This will trigger the debug output in getClientTableNumber
        int tableNumber = DatabaseManager.getClientTableNumber(clientName, clientNumber);
        System.out.println("Retrieved table number: " + tableNumber);

        // Test 3: Add order
        System.out.println("\nTest 3: Adding order for " + clientName + " " + clientNumber);
        boolean orderAdded = DatabaseManager.addOrder(clientName, clientNumber, "Pizza", 2, 15.99, "2024-01-15 12:30:00");
        System.out.println("Order added: " + (orderAdded ? "Success" : "Failed"));

        // Test 4: Check order summaries
        System.out.println("\nTest 4: Retrieving order summaries");
        java.util.List<OrderSummary> summaries = DatabaseManager.getOrderSummaries();
        System.out.println("Found " + summaries.size() + " order summaries:");
        for (OrderSummary summary : summaries) {
            System.out.println("  Order " + summary.getOrderId() + " at table " + summary.getTableNumber() + " total $" + summary.getTotal());
        }

        // Close database
        DatabaseManager.close();

        System.out.println("\n=== All tests completed ===");
    }
}