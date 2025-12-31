package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.io.IOException;

public class ClientController {

    @FXML
    private Label clientNameLabel;

    @FXML
    private Label clientNumberLabel;

    @FXML
    private Label statusLabel;

    private String clientName;
    private String clientNumber;

    // Set client information when navigating to this page
    public void setClientInfo(String name, String number) {
        this.clientName = name;
        this.clientNumber = number;
        clientNameLabel.setText("Welcome, " + name);
        clientNumberLabel.setText("Number: " + number);
        // Store in App for navigation
        App.setCurrentClient(name, number);
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
    private void handleViewMenu() {
        try {
            App.setRoot("menu");
        } catch (Exception e) {
            statusLabel.setText("Error loading menu");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMyOrders() {
        statusLabel.setText("Orders feature coming soon...");
        statusLabel.setStyle("-fx-text-fill: #6c757d;");
    }

    @FXML
    private void handleReservation() {
        statusLabel.setText("Reservation feature coming soon...");
        statusLabel.setStyle("-fx-text-fill: #6c757d;");
    }

    @FXML
    private void handleProfile() {
        statusLabel.setText("Profile feature coming soon...");
        statusLabel.setStyle("-fx-text-fill: #6c757d;");
    }
}
