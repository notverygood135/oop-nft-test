package OOP.FE.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Map;
import java.util.stream.Collectors;

public class TagFrequencyController {

    @FXML
    private TextField filterTextField;

    @FXML
    private TableView<TagFrequency> tableView;

    @FXML
    private TableColumn<TagFrequency, Integer> indexColumn;

    @FXML
    private TableColumn<TagFrequency, String> tagNameColumn;

    @FXML
    private TableColumn<TagFrequency, Integer> frequencyColumn;

    private ObservableList<TagFrequency> data;

    public void initialize() {
        // Initialize the TableColumn instances
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
        tagNameColumn.setCellValueFactory(new PropertyValueFactory<>("tagName"));
        frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        // Add the TableColumn instances to the TableView
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
    }

    @FXML
    private void handleFilterButtonClick() {
        String filterText = filterTextField.getText().trim();

        if (!filterText.isEmpty()) {
            int filterValue = Integer.parseInt(filterText);
            ObservableList<TagFrequency> filteredData = FXCollections.observableArrayList(
                    data.stream().limit(filterValue).collect(Collectors.toList())
            );
            tableView.setItems(filteredData);
        } else {
            tableView.setItems(data);
        }
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

    public void setData(Map<String, Integer> tagFrequencyMap) {
        data = FXCollections.observableArrayList();
        int index = 1;
        for (Map.Entry<String, Integer> entry : tagFrequencyMap.entrySet()) {
            data.add(new TagFrequency(index++, entry.getKey(), entry.getValue()));
        }
        tableView.setItems(data);
    }
}