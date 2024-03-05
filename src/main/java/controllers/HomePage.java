package controllers;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    List<String> fxml1 = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {


        // Add elements to the list
        fxml1.add("AjouterEvent.fxml");
        fxml1.add("AllEvent.fxml");
        fxml1.add("EventINFO.fxml");
        fxml1.add("FormReserverEvent.fxml");
        fxml1.add("GestionEvent.fxml");
        fxml1.add("ModifierEvent.fxml");

        try {
            Parent root= FXMLLoader.load(getClass().getResource("/HomePage.fxml"));

            primaryStage.setTitle("GestionEvent");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
@FXML
private ImageView event;
    @FXML
    private void redirectToAllEvent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) event.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    }

