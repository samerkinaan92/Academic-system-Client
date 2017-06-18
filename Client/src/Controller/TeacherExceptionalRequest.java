package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
* this controller handles the exceptional change teacher request
* @author Samer Kinaan
*
*/
public class TeacherExceptionalRequest implements Initializable {
	
	/**
	 * ObservableList for the request table 
	 */
	private final ObservableList<TeacherRequestInfo> data =
	        FXCollections.observableArrayList();
	
	@FXML // fx:id="excepTbl"
    private TableView<TeacherRequestInfo> excepTbl; // Value injected by FXMLLoader

    @FXML // fx:id="currTeacherCln"
    private TableColumn<TeacherRequestInfo, String> currTeacherCln; // Value injected by FXMLLoader

    @FXML // fx:id="newTeacherCln"
    private TableColumn<TeacherRequestInfo, String> newTeacherCln; // Value injected by FXMLLoader

    @FXML // fx:id="CorsCln"
    private TableColumn<TeacherRequestInfo, String> CorsCln; // Value injected by FXMLLoader

    @FXML // fx:id="classCln"
    private TableColumn<TeacherRequestInfo, String> classCln; // Value injected by FXMLLoader

    @FXML // fx:id="aprvBtn"
    private Button aprvBtn; // Value injected by FXMLLoader

    @FXML // fx:id="DisaprvBtn"
    private Button DisaprvBtn; // Value injected by FXMLLoader

