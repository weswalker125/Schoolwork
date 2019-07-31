/*
 * Author: Wesley Walker
 * Date: October 14th, 2011
 * For: CS405 - 001 at the University of Kentucky.
 * 
 * The code submitted herewith is an original work 
 * performed by me expressly in fulfillment of requirements 
 * associated with the Project 1 of the class CS405, Fall 
 * semester 2011, possibly except  GUI Designer code (performing
 * declaration/initialization of design settings) created by
 * the Eclipse Indigo IDE, and mySQL connection techniques - knowledge
 * was aquired through Kyle Kolpek's tutorial distributed via class email.
 */

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JScrollPane;


public class DatabaseGUI {
	/*------------------------------------------------------------------------------------------------------------------*/
	/* Global GUI variables */
	/*------------------------------------------------------------------------------------------------------------------*/
	private JFrame project1Frame;
	private JTextField serviceText;
	private JTextField companyText;
	private JTextField amountText;
	private JTextField monthText;
	private JTextField dayText;
	private JTextField yearText;
	private JTextField edit_companyNameText;
	private JTextField edit_amountText;
	private JTextField edit_monthText;
	private JTextField edit_dayText;
	private JTextField edit_yearText;
	
	JPanel mainPanel;
	JPanel titlePanel;
	JPanel createPanel;
	JPanel viewPanel;
	JList serviceList;
	JList view_List;
	JList paidList;
	JPanel editPanel;
	JPanel paidPanel;
	static java.sql.Statement myStatement;
	private JTextField create_cardNumberText;
	private JTextField main_monthText;
	private JTextField textField_1;
	private JTextField main_yearText;
	/*------------------------------------------------------------------------------------------------------------------*/
	/* Global GUI variables END*/
	/*------------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DatabaseGUI window = new DatabaseGUI();
					window.project1Frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DatabaseGUI() {
		initialize();
		//Hide unwanted initial elements
		titlePanel.setVisible(true);
		viewPanel.setVisible(false);
		createPanel.setVisible(false);
		editPanel.setVisible(false);
		paidPanel.setVisible(false);
		
		//List all services in database
		serviceList.setModel(showTables());
	}
	/*------------------------------------------------------------------------------------------------------------------*/
	// getConnection()
	// input: null
	// action: create connection to SQL database
	// return: void
	private static void getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String userName = "root";
			String password = "change_it"; //TODO: CHANGE THIS
			String dbName = "CS405";
			String dbUrl = "jdbc:mysql://127.0.0.1:3306/" + dbName + "?user=" + userName + "&password=" + password;
			
			//Create connection
			java.sql.Connection con = DriverManager.getConnection(dbUrl);
			//Set handle to connection
			myStatement = con.createStatement();
			
