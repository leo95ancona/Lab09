package it.polito.tdp.metrodeparis;

	import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.metrodeparis.model.Fermata;
import it.polito.tdp.metrodeparis.model.Model;
import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.scene.control.Button;
	import javafx.scene.control.ComboBox;
	import javafx.scene.control.TextField;

	public class MetroDeParisController {

		Model model ;
		
	    @FXML
	    private ResourceBundle resources;

	    @FXML
	    private URL location;

	    @FXML
	    private ComboBox<Fermata> boxPartenza;

	    @FXML
	    private ComboBox<Fermata> boxDestinazione;

	    @FXML
	    private Button bttnCalcola;
	    
	    @FXML
	    private Button btnRaggiungibili;

	    @FXML
	    private TextField txtArea;

	    @FXML
	    void calcola(ActionEvent event) {
	    	Fermata partenza = boxPartenza.getValue();
	    	Fermata destinazione = boxDestinazione.getValue();
	    	if (destinazione==null){
	    		txtArea.appendText("Errore, seleziona una destinazione");
	    		return;
	    	}
	    	String percorso = model.calcolaPercorsoMinimo(partenza,destinazione);
	    	
	    	txtArea.appendText(percorso);

	    }
	    
	    @FXML
	    void handleRaggiungibili(ActionEvent event) {
	    	Fermata partenza = boxPartenza.getValue();
	    	if (partenza==null){
	    		txtArea.appendText("Errore, seleziona una partenza");
	    		return;
	    	}
	    	
	    	List<Fermata> raggiungibili = model.getRaggiungibili(partenza);
	    	
	    	txtArea.appendText("Destinazioni caricate correttamente \n");
	    	
	    	boxDestinazione.getItems().clear();
	    	boxDestinazione.getItems().addAll(raggiungibili);
	    }

	    @FXML
	    void initialize() {
	        assert boxPartenza != null : "fx:id=\"boxPartenza\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
	        assert boxDestinazione != null : "fx:id=\"boxDestinazione\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
	        assert bttnCalcola != null : "fx:id=\"bttnCalcola\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
	        assert btnRaggiungibili != null : "fx:id=\"btnRaggiungibili\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
	        assert txtArea != null : "fx:id=\"txtArea\" was not injected: check your FXML file 'MetroDeParis.fxml'.";

	    }

	
	public void setModel(Model model) {
		this.model=model;
		
		boxPartenza.getItems().addAll(this.model.getStazioni());
		
	}
	
}
