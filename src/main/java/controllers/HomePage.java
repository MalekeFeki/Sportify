package controllers;

import entities.Utilisateur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.UtilisateurCrud;

import java.io.IOException;
import java.util.List;

public class HomePage extends Application {
    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root= FXMLLoader.load(getClass().getResource("/inscription.fxml"));
            primaryStage.setTitle("Inscription");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    }

