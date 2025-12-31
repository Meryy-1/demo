package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import java.io.IOException;

public class MenuController {

    @FXML
    private FlowPane menuContainer;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        statusLabel.setText("Browse our menu and add items to your order");
    }

    @FXML
    private void handleAddItem(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        Node parent = sourceButton.getParent();

        // Navigate up to get the menu card VBox
        while (parent != null && !parent.getStyleClass().contains("menu-card")) {
            parent = parent.getParent();
        }

        if (parent != null) {
            // Get the item title from the first label in the card
            Label titleLabel = (Label) parent.lookup(".menu-item-title");
            if (titleLabel != null) {
                String itemName = titleLabel.getText();
                statusLabel.setText("Added to order: " + itemName);
                statusLabel.setStyle("-fx-text-fill: #27ae60;");
            }
        }
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
