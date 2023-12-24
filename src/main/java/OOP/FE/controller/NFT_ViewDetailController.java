package OOP.FE.controller;

import OOP.BE.datascraping.dataloader.BlogGenerator;
import OOP.BE.datascraping.model.Entity;
import OOP.BE.datascraping.model.blog.Blog;
import OOP.BE.datascraping.model.nftcollection.NFTCollection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.*;

public class NFT_ViewDetailController implements Initializable {

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

    @FXML
    private ListView<Blog> blogListView = new ListView<>(FXCollections.observableArrayList());

    @FXML
    private Label blogCount;

    private List<Blog> blogList = new ArrayList<>();

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

        String collectionName = collection.getName();
        int count = 0;
        for (Blog b : blogList) {
            if (b.getTitle().contains(collectionName) || b.getContent().contains(collectionName)) {
                blogListView.getItems().add(b);
                count++;
            }
        }
        blogCount.setText(String.valueOf(count));

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
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        ObservableList<Blog> list = FXCollections.observableArrayList();
        Map<String, Collection<Entity>> data = new BlogGenerator().generate();
        Collection<Entity> blogs = data.get("Blog");
        for(Entity e: blogs){
            Blog blog = (Blog) e;
            blogList.add(blog);
        }
    }
}