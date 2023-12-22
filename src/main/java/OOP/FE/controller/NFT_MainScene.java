package OOP.FE.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import OOP.BE.datascraping.dataloader.EntitiesGenerator;
import OOP.BE.datascraping.dataloader.NFTGenerator;
import OOP.BE.datascraping.model.Entity;
import OOP.BE.datascraping.model.nftcollection.NFTCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class NFT_MainScene implements Initializable {

    public void switchToBlogScene(ActionEvent e) throws IOException{
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resources/OOP/screen/MainScene.fxml")));
//        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
//        Scene scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/OOP/screen/MainScreen.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private TableView<NFTCollection> tableView;
    @FXML
    private TableColumn<NFTCollection, String> TblVwColCollection;
    @FXML
    private TableColumn<NFTCollection, Double> TblVwColVolume;
    @FXML
    private TableColumn<NFTCollection, Double> TblVwColFloorPrice;
    @FXML
    private TableColumn<NFTCollection, Integer> TblVwColSale;
    @FXML
    private TableColumn<NFTCollection, Double> TblVwColChange;
    @FXML
    private TextField searchText;
    @FXML
    private Text dayStatus;
    @FXML
    private Text webStatus;
    @FXML
    private TextField fromPrice;
    @FXML
    private TextField toPrice;

    ObservableList<NFTCollection> list = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle rb) {
        Map<String, Collection<Entity>> data = new NFTGenerator(0).generate();
        Collection<Entity> twit = data.get("NFTCollection");
        for(Entity e: twit){
            list.add((NFTCollection) e);
        }
        initializeTableView();
        setSearchText();
    }
    private void initializeTableView() {
        TblVwColCollection.setCellValueFactory(new PropertyValueFactory<NFTCollection, String>("name"));
        TblVwColVolume.setCellValueFactory(new PropertyValueFactory<NFTCollection, Double>("volume"));
        TblVwColFloorPrice.setCellValueFactory(new PropertyValueFactory<NFTCollection, Double>("floorPrice"));
        TblVwColSale.setCellValueFactory(new PropertyValueFactory<NFTCollection, Integer>("numOfSales"));
        TblVwColChange.setCellValueFactory(new PropertyValueFactory<NFTCollection, Double>("volumeChange"));
    }
    private void updateTableDays(int days) {
        list.clear();
        Map<String, Collection<Entity>> data = new NFTGenerator(days).generate();
        Collection<Entity> twit = data.get("NFTCollection");
        for (Entity x : twit) {
            list.add((NFTCollection) x);
        }
        tableView.setItems(list);
        setSearchText();
    }
    @FXML
    private void setTable1day(ActionEvent e) {
        updateTableDays(1);
        dayStatus.setText("1 day");
        webStatus.setText("All");
    }
    @FXML
    private void setTable7day(ActionEvent e) {
        updateTableDays(7);
        dayStatus.setText("7 day");
        webStatus.setText("All");
    }
    @FXML
    private void setTableAllDay(ActionEvent e) {
        updateTableDays(0);
        dayStatus.setText("All");
        webStatus.setText("All");
    }
    private void updateTableWeb(String web) {
        list.clear();
        Map<String, Collection<Entity>> data = new NFTGenerator(web).generate();
        Collection<Entity> twit = data.get("NFTCollection");
        for (Entity x : twit) {
            list.add((NFTCollection) x);
        }
        tableView.setItems(list);
        setSearchText();
    }
    @FXML
    private void setTableBinance(ActionEvent e) {
        updateTableWeb("binance");
        dayStatus.setText("All");
        webStatus.setText("Binance");
    }
    @FXML
    private void setTableOpensea(ActionEvent e) {
        updateTableWeb("opensea");
        dayStatus.setText("All");
        webStatus.setText("Opensea");
    }
    @FXML
    private void setTableRarible(ActionEvent e) {
        updateTableWeb("rarible");
        dayStatus.setText("All");
        webStatus.setText("Rarible");
    }
    @FXML
    private void setTableNiftygateway(ActionEvent e) {
        updateTableWeb("niftygateway");
        dayStatus.setText("All");
        webStatus.setText("Niftygateway");
    }

    void setSearchText() {
        FilteredList<NFTCollection> filteredList = new FilteredList<>(list, b -> true);
        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(nftCollection -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                return nftCollection.getName().toLowerCase().contains(searchKeyword);
            });
        });
        SortedList<NFTCollection> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
        setPriceSearchText(sortedList);
    }

    void setPriceSearchText(ObservableList<NFTCollection> list) {
        FilteredList<NFTCollection> priceFilteredList = new FilteredList<>(list, b -> true);

        // Listener for 'fromPrice' TextField
        fromPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            priceFilteredList.setPredicate(nftCollection -> {
                double from = parseDoubleOrDefault(fromPrice.getText(), Double.NEGATIVE_INFINITY);
                double to = parseDoubleOrDefault(toPrice.getText(), Double.POSITIVE_INFINITY);

                return nftCollection.getFloorPrice() >= from && nftCollection.getFloorPrice() <= to;
            });
        });

        // Listener for 'toPrice' TextField
        toPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            priceFilteredList.setPredicate(nftCollection -> {
                double from = parseDoubleOrDefault(fromPrice.getText(), Double.NEGATIVE_INFINITY);
                double to = parseDoubleOrDefault(toPrice.getText(), Double.POSITIVE_INFINITY);

                return nftCollection.getFloorPrice() >= from && nftCollection.getFloorPrice() <= to;
            });
        });

        // Create a sorted list
        SortedList<NFTCollection> sortedList = new SortedList<>(priceFilteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            if (value.isEmpty() || value.isBlank()) {
                return defaultValue;
            }
            return Double.parseDouble(value);
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }

    public void changeDetailScene(ActionEvent e) throws IOException {
        NFTCollection selectedItem = tableView.getSelectionModel().getSelectedItem();

        if (selectedItem == null || selectedItem.equals(null)) {
            // Hiển thị thông báo cho người dùng về việc chưa chọn mục
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một mục!");
            alert.showAndWait();
        } else {
            // Mở cửa sổ chi tiết
            Stage detailStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OOP/screen/NFTDetailScene.fxml"));
            Parent root = loader.load();

            NFT_ViewDetailController controller = loader.getController();
            controller.setDetChange(selectedItem);

            Scene scene = new Scene(root);
            detailStage.setScene(scene);
            detailStage.show();
        }
    }
}
