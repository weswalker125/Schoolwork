package edu.jhu.wwalke24;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import edu.jhu.wwalke24.PersonController.PersonType;

@SuppressWarnings("deprecation")
public class MockUtil {
	public static Person mockPerson(PersonType t) {
		Person p = new Person();
		p.setCity(RandomStringUtils.randomAlphabetic(5, 12));
		p.setState(states[RandomUtils.nextInt(0, states.length - 1)]);
		p.setStreetAddress(String.format("%s %s st", RandomUtils.nextInt(), RandomStringUtils.randomAlphabetic(3, 15)));
		p.setZipcode(RandomStringUtils.randomNumeric(5));
		p.setGender(genders[RandomUtils.nextInt(0, 2)]);
		p.setFirstName(RandomStringUtils.randomAlphabetic(3, 10));
		p.setLastName(RandomStringUtils.randomAlphabetic(5, 12));
		if (t == PersonType.CUSTOMER) {
			p.setCustomer(true);
		}
		return p;
	}
	
	public static Product mockProduct() {
		Product p = new Product();
		p.setName(RandomStringUtils.randomAlphabetic(3, 10));
		p.setPrice(RandomUtils.nextInt(1, 300));
		p.setDescription(generateSentence());
		return p;
	}
	
	public static String generateSentence() {
		String ret = "";
		int wordCount = RandomUtils.nextInt(5, 20);
		
		for (int i = 0; i < wordCount; ++i) {
			ret += " " + RandomStringUtils.randomAlphabetic(1, 10);
		}
		
		return ret + ".";
	}
	
	public static String[] genders = { "Male", "Female" }; 
	
	public static String[] states = { "Alabama",
			"Alaska",
			"Arizona",
			"Arkansas",
			"California",
			"Colorado",
			"Connecticut",
			"District of Columbia",
			"Delaware",
			"Florida",
			"Georgia",
			"Hawaii",
			"Idaho",
			"Illinois",
			"Indiana",
			"Iowa",
			"Kansas",
			"Kentucky",
			"Louisiana",
			"Maine",
			"Maryland",
			"Massachusetts",
			"Michigan",
			"Minnesota",
			"Mississippi",
			"Missouri",
			"Montana",
			"Nebraska",
			"Nevada",
			"New Hampshire",
			"New Jersey",
			"New Mexico",
			"New York",
			"North Carolina",
			"North Dakota",
			"Ohio",
			"Oklahoma",
			"Oregon",
			"Pennsylvania",
			"Rhode Island",
			"South Carolina",
			"South Dakota",
			"Tennessee",
			"Texas",
			"Utah",
			"Vermont",
			"Virginia",
			"Washington",
			"West Virginia",
			"Wisconsin",
			"Wyoming" };
	
	public enum Models {
		CUSTOMER, EMPLOYEE, MERCHANDISE
	}
}
