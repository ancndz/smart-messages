package ru.utkaev.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ru.utkaev.entity.User;

public class UserController {
	@FXML
	public Label errorLabel;
	@FXML
	public Button clearButton;
	@FXML
	public TextArea messagesArea;
	@FXML
	public ComboBox receiversBox;
	private User user;

	@FXML
	private TextField userTextField;

	@FXML
	private TextArea textArea;

	@FXML
	private Button getKeyButton;

	@FXML
	private Button sendButton;

	@FXML
	private Button checkButton;

	@FXML
	void getKeys(final ActionEvent ae) throws NoSuchAlgorithmException, IOException {
		user = new User(userTextField.getText());
		user.start();
		user.broadcastPublicKey();

		getKeyButton.setDisable(true);
		userTextField.setDisable(true);
		sendButton.setDisable(false);
		checkButton.setDisable(false);
		receiversBox.setDisable(false);
		textArea.setDisable(false);
		clearButton.setDisable(false);
	}

	@FXML
	void sendMsg(final ActionEvent ae) throws Exception {
		this.errorLabel.setText("");
		if (user.createMessage(textArea.getText(), receiversBox.getValue().toString())) {
			textArea.setText("");
		} else {
			this.errorLabel.setText("User does not exists!");
		}
	}

	@FXML
	void displayAllMsgs(final ActionEvent ae) {
		this.errorLabel.setText("");
		messagesArea.setText(user.printMyMessages());
	}

	@FXML
	public void clearAll(final ActionEvent actionEvent) {
		this.errorLabel.setText("");
		this.textArea.setText("");
	}

	public void refreshReceiverList(MouseEvent mouseEvent) {
		this.errorLabel.setText("");
		this.receiversBox.setItems(FXCollections.observableList(Arrays.asList(user.getAllReceivers().toArray())));
	}
}
