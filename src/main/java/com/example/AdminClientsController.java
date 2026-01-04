package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminClientsController {

    @FXML
    private TableView<Client> clientsTable;

    @FXML
    private TableColumn<Client, Integer> idColumn;

    @FXML
    private TableColumn<Client, String> nameColumn;

    @FXML
    private TableColumn<Client, String> numberColumn;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // Set up table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        loadClients();
    }

    private void loadClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList();

        String query = "SELECT id, name, number FROM client ORDER BY id";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:restaurant.db");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String number = rs.getString("number");
                clients.add(new Client(id, name, number));
            }

            clientsTable.setItems(clients);
            statusLabel.setText("Loaded " + clients.size() + " clients");

        } catch (SQLException e) {
            statusLabel.setText("Error loading clients: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
        try {
            App.setRoot("admin");
        } catch (Exception e) {
            statusLabel.setText("Error going back");
            e.printStackTrace();
        }
    }

    // Inner class for Client
    public static class Client {
        private int id;
        private String name;
        private String number;

        public Client(int id, String name, String number) {
            this.id = id;
            this.name = name;
            this.number = number;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getNumber() { return number; }
    }
}