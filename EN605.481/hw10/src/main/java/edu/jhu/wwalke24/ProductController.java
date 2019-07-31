package edu.jhu.wwalke24;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ProductController {
	
	@FXML private TextField productNameText;
	@FXML private TextField productPriceText;
	@FXML private TextArea productDescriptionText;
	
	private Product product;
	
	@FXML
	protected void handleAddProductOkButton(ActionEvent event) {
		System.out.println("OK");
		
		// Gather Person details
		Product p = new Product();
		p.setName(this.productNameText.getText());
		p.setPrice(Double.parseDouble(this.productPriceText.getText().replace("$", "").replace(",", "")));
		p.setDescription(this.productDescriptionText.getText());
				
		MainController.addProduct(p);
		
		// Close this window
		((Stage) ((Node)(event.getSource())).getScene().getWindow()).close();
	}
	
	@FXML
	protected void handleAddProductCancelButton(ActionEvent event) {
		System.out.println("Cancel");
		
		// Close this window
		((Stage) ((Node)(event.getSource())).getScene().getWindow()).close();
	}
	
	public void setProduct(Product p) {
		this.product = p;
		this.productDescriptionText.setText(p.getDescription());
		this.productNameText.setText(p.getName());
		this.productPriceText.setText(p.getPriceText());
	}
}
