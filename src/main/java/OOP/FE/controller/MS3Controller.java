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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

	public class MS3Controller implements Initializable {
	private Scene scene;
	private Stage stage;
	private Parent root;

	@FXML
	public void priceClicked(ActionEvent e){
		System.out.println("Price");
	}

	public void switchToScene1(ActionEvent e)throws IOException {
		root = FXMLLoader.load(getClass().getResource("/OOP/screen/MainScene1.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}

	public void switchToScene2(ActionEvent e) throws IOException{
		root = FXMLLoader.load(getClass().getResource("/OOP/screen/MainScene2.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void switchToScene3(ActionEvent e) throws IOException{
		root = FXMLLoader.load(getClass().getResource("/OOP/screen/MainScene3.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	@FXML
	private TableView<NFTCollection> tableView;

	@FXML
	private TableColumn<NFTCollection, Double> TblVwColChange;

	@FXML
	private TableColumn<NFTCollection, String> TblVwColCollection;

	@FXML
	private TableColumn<NFTCollection, Double> TblVwColFloorPrice;

	@FXML
	private TableColumn<NFTCollection, Integer> TblVwColSale;

//    @FXML
//    private TableColumn<NFTCollection, String> TblVwColType;

	@FXML
	private TableColumn<NFTCollection, Double> TblVwColVolume;

	@Override
	public void initialize(URL location, ResourceBundle rb) {
		ObservableList<NFTCollection> list = FXCollections.observableArrayList(
//			new NFTCollection("0x6303a52ddd38325f7f5eb953b40624172492e2ba", "Heavenly Bodies", "https://res.cloudinary.com/nifty-gateway/video/upload/v1616539847/ADaniel/JasonEbeyer/Drop3.30/Edition/Heavenly_Bodies_JasonEbeyer_4K_-_Jason_Ebeyer_u0mqzj.mp4", 122.24908985552602, 0.0,0.0, 0, 1, 1),
//			new NFTCollection("0xf8690324728c77d80985657d555f70607aae7633", "Pixellitist Resurrects", "", 0.0, 0.0, 0.0, 0, 1, 1),
//		new NFTCollection("0xb26ebf2beab3071bf0fa28ca829d3e5492fa12fa", "Adventures Of Tako", "", 0.0, 0.0, 0.0, 1,23,4)
						);

		Map<String, Collection<Entity>> data = new EntitiesGenerator().generate();
		Collection<Entity> twit = data.get("NFTCollection");

		for(Entity e: twit){
			list.add((NFTCollection) e);
			e.printDetail();
		}

		initializeTableView();
//		System.out.println();
		tableView.setItems(list);
	}

	private void initializeTableView() {
		TblVwColCollection.setCellValueFactory(new PropertyValueFactory<>("name"));
		TblVwColVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
		TblVwColFloorPrice.setCellValueFactory(new PropertyValueFactory<>("floorPrice"));
		TblVwColSale.setCellValueFactory(new PropertyValueFactory<>("numOfSales"));
		TblVwColChange.setCellValueFactory(new PropertyValueFactory<>("volumeChange"));
	}
}
