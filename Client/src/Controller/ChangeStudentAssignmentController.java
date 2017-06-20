package Controller;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.naming.spi.DirStateFactory.Result;

import Controller.ChangeTeacherPlacementController.Classes;
import Entity.Action;
import Entity.Course;
import Entity.NewStudenCoursePlacement;
import Entity.Student;
import Entity.TeachingUnit;
import Entity.claSS;
import application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

/**
 * this controller handles the changing student course or class
 * @author Samer Kinaan
 *
 */
public class ChangeStudentAssignmentController implements Initializable{
	
	/**
	 * the student downing the changes
	 */
	private Student student;
	
	/**
	 * current semester
	 */
	private int currSem= -1;
	
	/**
	 * data for courses taken by the student to present in table
	 */
	private final ObservableList<CourseInfo> data =
	        FXCollections.observableArrayList();
	
	/**
	 *  Value injected by FXMLLoader
	 */
	@FXML // fx:id="stdIdLbl"
	private Label stdIdLbl; // Value injected by FXMLLoader

	/**
	 *  Value injected by FXMLLoader
	 */
	@FXML // fx:id="stdIdTxt"
    private TextField stdIdTxt; // Value injected by FXMLLoader

	/**
	 *  Value injected by FXMLLoader
	 */
    @FXML // fx:id="stdNameLbl"
    private Label stdNameLbl; // Value injected by FXMLLoader

    /**
	 *  Value injected by FXMLLoader
	 */
    @FXML // fx:id="ClsLbl"
    private Label ClsLbl; // Value injected by FXMLLoader

    /**
	 *  Value injected by FXMLLoader
	 */
    @FXML // fx:id="newAsnBtn"
    private Button newAsnBtn; // Value injected by FXMLLoader

    /**
	 *  Value injected by FXMLLoader
	 */
    @FXML // fx:id="crsTbl"
    private TableView<CourseInfo> crsTbl; // Value injected by FXMLLoader

    /**
	 *  Value injected by FXMLLoader
	 */
    @FXML // fx:id="nameCln"
    private TableColumn<CourseInfo, String> nameCln; // Value injected by FXMLLoader

    /**
	 *  Value injected by FXMLLoader
	 */
    @FXML // fx:id="idCln"
    private TableColumn<CourseInfo, String> idCln; // Value injected by FXMLLoader

    /**
	 *  Value injected by FXMLLoader
	 */
    @FXML // fx:id="removeCrs"
    private Button removeCrs; // Value injected by FXMLLoader
 
    /**
	 *  Value injected by FXMLLoader
	 */
    @FXML // fx:id="tuChosBox"
    private ChoiceBox<String> tuChosBox; // Value injected by FXMLLoader

    /**
	 *  Value injected by FXMLLoader
	 */
    @FXML // fx:id="corsChosBox"
    private ChoiceBox<Course> corsChosBox; // Value injected by FXMLLoader
 
    /**
	 *  Value injected by FXMLLoader
	 */
    @FXML // fx:id="classChoiceBox"
    private ChoiceBox<String> classChoiceBox; // Value injected by FXMLLoader

    /**
	 *  Value injected by FXMLLoader
	 */
    @FXML // fx:id="moveClassBtn"
    private Button moveClassBtn; // Value injected by FXMLLoader
 