			System.out.print("Connected to database!\n \t " + con + "\n");		
		}
		catch (Exception e) {
			System.out.print(e.getMessage());
		}
	}
	/*------------------------------------------------------------------------------------------------------------------*/
	// showTables()
	// input: None
	// action: retrieve list of all tables in database
	// return: DefaultListModel<String>
	private DefaultListModel<String> showTables() {
		getConnection();
		DefaultListModel<String> tableList = new DefaultListModel<String>();
		//Format query statement
		String query = "show tables";
		ResultSet queryResults;
		boolean paidTableExists = false;
		try {
			//Execute query
			queryResults = myStatement.executeQuery(query);
			while(queryResults.next()) {
				//Get results
				if(queryResults.getString(1).equals("paid"))
					paidTableExists = true;
				else
					tableList.addElement(queryResults.getString(1));
			}
		}
		catch(Exception e) {
			System.out.print(e.getMessage());
			tableList.addElement("SQL ERROR");
		}
		if(!paidTableExists) {
			//Create "paid" table to keep track of all 'paid' records.
			query = "CREATE TABLE paid (`companyName` VARCHAR(50) NOT NULL, `cardNumber` CHAR(16), `amount` DOUBLE, `dueDate` VARCHAR(10)," + 
						"`paidDate` VARCHAR(10) NOT NULL, `repeatable` TINYINT(1)  NOT NULL, `paid` TINYINT(1) NOT NULL, PRIMARY KEY (`companyName`) );";
			try {
				//Execute query
				myStatement.execute(query);
			}
			catch(Exception e) {
				System.out.print(e.getMessage() + "\n");
			}
		}
		return tableList;
	}
	/*------------------------------------------------------------------------------------------------------------------*/
	//viewTable(String tableName)
	//input: tableName to be viewed
	//action: query all records in the tableName requested
	//return: void
	private void viewTable(String tableName) {
		//Connect to Database
		getConnection();
		//SELECT SQL statement
		String selectQuery = "SELECT * FROM " + tableName;
		
		DefaultListModel<String> recordList = new DefaultListModel<String>();
		ResultSet queryResults;		
		try {
			//Execute query
			queryResults = myStatement.executeQuery(selectQuery);
			while(queryResults.next()) {				
				//Display results to list
				recordList.addElement("Company: " + queryResults.getString("companyName"));
				recordList.addElement("Card Number: " + queryResults.getString("cardNumber")); 
				recordList.addElement("Amount Due: " + queryResults.getDouble("amount"));
				recordList.addElement("Date Due: " + queryResults.getString("dueDate"));
				recordList.addElement("Repeat: " + queryResults.getBoolean("repeatable"));
				recordList.addElement("Paid: " + queryResults.getBoolean("paid"));
				recordList.addElement("    ");
			}			
		}
		catch(Exception e) {
			System.out.print(e.getMessage() + "\n");
			//Show that an error occurred
			recordList.addElement("Error in SELECT query");
		}
		
		view_List.setModel(recordList);		
	}
	/*------------------------------------------------------------------------------------------------------------------*/
	// formatDate
	// input: integers representing month, day and year
	// action: Adds leading 0 (keep consistent with 2 digit fields)
	// output: returns string of form mm-dd-yy
	private String formatDate(int month, int day, int year) {
		String sMonth = "", sDay = "", sYear = "", sDate = "";
		//Concatenate date string
		sMonth = String.valueOf(month);
		sDay = String.valueOf(day);
		sYear = String.valueOf(year);
		
		//Check length of strings (add leading 0)
		if(sMonth.length() < 2)
			sMonth = "0" + sMonth;
		if(sDay.length() < 2)
			sDay = "0" + sDay;
		if(sYear.length() > 4)
			sYear = sYear.substring(0, 4);
		
		//concatenate with "-"s
		sDate = sMonth + "-" + sDay + "-" + sYear;
		return sDate;
		
	}
	/*------------------------------------------------------------------------------------------------------------------*/
	//createTable(String createQuery, String insertQuery)
	//input: two SQL queries, one formatted to create, one to insert
	//action: execute commands
	//return: void
	private void createTable(String createQuery, String insertQuery) {
		getConnection();
		try {
			//Execute queries
			myStatement.execute(createQuery);
			myStatement.execute(insertQuery);
		}
		catch(Exception e) {
			System.out.print(e.getMessage() + "\n");
		}
	}
	/*------------------------------------------------------------------------------------------------------------------*/
	// dropTable(String tableName)
	// input: name of the service (table) to be dropped
	// action: Execute sql drop statement
	// output: void
	private void dropTable(String tableName) {
		getConnection();
		//Format drop query
		String dropQuery = "DROP TABLE " + tableName;
		try {
			//Execute statement
			myStatement.execute(dropQuery);
		}
		catch(Exception e) {
			System.out.print(e.getMessage());
		}
	}
	/*------------------------------------------------------------------------------------------------------------------*/
	// payBill(String tableName)
	// input: name of service (table) to pay the bill of
	// action: change boolean to paid, 
	// output: void
	private void payBill(String tableName) {
		getConnection();
		String selectQuery = "SELECT * FROM " + tableName;
		ResultSet queryResults;
		String companyName = "", cardNumber = "", date = "", paidDate = "";
		double amount = 0;
		boolean repeatable = false, paid = false;
		try {
			//Execute query
			queryResults = myStatement.executeQuery(selectQuery);
			while(queryResults.next()) {
				//Extract query results
				companyName = queryResults.getString("companyName");
				cardNumber = queryResults.getString("cardNumber");
				amount = queryResults.getDouble("amount");
				date = queryResults.getString("dueDate");
				repeatable = queryResults.getBoolean("repeatable");
				paid = queryResults.getBoolean("paid");
			}
		}
		catch(Exception e) {
			System.out.print(e.getMessage() + "\n");
		}
		
		int month=0, day=0, year=0;
			
		if(!paid) {
			//Get today's date
			java.util.Date currentDate = new java.util.Date();
			SimpleDateFormat normalizedDate = new SimpleDateFormat("MM-dd-yyyy");
			paidDate = normalizedDate.format(currentDate);
					
			//Format update query to service
			String updateQuery = "UPDATE " + tableName + " SET paid=true WHERE companyName='" + companyName + "';";
			//Format update query to "paid" table
			String updatePaidQuery = "INSERT INTO paid VALUES('" + companyName + "', '" + cardNumber + "', " + 
					amount + ", '" + date + "', '" + paidDate + "', " + repeatable + ", TRUE);";
			
			System.out.print(updateQuery + "\n");
			System.out.print(updatePaidQuery + "\n");
			try {
				//Execute queries
				myStatement.executeUpdate(updateQuery);
				myStatement.executeUpdate(updatePaidQuery);
			}
			catch(Exception e) {
				System.out.print(e.getMessage() + "\n");
			}
			//For repeatable services
			if(repeatable) {
				//Separate date
				month = Integer.parseInt(date.substring(0, 2));
				day = Integer.parseInt(date.substring(3, 5));
				year = Integer.parseInt(date.substring(6));	
				//update (month + 1) % 12
				month ++;
				if(month > 12) {
					year++;
					month = month % 12;
				}
				if(day == 31) {
					//check month
					if(month == 4 ||month == 6||month == 9||month == 11)
						day--;
					if(month == 2)
						day = 28;
				}
				//Concatenate date string
				date = formatDate(month, day, year);
				
				//Format update query 
				updateQuery = "UPDATE " + tableName + " SET dueDate='" + date + "', paid=false WHERE companyName='" + companyName + "';";
				System.out.print(updateQuery + "\n");
				try {
					//Execute query
					myStatement.executeUpdate(updateQuery);
				}
				catch (Exception e){
					System.out.print(e.getMessage() + "\n");
				}
			}
		}
		else {
			System.out.print("Bill is paid\n");
		}
	}
	/*------------------------------------------------------------------------------------------------------------------*/
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//START GUI Declarations/Initializations
		project1Frame = new JFrame();
		project1Frame.setTitle("CS405 (Wesley Walker)");
		project1Frame.setBounds(100, 100, 495, 395);
		project1Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JScrollPane scrollPane = new JScrollPane();
		
		mainPanel = new JPanel();
		titlePanel = new JPanel();
		createPanel = new JPanel();
		viewPanel = new JPanel();
		editPanel = new JPanel();
		paidPanel = new JPanel();
		paidList = new JList();
		view_List = new JList();
		serviceList = new JList();
		edit_companyNameText = new JTextField();
		edit_companyNameText.setEditable(false);
		edit_companyNameText.setColumns(10);
		edit_amountText = new JTextField();
		edit_amountText.setColumns(10);
		edit_monthText = new JTextField();
		edit_monthText.setColumns(2);
		edit_dayText = new JTextField();
		edit_dayText.setColumns(2);
		edit_yearText = new JTextField();
		edit_yearText.setColumns(4);
		serviceText = new JTextField();
		serviceText.setColumns(10);
		companyText = new JTextField();
		companyText.setColumns(10);
		amountText = new JTextField();
		amountText.setColumns(10);
		monthText = new JTextField();
		monthText.setColumns(10);
		dayText = new JTextField();
		dayText.setColumns(10);
		yearText = new JTextField();
		yearText.setColumns(10);
		main_yearText = new JTextField();
		main_yearText.setEditable(false);
		main_yearText.setColumns(4);
		create_cardNumberText = new JTextField();
		create_cardNumberText.setText("(Card number)");
		create_cardNumberText.setEnabled(false);
		create_cardNumberText.setColumns(10);
		
		final JCheckBox repeatableCheck = new JCheckBox("Repeatable");
		final JCheckBox edit_repeatableCheck = new JCheckBox("Repeatable");
		final JLabel dateLabel = new JLabel("Due Date");
		final JLabel paidLabel = new JLabel("(Paid/Unpaid)");
		final JLabel view_ServiceLabel = new JLabel("Service");
		final JLabel edit_serviceNameLabel = new JLabel("(Service)");
		paidLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 20));
		view_ServiceLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 18));
		JLabel titleLabel = new JLabel("Project 1");
		titleLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 24));
		JLabel serviceLabel = new JLabel("Services");
		serviceLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 18));
		JLabel edit_label1 = new JLabel("Edit Service:");
		edit_label1.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		edit_serviceNameLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		
		JLabel slash3 = new JLabel("/");
		JLabel slash4 = new JLabel("/");
		JLabel lblCompanyName = new JLabel("Company Name");
		JLabel lblAmount = new JLabel("Amount");
		JLabel lblDueDate = new JLabel("Due Date");
		JLabel serviceNameLabel = new JLabel("Service Name");
		JLabel companyNameLabel = new JLabel("Company Name");
		JLabel amountLabel = new JLabel("Amount");
		JLabel slash1 = new JLabel("/");
		JLabel slash2 = new JLabel("/");
		
		GroupLayout gl_paidPanel = new GroupLayout(paidPanel);
		GroupLayout gl_editPanel = new GroupLayout(editPanel);

		paidList.setValueIsAdjusting(true);
		scrollPane.setViewportView(paidList);
		paidPanel.setLayout(gl_paidPanel);		
		GroupLayout gl_viewPanel = new GroupLayout(viewPanel);
		//END GUI Declarations/Initializations		
		/*------------------------------------------------------------------------------------------------------------------*/
		/*------------------------------------------------------------------------------------------------------------------*/
		/*------------------------------------------------------------------------------------------------------------------*/
		
		//EDIT WINDOW: DONE BUTTON CLICK
		JButton edit_doneButton = new JButton("Done");
		edit_doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String tableName = edit_serviceNameLabel.getText();
				
				String companyName = edit_companyNameText.getText();
				//date format:
				if(edit_monthText.getText().length() == 1)
					edit_monthText.setText("0" + edit_monthText.getText());
				if(edit_dayText.getText().length() == 1)
					edit_dayText.setText("0" + edit_dayText.getText());
				String date = "'" + edit_monthText.getText() + "-" + edit_dayText.getText() + "-" + edit_yearText.getText() + "'";
				
				//Format update query
				String updateQuery = "UPDATE " + tableName + " " + 
						"SET amount=" + edit_amountText.getText() + 
						", dueDate=" + date +
						", repeatable=" + String.valueOf(edit_repeatableCheck.isSelected()) + 
						" WHERE companyName='" + companyName + "';";				
				try {
					//Execute query
					myStatement.executeUpdate(updateQuery);
				} 
				catch (SQLException error) {
					System.out.print(error.getMessage() + "\n");
				}
				
				//Back to Main panel
				viewPanel.setVisible(false);
				createPanel.setVisible(false);
				editPanel.setVisible(false);
				paidPanel.setVisible(false);
				mainPanel.setVisible(true);
				serviceList.setModel(showTables());
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//EDIT WINDOW: CANCEL BUTTON CLCIK
		JButton edit_cancelButton = new JButton("Cancel");
		edit_cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Back to Main panel
				viewPanel.setVisible(false);
				createPanel.setVisible(false);
				editPanel.setVisible(false);
				paidPanel.setVisible(false);
				mainPanel.setVisible(true);
				serviceList.setModel(showTables());
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//VIEW_DONE BUTTON CLICK
		JButton view_doneButton = new JButton("Done");
		view_doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Back to Main panel
				viewPanel.setVisible(false);
				createPanel.setVisible(false);
				paidPanel.setVisible(false);
				mainPanel.setVisible(true);
				serviceList.setModel(showTables());
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//CREATE_DONE BUTTON CLICK
		JButton create_doneButton = new JButton("Done");
		create_doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// date format:
				boolean correctDate = true;
				int month = Integer.valueOf(monthText.getText());
				int day = Integer.valueOf(dayText.getText());
				int year = Integer.valueOf(yearText.getText());
				if(month > 12 || day > 31 || year > 9999 || year < 1000 || day < 1 || month < 1)
					correctDate = false;
				String date = formatDate(month, day, year);
			
				String cardNumber;
				if(!create_cardNumberText.isEnabled())
					cardNumber = "NULL";
				else
					cardNumber = create_cardNumberText.getText();
				if(amountText.getText().isEmpty())
					amountText.setText("NULL");
				
				//Format create query
				String createQuery = "CREATE TABLE " + serviceText.getText() + 
						" (`companyName` VARCHAR(50) NOT NULL, `cardNumber` CHAR(16), `amount` DOUBLE, `dueDate` VARCHAR(10) NOT NULL," + 
						"`repeatable` TINYINT(1)  NOT NULL, `paid` TINYINT(1) NOT NULL, PRIMARY KEY (`companyName`) );";
				//Format insert query
				String insertQuery = "INSERT INTO " +  serviceText.getText() + 
						" VALUES('" + companyText.getText() + "', '" + cardNumber + "', " + 
						amountText.getText() + ", '" + 
						date + "', " +
						String.valueOf(repeatableCheck.isSelected()) + 
						", false);";						
				
				System.out.print(createQuery + "\n");
				System.out.print(insertQuery + "\n");
				//Call creation/insertion function
				if(correctDate) {
					createTable(createQuery, insertQuery);
					
					//Clear text info
					amountText.setText(null);
					companyText.setText(null);
					serviceText.setText(null);
					create_cardNumberText.setText(null);
					monthText.setText(null);
					dayText.setText(null);
					yearText.setText(null);
					//Back to Main panel
					viewPanel.setVisible(false);
					createPanel.setVisible(false);
					paidPanel.setVisible(false);
					mainPanel.setVisible(true);
					serviceList.setModel(showTables());
				}
				//Display that there is an error in the date format.
				else {
					dateLabel.setForeground(Color.RED);
				}
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//CREATE_CANCEL BUTTON CLICK
		JButton create_cancelButton = new JButton("Cancel");
		create_cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Back to Main panel
				viewPanel.setVisible(false);
				createPanel.setVisible(false);
				mainPanel.setVisible(true);
				serviceList.setModel(showTables());
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//VIEW BUTTON CLICK
		JButton viewButton = new JButton("View/Pay");
		viewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(serviceList.getSelectedValue() == null)
					return;
				String tableName = serviceList.getSelectedValue().toString();
				//Hide other panels:
				mainPanel.setVisible(false);
				createPanel.setVisible(false);
				paidPanel.setVisible(false);
				//Show view panel
				viewPanel.setVisible(true);
				view_ServiceLabel.setText(tableName);
				viewTable(tableName);				
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//EDIT BUTTON CLICK
		JButton editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serviceList.getSelectedValue() == null)
					return;
				//Hide other panels:
				mainPanel.setVisible(false);
				viewPanel.setVisible(false);
				paidPanel.setVisible(false);
				createPanel.setVisible(false);
				//Show edit panel
				editPanel.setVisible(true);
				String tableName = serviceList.getSelectedValue().toString();
				edit_serviceNameLabel.setText(tableName);
				
				//Get original service information:
				getConnection();
				String selectQuery = "SELECT * FROM " + tableName + ";";
				ResultSet queryResults;
				String companyName = "", cardNumber = "", month = "", day = "", year = "", dueDate = "";
				double amount = 0;
				boolean repeatable = false;
				
				try {
					//Execute query
					queryResults = myStatement.executeQuery(selectQuery);
					while(queryResults.next()) {
						companyName = queryResults.getString("companyName");
						cardNumber = queryResults.getString("cardNumber");
						amount = queryResults.getDouble("amount");
						dueDate = queryResults.getString("dueDate");
						repeatable = queryResults.getBoolean("repeatable");
					}
				}
				catch(Exception error) {
					System.out.print(error.getMessage() + "\n");
				}
				
				//Separate date
				month = dueDate.substring(0, 2);
				day = dueDate.substring(3, 5);
				year = dueDate.substring(6);
				
				//Place current values into the text fields:
				edit_companyNameText.setText(companyName);
				edit_amountText.setText(String.valueOf(amount));
				edit_monthText.setText(month);
				edit_dayText.setText(day);
				edit_yearText.setText(year);
				edit_repeatableCheck.setSelected(repeatable);
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//CREATE BUTTON CLICK
		JButton createButton = new JButton("Create");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Hide other panels:
				mainPanel.setVisible(false);
				viewPanel.setVisible(false);
				paidPanel.setVisible(false);
				editPanel.setVisible(false);
				//Show create panel
				createPanel.setVisible(true);
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//DROP BUTTON CLICK
		JButton dropButton = new JButton("Remove");
		dropButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(serviceList.getSelectedValue() == null)
					return;
				//Drop the selected service
				dropTable(serviceList.getSelectedValue().toString());
				//Refresh serviceList
				serviceList.setModel(showTables());
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//PAY BUTTON CLICK
		JButton payButton = new JButton("Pay");
		payButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String tableName = serviceList.getSelectedValue().toString();
				payBill(tableName);
				viewTable(tableName);
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//QUERY BY YEAR CHECK
		final JCheckBox byYearCheck = new JCheckBox("Year");
		byYearCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				//Enable text fields
				main_yearText.setEditable(!main_yearText.isEditable());
				if(!main_yearText.isEditable()) 
					main_yearText.setText("");
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//QUERY BY MONTH CHECK
		JCheckBox byMonthCheck = new JCheckBox("Month");
		byMonthCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				//allow both to be editable
				main_monthText.setEditable(!main_monthText.isEditable());
				if(!main_monthText.isEditable())
					main_monthText.setText("");
				main_yearText.setEditable(!main_monthText.isEditable());
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//PAID BUTTON CLICK
		JButton paidButton = new JButton("View Paid services");
		paidButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Hide other panels:
				mainPanel.setVisible(false);
				viewPanel.setVisible(false);
				createPanel.setVisible(false);
				editPanel.setVisible(false);
				//Show paid panel
				paidPanel.setVisible(true);
				paidLabel.setText("Paid services");
				
				//Query by Month or Year booleans
				boolean queryByDate_month = false;
				boolean queryByDate_year = false;
				if(main_yearText.isEditable()) 
					queryByDate_year = true;
				if(main_monthText.isEditable())
					queryByDate_month = true;

				//Prepare query
				DefaultListModel<String> paidListModel = new DefaultListModel<String>();
				ResultSet queryResults;
				String selectQuery, tempTableName = "", sYear = "", sMonth = "";
				int iYear = 0, iMonth = 0;
				//all paid summary
				if(serviceList.getSelectedIndex() == -1)
					selectQuery = "SELECT * FROM paid;";
				//specific service's paid summary
				else {
					getConnection();
					//Get the company Name of the service
					selectQuery = "SELECT companyName FROM " + serviceList.getSelectedValue().toString() + ";";
					try {
						queryResults = myStatement.executeQuery(selectQuery);
						while(queryResults.next()) {
							tempTableName = queryResults.getString(1);
						}
					}
					catch(Exception e) {
						System.out.print(e.getMessage() + "\n");
					}
					//Get all paid records from selected service's company name
					selectQuery = "SELECT * FROM paid WHERE companyName='" + tempTableName + "';";
				}
				
				try{
					getConnection();
					//Execute query
					queryResults = myStatement.executeQuery(selectQuery);
					while(queryResults.next()) {
						if(queryByDate_year) {
							sYear = queryResults.getString("paidDate").substring(6);
							iYear = Integer.valueOf(sYear);
							if(queryByDate_month){
								sMonth = queryResults.getString("paidDate").substring(3, 4);
								iMonth = Integer.valueOf(sMonth);
							}
							//Query services by year
							if(iYear ==  Integer.valueOf(main_yearText.getText())) {
								//Query services by month
								if(queryByDate_month) {
									if(iMonth == Integer.valueOf(main_monthText.getText())) {
										//Display to list:
										paidListModel.addElement("Company: " + queryResults.getString("companyName"));
										paidListModel.addElement("Card Number: " + queryResults.getString("cardNumber")); 
										paidListModel.addElement("Amount Due: " + queryResults.getDouble("amount"));
										paidListModel.addElement("Date Due: " + queryResults.getString("dueDate"));
										paidListModel.addElement("Date Paid: " + queryResults.getString("paidDate"));
										paidListModel.addElement("Repeat: " + queryResults.getBoolean("repeatable"));
										paidListModel.addElement("Paid: " + queryResults.getBoolean("paid"));
										paidListModel.addElement("    ");	
									}
								}
								//Display to list:
								paidListModel.addElement("Company: " + queryResults.getString("companyName"));
								paidListModel.addElement("Card Number: " + queryResults.getString("cardNumber")); 
								paidListModel.addElement("Amount Due: " + queryResults.getDouble("amount"));
								paidListModel.addElement("Date Due: " + queryResults.getString("dueDate"));
								paidListModel.addElement("Date Paid: " + queryResults.getString("paidDate"));
								paidListModel.addElement("Repeat: " + queryResults.getBoolean("repeatable"));
								paidListModel.addElement("Paid: " + queryResults.getBoolean("paid"));
								paidListModel.addElement("    ");	
							}
						}
						//All paid records
						else {
							//Display to list:
							paidListModel.addElement("Company: " + queryResults.getString("companyName"));
							paidListModel.addElement("Card Number: " + queryResults.getString("cardNumber")); 
							paidListModel.addElement("Amount Due: " + queryResults.getDouble("amount"));
							paidListModel.addElement("Date Due: " + queryResults.getString("dueDate"));
							paidListModel.addElement("Date Paid: " + queryResults.getString("paidDate"));
							paidListModel.addElement("Repeat: " + queryResults.getBoolean("repeatable"));
							paidListModel.addElement("Paid: " + queryResults.getBoolean("paid"));
							paidListModel.addElement("    ");	
						}
					}
				}
				catch(Exception e) {
						System.out.print(e.getMessage() + "\n");
				}
			//Display new list:
			paidList.setModel(paidListModel);				
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		//UNPAID BUTTON CLICK
		JButton unpaidButton = new JButton("View Unpaid services");
		unpaidButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Hide other panels:
				mainPanel.setVisible(false);
				viewPanel.setVisible(false);
				createPanel.setVisible(false);
				editPanel.setVisible(false);
				//Show paid panel
				paidPanel.setVisible(true);
				paidLabel.setText("Unpaid services");
				
				DefaultListModel<String> unpaidListModel = new DefaultListModel<String>();
				ResultSet queryResults;
				String selectQuery = "";
				int numTables = serviceList.getModel().getSize(); //number of services we're tracking
				
				try{
					getConnection();
					//Cycle through tables for unpaid services:
					for(int i = 0; i < numTables; i++) {
						System.out.print(serviceList.getModel().getElementAt(i) + "\n");
						selectQuery = "SELECT * FROM " + serviceList.getModel().getElementAt(i) + " WHERE paid=FALSE;";
						queryResults = myStatement.executeQuery(selectQuery);
						while(queryResults.next()) {
							//Display to list:
							unpaidListModel.addElement("Company: " + queryResults.getString("companyName"));
							unpaidListModel.addElement("Card Number: " + queryResults.getString("cardNumber")); 
							unpaidListModel.addElement("Amount Due: " + queryResults.getDouble("amount"));
							unpaidListModel.addElement("Date Due: " + queryResults.getString("dueDate"));
							unpaidListModel.addElement("Repeat: " + queryResults.getBoolean("repeatable"));
							unpaidListModel.addElement("Paid: " + queryResults.getBoolean("paid"));
							unpaidListModel.addElement("    ");
						}
					}
				}
				catch(Exception e) {
						System.out.print(e.getMessage() + "\n");
				}
			//Display new list:
			paidList.setModel(unpaidListModel);				
			}
		});
		
		JButton paid_doneButton = new JButton("Done");
		paid_doneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Back to Main panel
				viewPanel.setVisible(false);
				createPanel.setVisible(false);
				paidPanel.setVisible(false);
				mainPanel.setVisible(true);
				serviceList.setModel(showTables());
			}
		});
		
		JCheckBox create_creditCardCheck = new JCheckBox("Credit Card");
		create_creditCardCheck.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				create_cardNumberText.setEnabled(!create_cardNumberText.isEnabled());
			}
		});
		/*------------------------------------------------------------------------------------------------------------------*/
		/*------------------------------------------------------------------------------------------------------------------*/
		/*------------------------------------------------------------------------------------------------------------------*/
		/*------------------------------------------------------------------------------------------------------------------*/
