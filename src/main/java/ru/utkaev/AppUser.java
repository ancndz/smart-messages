package ru.utkaev;

import java.nio.file.Path;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppUser extends Application {

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final Parent root = FXMLLoader.load(Path.of("main.fxml").toUri().toURL());
		primaryStage.setTitle("Messenger");
		primaryStage.setScene(new Scene(root, 640, 347));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(final String[] args) {
		launch(args);
		System.exit(0);
	}
}