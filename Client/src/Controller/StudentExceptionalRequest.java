package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class StudentExceptionalRequest implements Initializable{
	
	private int currSem= -1;
	private final ObservableList<StudRequestInfo> data =
	        FXCollections.observableArrayList();
	
	@FXML // fx:id="studTbl"
    private TableView<StudRequestInfo> studTbl; // Value injected by FXMLLoader

    @FXML // fx:id="studIdCln"
    private TableColumn<StudRequestInfo, String> studIdCln; // Value injected by FXMLLoader

    @FXML // fx:id="studNameCln"
    private TableColumn<StudRequestInfo, String> studNameCln; // Value injected by FXMLLoader

    @FXML // fx:id="CorsCln"
    private TableColumn<StudRequestInfo, String> CorsCln; // Value injected by FXMLLoader

    @FXML // fx:id="requestCln"
    private TableColumn<StudRequestInfo, String> requestCln; // Value injected by FXMLLoader

    @FXML // fx:id="aprvBtn"
    private Button aprvBtn; // Value injected by FXMLLoader

    @FXML // fx:id="DisaprvBtn"
    private Button DisaprvBtn; // Value injected by FXMLLoader

    @FXML
    void approve(ActionEvent event) {
    	StudRequestInfo requestInfo = studTbl.getSelectionModel().selectedItemProperty().get();
    	
    	//show confirmation dialog
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to approve it?\nThe change will be permanent");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
    		if(requestInfo.getRequest().equals("assign")){
        		assignToCourse(requestInfo);
        	}else{
        		removeFromCourse(requestInfo);
        	}
    	}
    }

    @FXML
    void disapprove(ActionEvent event) {
    	StudRequestInfo requestInfo = studTbl.getSelectionModel().selectedItemProperty().get();
    	
    	//show confirmation dialog
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to disapprove it?\nThe change will be permanent");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
    		deleteRow(requestInfo);
    	}
    }
    
    //remove the student from course
    private void removeFromCourse(StudRequestInfo requestInfo){
    	HashMap<String, String> msg = new HashMap<>();
    	
    	//remove from DB
    	msg.put("msgType", "delete");
    	msg.put("query", "DELETE FROM Course_Student WHERE CourseID = " + requestInfo.getCourseId() + " AND StudentID = " + requestInfo.getId() + " AND semesterId = " + getCurrSem() + ";");
    	
    	synchronized (Main.client) {
			try {
				Main.client.sendMessageToServer(msg);
				Main.client.wait();
				//delete from table
				deleteRow(requestInfo);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    //Assigns the student to the course
    private void assignToCourse(StudRequestInfo requestInfo){
    	HashMap<String, String> msg = new HashMap<>();
    	
    	msg.put("msgType", "insert");
    	msg.put("query", "INSERT INTO Course_Student(CourseID, StudentID,semesterId) VALUES (" + requestInfo.getCourseId() + ", " + requestInfo.getId() + ", " + getCurrSem() + ");");
    	
    	Main.client.sendMessageToServer(msg);
    	synchronized (Main.client) {
			try {
				Main.client.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	deleteRow(requestInfo);
    }
    
    //gets the current semester id
    private int getCurrSem(){
    	if(currSem != -1){
    		return currSem;
    	}
    	HashMap<String, String> msg = new HashMap<>();
    	msg.put("msgType", "select");
    	msg.put("query", "SELECT semesterId FROM Semester WHERE isCurr = 1;");
    	
    	Main.client.sendMessageToServer(msg);
		synchronized (Main.client){
			try {
			Main.client.wait();
			ArrayList<String> serverMsg = (ArrayList<String>) Main.client.getMessage();
	    	if(!serverMsg.isEmpty()){
	    		currSem = Integer.parseInt(serverMsg.get(0));
	    	}
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return currSem;
    }
    
    // delete row from DB and table
    private void deleteRow(StudRequestInfo requestInfo){
    	HashMap<String, String> msg = new HashMap<>();
    	
    	msg.put("msgType", "delete");
    	msg.put("query", "DELETE FROM NewStudentAssignment WHERE StudentID = '" + requestInfo.getId() + "' AND CourseID = '" + requestInfo.getCourseId() + "';");
    	
    	Main.client.sendMessageToServer(msg);
    	synchronized (Main.client) {
			try {
				Main.client.wait();
				int msgFromServer = (int)Main.client.getMessage();
		    	if(msgFromServer > 0){
		    		data.remove(requestInfo);
		    	}else{
		    		Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialog");
					alert.setHeaderText(null);
					alert.setContentText("Connection error!!");
					alert.show();

		    	}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	
    	//disable the button a row is selected 
    	aprvBtn.setDisable(true);
    	DisaprvBtn.setDisable(true);
    	
    	// sets listener for enabling the buttons
    	studTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	aprvBtn.setDisable(false);
		    	DisaprvBtn.setDisable(false);
		    }else{
		    	aprvBtn.setDisable(true);
		    	DisaprvBtn.setDisable(true);
		    }
		});
    	
    	//sets the columns string from the class
    	studIdCln.setCellValueFactory(new PropertyValueFactory<StudRequestInfo, String>("id"));
    	studNameCln.setCellValueFactory(new PropertyValueFactory<StudRequestInfo, String>("name"));
    	CorsCln.setCellValueFactory(new PropertyValueFactory<StudRequestInfo, String>("course"));
    	requestCln.setCellValueFactory(new PropertyValueFactory<StudRequestInfo, String>("request"));
    	
    	setStudentsRequestsTbl();
	}
    
    //set all the students requests in the table
    private void setStudentsRequestsTbl(){
    	HashMap<String, String> msg = new HashMap<>();
    	ArrayList<String> msgFromServer;
    	
    	//gets the request from the DB
    	msg.put("msgType", "select");
    	msg.put("query", "SELECT R.StudentID, U.Name, C.CourseName, R.action, R.CourseID FROM NewStudentAssignment R, Course C, Users U WHERE R.StudentID = U.ID AND C.CourseID = R.CourseID");
    	
    	Main.client.sendMessageToServer(msg);
    	synchronized (Main.client) {
			try {
				Main.client.wait();
				msgFromServer = (ArrayList<String>)Main.client.getMessage();
		    	
		    	for(int i = 0; i < msgFromServer.size(); i += 5){
		    		data.add(new StudRequestInfo(msgFromServer.get(i), msgFromServer.get(i + 1), msgFromServer.get(i + 2), msgFromServer.get(i + 3), msgFromServer.get(i + 4)));
		    	}
		    	
		    	studTbl.setItems(data);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
	
    //class for the students requests table
    public static class StudRequestInfo{
    	private StringProperty name;
        public void setName(String value) { nameProperty().set(value); }
        public String getName() { return nameProperty().get(); }
        public StringProperty nameProperty() { 
            if (name == null) name = new SimpleStringProperty(this, "name");
            return name; 
        }
    
        private StringProperty id;
        public void setId(String value) { idProperty().set(value); }
        public String getId() { return idProperty().get(); }
        public StringProperty idProperty() { 
            if (id == null) id = new SimpleStringProperty(this, "id");
            return id; 
        } 
        
        private StringProperty course;
        public void setCourse(String value) { courseProperty().set(value); }
        public String getCourse() { return courseProperty().get(); }
        public StringProperty courseProperty() { 
            if (course == null) course = new SimpleStringProperty(this, "course");
            return course; 
        }
        
        private StringProperty request;
        public void setRequest(String value) { requestProperty().set(value); }
        public String getRequest() { return requestProperty().get(); }
        public StringProperty requestProperty() { 
            if (request == null) request = new SimpleStringProperty(this, "request");
            return request; 
        }
        
        private StringProperty courseId;
        public void setCourseId(String value) { courseIdProperty().set(value); }
        public String getCourseId() { return courseIdProperty().get(); }
        public StringProperty courseIdProperty() { 
            if (courseId == null) courseId = new SimpleStringProperty(this, "courseId");
            return courseId; 
        } 
        
        public StudRequestInfo(String studentId, String studentName, String courseName, String request, String courseId){
        	setName(studentName);
        	setId(studentId);
        	setCourse(courseName);
        	setRequest(request);
        	setCourseId(courseId);
        }
    }
}
