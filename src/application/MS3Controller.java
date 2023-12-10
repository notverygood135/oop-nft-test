package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;



public class MS3Controller {
	private Scene scene;
	private Stage stage;
	private Parent root;
	
	@FXML
	public void priceClicked(ActionEvent e){
		System.out.println("Price");
	}
	
	public void switchToScene1(ActionEvent e)throws IOException {
		root = FXMLLoader.load(getClass().getResource("MainScene1.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}
	
	public void switchToScene2(ActionEvent e) throws IOException{
		root = FXMLLoader.load(getClass().getResource("MainScene2.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToScene3(ActionEvent e) throws IOException{
		root = FXMLLoader.load(getClass().getResource("MainScene3.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}