    /**
     * approve the request when the approve button clicks
     * @param event
     */
    @FXML
    void approve(ActionEvent event) {
    	TeacherRequestInfo requestInfo = excepTbl.getSelectionModel().getSelectedItem();
    	//show confirmation dialog
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to approve it?\nThe change will be permanent");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
    		try {
				changeTeacher(requestInfo);
	    		String title = "The request was approved";
				String msg = "Hello\nThe request for changing " 
						+ requestInfo.getCurrTeacherName() + " to " + requestInfo.getNewTeacherName() 
						+ " in class " + requestInfo.getClassRoom() + " was approved.";
				sendMsg(title, msg, requestInfo.getCurrTeacherId(), null, false);
				sendMsg(title, msg, requestInfo.getNewTeacherId(), null, false);
				sendMsg(title, msg, null, "Secretary", true);
	    	} catch (InterruptedException e) {
	    		alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Connection error!!");
				alert.show();
				e.printStackTrace();
			}
    	}
    }

    /**
     * disapproves the request when the disapprove button clicks
     * @param event
     */
    @FXML
    void disapprove(ActionEvent event) {
    	TeacherRequestInfo requestInfo = excepTbl.getSelectionModel().getSelectedItem();
    	//show confirmation dailog
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to disapprove it?\nThe change will be permanent");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
    		try {
	    		deleteRow(requestInfo);
	    		String title = "The request was disapproved";
				String msg = "Hello\nThe request for changing " 
						+ requestInfo.getCurrTeacherName() + " to " + requestInfo.getNewTeacherName() 
						+ " in class " + requestInfo.getClassRoom() + " was disapproved.";
				sendMsg(title, msg, requestInfo.getCurrTeacherId(), null, false);
				sendMsg(title, msg, requestInfo.getNewTeacherId(), null, false);
				sendMsg(title, msg, null, "Secretary", true);
    		} catch (InterruptedException e) {
	    		alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Connection error!!");
				alert.show();
				e.printStackTrace();
			}
    	} 
    }
    
    
    /**
     * send message to user
     * @param title	 the title of the message
     * @param msg	the message body
     * @param id	the user id to send to
     * @param role	the role of the receiver
     * @param group	false if for 1 receiver, true if for a job type
     * @throws InterruptedException		if interrupted will waiting for message from the server
     */
    private void sendMsg(String title, String msg, String id, String role, boolean group) throws InterruptedException{
    	HashMap<String, String> msgToServer = new HashMap<>();
    	
    	msgToServer.put("msgType", "insert");
    	if(!group){
	    	msgToServer.put("query", "INSERT INTO messages (`sendTime`, `title`, `message`, `from`, `to`) "
	    			+ "VALUES (now(), '" + title + "', '" + msg + "', '" + Main.user.getID() + "', '" + id + "');");
	    	
	    	synchronized (Main.client) {
	    		Main.client.sendMessageToServer(msgToServer);
	    		Main.client.wait();
			}
    	}else{
    		ArrayList<String> ids = getUserTypeIds(role);
    		for(int i = 0; i < ids.size(); i++){
    			msgToServer.put("query", "INSERT INTO messages(`sendTime`, `title`, `message`, `from`, `to`) "
		    			+ "VALUES (now(), '" + title + "', '" + msg + "', '" + Main.user.getID() + "', '" + ids.get(i) + "');");
		    	
		    	synchronized (Main.client) {
		    		Main.client.sendMessageToServer(msgToServer);
		    		Main.client.wait();
				}
    		}
    	}
    }
    
    /**
     * gets all the users id with role
     * @param type	role of the group of user
     * @return	array list containing all the ids of the role
     * @throws InterruptedException if interrupted will waiting for message from the server
     */
    private ArrayList<String> getUserTypeIds(String type) throws InterruptedException{
    	HashMap<String, String> msgToServer = new HashMap<>();
    	ArrayList<String> ids = null;
    	
    	msgToServer.put("msgType", "select");
    	msgToServer.put("query", "SELECT ID FROM users WHERE Role = '" + type + "';");
    	
    	synchronized (Main.client) {
    		Main.client.sendMessageToServer(msgToServer);
    		Main.client.wait();
    		ids = (ArrayList<String>) Main.client.getMessage();
		}
    	return ids;
    }
    
    /**
     *  change teacher teaching class
     * @param requestInfo request info
     * @throws InterruptedException
     */
    private void changeTeacher(TeacherRequestInfo requestInfo) throws InterruptedException{
    	HashMap<String, String> msg = new HashMap<>();
    	
    	//updates the teacher in course class
    	msg.put("msgType", "update");
    	msg.put("query", "UPDATE Class_Course SET teacherID = '" + requestInfo.getNewTeacherId() + "' WHERE id = '" + requestInfo.getCourseClassId() + "';");
    	
    	Main.client.sendMessageToServer(msg);
    	synchronized (Main.client) {
			Main.client.wait();
			deleteRow(requestInfo);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("The teacher have been changed");
			alert.show();
		}
    }
    
    /**
     *  delete row from DB and table
     * @param requestInfo request info
     * @throws InterruptedException
     */
    private void deleteRow(TeacherRequestInfo requestInfo) throws InterruptedException{
    	HashMap<String, String> msg = new HashMap<>();
    	
    	//delete from DB
    	msg.put("msgType", "delete");
    	msg.put("query", "DELETE FROM NewTeacherPlacement WHERE Class_Courseid = '" + requestInfo.getCourseClassId() + "';");
    	
    	Main.client.sendMessageToServer(msg);
    	synchronized (Main.client) {
			Main.client.wait();
			int msgFromServer = (int)Main.client.getMessage();
	    	if(msgFromServer > 0){
	    		//delete from table
	    		data.remove(requestInfo);
	    	}
		}
    }
    
    /**
     *  set the changing teacher requests table items
     */
    private void setTeachersReqestsTbl(){
    	HashMap<String, String> msg = new HashMap<>();
    	ArrayList<String> msgFromServer;
    	
    	msg.put("msgType", "select");
    	msg.put("query", "SELECT U1.Name, U2.Name, CR.CourseName, CL.ClassName, R.Class_Courseid, R.newTeacherID "
    			+ "FROM NewTeacherPlacement R, Users U1, Users U2, Course CR, Class CL, Class_Course CLCR "
    			+ "WHERE R.newTeacherID = U2.ID AND R.currTeacherID = U1.ID AND R.Class_Courseid = CLCR.id AND CLCR.CourseID = CR.CourseID AND CLCR.ClassName = CL.ClassName;");
    	
    	Main.client.sendMessageToServer(msg);
    	synchronized (Main.client) {
			try {
				Main.client.wait();
				msgFromServer = (ArrayList<String>)Main.client.getMessage();
		    	
		    	for(int i = 0; i < msgFromServer.size(); i += 6){
		    		data.add(new TeacherRequestInfo(msgFromServer.get(i), msgFromServer.get(i + 1), msgFromServer.get(i + 2), msgFromServer.get(i + 3), msgFromServer.get(i + 4), msgFromServer.get(i + 5)));
		    	}
		    	
		    	excepTbl.setItems(data);
			} catch (InterruptedException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Connection error!!");
				alert.show();
				e.printStackTrace();
			}
		}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		aprvBtn.setDisable(true);
    	DisaprvBtn.setDisable(true);
    	
    	//set listener for enabling the button on select item
    	excepTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	aprvBtn.setDisable(false);
		    	DisaprvBtn.setDisable(false);
		    }else{
		    	aprvBtn.setDisable(true);
		    	DisaprvBtn.setDisable(true);
		    }
		});
    	
    	// sets the columns string from the class
    	currTeacherCln.setCellValueFactory(new PropertyValueFactory<TeacherRequestInfo, String>("currTeacherName"));
    	newTeacherCln.setCellValueFactory(new PropertyValueFactory<TeacherRequestInfo, String>("newTeacherName"));
    	CorsCln.setCellValueFactory(new PropertyValueFactory<TeacherRequestInfo, String>("courseName"));
    	classCln.setCellValueFactory(new PropertyValueFactory<TeacherRequestInfo, String>("classRoom"));
    	
    	setTeachersReqestsTbl();
	}
	
	/**
	 *  class for the changing teacher request table
	 * @author Samer Kinaan
	 *
	 */
	public static class TeacherRequestInfo{
		private String courseClassId, currTeacherId, newTeacherId;
		
    	private StringProperty currTeacherName;
        public void setCurrTeacherName(String value) { currTeacherNameProperty().set(value); }
        public String getCurrTeacherName() { return currTeacherNameProperty().get(); }
        public StringProperty currTeacherNameProperty() { 
            if (currTeacherName == null) currTeacherName = new SimpleStringProperty(this, "currTeacherName");
            return currTeacherName; 
        }
        
        private StringProperty newTeacherName;
        public void setNewTeacherName(String value) { newTeacherNameProperty().set(value); }
        public String getNewTeacherName() { return newTeacherNameProperty().get(); }
        public StringProperty newTeacherNameProperty() { 
            if (newTeacherName == null) newTeacherName = new SimpleStringProperty(this, "newTeacherName");
            return newTeacherName; 
        }
        
        private StringProperty courseName;
        public void setCourseName(String value) { courseNameProperty().set(value); }
        public String getCourseName() { return courseNameProperty().get(); }
        public StringProperty courseNameProperty() { 
            if (courseName == null) courseName = new SimpleStringProperty(this, "courseName");
            return courseName; 
        }
        
        private StringProperty classRoom;
        public void setClassRoom(String value) { classRoomProperty().set(value); }
        public String getClassRoom() { return classRoomProperty().get(); }
        public StringProperty classRoomProperty() { 
            if (classRoom == null) classRoom = new SimpleStringProperty(this, "classRoom");
            return classRoom; 
        }
        
        
        
        public String getCourseClassId() {
			return courseClassId;
		}
		
		public void setCourseClassId(String courseClassId) {
			this.courseClassId = courseClassId;
		}
		
		
		
		
		public String getCurrTeacherId() {
			return currTeacherId;
		}
		public String getNewTeacherId() {
			return newTeacherId;
		}
		public void setCurrTeacherId(String currTeacherId) {
			this.currTeacherId = currTeacherId;
		}
		public void setNewTeacherId(String newTeacherId) {
			this.newTeacherId = newTeacherId;
		}
		
		public TeacherRequestInfo(String currTeacherName, String newTeacherName, String courseName, String classRoom, String courseClassId, String newTeacherId){
        	setCurrTeacherName(currTeacherName);
        	setNewTeacherName(newTeacherName);
        	setCourseName(courseName);
        	setClassRoom(classRoom);
        	setCourseClassId(courseClassId);
        	setNewTeacherId(newTeacherId);
        }
    }

}
