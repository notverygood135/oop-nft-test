package OOP.FE;

import OOP.BE.datascraping.dataloader.EntitiesGenerator;
import OOP.BE.datascraping.model.Entity;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static OOP.BE.datascraping.utils.BlogTagFrequency.calculateTagFrequency;

public class TagFrequencyTableView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Assume you have a Map<String, Integer> tagFrequencyMap from calculateTagFrequency
        Map<String, Collection<Entity>> data1 =   new EntitiesGenerator().generate();
        Collection<Entity> blogs =  data1.get("Blog");
        Collection<Entity> twits =  data1.get("Twitter");
        Collection<Entity> combinedCollection = new ArrayList<>();
        combinedCollection.addAll(blogs);
        combinedCollection.addAll(twits);

        // thay key NFTCollection bang Twitter hoac Blog de lay du lieu tuong ung
        Map<String, Integer> tagFrequencyMap = calculateTagFrequency(combinedCollection);
       // Map<String, Integer> tagFrequencyMap1 = calculateTagFrequency(twits);


        TableView<TagFrequency> tableView = new TableView<>();
        TableColumn<TagFrequency, Integer> indexColumn = new TableColumn<>("STT");
        TableColumn<TagFrequency, String> tagNameColumn = new TableColumn<>("Tag Name");
        TableColumn<TagFrequency, Integer> frequencyColumn = new TableColumn<>("Frequency");

        // Set up the cell value factories
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        tagNameColumn.setCellValueFactory(new PropertyValueFactory<>("tagName"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));

        // Populate data into the table
        ObservableList<TagFrequency> data = FXCollections.observableArrayList();
        int index=0;
        for (Map.Entry<String, Integer> entry : tagFrequencyMap.entrySet()) {
            data.add(new TagFrequency(++index,entry.getKey(), entry.getValue()));
        }

        tableView.setItems(data);
        tableView.getColumns().addAll(indexColumn,tagNameColumn, frequencyColumn);

        // Set the preferred width for each column
        double totalWidth = 1560;  // Total width of the table
        double indexWidth = 100;   // Width for the index column
        double tagNameWidth = (totalWidth - indexWidth) / 2;  // Equal width for the other two columns
        double frequencyWidth = (totalWidth - indexWidth) / 2;

        indexColumn.setPrefWidth(indexWidth);
        tagNameColumn.setPrefWidth(tagNameWidth);
        frequencyColumn.setPrefWidth(frequencyWidth);

        // Remove the empty column on the right
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Scene scene = new Scene(tableView, totalWidth, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tag Frequency Table");
        primaryStage.show();
    }
    // Model class for TableView
    public static class TagFrequency {
        private final int index;
        private final String tagName;
        private final Integer frequency;

        public TagFrequency(int index, String tagName, Integer frequency) {
            this.index = index;
            this.tagName = tagName;
            this.frequency = frequency;
        }

        public int getIndex() {
            return index;
        }

        public String getTagName() {
            return tagName;
        }

        public Integer getFrequency() {
            return frequency;
        }
    }
}

