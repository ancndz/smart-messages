package ru.utkaev;

import java.nio.file.Path;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.utkaev.entity.Miner;

public class AppMiner extends Application {

	@Override
	public void start(final Stage primaryStage) throws Exception {
		final Parent root = FXMLLoader.load(Path.of("miner.fxml").toUri().toURL());
		primaryStage.setTitle("Miner");
		primaryStage.setScene(new Scene(root, 368, 400));
		primaryStage.setResizable(false);
		final Miner miner = new Miner("miner");
		miner.start();
		primaryStage.show();
	}

	public static void main(final String[] args) {
		launch(args);
		System.exit(0);
	}
}
