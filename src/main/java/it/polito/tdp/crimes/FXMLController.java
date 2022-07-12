/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.crimes.model.District;
import it.polito.tdp.crimes.model.Model;
import it.polito.tdp.crimes.model.Vicino;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;
	Graph<District, DefaultWeightedEdge> grafo;

	private boolean grafoCreato = false;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	Integer anno = this.boxAnno.getValue();
    	if(anno == null) {
    		txtResult.setText("seleziona un anno plis");
    	
    	}else {
    		this.model.creaGrafo(anno);
    		
    		this.grafo = this.model.getGrafo();
    		this.grafoCreato = true;
    		
    		txtResult.setText("GRAFO CREATO");
    		txtResult.appendText("\nvertici: "+this.grafo.vertexSet().size());
    		txtResult.appendText("\narchi: "+this.grafo.edgeSet().size());
    		
    		for(District d : this.grafo.vertexSet()) {
    			txtResult.appendText("\n\nVICINI PER IL DISTRETTO: "+d.getDistrictId());
    			List<Vicino> vicini = new LinkedList<>();
    			for(District v : Graphs.neighborListOf(this.grafo, d)) {
    				Vicino vi= new Vicino(v, this.grafo.getEdgeWeight(this.grafo.getEdge(v, d)));
    				vicini.add(vi);
    			}
    			Collections.sort(vicini);
    			for(Vicino v : vicini) {
    				txtResult.appendText("\n"+v.toString());
    			}
    		}
    		
    		this.boxMese.getItems().clear();
    		for(int i=1; i<=12; i++) {
    			this.boxMese.getItems().add(i);
    		}
    		this.boxGiorno.getItems().clear();
    		for(int i=1; i<=31; i++) {	
    			this.boxGiorno.getItems().add(i);
    		}
    		
    	}
    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	if(this.grafoCreato == false) {
    		txtResult.setText("DEVI PRIMA CREARE IL GRAFO BRO\n");
    	}else {
    		int mese = this.boxMese.getValue();
    		int giorno = this.boxGiorno.getValue();
    		int anno = this.boxAnno.getValue();
    		String n = this.txtN.getText();
    		if(this.boxMese.getValue().equals(null) ||this.boxGiorno.getValue().equals(null) || this.boxAnno.getValue().equals(null) || n.equals("") ) {
    			txtResult.appendText("COMPLETA TUTTI I CAMPI");
    		}else {
    			
    			if(mese == 4 || mese == 6 || mese == 9 || mese == 11) {
    				if(giorno == 31) {
    					txtResult.appendText("\nil giono può essere max 30");
    				}
    			}else if(mese == 2) {
    				if( giorno <28) {
    					txtResult.appendText("\nfebbraio ha solo 28 giorni bro");
    				}
    			}else {
    				txtResult.appendText("\nData valida");
    				
    				try {
    					int agenti = Integer.parseInt(n);
    					if(agenti<1 || agenti>10) {
    						txtResult.appendText("\ninserisci numero compreso tra 1 e 10");
    					}else {
    						
    						this.model.simula(anno, mese, giorno, agenti);
    					}
    					
    				}catch(NumberFormatException e) {
    					e.printStackTrace();
    					txtResult.appendText("\ninserisci solo campi numerici");
    				}
    				
    				
    			}
    		}
    		
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxAnno.getItems().clear();
    	this.boxAnno.getItems().addAll(this.model.getYears());
    }
}
