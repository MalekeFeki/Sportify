package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.lookups.v1.PhoneNumber;
import entities.Mailing;
import entities.Utilisateur;
import entities.enums.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.UtilisateurCrud;


public class InscriptionController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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
    @FXML
    private Button btn_annul;

    @FXML
    private Button btn_inscri;
    @FXML
    private Hyperlink hyperlink;
    private TableView<Utilisateur> tableView;
    private List<Utilisateur> registeredUsers;
    private Stage primaryStage;
    // Twilio account credentials
    private static final String ACCOUNT_SID = "ACd6178ca26eb4a78ebcf30b381f0ebeab";
    private static final String AUTH_TOKEN = "9c80cfd68e4cd0692781ba469c2e819b";
    public void setRegisteredUsers(List<Utilisateur> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }
    public void setTableView(TableView<Utilisateur> tableView) {
        this.tableView = tableView;
    }
    public void setHomePage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void savePerson(ActionEvent event) {
        if (tfcin.getText().isEmpty() || tfnum_tel.getText().isEmpty() || tfnom.getText().isEmpty() ||
                tfprenom.getText().isEmpty() || tfemail.getText().isEmpty() || tfmdp.getText().isEmpty()) {
            // Afficher un message d'erreur si les champs sont vides
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs.", ButtonType.OK);
            alert.show();
            return;
        }
        if (!validerCIN(tfcin.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Le CIN doit avoir 8 chiffres et commencer par 0 ou 1.", ButtonType.OK);
            alert.show();
            return;
        }
        if (!validerFormatEmail(tfemail.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "L'adresse email n'est pas dans un format valide.", ButtonType.OK);
            alert.show();
            return;
        }
        String password = tfmdp.getText();
        if (password.length() < 8 || password.equals(password.toLowerCase())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Le mot de passe doit contenir au moins 8 caractères et au moins une majuscule.", ButtonType.OK);
            alert.show();
            return;
        }
        if (!validerNumeroTelephone(tfnum_tel.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Le numéro de téléphone doit être composé de 8 chiffres.", ButtonType.OK);
            alert.show();
            return;
        }
        // Sauvegarde de personne dans la BD


        Utilisateur p = new Utilisateur(Integer.parseInt(tfcin.getText()), Integer.parseInt(tfnum_tel.getText()), tfnom.getText(), tfprenom.getText(), tfemail.getText(), tfmdp.getText(), rbmembre.isSelected() ? Role.MEMBRE : Role.PROPRIETAIRE);

        // Ajouter des vérifications ici avant d'ajouter l'utilisateur
        UtilisateurCrud uc = new UtilisateurCrud();
        if (!verifierChampsUtilisateur(p)) {
            // Afficher un message d'erreur si les champs ne sont pas valides
            Alert alert = new Alert(Alert.AlertType.ERROR, "Veuillez remplir tous les champs correctement.", ButtonType.OK);
            alert.show();
            return;
        }

        if (uc.utilisateurExisteDeja(tfcin.getText(), tfemail.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cet utilisateur existe déjà.", ButtonType.OK);
            alert.show();
            return;
        }

        // Ajouter l'utilisateur uniquement si les vérifications sont réussies
        uc.ajouterEntite2(p);


// Send welcome SMS
        //sendWelcomeSMS(p);
        Role selectedRole = p.getRole();
        List<Utilisateur> allUsers = uc.getAllUtilisateurs();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Utilisateur ajouté", ButtonType.OK);
        alert.showAndWait();
        // Envoyer un e-mail de bienvenue à l'utilisateur ajouté
        String emailSubject = "Bienvenue sur notre plateforme";
        String emailBody = "Bonjour " + p.getPrenom() + ",\n\nBienvenue sur notre plateforme Sportify. Merci pour votre inscription.\n\nCordialement,\nL'équipe de notre plateforme.";
        Mailing.sendEmail( p.getEmail(), emailSubject, emailBody);
        // Redirection vers la page d'authentification
        redirectToAuthPage();

    }

    private void sendWelcomeSMS(Utilisateur p) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String receiverPhoneNumber = tfnum_tel.getText();
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(receiverPhoneNumber), // Receiver's phone number
                new com.twilio.type.PhoneNumber("+15415267049"), // Twilio phone number
                "Bienvenue dans notre plateforme Sportify!"
        ).create();

        System.out.println(message.getSid());
    }


    private boolean validerCIN(String cin) {
        String regex = "^[01]\\d{7}$";
        return cin.matches(regex);
    }
    private boolean verifierChampsUtilisateur(Utilisateur utilisateur) {
        // Ajoutez vos vérifications de champs ici
        // Par exemple, vérifiez si les champs ne sont pas vides
        return !utilisateur.getNom().isEmpty() &&
                !utilisateur.getPrenom().isEmpty() &&
                !utilisateur.getEmail().isEmpty() &&
                !utilisateur.getMdp().isEmpty();
    }
    private boolean validerFormatEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return email.matches(regex);
    }
    private boolean validerNumeroTelephone(String numeroTelephone) {
        // Ajuster selon le format spécifique du pays (Tunisie)
        String regex = "^\\d{8}$";
        return numeroTelephone.matches(regex);
    }
    @FXML
    void annuler(ActionEvent event) {
        // Obtenez la scène à partir du bouton
        Scene scene = btn_annul.getScene();
        if (scene != null) {
            // Obtenez la fenêtre à partir de la scène
            Stage stage = (Stage) scene.getWindow();
            if (stage != null) {
                // Fermez la fenêtre
                stage.close();
            }
        }
    }

    @FXML
    void initialize(URL url, ResourceBundle resourceBundle) {
        hyperlink.setOnAction(event -> {
            try {
                // Chargez la page d'authentification depuis le fichier FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("authentification.fxml"));
                Parent root = loader.load();

                // Créez une nouvelle scène et un nouveau stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));

                // Affichez la nouvelle scène
                stage.show();

                // Fermez la fenêtre actuelle
                ((Stage) hyperlink.getScene().getWindow()).close();
            } catch (IOException e) {
                e.printStackTrace(); // Gérez l'exception selon vos besoins
            }

        });

    }


    @FXML
    private void redirectToAuthPage() {
        try {
            // Load the authentication FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/authentification.fxml"));
            Parent authParent = loader.load();

            // Create a new scene
            Scene authScene = new Scene(authParent);

            // Get the stage information
            Stage stage = new Stage();
            stage.setTitle("Authentification");
            stage.setScene(authScene);

            // Close the current registration window
            Stage currentStage = (Stage) hyperlink.getScene().getWindow();
            currentStage.close();

            // Show the authentication window
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


