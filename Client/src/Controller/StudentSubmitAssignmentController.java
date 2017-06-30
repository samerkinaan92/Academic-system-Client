package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import Entity.Assignment;
import Entity.Course;
import Entity.Message;
import Entity.Student;
import Entity.SubmittedAssignment;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

/**
 * This is the controller class for: "StudentSubmitAssignment.fxml"
 * @author Idan Agam
 * */

public class StudentSubmitAssignmentController implements Initializable {
	
	  /** Course list to be displayed on ListView	*/
	  @FXML
	  private ListView<String> courseListView;

	  /** Text field to filter course results */
	  @FXML
	  private TextField courseSearch;

	  /** Assignment list to be displayed on ListView	*/
	  @FXML
	  private ListView<String> assignmentListView;

	  /** Text field to filter assignment results */
	  @FXML
	  private TextField assignmentSearch;
	  
	  /** Label to display selected file path  */
	  @FXML
	  private Label filePath;
	  
	  /** Label to display messages to user  */
	  @FXML
	  private Label guiMsg;
	  
	  /** Button to submit assignment  */
	  @FXML
	  private Button submitBtn;

	  /** Button to browse file from local disk  */
	  @FXML
	  private Button browseBtn;

	  @FXML
	  private Label uploadLabel;
	  
	  /** Button to download assignment from server  */
	  @FXML
	  private Button downloadBtn;
	  
	  /** ArrayList of all courses in DB  */
	  private ArrayList<Course> courseArr;
	  
	  /** ArrayList of all assignments in DB  */
	  private ArrayList<Assignment> assignmentArr;
	  
	  /** ArrayList of all courses in DB organized by name & ID (sorted)  */
	  private ArrayList<String> organizedCourseList;
	  
	  /** ArrayList of all assignments in DB organized by name & ID (sorted)  */
	  private ArrayList<String> organizedAssignmentList;
	  
	  /** HashMap for sending messages to server  */
	  private HashMap <String,String> msgServer;
	 
	  /** File to store local assignment info  */
	  private  File file;
	  
	  Alert infoMsg = new Alert(AlertType.INFORMATION);
	  Alert errMsg = new Alert(AlertType.ERROR);
	  Alert warMsg = new Alert(AlertType.WARNING);

	  //-------------------------------------------------------------------------------------------------------------------
	  
	  /**
	   * Clear & update screen details.
	   */
	  private void clearScreen(){ // Clear & update screen details.
		  SplitPane pane = null;
		  try {
			  pane = FXMLLoader.load(getClass().getResource("/FXML/StudentSubmitAssignment.fxml"));
		  } 
		  catch (IOException e) {
			  errMsg.setContentText("Send Message To Server Failed!");
			  errMsg.showAndWait();
			  return;
		  };
		Main.getRoot().setCenter(pane);
	  }
	  
	  /** Remove all submitted assignments from list. */
	  private void removeSubmitted(){ // Remove all submitted assignments from list.
		  
		  ArrayList<String> submitted = SubmittedAssignment.getSubmittedAssignments();
		  
		  if (submitted == null || assignmentArr == null){
			  return;
		  }
		  
		  int size = assignmentArr.size();
		  
		  for (int i = size-1; i >= 0; i--){
			  for (int j = 0; j < submitted.size(); j++){
				  if (Integer.parseInt(submitted.get(j)) == assignmentArr.get(i).getAssignmentID()){
					  assignmentArr.remove(i);
				  }
			  }
		  }  
	  }
	  
	  /**
	   * Get courses names & ID's for list.
	   * @param courseArr Course array.
	   * @return Course (ID)
	   */
	  private ArrayList<String> getCourseList(ArrayList<Course> courseArr){ // Get courses names & ID's for list.
	    	
		  ArrayList<String> temp = new ArrayList<String>();
			
		  if (courseArr == null){
			  return temp;
		  }
		  
		  for (int i = 0; i < courseArr.size(); i++)
			  temp.add(courseArr.get(i).getName() + " (" + courseArr.get(i).getCourseID() + ")");
			
		  return temp;	
		}
	  
