package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static String currentClientName;
    private static String currentClientNumber;

    public static void setCurrentClient(String name, String number) {
        currentClientName = name;
        currentClientNumber = number;
    }

    public static String getCurrentClientName() {
        return currentClientName;
    }

    public static String getCurrentClientNumber() {
        return currentClientNumber;
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize database
        DatabaseManager.initialize();
        
        scene = new Scene(loadFXML("main"), 700, 600);
        stage.setScene(scene);
        stage.setTitle("Restaurant Management System");
        stage.show();
        
        // Close database connection when application closes
        stage.setOnCloseRequest(event -> DatabaseManager.close());
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    static FXMLLoader setRootWithController(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        scene.setRoot(fxmlLoader.load());
        return fxmlLoader;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}