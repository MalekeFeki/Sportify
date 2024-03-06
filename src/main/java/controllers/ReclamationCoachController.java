package controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import services.ReclamationCrud;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class ReclamationCoachController {

    @FXML
    private TableView<Reclamation> tableViewReclamations;

    @FXML
    private TableColumn<Reclamation, String> colReclamation;

    @FXML
    private Button tfajoutreclamation;

    @FXML
    private Button tfmodifreclamation;

    @FXML
    private Button tfsuppressionreclamation;

    @FXML
    private TextField texte_reclamation;

    private ReclamationCrud reclamationCrud;
    private ObservableList<Reclamation> reclamationsObservableList;
    private int nombreTentativesAjout = 0;
    private final int MAX_TENTATIVES_AJOUT = 3;
    private int nombreTentativesModification = 0;
    private final int MAX_TENTATIVES_MODIFICATION = 3;

    @FXML
    public void initialize() {
        reclamationCrud = new ReclamationCrud();
        chargerDonnees();

        colReclamation.setCellValueFactory(new PropertyValueFactory<>("texte"));

        tableViewReclamations.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                afficherReclamationSelectionnee(newSelection);
            }
        });
    }

    private void bloquerBoutonAjout() {
        tfajoutreclamation.setDisable(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(20), event -> {
            tfajoutreclamation.setDisable(false);
        }));
        timeline.play();
    }

    @FXML
    void ajouterReclamation(ActionEvent event) {
        if (nombreTentativesAjout >= MAX_TENTATIVES_AJOUT) {
            afficherMessageErreur("Vous avez atteint le nombre maximum de tentatives d'ajout (3)");
            bloquerBoutonAjout();
            return;
        }

        String texteReclamation = texte_reclamation.getText();
        if (!texteReclamation.isEmpty()) {
            if (texteReclamation.length() <= 20) {
                if (!BadWordsDetector.containsBadWord(texteReclamation)) {
                    Reclamation nouvelleReclamation = new Reclamation(texteReclamation);
                    reclamationCrud.ajouterReclamation(nouvelleReclamation);
                    sendMail("ines.dkhili@esprit.tn");
                    afficherMessage("Réclamation ajoutée avec succès");

                    viderChamps();
                    chargerDonnees();
                    nombreTentativesAjout++;
                } else {
                    afficherMessageErreur("La réclamation contient des mots inappropriés.");
                }
            } else {
                afficherMessageErreur("La réclamation ne doit pas dépasser 20 caractères.");
            }
        } else {
            afficherMessageErreur("Veuillez saisir une réclamation");
        }
    }

    @FXML
    void modifierReclamation(ActionEvent event) {
        if (nombreTentativesModification < MAX_TENTATIVES_MODIFICATION) {
            Reclamation reclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
            if (reclamation != null) {
                String nouveauTexteReclamation = texte_reclamation.getText();
                if (!nouveauTexteReclamation.isEmpty()) {
                    if (!BadWordsDetector.containsBadWord(nouveauTexteReclamation)) {
                        String ancienTexteReclamation = reclamation.getTexte();
                        reclamation.setTexte(nouveauTexteReclamation);
                        reclamationCrud.modifierReclamation(reclamation, ancienTexteReclamation);
                        afficherMessage("Réclamation modifiée avec succès");
                        viderChamps();
                        chargerDonnees();
                        nombreTentativesModification++;
                    } else {
                        afficherMessageErreur("La réclamation contient des mots inappropriés.");
                    }
                } else {
                    afficherMessageErreur("Veuillez saisir le nouveau texte de la réclamation");
                }
            } else {
                afficherMessageErreur("Veuillez sélectionner une réclamation à modifier");
            }
        } else {
            afficherMessageErreur("Vous avez atteint le nombre maximum de tentatives de modification (3)");
        }
    }

    @FXML
    void supprimerReclamation(ActionEvent event) {
        Reclamation reclamation = tableViewReclamations.getSelectionModel().getSelectedItem();
        if (reclamation != null) {
            String textereclamation = reclamation.getTexteReclamation();
            reclamationCrud.supprimerReclamation(textereclamation);
            afficherMessage("Réclamation supprimée avec succès");
            viderChamps();
            chargerDonnees();
        } else {
            afficherMessageErreur("Veuillez sélectionner une réclamation à supprimer");
        }
    }

    private void chargerDonnees() {
        List<Reclamation> reclamations = reclamationCrud.afficherReclamations();
        reclamationsObservableList = FXCollections.observableArrayList(reclamations);
        tableViewReclamations.setItems(reclamationsObservableList);
    }

    private void viderChamps() {
        texte_reclamation.clear();
    }

    private void afficherMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherMessageErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void afficherReclamationSelectionnee(Reclamation reclamation) {
        if (reclamation != null) {
            texte_reclamation.setText(reclamation.getTexte());
        }
    }

    /*  private void envoyerEmail(String to, String subject, String content) {
          // Paramètres de configuration pour l'envoi d'e-mails
          Properties props = new Properties();
          props.put("mail.smtp.auth", "true");
          props.put("mail.smtp.starttls.enable", "true");
          props.put("mail.smtp.host", "smtp.gmail.com"); // Remplacez par votre serveur SMTP
          props.put("mail.smtp.port", "587"); // Port SMTP (peut varier)

          // Informations d'authentification pour l'envoi d'e-mails
          String username = "inesdkhl@gmail.com"; // Remplacez par votre adresse e-mail
          String password = "sawg zsjc ciej ahze"; // Remplacez par votre mot de passe

          // Création de*/
    private Message preparedMessage(Session session, String myAccountEmail, String recepient) throws MessagingException {
        String texteReclamation = texte_reclamation.getText();
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myAccountEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
        message.setSubject("Nouvelle réclamation ajoutée");
        message.setText("  Une nouvelle réclamation a été ajoutée : "+ texteReclamation);
        return message;
    }
    public void sendMail(String recepient){
        System.out.println("Preparing to send email");
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        String myAccountEmail = "inesdkhl@gmail.com";
        String passwordd = "sawg zsjc ciej ahze";

        Session session = Session.getInstance(properties, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(myAccountEmail,passwordd);
            }
        });

        Message message = null;
        try {
            message = preparedMessage(session,myAccountEmail,recepient);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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

}