	  /**
	   * Get Assignment names & ID's for list.
	   * @param assignmentArr Assignment array.
	   * @return Assignment (ID)
	  */
	  private ArrayList<String> getAssignmentList(ArrayList<Assignment> assignmentArr){ // Get Assignment names & ID's for list.
	    	
			ArrayList<String> temp = new ArrayList<String>();
			if (assignmentArr == null){
				return temp;
			}
			
			for (int i = 0; i < assignmentArr.size(); i++)
				temp.add(assignmentArr.get(i).getAssignmentName() + " (" + assignmentArr.get(i).getAssignmentID() + ")");
			
			return temp;	
		}
	  
	  /**
	   * Check if submission is late.
	   * @param id Submission ID
	   * @return 1 if late, else 0.
	   */
	  @SuppressWarnings("unchecked")
	  private int isLate(String id){ // Check if submission is late.
		  
		  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  msgServer = new HashMap <String,String>();
		  msgServer.put("msgType", "select");
		  msgServer.put("query", "Select * FROM Assignment WHERE Assignment.AssignmentID = " + id);
		  
		  try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					errMsg.setContentText("Send Message To Server Failed!");
					errMsg.showAndWait();
					return -1;
				}
			synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException e) {
				errMsg.setContentText("Send Message To Server Failed!");
				errMsg.showAndWait();
				return -1;
			}}
			ArrayList<String> date = (ArrayList<String>)Main.client.getMessage();
			Date subDate = null;
			try {
				subDate = format.parse(date.get(4));
			} catch (ParseException e) {
				errMsg.setContentText("Date Format Not Valid In Data Base!");
				errMsg.showAndWait();
				return -1;
			}

			Date curDate = new Date();
			
			if (subDate.compareTo(curDate) < 0)
				return 1;
			else
				return 0; 
	  }
	  
	  /**
		 * Send message to all relevant users.
		 */
		@SuppressWarnings("unchecked")
		private void sendMsgs(){
			
			String selectedAss = assignmentListView.getSelectionModel().getSelectedItem();
			String selectedCourseID = courseListView.getSelectionModel().getSelectedItem();
			String msg = Main.user.getName() + " (" + Main.user.getID() + ")\n\nSubmitted assignment: " + selectedAss +
					"\nCourse: " + selectedCourseID;
			String title = "Assignment Submission";
			
			selectedCourseID = selectedCourseID.substring(selectedCourseID.indexOf('(')+1, selectedCourseID.indexOf(')'));
			
			HashMap <String,String> msgServer = new HashMap <String,String>();
			msgServer.put("msgType", "select");
			msgServer.put("query", "SELECT teacherID FROM mat.class_course WHERE class_course.CourseID = " +
					selectedCourseID +";");
			
			try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					errMsg.setContentText("Send Message To Server Failed!");
					errMsg.showAndWait();
					return;
				}
			synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException e) {
				errMsg.setContentText("Send Message To Server Failed!");
				errMsg.showAndWait();
				return;
			}}
			ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
			
			if (result.size() > 0){
				Message.sendMsg(new Message(title, msg, Integer.parseInt(Main.user.getID()), Integer.parseInt(result.get(0))));
			}
			else{
				warMsg.setContentText("Send Message To Teacher Failed!");
				warMsg.showAndWait();
			}
			
			if (Message.sendMsg(new Message(title, msg, Integer.parseInt(Main.user.getID()), Integer.parseInt(Main.user.getID()))) == 0){
				warMsg.setContentText("Send Message To Student Failed!");
				warMsg.showAndWait();
			}
		}
	 	  
	  //-------------------------------------------------------------------------------------------------------------------
	  
	  /**
	   * Download Assignment from server to local disk.
	   * @param e
	   */
	  public void download(ActionEvent e){ //Download Assignment from server.
		  
		  Assignment ass = null;
		  String selected = assignmentListView.getSelectionModel().getSelectedItem();
		 		  
		  if (selected == null){
			  warMsg.setContentText("No Assignment Was Selected!");
			  warMsg.showAndWait();
			  return;
		  }
		
		  selected = selected.substring(selected.indexOf('(') + 1, selected.indexOf(')'));
		  
		  for (int i = 0; i < assignmentArr.size(); i++){
			  if (assignmentArr.get(i).getAssignmentID() == Integer.parseInt(selected)){
				  ass = assignmentArr.get(i);
				  break;
			  }
		  }
		  
		  if (ass == null){
			  warMsg.setContentText("Assignment Was Not Found In Data Base!");
			  warMsg.showAndWait();
			  return;
		  }
		  
		  String p = ass.getFilePath();
		  
		  byte[] file = Assignment.getFile(p);
		  
		  if (file == null){
			  errMsg.setContentText("Failed Getting File Stream From Server!");
        	  errMsg.showAndWait();
        	  return;
		  }
		  
		  FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Save Assignment");
          fileChooser.getExtensionFilters().addAll(
                  new FileChooser.ExtensionFilter("All Files", "*.*"),
                  new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                  new FileChooser.ExtensionFilter("Acrobat Reader Files", "*.pdf"),
                  new FileChooser.ExtensionFilter("Word Documents", "*.docx")
              );
          String[] fileName = p.split("/");
          String type = fileName[fileName.length - 1];
          type = type.substring(type.indexOf('.'), type.length());
          fileChooser.setInitialFileName(fileName[fileName.length - 1] + type);
          File pa = fileChooser.showSaveDialog(assignmentListView.getScene().getWindow());
          
          
          if (pa == null){
        	  warMsg.setContentText("Save Path Was Not Selected!");
			  warMsg.showAndWait();
        	  return;
          }
          
          Path savePath = Paths.get(pa.getAbsolutePath());
          FileOutputStream stream;
          
          try {
        	  stream = new FileOutputStream(savePath.toString());
        	  stream.write(file);
        	  stream.close();
        	  infoMsg.setContentText("Assignment Saved");
        	  infoMsg.showAndWait();
          }
          catch (IOException ex) {
        	  ex.printStackTrace();
        	  errMsg.setContentText("Download Failed!");
        	  errMsg.showAndWait();
          } 
	  }
	  
	  /**
	   * Filter course list results.
	   * @param e
	   */
	  public void CourseSearch(ActionEvent e){ // Filter course list results.
		  ArrayList<String> temp = new  ArrayList<String>();
		  
		  if (organizedCourseList == null){
			  return;
		  }
	    	
		  for (int i = 0; i < organizedCourseList.size(); i++)
			  if (organizedCourseList.get(i).toLowerCase().contains(courseSearch.getText().toLowerCase()))
				  temp.add(organizedCourseList.get(i));
	    	
		  courseListView.setItems(FXCollections.observableArrayList(temp));
	  }
	 
	  /**
	   * Filter assignment list results.
	   * @param e
	   */
	  public void AssignmentSearch(ActionEvent e){ // Filter assignment list results.
	  
		  ArrayList<String> temp = new  ArrayList<String>();
		  
		  if (organizedAssignmentList == null){
			  return;
		  }
	    	
		  for (int i = 0; i < organizedAssignmentList.size(); i++)
			  if (organizedAssignmentList.get(i).toLowerCase().contains(assignmentSearch.getText().toLowerCase()))
				  temp.add(organizedAssignmentList.get(i));
	    	
		  assignmentListView.setItems(FXCollections.observableArrayList(temp));
	  }

	  /**
	   * Submit file from disk to server.
	   * @param e
	   */
	  public void submit(ActionEvent e){ // Submit file from disk to server. 
		  
		  if (assignmentListView.getSelectionModel().getSelectedItem().isEmpty()){
			  errMsg.setContentText("No assignment was chosen! Please choose assignment from the assignment list.");
			  errMsg.showAndWait();
			  return;
		  }
			  
		  if (file == null){
			  errMsg.setContentText("No file was chosen to upload! Please browse for a file from the computer.");
			  errMsg.showAndWait();
			  return;
		  }
		  
		  // Send file info for server. 
		  
		  String filename = assignmentListView.getSelectionModel().getSelectedItem().toString();
		  filename = filename.substring(filename.indexOf('(') + 1, filename.indexOf(')'));
		  filename = filename.concat("_" + Main.user.getID());
		  String path = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf('.'));
		  filename = filename.concat(path);
		  
		  msgServer = new HashMap <String, String>();
		  msgServer.put("msgType", "fileInfo");
		  msgServer.put("fileName", filename);
		  msgServer.put("dir", "submission");
		  
		  try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					errMsg.setContentText("Send Message To Server Failed!");
					errMsg.showAndWait();
					return;
				}
		  
		  synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException ex) {
				errMsg.setContentText("Send Message To Server Failed!");
				errMsg.showAndWait();
				return;
			}}
		  
		  //-----------------------------------------------------------------------------------------------------------------
		  
		  // Send file data to server. 
		  
		  if ((boolean) Main.client.getMessage()){
			  Path p =Paths.get(file.getAbsolutePath());
			  byte[] data = null;
			  
			  try {
				data = Files.readAllBytes(p);
			} catch (IOException e1) {
				errMsg.setContentText("Reading File Data Failed!");
				errMsg.showAndWait();
				return;
			}
			  Main.client.sendMessageToServer(data);
			  
			  synchronized (Main.client){try {
					Main.client.wait();
				} catch (InterruptedException e2) {
					errMsg.setContentText("Send Message To Server Failed!");
					errMsg.showAndWait();
					return;
				}}
		  }
		  else{
			  errMsg.setContentText("Send file info to server failed!");
			  errMsg.showAndWait();
			  return;
		  }
		  
		  //----------------------------------------------------------------------------------------------------------------
		  
		  // Insert new submission data into data base.
		  
		  if (Main.client.getMessage() == null){
			  errMsg.setContentText("Send file data to server failed!");
			  errMsg.showAndWait();
			  return;
		  }
		  
		  String filepath = (String) Main.client.getMessage();
		  filepath = filepath.replace("\\","\\\\");
		  String assignmentID = assignmentListView.getSelectionModel().getSelectedItem().toString();
		  assignmentID = assignmentID.substring(assignmentID.indexOf('(') + 1, assignmentID.indexOf(')'));
		  
		  String msg = "Insert INTO submission (AssignmentID, StudentStudentID, filePath, isLate)";
	    	String values = " VALUES (" + assignmentID + ", " + Main.user.getID() + ", '" + 
	    			filepath + "', " + isLate(assignmentID) + ")";
	    	
	    	msgServer = new HashMap <String,String>();
	    	msgServer.put("msgType", "insert");
			msgServer.put("query", msg + values);
	    	
	    	try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					errMsg.setContentText("Send Message To Server Failed!");
					errMsg.showAndWait();
					return;
				}
	    	synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException ex) {
				errMsg.setContentText("Send Message To Server Failed!");
				errMsg.showAndWait();
				return;
			}}
	    	
	    	if ((int)Main.client.getMessage() > 0){
	    		infoMsg.setContentText("submission Successful");
	    		infoMsg.showAndWait();
	    		sendMsgs();
	    	}
	    	else{
	    		errMsg.setContentText("submission Filed: Data base error!");
	    		errMsg.showAndWait();
	    	}
	    	clearScreen();
	    	updateStatistical(Main.user.getID(),assignmentID);
	  }
	  
	    
	    /**insertStatistical() - Updating statistical table in DB.
	     * @param studentID - Student ID
	     * @param assignmentID - Assignment ID
	     * @param grade - Assignment grade
	     * */
		private void updateStatistical(String studentID, String assignmentID){
	    	
			HashMap<String, String >sentMSG = new HashMap <String,String>();
	    	ArrayList<String> assignDetails= Assignment.getAssignDetails(assignmentID);  	
	    	
	    	sentMSG.put("msgType", "insert");
			sentMSG.put("query", "INSERT INTO `mat`.`statistical` (`assignID`, `assignName`, `studentID`) VALUES ('"+assignmentID+"', '"+assignDetails.get(0)+"', '"+studentID+"');");
			synchronized (Main.client) {	
				Main.client.sendMessageToServer(sentMSG);
				try {Main.client.wait();}
				catch (InterruptedException e) {
					e.printStackTrace();
					System.out.println("Thread cant move to wait()");
				}
				
			}
	    
	    }
	  
	  /**
	   * Open file explorer for upload file.
	   * @param e
	   */
	  public void browse(ActionEvent e){ // Open file explorer for upload file.
		  
		  if (assignmentListView.getSelectionModel().getSelectedItem().isEmpty()){
			  errMsg.setContentText("No assignment was chosen! Please choose assignment from the assignment list.");
			  errMsg.showAndWait();
			  return;
		  }
		  
		  FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Upload File");
		  file = fileChooser.showOpenDialog(courseSearch.getScene().getWindow());
		  if (file != null){
			  filePath.setText(file.getAbsolutePath());
			  submitBtn.setDisable(false);
		  }
		  else{
			  warMsg.setContentText("No file was selected!");
			  warMsg.showAndWait();
		  }
	  }

	  //-------------------------------------------------------------------------------------------------------------------
	  
	  @Override
	  public void initialize(URL arg0, ResourceBundle arg1) { // Initialize window.
		  
		  infoMsg.setTitle("Operation Successful");
		  infoMsg.setHeaderText(null);
		  errMsg.setTitle("Error Accord");
		  errMsg.setHeaderText(null);
		  warMsg.setTitle("Notifcation");
		  warMsg.setHeaderText(null);
		  file = null;
		  courseArr = Student.getCourse();
		  // courseArr = Course.filterOldCourses(courseArr);
		  
		  organizedCourseList = getCourseList(courseArr);
		  Collections.sort(organizedCourseList);
		  courseListView.setItems(FXCollections.observableArrayList(organizedCourseList));
		  
		  courseListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			    	int courseID = -1;
			    	for (int i = 0; i < organizedCourseList.size(); i++)
			    		//if (organizedCourseList.get(i).equals(newValue))
			    		if (organizedCourseList.get(i).equals(courseListView.getSelectionModel().getSelectedItem().toString()))
			    			courseID = Integer.parseInt
			    			(courseListView.getSelectionModel().getSelectedItem().toString().substring
			    			(courseListView.getSelectionModel().getSelectedItem().toString().indexOf('(') + 1
			    			, courseListView.getSelectionModel().getSelectedItem().toString().indexOf(')')));
			    				
			    	assignmentArr = Student.getAssignments(courseID);
			    	removeSubmitted();
			    	organizedAssignmentList = getAssignmentList(assignmentArr);
			    	Collections.sort(organizedAssignmentList);
			    	assignmentListView.setItems(FXCollections.observableArrayList(organizedAssignmentList));
			    	
			    	submitBtn.setDisable(true);
			    	uploadLabel.setDisable(true);
			    	browseBtn.setDisable(true);
			    	downloadBtn.setDisable(true);
			    	guiMsg.setText("");
			    }
			}); 
		  
		  assignmentListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			    	
			    	
			    	Date publish = null, dead = null;
			    	String selected = assignmentListView.getSelectionModel().getSelectedItem();
			    	selected = selected.substring(selected.indexOf('(') + 1, selected.indexOf(')'));
			    	
			    	for (int i = 0; i < assignmentArr.size(); i++){
			    		if (assignmentArr.get(i).getAssignmentID() == Integer.parseInt(selected)){
			    			publish = assignmentArr.get(i).getPublishDate();
			    			dead = assignmentArr.get(i).getDeadLine();
			    			break;
			    		}
			    	}
			    	
			    	guiMsg.setText("Publish Date: " + publish + "\nDead line Date: " + dead);
			    	uploadLabel.setDisable(false);
			    	browseBtn.setDisable(false);
			    	downloadBtn.setDisable(false);
			    }
			});  
	  }
}