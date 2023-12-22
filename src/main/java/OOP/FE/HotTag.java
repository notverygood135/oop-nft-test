package OOP.FE;

import OOP.BE.datascraping.dataloader.EntitiesGenerator;
import OOP.BE.datascraping.model.Entity;
import OOP.BE.datascraping.utils.BlogTagFrequency;
import OOP.FE.controller.TagFrequencyController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class HotTag extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage)  {
        // Load FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/OOP/screen/tagfrequencytable.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // Get the controller
        TagFrequencyController controller = loader.getController();
        Map<String, Collection<Entity>> data1 =   new EntitiesGenerator().generate();
        Collection<Entity> blogs =  data1.get("Blog");
        Collection<Entity> twits =  data1.get("Twitter");
        Collection<Entity> combinedCollection = new ArrayList<>();
        combinedCollection.addAll(blogs);
        combinedCollection.addAll(twits);

        Map<String, Integer> tagFrequencyMap = BlogTagFrequency.calculateTagFrequency(combinedCollection);
        controller.setData(tagFrequencyMap);

        Scene scene = new Scene(root, 1560, 800);
        stage.setTitle("Hot Tag Screen");
        stage.setScene(scene);
        stage.show();
    }
}
