package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import services.EvenementCrud;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import entities.Evenement;
import services.EvenementCrud;
public class AllEventController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<?> cityFilter;

    @FXML
    private ComboBox<?> monthFilter;

    @FXML
    private ComboBox<?> sportTypeFilter;

    @FXML
    private ComboBox<?> typeFilter;
    @FXML
    private ListView<Evenement> eventList;
    @FXML
    private TextField searchField;

    @FXML
    private ListView<Evenement> eventListView;

    private EvenementCrud evenementCrud = new EvenementCrud();
    private ObservableList<Evenement> allEvents;

    @FXML
    void initialize() {

        loadEvents();
        assert cityFilter != null : "fx:id=\"cityFilter\" was not injected: check your FXML file 'AllEvent.fxml'.";
        assert monthFilter != null : "fx:id=\"monthFilter\" was not injected: check your FXML file 'AllEvent.fxml'.";
        assert sportTypeFilter != null : "fx:id=\"sportTypeFilter\" was not injected: check your FXML file 'AllEvent.fxml'.";
        assert typeFilter != null : "fx:id=\"typeFilter\" was not injected: check your FXML file 'AllEvent.fxml'.";

    }
    private void loadEvents() {
        List<Evenement> events = evenementCrud.afficherEvent();
        allEvents = FXCollections.observableArrayList(events);

        eventListView.setItems(allEvents);
        eventListView.setCellFactory(param -> new ListCell<Evenement>() {
            @Override
            protected void updateItem(Evenement event, boolean empty) {
                super.updateItem(event, empty);

                if (empty || event == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Customize the cell content here
                    setText(event.getNomEv() + "\n" + event.getDescrptionEv().substring(0, Math.min(event.getDescrptionEv().length(), 50)) + "...");

                    String imagePath = event.getPhoto();

                    // Add a small image using a local file path
                    ImageView imageView = new ImageView(new Image("file:" + imagePath));
                    imageView.setFitWidth(10);
                    imageView.setFitHeight(50);
                    setGraphic(imageView);
                }
            }
        });
    }




}

//work
//    private void loadEvents() {
//        List<Evenement> events = evenementCrud.afficherEvent();
//        allEvents = FXCollections.observableArrayList();
//
//        for (Evenement evenement : events) {
//            allEvents.add(evenement.getNomEv());
//        }
//
//        eventListView.setItems(allEvents);
//    }
