package it.polito.tdp.metrodeparis;

import it.polito.tdp.metrodeparis.model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MetroDeParis.fxml"));
			BorderPane root = (BorderPane) loader.load();
			MetroDeParisController controller = loader.getController();

			// TODO: Set the model
			Model model = new Model();
			
			controller.setModel(model);

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
