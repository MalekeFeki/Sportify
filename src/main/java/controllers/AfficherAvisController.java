package controllers;

import entities.Avis;
import entities.enums.TypeAvis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import services.AvisCrud;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherAvisController implements Initializable {
    private AvisCrud avisCrud;
     private TableColumn<Avis,Integer> idA ;
    private TableColumn<Avis,TypeAvis> type ;
    private TableColumn<Avis,String> description ;
    @FXML
    private TableView<Avis> avisTableView ;

    @FXML
    private ObservableList<Avis> observableAVIS = FXCollections.observableArrayList();
    private void loadAvis() {
        observableAVIS.clear();
        List<Avis> events = avisCrud.afficherAvis();
        observableAVIS.addAll(events);
        System.out.println(observableAVIS);
        avisTableView.setItems(observableAVIS);
    }


    public AfficherAvisController()  {
        this.avisCrud = new AvisCrud();
    }

    public void ajouterAvis(TypeAvis type, String description) {
        Avis avis = new Avis(0, type, description);
        avisCrud.ajouterAvis(avis);
    }

    public void afficherAvis() {
        List<Avis> avisList = avisCrud.afficherAvis();
        if (avisList.isEmpty()) {
            System.out.println("Aucun avis disponible");
        } else {
            for (Avis avis : avisList) {
                System.out.println(avis.toString());
            }
        }
    }

    public void modifierAvis(int id, TypeAvis type, String description) {
        Avis avis = new Avis(id, type, description);
        avisCrud.modifierAvis(avis);
    }

    public void supprimerAvis(int id) {
        avisCrud.supprimerAvis(id);
    }

    public void filterBadWordsAndPrint(String description) {
        String filteredDescription = avisCrud.filterBadWords(description);
        System.out.println(filteredDescription);
    }

    public void calculerEtAfficherAvisMoyen(List<Avis> listeAvis) {
        Avis avisMoyen = avisCrud.calculerAvisMoyen(listeAvis);
        System.out.println(avisMoyen.toString());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Avis, Integer> idA = new TableColumn<>("idA");
        TableColumn<Avis, String> type = new TableColumn<>("typeAvis");
        TableColumn<Avis, String> description = new TableColumn<>("description");
        TableColumn<Avis, Void> optionsColumn = new TableColumn<>("options");
        idA.setCellValueFactory(new PropertyValueFactory<>("idA"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));



        optionsColumn.setCellFactory(param -> new TableCell<Avis, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Button modifyButton = new Button("Modify");

            {
                deleteButton.setOnAction(event -> {
                    Avis avisToDelete = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Delete Avis");
                    alert.setContentText("Voulez-vous vraiment supprimer l'avis " + avisToDelete.getIdA() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        avisCrud.supprimerAvis(avisToDelete.getIdA());
                        loadAvis();
                        showAlert("Avis Deleted", "Avis has been deleted successfully.");
                    }
                });

                modifyButton.setOnAction(event -> {
                    Avis avisToModify = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Modify Avis");
                    alert.setContentText("Voulez-vous vraiment modifier l'avis " + avisToModify.getIdA() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        redirectToModifierAvis(avisToModify);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(deleteButton, modifyButton);
                    buttons.setSpacing(5);
                    setGraphic(buttons);
                }
            }
        });

        avisTableView.getColumns().addAll(idA,type,description);
        avisTableView.getColumns().add(optionsColumn);
        loadAvis();

    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void redirectToModifierAvis(Avis AvisToModify) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAvis.fxml"));
            Parent root = loader.load();
            ModifierAvisController modifierAvisController = loader.getController();
            modifierAvisController.setAvisToModify(AvisToModify);
            Stage stage = (Stage) avisTableView.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
