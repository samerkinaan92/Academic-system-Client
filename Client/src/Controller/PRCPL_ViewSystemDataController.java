
	package Controller;

	import java.awt.Button;
import java.awt.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

	public class PRCPL_ViewSystemDataController implements Initializable{

	    @FXML
	    private Label Label1;

	    @FXML
	    private ComboBox<String> dataCB;

	    @FXML
	    private Button selectBtn;
	  
	    @FXML
	    private Label res;
	    
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			dataCB.setItems(boxDataType);
		}
	  
	    @FXML
	    void clickSelect(ActionEvent event) throws IOException {
	    	//if (((dataCB.getSelectionModel().getSelectedItem()).equals("Classes Of Teacher")))
	    	
	    	if (dataCB.getValue().equals("Classes Of Teacher")) 
	    		res.setText("you Chose 1!");
	    	if (dataCB.getSelectionModel().equals("Teachers Of Class")) 
    			res.setText("you Chose 2!");
	    	if (dataCB.getValue().equals("Courses Of Teacher")) 
    			res.setText("you Chose 3!");
	    	else res.setText("PROBLEM!");
	    
	    }
	    
	    
	    private final String[] dataType = {"Classes Of Teacher",
	    								   "Teachers Of Class",
	    								   "Courses Of Teacher"};
	    
	    private final ObservableList<String> boxDataType = FXCollections.observableArrayList(dataType);

	
	    

	}

