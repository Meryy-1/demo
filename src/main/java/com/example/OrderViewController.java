package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.util.List;

public class OrderViewController {

    @FXML
    private TableView<Order> ordersTable;

    @FXML
    private TableColumn<Order, String> itemNameColumn;

    @FXML
    private TableColumn<Order, Integer> quantityColumn;

    @FXML
    private TableColumn<Order, Double> priceColumn;

    @FXML
    private TableColumn<Order, Double> totalPriceColumn;

    @FXML
    private TableColumn<Order, String> orderDateColumn;

    @FXML
    private Label clientNameLabel;

    @FXML
    private Label statusLabel;

    private String clientName;
    private String clientNumber;

    @FXML
    public void initialize() {
        // Set up table columns
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
    }

    public void setClientInfo(String name, String number) {
        this.clientName = name;
        this.clientNumber = number;
        clientNameLabel.setText("Orders for: " + name);
        loadOrders();
    }

    private void loadOrders() {
        if (clientName == null || clientNumber == null) {
            statusLabel.setText("Client information not available");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            return;
        }

        List<Order> orders = DatabaseManager.getClientOrders(clientName, clientNumber);

        if (orders.isEmpty()) {
            statusLabel.setText("No orders found. Start ordering from the menu!");
            statusLabel.setStyle("-fx-text-fill: #6c757d;");
        } else {
            statusLabel.setText("Total orders: " + orders.size());
            statusLabel.setStyle("-fx-text-fill: #27ae60;");
        }

        ObservableList<Order> orderData = FXCollections.observableArrayList(orders);
        ordersTable.setItems(orderData);
    }

    @FXML
    private void handleRefresh() {
        loadOrders();
        statusLabel.setText("Orders refreshed!");
        statusLabel.setStyle("-fx-text-fill: #27ae60;");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = App.setRootWithController("client");
            ClientController controller = loader.getController();
            controller.setClientInfo(clientName, clientNumber);
        } catch (IOException e) {
            statusLabel.setText("Error going back");
            statusLabel.setStyle("-fx-text-fill: #dc3545;");
            e.printStackTrace();
        }
    }
}
