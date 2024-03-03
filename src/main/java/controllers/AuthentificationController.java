package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import entities.MdpGen;
import entities.MdpHash;
import entities.Utilisateur;
import entities.Mailing;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.UtilisateurCrud;
import tools.MyConnection;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class AuthentificationController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Button btn_auth;

    @FXML
    private TextField tfemail;

    @FXML
    private PasswordField tfmdp;

    @FXML
    private CheckBox show;
    private List<Utilisateur> registeredUsers;
    private UtilisateurCrud utilisateurCrud = new UtilisateurCrud();

    private String cin;
    private String numTel;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;
    @FXML
    private Hyperlink hyperlink;
    @FXML
    private TextField tfshowpassword;
    private int tentativesInfructueuses = 0;
    private static final int SEUIL_TENTATIVES = 3;

    public void setRegisteredUsers(List<Utilisateur> registeredUsers) {
        this.registeredUsers = registeredUsers;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.show();
    }
    private void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Connexion réussie !");
        alert.showAndWait();
    }
    int idconnecte ;
    private String enteredEmail;
    @FXML
    void initialize() {

        show.setOnAction(event -> handleShowPassCheckboxClick());
        tfshowpassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (show.isSelected()) {
                tfmdp.setText(newValue);
            }
        });

        tfmdp.textProperty().addListener((observable, oldValue, newValue) -> {
            if (show.isSelected()) {
                tfshowpassword.setText(newValue);
            }
        });


        btn_auth.setOnAction(event -> {
            // Récupérer les informations d'identification fournies par l'utilisateur (par exemple, à partir de champs de texte)
            String email = tfemail.getText();
            String password = tfmdp.getText();

            // Appeler la fonction de login de votre classe UtilisateurCrud
            UtilisateurCrud utilisateurCrud = new UtilisateurCrud();

            // Vérifier si les informations d'identification sont valides
            if (utilisateurCrud.authenticateUser(email, password)) {
                // Si l'utilisateur est trouvé, afficher un message de succès
                showSuccessMessage();
                Connection cnx2 = MyConnection.instance.getCnx();
                String reqUserId = "SELECT id FROM utilisateur WHERE email = ?";
                PreparedStatement pstUserId = null;
                try {
                    pstUserId = cnx2.prepareStatement(reqUserId);
                    pstUserId.setString(1, tfemail.getText());
                    ResultSet rsUserId = pstUserId.executeQuery();
                    rsUserId.next();
                    idconnecte = rsUserId.getInt(1);
                    MyConnection.getInstance().setId(idconnecte);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                // Store the entered email in the array
                enteredEmail = tfemail.getText();


                // Rediriger vers le profil correspondant au rôle de l'utilisateur
                redirectToProfile(utilisateurCrud.getUtilisateurByEmail(email).getRole().toString());

                // Fermer la fenêtre d'authentification
                Stage stage = (Stage) btn_auth.getScene().getWindow();
                stage.close();

            } else {
                // Si l'authentification échoue, afficher un message d'erreur
                showAlert("Email ou mot de passe incorrect !");
                tentativesInfructueuses++;
                if (tentativesInfructueuses>=SEUIL_TENTATIVES){
                    showAlert("3 tentatives fausses ou plus!Votre compte est bloqué pour 10 minutes");
                    // Vous pouvez ajouter ici la logique pour bloquer l'utilisateur pendant 10 minutes
                    // Par exemple, désactiver le bouton de connexion et afficher un message à l'utilisateur
                    btn_auth.setDisable(true);
                    // Utilisez une tâche planifiée (ScheduledExecutorService) pour débloquer l'utilisateur après 10 minutes
                    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                    scheduler.schedule(() -> {
                        Platform.runLater(() -> {
                            // Débloquer l'utilisateur après 10 minutes en réactivant le bouton de connexion
                            btn_auth.setDisable(false);
                            showAlert("Votre compte est maintenant débloqué. Vous pouvez essayer de vous connecter à nouveau.");
                        });
                    }, 10, TimeUnit.MINUTES);
                }
            }


        });

        hyperlink.setOnAction(event1 -> {
            // Handle the hyperlink click event
            if (tfemail == null || tfemail.getText().isEmpty()) {
                // Show an alert or handle the case where the email is empty
                System.out.println("Veuillez entrer votre email pour réinitialiser votre mot de passe");
            } else {
                // Generate a new password
                String newPassword = MdpGen.genererMdp();
                System.out.println("new pass: " + newPassword);

                // Fetch the user from the database based on the provided email
                Utilisateur utilisateur = utilisateurCrud.getUtilisateurByEmail(tfemail.getText());


                utilisateur.setMdp(newPassword);
                // Update the password in the database
                utilisateurCrud.modifierEntite(utilisateur);
// Send the new password to the user's email
                sendMail("Votre nouveau mot de passe est: " + newPassword);
                String hashedNewPassword = MdpHash.hashPassword(newPassword);
                utilisateur.setMdp(hashedNewPassword);
                // Update the password in the database
                utilisateurCrud.modifierEntite(utilisateur);


            }
        });

    }

    @FXML
    void handleShowPassCheckboxClick() {
        if (show.isSelected()) {
            // Afficher le mot de passe en clair
            tfshowpassword.setText(tfmdp.getText());
            tfshowpassword.setVisible(true);
            tfmdp.setVisible(false);
        } else {
            // Masquer le mot de passe en clair et montrer le PasswordField à nouveau
            tfmdp.setText(tfshowpassword.getText());
            tfmdp.setVisible(true);
            tfshowpassword.setVisible(false);
        }
    }
    private void sendPassword(){
        System.out.println("cc");
        String query2="select * from utilisateur where email=? ";
        String email1="empty";
        try {
            // Access the connection using the singleton pattern
            Connection cnx2 = MyConnection.getInstance().getCnx();
            PreparedStatement smt = cnx2.prepareStatement(query2);
            smt.setString(1, tfemail.getText());
            ResultSet rs= smt.executeQuery();
            if(rs.next()){
                email1=rs.getString("email");
                System.out.println(email1);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        Mailing.sendEmail(email1,"Recupération du mot de passe","votre mot de passe est"+tfmdp);
    }
    public void sendMail(String recepient){
        System.out.println("Preparing to send email");
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        String myAccountEmail = "malekfeki18@gmail.com";
        String passwordd = "ozgm ipxf foxo uplz";

        Session session = Session.getInstance(properties, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(myAccountEmail,passwordd);
            }
        });
        Message message =preparedMessage(session,myAccountEmail,recepient);
        try{
            Transport.send(message);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Sportify :: Boite Mail");
            alert.setHeaderText(null);
            alert.setContentText("consulter votre boite mail !!");
            alert.showAndWait();

        }catch(Exception ex){
            ex.printStackTrace();

        }

    }
    public void sendPasswordResetEmail(String recipientEmail, String resetCode) throws MessagingException {
        // Email message details
        String subject = "Password Reset Request";
        String body = "Dear User,\n\nTo reset your password, please use the following code:\n\n" + resetCode + "\nBest regards,\nInsight Team";
        System.out.println("Email body: " + body); // Print the email body with the reset code

        // Send the email
        Mailing.sendEmail(recipientEmail, subject, body);
    }
    private Message preparedMessage(Session session, String myAccountEmail, String recepient){
        String query2="select * from utilisateur where email=?";
        String userEmail="null" ;
        String pass="empty";
        try {
            // Access the connection using the singleton pattern
            Connection cnx2 = MyConnection.getInstance().getCnx();
            PreparedStatement smt = cnx2.prepareStatement(query2);
            smt.setString(1, tfemail.getText());
            ResultSet rs= smt.executeQuery();
            System.out.println(rs);
            if(rs.next()){
                pass=rs.getString("mdp");
                userEmail=rs.getString("email");                }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.print("testtt");
        String text="Votre mot de passe est :"+pass+"";
        String object ="Recupération de mot de passe";
        Message message = new MimeMessage(session);
        try{
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject(object);
            String htmlcode ="<h1> "+text+" </h1> <h2> <b> </b2> </h2> ";
            message.setContent(htmlcode, "text/html");
            System.out.println("Message envoyé");

            System.out.println(pass);

            return message;

        }catch(MessagingException ex){
            ex.printStackTrace();
        }
        return null;
    }

    private void sendPasswordResetEmail(String recipient) {

            String senderEmail = "malekfeki18@gmail.com";
            String senderPassword = "ozgm ipxf foxo uplz";


            String subject = "Réinitialisation de mot de passe";
            String body = "Cliquez sur le lien suivant pour réinitialiser votre mot de passe: "+tfmdp;

            // Set the recipient to the value of tfemail.getText()
             recipient = tfemail.getText();

            Mailing.sendEmail(recipient, subject, body);
        }

    public void redirectToProfile(String role) {

        try {
            String fxmlFileName;
            switch (role) {
                case "ADMIN":
                    fxmlFileName = "/ProfilAdmin.fxml";
                    break;
                case "MEMBRE":
                    fxmlFileName = "/ProfilMembre.fxml";
                    break;
                case "PROPRIETAIRE":
                    fxmlFileName = "/ProfilProp.fxml";
                    break;
                default:
                    // Gérer d'autres cas si nécessaire
                    return;
            }

            // Charger le fichier FXML correspondant au rôle de l'utilisateur
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
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
    }


}