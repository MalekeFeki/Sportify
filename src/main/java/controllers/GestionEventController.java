package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import entities.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.EvenementCrud;
import entities.Evenement;

public class GestionEventController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private ObservableList<Evenement> observableEvents = FXCollections.observableArrayList();

    private EvenementCrud evenementCrud = new EvenementCrud();
    @FXML
    private TableColumn<Evenement, Integer> idColumn;
    @FXML
    private TableColumn<Evenement, String> nomColumn;
    @FXML
    private TableColumn<Evenement, String> dateDebutColumn;
    @FXML
    private TableColumn<Evenement, String> datefinColumn;
    @FXML
    private TableColumn<Evenement, String> lieuColumn;
    @FXML
    private TableColumn<Evenement, String> GenreColumn;
    @FXML
    private TableColumn<Evenement, String> typeColumn;
    @FXML
    private TableColumn<Evenement, Integer> nombrePersonneInteresseColumn;
    @FXML
    private TableColumn<Evenement, Integer> CapaciteColumn;
    @FXML
    private TableView<Evenement> eventTableView;
    @FXML
    private Button AjouterEvenement;

    @FXML
    private void initialize() {
        TableColumn<Evenement, Integer> idColumn = new TableColumn<>("IDevent");
        TableColumn<Evenement, String> nomColumn = new TableColumn<>("Nom");
        TableColumn<Evenement, String> dateDebutColumn = new TableColumn<>("Date Debut");
        TableColumn<Evenement, String> datefinColumn = new TableColumn<>("Date Fin");
        TableColumn<Evenement, String> lieuColumn = new TableColumn<>("Lieu");
        TableColumn<Evenement, String> GenreColumn = new TableColumn<>("Genre Evenement");
        TableColumn<Evenement, String> typeColumn = new TableColumn<>("Type Evenement");
        TableColumn<Evenement, Integer> nombrePersonneInteresseColumn = new TableColumn<>("Nombre Personne Interesse");
        TableColumn<Evenement, Integer> CapaciteColumn = new TableColumn<>("Capacite");
        TableColumn<Evenement, Void> optionsColumn = new TableColumn<>("options");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("IDevent"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("NomEv"));
        dateDebutColumn.setCellValueFactory(new PropertyValueFactory<>("DatedDebutEV"));
        datefinColumn.setCellValueFactory(new PropertyValueFactory<>("DatedFinEV"));
        lieuColumn.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        GenreColumn.setCellValueFactory(new PropertyValueFactory<>("GenreEvenement"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeEV"));
        nombrePersonneInteresseColumn.setCellValueFactory(new PropertyValueFactory<>("nombrePersonneInteresse"));
        CapaciteColumn.setCellValueFactory(new PropertyValueFactory<>("Capacite"));
        optionsColumn.setCellFactory(param -> new TableCell<Evenement, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Button modifyButton = new Button("Modify");
            private final Button reservationButton = new Button("reservation");
            private final Button printButton = new Button("Print");
            {
                deleteButton.setOnAction(event -> {
                    Evenement eventToDelete = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Delete Event");
                    alert.setContentText("Voulez-vous vraiment supprimer l'événement " + eventToDelete.getNomEv() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        evenementCrud.supprimerEvent(eventToDelete.getIDevent());
                        loadEvents();
                        showAlert("Event Deleted", "Event has been deleted successfully.");
                    }
                });

                modifyButton.setOnAction(event -> {
                    Evenement eventToModify = getTableView().getItems().get(getIndex());


                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Modify Event");
                    alert.setContentText("Voulez-vous vraiment modifier l'événement " + eventToModify.getNomEv() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        redirectToModifierEvent(eventToModify);
                    }
                });
                reservationButton.setOnAction(event -> {
                    Evenement eventToModify = getTableView().getItems().get(getIndex());
                    System.out.println(eventToModify);
                    redirectToEventReservations(eventToModify);
                });
                printButton.setOnAction(event -> {
                    Evenement eventToModify = getTableView().getItems().get(getIndex());
                    System.out.println(eventToModify);
                    printEventDetails(eventToModify);
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(deleteButton, modifyButton,reservationButton,printButton);
                    buttons.setSpacing(5);
                    setGraphic(buttons);
                }
            }
        });
// Set preferred column widths
        idColumn.setPrefWidth(70);  // Adjust the value based on your preference
        nomColumn.setPrefWidth(120);  // Adjust the value based on your preference
        dateDebutColumn.setPrefWidth(120);  // Adjust the value based on your preference
        datefinColumn.setPrefWidth(120);  // Adjust the value based on your preference
        lieuColumn.setPrefWidth(100);  // Adjust the value based on your preference
        GenreColumn.setPrefWidth(120);  // Adjust the value based on your preference
        typeColumn.setPrefWidth(120);  // Adjust the value based on your preference
        nombrePersonneInteresseColumn.setPrefWidth(150);  // Adjust the value based on your preference
        CapaciteColumn.setPrefWidth(70);  // Adjust the value based on your preference

        eventTableView.getColumns().addAll(idColumn, nomColumn, dateDebutColumn, datefinColumn, lieuColumn, GenreColumn, typeColumn, nombrePersonneInteresseColumn, CapaciteColumn);
        eventTableView.getColumns().add(optionsColumn);
        loadEvents();
    }
    @FXML
    private void printEventDetails(Evenement selectedEvent) {
        if (selectedEvent != null) {
            // Call a method to retrieve important information for the event
//            String eventInfo = retrieveEventInfo(selectedEvent);

            // Call the print method with the event information
            printEvent(selectedEvent);
        } else {
            showAlert("Error", "Please select an event.");
        }
    }
    private String retrieveEventInfo(Evenement event) {
        // Implement logic to retrieve important information for the event
        // Concatenate event details like name, description, date, location, etc.
        return "Event name: " + event.getNomEv() +
                "\nDescription: " + event.getDescrptionEv() +
                "\nDate de Debut: " + event.getDatedDebutEV() +
                "\nEnd Date: " + event.getDatedFinEV() +
                "\nStart Time: " + event.getHeureEV() +
                "\nCapacity: " + event.getCapacite() +
                "\nLocation: " + event.getLieu() + event.getCity()+
                "\ntype Evenement: " + event.getTypeEV()
                +
                "\nGenre Evenement: " + event.getGenreEvenement()+
                "\nTele: " + event.getTele() +
                "\nEmail: " + event.getEmail() ;
    }
    private void printEvent(Evenement event) {
        // Retrieve event details
        String eventInfo = retrieveEventInfo(event);

        // Create a PrinterJob
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            // Create a printable area
            PageLayout pageLayout = printerJob.getPrinter().createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
            double printableWidth = pageLayout.getPrintableWidth();
            double printableHeight = pageLayout.getPrintableHeight();

            // Create a GridPane to hold the event information
            GridPane content = new GridPane();
            content.setHgap(10);
            content.setVgap(5);

            // Create Labels and Text nodes for each piece of information
            Label nameLabel = createBoldLabel("Event Name:");
            Text nameText = new Text(event.getNomEv());

            Label descriptionLabel = createBoldLabel("Description:");
            Text descriptionText = new Text(event.getDescrptionEv());

            Label dateDebutLabel = createBoldLabel("Date de Debut:");
            Text dateDebutText = new Text(event.getDatedDebutEV().toString());

            Label endDateLabel = createBoldLabel("End Date:");
            Text endDateText = new Text(event.getDatedFinEV().toString());

            Label startTimeLabel = createBoldLabel("Start Time:");
            Text startTimeText = new Text(event.getHeureEV());

            Label capacityLabel = createBoldLabel("Capacity:");
            Text capacityText = new Text(String.valueOf(event.getCapacite()));

            Label locationLabel = createBoldLabel("Location:");
            Text locationText = new Text(event.getLieu() + " " + event.getCity());

            Label typeLabel = createBoldLabel("Type Evenement:");
            Text typeText = new Text(event.getTypeEV().toString());

            Label genreLabel = createBoldLabel("Genre Evenement:");
            Text genreText = new Text(event.getGenreEvenement().toString());

            Label teleLabel = createBoldLabel("Tele:");
            Text teleText = new Text(event.getTele());

            Label emailLabel = createBoldLabel("Email:");
            Text emailText = new Text(event.getEmail());

            // Load the image
            Image image = new Image(event.getPhoto()); // Replace with the actual path to your image
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(printableWidth - 20); // Adjust the width based on your preference
            imageView.setPreserveRatio(true);

            // Add labels, text nodes, and image to the GridPane
            content.addColumn(0, nameLabel, descriptionLabel, dateDebutLabel, endDateLabel, startTimeLabel,
                    capacityLabel, locationLabel, typeLabel, genreLabel, teleLabel, emailLabel);
            content.addColumn(1, nameText, descriptionText, dateDebutText, endDateText, startTimeText,
                    capacityText, locationText, typeText, genreText, teleText, emailText);
            content.add(imageView, 0, content.getRowCount(), 2, 1);

            // Create a new Text node for the current page
            Text pageText = new Text();
            pageText.getStyleClass().add("event-info");

            // Calculate the number of pages needed
            double contentHeight = content.getBoundsInLocal().getHeight();
            int numPages = (int) Math.ceil(contentHeight / printableHeight);

            // Print each page
            for (int page = 0; page < numPages; page++) {
                // Update the GridPane for the current page
                content.setLayoutY(-page * printableHeight);

                // Print the page
                if (printerJob.printPage(pageLayout, content)) {
                    // Page printed successfully
                } else {
                    showAlert("Print Error", "Error occurred while printing.");
                    break;
                }
            }

            // End the print job
            printerJob.endJob();
        } else {
            showAlert("Print Error", "Error creating PrinterJob.");
        }
    }

    private Label createBoldLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold;");
        return label;
    }

    private void loadEvents() {
        observableEvents.clear();
        List<Evenement> events = evenementCrud.afficherEvent();
        observableEvents.addAll(events);
        eventTableView.setItems(observableEvents);
    }
    @FXML
    private void redirectToEventReservations(Evenement selectedEvent) {
//        Evenement selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedEvent);
        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListOfEventReservation.fxml"));
                Parent root = loader.load();
                ListOfEventReservation eventReservationController = loader.getController();
                eventReservationController.setEventId(selectedEvent);
                Stage stage = (Stage) eventTableView.getScene().getWindow();
                stage.setScene(new Scene(root));
                eventReservationController.initialize();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading FXML: " + e.getMessage());
            }
        } else {
            System.err.println("Event not selected.");
        }
    }

    @FXML
    private void redirectToAjouterEvenement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) AjouterEvenement.getScene().getWindow();
            stage.setScene(new Scene(root));
            GestionEventController gestionEventController = loader.getController();
            gestionEventController.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void redirectToModifierEvent(Evenement eventToModify) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierEvent.fxml"));
            Parent root = loader.load();
            ModifierEventController modifierEventController = loader.getController();
            modifierEventController.setEventToModify(eventToModify);

            Stage stage = (Stage) eventTableView.getScene().getWindow();

            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

