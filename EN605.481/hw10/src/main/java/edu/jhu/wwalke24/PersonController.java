package edu.jhu.wwalke24;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class PersonController {
	@FXML
	private TextField firstNameText;
	
	@FXML
	private TextField lastNameText;
	
	@FXML
	private TextField streetAddressText;
	
	@FXML
	private TextField cityText;
	
	@FXML
	private ComboBox<String> stateCombo;
	
	@FXML
	private TextField zipcodeText;
	
	@FXML
	private ToggleGroup genderToggle;
	
	@FXML
	private RadioButton radioMale;
	
	@FXML 
	private RadioButton radioFemale;
	
	public PersonType type;
	
	private Person person;

	@FXML
	protected void handleAddPersonOkButton(ActionEvent event) {
		System.out.println("OK");
		
		// Gather Person details
		if (null == person) {
			person = new Person();
		}
		person.setFirstName(this.firstNameText.getText());
		person.setLastName(this.lastNameText.getText());
		person.setStreetAddress(this.streetAddressText.getText());
		person.setCity(this.cityText.getText());
		person.setState(this.stateCombo.getValue());
		person.setZipcode(this.zipcodeText.getText());
		person.setGender(((RadioButton)this.genderToggle.getSelectedToggle()).getText());
		
		// Add the person to the appropriate list
		switch (type) {
		case CUSTOMER:
			person.setCustomer(true);
			MainController.addCustomer(person);
			break;
		case EMPLOYEE:
			person.setCustomer(false);
			MainController.addEmployee(person);
			break;
		}
		
		// This feels like an overly complicated means of closing the window...
		((Stage) ((Node)(event.getSource())).getScene().getWindow()).close();
	}

	@FXML
	protected void handleAddPersonCancelButton(ActionEvent event) {
		System.out.println("Cancel");
		
		((Stage) ((Node)(event.getSource())).getScene().getWindow()).close();
	}
	
	public static enum PersonType {
		EMPLOYEE, CUSTOMER
	}
	
	public void setPerson(Person p) {
		this.person = p;
		this.cityText.setText(p.getCity());
		this.firstNameText.setText(p.getFirstName());
		this.lastNameText.setText(p.getLastName());
		this.stateCombo.setValue(p.getState());
		this.streetAddressText.setText(p.getStreetAddress());
		this.zipcodeText.setText(p.getZipcode());
		if (p.getGender().equals("Male")) {
			this.radioMale.setSelected(true);
		} else {
			this.radioFemale.setSelected(true);
		}
	}
}
