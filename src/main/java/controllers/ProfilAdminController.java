package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.Utilisateur;
import entities.enums.Role;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.UtilisateurCrud;
import tools.MyConnection;


public class ProfilAdminController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_annul;

    @FXML
    private Button btn_deconn;

    @FXML
    private Button btn_deco1;

    @FXML
    private Button btn_modif;

    @FXML
    private Button btn_membres;

    @FXML
    private Button btn_enregismodif;

    @FXML
    private RadioButton rbmembre;

    @FXML
    private RadioButton rbproprietaire;

    @FXML
    private TextField tfcin;

    @FXML
    private TextField tfemail;

    @FXML
    private PasswordField tfmdp;

    @FXML
    private TextField tfnom;

    @FXML
    private TextField tfnum_tel;

    @FXML
    private TextField tfprenom;
    private Utilisateur admin;
    private String email;
    private String password;
    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();
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
    @FXML
    private Label nomUtilisateur;


    public void setAdmin(Utilisateur admin) throws SQLException {
        this.admin = admin;
        if (this.admin != null) {
            nomUtilisateur.setText(this.admin.getNom());
        }
        afficherDetailsProfil(admin);  // Populate the fields when the admin is set
    }
    public void initData(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public void afficherDetailsProfil(Utilisateur admin) throws SQLException {
        Connection cnx = MyConnection.getInstance().getCnx();
        String req = "SELECT * from utilisateur WHERE id =?";

        PreparedStatement pst = cnx.prepareStatement(req);
        pst.setInt(1,MyConnection.instance.getId());
        ResultSet rs = pst.executeQuery();
        if(rs.next()){
            tfcin.setText(String.valueOf(admin.getCin()));
            tfnum_tel.setText(String.valueOf(admin.getNum_tel()));
            tfnom.setText(admin.getNom());
            tfprenom.setText(admin.getPrenom());
            tfemail.setText(admin.getEmail());
            tfmdp.setText(admin.getMdp());
            nomUtilisateur.setText(tfnom.getText());
            rbproprietaire.setSelected(false);
            rbmembre.setSelected(false);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void initialize() {

        // Effectuer la liaison bidirectionnelle entre le texte de tfnom et le texte de nomUtilisateur
        nomUtilisateur.textProperty().bindBidirectional(tfnom.textProperty());

        Utilisateur utilisateur = utilisateurCrud.getUtilisateurById(MyConnection.instance.getId());
        if (utilisateur != null) {
            tfcin.setText(String.valueOf(utilisateur.getCin()));
            tfnum_tel.setText(String.valueOf(utilisateur.getNum_tel()));
            tfnom.setText(utilisateur.getNom());
            tfprenom.setText(utilisateur.getPrenom());
            tfemail.setText(utilisateur.getEmail());
            tfmdp.setText(utilisateur.getMdp());
            rbproprietaire.setSelected(false);
            rbmembre.setSelected(true);
        } else {
            // Handle the case where user details are not found
            System.out.println("User details not found.");
        }
        btn_deconn.setOnAction(event -> {
            // Display a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment vous déconnecter ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // If the user clicks "OK", close the current window and open the authentication page
                Stage stage = (Stage) btn_deconn.getScene().getWindow();
                stage.close();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/authentification.fxml"));
                    Parent root = loader.load();
                    Stage authStage = new Stage();
                    authStage.setScene(new Scene(root));
                    authStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // maysir chay
            }
        });
        // Gérer l'événement du bouton "Modifier profil"
       /* btn_enregismodif.setOnAction(event -> {
            // Récupérer les nouvelles valeurs des champs de texte
            int cin = Integer.parseInt(tfcin.getText());
            int num_tel = Integer.parseInt(tfnum_tel.getText());
            String nom = tfnom.getText();
            String prenom = tfprenom.getText();
            String email = tfemail.getText();
            String mdp = tfmdp.getText();

            // Créer un objet Utilisateur avec les nouvelles valeurs

            utilisateur.setCin(cin);
            utilisateur.setNum_tel(num_tel);
            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setEmail(email);
            utilisateur.setMdp(mdp);
// Afficher une alerte de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Voulez-vous vraiment modifier cet utilisateur ?");

            // Attendre la réponse de l'utilisateur
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Si l'utilisateur a confirmé, mettre à jour l'utilisateur
                utilisateurCrud.modifier2(utilisateur);
                // Afficher un message de succès
                showAlert("Utilisateur modifié avec succès.");
                // Vous pouvez également ajouter du code ici pour rafraîchir les champs de texte si nécessaire
            }

        });*/
        btn_enregismodif.setOnAction(event -> modifierProfil());

        btn_membres.setOnAction(event -> {
            try {
                // Charger le fichier FXML de la page ListeUtilisateurs
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeUtilisateurs.fxml"));
                Parent root = loader.load();

                // Créer une nouvelle scène avec le contenu chargé du fichier FXML
                Scene scene = new Scene(root);

                // Créer une nouvelle fenêtre (stage) et définir sa scène
                Stage stage = new Stage();
                stage.setScene(scene);

                // Afficher la fenêtre
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        btn_deco1.setOnAction(event -> {
            afficherProprietairesSalles();
        });
    }

    @FXML
    void afficherProprietairesSalles() {
        ObservableList<Utilisateur> utilisateurs = utilisateurCrud.getUtilisateursByRole(Role.PROPRIETAIRE);
// Mettre à jour les éléments affichés dans la TableView
        tableView.setItems(utilisateurs);
    }
    @FXML
    private void modifierProfil() {
        // Récupérer les valeurs des champs de texte
        int cin = Integer.parseInt(tfcin.getText());
        int num_tel = Integer.parseInt(tfnum_tel.getText());
        String nom = tfnom.getText();
        String prenom = tfprenom.getText();
        String email = tfemail.getText();
        String mdp = tfmdp.getText();

        // Créer un nouvel objet Utilisateur avec les valeurs récupérées
        Utilisateur utilisateur = new Utilisateur(cin, num_tel, nom, prenom, email, mdp);

        // Appeler la méthode de mise à jour appropriée du CRUD Utilisateur
        utilisateurCrud.modifierEntite(utilisateur);
    }

}