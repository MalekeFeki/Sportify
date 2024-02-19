package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
        colNumTel.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));


    }

}
