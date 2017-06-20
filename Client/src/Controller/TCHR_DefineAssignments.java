package Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import Entity.Message;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**		Controller class for:  "TCHR_CheckAssignments.fxml"
 * @author Nadav Zrihan
 * 	*/
public class TCHR_DefineAssignments implements Initializable {
	
	/**		array list contains all course objects for the connected teacher from database	*/
	ArrayList<DBCourse> courses = new ArrayList<DBCourse>();
	
	/**		this class represents assignment object 	*/
	class DBAssignment {
		
		String name;
		int courseID;
		String publishDate;
		String deadLine;
		String filePath;
		int semesterID;
		
	}
	
	/**		this class represents course object 	*/
    class DBCourse {
    	int courseID;
    	String courseName;
    	public DBCourse (){};
		public String toString() {
			return "\t"+this.courseName;
		}
    }

	/**		message that will be sent to the server 	*/
	HashMap<String, String> sentMSG = new HashMap<>();	
	
	File file;
	String filename;
    
    @FXML
    private Button fileBTN;

    @FXML
    private Button submitBTN;

    @FXML
    private ComboBox<String> coursesCB;

    @FXML
    private Label ddLABEL;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label nameLABEL;

    @FXML
    private TextField nameTF;

    @FXML
    private Label fileLABEL;

    @FXML
    private TextField fileTF;

    @FXML
    private Label ddREDstar;

    @FXML
    private Label nameREDstar;

    /**	file chooser button event handler */
    @FXML
    void fileBTNaction(ActionEvent event) {

		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File");
		file = fileChooser.showOpenDialog(fileBTN.getScene().getWindow());
		if (file != null) {
			fileTF.setText(file.getAbsolutePath());
			submitBTN.setDisable(false);
		}
    }

    /**	submit button event handler */
    @FXML
    void submitBTNaction(ActionEvent event) {
    	
    	if (validInput()) {	// check for empty fields
    		
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Confirmation Dialog");
    		alert.setHeaderText("Confirm your request");
    		alert.setContentText("This will create the new assignment.\nContinue?");
    		Optional<ButtonType> result = alert.showAndWait();
    		
    		if (result.get() == ButtonType.OK) {
    		
    			DBAssignment ass = new DBAssignment();	/*	create the assignment	*/
    			ass.name = nameTF.getText();
    			ass.courseID = getSelectedCourseFromCB().courseID;
    			ass.publishDate = new SimpleDateFormat("YYYY-MM-dd").format(Calendar.getInstance().getTime());
    			ass.deadLine = datePicker.getValue().toString();
    			ass.semesterID = getCurrentSemesterID();
    			sendFileToServer();
    			ass.filePath = "C:/M.A.T files/assignments/"+filename;

    			createNewDBassignment(ass);
        	
    			showInfoMSG("success", "Assignment created succussfuly!\n(Alert message has been sent to users)");
    			alertAllStudents(ass);	//	alert all students
    			alertAllTeachers(ass);
      
    			getCoursesFromDB();		/*	init	*/
    			setCoursesInComboBox();
    			datePicker.setValue(LocalDate.now());
    			setItemsDisabled(true);
    			nameREDstar.setVisible(false);
    			ddREDstar.setVisible(false);
        	
    		}
    	}
    	
    	else {

    		if (datePicker.getValue() == null)
    			ddREDstar.setVisible(true);
    		else ddREDstar.setVisible(false);
    		
    		if (nameTF.getText().isEmpty())
    			nameREDstar.setVisible(true);
    		else nameREDstar.setVisible(false);
    		
    		showErrorMSG("invalid input", "do not leave empty fields");
    	}
    	
    	
    }
    
