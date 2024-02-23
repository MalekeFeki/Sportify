package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.UtilisateurCrud;

public class ListeUtilisateursController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private TableView<Utilisateur> tableView;
    @FXML
    private TableColumn<Utilisateur, Integer> colCin;

    @FXML
    private TableColumn<Utilisateur, Integer> colNumTel;
    @FXML
    private TableColumn<Utilisateur, Integer> colId;

    @FXML
    private TableColumn<Utilisateur, String> colNom;

    @FXML
    private TableColumn<Utilisateur, String> colPrenom;

    @FXML
    private TableColumn<Utilisateur, String> colEmail;

    @FXML
    private TableColumn<Utilisateur, Role> colRole;
    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();
    public void setResId(String resId) {
        this.colId.setText(resId);
    }

    public void setResCin(String resCin) {
        this.colCin.setText(resCin);
    }
    public void setResNumTel(String resNumTel) {
        this.colNumTel.setText(resNumTel);
    }

    public void setResNom(String resNom) {
        this.colNom.setText(resNom);
    }

    public void setResPrenom(String resPrenom) {
        this.colPrenom.setText(resPrenom);

    }
    public void setResEmail(String resEmail) {
        this.colEmail.setText(resEmail);
    }

    public void setResRole(Role resRole) {
        this.colRole.setText(resRole.toString());
    }

    @FXML
    void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        colNumTel.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        List<Utilisateur> allUtilisateurs = utilisateurCrud.getAllUtilisateurs();
        tableView.getItems().addAll(allUtilisateurs);





    }

}
