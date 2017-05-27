package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Entity.Course;
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
    
    private final String[] hours = {"1", "2", "3", "4"};
    private final String[] units = {"Software", "Mathematics", "Physics", "English"};
    
    ArrayList<Course> courseArr;
    
    ArrayList<String> organizedList;
    ArrayList<String> PreList = new ArrayList<String>();
    
    Course newCourse;
    boolean err;
    
    private final ObservableList<String> boxUnit = FXCollections.observableArrayList(units);
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
    			teachingUnit.getSelectionModel().getSelectedItem().toString(), 
    			Integer.parseInt(weaklyHours.getSelectionModel().getSelectedItem().toString()));
    	
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
    	
    	//---------------------------------------------------------------------------------------------------------
    	/*
    	// Verify course id in data base.
    	
    	if (checkId(courseId.getText())){
    		idErr.setText("Course ID already exists!");
    		return;
    	}
    	*/
    	//--------------------------------------------------------------------------------------------------------------
    	
    	/*
    	// Update course in data base. 
    	
    	String msg = "Insert INTO course (CourseID, Name, TUID, weeklyHours)";
    	String values = " VALUES (" + Integer.parseInt(courseId.getText()) + ", '" + courseName.getText() + "', '" + 
    			teachingUnit.getSelectionModel().getSelectedItem().toString() + "', " + 
    			weaklyHours.getSelectionModel().getSelectedItem().toString() + ")";
    	
    	msgServer = new HashMap <String,String>();
    	msgServer.put("msgType", "insert");
		msgServer.put("query", msg + values);
    	
    	try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				System.out.println("Server fatal error!");
			}
		MessageThread msgT = new MessageThread(Main.client);
		msgT.start();
		synchronized (msgT){try {
			msgT.wait();
		} catch (InterruptedException exp) {
			exp.printStackTrace();
		}}
    	
		*/
    	
    	
    	//------------------------------------------------------------------------------------------------------------
    	/*
    	
    	// Update Precourses in data base.
    	
    	for (int i = 0; i < PreList.size(); i++){
    	
	    	msg = "Insert INTO PreRequests (CourseID, preReqCourseID)";
	    	values = " VALUES (" + Integer.parseInt(courseId.getText()) + ", " + 
	    	PreList.get(i).substring(PreList.get(i).indexOf('(') + 1, PreList.get(i).indexOf(')')) + ")";
	    	
	    	
	    	msgServer = new HashMap <String,String>();
	    	msgServer.put("msgType", "insert");
			msgServer.put("query", msg + values);
	    	
	    	try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					System.out.println("Server fatal error!");
				}
			msgT = new MessageThread(Main.client);
			msgT.start();
			synchronized (msgT){try {
				msgT.wait();
			} catch (InterruptedException exp) {
				exp.printStackTrace();
			}}
    	}
    	*/
    	//--------------------------------------------------------------------------------------------------------------
    	
    //	numOfChanges += (int)Main.client.getMessage();
    	
    	
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
		weaklyHours.setItems(boxHours);
		teachingUnit.setItems(boxUnit);	
	}
}
