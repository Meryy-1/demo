package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;

public class ReservationController {

    @FXML
    private Label clientNameLabel;

    @FXML
    private TextField partySizeField;

    @FXML
    private ListView<String> tablesListView;

    @FXML
    private VBox tablesContainer;

    @FXML
    private Button bookButton;

    @FXML
    private VBox currentReservationBox;

    @FXML
    private Label currentReservationLabel;

    @FXML
    private Label statusLabel;

    private String clientName;
    private String clientNumber;
    private ObservableList<DatabaseManager.Table> availableTables;

    @FXML
    public void initialize() {
        // Initialize from App's stored client info
        this.clientName = App.getCurrentClientName();
        this.clientNumber = App.getCurrentClientNumber();

        if (clientName != null) {
            clientNameLabel.setText(clientName);
            checkExistingReservation();
        }
    }

    // Set client information when navigating to this page
    public void setClientInfo(String name, String number) {
        this.clientName = name;
        this.clientNumber = number;
        clientNameLabel.setText(name);
        checkExistingReservation();
    }

    // Check if client already has a reservation
    private void checkExistingReservation() {
        DatabaseManager.Reservation reservation = DatabaseManager.getClientReservation(clientName, clientNumber);

        if (reservation != null) {
            // Client already has a reservation
            DatabaseManager.Table table = DatabaseManager.getTableById(reservation.getTableId());
            if (table != null) {
                currentReservationLabel.setText(String.format(
                        "Table #%d (Capacity: %d)\n",
                        table.getId(), table.getMaxCapacity(), reservation.getPartySize()));
                currentReservationBox.setVisible(true);
                currentReservationBox.setManaged(true);

                // Hide booking UI
                partySizeField.setDisable(true);
                tablesContainer.setVisible(false);
                tablesContainer.setManaged(false);
                bookButton.setVisible(false);
                bookButton.setManaged(false);
            }
        }
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = App.setRootWithController("client");
            ClientController controller = loader.getController();
            controller.setClientInfo(clientName, clientNumber);
        } catch (IOException e) {
            showError("Error navigating back");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFindTables() {
        String partySizeText = partySizeField.getText().trim();

        if (partySizeText.isEmpty()) {
            showError("Please enter party size");
            return;
        }

        try {
            int partySize = Integer.parseInt(partySizeText);

            if (partySize <= 0) {
                showError("Party size must be greater than 0");
                return;
            }

            if (partySize > 10) {
                showError("Party size cannot exceed 10 guests");
                return;
            }

            // Get available tables that can accommodate the party size
            availableTables = FXCollections.observableArrayList(
                    DatabaseManager.getAvailableTables());

            // Filter tables by capacity
            ObservableList<String> tableDisplayList = FXCollections.observableArrayList();
            for (DatabaseManager.Table table : availableTables) {
                if (table.getMaxCapacity() >= partySize) {
                    tableDisplayList.add(String.format("Table #%d (Capacity: %d)",
                            table.getId(), table.getMaxCapacity()));
                }
            }

            if (tableDisplayList.isEmpty()) {
                showError("No tables available for party size of " + partySize);
                tablesContainer.setVisible(false);
                tablesContainer.setManaged(false);
                bookButton.setVisible(false);
                bookButton.setManaged(false);
                return;
            }

            tablesListView.setItems(tableDisplayList);
            tablesContainer.setVisible(true);
            tablesContainer.setManaged(true);
            bookButton.setVisible(true);
            bookButton.setManaged(true);

            showSuccess("Found " + tableDisplayList.size() + " available table(s)");

        } catch (NumberFormatException e) {
            showError("Please enter a valid number for party size");
        }
    }

    @FXML
    private void handleBookTable() {
        int selectedIndex = tablesListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
            showError("Please select a table");
            return;
        }

        String partySizeText = partySizeField.getText().trim();
        int partySize = Integer.parseInt(partySizeText);

        // Get the selected table from the filtered list
        int tableIndex = 0;
        DatabaseManager.Table selectedTable = null;
        for (DatabaseManager.Table table : availableTables) {
            if (table.getMaxCapacity() >= partySize) {
                if (tableIndex == selectedIndex) {
                    selectedTable = table;
                    break;
                }
                tableIndex++;
            }
        }

        if (selectedTable == null) {
            showError("Error selecting table");
            return;
        }

        // Book the table
        boolean success = DatabaseManager.addReservation(
                clientName,
                clientNumber,
                selectedTable.getId(),
                partySize);

        if (success) {
            showSuccess("Table #" + selectedTable.getId() + " booked successfully!");

            // Update UI to show current reservation
            currentReservationLabel.setText(String.format(
                    "Table #%d (Capacity: %d)\nParty Size: %d",
                    selectedTable.getId(), selectedTable.getMaxCapacity(), partySize));
            currentReservationBox.setVisible(true);
            currentReservationBox.setManaged(true);

            // Hide booking UI
            partySizeField.setDisable(true);
            tablesContainer.setVisible(false);
            tablesContainer.setManaged(false);
            bookButton.setVisible(false);
            bookButton.setManaged(false);
        } else {
            showError("Failed to book table. It may have been taken by another client.");
        }
    }

    @FXML
    private void handleCancelReservation() {
        boolean success = DatabaseManager.cancelClientReservation(clientName, clientNumber);

        if (success) {
            showSuccess("Reservation cancelled successfully!");

            // Reset UI
            currentReservationBox.setVisible(false);
            currentReservationBox.setManaged(false);
            partySizeField.setDisable(false);
            partySizeField.clear();
            tablesContainer.setVisible(false);
            tablesContainer.setManaged(false);
            bookButton.setVisible(false);
            bookButton.setManaged(false);
        } else {
            showError("Failed to cancel reservation");
        }
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #dc3545;");
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #28a745;");
    }
}