    /**
     * move student to another class
     * @param event
     */
    @FXML
    void moveStudentToClass(ActionEvent event) {
    	String newClass = classChoiceBox.getSelectionModel().getSelectedItem();
    	Alert alert;
    	if(newClass.equals(student.getClassRoom())){
    		alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Invalid input");
			alert.setHeaderText(null);
			alert.setContentText("Invalid input you choose the same class room the student currently in!!\nPlease choose another class room.");
			alert.show();
    	}else{
    		alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Confirmation Dialog");
        	alert.setHeaderText(null);
        	alert.setContentText("Are you sure you want to move student to another class room?");

        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.get() == ButtonType.OK){
        		try {
					moveStudentToClass(newClass);
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
    }

    /**
     * send request for removing student from choosing course
     * @param event
     */
    @FXML
    void removeCourse(ActionEvent event) {
    	CourseInfo courseInfo = crsTbl.getSelectionModel().getSelectedItem();
    	NewStudenCoursePlacement newPlacement = new NewStudenCoursePlacement(student.getID(), courseInfo.getId(), Action.remove);
    	//show confirmation dialog
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to remove student from course?");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
	    	try {
				if(sendNewRequest(newPlacement) > 0){
					//send messages to users
					String title = "Request was sent to principal";;
					String msg = "Hello\nRequest for removing you from course " + courseInfo.getName() + " was sent to the principal.";
					sendMsg(title, msg, student.getID(), null, false);
					msg = "Hello\nYou have a new changing course requset to handle.";
					sendMsg(title, msg, null, "Principal", true);
					
					//show dialog
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Request sent");
					alert.setHeaderText(null);
					alert.setContentText("The request has been sent to the principal successfully");
					alert.show();
				}else{
					alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Request sent");
					alert.setHeaderText(null);
					alert.setContentText("The request has been already sent before!!\nPlease wait for the pricipal.");
					alert.show();
				}
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
     * send request for assigning student to choosing course
     * @param event
     */
    @FXML
    void AssignNewCourse(ActionEvent event) {
    	Course selectedCourse = corsChosBox.getSelectionModel().getSelectedItem();
    	String courseId = Integer.toString(selectedCourse.getCourseID());
    	
    	//show confirmation dialog
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to assign " + student.getName() + " to course " + selectedCourse.getName() + "?");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
	    	if(!isSignedToCourse(courseId)){
	    		NewStudenCoursePlacement newPlacement = new NewStudenCoursePlacement(student.getID(), courseId, Action.assign);
	    		try {
					if(sendNewRequest(newPlacement) > 0){
						String title = "Request was sent to principal";;
						String msg = "Hello\nRequest for assigning you for course " + selectedCourse.getName() + " was sent to the principal.";
						sendMsg(title, msg, student.getID(), null, false);
						msg = "Hello\nYou have a new changing course requset to handle.";
						sendMsg(title, msg, null, "Principal", true);
						
						// show dialog
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Request sent");
						alert.setHeaderText(null);
						alert.setContentText("The request has been sent to the principal successfully");
						alert.show();
					}else{
						alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Request sent");
						alert.setHeaderText(null);
						alert.setContentText("The request has been already sent before!!\nPlease wait for the pricipal.");
						alert.show();
					}
				} catch (InterruptedException e) {
					alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialog");
					alert.setHeaderText(null);
					alert.setContentText("Connection error!!");
					alert.show();
					e.printStackTrace();
				}
	    	}else{
	    		alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Invalid input");
				alert.setHeaderText(null);
				alert.setContentText("Student is already assigned to course!!");
				alert.show();
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
  
    //TODO move student from class
    private boolean moveStudentToClass(String classRoom) throws InterruptedException{
    	boolean moved = false;
    	HashMap<String, String> msg = new HashMap<>();
    	
    	msg.put("msgType", "update");
    	msg.put("query", "UPDATE Student_Class SET ClassName = '" + classRoom + "' WHERE StudentID = '" + student.getID() + "';");
    	
    	synchronized (Main.client) {
			Main.client.sendMessageToServer(msg);
			Main.client.wait();
			int result = (int) Main.client.getMessage();
			if(result > 0){
				moved = true;
			}
		}
    	return moved;
    }
    
    /**
     * sends new request to the principal
     * @param newPlacement to be sent to the principal
     * @return	if the request was sent
     * @throws InterruptedException
     */
    private int sendNewRequest(NewStudenCoursePlacement newPlacement) throws InterruptedException{
    	HashMap<String, String> msg = new HashMap<>();
    	int msgFromServer;
    	
    	//insert new request in to DB
    	msg.put("msgType", "insert");
    	msg.put("query", "INSERT INTO NewStudentAssignment (action, StudentID, CourseID ) VALUES ('" + newPlacement.getAction() + "', " + newPlacement.getStudentID() + " , " + newPlacement.getCoureID() + ");");
    	Main.client.sendMessageToServer(msg);
		synchronized (Main.client){
			Main.client.wait();
			msgFromServer = (int) Main.client.getMessage();
		}
		return msgFromServer;
    }
    
    /**
     * checks if the student already signed to the course
     * @param courseId course id
     * @return	true if the student is assigned to the course, else false
     */
    private boolean isSignedToCourse(String courseId){
    	for(int i = 0; i < data.size(); i++){
    		if(data.get(i).id.equals(courseId))
    			return true;
    	}
    	return false;
    }
    
    /**
     * get the current semester id
     * @return	the id of the current semester
     */
    private int getCurrSem(){
    	// if the currSem variable is not -1 then no need to get it from the DB
    	if(currSem != -1){
    		return currSem;
    	}
    	
    	//gets the current semester id from the DB
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
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Connection error!!");
				alert.show();
				e.printStackTrace();
			}
		}
		return currSem;
    }

    /**
     * gets all the class room in school
     * @return	array list of all the class room in the school
     * @throws InterruptedException
     */
    private ArrayList<String> getClassRooms() throws InterruptedException{
    	HashMap <String,String> msgServer = new HashMap <String,String>();
    	ArrayList<String> result = null;
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select * From class");
		
		synchronized (Main.client){
			Main.client.sendMessageToServer(msgServer);
			Main.client.wait();
			result = (ArrayList<String>)Main.client.getMessage();
		}
		return result;
    }
    
    /**
     * set the table with the courses taken by 
     * the student this semester.
     * 
     * @param coursesIds all the courses id taken by the student
     * @throws InterruptedException
     */
    private void setCourses(ArrayList<String> coursesIds) throws InterruptedException{
    	String courseId, courseName;
    	HashMap<String, String> msg = new HashMap<>();
    	ArrayList<String> msgFromServer;
    	
    	//set the columns String from CourseInfo class
    	nameCln.setCellValueFactory(new PropertyValueFactory<CourseInfo, String>("name"));
    	idCln.setCellValueFactory(new PropertyValueFactory<CourseInfo, String>("id"));
    	
    	//gets the name of the courses
    	msg.put("msgType", "select");
    	if(!coursesIds.isEmpty()){
    		for(int i = 0; i < coursesIds.size(); i++){
    			courseId = coursesIds.get(i);
    			msg.put("query", "SELECT CourseName FROM Course WHERE CourseID = '" + courseId + "'");
    			Main.client.sendMessageToServer(msg);
        		synchronized (Main.client){
        			Main.client.wait();
        			msgFromServer = (ArrayList<String>) Main.client.getMessage();
        			courseName = msgFromServer.get(0);
        			data.add(new CourseInfo(courseName, courseId));
        		}
    		}
    		crsTbl.setItems(data);
    	}
    }
    
    
    /**
     * sets and get all the data of the student
     * 
     * @param student student instance
     */
    public void setStudent(Student student){
    	HashMap<String, String> msg = new HashMap<>();
    	ArrayList<String> array = (ArrayList<String>) Main.client.getMessage();
    	
    	this.student = student;
    	//set the fields for student
		stdNameLbl.setText("Student name: " + student.getName());
		ClsLbl.setText("Class room: " + student.getClassRoom());
		stdIdLbl.setText("Student id: " + student.getID());
		
		// gets all the course the student is taken this semester
		msg.put("msgType", "select");
		msg.put("query", "SELECT CourseID FROM Course_Student WHERE StudentID = '" + student.getID() + "' AND semesterId = '" + getCurrSem() + "';");
		synchronized (Main.client) {
			Main.client.sendMessageToServer(msg);
			try {
				Main.client.wait();		
				array = (ArrayList<String>) Main.client.getMessage();
				setCourses(array);
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
    
    /**
     * class for courses taken by the student
     * @author Samer Kinaan
     *
     */
    public static class CourseInfo{
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
        
        public CourseInfo(String courseName, String courseId){
        	setName(courseName);
        	setId(courseId);
        }
    }

	/**
	 * initialize the listeners for all the nodes
	 */
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	ArrayList<String> teachingunits;
    	ArrayList<String> classes = null;
		removeCrs.setDisable(true);
		newAsnBtn.setDisable(true);
		corsChosBox.setDisable(true);
		moveClassBtn.setDisable(true);
		
		teachingunits = TeachingUnit.getTeachingUnit();
		tuChosBox.setItems(FXCollections.observableArrayList(teachingunits));
		
		try {
			classes = getClassRooms();
		} catch (InterruptedException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Connection error!!");
			alert.show();
			e.printStackTrace();
		}
		classChoiceBox.setItems(FXCollections.observableArrayList(classes));
		
		tuChosBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					ArrayList<Course> courses = Course.getCourses(teachingunits.get(newValue.intValue()));
					corsChosBox.setItems(FXCollections.observableArrayList(courses));
					corsChosBox.setDisable(false);
					newAsnBtn.setDisable(true);
				}
	          });
		
		corsChosBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				newAsnBtn.setDisable(false);
			}
          });
		
		classChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				moveClassBtn.setDisable(false);
			}
          });
		
		crsTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	removeCrs.setDisable(false);
		    }else{
		    	removeCrs.setDisable(true);
		    }
		});
	}
    
}
