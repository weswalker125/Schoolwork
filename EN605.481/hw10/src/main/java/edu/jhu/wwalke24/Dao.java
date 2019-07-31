package edu.jhu.wwalke24;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.jhu.wwalke24.PersonController.PersonType;

public class Dao {
	public static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
	private static final String CREATE_EMPLOYEES = "CREATE TABLE employees (id varchar(50), firstName varchar(255), lastName varchar(255), streetAddress varchar(255), city varchar(255), state varchar(30), zipcode varchar(5), gender varchar(10))";
	private static final String CREATE_CUSTOMERS = "CREATE TABLE customers (id varchar(50), firstName varchar(255), lastName varchar(255), streetAddress varchar(255), city varchar(255), state varchar(30), zipcode varchar(5), gender varchar(10))";
	private static final String CREATE_MERCHANDISE = "CREATE TABLE merchandise (id varchar(50), name varchar(255), price float, description varchar(255))";
	private static final String SELECT_EMPLOYEE = "SELECT id, firstName, lastName, streetAddress, city, state, zipcode, gender FROM employees";
	private static final String SELECT_CUSTOMERS = "SELECT id, firstName, lastName, streetAddress, city, state, zipcode, gender FROM customers";
	private static final String SELECT_MERCHANDISE = "SELECT id, name, price, description FROM merchandise";
	private static final String INSERT_EMPLOYEE = "INSERT INTO employees (id, firstName, lastName, streetAddress, city, state, zipcode, gender) VALUES(?,?,?,?,?,?,?,?)";
	private static final String INSERT_CUSTOMER = "INSERT INTO customers (id, firstName, lastName, streetAddress, city, state, zipcode, gender) VALUES(?,?,?,?,?,?,?,?)";
	private static final String INSERT_MERCHANDISE = "INSERT INTO merchandise (id, name, price, description) VALUES(?,?,?,?)";
	private static final String UPDATE_EMPLOYEE= "UPDATE employees SET firstName = ?, lastName = ?, streetAddress = ?, city = ?, state = ?, zipcode = ?, gender = ? WHERE id = ?";
	private static final String UPDATE_CUSTOMER = "UPDATE customers SET firstName = ?, lastName = ?, streetAddress = ?, city = ?, state = ?, zipcode = ?, gender = ? WHERE id = ?";
	private static final String UPDATE_MERCHANDISE = "UPDATE merchandise SET name = ?, price = ?, description = ? WHERE id = ?";
	
	static {
		try {
			Class.forName(driver).newInstance();
			
			// Create tables
			try (Connection c = getConnection()) {
				try (Statement s = c.createStatement()) {
					String[] queries = { CREATE_CUSTOMERS, CREATE_EMPLOYEES, CREATE_MERCHANDISE };
					
					for (String query : queries) {
						try {
							s.execute(query);
						} catch (SQLException e) {
							System.out.println("Failed to create table. Error: " + e.getMessage());
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:derby:derbyDB;create=true");
	}
	
	public List<Rowable> getEmployees() {
		return getPeople(PersonController.PersonType.EMPLOYEE);
	}
	
	public List<Rowable> getCustomers() {
		return getPeople(PersonController.PersonType.CUSTOMER);
	}
	
	public List<Rowable> getPeople(PersonController.PersonType type) {
		List<Rowable> ret = new ArrayList<>();
		
		try (Connection c = getConnection()) {
			try (Statement stmt = c.createStatement()) {
				ResultSet result = null;
				if (type == PersonType.EMPLOYEE) {
					result = stmt.executeQuery(SELECT_EMPLOYEE);
				} else {
					result = stmt.executeQuery(SELECT_CUSTOMERS);
				}
				
				while (result.next()) {
					Person p = new Person();
					p.setCustomer(type == PersonController.PersonType.CUSTOMER);
					p.setId(result.getString(1));
					p.setFirstName(result.getString(2));
					p.setLastName(result.getString(3));
					p.setStreetAddress(result.getString(4));
					p.setCity(result.getString(5));
					p.setState(result.getString(6));
					p.setZipcode(result.getString(7));
					p.setGender(result.getString(8));
					
					ret.add(p);
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return ret;
	}
	
	public List<Rowable> getMerchandise() {
		List<Rowable> ret = new ArrayList<>();
		
		try (Connection c = getConnection()) {
			try (Statement stmt = c.createStatement()) {
				ResultSet result = stmt.executeQuery(SELECT_MERCHANDISE);
				while (result.next()) {
					Product p = new Product();
					p.setId(result.getString(1));
					p.setName(result.getString(2));
					p.setPrice(result.getDouble(3));
					p.setDescription(result.getString(4));
					
					ret.add(p);
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return ret;
	}

	public void addEmployee(Person p) {
		try (Connection c = getConnection()) {
			try (PreparedStatement stmt = c.prepareStatement(INSERT_EMPLOYEE)) {
				stmt.setString(1, p.getId());
				stmt.setString(2, p.getFirstName());
				stmt.setString(3, p.getLastName());
				stmt.setString(4, p.getStreetAddress());
				stmt.setString(5, p.getCity());
				stmt.setString(6, p.getState());
				stmt.setString(7, p.getZipcode());
				stmt.setString(8, p.getGender());
				
				stmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addCustomer(Person p) {
		try (Connection c = getConnection()) {
			try (PreparedStatement stmt = c.prepareStatement(INSERT_CUSTOMER)) {
				stmt.setString(1, p.getId());
				stmt.setString(2, p.getFirstName());
				stmt.setString(3, p.getLastName());
				stmt.setString(4, p.getStreetAddress());
				stmt.setString(5, p.getCity());
				stmt.setString(6, p.getState());
				stmt.setString(7, p.getZipcode());
				stmt.setString(8, p.getGender());
				
				stmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addMerchandise(Product p) {
		try (Connection c = getConnection()) {
			try (PreparedStatement stmt = c.prepareStatement(INSERT_MERCHANDISE)) {
				stmt.setString(1, p.getId());
				stmt.setString(2, p.getName());
				stmt.setDouble(3, p.getPrice());
				stmt.setString(4, p.getDescription());
				
				stmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateEmployee(Person p) {
		try (Connection c = getConnection()) {
			try (PreparedStatement stmt = c.prepareStatement(UPDATE_EMPLOYEE)) {
				stmt.setString(1, p.getFirstName());
				stmt.setString(2, p.getLastName());
				stmt.setString(3, p.getStreetAddress());
				stmt.setString(4, p.getCity());
				stmt.setString(5, p.getState());
				stmt.setString(6, p.getZipcode());
				stmt.setString(7, p.getGender());
				stmt.setString(8, p.getId());
				
				stmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateCustomer(Person p) {
		try (Connection c = getConnection()) {
			try (PreparedStatement stmt = c.prepareStatement(UPDATE_CUSTOMER)) {
				stmt.setString(1, p.getFirstName());
				stmt.setString(2, p.getLastName());
				stmt.setString(3, p.getStreetAddress());
				stmt.setString(4, p.getCity());
				stmt.setString(5, p.getState());
				stmt.setString(6, p.getZipcode());
				stmt.setString(7, p.getGender());
				stmt.setString(8, p.getId());
				
				stmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateMerchandise(Product p) {
		try (Connection c = getConnection()) {
			try (PreparedStatement stmt = c.prepareStatement(UPDATE_MERCHANDISE)) {
				stmt.setString(1, p.getName());
				stmt.setDouble(2, p.getPrice());
				stmt.setString(3, p.getDescription());
				stmt.setString(4, p.getId());
				
				stmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
