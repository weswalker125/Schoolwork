package edu.jhu.wwalke24;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.jhu.wwalke24.MockUtil.Models;
import edu.jhu.wwalke24.PersonController.PersonType;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Collection of event handlers for the FXML user interface.
 */
public class MainController {

	private static List<Rowable> employees = new ArrayList<>();
	private static List<Rowable> customers = new ArrayList<>();
	private static List<Rowable> merchandise = new ArrayList<>();
	private static MainController _instance;
	private MockUtil.Models displayType = Models.CUSTOMER;
	
	public MainController() {
		_instance = this;
	}

	@SuppressWarnings("rawtypes")
	@FXML
	private TableView dataTable;
	
	@FXML
	private Button addButton;
	
	@FXML
	private Label headingLabel;
	
	private Rowable selectedItem;

	public static void addEmployee(Person p) {
		// check for existing record
		Optional<Rowable> existingPerson = employees.stream().filter(x -> p.getId().equals(x.getId())).findFirst();
		if (existingPerson.isPresent()) {
			// update existing record
			System.out.println("found it");
			Person e = (Person) existingPerson.get();
			p.copy(e);
		} else {
			employees.add(p);
		}
		_instance.drawTable(employees);
	}

	public static void addCustomer(Person p) {
		// check for existing record
		Optional<Rowable> existingPerson = customers.stream().filter(x -> p.getId().equals(x.getId())).findFirst();
		if (existingPerson.isPresent()) {
			// update existing record
			System.out.println("found it");
			Person e = (Person) existingPerson.get();
			p.copy(e);
		} else {
			customers.add(p);
		}
		_instance.drawTable(customers);
	}

	public static void addProduct(Product p) {
		// check for existing record
		Optional<Rowable> existingProduct = merchandise.stream().filter(x -> p.getId().equals(x.getId())).findFirst();
		if (existingProduct.isPresent()) {
			// update existing record
			System.out.println("found it");
			Product e = (Product) existingProduct.get();
			p.copy(e);
		} else {
			merchandise.add(p);
		}
		_instance.drawTable(merchandise);
	}

	@FXML
	protected void showCustomers(ActionEvent event) throws IOException {
		displayType = Models.CUSTOMER;
		headingLabel.setText("CUSTOMER LISTING");
		drawTable(customers);
	}

	@FXML
	protected void showEmployees(ActionEvent event) throws IOException {
		displayType = Models.EMPLOYEE;
		headingLabel.setText("EMPLOYEE LISTING");
		drawTable(employees);
	}

	@FXML
	protected void showMerchandise(ActionEvent event) throws IOException {
		displayType = Models.MERCHANDISE;
		headingLabel.setText("MERCHANDISE LISTING");
		drawTable(merchandise);
	}

	@FXML
	protected void newCustomer(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/addPerson.fxml"));

		Parent root = loader.load();
		PersonController controller = loader.<PersonController>getController();
		controller.type = PersonType.CUSTOMER;
		Stage stage = new Stage();
		stage.setTitle("Add New Customer");
		stage.setScene(new Scene(root, 430, 393));
		stage.show();
	}

	@FXML
	protected void newEmployee(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/addPerson.fxml"));

		Parent root = loader.load();
		PersonController controller = loader.<PersonController>getController();
		controller.type = PersonType.EMPLOYEE;
		Stage stage = new Stage();
		stage.setTitle("Add New Employee");
		stage.setScene(new Scene(root, 430, 393));
		stage.show();
	}

	@FXML
	protected void newMerchandise(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(this.getClass().getResource("/addProduct.fxml"));
		Stage stage = new Stage();
		stage.setTitle("Add New Merchandise");
		stage.setScene(new Scene(root, 430, 393));
		stage.show();
	}

	@FXML
	protected void quit(ActionEvent event) {
		System.exit(0);
	}
	
