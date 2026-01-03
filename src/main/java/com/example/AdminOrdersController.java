package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controller for the Manage Orders (Admin) page.
 * Shows order summaries on the left and order details on the right.
 */
public class AdminOrdersController {

    // SplitPane layout
    @FXML
    private SplitPane splitPane;

    // Left side - Order summaries
    @FXML
    private TableView<OrderSummary> ordersTable;
    @FXML
    private TableColumn<OrderSummary, Integer> orderIdColumn;
    @FXML
    private TableColumn<OrderSummary, Integer> tableNumberColumn;
    @FXML
    private TableColumn<OrderSummary, String> dateColumn;
    @FXML
    private TableColumn<OrderSummary, Double> totalColumn;
    @FXML
    private TableColumn<OrderSummary, String> statusColumn;

    // Right side - Order details
    @FXML
    private Label orderDetailsLabel;
    @FXML
    private TableView<Order> orderItemsTable;
    @FXML
    private TableColumn<Order, String> itemNameColumn;
    @FXML
    private TableColumn<Order, Integer> quantityColumn;
    @FXML
    private TableColumn<Order, Double> priceColumn;
    @FXML
    private TableColumn<Order, Double> subtotalColumn;
    @FXML
    private Label totalAmountLabel;

    // Buttons
    @FXML
    private Button markCompletedButton;
    @FXML
    private Button cancelOrderButton;

    // Status label
    @FXML
    private Label statusLabel;

    private OrderSummary selectedOrder;

    @FXML
    public void initialize() {
        setupOrdersTable();
        setupOrderItemsTable();
        loadOrders();
        statusLabel.setText("Orders loaded from database");
    }

    /**
     * Configure the orders table columns for OrderSummary model
     */
    private void setupOrdersTable() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tableNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Handle order selection
        ordersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedOrder = newSelection;
            if (newSelection != null) {
                showOrderDetails(newSelection);
                updateButtons();
            }
        });
    }

    /**
     * Configure the order items table columns
     */
    private void setupOrderItemsTable() {
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        subtotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    /**
     * Load orders from the database
     */
    private void loadOrders() {
        try {
            ObservableList<OrderSummary> orders = FXCollections.observableArrayList(
                DatabaseManager.getOrderSummaries()
            );
            ordersTable.setItems(orders);
            statusLabel.setText("Loaded " + orders.size() + " orders from database");
        } catch (Exception e) {
            statusLabel.setText("Error loading orders: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Show details of the selected order
     */
    private void showOrderDetails(OrderSummary order) {
        orderDetailsLabel.setText("Order Details - Order #" + order.getOrderId());
        ObservableList<Order> orderItems = FXCollections.observableArrayList(order.getOrderItems());
        orderItemsTable.setItems(orderItems);
        totalAmountLabel.setText(String.format("Total Amount: $%.2f", order.getTotal()));
    }

    /**
     * Update button states based on selected order status
     */
    private void updateButtons() {
        if (selectedOrder != null) {
            String status = selectedOrder.getStatus();
            markCompletedButton.setDisable(OrderSummary.STATUS_COMPLETED.equals(status) ||
                                         OrderSummary.STATUS_CANCELLED.equals(status));
            cancelOrderButton.setDisable(OrderSummary.STATUS_COMPLETED.equals(status) ||
                                       OrderSummary.STATUS_CANCELLED.equals(status));
        } else {
            markCompletedButton.setDisable(true);
            cancelOrderButton.setDisable(true);
        }
    }

    /**
     * Mark selected order as completed
     */
    @FXML
    private void handleMarkCompleted() {
        if (selectedOrder != null) {
            boolean success = DatabaseManager.updateOrderStatus(selectedOrder.getOrderId(),
                                                              OrderSummary.STATUS_COMPLETED);
            if (success) {
                selectedOrder.setStatus(OrderSummary.STATUS_COMPLETED);
                ordersTable.refresh();
                updateButtons();
                statusLabel.setText("Order #" + selectedOrder.getOrderId() + " marked as completed");
            } else {
                statusLabel.setText("Error updating order status");
            }
        }
    }

    /**
     * Cancel selected order
     */
    @FXML
    private void handleCancelOrder() {
        if (selectedOrder != null) {
            boolean success = DatabaseManager.updateOrderStatus(selectedOrder.getOrderId(),
                                                              OrderSummary.STATUS_CANCELLED);
            if (success) {
                selectedOrder.setStatus(OrderSummary.STATUS_CANCELLED);
                ordersTable.refresh();
                updateButtons();
                statusLabel.setText("Order #" + selectedOrder.getOrderId() + " cancelled");
            } else {
                statusLabel.setText("Error updating order status");
            }
        }
    }

    /**
     * Handle back to admin menu
     */
    @FXML
    private void handleBack() {
        try {
            App.setRoot("admin");
        } catch (Exception e) {
            statusLabel.setText("Error going back: " + e.getMessage());
            e.printStackTrace();
        }
    }
}