package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminReportsController {

    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private Button filterButton;

    @FXML
    private Label totalClientsLabel;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label totalRevenueLabel;

    @FXML
    private Label mostOrderedItemLabel;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // Set default date range (last 30 days)
        toDatePicker.setValue(LocalDate.now());
        fromDatePicker.setValue(LocalDate.now().minusDays(30));

        loadReports(null, null);
    }

    @FXML
    void handleFilter() {
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            statusLabel.setText("Error: From date cannot be after To date");
            return;
        }

        loadReports(fromDate, toDate);
    }

    private void loadReports(LocalDate fromDate, LocalDate toDate) {
        try {
            int totalClients = getTotalClients();
            int totalOrders = getTotalOrders(fromDate, toDate);
            double totalRevenue = getTotalRevenue(fromDate, toDate);
            String mostOrderedItem = getMostOrderedItem(fromDate, toDate);

            totalClientsLabel.setText("Total Clients: " + totalClients);
            totalOrdersLabel.setText("Total Orders: " + totalOrders);
            totalRevenueLabel.setText("Total Revenue: $" + String.format("%.2f", totalRevenue));
            mostOrderedItemLabel.setText("Most Ordered Item: " + mostOrderedItem);

            String dateRange = (fromDate != null && toDate != null) ?
                " (" + fromDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " to " +
                toDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ")" : " (All time)";
            statusLabel.setText("Reports loaded successfully" + dateRange);

        } catch (SQLException e) {
            statusLabel.setText("Error loading reports: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int getTotalClients() throws SQLException {
        String query = "SELECT COUNT(*) as count FROM client";
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next() ? rs.getInt("count") : 0;
        }
    }

    private int getTotalOrders(LocalDate fromDate, LocalDate toDate) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT COUNT(*) as count FROM orders");
        if (fromDate != null && toDate != null) {
            query.append(" WHERE date(order_date) BETWEEN '")
                 .append(fromDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                 .append("' AND '")
                 .append(toDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                 .append("'");
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {
            return rs.next() ? rs.getInt("count") : 0;
        }
    }

    private double getTotalRevenue(LocalDate fromDate, LocalDate toDate) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT SUM(quantity * price) as revenue FROM orders");
        if (fromDate != null && toDate != null) {
            query.append(" WHERE date(order_date) BETWEEN '")
                 .append(fromDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                 .append("' AND '")
                 .append(toDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                 .append("'");
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {
            return rs.next() ? rs.getDouble("revenue") : 0.0;
        }
    }

    private String getMostOrderedItem(LocalDate fromDate, LocalDate toDate) throws SQLException {
        StringBuilder query = new StringBuilder(
            "SELECT item_name, SUM(quantity) as total_quantity " +
            "FROM orders");

        if (fromDate != null && toDate != null) {
            query.append(" WHERE date(order_date) BETWEEN '")
                 .append(fromDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                 .append("' AND '")
                 .append(toDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                 .append("'");
        }

        query.append(" GROUP BY item_name ORDER BY total_quantity DESC LIMIT 1");

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {
            if (rs.next()) {
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("total_quantity");
                return itemName + " (" + quantity + " orders)";
            }
            return "None";
        }
    }

    @FXML
    void handleBack() {
        try {
            App.setRoot("admin");
        } catch (Exception e) {
            statusLabel.setText("Error going back");
            e.printStackTrace();
        }
    }
}