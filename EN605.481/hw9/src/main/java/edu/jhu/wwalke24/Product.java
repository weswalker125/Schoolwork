package edu.jhu.wwalke24;

import java.text.NumberFormat;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

public class Product implements Rowable {
	private final static NumberFormat dollarFormatter = NumberFormat.getCurrencyInstance();
	private final static String[] columnNames =  new String[] { "ID", "Name", "Price", "Description" };
	
	@Getter
	private String id;
	
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private double price;
	
	@Getter @Setter
	private String description;
	
	public Product() {
		id = UUID.randomUUID().toString().substring(0, 5);
	}

	public String getPriceText() {
		return dollarFormatter.format(price);
	}
	
	public String[] getColumnNames() {
		return columnNames;
	}
	
	public ObservableList<String> toRow() {
		ObservableList<String> ret = FXCollections.observableArrayList();
		ret.add(this.id);
		ret.add(this.name);
		ret.add(String.valueOf(this.price));
		ret.add(this.description);
		return ret;
	}
	
	public void copy(Product e) {
		this.setName(e.name);
		this.setPrice(e.price);
		this.setDescription(e.description);
		this.id = e.getId();
	}
}
