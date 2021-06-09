/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.DirectorConPeso;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	if (boxAnno.getValue()== null) {
    		txtResult.appendText("Devi Selezionare un anno!!");
    	} else {
    		txtResult.clear();
    		this.model.creaGrafo(boxAnno.getValue());
    		
    		txtResult.appendText("#Vertici: " +this.model.getVertici().size()+"\n");
    		txtResult.appendText("#Archi: " +this.model.getArchi());
    	}

    	this.boxRegista.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	txtResult.clear();
    	if(this.model.getVertici().size()==0 || boxRegista.getValue()== null) {
    		txtResult.appendText("Devi creare il grafo prima o selezioanre regista!!");
    	} else {
    		txtResult.clear();
    		List<DirectorConPeso> res =this.model.getViciniConPeso1(boxRegista.getValue());
    		
    		if(res.size()==0) {
    			txtResult.appendText("Il Regista non ha attori in comune\n");
    		}
    		
    		for (DirectorConPeso d : res ) {
    			txtResult.appendText("Regista: "+d.getDirector().getFirstName()+" "+d.getDirector().getLastName()+" con "+d.getPeso()+" attori in comune\n");
    		}
    		
    	}
    	
    	

    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	
    	try {
    	int numeroAttori = Integer.parseInt(txtAttoriCondivisi.getText());
    	
    	
    	Director partenza = this.boxRegista.getValue();
    	
    	List<DirectorConPeso> res = this.model.getCammino(partenza, numeroAttori);
    	
    	txtResult.clear();
    	for (DirectorConPeso d : res) {
    		txtResult.appendText("Cammino dei Registi:\n");
    		txtResult.appendText("Regista: "+d.getDirector().getFirstName()+" "+d.getDirector().getLastName()+"\n");
    	}
    	
    	   	
    	} catch(NumberFormatException e) {
    		txtResult.clear();
    		txtResult.appendText("Devi inserire un numero");
    	}
    	
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        setComboAnni();
    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	
    }
   
   public void setComboAnni() {
	   for (int i=2004; i<2007; i++) {
		   this.boxAnno.getItems().add(i);
	   }
   }
    
}
