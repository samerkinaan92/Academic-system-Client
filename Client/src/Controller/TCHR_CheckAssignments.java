package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Entity.Assignment;
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
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/** 	Controller class for: "TCHR_CheckAssignments.fxml"	
 * 		@author Nadav Zrihan
 * */

public class TCHR_CheckAssignments implements Initializable {
		
	/**		holds list of strings (each represents an assignment) to be displayed @ combo box	*/
	private final ObservableList<String> assignemntsOBSRlist = FXCollections.observableArrayList();
	
	/**		holds list of strings (each represents COURSE ) to be displayed @ combo box	*/
	private final ObservableList<String> coursesOBSRlist = FXCollections.observableArrayList();
	
	/**		holds list of strings (each represents an ASSIGNMENT) given from the server	 	*/
	ArrayList<String> submissionsFromDB;
	
	/**		holds list of strings (each represents COURSE) given from the server	 	*/
	ArrayList<String> coursesFromDB;
	
	/**		message that will be sent to the server 	*/
	HashMap<String, String> sentMSG = new HashMap<>();	
	
	/**		contains submission OBJECTIVES from DataBase 	*/
	ArrayList<DBSubmission> submissions = new ArrayList<DBSubmission>();
	
	/**		contains courses OBJECTIVES from DataBase 	*/
	ArrayList<DBCourse> courses = new ArrayList<DBCourse>();
	
	/**		represents Submission object	*/
    class DBSubmission {
    	
    	int assignmentID;
    	int studentID;
    	float Grade = -1;
    	String filePath;
    	String evaluation;
    	boolean isLate=false;
    	boolean isGotten=false;

    	public DBSubmission (){};

		public String toString() {
			String late="";
			if (this.isLate) 
				late = "\t\t(late submission)";
			return "ID: "+this.studentID+"\t Assignment: "+this.assignmentID+late;
		}
    }
    
	/**		represents Course object	*/
    class DBCourse {
    	int courseID;
    	String courseName;
    	public DBCourse (){};
		public String toString() {
			return "\t"+this.courseName;
		}
    }
    
    /**	String of the current selected course in Combo Box*/
    String currentSelectedCourseName = "";
    
    File file;
    
    boolean coursesCBcontorl = false;

    @FXML
    private Button saveEvaluationBTN;

    @FXML
    private TextArea evaluationTextArea;
    
    @FXML
    private Label redLABEL;

    @FXML
    private TextField finalGradeTextField;

    @FXML
    private ComboBox<String> assignmentsCB;

    @FXML
    private ComboBox<String> coursesCOMBOBOX;
    
    @FXML
    private Label finalGradeLABEL;
    
    @FXML
    private Label evaluationLABEL;
    
    @FXML
    private Label selectAssignmentLABEL;
    
    @FXML
    private Button getBTN;
    
    @FXML
    private Button sendBTN;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    		
    	/*	set courses combobox handler and items	*/
    	
