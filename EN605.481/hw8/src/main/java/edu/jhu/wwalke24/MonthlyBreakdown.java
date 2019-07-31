package edu.jhu.wwalke24;

import java.text.NumberFormat;

import lombok.Getter;
import lombok.Setter;

public class MonthlyBreakdown {
	private final static NumberFormat dollarFormatter = NumberFormat.getCurrencyInstance();
	/**
	 * What payment this is between 1 and the loan term.
	 */
	@Getter @Setter
	private int monthNumber;
	
	/** 
	 * Amount paid for this month.
	 */
	@Getter @Setter
	private double payment;
	
	/** 
	 * Amount of the payment which goes towards the principal.
	 */
	@Getter @Setter
	private double principalPaid;
	
	/**
	 * Amount of the payment which goes towards interest.
	 */
	@Getter @Setter
	private double interestPaid;
	
	/**
	 * Total amount paid towards interest thus far.
	 */
	@Getter @Setter
	private double totalInterestPaid;
	
	/**
	 * Loan balance remaining.
	 */
	@Getter @Setter
	private double remainingBalance;
	
	public String getPaymentString() {
		return dollarFormatter.format(payment);
	}
	
	public String getPrincipalPaidString() {
		return dollarFormatter.format(principalPaid);
	}
	
	public String getInterestPaidString() {
		return dollarFormatter.format(interestPaid);
	}
	
	public String getTotalInterestPaidString() {
		return dollarFormatter.format(totalInterestPaid);
	}
	
	public String getRemainingBalanceString() {
		return dollarFormatter.format(remainingBalance);
	}
}
