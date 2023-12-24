package OOP.FE.controller;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController{
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void switchToMainScreen(ActionEvent event) throws IOException{
		Parent root = FXMLLoader.load(getClass().getResource("D:/.vs/Java/NFT Project/nft-test-other-folder/src/main/resources/OOP/screen/MainScreen.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToPost(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("D:/.vs/Java/NFT Project/nft-test-other-folder/src/main/resources/OOP/screen/Tweet.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}