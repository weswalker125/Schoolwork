package edu.jhu.wwalke24;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Collection of event handlers for the FXML user interface.
 */
public class Homework7Controller {
	/**
	 * Event handler for "OK" button.
	 * Prints message to stdout when button is clicked.
	 * @param event
	 */
	@FXML
	protected void handleOkButtonAction(ActionEvent event) {
		System.out.println("[ OK ] button clicked.");
	}
	
	/**
	 * Event handler for "Cancel" button.
	 * Prints message to stdout when button is clicked.
	 * @param event
	 */
	@FXML
	protected void handleCancelButtonAction(ActionEvent event) {
		System.out.println("[ Cancel ] button clicked.");
	}
	
	/**
	 * Event handler for "Setup" button.
	 * Prints message to stdout when button is clicked.
	 * @param event
	 */
	@FXML
	protected void handleSetupButtonAction(ActionEvent event) {
		System.out.println("[ Setup ] button clicked.");
	}
	
	/**
	 * Event handler for "Help" button.
	 * Prints message to stdout when button is clicked.
	 * @param event
	 */
	@FXML
	protected void handleHelpButtonAction(ActionEvent event) {
		System.out.println("[ Help ] button clicked.");
	}
}
