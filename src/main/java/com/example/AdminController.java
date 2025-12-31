package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;

public class AdminController {
    
    @FXML
    private Label adminCodeLabel;
    
    @FXML
    private Label statusLabel;
    
    private String adminCode;
    
    // Set admin information when navigating to this page
    public void setAdminInfo(String code) {
        this.adminCode = code;
        adminCodeLabel.setText("Admin: " + code);
    }
    
    @FXML
    private void handleLogout() {
        try {
            App.setRoot("main");
        } catch (IOException e) {
            statusLabel.setText("Error logging out");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleManageClients() {
        statusLabel.setText("Manage Clients feature coming soon...");
        statusLabel.setStyle("-fx-text-fill: #6c757d;");
    }
    
    @FXML
    private void handleManageOrders() {
        statusLabel.setText("Manage Orders feature coming soon...");
        statusLabel.setStyle("-fx-text-fill: #6c757d;");
    }
    
    @FXML
    private void handleManageMenu() {
        statusLabel.setText("Manage Menu feature coming soon...");
        statusLabel.setStyle("-fx-text-fill: #6c757d;");
    }
    
    @FXML
    private void handleViewReports() {
        statusLabel.setText("View Reports feature coming soon...");
        statusLabel.setStyle("-fx-text-fill: #6c757d;");
    }
    
    @FXML
    private void handleSettings() {
        statusLabel.setText("Settings feature coming soon...");
        statusLabel.setStyle("-fx-text-fill: #6c757d;");
    }
}