	@SuppressWarnings("unchecked")
	@FXML
	protected void onTableViewClick(MouseEvent event) {
		List<String> selected = (List<String>) dataTable.getSelectionModel().getSelectedItem();
		if (selected == null || selected.isEmpty()) {
			selectedItem = null;
		} else {
			Optional<Rowable> existing = null;
			if ((existing = employees.stream().filter(x -> selected.get(0).equals(x.getId())).findFirst()).isPresent()) {
				System.out.println("existing employee.");
			} else if ((existing = customers.stream().filter(x -> selected.get(0).equals(x.getId())).findFirst()).isPresent()) {
				System.out.println("existing customer.");
			} else if ((existing = merchandise.stream().filter(x -> selected.get(0).equals(x.getId())).findFirst()).isPresent()) {
				System.out.println("existing product.");
			} else {
				System.out.println("no item selected.");
				selectedItem = null;
			}
			
			if (existing.isPresent()) {
				selectedItem = existing.get();
				// Show editing form
				addButton.setText("Edit");
			}
		}
	}
	
	@FXML
	protected void onAddOrEditClick(ActionEvent event) throws IOException {
		String windowTitle = "";
		FXMLLoader loader = null;
		Parent root = null;
		if (null == selectedItem) {
			switch (displayType) {
			case CUSTOMER:
				newCustomer(null);
				break;
			case EMPLOYEE:
				newEmployee(null);
				break;
			case MERCHANDISE:
				newMerchandise(null);
				break;
			}
		} else {
			if (selectedItem instanceof Person) {
				Person selectedPerson = (Person) selectedItem;
				
				loader = new FXMLLoader(this.getClass().getResource("/addPerson.fxml"));
				root = loader.load();
				PersonController controller = loader.<PersonController>getController();
				controller.setPerson(selectedPerson);
				
				// Show editing form
				if (selectedPerson.isCustomer()) {	
					controller.type = PersonType.CUSTOMER;
					windowTitle = "Add New Customer";
				} else {
					controller.type = PersonType.EMPLOYEE;
					windowTitle = "Add New Employee";
				}
				
			} else {
				Product selectedProduct = (Product) selectedItem;
				
				// Show editing form
				loader = new FXMLLoader(this.getClass().getResource("/addProduct.fxml"));
				root = loader.load();
				ProductController controller = loader.<ProductController>getController();
				controller.setProduct(selectedProduct);
				windowTitle = "Add New Merchandise";
			}
		
			Stage stage = new Stage();
			stage.setTitle(windowTitle);
			stage.setScene(new Scene(root, 430, 393));
			stage.show();
		}
	}
	
	private void drawTable(List<Rowable> items) {
		dataTable.getColumns().clear();
		dataTable.getItems().clear();

		// dynamic column names
		if (!items.isEmpty()) {
			String[] columnNames = items.get(0).getColumnNames();
			for (int i = 0; i < columnNames.length; ++i) {
				final int finalIdx = i;
				TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>(columnNames[i]);
				column.setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().get(finalIdx)));
				dataTable.getColumns().add(column);
			}
			
			for (int n = 0; n < items.size(); ++n) {
				dataTable.getItems().add(items.get(n).toRow());
			}
		}
		addButton.setText("Add New");
		selectedItem = null;
	}
	
	public void mockCustomers(ActionEvent event) throws IOException {
		for (int i = 0; i < 30; ++i) {
			this.customers.add(MockUtil.mockPerson(PersonType.CUSTOMER));
		}
		showCustomers(null);
	}
	
	public void mockEmployees(ActionEvent event) throws IOException {
		for (int i = 0; i < 30; ++i) {
			this.employees.add(MockUtil.mockPerson(PersonType.EMPLOYEE));
		}
		showEmployees(null);
	}
	
	public void mockMerchandise(ActionEvent event) throws IOException {
		for (int i = 0; i < 30; ++i) {
			
			this.merchandise.add(MockUtil.mockProduct());
		}
		showMerchandise(null);
	}
}