    /** send alert to all teacher's on this course about the new assignment
     * @param ass	Course ID
     * */
    @SuppressWarnings("unchecked")
	private void alertAllTeachers (DBAssignment ass) {
    	
    	sentMSG.put("msgType", "select");
    	sentMSG.put("query", "SELECT DISTINCT teacherID FROM class_course CC WHERE CC.CourseID='"+ass.courseID+"'");
		Main.client.sendMessageToServer(sentMSG);
		ArrayList<String> teachers = null;
		
		synchronized (Main.client) {		
			try {
				Main.client.wait();
				teachers = (ArrayList<String>)Main.client.getMessage();	
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		String title = "New assginemt has been uploaded";
		String MSG = "Course ID:\t"+ass.courseID+"\n"
				+"Name:\t\t"+ass.name+"\nPublish Date:\t"+ass.publishDate+"\nDeadLine:\t\t"+ass.deadLine;
		for (int i=0;i<teachers.size();i++) 
			Message.sendMsg(new Message(title, MSG, Integer.parseInt(Main.user.getID()), Integer.parseInt(teachers.get(i))));
    }
    
    /** send alert to all student's about the new assignment
     * @param ass	Course ID
     * */
    @SuppressWarnings("unchecked")
	private void alertAllStudents(DBAssignment ass) {
    	
    	sentMSG.put("msgType", "select");
    	sentMSG.put("query", "SELECT StudentID FROM course_student WHERE CourseID='"+ass.courseID+"'");
		Main.client.sendMessageToServer(sentMSG);
		ArrayList<String> students = null;
		
		synchronized (Main.client) {		
			try {
				Main.client.wait();
				students = (ArrayList<String>)Main.client.getMessage();	
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		String title = "New assginemt has been uploaded";
		String MSG = "Course ID:\t"+ass.courseID+"\n"
				+"Name:\t\t"+ass.name+"\nPublish Date:\t"+ass.publishDate+"\nDeadLine:\t"+ass.deadLine;
		for (int i=0;i<students.size();i++) 
			Message.sendMsg(new Message(title, MSG, Integer.parseInt(Main.user.getID()), Integer.parseInt(students.get(i))));
		
    }
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
    	coursesCB.valueProperty().addListener(new ChangeListener<String>() {
    		@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				coursesCOMBOBOXchangeHandler();
			}});
		
		getCoursesFromDB();
		setCoursesInComboBox();
		setItemsDisabled(true);
		
		fileBTN.setTooltip(new Tooltip("upload assignment file"));
		submitBTN.setTooltip(new Tooltip("create new assignment"));
		
	}
	
	/**	check for empty name/date fields
	 * @return	true only if all fields have values
	 * */
	private boolean validInput() {
		
		if (nameTF.getText().isEmpty())
			return false;
		if (datePicker.getValue() == null)
			return false;
		return true;
		
	}
	
	/**	disables items in this scene
	 * @param s boolean for true or false
	 *  */
	private void setItemsDisabled(boolean s) {
		
		if (s) {		// disable all
			ddLABEL.setDisable(true);
			datePicker.setDisable(true);
			datePicker.setValue(null);
			nameTF.setDisable(true);
			nameTF.setText("");
			nameLABEL.setDisable(true);
			fileLABEL.setDisable(true);
			fileTF.setDisable(true);
			fileTF.setText("");
			fileBTN.setDisable(true);
			submitBTN.setDisable(true);
			ddREDstar.setVisible(false);
			nameREDstar.setVisible(false);
		}
		else {			// enable all
			
			ddLABEL.setDisable(false);
			datePicker.setDisable(false);
			datePicker.setValue(LocalDate.now());
			nameTF.setDisable(false);
			nameLABEL.setDisable(false);
			fileLABEL.setDisable(false);
			//fileTF.setDisable(false);
			fileBTN.setDisable(false);
			//submitBTN.setDisable(false);
			
		}
		
	}
	
	/**	get all the courses assigned to the current teacher using the system 
	 * @return array list of course objects belonged to the current techer */
	@SuppressWarnings("unchecked")
	private void getCoursesFromDB() {
		
		courses.clear();
		
    	sentMSG.put("msgType", "select");
    	sentMSG.put("query", "SELECT DISTINCT CC.CourseID, C.CourseName FROM course C, class_course CC WHERE teacherID = '"+Main.user.getID()+"' and CC.CourseID = C.CourseID;");
		Main.client.sendMessageToServer(sentMSG);
		ArrayList<String> coursesFromDB = null;
		
		synchronized (Main.client) {		
			try {
				Main.client.wait();
				coursesFromDB = (ArrayList<String>)Main.client.getMessage();	
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		for (int i=0;i<coursesFromDB.size();i+=2) {
			DBCourse course = new DBCourse();
			course.courseID = Integer.parseInt(coursesFromDB.get(i));
			course.courseName = coursesFromDB.get(i+1);
    		courses.add(course);
    	}
		
	}
	
	/**		initialize courses ComboBox with strings representing courses	*/
    private void setCoursesInComboBox() {
    	
    	coursesCB.getItems().clear();
    	
    	ObservableList<String> oList = FXCollections.observableArrayList();

    	for (int i=0;i<courses.size();i++) {
    		oList.add(courses.get(i).toString());
    	}
    	
    	coursesCB.setItems(oList);			/*	POPULATE COMBOBOX COLLECTION	*/
    	if (oList.isEmpty()) {
    		showErrorMSG("No courses found for USER ID: "+Main.user.getID(), "");
    		coursesCB.setDisable(true);
    	}
    	
    }

    /**		pop up error message
     * 	@param	title title
     * @param MSG message
     * 	*/
	private void showErrorMSG(String title, String MSG) {
		
  		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error!");
		alert.setHeaderText(title);
		alert.setContentText(MSG);
		alert.showAndWait();
		
	}
	
	/**	shows information message using GUI
	 * 
	 * @param title 'title of the screen'
	 * @param MSG 'message to be displayed'
	 * */
	private void showInfoMSG(String title, String MSG) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(title);
		alert.setContentText(MSG);
		alert.showAndWait();
		
	}
	
	/**	returns course obj of the one selected in the 'courses' Combo Box */
    private DBCourse getSelectedCourseFromCB() {
    	
    	DBCourse course = new DBCourse();
    	String selectedItem = coursesCB.getSelectionModel().getSelectedItem().toString();
    	
    	for (int i=0;i<courses.size();i++) {	/* get matching semester */
    		if (courses.get(i).toString().equals(selectedItem)) {
    			course = courses.get(i);
    			break;
    		}
    	}
    	return course;	/*	return selected semester	*/
    }
	
    /**	send's selected file to the server */
    private void sendFileToServer() {
    	
    String timeStamp = new SimpleDateFormat("HH-mm-ss").format(Calendar.getInstance().getTime());
    	
	if (file != null){
		  
		 filename = timeStamp+"_"+Main.user.getID(); // TODO
		 String type = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf('.'));
		 filename = filename.concat(type);
		  
		 sentMSG = new HashMap <String, String>();
		 sentMSG.put("msgType", "fileInfo");
		 sentMSG.put("fileName", filename);
		 sentMSG.put("dir", "assignment");
		  
		 try{Main.client.sendMessageToServer(sentMSG);}
			catch(Exception exp){showErrorMSG("error","server fatal error!");}
		 synchronized (Main.client){
			 try {Main.client.wait();}
			 	catch (InterruptedException ex) {ex.printStackTrace();}}

		  if ((boolean) Main.client.getMessage()){
			  Path p = Paths.get(file.getAbsolutePath());
			  
			  byte[] data = null;
			  
			  try {data = Files.readAllBytes(p);}
			  	catch (IOException e1) {e1.printStackTrace();}
			  Main.client.sendMessageToServer(data);
			  
			  synchronized (Main.client){
				  try {Main.client.wait();}
				  	catch (InterruptedException e2) {e2.printStackTrace();}}
		  }
		  else{
			  System.out.println("couldnt send file info for the server!");
			  return;
		  } 
	  }
    }

	/** This method returns INT value of the Current Semester's ID */
	@SuppressWarnings("unchecked")
	private int getCurrentSemesterID () {

		ArrayList <String> currentSemester = null;
		sentMSG.put("msgType", "select");
		sentMSG.put("query", "SELECT semesterID FROM mat.semester WHERE isCurr='1'");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {
			try { 
				Main.client.wait();
				currentSemester = (ArrayList<String>)Main.client.getMessage();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}

		
		return Integer.parseInt(currentSemester.get(0));
		
	}

	/**	send's assignment object to database
	 * @param ass assignment object
	 *  */
	private void createNewDBassignment(DBAssignment ass) {
		
		sentMSG.put("msgType", "update");
		sentMSG.put("query", "INSERT INTO `mat`.`assignment` (`AssignmentName`, `CourseID`,"
				+ " `publishDate`, `deadLine`, `filePath`, `semesterId`) VALUES"
				+ " ('"+ass.name+"', '"+ass.courseID+"', '"+ass.publishDate+"', '"+ass.deadLine+"',"
						+ " '"+ass.filePath+"', '"+ass.semesterID+"');");
		synchronized (Main.client) {
			try {
				Main.client.sendMessageToServer(sentMSG);
				Main.client.wait();}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thrsead cant move to wait()");
			}
		}
	}
	
	/**	courses combo box event handler*/
	private void coursesCOMBOBOXchangeHandler() {
		setItemsDisabled(false);
	}
}
