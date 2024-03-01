package controllers;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import services.UtilisateurCrud;
import javafx.scene.layout.HBox;
import tools.MyConnection;


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
    private TableColumn<Utilisateur, String> colMdp;


    @FXML
    private TableColumn<Utilisateur, Role> colRole;
    private Connection connection;
    @FXML
    private Label totalMembres;

    @FXML
    private Label totalProprietaires;

    @FXML
    private Label totalUtilisateurs;

    @FXML
    private Label totalAdmins;
    @FXML
    private TextField tfrecherche;
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

        // Ajouter un écouteur de changement de texte pour le champ de recherche
        tfrecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            // Filtrer la TableView en fonction du texte de recherche
            filterTableView(newValue);
        });


        // Récupérer le nombre de membres
        int membres = utilisateurCrud.countUsersByRole("MEMBRE");
        totalMembres.setText(String.valueOf(membres));

        // Récupérer le nombre de propriétaires
        int proprietaires = utilisateurCrud.countUsersByRole("PROPRIETAIRE");
        totalProprietaires.setText(String.valueOf(proprietaires));
        // Récupérer le nombre d'administrateurs
        int administrateurs = utilisateurCrud.countUsersByRole("ADMIN");
        totalAdmins.setText(String.valueOf(administrateurs));


// Calculer le nombre total d'utilisateurs
        int utilisateurs = membres + proprietaires + administrateurs;
        totalUtilisateurs.setText(String.valueOf(utilisateurs));


// Initialiser la connexion
        MyConnection myConnection = MyConnection.getInstance();
        connection = myConnection.getCnx();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        colNumTel.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colMdp.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        colNom.setCellFactory(TextFieldTableCell.forTableColumn());
        colNom.setOnEditCommit(event -> {
            Utilisateur user = event.getRowValue();
            user.setNom(event.getNewValue());
            utilisateurCrud.modifierEntite(user);
            refreshTableViewData();
        });
        colPrenom.setCellFactory(TextFieldTableCell.forTableColumn());
        colPrenom.setOnEditCommit(event -> {
            Utilisateur user = event.getRowValue();
            user.setPrenom(event.getNewValue());
            utilisateurCrud.modifierEntite(user);
            refreshTableViewData();
        });
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(event -> {
            Utilisateur user = event.getRowValue();
            user.setEmail(event.getNewValue());
            utilisateurCrud.modifierEntite(user);
            refreshTableViewData();
        });
        colMdp.setCellFactory(TextFieldTableCell.forTableColumn());
        colMdp.setOnEditCommit(event -> {
            Utilisateur user = event.getRowValue();
            user.setMdp(event.getNewValue());
            utilisateurCrud.modifierEntite(user);
            refreshTableViewData();
        });
        colNumTel.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colNumTel.setOnEditCommit(event -> {
            Utilisateur user = event.getRowValue();
            try {
                int newNumTel = Integer.parseInt(String.valueOf(event.getNewValue()));
                user.setNum_tel(newNumTel);
                utilisateurCrud.modifierEntite(user);
                refreshTableViewData();
            } catch (NumberFormatException e) {
                showAlert("Veuillez saisir un numéro de téléphone valide (entier).");
            }
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
    private void filterTableView(String searchText) {
        // Créer un prédicat pour filtrer les éléments de la TableView
        Predicate<Utilisateur> predicate = utilisateur ->
                utilisateur.getNom().toLowerCase().contains(searchText.toLowerCase());

        // Créer une liste filtrée à partir de la liste originale des utilisateurs
        List<Utilisateur> filteredList = utilisateurCrud.getAllUtilisateurs().stream()
                .filter(predicate)
                .collect(Collectors.toList());

        // Effacer toutes les lignes existantes dans la TableView
        tableView.getItems().clear();

        // Ajouter les éléments filtrés à la TableView
        tableView.getItems().addAll(filteredList);
    }
    private void addActionButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<Utilisateur, Void>() {
            private final Button btnUpdate = new Button("Modifier");
            private final Button btnDelete = new Button("Supprimer");

            {
                btnUpdate.setOnAction(event -> {
                    Utilisateur user = getTableView().getItems().get(getIndex());
                    handleUpdateButtonAction();
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
    private void configureEditableCells() {
        colNom.setCellFactory(TextFieldTableCell.forTableColumn());
        colNom.setOnEditCommit(event -> {
            Utilisateur user = event.getRowValue();
            user.setNom(event.getNewValue());
            user.setPrenom(event.getNewValue());
            utilisateurCrud.modifierEntite(user);
            refreshTableViewData();
        });
    }
    @FXML
    private void handleUpdateButtonAction() {
        Utilisateur user = tableView.getSelectionModel().getSelectedItem();
        if (user == null) {
            showAlert("Veuillez sélectionner un utilisateur à modifier.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment modifier cet utilisateur ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            utilisateurCrud.modifierEntite(user);
            // Rafraîchissez les données dans la TableView après la mise à jour.
            refreshTableViewData();
        }
    }


    private void handleDeleteButtonAction(Utilisateur user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment supprimer cet utilisateur ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            utilisateurCrud.supprimerEntite(user.getId());
            // Mettre à jour les compteurs après la suppression
            updateCounters();
            // Mettre à jour l'affichage en supprimant l'utilisateur de la TableView
            tableView.getItems().remove(user);
            // Rafraîchir la TableView après la suppression

            refreshTableViewData();

        }

        }

    private void showAlert(String message) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Attention");
      alert.setHeaderText(null);
      alert.setContentText(message);
      alert.showAndWait();
  }
    private void refreshTableViewData() {
        // Effacez toutes les lignes existantes dans la TableView
        tableView.getItems().clear();
        // Chargez à nouveau toutes les données depuis votre base de données
        List<Utilisateur> allUtilisateurs = utilisateurCrud.getAllUtilisateurs();
        // Ajoutez les données rechargées dans la TableView
        tableView.getItems().addAll(allUtilisateurs);
    }
    private void updateCounters() {
        // Récupérer le nombre de membres après la suppression
        int membres = utilisateurCrud.countUsersByRole("MEMBRE");
        totalMembres.setText(String.valueOf(membres));

        // Récupérer le nombre de propriétaires après la suppression
        int proprietaires = utilisateurCrud.countUsersByRole("PROPRIETAIRE");
        totalProprietaires.setText(String.valueOf(proprietaires));

        // Récupérer le nombre d'administrateurs après la suppression
        int administrateurs = utilisateurCrud.countUsersByRole("ADMIN");
        totalAdmins.setText(String.valueOf(administrateurs));

        // Recalculer le nombre total d'utilisateurs après la suppression
        int utilisateurs = membres + proprietaires + administrateurs;
        totalUtilisateurs.setText(String.valueOf(utilisateurs));
    }
}
