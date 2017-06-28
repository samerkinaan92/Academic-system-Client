package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import Entity.ParentStudent;
import Entity.Semester;
import Entity.Parent;
import Entity.claSS;
import Entity.Student_Class;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * This controller is to select a parent to get the parent permissions and general data.
 */
public class PrincipalViewParInfo implements Initializable{

	/**classArr - is used for displaying the classes list*/
    public static ArrayList<claSS> classArr;
    
    /**parArr - is used for storing the parent permissions for student.*/
    public static ArrayList<ParentStudent> parArr;
    
    /**parArrdata - is used for storing the parent information.*/
    public static ArrayList<Parent> parArrdata;
    
    /**studentArr - is used for storing the student in specific class.*/
    public static ArrayList<Student_Class> studentArr;
    
    /**organizedListClass - list of classes for the table view.*/
    public ArrayList<String> organizedListClass;
    
    /**organizedListStudent - list of students for the table view.*/
    public ArrayList<String> organizedListStudent;
    
    /**stdForTable - converting array list to ObservableList of students for the table view.*/
	private final ObservableList<Student_Class> stdForTable = FXCollections.observableArrayList();
	
	/**parForTable - converting array list to ObservableList of parents for the table view.*/
	private final ObservableList<Parent> parForTable = FXCollections.observableArrayList();
	
	/**classListCB - list of classes for the classes combo box*/
    private ObservableList<String> classListCB;
    
    /**par - use for future initialize of parent delete permissions controller*/
    public static Parent par;
    
    /**stdLabel - label to describe the student table*/
    @FXML
    private Label stdLabel;
    
    /**stdLabel - label to describe the parent table*/
    @FXML
    private Label parLabel;
    
    /**infoBtn - Button that the user presse in order to get the parent information.*/
    @FXML
    private Button infoBtn;
    
    /**listPane - Main screen pane.*/
    @FXML
    private Pane listPane;
    
    /**classList - List of classes for the selection combobox.*/
    @FXML // fx:id="classList"
    private ComboBox<String> classList; // Value injected by FXMLLoader

    /**listPane - Main screen pane.*/
    @FXML
    private TableView<Student_Class> stdTable;

    /**IDcolumn - Use for the student ID column.*/
    @FXML
    private TableColumn<Student_Class,String> IDcolumn;

    
    /**nameColumn - Use for the student name column.*/
    @FXML
    private TableColumn<Student_Class,String> nameColumn;
    
    /**noStudMsg - Use for presenting error message when there are no students in class.*/
    @FXML // fx:id="noStudMsg"
    private Label noStudMsg; // Value injected by FXMLLoader
    
    
    /**parTable - Parent table.*/
    @FXML
    private TableView<Parent> parTable;

    /**parIDcolumn - Parent ID column.*/
    @FXML
    private TableColumn<Parent, String> parIDcolumn;

    /**nameParColumn - Parent name column.*/
    @FXML
    private TableColumn<Parent, String> nameParColumn;
    
    
    /**initialize() is used to intilize the information screen.*/
    /*Initilizing the classes combobox*/
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	classArr = claSS.getClasses();	
    	organizedListClass = getListClass(classArr);	
		Collections.sort(organizedListClass);
		classListCB= FXCollections.observableArrayList(organizedListClass);
		classList.setItems(classListCB);
		IDcolumn.setCellValueFactory(new PropertyValueFactory<Student_Class, String>("StudentID"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Student_Class, String>("StudName"));
		parIDcolumn.setCellValueFactory(new PropertyValueFactory<Parent, String>("ID"));
		nameParColumn.setCellValueFactory(new PropertyValueFactory<Parent, String>("Name"));
	} 
    
    
    /**showPar() - presenting the parents for selected student.*/
    @FXML
    void showPar(MouseEvent event) {
    	
    	Student_Class std;
    	if((std=stdTable.getSelectionModel().getSelectedItem())!=null){
	    	parArr=ParentStudent.getParentsByStud(std.getStudentID(),std.getStudName());
	    	parForTable.clear();
	    	parLabel.setText("Parents of "+std.getStudName()+":");    	
	    	for(int i = 0 ; i < parArr.size() ; i++ )
	    			parForTable.add(parArr.get(i).getPar());
	    	
	    	parTable.setItems(parForTable);		
	    	infoBtn.setDisable(true);
	    	}
    }
    
    /**paneEvent() - Disable the information button */
    @FXML
    void paneEvent(MouseEvent event) {
    	infoBtn.setDisable(true);
    }
    /**paneEvent() - Enable the information button */
    @FXML
    void parSelect(MouseEvent event) {
    	infoBtn.setDisable(false);
    }
    

    /**showStud() - presenting the students for selected class.*/
    @FXML
    public void showStud(ActionEvent event) {
    	noStudMsg.setVisible(false);
    	
    	String classSelection;
    	if((classSelection= classList.getSelectionModel().getSelectedItem().toString())!=null){
	    	studentArr=Student_Class.getStudentsByClass(classSelection); // getting students by the selected class
	    	
	    	if(studentArr.isEmpty())
	    	{
	    		noStudMsg.setVisible(true);
	    		listPane.setVisible(false);
	    	}else{
	    	infoBtn.setDisable(true);
	    	parTable.setPlaceholder(new Label("Please select student"));
	    	stdForTable.clear();
	    	parForTable.clear();
	    	parTable.setItems(parForTable);
	    	listPane.setVisible(true);
	    	stdLabel.setText("Students of class "+classSelection+":");
	    	parLabel.setText("Parents of class "+classSelection+":");    	
	    	for(int i = 0 ; i < studentArr.size() ; i++ )
	    			stdForTable.add(new Student_Class(studentArr.get(i).getStudentID() , studentArr.get(i).getStudName(),"0"));
	    	stdTable.setItems(stdForTable);		
			}
    	}	
    }

    
    /**viewInfo() - is used to present the parent information and initialize that relevant data in PrincipalDeletePermissions screen*/
    @FXML
    void viewInfo(ActionEvent event) {
    	
    	if((par=parTable.getSelectionModel().getSelectedItem())!=null){
    	
	    	try {
	    		setFields(); // setting fields in the new screen
			   URL paneOneUrl = getClass().getResource("/FXML/PrincipalDeletePermissions.fxml");
			   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
			   BorderPane border = Main.getRoot();			    
			   border.setCenter(paneOne);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
    	}
    }


    /**getListClass() = is used for creating array list of classes name*/
    private ArrayList<String> getListClass(ArrayList<claSS> classArr){ 
    	
		ArrayList<String> temp = new ArrayList<String>();
		
		for (int i = 0; i < classArr.size(); i++)
				temp.add(classArr.get(i).getClassName());
		
		return temp;	
	}
  
    /**backBtn() - event that incharge of returning to the back screen.*/
    @FXML
    void backBtn(ActionEvent event) {
    	try {
		   URL paneOneUrl = getClass().getResource("/FXML/WelcomeScreen.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException e) {
            e.printStackTrace();
          }      
    }
    
    
    /**setFields() - initialize fields in the new controller*/
    public void setFields(){
    	
    	Parent par;
    	par=parTable.getSelectionModel().getSelectedItem(); 	
    	PrincipalDeletePermissionsController.setPar(par);
    }
    
    
    @FXML
    void ViewPerInfo(ActionEvent event) {
    	try {
		   URL paneOneUrl = getClass().getResource("/FXML/UserViewPersonalInfo.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException exp) {
        	exp.printStackTrace();
          }       
    }
    


    

}
