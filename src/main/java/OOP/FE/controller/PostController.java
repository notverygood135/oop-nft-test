package OOP.FE.controller;

import OOP.BE.datascraping.dataloader.EntitiesGenerator;
import OOP.BE.datascraping.model.Entity;
import OOP.BE.datascraping.model.twitter.Twitter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import java.util.*;

public class PostController implements Initializable {
    @FXML
    private TextField searchField;

    @FXML
    private ListView<Twitter> postListView;

    @FXML
    private FlowPane flowPane;

    @FXML
    private VBox searchContainer;

    private final Popup popup = new Popup();

    private List<Twitter> posts;

    private Set<String> currentTags = new HashSet<>();
    
    private ObservableList<Twitter> twitterPosts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchField.setDisable(true);

        searchContainer.setOnMouseClicked(event -> {
            if (searchField.isDisabled()) {
                searchField.setDisable(false);
                searchField.requestFocus();
            }
        });

        searchField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                searchField.setDisable(true);
            }
        });

        if (searchField.getScene() != null) {
            searchField.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                Node source = (Node) event.getSource();
                Bounds bounds = searchContainer.localToScreen(searchContainer.getBoundsInLocal());

                if (source != searchField && !bounds.contains(event.getScreenX(), event.getScreenY())) {
                    if (!searchField.isFocused()) {
                        searchField.setDisable(true);
                    }
                }
            });
        }
        
        Map<String, Collection<Entity>> data =   new EntitiesGenerator().generate();
        Collection<Entity> twit = data.get("Twitter");
        // thay key NFTCollection bang Twitter hoac Blog de lay du lieu tuong ung
        for(Entity e: twit){
            postListView.getItems().add((Twitter) e);
        }

//        loadPostsAndTags(); // this is commented due to entitiesgenerator applied
        setupTaggingSystems();
    }

//    private void loadPostsAndTags() {
//        posts = loadPostsFromJson("/OOP/data/twitter.json");
//
//        if (posts == null) {
//            posts = loadPostsFromJson("OOP/data/twitter.json");
//        }
//
//        if (posts != null) {
//            displayPopularTags();
//            displayPosts();
//        } else {
//            System.out.println("Posts are null or empty.");
//        }
//    }

    private List<Twitter> loadPostsFromJson(String jsonFilePath) {
        try {
            URL url = getClass().getResource(jsonFilePath);
            if (url == null) {
                System.err.println("File does not exist: " + jsonFilePath);
                return null;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(url, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void displayPopularTags() {
        Map<String, Integer> tagFrequency = new HashMap<>();
        for (Twitter post : posts) {
            for (String hashtag : post.getHashtags()) {
                tagFrequency.put(hashtag, tagFrequency.getOrDefault(hashtag, 0) + 1);
            }
        }

        List<String> popularTags = tagFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

        for (String tag : popularTags) {
            addTagToFlowPane(tag);
        }

        currentTags.addAll(popularTags); // Initialize current tags with popular tags
    }

    private void displayPosts() {
        ObservableList<Twitter> postContents = FXCollections.observableArrayList();
        for (Twitter post : posts) {
            postContents.add(post);
        }
        postListView.setItems(postContents);
    }

    private void addTagToFlowPane(String tagText) {
        StackPane tagPane = new StackPane();
        tagPane.getStyleClass().add("tag");

        Text textNode = new Text(tagText);
        tagPane.getChildren().add(textNode);

        tagPane.setOnMouseClicked(createTagClickHandler(tagPane));

        flowPane.getChildren().add(tagPane);
    }


    private void setupTaggingSystems() {
        for (Node node : flowPane.getChildren()) {
            if (node instanceof StackPane && node.getStyleClass().contains("tag")) {
                StackPane tagPane = (StackPane) node;
                tagPane.setOnMouseClicked(createTagClickHandler(tagPane));
            }
        }
    }

    private EventHandler<MouseEvent> createTagClickHandler(StackPane tagPane) {
        return event -> {
            Text textNode = (Text) tagPane.getChildren().get(0);
            String tagText = textNode.getText();

            // Update current tags and refresh the FlowPane
            currentTags.remove(tagText);
            displayPopularTags();
            setupTaggingSystems();

            if (!isTagPresent(tagText, searchField)) {
                addTag(tagText);
            }

            flowPane.getChildren().remove(tagPane);
        };
    }

    private boolean isTagPresent(String tagText, TextField searchField) {
        String[] existingTags = searchField.getText().split(", ");
        for (String existingTag : existingTags) {
            if (existingTag.equals(tagText)) {
                return true;
            }
        }
        return false;
    }

    private void addTag(String tagText) {
        String currentText = searchField.getText().trim();
        if (!currentText.isEmpty()) {
            searchField.setText(currentText + ", " + tagText);
        } else {
            searchField.setText(tagText);
        }
        searchField.setDisable(false);
    }

    @FXML
    private void search(KeyEvent event) {
        String searchText = searchField.getText().toLowerCase();

        if (searchText.isEmpty()) {
            popup.hide();
            displayPosts(); // Display all posts when the search bar is empty
        } else {
            List<Twitter> filteredPosts = posts.stream()
                    .filter(post -> postContainsKeywordOrTag(post, searchText))
                    .toList();

            ObservableList<Twitter> postContents = FXCollections.observableArrayList();
            for (Twitter post : filteredPosts) {
                postContents.add(post);
            }
            postListView.setItems(postContents);

            if (!filteredPosts.isEmpty()) {
                showPopup();
            } else {
                popup.hide();
            }
        }
    }

    private boolean postContainsKeywordOrTag(Twitter post, String searchText) {
        String lowerCaseContent = post.getContent().toLowerCase();
        List<String> hashtags = post.getHashtags().stream()
                .map(String::toLowerCase)
                .toList();

        return lowerCaseContent.contains(searchText) || hashtags.contains(searchText);
    }

    private void showPopup() {
        Bounds bounds = searchField.localToScreen(searchField.getBoundsInLocal());
        popup.getContent().clear();
        popup.show(searchField.getScene().getWindow(), bounds.getMinX(), bounds.getMaxY());
    }

    @FXML
    private void goToBlog(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/OOP/screen/Blog.fxml"));
        Scene scene = new Scene(loader.load(), 731, 657);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    
    @FXML
    public void switchToMainScreen(ActionEvent event) throws IOException{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/OOP/screen/MainScreen.fxml"));
    	Scene scene = new Scene(loader.load(), 731, 657);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
	}

    @FXML
    public void switchToBlog(ActionEvent event) throws IOException{
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/OOP/screen/Blog.fxml"));
    	Scene scene = new Scene(loader.load(), 731, 657);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
	}
}
