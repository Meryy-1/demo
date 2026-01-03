package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Optional;

public class AdminMenuController {

    @FXML
    private TableView<MenuItem> menuTable;

    @FXML
    private TableColumn<MenuItem, String> itemNameColumn;

    @FXML
    private TableColumn<MenuItem, Double> priceColumn;

    @FXML
    private TableColumn<MenuItem, Boolean> availableColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button toggleButton;

    @FXML
    private Label statusLabel;

    @FXML
    public void initialize() {
        // Set up table columns
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        loadMenu();

        // Enable/disable buttons based on selection
        menuTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean hasSelection = newSelection != null;
            editButton.setDisable(!hasSelection);
            deleteButton.setDisable(!hasSelection);
            toggleButton.setDisable(!hasSelection);
        });
    }

    private void loadMenu() {
        ObservableList<MenuItem> menuItems = FXCollections.observableArrayList(DatabaseManager.getAllMenuItems());
        menuTable.setItems(menuItems);
        statusLabel.setText("Loaded " + menuItems.size() + " menu items");
    }

    @FXML
    void handleAdd() {
        Dialog<MenuItem> dialog = createMenuItemDialog("Add Menu Item", null);
        Optional<MenuItem> result = dialog.showAndWait();

        result.ifPresent(item -> {
            if (DatabaseManager.addMenuItem(item.getName(), item.getPrice(), item.isAvailable())) {
                loadMenu();
                statusLabel.setText("Menu item added successfully");
            } else {
                statusLabel.setText("Error adding menu item");
            }
        });
    }

    @FXML
    void handleEdit() {
        MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        Dialog<MenuItem> dialog = createMenuItemDialog("Edit Menu Item", selectedItem);
        Optional<MenuItem> result = dialog.showAndWait();

        result.ifPresent(item -> {
            if (DatabaseManager.updateMenuItem(selectedItem.getId(), item.getName(), item.getPrice(), item.isAvailable())) {
                loadMenu();
                statusLabel.setText("Menu item updated successfully");
            } else {
                statusLabel.setText("Error updating menu item");
            }
        });
    }

    @FXML
    void handleDelete() {
        MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Menu Item");
        alert.setHeaderText("Delete " + selectedItem.getName() + "?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (DatabaseManager.deleteMenuItem(selectedItem.getId())) {
                loadMenu();
                statusLabel.setText("Menu item deleted successfully");
            } else {
                statusLabel.setText("Error deleting menu item");
            }
        }
    }

    @FXML
    void handleToggle() {
        MenuItem selectedItem = menuTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        String action = selectedItem.isAvailable() ? "make unavailable" : "make available";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Toggle Availability");
        alert.setHeaderText("Make " + selectedItem.getName() + " " + action + "?");
        alert.setContentText("This will affect whether customers can order this item.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (DatabaseManager.toggleMenuItemAvailability(selectedItem.getId())) {
                loadMenu();
                statusLabel.setText("Menu item availability toggled successfully");
            } else {
                statusLabel.setText("Error toggling menu item availability");
            }
        }
    }

    private Dialog<MenuItem> createMenuItemDialog(String title, MenuItem existingItem) {
        Dialog<MenuItem> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(title);

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the form fields
        TextField nameField = new TextField();
        nameField.setPromptText("Item name");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        CheckBox availableCheckBox = new CheckBox("Available");

        if (existingItem != null) {
            nameField.setText(existingItem.getName());
            priceField.setText(String.valueOf(existingItem.getPrice()));
            availableCheckBox.setSelected(existingItem.isAvailable());
        }

        // Layout the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Price:"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(availableCheckBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a MenuItem when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = nameField.getText().trim();
                    double price = Double.parseDouble(priceField.getText());
                    boolean available = availableCheckBox.isSelected();

                    if (name.isEmpty() || price < 0) {
                        return null;
                    }

                    return new MenuItem(name, price, available);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        return dialog;
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
}