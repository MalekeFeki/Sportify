package controllers;

import entities.enums.GenreEv;
import entities.enums.cityEV;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.EvenementCrud;
import entities.Evenement;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AllEventController implements Initializable {

    @FXML
    private FlowPane eventFlowPane;

    @FXML
    private Pagination pagination;

    private EvenementCrud evenementCrud = new EvenementCrud();
    private ObservableList<Evenement> allEvents;
    @FXML
    private ComboBox<GenreEv> typeFilter;

    @FXML
    private ComboBox<cityEV> cityFilter;



    private static final int EVENTS_PER_PAGE = 3; // Set the number of events per page
    @FXML
    private Button clearFiltersButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate GenreEv ComboBox with values from the enum
        ObservableList<GenreEv> genreEvenementList = FXCollections.observableArrayList(GenreEv.values());
        typeFilter.setItems(genreEvenementList);
        // Populate cityEV ComboBox with values from the enum
        ObservableList<cityEV> cityEVList = FXCollections.observableArrayList(cityEV.values());
        cityFilter.setItems(cityEVList);
        clearFiltersButton.setOnAction(event -> clearFiltersAndReload());
        loadEvents();
        configurePagination();
        configureScrollPane();
    }

    private void loadEvents() {
        List<Evenement> events = evenementCrud.afficherEvent();
        allEvents = FXCollections.observableArrayList(events);
        displayEvents(allEvents);
    }


    private void displayEvents(List<Evenement> events) {

        int pageCount = (int) Math.ceil((double) events.size() / EVENTS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(pageIndex -> createPage(pageIndex, events));

    }

    private VBox createPage(int pageIndex, List<Evenement> events) {
        int fromIndex = pageIndex * EVENTS_PER_PAGE;
        int toIndex = Math.min(fromIndex + EVENTS_PER_PAGE, events.size());

        List<Evenement> eventsToDisplay = events.subList(fromIndex, toIndex);

        FlowPane pageFlowPane = new FlowPane();
        pageFlowPane.setHgap(10);
        pageFlowPane.setVgap(10);

        for (Evenement event : eventsToDisplay) {
            VBox eventBox = createEventBox(event);
            pageFlowPane.getChildren().add(eventBox);
        }

        // Clear existing children and add the new FlowPane
//        eventFlowPane.getChildren().clear();
//        eventFlowPane.getChildren().add(pageFlowPane);

        return new VBox(pageFlowPane);
    }


    private VBox createPage(int pageIndex) {
        int fromIndex = pageIndex * EVENTS_PER_PAGE;
        int toIndex = Math.min(fromIndex + EVENTS_PER_PAGE, allEvents.size());

        List<Evenement> eventsToDisplay = allEvents.subList(fromIndex, toIndex);

        FlowPane pageFlowPane = new FlowPane();
        pageFlowPane.setHgap(10);
        pageFlowPane.setVgap(10);

        for (Evenement event : eventsToDisplay) {
            VBox eventBox = createEventBox(event);
            pageFlowPane.getChildren().add(eventBox);
        }

        return new VBox(pageFlowPane);
    }

    private VBox createEventBox(Evenement event) {
        Text eventNameText = new Text(event.getNomEv());
        // Limit the displayed description to 20 characters
        String eventDescription = event.getDescrptionEv();
        if (eventDescription.length() > 100) {
            eventDescription = eventDescription.substring(0, 20) + " ...";
        }

        Text eventDescriptionText = new Text(eventDescription);

        Button moreInfoButton = new Button("More Info");
        moreInfoButton.setOnAction(event1 -> showEventDetails(event));

        Button interestButton = new Button();
        updateInterestButtonText(interestButton, evenementCrud.getInterestStatus(event.getIDevent()));

        interestButton.setOnAction(event1 -> {
            int newInterestCount = (interestButton.getText().equals("Show Interest")) ? 1 : -1;

            evenementCrud.updateInterestStatus(event.getIDevent(), newInterestCount);

            // Update the button text
            interestButton.setText((newInterestCount == 1) ? "Remove Interest" : "Show Interest");
        });

        ImageView imageView = new ImageView(new Image("file:" + event.getPhoto()));
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        VBox eventBox = new VBox(
                new VBox(eventNameText, eventDescriptionText, moreInfoButton, interestButton),
                imageView
        );
        eventBox.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-spacing: 5px;");
        eventBox.setMinWidth(100);

        return eventBox;
    }
    // Add this helper method
    // Add this helper method
    private void updateInterestButtonText(Button interestButton, int interestCount) {
        interestButton.setText((interestCount > 0) ? "Remove Interest" : "Show Interest");
    }


    private void configureScrollPane() {
        scrollPane.setOnScroll(this::handleScroll);
    }
    @FXML
    private ScrollPane scrollPane;
    private void handleScroll(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        double scrollSpeed = 0.01; // You can adjust the scroll speed

        scrollPane.setVvalue(scrollPane.getVvalue() - deltaY * scrollSpeed);
    }

    private void configurePagination() {
        pagination.setPageFactory(pageIndex -> createPage(pageIndex));
    }

    @FXML
    private void filterEvents() {
        String selectedEventType = typeFilter.getValue() != null ? typeFilter.getValue().name() : null;
        String selectedCity = cityFilter.getValue() != null ? cityFilter.getValue().name() : null;
        System.out.println(selectedEventType+selectedCity);

        if (selectedEventType != null && selectedCity != null) {
            List<Evenement> filteredEvents = evenementCrud.filterEvents(selectedEventType, selectedCity);
            System.out.println("selectedEventType et selectedCity"+filteredEvents);
            displayEvents(filteredEvents);
        } else if (selectedEventType != null) {
            // Only event type is selected
            List<Evenement> filteredEvents = evenementCrud.filterEventsByEventType(selectedEventType);
            System.out.println("selectedEventType"+filteredEvents);
            displayEvents(filteredEvents);
        } else if (selectedCity != null) {
            // Only city is selected
            List<Evenement> filteredEvents = evenementCrud.filterEventsByCity(selectedCity);
            System.out.println("selectedCity"+filteredEvents);
            displayEvents(filteredEvents);
        } else {
            // If neither EventType nor City is selected, load all events
            loadEvents();
        }
    }
    @FXML
    private TextField textFieldSEARCH;

    @FXML
    private void searchEvents() {
        String searchQuery = textFieldSEARCH.getText();
        List<Evenement> searchResults = evenementCrud.searchEventsByName(searchQuery);
        System.out.println(searchResults);
        displayEvents(searchResults);
    }
    private void clearFiltersAndReload() {
        // Clear the selected values in ComboBoxes
        typeFilter.getSelectionModel().clearSelection();
        cityFilter.getSelectionModel().clearSelection();

        // Load all events without filters
        loadEvents();
    }
    private void showEventDetails(Evenement selectedEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventINFO.fxml"));
            Parent root = loader.load();
            EventINFOController eventINFOController = loader.getController();
            eventINFOController.setEventDetails(selectedEvent);
            // Get the current stage
            Stage stage = (Stage) eventFlowPane.getScene().getWindow();
            // Set the new scene to the current stage
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML: " + e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.out.println("FXML file not found: " + e.getMessage());
        }
    }





}
