package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Entity.*;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


/**principalTechClassReport - Use for the principal to generate a report.
 * @author Tal Asulin
 * */
public class principalTechClassReport implements Initializable{
    
	/**nextBtn - generate by FXML - next button.*/
	@FXML
    private Button nextBtn;
	
	/**className - Class name that was selected by the principal.*/
    private String className;
    
    /**semOlist - list of semesters that will be presented to the principal.*/
    private ObservableList<Statistical> semOlist;   
    
    /**semaddlist  - list of semesters that was choosen by the principal.*/
    private ObservableList<Statistical> semaddlist = FXCollections.observableArrayList();
    
    /**corrddlist - list of courses that will be added to the report.*/
    private ObservableList<Statistical> corrddlist = FXCollections.observableArrayList();
    
    /**courrList - list of courses that holding the statistical data that will be pass to the report controller.*/
    private ArrayList<Statistical> courrList = new ArrayList<Statistical>();
    
    
    /**classList -list of classes the will be converted from the array list.*/
    private ObservableList<String> classList;
    
    /**classCbox - classes combobox.*/
    @FXML
    private ComboBox<String> classCbox;
    

    /**courseDetTable - Table which contain all the information about the classes that will be presented on the report.*/
    @FXML
    private TableView<Statistical> courseDetTable;

    /**semT1Year - Column of semester year that contain the list of the relevant semesters.*/
    @FXML
    private TableColumn<Statistical, String> semT1Year;

    /**semT1Type - Column of semester type that contain the list of the relevant semesters.*/
    @FXML
    private TableColumn<Statistical, String> semT1Type;

    /**semT2Type - Column of semester type that contain the list of the relevant semesters.*/
    @FXML
    private TableColumn<Statistical, String> semT2Type;

    /**semT2Year - Column of semester year that contain the list of the relevant semesters.*/
    @FXML
    private TableColumn<Statistical, String> semT2Year;

    /**courIDLabel - Column of course that will present in the courses table.*/
    @FXML
    private TableColumn<Statistical, String> courIDLabel;
    
    /**courNameLabel - Column of courses name that will present in the courses table.*/
    @FXML
    private TableColumn<Statistical, String> courNameLabel;

    /**yearLabel - Column of year that will present in the course table.*/
    @FXML
    private TableColumn<Statistical, String> yearLabel;

    /**typeLabel - Column of semester type that will be present in the courses table.*/
    @FXML
    private TableColumn<Statistical, String> typeLabel;
    
    /**sem1Table - Semester table that will present the current semesters.*/
    @FXML
    private TableView<Statistical> sem1Table;

    /**sem2Table - Semester table that will present the choosen semesters.*/
    @FXML
    private TableView<Statistical> sem2Table;
    
    
    /**initialize() - will load the relevant details on the screen.*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		sem1Table.setPlaceholder(new Label(""));
		sem2Table.setPlaceholder(new Label(""));
		courseDetTable.setPlaceholder(new Label(""));
		
		semT1Type.setCellValueFactory(new PropertyValueFactory<Statistical, String>("semName"));
		semT1Year.setCellValueFactory(new PropertyValueFactory<Statistical, String>("semYear"));	
		semT2Type.setCellValueFactory(new PropertyValueFactory<Statistical, String>("semName"));
		semT2Year.setCellValueFactory(new PropertyValueFactory<Statistical, String>("semYear"));
		
		courIDLabel.setCellValueFactory(new PropertyValueFactory<Statistical, String>("courseID"));	
		courNameLabel.setCellValueFactory(new PropertyValueFactory<Statistical, String>("courseName"));	
		yearLabel.setCellValueFactory(new PropertyValueFactory<Statistical, String>("semYear"));	
		typeLabel.setCellValueFactory(new PropertyValueFactory<Statistical, String>("semName"));
		
		
		
		classList= FXCollections.observableArrayList(Statistical.getClasses());
		classCbox.setItems(classList);
		
		classCbox.valueProperty().addListener(new ChangeListener<String>() {
    		@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
    			setSemesters();
    			}
			});
	}
    
	
	
    /**setSemesters() - will load the relevant semesters when the class will be selected from the combobox.*/
	public void setSemesters()
	{
		className= classCbox.getSelectionModel().getSelectedItem().toString();
		courrList.clear();
		corrddlist.clear();
		semaddlist.clear();
		courrList = Statistical.getCoursesDetailsByClass(className);
		ArrayList<Statistical> semList = Statistical.getSemListByClass(className);
		semOlist = FXCollections.observableArrayList(semList);
		sem1Table.setItems(semOlist);
	}
	
	/**RemoveSem() - event that removes semester from the choosen list.*/
    @FXML
    void RemoveSem(MouseEvent event) {
    	
    	if(sem2Table.getSelectionModel().getSelectedItem()!=null){
	    	Statistical s = sem2Table.getSelectionModel().getSelectedItem();
	    	semaddlist.remove(s);
	    	sem2Table.setItems(semaddlist);
	    	
    		for(int i=0 ; i<courrList.size() ; i++)
    			if(s.getSemYear().equals(courrList.get(i).getSemYear()) && s.getSemName().equals(courrList.get(i).getSemName()))
    				if(corrddlist.contains(courrList.get(i)))
    					corrddlist.remove(courrList.get(i));	    					
    		
    		courseDetTable.setItems(corrddlist);
	    	
	    	
	    	if(semaddlist.size() == 0)
	    		nextBtn.setDisable(true);
	    	else
	    		nextBtn.setDisable(false);
    	}
    }



    /**selectSem() - event that will add a new semesters to the chosen list.*/
    @FXML
    void selectSem(MouseEvent event) {
    	if(sem1Table.getSelectionModel().getSelectedItem()!=null)
    	{
	    	Statistical s = sem1Table.getSelectionModel().getSelectedItem();
	    	if(!semaddlist.contains(s)){
	    		semaddlist.add(s);
	    		sem2Table.setItems(semaddlist);
	    		
	    		for(int i=0 ; i<courrList.size() ; i++)
	    			if(s.getSemYear().equals(courrList.get(i).getSemYear()) && s.getSemName().equals(courrList.get(i).getSemName()))
	    				if(!corrddlist.contains(courrList.get(i)))
	    					corrddlist.add(courrList.get(i));	    					
	    		
	    		courseDetTable.setItems(corrddlist);
	    		
		    	if(semaddlist.size() == 0)
		    		nextBtn.setDisable(true);
		    	else
		    		nextBtn.setDisable(false);
	    	}
	    	else
	    		showErrorMSG("Semester alredy selected.","Semester '"+s.getSemYear() + ""  + s.getSemName()+"' already selected.");
    	}
    }
    

    /**showReport() - will pass the argument to the controller that responsible to generate the report.*/
    @FXML
    void showReport(ActionEvent event) {
    	try {
	    		PrincGenClassReport.initializeChart("Class " + className, "Semsesters", "Grade");
	    		PrincGenClassReport.setCourseList(corrddlist,1);
			   URL paneOneUrl = getClass().getResource("/FXML/PrincTeachClassReport.fxml");
			   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
			   BorderPane border = Main.getRoot();			    
			   border.setCenter(paneOne);
    		
        } catch (IOException exp) {
        	exp.printStackTrace();
          }   
    }
    
    
    /**showErrorMSG() - will present an error/warning message to the principal.
     * @param title - message title
     * @param MSG - message content
     * */
    private void showErrorMSG(String title, String MSG) {
		
  		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error!");
		alert.setHeaderText(title);
		alert.setContentText(MSG);
		alert.showAndWait();
		
	}

}
