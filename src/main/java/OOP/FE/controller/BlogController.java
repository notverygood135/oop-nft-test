package OOP.FE.controller;

import OOP.BE.datascraping.dataloader.EntitiesGenerator;
import OOP.BE.datascraping.model.Entity;
import OOP.BE.datascraping.model.blog.Blog;

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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class BlogController implements Initializable {
    @FXML
    private TextField searchField;

    @FXML
    private ListView<Blog> blogListView;

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
    private MenuItem ascended;

    @FXML
    private MenuItem descended;

    @FXML
    private MenuItem earliest;

    @FXML
    private MenuItem latest;

    @FXML
    private List<Blog> blogs;

    private Set<String> currentTags = new HashSet<>();

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

        Map<String, Collection<Entity>> data = new EntitiesGenerator().generate();
        Collection<Entity> blogs = data.get("Blog");
        blogs = new ArrayList<>(); // Initialize the blogs list

        for(Entity e: blogs){
            System.out.println(e);
            blogListView.getItems().add((Blog) e);
            blogs.add((Blog) e); // Add blogs to the list
        }

        blogListView.setCellFactory(param -> new ListCell<>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(Blog blog, boolean empty) {
                super.updateItem(blog, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Image image = new Image(blog.getImg(), 100, 100, false, false);
                    imageView.setImage(image);
                    setText(blog.toString());
                    setGraphic(imageView);
                }
            }
        });

        setupTaggingSystems();
        initializeSorting();
    }

    private void displayPopularTags() {
        Map<String, Integer> tagFrequency = new HashMap<>();
        for (Blog blog : blogs) {
            for (String hashtag : blog.getTag()) {
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

    private void displayBlogs() {
        ObservableList<Blog> blogContents = FXCollections.observableArrayList();
        blogContents.addAll(blogs);
        blogListView.setItems(blogContents);
        System.out.println(blogListView);
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
        ascended.setOnAction(event -> sortBlogs(Comparator.comparing(Blog::getTitle)));
        descended.setOnAction(event -> sortBlogs(Comparator.comparing(Blog::getTitle).reversed()));
        earliest.setOnAction(event -> sortBlogs(Comparator.comparing(Blog::getDate)));
        latest.setOnAction(event -> sortBlogs(Comparator.comparing(Blog::getDate).reversed()));
    }

    // Sort blogs
    private void sortBlogs(Comparator<Blog> comparator) {
        ObservableList<Blog> blogContents = FXCollections.observableArrayList(blogListView.getItems());
        blogContents.sort(comparator);
        blogListView.setItems(blogContents);
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



//    private void filteringBlogs() {
//    	
//    	filterDropDownMenu.getItems().addAll(thisWeek, last30Days, last6Months, last12Months);
//    	
//    	ObservableList<Blog> blogContents = FXCollections.observableArrayList();
//    	blogListView.setItems(blogContents);
//    	for (Blog blog : blogListView.getItems()) {
//			blogContents.add(blog);
//		}
//    	FilteredList<Blog> filteredContents = new FilteredList<Blog>(blogContents);
//    	
//    	//Last 7 days
//    	thisWeek.setOnAction(event -> {
//    		//Set the predicate to filter the last 30 Days
//    		Predicate<Blog> thisWeekPredicate = item ->
//    			LocalDate.parse(item.getDate()).isAfter(LocalDate.now().minusDays(7));
//    		filteredContents.setPredicate(thisWeekPredicate);
//    		
//    	
//    	});
//    	
//    	//Last 30 days
//    	last30Days.setOnAction(event -> {
//    		//Set the predicate to filter the last 30 Days
//    		Predicate<Blog> lastMonthPredicate = item ->
//    			LocalDate.parse(item.getDate()).isAfter(LocalDate.now().minusDays(30));
//    		filteredContents.setPredicate(lastMonthPredicate);
//    	});
//    	
//    	//Last 6 Months
//    	last6Months.setOnAction(event -> {
//    		//Set the predicate to filter the last 30 Days
//    		Predicate<Blog> last6MonthsPredicate = item ->
//    			LocalDate.parse(item.getDate()).isAfter(LocalDate.now().minusMonths(6));
//    		filteredContents.setPredicate(last6MonthsPredicate);
//    	});
//    	
//    	//Last 12 months
//    	last12Months.setOnAction(event -> {
//    		//Set the predicate to filter the last 30 Days
//    		Predicate<Blog> lastYearPredicate = item ->
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
            displayBlogs(); // Display all blogs when the search bar is empty
        } else {
            List<Blog> filteredBlogs = blogs.stream()
                    .filter(blog -> blogContainsKeywordOrTag(blog, searchText))
                    .toList();

            ObservableList<Blog> blogContents = FXCollections.observableArrayList();
            blogContents.addAll(filteredBlogs);
            blogListView.setItems(blogContents);

            if (!filteredBlogs.isEmpty()) {
                showPopup();
            } else {
                popup.hide();
            }
        }
    }

    private boolean blogContainsKeywordOrTag(Blog blog, String searchText) {
        String lowerCaseContent = blog.getContent().toLowerCase();
        List<String> hashtags = blog.getTag().stream()
                .map(String::toLowerCase)
                .toList();

        // Check if the search text matches the content or any tag
        return lowerCaseContent.contains(searchText) || hashtags.contains(searchText) || containsAllTags(hashtags, searchText);
    }

    // Filter blogs with Tags
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
