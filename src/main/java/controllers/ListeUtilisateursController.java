package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import services.UtilisateurCrud;
import javafx.scene.layout.HBox;


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

    @FXML
    private TableColumn<Utilisateur, Void> colAction;
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

        colNom.setCellFactory(TextFieldTableCell.forTableColumn());
        colNom.setOnEditCommit(event -> {
            Utilisateur user = event.getRowValue();
            user.setNom(event.getNewValue());
            utilisateurCrud.modifierEntite(user);
            refreshTableViewData();
        });

        addActionButtonToTable();
        List<Utilisateur> allUtilisateurs = utilisateurCrud.getAllUtilisateurs();
        tableView.getItems().addAll(allUtilisateurs);


        // Activer l'édition lors du double-clic
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tableView.edit(tableView.getSelectionModel().getSelectedIndex(), colNom);
                // Remplacez "colNom" par la colonne sur laquelle vous souhaitez activer l'édition par défaut
            }
        });

    }
    private void addActionButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<Utilisateur, Void>() {
            private final Button btnUpdate = new Button("Update");
            private final Button btnDelete = new Button("Delete");

            {
                btnUpdate.setOnAction(event -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    handleUpdateButtonAction(user);
                });

                btnDelete.setOnAction(event -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    handleDeleteButtonAction(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(btnUpdate, btnDelete);
                    setGraphic(hbox);
                }
            }
        });
    }
    private void handleUpdateButtonAction(Utilisateur user) {
        utilisateurCrud.modifierEntite(user);
        // Rafraîchissez les données dans la TableView après la mise à jour.
        refreshTableViewData();
    }

    private void handleDeleteButtonAction(Utilisateur user) {
        utilisateurCrud.supprimerEntite(user.getId());
        // Mettre à jour l'affichage en supprimant l'utilisateur de la TableView
        tableView.getItems().remove(user);
    }
    private void refreshTableViewData() {
        // Effacez toutes les lignes existantes dans la TableView
        tableView.getItems().clear();
        // Chargez à nouveau toutes les données depuis votre base de données
        List<Utilisateur> allUtilisateurs = utilisateurCrud.getAllUtilisateurs();
        // Ajoutez les données rechargées dans la TableView
        tableView.getItems().addAll(allUtilisateurs);
    }
}