    	coursesCOMBOBOX.valueProperty().addListener(new ChangeListener<String>() {
    		@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				coursesCOMBOBOXchangeHandler(arg2);
			}});
    	
    	assignmentsCB.valueProperty().addListener(new ChangeListener<String>() {
    		@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				submissionsCOMBOBOXchangeHandler(arg2);
			}});
    	
    	getCoursesFromDB();
    	setCoursesInComboBox();
    	enableElements(false);
    	
    }
    
    /**		actions to be taken when  "GET" button has been pressed	*/
    @FXML
    void getSubmissionFile(ActionEvent event) {
    	
    	DBSubmission sub = getSelectedSubmission();
    	
    	byte[] file = Assignment.getFile(sub.filePath);	// file obj
		FileChooser fileChooser = new FileChooser();	// create file chooser
		
        fileChooser.setTitle("Save Submission");
        
        String type = (sub.filePath).substring(sub.filePath.indexOf('.'));
        fileChooser.setInitialFileName(sub.assignmentID+"_"+sub.studentID+type);	// TODO
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Word Documents", "*.docx")
            );
        File pa = fileChooser.showSaveDialog(getBTN.getScene().getWindow());	// open it (returns save path)
        if (pa != null) {
        	Path savePath = Paths.get(pa.getAbsolutePath());	// get save path
        	FileOutputStream stream;
        	try {
        		stream = new FileOutputStream(savePath.toString());
        		stream.write(file);
        		stream.close();
        		showInfoMSG("success!","submission file saved!");
        		showInfoMSG("Next step:","After editing the student submission,\n"
        				+ "use the 'send back' button to send it back to:\n\nstudent ID: "+sub.studentID);
        		sub.isGotten = true;
        		
        	}
        	catch (IOException ex) {
        		ex.printStackTrace();
        		showErrorMSG("error!","error downloading file.");
        	}
        }
    }
    
    /**		actions to be taken when  "SEND" button has been pressed	*/
    @FXML
    void sendSubmissionFile(ActionEvent event) {
    	
    	  DBSubmission sub = getSelectedSubmission();
    	
		  FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Upload File");
          showInfoMSG("Please select your EDITED submission file.\nThe selected file will be sent to:", "Student ID: "+sub.studentID);
		  file = fileChooser.showOpenDialog(sendBTN.getScene().getWindow());
		  
		  if (file != null){
			  
			  /* START SENDING FILE */
			 
			 String filename = sub.assignmentID+"_"+sub.studentID;
			 String type = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf('.'));
			 filename = filename.concat(type);
			  
			 sentMSG = new HashMap <String, String>();
			 sentMSG.put("msgType", "fileInfo");
			 sentMSG.put("fileName", filename);
			 sentMSG.put("dir", "submission");
			  
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
				  showInfoMSG("File has been sent to Student ID: "+sub.studentID, "Next Step:\nFill evaluation form and grade if you havent done so yet.");
				  
	    			String title = "Submission file checked";
	    			String MSG = "Your submission FILE in course: "+currentSelectedCourseName+" has been checked";
	    			Message.sendMsg(new Message(title,MSG, Integer.parseInt(Main.user.getID()),sub.studentID));
				  
			  }
			  else{
				  System.out.println("couldnt send file info for the server!");
				  return;
			  }
			  /*	END FILE SENDING	 */
			  
			  
			  
		  }
    	
    }

    /**		actions to be taken when  "Save Evaluation" button has been pressed	*/
    @FXML
    void saveEvaluationForm(ActionEvent event) {
    	
    	int inputValidation = isWrongInput();
    	
    	if (inputValidation == 0) {
    		
    		DBSubmission sub = getSelectedSubmission();
    		sub.Grade = Float.valueOf(finalGradeTextField.getText());
			sub.evaluation = evaluationTextArea.getText();
    		

    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Confirmation Dialog");
    		alert.setHeaderText("Please confirm your request");
    		alert.setContentText("This will update any existing data.\nContinue?");

    		Optional<ButtonType> result = alert.showAndWait();
    		if (result.get() == ButtonType.OK){
    			
    			sendSubmissionToDB(sub.studentID,sub.assignmentID,sub.Grade,sub.evaluation);
    			alert.setAlertType(AlertType.INFORMATION);
    			alert.setTitle("Information Dialog");
    			alert.setHeaderText(null);
    			alert.setContentText("Your evaluation has been saved!");
    			alert.showAndWait();
    			
    			String title = "Submission Checked";
    			String MSG = "Your submission in course: "+currentSelectedCourseName+"\nhas been checked and evaluated.\n\n"
    					+"Grade: "+sub.Grade+"\nEvaluation: "+sub.evaluation;
    			Message.sendMsg(new Message(title,MSG, Integer.parseInt(Main.user.getID()),sub.studentID));
    			
    			evaluationTextArea.clear();
    			finalGradeTextField.clear();
    			evaluationTextArea.setPromptText(sub.evaluation);
    			finalGradeTextField.setPromptText(""+sub.Grade);
	
    		}
    		
    		redLABEL.setText("");
    		
    	}
    	
    	else if (inputValidation == 1)  	{						/*	empty evaluation field */
    		
    		redLABEL.setText("empty evaluation field!");
    		
    	}
    	
    	else if (inputValidation == 2) {							/*	empty grade field */
    		
    		redLABEL.setText("empty grade field!");
    		
    	}
    	
    	else if (inputValidation == 3) {							/*	grade < 0 */
    		
    		redLABEL.setText("negative grade value!");
    		
    	}

    }
    
    /**		initializes combobox with all the current submissions 	*/
    private void setSubmissionsInComboBox() {
    	
    	for (int i=0;i<submissions.size();i++) {
    		if (!assignemntsOBSRlist.contains(submissions.get(i).toString()))
    			assignemntsOBSRlist.add(submissions.get(i).toString());
    	}
    	
    	assignmentsCB.setItems(assignemntsOBSRlist);			/*	POPULATE COMBOBOX COLLECTION	*/
    	assignmentsCB.getSelectionModel().selectFirst();
    	
    }
    
    /**		get all submission objects from database 
     * @param courseID	course ID
     * 	*/
    @SuppressWarnings("unchecked")
	private void getSubmissionsFromDB(int courseID) {

    	sentMSG.put("msgType", "select");
    	sentMSG.put("query", "SELECT S.AssignmentID, S.StudentStudentID, S.Grade, S.filePath, S.evaluation, S.isLate FROM submission S, assignment A WHERE S.AssignmentID=A.AssignmentID and A.CourseID='"+courseID+"'");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {		
			try {Main.client.wait();}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		submissionsFromDB = (ArrayList<String>)Main.client.getMessage();
		submissions.clear();

		for (int i=0;i<submissionsFromDB.size();i+=6) {
			
			DBSubmission sub = new DBSubmission();
				
			sub.assignmentID = Integer.parseInt(submissionsFromDB.get(i));
			sub.studentID = Integer.parseInt(submissionsFromDB.get(i+1));
			if (submissionsFromDB.get(i+2) != null)
				sub.Grade = Float.parseFloat(submissionsFromDB.get(i+2));
			sub.filePath = submissionsFromDB.get(i+3);
			sub.evaluation = submissionsFromDB.get(i+4);
			if (submissionsFromDB.get(i+5).equals("1"))
				sub.isLate = true;
			
			submissions.add(sub);
    	}
    }
    
    /**		get all COURSES objects from database 	*/
    @SuppressWarnings("unchecked")
	private void getCoursesFromDB() {
    	
    	sentMSG.put("msgType", "select");
    	sentMSG.put("query", "SELECT DISTINCT CC.CourseID, C.CourseName FROM course C, class_course CC WHERE teacherID = '"+Main.user.getID()+"' and CC.CourseID = C.CourseID;");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {		
			try {Main.client.wait();}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		coursesFromDB = (ArrayList<String>)Main.client.getMessage();
		
		for (int i=0;i<coursesFromDB.size();i+=2) {
			DBCourse course = new DBCourse();
			
			course.courseID = Integer.parseInt(coursesFromDB.get(i));
			course.courseName = coursesFromDB.get(i+1);
    		
    		courses.add(course);
    		
    	}
    }
    
	/**		initialize courses ComboBox with strings representing courses	*/
    private void setCoursesInComboBox() {

    	for (int i=0;i<courses.size();i++) {
    		coursesOBSRlist.add(courses.get(i).toString());
    	}
    	
    	coursesCOMBOBOX.setItems(coursesOBSRlist);			/*	POPULATE COMBOBOX COLLECTION	*/
    	
    	if (coursesOBSRlist.isEmpty()) {
    		showErrorMSG("No courses found for USER ID: "+Main.user.getID(), "");
    		coursesCOMBOBOX.setDisable(true);
    	}
    	
    }

    /**		check input validation 	*/
    private int isWrongInput() {							/* returns 0 for valid input */
    	if (finalGradeTextField.getText().isEmpty())
    		return 2;										/*	returns 2 for empty grade field */
    	else if (evaluationTextArea.getText().isEmpty())
    		return 1;										/*	returns 1 for empty evaluation field */
    	else if (Integer.parseInt(finalGradeTextField.getText()) < 0)	/* 	returns 3 for negative grade input */
    		return 3;
    	return 0;
    }
    
    /**		get the submission object who matches the selection in the combobox	*/
    private DBSubmission getSelectedSubmission() {
    	
    	DBSubmission sub = new DBSubmission();
    	String selectedItem = assignmentsCB.getSelectionModel().getSelectedItem().toString(); /* get selected string */
    	for (int i=0;i<submissions.size();i++) {	/* find submission object */
    		if (submissions.get(i).toString().equals(selectedItem)) {
    			sub = submissions.get(i);
    			break;
    		}
    	}
    	return sub;	/*	return selected submission	*/
    }
    
    /**		send the given SUBMISSION information to database
     * @param studentID		student ID number
     * @param assignmentID	assignments ID number
     * @param grade			assignments final grade taken from user input
     * @param evaluation	assignments final evaluation taken from user input
     * 	*/
    private void sendSubmissionToDB(int studentID, int assignmentID, float grade, String evaluation) {

    	sentMSG.put("msgType", "update");
		sentMSG.put("query", "UPDATE `mat`.`submission` SET `Grade`='"+grade+"', `evaluation`='"+evaluation+"' WHERE `AssignmentID`='"+assignmentID+"' and`StudentStudentID`='"+studentID+"'");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {		
			try {Main.client.wait();}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
    }
    
    /**		event handler for courses combo box
     * @param	SI	the string selected in courses ComboBox
     * */
    private void coursesCOMBOBOXchangeHandler (String SI) {
    	
    	
    	
    	assignmentsCB.getItems().clear();
    	
    	DBCourse course = getSelectedCourse(SI);
    	currentSelectedCourseName = course.courseName;
    	
    	getSubmissionsFromDB(course.courseID);	/*	get all submission's of a given course ID	*/
    	
    	
    	if (submissions.isEmpty()) {
    		showErrorMSG("null submission files", "this course does not yet have any submissions");
    		enableElements(false);
    	}
    		
    	if (!submissions.isEmpty()) {
    		setSubmissionsInComboBox();
    		enableElements(true);
    	}
    }
    
    /**		event handler for submissions combo box
     * @param	SI	the string selected in submissions ComboBox
     * */
    private void submissionsCOMBOBOXchangeHandler(String SI) {
    	
    	evaluationTextArea.setText("");
    	finalGradeTextField.setText("");
    	
    	
    	DBSubmission sub = new DBSubmission();
    	for (int i=0;i<submissions.size();i++) {				/* get matching submission */
    		if ((submissions.get(i).toString()).equals(SI)) {
    			sub = submissions.get(i); break;
    		}
    	}
    	
    	if (sub.evaluation != null)
    		evaluationTextArea.setPromptText(sub.evaluation);
    	else
    		evaluationTextArea.setPromptText("");
    	
    	if (sub.Grade != -1)
    		finalGradeTextField.setPromptText(""+sub.Grade);
    	else
    		finalGradeTextField.setPromptText("");
    	
  
    	
    }
    
    /**		get the COURSE object who matches the selection in the combobox	*/
    private DBCourse getSelectedCourse(String selectedItem) {
    	
    	DBCourse course = new DBCourse();
    	
    	for (int i=0;i<courses.size();i++) {	/* get matching submission */
    		if ((courses.get(i).toString()).equals(selectedItem)) {
    			course = courses.get(i);
    			break;
    		}
    	}
    	return course;	/*	return selected submission	*/
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

	/**	enable/disable scene elements
	 * @param s	true: enable all, 
	 * 			false: disable all.
	 * 
	 * */
	private void enableElements(boolean s) {
		
		if (s) {
        	assignmentsCB.setDisable(false);
        	getBTN.setDisable(false);
        	finalGradeTextField.setDisable(false);
        	evaluationTextArea.setDisable(false);
        	saveEvaluationBTN.setDisable(false);
        	sendBTN.setDisable(false);
        	evaluationLABEL.setDisable(false);
        	finalGradeLABEL.setDisable(false);
        	selectAssignmentLABEL.setDisable(false);
		}
			
		else {
        	assignmentsCB.setDisable(true);
        	getBTN.setDisable(true);
        	finalGradeTextField.setDisable(true);
        	evaluationTextArea.setDisable(true);
        	saveEvaluationBTN.setDisable(true);
        	sendBTN.setDisable(true);
        	evaluationLABEL.setDisable(true);
        	finalGradeLABEL.setDisable(true);
        	selectAssignmentLABEL.setDisable(true);
		}
	}

}
