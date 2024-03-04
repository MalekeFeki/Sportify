package controllers;
import controllers.ModifierAvisController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.AvisCrud;
import entities.Avis;
import entities.enums.TypeAvis;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AfficherAvisController implements Initializable {
    private AvisCrud avisCrud;
    private TableColumn<Avis, Integer> idA;
    private TableColumn<Avis, TypeAvis> type;
    private TableColumn<Avis, String> description;
    @FXML
    private TableView<Avis> avisTableView;

    @FXML
    private ObservableList<Avis> observableAVIS = FXCollections.observableArrayList();

    @FXML
    private PieChart avisPieChart;

    @FXML
    private Label ratingLabel;

    private void loadAvis() {
        observableAVIS.clear();
        List<Avis> avis = avisCrud.afficherAvis();
        observableAVIS.addAll(avis);
        avisTableView.setItems(observableAVIS);
    }

    public AfficherAvisController() {
        this.avisCrud = new AvisCrud();
    }

    public void ajouterAvis(TypeAvis type, String description) {
        Avis avis = new Avis(0, type, description);
        avisCrud.ajouterAvis(avis);
    }

    public void afficherAvis() {
        List<Avis> avisList = avisCrud.afficherAvis();
        if (avisList.isEmpty()) {
            System.out.println("Aucun avis disponible");
        } else {
            for (Avis avis : avisList) {
                System.out.println(avis.toString());
            }
        }
    }

    public void modifierAvis(int id, TypeAvis type, String description) {
        Avis avis = new Avis(id, type, description);
        avisCrud.modifierAvis(avis);
    }

    public void supprimerAvis(int id) {
        avisCrud.supprimerAvis(id);
        loadAvis();
        updatePieChartData();
    }

    public void calculerAvisMoyen() {
        List<Avis> avisList = avisCrud.afficherAvis();
        Avis avisMoyen = avisCrud.calculerAvisMoyen(avisList);

        System.out.println("Avis moyen calculé:");
        System.out.println(avisMoyen);

        displayStarRating(avisMoyen.getType());
    }

    public void exporterAvis() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Avis");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));

        // Show save file dialog
        Stage stage = (Stage) avisTableView.getScene().getWindow();
        java.io.File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (Writer writer = new FileWriter(file)) {
                List<Avis> allAvis = avisCrud.afficherAvis();

                String csvData = allAvis.stream()
                        .map(avis -> String.format("%d,%s,%s", avis.getIdA(), avis.getType(), avis.getDescription()))
                        .collect(Collectors.joining("\n"));

                writer.write(csvData);

                showAlert("Export Successful", "Avis data has been exported to " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Export Error", "An error occurred while exporting Avis data.");
            }
        }
    }

    private void displayStarRating(TypeAvis typeAvis) {
        String starRating = switch (typeAvis) {
            case MEDIOCRE -> "A"; // 1 star
            case PASSABLE -> "AA"; // 2 stars
            case MOYEN -> "AAA"; // 3 stars
            case BIEN -> "AAAA"; // 4 stars
            case EXCELLENT -> "AAAAA"; // 5 stars
        };

        ratingLabel.setText(starRating);
    }

    private void filterBadWords(Avis avis) {
        // Your logic to filter out bad words in the description
        String filteredDescription = avis.getDescription().replaceAll("badword1|badword2", "***");
        avis.setDescription(filteredDescription);
    }

    private void updatePieChartData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        List<Avis> avisList = avisCrud.afficherAvis();
        for (TypeAvis typeAvis : TypeAvis.values()) {
            long count = avisList.stream()
                    .filter(avis -> avis.getType() == typeAvis)
                    .count();
            pieChartData.add(new PieChart.Data(typeAvis.name(), count));
        }

        avisPieChart.setData(pieChartData);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idA = new TableColumn<>("idA");
        type = new TableColumn<>("typeAvis");
        description = new TableColumn<>("description");
        TableColumn<Avis, Void> optionsColumn = new TableColumn<>("options");

        idA.setCellValueFactory(new PropertyValueFactory<>("idA"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));

        optionsColumn.setCellFactory(param -> new TableCell<Avis, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Button modifyButton = new Button("Modify");

            {
                deleteButton.setOnAction(event -> {
                    Avis avisToDelete = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Delete Avis");
                    alert.setContentText("Voulez-vous vraiment supprimer l'avis " + avisToDelete.getIdA() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        supprimerAvis(avisToDelete.getIdA());
                        showAlert("Avis Deleted", "Avis has been deleted successfully.");
                    }
                });

                modifyButton.setOnAction(event -> {
                    Avis avisToModify = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Modify Avis");
                    alert.setContentText("Voulez-vous vraiment modifier l'avis " + avisToModify.getIdA() + "?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        redirectToModifierAvis(avisToModify);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(deleteButton, modifyButton);
                    buttons.setSpacing(5);
                    setGraphic(buttons);
                }
            }
        });

        avisTableView.getColumns().addAll(idA, type, description);
        avisTableView.getColumns().add(optionsColumn);
        loadAvis();

        // PieChart initialization
        updatePieChartData();
    }

    private void redirectToModifierAvis(Avis avisToModify) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAvis.fxml"));
            Parent root = loader.load();
            ModifierAvisController modifierAvisController = loader.getController();
            modifierAvisController.setAvisToModify(avisToModify);
            Stage stage = (Stage) avisTableView.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
