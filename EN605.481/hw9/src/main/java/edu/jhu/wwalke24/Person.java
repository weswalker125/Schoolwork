package edu.jhu.wwalke24;

import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

public class Person implements Rowable {
	private final static String[] columnNames =  new String[] { "ID", "First Name","Last Name", "Street Address", "City", "State", "Zipcode", "Gender" };
	
	@Getter
	private String id;
	
	@Getter @Setter
	private String firstName;
	
	@Getter @Setter
	private String lastName;
	
	@Getter @Setter
	private String streetAddress;
	
	@Getter @Setter
	private String city;
	
	@Getter @Setter
	private String state;
	
	@Getter @Setter
	private String zipcode;
	
	@Getter @Setter
	private String gender;
	
	@Getter @Setter
	private boolean customer;
	
	public Person() {
		id = UUID.randomUUID().toString().substring(0, 6);
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}
	
	public ObservableList<String> toRow() {
		ObservableList<String> ret = FXCollections.observableArrayList();
		ret.add(this.id);
		ret.add(this.firstName);
		ret.add(this.lastName);
		ret.add(this.streetAddress);
		ret.add(this.city);
		ret.add(this.state);
		ret.add(this.zipcode);
		ret.add(this.gender);
		return ret;
	}

	public void copy(Person e) {
		this.setCity(e.city);
		this.setCustomer(e.customer);
		this.setFirstName(e.firstName);
		this.setGender(e.gender);
		this.setLastName(e.lastName);
		this.setState(e.state);
		this.setStreetAddress(e.streetAddress);
		this.setZipcode(e.zipcode);
		this.id = e.getId();
	}
}
