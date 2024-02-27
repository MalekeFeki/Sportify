package controllers;

import entities.Avis;
import entities.enums.TypeAvis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.AvisCrud;

import java.net.URL;
import java.sql.SQLOutput;
import java.util.List;
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
        observableAVIS.clear(); // Clear existing items
        List<Avis> events = avisCrud.afficherAvis();
        observableAVIS.addAll(events);
        System.out.println(observableAVIS);

        // Set the items in the TableView
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
        TableColumn<Avis, String> type = new TableColumn<>("typeAvis");  // Corrected column name
        TableColumn<Avis, String> description = new TableColumn<>("description");
        idA.setCellValueFactory(new PropertyValueFactory<>("idA"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));  // Corrected field name
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        avisTableView.getColumns().addAll(idA,type,description);
        loadAvis();
    }
}
