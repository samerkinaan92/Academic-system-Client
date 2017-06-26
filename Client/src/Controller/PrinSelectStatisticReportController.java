package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


/**PrinSelectStatisticReportController - controller for PrincipalViewStatisticReport.fxml
 * Controller for the principal use for choose the type of the report.
 * @author Tal Asulin
 * */
public class PrinSelectStatisticReportController implements Initializable{

	/**dataType - contain the type of reports that the principal can choose.*/
    private final String[] dataType = {"Courses of Class","Classes of teacher","Teachers of class"};

    /**boxDataType - list of options for the combobox.*/
    private final ObservableList<String> boxDataType = FXCollections.observableArrayList(dataType);

    /**selctBtn - select button that will transfer to the right controller according to the report selection.*/
    @FXML // fx:id="selctBtn"
    private Button selctBtn; // Value injected by FXMLLoader
        
    /**selectedOption - selected option of the principal from the combobox.*/
    private String selectedOption;

    /**repoCbox - combobox which contain all the type of report selections.*/
    @FXML // fx:id="repoCbox"
    private ComboBox<String> repoCbox; // Value injected by FXMLLoader
    
    /**initialize() - will intialize the screen on load.*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		repoCbox.setItems(boxDataType);
		
		repoCbox.valueProperty().addListener(new ChangeListener<String>() {
    		@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
    			
    			if(repoCbox.getSelectionModel().getSelectedItem().toString()!=null){
    				selctBtn.setDisable(false);
    				selectedOption=repoCbox.getSelectionModel().getSelectedItem().toString();
    			}
			}});
	}


	/**selectReport() - Event that will transfer the principal to the right controller according to his selection.*/
    @FXML
    void selectReport(ActionEvent event) { 
    	
    	try {
    		
        	switch (selectedOption){
        	
	        	case "Courses of Class":
	        	{
	    			URL paneOneUrl= getClass().getResource("/FXML/PrinTeacherClassesSelection.fxml");
	    			AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
	    			BorderPane border = Main.getRoot();			    
	    			border.setCenter(paneOne);
	        		break;
	        	}
	        	case "Classes of teacher":
	        	{
	    			URL paneOneUrl= getClass().getResource("/FXML/PrinClassTeachSelection.fxml");
	    			AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
	    			BorderPane border = Main.getRoot();			    
	    			border.setCenter(paneOne);
	        		break;
	        	}
	        	case "Teachers of class":
	        	{
	    			URL paneOneUrl= getClass().getResource("/FXML/PrinTeachSemReport.fxml");
	    			AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
	    			BorderPane border = Main.getRoot();			    
	    			border.setCenter(paneOne);
	    			break;	
	        	}
	        	
        	}
        	} catch (IOException ex) {
            ex.printStackTrace();
          }
    	
    	

    	
    	}
    


	
}
