package OOP.FE.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ListController implements Initializable {
    // Text field to get input from keyboard to search
    @FXML
    private TextField searchField;

    // List will appear when have an input
    @FXML
    private ListView<String> resultList;

    // Hyperlink for other in Mainscreen
    @FXML
    private Hyperlink twitterLink;

    @FXML
    private Hyperlink blogLink;

    // FlowPane for Tagging systems
    @FXML
    private FlowPane flowPane;

    @FXML
    private VBox searchContainer;
    // Keywords in search bar
    final private ObservableList<String> keywords = FXCollections.observableArrayList(
            "Blockchain", "NFT", "Twitter", "Blog");

    final private Popup popup = new Popup();

    // Handling search
    @FXML
    private void search(KeyEvent event) {
        String searchText = searchField.getText().toLowerCase();

        if (searchText.isEmpty()) {
            popup.hide();
        } else {
            ObservableList<String> filteredKeywords = keywords.filtered(keyword ->
                    keyword.toLowerCase().contains(searchText));
            resultList.setItems(filteredKeywords);

            if (!filteredKeywords.isEmpty()) {
                showPopup();
            } else {
                popup.hide();
            }
        }
    }

    // Pop up when search
    private void showPopup() {
        Bounds bounds = searchField.localToScreen(searchField.getBoundsInLocal());
        popup.getContent().clear();
        popup.getContent().add(resultList);
        popup.show(searchField.getScene().getWindow(), bounds.getMinX(), bounds.getMaxY());
    }

    // Go to NFT when click NFT Button
    @FXML
    private void goToBlog(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/OOP/screen/Blog.fxml")); // Khong co link fxml nen dung tam
        Scene scene = new Scene(loader.load(), 800, 700); // Adjust the size as needed
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    // Tagging Systems
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Disable the search field by default
        searchField.setDisable(true);

        // Enable the search field and set focus on mouse click
        searchContainer.setOnMouseClicked(event -> {
            if (searchField.isDisabled()) {
                searchField.setDisable(false);
                searchField.requestFocus();
            }
        });
        // Disable the search field when it loses focus, but not when clicking inside it
        searchField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                searchField.setDisable(true);
            }
        });

        // Add an event filter to the scene to detect clicks outside the search box
        if (searchField.getScene() != null) {
            searchField.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                Node source = (Node) event.getSource();
                Bounds bounds = searchContainer.localToScreen(searchContainer.getBoundsInLocal());

                if (source != searchField && !bounds.contains(event.getScreenX(), event.getScreenY())) {
                    // Click occurred outside the search box or its parent container
                    if (!searchField.isFocused()) {
                        searchField.setDisable(true);
                    }
                }
            });
        }

        for (Node node : flowPane.getChildren()) {
            if (node instanceof StackPane && node.getStyleClass().contains("tag")) {
                StackPane tagPane = (StackPane) node;
                tagPane.setOnMouseClicked(createTagClickHandler(tagPane));
            }
        }
    }

    // Handle when the tag is clicked
    private EventHandler<MouseEvent> createTagClickHandler(StackPane tagPane) {
        return event -> {
            // Extract tag text from the clicked tag
            Text textNode = (Text) tagPane.getChildren().get(1);
            String tagText = textNode.getText();

            // Check if the tag is already present in the searchField
            if (!isTagPresent(tagText, searchField)) {
                // Add the tag to the searchField
                addTag(tagText);
            }

            // Remove the clicked tag from the FlowPane
            flowPane.getChildren().remove(tagPane);
        };
    }

    // Check if the tag is in the search box to avoid duplication
    private boolean isTagPresent(String tagText, TextField searchField) {
        // Check if the tag is already present in the searchField
        String[] existingTags = searchField.getText().split(", ");
        for (String existingTag : existingTags) {
            if (existingTag.equals(tagText)) {
                return true;
            }
        }
        return false;
    }

    // Add new tag to the search bar
    private void addTag(String tagText) {
        // Add the tag to the searchField
        String currentText = searchField.getText().trim();
        if (!currentText.isEmpty()) {
            // If the searchField is not empty, append a comma and a space before adding the new tag
            searchField.setText(currentText + ", " + tagText);
        } else {
            // If the searchField is empty, add the new tag directly
            searchField.setText(tagText);
        }
        // Enable the search field after clicking a tag
        searchField.setDisable(false);
    }


}

