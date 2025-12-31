package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class MainController {

    @FXML
    private Button clientOptionBtn;

    @FXML
    private Button adminOptionBtn;

    @FXML
    private VBox clientSection;

    @FXML
    private VBox adminSection;

    @FXML
    private TextField clientNameField;

    @FXML
    private TextField clientNumberField;

    @FXML
    private TextField adminIdField;

    @FXML
    private PasswordField adminPasswordField;

    @FXML
    private Label statusLabel;

    @FXML
    private void toggleClientSection() {
        boolean isVisible = clientSection.isVisible();

        if (isVisible) {
            // Close client section and show both options
            clientSection.setVisible(false);
            clientSection.setManaged(false);
            adminOptionBtn.setVisible(true);
            adminOptionBtn.setManaged(true);
        } else {
            // Open client section and hide admin option
            clientSection.setVisible(true);
            clientSection.setManaged(true);
            adminSection.setVisible(false);
            adminSection.setManaged(false);
            adminOptionBtn.setVisible(false);
            adminOptionBtn.setManaged(false);
        }

        // Clear status
        statusLabel.setText("");
    }

    @FXML
    private void toggleAdminSection() {
        boolean isVisible = adminSection.isVisible();

        if (isVisible) {
            // Close admin section and show both options
            adminSection.setVisible(false);
            adminSection.setManaged(false);
            clientOptionBtn.setVisible(true);
            clientOptionBtn.setManaged(true);
        } else {
            // Open admin section and hide client option
            adminSection.setVisible(true);
            adminSection.setManaged(true);
            clientSection.setVisible(false);
            clientSection.setManaged(false);
            clientOptionBtn.setVisible(false);
            clientOptionBtn.setManaged(false);
        }

        // Clear status
        statusLabel.setText("");
    }

    @FXML
    private void handleClientLogin() {
        String name = clientNameField.getText().trim();
        String number = clientNumberField.getText().trim();

        if (name.isEmpty()) {
            statusLabel.setText("Please enter your name");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            return;
        }

        if (number.isEmpty()) {
            statusLabel.setText("Please enter your number");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            return;
        }

        // Check if client exists in database
        if (DatabaseManager.clientExists(name, number)) {
            // Navigate to client page
            navigateToClientPage(name, number);
        } else {
            // Add new client to database
            if (DatabaseManager.addClient(name, number)) {
                // Navigate to client page
                navigateToClientPage(name, number);
            } else {
                statusLabel.setText("Error adding client to database");
                statusLabel.setStyle("-fx-text-fill: #dc3545;");
            }
        }
    }

    private void navigateToClientPage(String name, String number) {
        try {
            FXMLLoader loader = App.setRootWithController("client");
            ClientController controller = loader.getController();
            controller.setClientInfo(name, number);
        } catch (IOException e) {
            statusLabel.setText("Error loading client page");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdminLogin() {
        String adminCode = adminIdField.getText().trim();
        String password = adminPasswordField.getText();

        if (adminCode.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both Admin Code and Password");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            return;
        }

        // Verify admin credentials from database
        if (DatabaseManager.verifyAdmin(adminCode, password)) {
            // Navigate to admin page
            navigateToAdminPage(adminCode);
        } else {
            statusLabel.setText("Invalid credentials");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
        }
    }

    private void navigateToAdminPage(String adminCode) {
        try {
            FXMLLoader loader = App.setRootWithController("admin");
            AdminController controller = loader.getController();
            controller.setAdminInfo(adminCode);
        } catch (IOException e) {
            statusLabel.setText("Error loading admin page");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            e.printStackTrace();
        }
    }
}
