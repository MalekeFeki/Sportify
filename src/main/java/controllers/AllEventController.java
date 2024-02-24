package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import services.EvenementCrud;
import entities.Evenement;

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

    private static final int EVENTS_PER_PAGE = 1; // Set the number of events per page

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        pagination.setPageFactory(pageIndex -> createPage(pageIndex));
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
        Text eventDescriptionText = new Text(event.getDescrptionEv());
        eventDescriptionText.setWrappingWidth(200);

        Button moreInfoButton = new Button("More Info");

        ImageView imageView = new ImageView(new Image("file:" + event.getPhoto()));
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        VBox eventBox = new VBox(
                new VBox(eventNameText, eventDescriptionText, moreInfoButton),
                imageView
        );
        eventBox.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-spacing: 5px;");
        eventBox.setMinWidth(150);

        return eventBox;
    }

    private void configureScrollPane() {
        scrollPane.setOnScroll(this::handleScroll);
    }
    @FXML
    private ScrollPane scrollPane;
    private void handleScroll(ScrollEvent event) {
        double deltaY = event.getDeltaY();
        double scrollSpeed = 0.005; // You can adjust the scroll speed

        scrollPane.setVvalue(scrollPane.getVvalue() - deltaY * scrollSpeed);
    }

    private void configurePagination() {
        pagination.setPageFactory(pageIndex -> createPage(pageIndex));
    }
}
