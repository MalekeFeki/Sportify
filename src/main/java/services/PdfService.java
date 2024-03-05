package services;

import entities.Evenement;
import javafx.print.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class PdfService {
    public static void printEvent(Evenement event) {

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

    private static Label createBoldLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold;");
        return label;
    }
    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
