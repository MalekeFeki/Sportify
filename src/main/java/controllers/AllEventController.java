package controllers;

import entities.enums.GenreEv;
import entities.enums.cityEV;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import services.EvenementCrud;
import entities.Evenement;
import services.EventReservationCrud;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
    private EventReservationCrud eventReservationCrud = new EventReservationCrud();

@FXML
private Button reserveButton ;

    private static final int EVENTS_PER_PAGE = 3;
    @FXML
    private Button clearFiltersButton;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<GenreEv> genreEvenementList = FXCollections.observableArrayList(GenreEv.values());
        typeFilter.setItems(genreEvenementList);

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
        eventNameText.getStyleClass().add("event-title");

        String eventDescription = event.getDescrptionEv();
        if (eventDescription.length() > 100) {
            eventDescription = eventDescription.substring(0, 20) + " ...";
        }

        Text eventDescriptionText = new Text(eventDescription);
        Text startDateText = new Text("Start Date: " + event.getDatedDebutEV().toString());
        Text endDateText = new Text("End Date: " + event.getDatedFinEV().toString());
        endDateText.getStyleClass().add("date-text");  // Add style class to end date text
        startDateText.getStyleClass().add("date-text");  // Add style class to start date text

        Button moreInfoButton = new Button("More Info");
        moreInfoButton.getStyleClass().add("info-button");
        moreInfoButton.setOnAction(event1 -> showEventDetails(event));
        Button reserveButton = new Button("reserve");
        reserveButton.getStyleClass().add("reserve-button");
        reserveButton.setOnAction(event1 -> redirectToReservationForm(event));
        Button interestButton = new Button();
        interestButton.getStyleClass().add("interest-button");
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
        if ("publicevent".equalsIgnoreCase(event.getTypeEV().toString()) || eventReservationCrud.afficherReservation(event.getIDevent()).size()>=event.getCapacite()) {
            reserveButton.setDisable(true);
        } else {
            reserveButton.setDisable(false);
        }


        Label countdownLabel = new Label();
        updateCountdownLabel(countdownLabel,event.getDatedDebutEV(),event.getDatedFinEV(),event.getHeureEV(),reserveButton);
        countdownLabel.getStyleClass().add("countdown-label");
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event1 -> updateCountdownLabel(countdownLabel, event.getDatedDebutEV(),event.getDatedFinEV(),event.getHeureEV()))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        HBox buttonsHBox = new HBox(10, moreInfoButton, reserveButton, interestButton);
        buttonsHBox.getStyleClass().add("buttons-hbox");

        StackPane buttonsStackPane = new StackPane(buttonsHBox);
        StackPane.setAlignment(buttonsHBox, Pos.CENTER_RIGHT);

        VBox eventBox = new VBox(
                new VBox(eventNameText, eventDescriptionText, startDateText, endDateText, buttonsStackPane),
                imageView ,countdownLabel
        );
        eventBox.getStyleClass().add("event-box");
//        eventBox.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-spacing: 5px; -fx-border-color: #ff7741");
        eventBox.setMinWidth(100);

        return eventBox;
    }
    @FXML
    private Label countdownLabel;

    private void updateCountdownLabel(Label countdownLabel, Date eventdebutDate, Date eventfinDate, String heureEV,Button reserveButton) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime eventTime = LocalTime.parse(heureEV, formatter);
        LocalDateTime eventDebutDateTime = LocalDateTime.of(eventdebutDate.toLocalDate(), eventTime);
        LocalDateTime eventFinDateTime = LocalDateTime.of(eventfinDate.toLocalDate(), eventTime);
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (currentDateTime.isBefore(eventDebutDateTime)) {
            long seconds = ChronoUnit.SECONDS.between(currentDateTime, eventDebutDateTime);
            updateLabelWithCountdown(countdownLabel, seconds);
        } else if (currentDateTime.isAfter(eventFinDateTime)) {
            countdownLabel.setText("Event has ended.");
            reserveButton.setDisable(true);
        } else {
            countdownLabel.setText("Event is happening now.");
            reserveButton.setDisable(true);
        }
    }
    private void updateCountdownLabel(Label countdownLabel, Date eventdebutDate, Date eventfinDate, String heureEV) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime eventTime = LocalTime.parse(heureEV, formatter);
        LocalDateTime eventDebutDateTime = LocalDateTime.of(eventdebutDate.toLocalDate(), eventTime);
        LocalDateTime eventFinDateTime = LocalDateTime.of(eventfinDate.toLocalDate(), eventTime);
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (currentDateTime.isBefore(eventDebutDateTime)) {
            long seconds = ChronoUnit.SECONDS.between(currentDateTime, eventDebutDateTime);
            updateLabelWithCountdown(countdownLabel, seconds);
        } else if (currentDateTime.isAfter(eventFinDateTime)) {
            countdownLabel.setText("Event has ended.");

        } else {
            countdownLabel.setText("Event is happening now.");

        }
    }

    private void updateLabelWithCountdown(Label countdownLabel, long seconds) {
        long days = seconds / (24 * 60 * 60);
        long hours = (seconds % (24 * 60 * 60)) / 3600;
        long minutes = ((seconds % (24 * 60 * 60)) % 3600) / 60;
        seconds = ((seconds % (24 * 60 * 60)) % 3600) % 60;

        String countdownText = String.format("Time left: %02d days %02d:%02d:%02d", days, hours, minutes, seconds);
        countdownLabel.setText(countdownText);
    }



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
        double scrollSpeed = 0.01;

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
            List<Evenement> filteredEvents = evenementCrud.filterEventsByEventType(selectedEventType);
            System.out.println("selectedEventType"+filteredEvents);
            displayEvents(filteredEvents);
        } else if (selectedCity != null) {
            List<Evenement> filteredEvents = evenementCrud.filterEventsByCity(selectedCity);
            System.out.println("selectedCity"+filteredEvents);
            displayEvents(filteredEvents);
        } else {
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
        typeFilter.getSelectionModel().clearSelection();
        cityFilter.getSelectionModel().clearSelection();
        loadEvents();
    }
    private void showEventDetails(Evenement selectedEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventINFO.fxml"));
            Parent root = loader.load();
            EventINFOController eventINFOController = loader.getController();
            eventINFOController.setEventDetails(selectedEvent);
            Stage stage = (Stage) eventFlowPane.getScene().getWindow();
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

    private void redirectToReservationForm(Evenement selectedEvent) {

        if (selectedEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FormReserverEvent.fxml"));
                Parent root = loader.load();
                FormReserverEventController formController = loader.getController();
                formController.setEvent(selectedEvent);
                Stage currentStage = (Stage) clearFiltersButton.getScene().getWindow();
                currentStage.setScene(new Scene(root));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Event null.");
        }
    }




}
