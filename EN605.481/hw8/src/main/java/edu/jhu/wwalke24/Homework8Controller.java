package edu.jhu.wwalke24;

import java.text.NumberFormat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Collection of event handlers for the FXML user interface.
 */
public class Homework8Controller {
	private NumberFormat dollarFormatter = NumberFormat.getCurrencyInstance();
	
	@FXML
	private TextField loanAmountText;

	@FXML
	private ComboBox<Double> interestRateCombo;

	@FXML
	private ComboBox<Integer> mortgageTermCombo;
	
	@FXML
	private TextField totalPaymentText;
	
	@FXML
	private TableView<MonthlyBreakdown> amortizationTable;
	
	private double monthlyInterestRate;
	private double numberOfPayments;
	private double initialLoanAmount;
	/**
	 * Event handler for "Calculate" button. Prints message to stdout when button is
	 * clicked.
	 * 
	 * @param event
	 */
	@FXML
	protected void handleCalculateButtonAction(ActionEvent event) {
		initialLoanAmount = 0l;
		monthlyInterestRate = interestRateCombo.getValue() / 100 / 12;
		numberOfPayments = mortgageTermCombo.getValue() * 12;
		
		// Validate input
		String amountText = loanAmountText.getText();
		if (!(null == amountText || "".equals(amountText))) {
			initialLoanAmount = Long.parseLong(amountText.replace(",", "").replace("$", "").trim());
		} else {
			System.out.println("loanAmountText is not filled!");
		}

		// Calculate payment schedule
		System.out.println("Calculating...");
		System.out.println(String.format("[ Calculate ] for amount: %s, interest: %s, term: %s.",
				initialLoanAmount, monthlyInterestRate, numberOfPayments));
		// Display payment schedule
		amortizationSchedule();
		
		// Display total payment
		totalPaymentText.setText(dollarFormatter.format(calculatePayment() * numberOfPayments));
	}
	
	private double calculatePayment() {
		return initialLoanAmount * (monthlyInterestRate + (monthlyInterestRate/(Math.pow(1+monthlyInterestRate, numberOfPayments) - 1)));
	}
	
	private double calculatePaymentTowardsInterest(double balance) {
		return balance * monthlyInterestRate;
	}
	
	private double calculatePaymentTowardsPrincipal(double balance) {
		return calculatePayment() - calculatePaymentTowardsInterest(balance);
	}
	
	private void amortizationSchedule() {
		ObservableList<MonthlyBreakdown> schedule = amortizationTable.getItems();
		schedule.clear();
		
		for (int t = 0; t <= numberOfPayments; ++t) {
			MonthlyBreakdown currentMonth = new MonthlyBreakdown();
			currentMonth.setMonthNumber(t);
			
			if (t == 0) {
				currentMonth.setPayment(0);
				currentMonth.setInterestPaid(0);
				currentMonth.setPrincipalPaid(0);
				currentMonth.setTotalInterestPaid(0);
				currentMonth.setRemainingBalance(initialLoanAmount);
			} else {
				currentMonth.setPayment(calculatePayment());
				currentMonth.setInterestPaid(calculatePaymentTowardsInterest(schedule.get(t-1).getRemainingBalance()));
				currentMonth.setPrincipalPaid(calculatePaymentTowardsPrincipal(schedule.get(t-1).getRemainingBalance()));
				currentMonth.setTotalInterestPaid(schedule.get(t-1).getTotalInterestPaid() + currentMonth.getInterestPaid());
				currentMonth.setRemainingBalance(schedule.get(t-1).getRemainingBalance() - currentMonth.getPrincipalPaid());
			}
			
			schedule.add(currentMonth);
		}
	}
}
