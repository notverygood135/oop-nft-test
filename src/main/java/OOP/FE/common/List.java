package OOP.FE.common;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class List extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Blog.class.getResource("Blog.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 730, 700);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}