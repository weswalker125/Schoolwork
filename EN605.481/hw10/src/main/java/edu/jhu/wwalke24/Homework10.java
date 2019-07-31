package edu.jhu.wwalke24;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Homework10 extends Application {
	/**
	 * Application initialize function for the JavaFX program.
	 * Load the FXML file which describes the graphical user interface,
	 * and display to screen.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		URL location = getClass().getResource("/gui.fxml");
		FXMLLoader loader = new FXMLLoader(location);
		Parent root = loader.load();
		Scene scene = new Scene(root, 749, 400);
		
		primaryStage.setTitle("HW10 - Walker");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
