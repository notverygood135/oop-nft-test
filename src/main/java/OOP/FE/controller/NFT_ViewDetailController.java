package OOP.FE.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

import OOP.BE.datascraping.dataloader.EntitiesGenerator;
import OOP.BE.datascraping.model.Entity;
import OOP.BE.datascraping.model.nftcollection.NFTCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;

public class NFT_ViewDetailController {

    @FXML
    private ImageView detImg;

    @FXML
    private Label detChange;

    @FXML
    private Label detFloorPrice;

    @FXML
    private Label detName;

    @FXML
    private Label detSale;

    @FXML
    private Label detSupply;

    @FXML
    private Label detVolume;

    public void setDetChange(NFTCollection collection){
        detName.setText(String.valueOf(collection.getName()));
        detFloorPrice.setText(String.valueOf(collection.getFloorPrice()));
        detVolume.setText(String.valueOf(collection.getVolume()));
        detSale.setText(String.valueOf(collection.getNumOfSales()));
        detChange.setText(String.valueOf(collection.getVolumeChange()));
        detSupply.setText(String.valueOf(collection.getTotalSupply()));

        String imageUrl = collection.getUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            detImg.setImage(image);
        } else {
             detImg.setImage(null); // Để ImageView trống
        }
    }
}