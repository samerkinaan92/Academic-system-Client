package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Entity.Course;
import Entity.TeachingUnit;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SystemManegerDefineCourseController implements Initializable {
	
	@FXML
    private Label nameErr;

    @FXML
    private Label idErr;

    @FXML
    private Label hoursErr;

    @FXML
    private Label unitErr;
	
    @FXML
    private ListView<String> courseList;
    
    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> weaklyHours;

    @FXML
    private ComboBox<String> teachingUnit;

    @FXML
    private TextField courseId;

    @FXML
    private ListView<String> preCourseList;

    @FXML
    private TextField courseName;
    
    //-------------------------------------------------------------------------------------------------------------------
    
    private final String[] hours = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    
    ArrayList<Course> courseArr;
    
    ArrayList<String> organizedList;
    ArrayList<String> PreList = new ArrayList<String>();
    
    Course newCourse;
    boolean err;
    
    private final ObservableList<String> boxHours = FXCollections.observableArrayList(hours);
    private ObservableList<String> coursesDB;
    
    //-------------------------------------------------------------------------------------------------------------------
    
    public void search(ActionEvent e){ // Filter course list.
    	ArrayList<String> temp = new  ArrayList<String>();
    	
    	for (int i = 0; i < organizedList.size(); i++)
    		if (organizedList.get(i).toLowerCase().contains(searchField.getText().toLowerCase()))
    			temp.add(organizedList.get(i));
    	
    	coursesDB  = FXCollections.observableArrayList(temp);
		courseList.setItems(coursesDB);
    }
    
    public void addPreCourse(ActionEvent e){ // Add Course to PreCourse list.
    	
    	for (int i = 0; i < PreList.size(); i++)
    		if (courseList.getSelectionModel().getSelectedItem().equals(PreList.get(i)))
    			return;
    	
    	PreList.add(courseList.getSelectionModel().getSelectedItem());
    	Collections.sort(PreList);
    	preCourseList.setItems(FXCollections.observableArrayList(PreList));
    }
    
    public void removePreCourse(ActionEvent e){ // Remove Course from PreCourse list.
    	
    	for (int i = 0; i < PreList.size(); i++)
    		if (preCourseList.getSelectionModel().getSelectedItem().equals(PreList.get(i)))
    			PreList.remove(i);
    	
    	Collections.sort(PreList);
    	preCourseList.setItems(FXCollections.observableArrayList(PreList));	
    }

    private ArrayList<String> getList(ArrayList<Course> courseArr){ // Get courses names & ID's for list.
    	
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < courseArr.size(); i++)
			temp.add(courseArr.get(i).getName() + " (" + courseArr.get(i).getCourseID() + ")");
		
		return temp;	
	}
    
    public void submitCourse(ActionEvent e) throws IOException{ //  Submit course for check & insert.
    	  	
    	if (err){ // Reset Screen errors.
	    	idErr.setText("");
	    	nameErr.setText("");
	    	hoursErr.setText("");
	    	unitErr.setText("");
    	}
    	
    	if (checkIlegalInput())
    		return;
    	
    	if (!Course.checkLegal(courseId.getText())){
    		idErr.setText("Course ID already exists!");
    		return;
    	}
    	
    	newCourse = new Course(Integer.parseInt(courseId.getText()), courseName.getText(),
    			Integer.parseInt(weaklyHours.getSelectionModel().getSelectedItem().toString()),
    			teachingUnit.getSelectionModel().getSelectedItem().toString());
    	
    	newCourse.insertCourse();
    	newCourse.updatePreCourses(PreList);
    	
    	int numOfChanges = (int)Main.client.getMessage();
    	
    	if (numOfChanges >= 1){
	    	JOptionPane.showMessageDialog(null,"Course definition successful :)");
	    	clearScreen();
    	}
    	else
    		JOptionPane.showMessageDialog(null, 
					  "No changes were made!", "Error", JOptionPane.ERROR_MESSAGE);
    }
	
	private boolean checkIlegalInput(){ // Check for illegal input.
		err = false;
		
		try{
    		Integer.parseInt(courseId.getText());
    		if (courseId.getText().isEmpty())
    			throw new Exception();
    	}
    	catch(Exception exp1){
    		idErr.setText("Course Id not valid!");
    		err = true;
    	}
    	
    	if (courseName.getText().isEmpty()){
    		nameErr.setText("Course Name not valid!");
    		err = true;
    	}
    	
    	if (weaklyHours.getSelectionModel().getSelectedItem() == null){
    		hoursErr.setText("Weakly Hours not selected!");
    		err = true;
    	}
    	
    	if (teachingUnit.getSelectionModel().getSelectedItem() == null){
    		unitErr.setText("Teaching Unit not selected!");
    		err = true;
    	}
    	
    	return err;
	}
	
	private void clearScreen() throws IOException{ // Clear & update screen details.
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/SystemManagerDefineCourses.fxml"));;
		Main.getRoot().setCenter(pane);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){ // Initialize window.
		courseArr = Course.getCourses();
		organizedList = getList(courseArr);
		Collections.sort(organizedList);
		courseList.setItems(FXCollections.observableArrayList(organizedList));
		ArrayList<String> temp;
		if ((temp = TeachingUnit.getTeachingUnit()) != null)
			teachingUnit.setItems(FXCollections.observableArrayList(temp));
		else
			teachingUnit.setItems(FXCollections.observableArrayList("No Units"));
			
		weaklyHours.setItems(boxHours);	
	}
}
