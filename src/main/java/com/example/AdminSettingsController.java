package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import java.io.IOException;

public class AdminSettingsController {

    // Admin Information
    @FXML
    private Label adminNameLabel;

    @FXML
    private Label adminRoleLabel;

    // Security
    @FXML
    private Button changePasswordButton;

    // Application Info
    @FXML
    private Label versionLabel;

    // Logout
    @FXML
    private Button logoutButton;

    @FXML
    private Label statusLabel;

    private String adminCode;

    @FXML
    public void initialize() {
        // Set default values
        adminNameLabel.setText("Admin");
        adminRoleLabel.setText("Administrator");
        versionLabel.setText("1.0.0");

        statusLabel.setText("Settings loaded successfully");
    }

    public void setAdminCode(String code) {
        this.adminCode = code;
        adminNameLabel.setText("Admin (" + code + ")");
    }

    @FXML
    private void handleChangePassword() {
        // For now, just show a message
        statusLabel.setText("Change password functionality coming soon!");
        statusLabel.setStyle("-fx-text-fill: #f39c12;");
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
    private void handleBack() {
        try {
            App.setRoot("admin");
        } catch (Exception e) {
            statusLabel.setText("Error going back");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            e.printStackTrace();
        }
    }
}