//GUI DESIGNER-GENERATED CODE
		GroupLayout groupLayout = new GroupLayout(project1Frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(titlePanel, GroupLayout.DEFAULT_SIZE, 1221, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(createPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 471, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(paidPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(336))
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(editPanel, 0, 0, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(viewPanel, GroupLayout.PREFERRED_SIZE, 439, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(titlePanel, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(createPanel, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(viewPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(paidPanel, GroupLayout.PREFERRED_SIZE, 303, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(editPanel, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		GroupLayout gl_titlePanel = new GroupLayout(titlePanel);
		gl_titlePanel.setHorizontalGroup(
			gl_titlePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_titlePanel.createSequentialGroup()
					.addGap(173)
					.addComponent(titleLabel)
					.addContainerGap(945, Short.MAX_VALUE))
		);
		gl_titlePanel.setVerticalGroup(
			gl_titlePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_titlePanel.createSequentialGroup()
					.addContainerGap(17, Short.MAX_VALUE)
					.addComponent(titleLabel)
					.addContainerGap())
		);
		titlePanel.setLayout(gl_titlePanel);
		gl_paidPanel.setHorizontalGroup(
			gl_paidPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_paidPanel.createSequentialGroup()
					.addGroup(gl_paidPanel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_paidPanel.createSequentialGroup()
							.addGap(20)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 316, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_paidPanel.createSequentialGroup()
							.addGap(32)
							.addComponent(paidLabel)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(paid_doneButton)))
					.addContainerGap(58, Short.MAX_VALUE))
		);
		gl_paidPanel.setVerticalGroup(
			gl_paidPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_paidPanel.createSequentialGroup()
					.addContainerGap(19, Short.MAX_VALUE)
					.addGroup(gl_paidPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_paidPanel.createSequentialGroup()
							.addComponent(paidLabel)
							.addGap(26))
						.addGroup(gl_paidPanel.createSequentialGroup()
							.addComponent(paid_doneButton)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
					.addGap(33))
		);
		gl_editPanel.setHorizontalGroup(
			gl_editPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_editPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(edit_label1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(edit_serviceNameLabel)
					.addContainerGap(257, Short.MAX_VALUE))
				.addGroup(gl_editPanel.createSequentialGroup()
					.addContainerGap(37, Short.MAX_VALUE)
					.addGroup(gl_editPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_editPanel.createSequentialGroup()
							.addComponent(edit_cancelButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(edit_doneButton))
						.addGroup(gl_editPanel.createSequentialGroup()
							.addGroup(gl_editPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_editPanel.createSequentialGroup()
									.addComponent(edit_companyNameText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(edit_amountText, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
									.addGap(10)
									.addComponent(edit_monthText, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(slash3, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_editPanel.createSequentialGroup()
									.addComponent(lblCompanyName)
									.addGap(18)
									.addComponent(lblAmount)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_editPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_editPanel.createSequentialGroup()
									.addGap(10)
									.addComponent(edit_repeatableCheck))
								.addGroup(gl_editPanel.createSequentialGroup()
									.addComponent(edit_dayText, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
									.addGap(4)
									.addComponent(slash4, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(edit_yearText, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblDueDate))
							.addGap(11)))
					.addGap(151))
		);
		gl_editPanel.setVerticalGroup(
			gl_editPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_editPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_editPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(edit_label1)
						.addComponent(edit_serviceNameLabel))
					.addGap(18)
					.addGroup(gl_editPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCompanyName)
						.addComponent(lblAmount)
						.addComponent(lblDueDate))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_editPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_editPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(edit_dayText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(slash4)
							.addComponent(edit_yearText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_editPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(edit_companyNameText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(edit_amountText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(edit_monthText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(slash3)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_editPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(edit_doneButton)
						.addComponent(edit_cancelButton)
						.addComponent(edit_repeatableCheck))
					.addGap(22))
		);
		editPanel.setLayout(gl_editPanel);
		gl_viewPanel.setHorizontalGroup(
			gl_viewPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_viewPanel.createSequentialGroup()
					.addGap(28)
					.addGroup(gl_viewPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_viewPanel.createSequentialGroup()
							.addComponent(view_List, GroupLayout.PREFERRED_SIZE, 358, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_viewPanel.createSequentialGroup()
							.addComponent(view_ServiceLabel)
							.addPreferredGap(ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
							.addComponent(view_doneButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(payButton, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(107, Short.MAX_VALUE))))
		);
		gl_viewPanel.setVerticalGroup(
			gl_viewPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_viewPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_viewPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(view_ServiceLabel)
						.addGroup(gl_viewPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(view_doneButton)
							.addComponent(payButton)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(view_List, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
					.addGap(33))
		);
		viewPanel.setLayout(gl_viewPanel);		
		GroupLayout gl_createPanel = new GroupLayout(createPanel);
		gl_createPanel.setHorizontalGroup(
			gl_createPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_createPanel.createSequentialGroup()
					.addGroup(gl_createPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_createPanel.createSequentialGroup()
							.addGap(152)
							.addComponent(create_cancelButton)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(create_doneButton))
						.addGroup(gl_createPanel.createSequentialGroup()
							.addGap(51)
							.addGroup(gl_createPanel.createParallelGroup(Alignment.TRAILING)
								.addComponent(serviceNameLabel)
								.addComponent(companyNameLabel)
								.addComponent(amountLabel)
								.addComponent(dateLabel))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_createPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(repeatableCheck)
								.addComponent(amountText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_createPanel.createSequentialGroup()
									.addComponent(companyText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(create_cardNumberText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_createPanel.createSequentialGroup()
									.addComponent(serviceText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(create_creditCardCheck))
								.addGroup(gl_createPanel.createSequentialGroup()
									.addComponent(monthText, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(slash1)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dayText, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(slash2)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(yearText, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap(112, Short.MAX_VALUE))
		);
		gl_createPanel.setVerticalGroup(
			gl_createPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_createPanel.createSequentialGroup()
					.addGap(25)
					.addGroup(gl_createPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(serviceText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(serviceNameLabel)
						.addComponent(create_creditCardCheck))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_createPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(companyText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(companyNameLabel)
						.addComponent(create_cardNumberText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_createPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(amountText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(amountLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_createPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(dateLabel)
						.addComponent(monthText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(slash1)
						.addComponent(dayText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(slash2)
						.addComponent(yearText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(repeatableCheck)
					.addPreferredGap(ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
					.addGroup(gl_createPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(create_doneButton)
						.addComponent(create_cancelButton))
					.addContainerGap())
		);
		createPanel.setLayout(gl_createPanel);
		
		main_monthText = new JTextField();
		main_monthText.setEditable(false);
		main_monthText.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		
		
		
		
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(serviceLabel)
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(serviceList, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_mainPanel.createSequentialGroup()
									.addGap(57)
									.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(editButton)
										.addGroup(gl_mainPanel.createSequentialGroup()
											.addComponent(viewButton)
											.addGap(18)
											.addComponent(createButton))
										.addComponent(dropButton)))
								.addGroup(gl_mainPanel.createSequentialGroup()
									.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(unpaidButton, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
										.addComponent(paidButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_mainPanel.createSequentialGroup()
											.addComponent(byYearCheck)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(main_yearText, 0, 0, Short.MAX_VALUE))
										.addGroup(gl_mainPanel.createSequentialGroup()
											.addComponent(byMonthCheck)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(main_monthText, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)))
									.addGap(57)
									.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 0, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap(14, Short.MAX_VALUE)
					.addComponent(serviceLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addGroup(gl_mainPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(viewButton)
								.addComponent(createButton))
							.addGap(8)
							.addComponent(editButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(dropButton)
							.addPreferredGap(ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
							.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, gl_mainPanel.createParallelGroup(Alignment.BASELINE)
									.addComponent(main_monthText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(byMonthCheck))
								.addComponent(unpaidButton, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 49, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(paidButton, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
								.addGroup(Alignment.TRAILING, gl_mainPanel.createSequentialGroup()
									.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_mainPanel.createParallelGroup(Alignment.BASELINE)
											.addComponent(byYearCheck)
											.addComponent(main_yearText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
									.addGap(28))))
						.addComponent(serviceList, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		mainPanel.setLayout(gl_mainPanel);
		project1Frame.getContentPane().setLayout(groupLayout);
	}
}
