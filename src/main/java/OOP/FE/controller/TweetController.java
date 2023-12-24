package OOP.FE.controller;

import OOP.BE.datascraping.dataloader.EntitiesGenerator;
import OOP.BE.datascraping.model.Entity;
import OOP.BE.datascraping.model.blog.Blog;
import OOP.BE.datascraping.model.twitter.Twitter;

import OOP.FE.common.Tweet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class TweetController implements Initializable {
    @FXML
    private TextField searchField;

    @FXML
    private ListView<Twitter> tweetListView;

    @FXML
    private FlowPane flowPane;

    @FXML
    private VBox searchContainer;

    private final Popup popup = new Popup();
    
    @FXML
    private SplitMenuButton filterDropDownMenu;
    
    @FXML
    private MenuItem thisWeek;
    
    @FXML
    private MenuItem last30Days;

    @FXML
    private MenuItem last6Months;
    
    @FXML
    private MenuItem last12Months;

    @FXML
    private SplitMenuButton sortDropDownMenu;

    @FXML
    private MenuItem earliest;

    @FXML
    private MenuItem latest;

    @FXML
    public TextField searchText;

    private ObservableList<Twitter> tweets = FXCollections.observableArrayList();
    private Set<String> currentTags = new HashSet<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Map<String, Collection<Entity>> data = new EntitiesGenerator().generate();
        Collection<Entity> twit = data.get("Twitter");

        for(Entity e: twit){
            tweetListView.getItems().add((Twitter) e);
            tweets.add((Twitter) e); // Add tweets to the list
        }

        setupTaggingSystems();
        initializeSorting();
        setSearchText();
    }

    private void displayPopularTags() {
        Map<String, Integer> tagFrequency = new HashMap<>();
        for (Twitter tweet : tweets) {
            for (String hashtag : tweet.getHashtags()) {
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

    void setSearchText() {
        FilteredList<Twitter> filteredList = new FilteredList<>(tweets, b -> true);
        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(tweet -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();

                List<String> hashtags = tweet.getHashtags();
                for (String hashtag : hashtags) {
                    if (hashtag.toLowerCase().contains(searchKeyword)) {
                        return true;
                    }
                }
                return false;
            });
        });
        tweetListView.setItems(filteredList);
    }

    private void displayTweets() {
        ObservableList<Twitter> tweetContents = FXCollections.observableArrayList();
        tweetContents.addAll(tweets);
        tweetListView.setItems(tweetContents);
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

    // Handle sort dropdown button
    @FXML
    private void initializeSorting() {
        earliest.setOnAction(event -> sortTweets(Comparator.comparing(Twitter::getDateTime)));
        latest.setOnAction(event -> sortTweets(Comparator.comparing(Twitter::getDateTime).reversed()));
    }

    // Sort tweets
    private void sortTweets(Comparator<Twitter> comparator) {
        ObservableList<Twitter> tweetContents = FXCollections.observableArrayList(tweetListView.getItems());
        tweetContents.sort(comparator);
        tweetListView.setItems(tweetContents);
    }

//    //Handle filter dropdown button pressed
//    @FXML
//    private void handleFilterMenuItemClick(ActionEvent event) {
//    	MenuItem menuItem = (MenuItem) event.getSource();
//    	filterDropDownMenu.setText(menuItem.getText());
//    }
    
    // set default filter value
//    @FXML
//    private void setDefaultFilterMenuItem() {
//    	// Set the default value 
//    	if (!filterDropDownMenu.getItems().isEmpty()) {
//    		MenuItem defaultFilterItem = filterDropDownMenu.getItems().get(1);
//    		filterDropDownMenu.setText(defaultFilterItem.getText());
//    	}
//    }
    
    
    
//    private void filteringTweets() {
//    	
//    	filterDropDownMenu.getItems().addAll(thisWeek, last30Days, last6Months, last12Months);
//    	
//    	ObservableList<Twitter> tweetContents = FXCollections.observableArrayList();
//    	tweetListView.setItems(tweetContents);
//    	for (Twitter tweet : tweetListView.getItems()) {
//			tweetContents.add(tweet);
//		}
//    	FilteredList<Twitter> filteredContents = new FilteredList<Twitter>(tweetContents);
//    	
//    	//Last 7 days
//    	thisWeek.setOnAction(event -> {
//    		//Set the predicate to filter the last 30 Days
//    		Predicate<Twitter> thisWeekPredicate = item ->
//    			LocalDate.parse(item.getDate()).isAfter(LocalDate.now().minusDays(7));
//    		filteredContents.setPredicate(thisWeekPredicate);
//    		
//    	
//    	});
//    	
//    	//Last 30 days
//    	last30Days.setOnAction(event -> {
//    		//Set the predicate to filter the last 30 Days
//    		Predicate<Twitter> lastMonthPredicate = item ->
//    			LocalDate.parse(item.getDate()).isAfter(LocalDate.now().minusDays(30));
//    		filteredContents.setPredicate(lastMonthPredicate);
//    	});
//    	
//    	//Last 6 Months
//    	last6Months.setOnAction(event -> {
//    		//Set the predicate to filter the last 30 Days
//    		Predicate<Twitter> last6MonthsPredicate = item ->
//    			LocalDate.parse(item.getDate()).isAfter(LocalDate.now().minusMonths(6));
//    		filteredContents.setPredicate(last6MonthsPredicate);
//    	});
//    	
//    	//Last 12 months
//    	last12Months.setOnAction(event -> {
//    		//Set the predicate to filter the last 30 Days
//    		Predicate<Twitter> lastYearPredicate = item ->
//    			LocalDate.parse(item.getDate()).isAfter(LocalDate.now().minusMonths(12));
//    		filteredContents.setPredicate(lastYearPredicate);
//    	});
//    	
//    }



    @FXML
    private void search(KeyEvent event) {
        String searchText = searchField.getText().toLowerCase();

        if (searchText.isEmpty()) {
            popup.hide();
            displayTweets(); // Display all tweets when the search bar is empty
        } else {
            List<Twitter> filteredTweets = tweets.stream()
                    .filter(tweet -> tweetContainsKeywordOrTag(tweet, searchText))
                    .toList();

            ObservableList<Twitter> tweetContents = FXCollections.observableArrayList();
            tweetContents.addAll(filteredTweets);
            tweetListView.setItems(tweetContents);

            if (!filteredTweets.isEmpty()) {
                showPopup();
            } else {
                popup.hide();
            }
        }
    }

    private boolean tweetContainsKeywordOrTag(Twitter tweet, String searchText) {
        String lowerCaseContent = tweet.getContent().toLowerCase();
        List<String> hashtags = tweet.getHashtags().stream()
                .map(String::toLowerCase)
                .toList();

        // Check if the search text matches the content or any tag
        return lowerCaseContent.contains(searchText) || hashtags.contains(searchText) || containsAllTags(hashtags, searchText);
    }

    // Filter tweets with Tags
    private boolean containsAllTags(List<String> tags, String searchText) {
        String[] searchTags = searchText.split(", ");
        return tags.containsAll(Arrays.asList(searchTags));
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
