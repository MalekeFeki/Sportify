package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        fxml1.add("/AjouterEvent.fxml");
        fxml1.add("/AllEvent.fxml");
        fxml1.add("/EventINFO.fxml");
        fxml1.add("/FormReserverEvent.fxml");
        fxml1.add("/GestionEvent.fxml");
        fxml1.add("/ModifierEvent.fxml");
        for (String a :
                fxml1) {
            try {
                Parent root= FXMLLoader.load(getClass().getResource("a"));

                primaryStage.setTitle("GestionEvent");
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }


    }
    }

