package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import Entity.Course;
import Entity.Message;
import Entity.TeachingUnit;
import Entity.User;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

/**
 * This is the controller class for: "SystemManegerDefineCourser.fxml"
 * @author Idan Agam
 * */

public class SystemManegerDefineCourseController implements Initializable {
	
	
	@FXML
    private Label nameErr;

    @FXML
    private Label idErr;

    @FXML
    private Label hoursErr;

    @FXML
    private Label unitErr;
	
    /** ListView for all courses defined in data base */
    @FXML
    private ListView<String> courseList;
    
    /** TextField to filter courses in list */
    @FXML
    private TextField searchField;

    /** ComboBox to select course weakly hours */
    @FXML
    private ComboBox<String> weaklyHours;

    /** ComboBox to select course teaching unit */
    @FXML
    private ComboBox<String> teachingUnit;

    /** TextField for new course id */
    @FXML
    private TextField courseId;

    /** ListView for all added PreCourses for course */
    @FXML
    private ListView<String> preCourseList;

    /** TextField for new course name */
    @FXML
    private TextField courseName;
       
    /** String[] to display all weakly hours */
    private final String[] hours = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    
    /** ArrayList to store all courses from data base	*/
    ArrayList<Course> courseArr;
    
    /** ArrayList to store all courses (name & id)	*/
    ArrayList<String> organizedList;
    
    /** ArrayList to store all PreCourses for new course (name & id)	*/
    ArrayList<String> PreList = new ArrayList<String>();
    
    /** Course object that the new instance will be save in */
    Course newCourse;
    
    boolean err;
    private final ObservableList<String> boxHours = FXCollections.observableArrayList(hours);
    private ObservableList<String> coursesDB;
    Alert infoMsg = new Alert(AlertType.INFORMATION);
	Alert errMsg = new Alert(AlertType.ERROR);
    
    //------------------------------------------------------------------------------------------------------------------------
    
	/**
	 * Send message to all relevant users.
	 */
	private void sendMsgs(){
		
		ArrayList<String> Secretary = User.getUserIdByRole("Secretary");
		ArrayList<String> Principal = User.getUserIdByRole("Principal");
		String title = "New course has created by: " + Main.user.getName();
		String msg = Main.user.getName() + " (" + Main.user.getID() + ") defined new course:\n\n" +
				"Course Id: " + newCourse.getCourseID() + "\n" +
				"Course Name: " + newCourse.getName() + "\n" +
				"Course Weakly Hours: " + newCourse.getWeeklyHours() + "\n" +
				"Course Teaching Unit: " + newCourse.getTUID();
		int from = Integer.parseInt(Main.user.getID());
		
		if (Secretary != null){
			for (int i = 0; i < Secretary.size(); i++){
				Message.sendMsg(new Message(title, msg, from, Integer.parseInt(Secretary.get(i))));
			}
		}
		
		if (Principal != null){
			for (int i = 0; i < Principal.size(); i++){
				Message.sendMsg(new Message(title, msg, from, Integer.parseInt(Principal.get(i))));
			}
		}
	}
	
	 /**
     * Get courses names & ID's for list.
     * @param courseArr list of all defined courses in DB
     * @return list of courses (name & id)
     */
    private ArrayList<String> getList(ArrayList<Course> courseArr){ // Get courses names & ID's for list.
    	
		ArrayList<String> temp = new ArrayList<String>();
		if (courseArr == null){
			return temp;
		}
		for (int i = 0; i < courseArr.size(); i++)
			temp.add(courseArr.get(i).getName() + " (" + courseArr.get(i).getCourseID() + ")");
		
		return temp;	
	}
	
