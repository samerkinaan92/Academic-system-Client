
	package Controller;

	import java.awt.Button;
import java.awt.Label;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

	public class PRCPL_ViewSystemDataController implements Initializable{

	    @FXML
	    private Label Label1;

	    @FXML
	    private ComboBox<String> dataCB;

	    @FXML
	    private Button selectBtn;
	  
	    @FXML
	    void clickSelect(ActionEvent event) {
	    
	    }
	    
	    
	    private final String[] dataType = {"Classes Of Teacher",
	    								   "Teachers Of Class",
	    								   "Courses Of Teacher"};
	    
	    private final ObservableList<String> boxDataType = FXCollections.observableArrayList(dataType);

		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			dataCB.setItems(boxDataType);
		}
	    

	}

