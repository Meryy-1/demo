package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

public class MenuController {

    @FXML
    private FlowPane menuContainer;

    @FXML
    private Label statusLabel;

    // Map to store menu item prices
    private static final Map<String, Double> MENU_PRICES = new HashMap<>();

    static {
        MENU_PRICES.put("Spicy Potato", 12.00);
        MENU_PRICES.put("Pasta", 15.00);
        MENU_PRICES.put("Garlic Bread", 8.00);
        MENU_PRICES.put("Burger", 14.00);
        MENU_PRICES.put("Pizza", 18.00);
        MENU_PRICES.put("Taco", 10.00);
    }

    @FXML
    public void initialize() {
        statusLabel.setText("Browse our menu and add items to your order");
    }

    @FXML
    private void handleAddItem(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        Node parent = sourceButton.getParent();

        // Navigate up to get the menu card VBox
        while (parent != null && !(parent instanceof VBox)) {
            parent = parent.getParent();
        }

        if (parent instanceof VBox) {
            VBox vbox = (VBox) parent;
            // Get all labels in the card
            Label titleLabel = null;
            for (Node node : vbox.getChildren()) {
                if (node instanceof Label) {
                    Label label = (Label) node;
                    String style = label.getStyle();
                    // Look for the label with larger font (title)
                    if (style != null && style.contains("font-size: 18px")) {
                        titleLabel = label;
                        break;
                    }
                }
            }

            if (titleLabel != null) {
                String itemName = titleLabel.getText();
                Double price = MENU_PRICES.get(itemName);

                if (price != null) {
                    showQuantityDialog(itemName, price);
                }
            }
        }
    }

    private void showQuantityDialog(String itemName, double price) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Order " + itemName);
        dialog.setHeaderText("How many would you like to order?");
        dialog.setContentText("Quantity:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(quantityStr -> {
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    showAlert("Invalid Quantity", "Please enter a positive number.", AlertType.ERROR);
                    return;
                }

                // Get current client info
                String clientName = App.getCurrentClientName();
                String clientNumber = App.getCurrentClientNumber();

                if (clientName == null || clientNumber == null) {
                    showAlert("Error", "Client information not found. Please log in again.", AlertType.ERROR);
                    return;
                }

                // Add order to database
                boolean success = DatabaseManager.addOrder(
                        clientName,
                        clientNumber,
                        itemName,
                        quantity,
                        price,
                        Order.getCurrentTimestamp());

                if (success) {
                    double totalPrice = quantity * price;
                    statusLabel.setText(String.format("Order placed: %d x %s ($%.2f)", quantity, itemName, totalPrice));
                    statusLabel.setStyle("-fx-text-fill: #27ae60;");
                    showAlert("Order Confirmed",
                            String.format("Successfully ordered %d x %s\nTotal: $%.2f", quantity, itemName, totalPrice),
                            AlertType.INFORMATION);
                } else {
                    statusLabel.setText("Failed to place order. Please try again.");
                    statusLabel.setStyle("-fx-text-fill: #dc3545;");
                    showAlert("Order Failed", "Could not place your order. Please try again.", AlertType.ERROR);
                }

            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number.", AlertType.ERROR);
            }
        });
    }

    private void showAlert(String title, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = App.setRootWithController("client");
            ClientController controller = loader.getController();
            controller.setClientInfo(App.getCurrentClientName(), App.getCurrentClientNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