     /**
     * Check for illegal input.
     * @return if new course is legal
     */
	private boolean checkIlegalInput(){ // Check for illegal input.
		err = false;
		
		try{
    		Integer.parseInt(courseId.getText());
    		if (courseId.getText().isEmpty()){
    			throw new Exception();
    		}
    		if (courseId.getText().length() > 6){
    			throw new Exception();
    		}
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
	
	 /**
	 * Clear & update screen details.
	 * @throws IOException
	 */
	private void clearScreen() throws IOException{ // Clear & update screen details.
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/SystemManagerDefineCourses.fxml"));;
		Main.getRoot().setCenter(pane);
	}
	
	//------------------------------------------------------------------------------------------------------------------------
	
    /**
     * Filter course list results.
     * @param e
     */
    public void search(ActionEvent e){ // Filter course list.
    	
    	ArrayList<String> temp = new  ArrayList<String>();
    	if (organizedList == null){
    		return;
    	}
    	
    	for (int i = 0; i < organizedList.size(); i++)
    		if (organizedList.get(i).toLowerCase().contains(searchField.getText().toLowerCase()))
    			temp.add(organizedList.get(i));
    	
    	coursesDB  = FXCollections.observableArrayList(temp);
		courseList.setItems(coursesDB);
    }
    
    /**
     * Add Course to PreCourse list.
     * @param e
     */
    public void addPreCourse(ActionEvent e){ // Add Course to PreCourse list.
    	
    	if (PreList == null){
    		return;
    	}
    	
    	for (int i = 0; i < PreList.size(); i++)
    		if (courseList.getSelectionModel().getSelectedItem().equals(PreList.get(i)))
    			return;
    	
    	PreList.add(courseList.getSelectionModel().getSelectedItem());
    	Collections.sort(PreList);
    	preCourseList.setItems(FXCollections.observableArrayList(PreList));
    }
    
    /**
     * Remove Course from PreCourse list.
     * @param e
     */
    public void removePreCourse(ActionEvent e){ // Remove Course from PreCourse list.
    	
    	if (PreList == null){
    		return;
    	}
    	
    	for (int i = 0; i < PreList.size(); i++)
    		if (preCourseList.getSelectionModel().getSelectedItem().equals(PreList.get(i)))
    			PreList.remove(i);
    	
    	Collections.sort(PreList);
    	preCourseList.setItems(FXCollections.observableArrayList(PreList));	
    }

    /**
     * Submit course for check & insert into DB.
     * @param e
     * @throws IOException
     */
    public void submitCourse(ActionEvent e) throws IOException{ //  Submit course for check & insert.
    	  	
    	if (err){ // Reset Screen errors.
	    	idErr.setText("");
	    	nameErr.setText("");
	    	hoursErr.setText("");
	    	unitErr.setText("");
    	}
    	
    	if (checkIlegalInput()){
    		errMsg.setContentText("Ilegal Input!");
    		errMsg.showAndWait();
    		return;
    	}
    	
    	if (!Course.checkLegal(courseId.getText())){
    		idErr.setText("Course ID already exists!");
    		errMsg.setContentText("Course Already Exists In Data Base!");
    		errMsg.showAndWait();
    		return;
    	}
    	
    	newCourse = new Course(Integer.parseInt(courseId.getText()), courseName.getText(),
    			Integer.parseInt(weaklyHours.getSelectionModel().getSelectedItem().toString()),
    			teachingUnit.getSelectionModel().getSelectedItem().toString());
    	
    	newCourse.insertCourse();
    	newCourse.updatePreCourses(PreList);
    	
    	int numOfChanges = (int)Main.client.getMessage();
    	
    	if (numOfChanges >= 1){
    		sendMsgs();
    		infoMsg.setContentText("Course definition successful :)");
    		infoMsg.showAndWait();
	    	clearScreen();
    	}
    	else{
    		infoMsg.setContentText("No changes were made!");
    		infoMsg.showAndWait();
    	}
    }
    
	//------------------------------------------------------------------------------------------------------------------------
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){ // Initialize window.
		infoMsg.setTitle("Operation Successful");
		infoMsg.setHeaderText(null);
		errMsg.setTitle("Error Accord");
		errMsg.setHeaderText(null);
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