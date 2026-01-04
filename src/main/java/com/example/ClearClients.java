package com.example;

public class ClearClients {
    public static void main(String[] args) {
        DatabaseManager.initialize();
        boolean success = DatabaseManager.clearClientsTable();

        if (success) {
            System.out.println("Successfully cleared all clients from the database!");
        } else {
            System.out.println("Failed to clear clients table.");
        }

        DatabaseManager.close();
    }
}
