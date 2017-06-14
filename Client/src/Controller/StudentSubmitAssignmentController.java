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

import javax.swing.JOptionPane;

import Entity.Assignment;
import Entity.Course;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
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

	  //-------------------------------------------------------------------------------------------------------------------
	  
	  /** Remove all submitted assignments from list. */
	  private void removeSubmitted(){ // Remove all submitted assignments from list.
		  
		  ArrayList<String> submitted = SubmittedAssignment.getSubmittedAssignments();
		  
		  if (submitted == null)
			  return;
		  
		  int size = assignmentArr.size();
		  
		  for (int i = 0; i < submitted.size(); i++){
			  for (int j = 0; j < size; j++){
				  if (Integer.parseInt(submitted.get(i)) == assignmentArr.get(j).getAssignmentID()){
					  assignmentArr.remove(j);
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
					System.out.println("Server fatal error!");
				}
			synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}}
			ArrayList<String> date = (ArrayList<String>)Main.client.getMessage();
			Date subDate = null;
			try {
				subDate = format.parse(date.get(3));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			Date curDate = new Date();
			
			if (subDate.compareTo(curDate) < 0)
				return 1;
			else
				return 0; 
	  }
	 	  
	  //-------------------------------------------------------------------------------------------------------------------
	  
	  /**
	   * Download Assignment from server to local disk.
	   * @param e
	   */
	  public void download(ActionEvent e){ //Download Assignment from server.
		  
		  Assignment ass = null;
		  String selected = assignmentListView.getSelectionModel().getSelectedItem();
		 		  
		  if (selected == null)
			  return;
		
		  selected = selected.substring(selected.indexOf('(') + 1, selected.indexOf(')'));
		  
		  for (int i = 0; i < assignmentArr.size(); i++){
			  if (assignmentArr.get(i).getAssignmentID() == Integer.parseInt(selected))
				  ass = assignmentArr.get(i);
		  }
		  
		  if (ass == null)
			  return;
		  
		  String p = ass.getFilePath();
		  byte[] file = Assignment.getFile(p);
		  
		  FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Save Assignment");
          fileChooser.getExtensionFilters().addAll(
                  new FileChooser.ExtensionFilter("All Files", "*.*"),
                  new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                  new FileChooser.ExtensionFilter("Word Documents", "*.docx")
              );
          
          File pa = fileChooser.showSaveDialog(assignmentListView.getScene().getWindow());
          Path savePath = Paths.get(pa.getAbsolutePath());
		  
          FileOutputStream stream;
          
          try {
        	  stream = new FileOutputStream(savePath.toString());
        	  stream.write(file);
        	  stream.close();
        	  JOptionPane.showMessageDialog(null, "Assignment Saved");
          }
          catch (IOException ex) {
        	  ex.printStackTrace();
        	  JOptionPane.showMessageDialog(null, 
					  "Download Failed!", "Error", JOptionPane.ERROR_MESSAGE);
          } 
	  }
	  
	  /**
	   * Filter course list results.
	   * @param e
	   */
	  public void CourseSearch(ActionEvent e){ // Filter course list results.
		  ArrayList<String> temp = new  ArrayList<String>();
	    	
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
			  guiMsg.setText("No assignment was chosen! Please choose assignment from the assignment list.");
			  return;
		  }
			  
		  if (file == null){
			  guiMsg.setText("No file was chosen to upload! Please browse for a file from the computer.");
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
					System.out.println("Server fatal error!");
				}
		  
		  synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}}
		  
		  //-----------------------------------------------------------------------------------------------------------------
		  
		  // Send file data to server. 
		  
		  if ((boolean) Main.client.getMessage()){
			  Path p =Paths.get(file.getAbsolutePath());
			  byte[] data = null;
			  
			  try {
				data = Files.readAllBytes(p);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			  Main.client.sendMessageToServer(data);
			  
			  synchronized (Main.client){try {
					Main.client.wait();
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}}
		  }
		  else{
			  guiMsg.setText("Send file info to server failed!");
			  return;
		  }
		  
		  //----------------------------------------------------------------------------------------------------------------
		  
		  // Insert new submission data into data base.
		  
		  if (Main.client.getMessage() == null){
			  guiMsg.setText("Send file data to server failed!");
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
					exp.printStackTrace();
				}
	    	synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}}
	    	
	    	if ((int)Main.client.getMessage() > 0){
	    		JOptionPane.showMessageDialog(null, "submission Successful");
	    		
	    	}
	    	else{
	    		JOptionPane.showMessageDialog(null, 
						  "submission Filed: Data base error!", "Error", JOptionPane.ERROR_MESSAGE);
	    	}
	    	clearScreen();
	  }
	  
	  /**
	   * Open file explorer for upload file.
	   * @param e
	   */
	  public void browse(ActionEvent e){ // Open file explorer for upload file.
		  
		  if (assignmentListView.getSelectionModel().getSelectedItem().isEmpty()){
			  guiMsg.setText("No assignment was chosen! Please choose assignment from the assignment list.");
			  return;
		  }
		  
		  FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Upload File");
		  file = fileChooser.showOpenDialog(courseSearch.getScene().getWindow());
		  if (file != null){
			  filePath.setText(file.getAbsolutePath());
			  submitBtn.setDisable(false);
		  }
		  else
			  guiMsg.setText("No file was selected!");
	  }

	  //-------------------------------------------------------------------------------------------------------------------
	  
	  /**
	   * Clear & update screen details.
	   */
	  private void clearScreen(){ // Clear & update screen details.
		  SplitPane pane = null;
		try {
			pane = FXMLLoader.load(getClass().getResource("/FXML/StudentSubmitAssignment.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		};
			Main.getRoot().setCenter(pane);
		}
	  
	  @Override
	  public void initialize(URL arg0, ResourceBundle arg1) { // Initialize window.
		  
		  file = null;
		  courseArr = Student.getCourse();
		  organizedCourseList = getCourseList(courseArr);
		  Collections.sort(organizedCourseList);
		  courseListView.setItems(FXCollections.observableArrayList(organizedCourseList));
		  
		  courseListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			    	int courseID = -1;
			    	for (int i = 0; i < organizedCourseList.size(); i++)
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
			    }
			}); 
		  
		  assignmentListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			    	
			    	guiMsg.setText("- Download Assignment,\n- Submit Assignment");
			    	uploadLabel.setDisable(false);
			    	browseBtn.setDisable(false);
			    	downloadBtn.setDisable(false);
			    }
			});  
	  }
}