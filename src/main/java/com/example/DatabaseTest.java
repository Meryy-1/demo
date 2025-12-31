package com.example;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("=== Database Test ===\n");

        // Initialize database
        DatabaseManager.initialize();

        // Test 1: Add new client
        System.out.println("Test 1: Adding new client 'John Doe' with number '12345'");
        boolean added = DatabaseManager.addClient("John Doe", "12345");
        System.out.println("Result: " + (added ? "Success" : "Failed") + "\n");

        // Test 2: Check if client exists
        System.out.println("Test 2: Checking if client 'John Doe' with number '12345' exists");
        boolean exists = DatabaseManager.clientExists("John Doe", "12345");
        System.out.println("Result: " + (exists ? "Client exists" : "Client not found") + "\n");

        // Test 3: Try adding same client again (should fail due to UNIQUE constraint)
        System.out.println("Test 3: Trying to add same client again");
        boolean duplicateAdd = DatabaseManager.addClient("John Doe", "12345");
        System.out.println("Result: " + (duplicateAdd ? "Success" : "Failed (expected - duplicate)") + "\n");

        // Test 4: Verify default admin
        System.out.println("Test 4: Verifying default admin (code: 'admin', password: 'admin123')");
        boolean adminValid = DatabaseManager.verifyAdmin("admin", "admin123");
        System.out.println("Result: " + (adminValid ? "Admin verified" : "Admin not found") + "\n");

        // Test 5: Try invalid admin credentials
        System.out.println("Test 5: Testing invalid admin credentials");
        boolean invalidAdmin = DatabaseManager.verifyAdmin("admin", "wrongpassword");
        System.out.println(
                "Result: " + (invalidAdmin ? "Verified (unexpected)" : "Invalid credentials (expected)") + "\n");

        // Close database
        DatabaseManager.close();

        System.out.println("=== All tests completed ===");
    }
}
