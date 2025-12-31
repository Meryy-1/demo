package com.example;

import java.sql.*;

public class ShowDatabaseData {
    public static void main(String[] args) {
        DatabaseManager.initialize();

        System.out.println("========================================");
        System.out.println("        DATABASE CONTENTS");
        System.out.println("========================================\n");

        // Show Client table
        System.out.println("CLIENT TABLE:");
        System.out.println("----------------------------------------");
        showClientTable();

        System.out.println("\n\nADMIN TABLE:");
        System.out.println("----------------------------------------");
        showAdminTable();

        DatabaseManager.close();
    }

    private static void showClientTable() {
        String query = "SELECT id, name, number FROM client ORDER BY id";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            int count = 0;
            System.out.printf("%-5s | %-20s | %-15s%n", "ID", "Name", "Number");
            System.out.println("------|----------------------|-----------------");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String number = rs.getString("number");
                System.out.printf("%-5d | %-20s | %-15s%n", id, name, number);
                count++;
            }

            if (count == 0) {
                System.out.println("No clients in database");
            } else {
                System.out.println("\nTotal clients: " + count);
            }

        } catch (SQLException e) {
            System.err.println("Error reading client table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showAdminTable() {
        String query = "SELECT admin_id, admin_code, admin_password FROM admin ORDER BY admin_id";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            int count = 0;
            System.out.printf("%-10s | %-20s | %-20s%n", "Admin ID", "Admin Code", "Password");
            System.out.println("-----------|----------------------|----------------------");

            while (rs.next()) {
                int adminId = rs.getInt("admin_id");
                String adminCode = rs.getString("admin_code");
                String password = rs.getString("admin_password");
                System.out.printf("%-10d | %-20s | %-20s%n", adminId, adminCode, password);
                count++;
            }

            if (count == 0) {
                System.out.println("No admins in database");
            } else {
                System.out.println("\nTotal admins: " + count);
            }

        } catch (SQLException e) {
            System.err.println("Error reading admin table: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
