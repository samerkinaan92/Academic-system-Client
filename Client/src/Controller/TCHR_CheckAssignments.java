package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	
	/**		holds list of strings (each represents an assignment) given from the server	 	*/
	ArrayList<String> submissionsFromDB;
	
	/**		message that will be sent to the server 	*/
	HashMap<String, String> sentMSG = new HashMap<>();	
	
	/**		contains submissions from DataBase 	*/
	ArrayList<DBSubmission> submissions = new ArrayList<DBSubmission>();
	
	/**		represents Submission object	*/
    class DBSubmission {
    	
    	int assignmentID;
    	int studentID;
    	float Grade;
    	String filePath;
    	String evaluation;
    	boolean isLate=false;

    	public DBSubmission (){};

		public String toString() {
			String late="";
			if (this.isLate) 
				late = "\t\t(late submission)";
			return "ID: "+this.studentID+"\t Assignment: "+this.assignmentID+late;
		}
    }

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
    private Button viewEditBTN;
    
    @FXML
    private Label blueLABEL;
    
    @FXML
    private Button clearFieldsBTN;
    
    /**		actions to be taken when  "clear fields" button has been pressed	*/
    @FXML
    void clearFieldsBTNaction(ActionEvent event) {
		evaluationTextArea.clear();
		finalGradeTextField.clear();
		redLABEL.setText("");
		
    }

    /**		actions to be taken when  "VIEW @ EDIT" button has been pressed	*/
    @FXML
    void openAssignmentFile(ActionEvent event) {
    	
    				/* 	OPEN THE FILE OF THE SELECTED ASSIGNMENT IN THE COMBOBOX	*/
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    	getSubmissionsFromDB();
    	setSubmissionsInComboBox();	

    }
    
    /**		initializes combobox with all the current submissions 	*/
    private void setSubmissionsInComboBox() {

    	for (int i=0;i<submissions.size();i++) {
    		assignemntsOBSRlist.add(submissions.get(i).toString());
    	}
    	
    	assignmentsCB.setItems(assignemntsOBSRlist);			/*	POPULATE COMBOBOX COLLECTION	*/
    	assignmentsCB.getSelectionModel().selectFirst();
    	
    }
    
    /**		get all submission objects from database 	*/
    @SuppressWarnings("unchecked")
	private void getSubmissionsFromDB() {
    	
    	sentMSG.put("msgType", "select");
    	sentMSG.put("query", "	SELECT AssignmentID, StudentStudentID, Grade, filePath, evaluation, isLate FROM submission");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {		
			try {Main.client.wait();}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		submissionsFromDB = (ArrayList<String>)Main.client.getMessage();
		
		for (int i=0;i<submissionsFromDB.size();i+=6) {
			DBSubmission sub = new DBSubmission();
    		sub.assignmentID = Integer.parseInt(submissionsFromDB.get(i));
    		sub.studentID = Integer.parseInt(submissionsFromDB.get(i+1));
    		sub.Grade = Float.parseFloat(submissionsFromDB.get(i+1));
    		sub.filePath = submissionsFromDB.get(1+3);
    		sub.evaluation = submissionsFromDB.get(1+4);
    		if (submissionsFromDB.get(i+5).equals("1"))
    			sub.isLate = true;
    		submissions.add(sub);
    		
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
    	String selectedItem = assignmentsCB.getSelectionModel().getSelectedItem().toString(); /* get selection string */
    	for (int i=0;i<submissions.size();i++) {	/* get matching submission */
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

}
