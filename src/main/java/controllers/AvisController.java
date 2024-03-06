package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import entities.Avis;
import entities.enums.TypeAvis;
import javafx.stage.Stage;
import services.AvisCrud;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class AvisController {

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ChoiceBox<String> ratingChoiceBox;

    @FXML
    private Button submitRatingButton;

    private static final List<String> motsVulgaires = Arrays.asList("chat", "chien", "animal");

    @FXML
    void initialize() {
        ratingChoiceBox.getItems().addAll("MEDIOCRE", "PASSABLE", "MOYEN", "BIEN", "EXCELLENT");
    }

    @FXML
    void ajouterAvis(ActionEvent avis1) {
        String description = descriptionTextArea.getText();
        String selectedRating = ratingChoiceBox.getValue();
        if (description.trim().isEmpty() || selectedRating == null) {
            showAlert(Alert.AlertType.WARNING, "Avis non ajouté", "La description et le type d'avis ne peuvent pas être vides.");
            return;
        }

        if (contientMotsVulgaires(description)) {
            showAlert(Alert.AlertType.WARNING, "Avis non ajouté", "La description contient des mots vulgaires.");
            return;
        }

        Avis avis = new Avis();
        avis.setDescription(description);
        avis.setType(TypeAvis.valueOf(selectedRating));
        AvisCrud avisCrud = new AvisCrud();
        avisCrud.ajouterAvis(avis);
        sendMail("melek.jdidi19@gmail.com");
        showAlert(Alert.AlertType.INFORMATION, "Rating Submitted", "Your rating has been submitted successfully!");
        redirectToAfficherAvis();
    }


    private boolean contientMotsVulgaires(String description) {
        for (String mot : motsVulgaires) {
            if (description.toLowerCase().contains(mot)) {
                return true;
            }
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void redirectToAfficherAvis() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAvis.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) submitRatingButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading AfficherAvis.fxml");
            e.printStackTrace();
        }
    }
    private Message preparedMessage(Session session, String myAccountEmail, String recepient) throws MessagingException {
        String selectedRating = ratingChoiceBox.getValue();
        String description = descriptionTextArea.getText();
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(myAccountEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
        message.setSubject("Avis ajouté");
        message.setText("  Un avis a été ajouté : "+ selectedRating +"par vous avec la description suivante : "+ description);
        return message;
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
