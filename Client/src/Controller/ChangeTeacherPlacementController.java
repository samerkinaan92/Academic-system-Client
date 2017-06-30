package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import Entity.Course;
import Entity.NewTeacherPlacement;
import Entity.TeachingUnit;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * 
 * the controller ChangeTeacherPlacementController contains all the methods to handle events from client.
 * this controller was built to handle all code for changing teacher from clss
 * 
 * @author SAMER KINAAN
 *
 */
public class ChangeTeacherPlacementController implements Initializable{
		
	/**
	 * variable for save the request to send
	 */
	private NewTeacherPlacement newPlacement;
	
	/**
	 * the current semester id
	 */
	private int currSem= -1;
	
	/**
	 * course weekly hours to teach
	 */
	private int weeklyHours;
	
	/**
	 * save all the data to present in classes table
	 */
	private final ObservableList<Classes> classesData =
	        FXCollections.observableArrayList();
	
	/**
	 * save all the data to present in teachers table
	 */
	private final ObservableList<Teachers> teachersData =
	        FXCollections.observableArrayList();


	/**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="tuChosBox"
    private ChoiceBox<String> tuChosBox;

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="corsChosBox"
    private ChoiceBox<Course> corsChosBox; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="classesTbl"
    private TableView<Classes> classesTbl; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="clsRomCln"
    private TableColumn<Classes, String> clsRomCln; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="tchrNmCln"
    private TableColumn<Classes, String> tchrNmCln; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="tchrIdCln"
    private TableColumn<Classes, String> tchrIdCln; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="crsNmLbl"
    private Label crsNmLbl; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="tuLbl"
    private Label tuLbl; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="wklyHourLbl"
    private Label wklyHourLbl; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="changeTchrBtn"
    private Button changeTchrBtn; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="teachersTbl"
    private TableView<Teachers> teachersTbl; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="tchrIdTcln"
    private TableColumn<Teachers, String> tchrIdTcln; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="tchrNmTcln"
    private TableColumn<Teachers, String> tchrNmTcln; // Value injected by FXMLLoader

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML // fx:id="horsLftTcln"
    private TableColumn<Teachers, String> horsLftTcln; // Value injected by FXMLLoader

    /**
     * sends change teacher request 
     * @param event changeTeacher button clicked
     */
    @FXML
    void changeTeacher(ActionEvent event) {
    	// gets the selected items
    	Teachers teacher = teachersTbl.getSelectionModel().getSelectedItem();
    	Classes selClass = classesTbl.getSelectionModel().getSelectedItem();
    	
    	
    	//show confirmation dialog
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to replace teacher in class " + selClass.getClassName() + "?");

    	Optional<ButtonType> conBtn = alert.showAndWait();
    	if (conBtn.get() == ButtonType.OK){
    	//checks if the same teacher was picked
	    	if(teacher.getTeacherId().equals(selClass.getTeacherId())){
	    		alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("INVALID INPUT");
				alert.setHeaderText(null);
				alert.setContentText("Invalid input! you choose the same teacher! \nPlaese choose another teacher");
				alert.show();
	    	}else{
	    		// checks if the chosen teacher to replace have enough hours for the course
	    		if(Integer.parseInt(teacher.getHoursLeft()) < weeklyHours){
	    			//if not show an error dialog
		    		alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("NO HOURS LEFT");
					alert.setHeaderText(null);
					alert.setContentText("The teacher you choose will exceed the maximum amount of hours allowed!\nDo you want to send the request?");
					conBtn = alert.showAndWait();
					
					if (conBtn.get() == ButtonType.CANCEL){
						return;
					}
	    		}
    			//sends a new request to the principal
    			newPlacement = new NewTeacherPlacement();
    			newPlacement.setCourseID(selClass.getCourseInClassId());
    			newPlacement.setCurrTeacherID(selClass.getTeacherId());
    			newPlacement.setNewTeacherID(teacher.getTeacherId());
    			
    			HashMap<String, String> msg = new HashMap<>();
    	    	msg.put("msgType", "insert");
    	    	msg.put("query", "INSERT INTO NewTeacherPlacement (newTeacherID, currTeacherID, Class_Courseid) VALUES (" + newPlacement.getNewTeacherID() + ", " + newPlacement.getCurrTeacherID() + ", " + newPlacement.getCourseID() + ");");
    	    	synchronized (Main.client) {
    	    		Main.client.sendMessageToServer(msg);
    				try {
						Main.client.wait();
						int result = (int)Main.client.getMessage();
						if(result > 0){
							String title = "Request was sent to principal";
							String MailMsg = "Hello\nRequest for changing " + selClass.getTeacherName() + " in class " + selClass.getClassName() + " to " + teacher.getTeacherName() + " was sent to pricipal approval.";
							sendMsg(title, MailMsg, selClass.getTeacherId(), null, false);
							sendMsg(title, MailMsg, teacher.getTeacherId(), null, false);
							MailMsg = "Hello\nYou have a new changing teacher requset to handle.";
							sendMsg(title, MailMsg, null, "Principal", true);
		    	    		alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Request sent");
							alert.setHeaderText(null);
							alert.setContentText("The request has been sent to the principal successfully");
							alert.show();
						}else{
		    	    		alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("FAILED!");
							alert.setHeaderText(null);
							alert.setContentText("Request for changing teacher for this class already exists!");
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
    	
    	// msg to the server
    	msgToServer.put("msgType", "insert");
    	if(!group){
	    	msgToServer.put("query", "INSERT INTO messages (`sendTime`, `title`, `message`, `from`, `to`) "
	    			+ "VALUES (now(), '" + title + "', '" + msg + "', '" + Main.user.getID() + "', '" + id + "');");
	    	
	    	//send message
	    	synchronized (Main.client) {
	    		Main.client.sendMessageToServer(msgToServer);
	    		Main.client.wait();
			}
    	}else{
    		//send to group of user
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
     * gets the current semester id
     * 
     * @return the id of the current semester
     */
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
    
    /**
     * set the course fields visibility
     * @param visible if true sets to visible
     */
    private void setLblVisible(boolean visible){
    	crsNmLbl.setVisible(visible);
    	tuLbl.setVisible(visible);
    	wklyHourLbl.setVisible(visible);
    }
    
    /**
     * sets the course fields
     * @param course	the course to set the fields of
     */
    private void setCourseFields(Course course){
    	crsNmLbl.setText("Course name: " + course.getName());
    	wklyHourLbl.setText("Teaching unit: " + course.getTUID());
    	tuLbl.setText("Weekly hours: " + course.getWeeklyHours());
    	weeklyHours = course.getWeeklyHours();
    }
    
    /**
     * sets all the classes taken the course this semester to the table
     * @param course	the course that all the classes are studying it
     * @throws InterruptedException 	if interrupted will waiting for message from the server
     */
    private void setCourseInClasses(Course course) throws InterruptedException{
    	HashMap<String, String> msg = new HashMap<>();
    	ArrayList<String> msgFromServer;
    	
    	msg.put("msgType", "select");
    	msg.put("query", "select CC.ClassName, U.Name, CC.teacherID, CC.id from Class_Course CC, Users U where CC.CourseID = '" + course.getCourseID() + "' AND U.ID = CC.teacherID AND CC.semesterId = '" + getCurrSem() + "';");
    	synchronized (Main.client) {
    		Main.client.sendMessageToServer(msg);
    		Main.client.wait();
    		msgFromServer = (ArrayList<String>) Main.client.getMessage();
    	}
    	classesData.clear();
    	for(int i = 0; i < msgFromServer.size(); i += 4){
			classesData.add(new Classes(msgFromServer.get(i), msgFromServer.get(i + 1), msgFromServer.get(i + 2), msgFromServer.get(i + 3)));	
    	}
    	classesTbl.setItems(classesData);
    }
    

    /**
     * sets all the teachers details in teaching unit in the table
     * @param teachingUnit	the teaching unit of the course
     * @throws InterruptedException
     */
    private void setTeachersTable(String teachingUnit) throws InterruptedException{
    	HashMap<String, String> msg = new HashMap<>();
    	ArrayList<String> msgFromServer;
    	int maxHours, workHours, sum;
    	String teacherId, teacherName;
    	
    	teachersData.clear();
    	msg.put("msgType", "select");
    	msg.put("query", "SELECT TUT.TeacherID, U.Name, T.MaxWorkHours FROM TeachingUnit_Teacher TUT, Users U, Teacher T WHERE TUName = '" + teachingUnit + "' AND U.ID = TUT.TeacherID AND T.TeacherID = TUT.TeacherID;");
    	synchronized (Main.client) {
    		Main.client.sendMessageToServer(msg);
			Main.client.wait();
			msgFromServer = (ArrayList<String>)Main.client.getMessage();
	    	
	    	for(int i = 0; i < msgFromServer.size(); i += 3){
	    		teacherId = msgFromServer.get(i);
	    		teacherName = msgFromServer.get(i + 1);
	    		maxHours = Integer.parseInt(msgFromServer.get(i + 2));
	    		workHours = getSumHours(teacherId);
	    		if(maxHours != -1 && workHours != -1){
	    			sum = maxHours - workHours;
	    			if(sum < 0)
	    				sum = 0;
	    		}else{
	    			sum = 0;
	    		}
	    		teachersData.add(new Teachers(String.valueOf(sum), teacherName, teacherId));
	    	}
	    	teachersTbl.setItems(teachersData);
		}
    }
    
    /**
     * gets the total hours the teacher is working during week
     * @param teacherId the teacher id
     * @return	the total hours the teacher is working during week
     * @throws InterruptedException
     */
    private int getSumHours(String teacherId) throws InterruptedException{
    	HashMap<String, String> msg = new HashMap<>();
    	ArrayList<String> msgFromServer;
    	
    	msg.put("msgType", "select");
    	msg.put("query", 
    			"select sum(C.WeeklyHours) from Course C where C.CourseID IN (SELECT CC.CourseID FROM Class_Course CC WHERE CC.teacherID = '" 
    					+ teacherId + "' AND semesterId = '" + getCurrSem() + "');");
    	synchronized (Main.client) {
    		Main.client.sendMessageToServer(msg);
			Main.client.wait();
			msgFromServer = (ArrayList<String>)Main.client.getMessage();
	    	if(!msgFromServer.isEmpty() && msgFromServer.get(0) != null)
	    		return Integer.parseInt(msgFromServer.get(0));
	    	else
	    		return 0;
		}
    }
    
    /**
     * initialize all the data for the gui
     */
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	ArrayList<String> teachingunits;
    	corsChosBox.setDisable(true);
		changeTchrBtn.setDisable(true);
		setLblVisible(false);
		//sets listener for item changed, makes sure that both tables items selected to enable the button
		classesTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null && teachersTbl.getSelectionModel().getSelectedItem() != null) {
		    	changeTchrBtn.setDisable(false);
		    }else{
		    	changeTchrBtn.setDisable(true);
		    }
		});
		
		//sets listener for item changed, makes sure that both tables items selected to enable the button 
		teachersTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null && classesTbl.getSelectionModel().getSelectedItem() != null) {
		    	changeTchrBtn.setDisable(false);
		    }else{
		    	changeTchrBtn.setDisable(true);
		    }
		});
		
		teachingunits = TeachingUnit.getTeachingUnit();
		tuChosBox.setItems(FXCollections.observableArrayList(teachingunits));
		
		tuChosBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue != null){
					ArrayList<Course> courses = Course.getCourses(newValue);
					corsChosBox.setItems(FXCollections.observableArrayList(courses));
					corsChosBox.setDisable(false);
					classesData.clear();
					try {
						setTeachersTable(newValue);
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
		});
		
		corsChosBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Course>() {

			@Override
			public void changed(ObservableValue observable, Course oldValue, Course newValue) {
				if(newValue != null){
					setLblVisible(true);
					setCourseFields(newValue);
					try {
						setCourseInClasses(newValue);
						setTeachersTable(tuChosBox.getSelectionModel().getSelectedItem());
					} catch (InterruptedException e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Dialog");
						alert.setHeaderText(null);
						alert.setContentText("Connection error!!");
						alert.show();
						e.printStackTrace();
					}
				}else{
					setLblVisible(false);
				}
			}
		});
		
		//sets the column string from the class
		clsRomCln.setCellValueFactory(new PropertyValueFactory<Classes, String>("className"));
    	tchrNmCln.setCellValueFactory(new PropertyValueFactory<Classes, String>("teacherName"));
    	tchrIdCln.setCellValueFactory(new PropertyValueFactory<Classes, String>("teacherId"));
    	
    	tchrIdTcln.setCellValueFactory(new PropertyValueFactory<Teachers, String>("teacherId"));
    	tchrNmTcln.setCellValueFactory(new PropertyValueFactory<Teachers, String>("teacherName"));
    	horsLftTcln.setCellValueFactory(new PropertyValueFactory<Teachers, String>("hoursLeft"));
	}
    
    /**
     * class for the classes table
     * @author SAMER KINAAN
     *
     */
    public static class Classes{
    	private StringProperty className;
        public void setClassName(String value) { classNameProperty().set(value); }
        public String getClassName() { return classNameProperty().get(); }
        public StringProperty classNameProperty() { 
            if (className == null) className = new SimpleStringProperty(this, "className");
            return className; 
        }
    
        private StringProperty teacherName;
        public void setTeacherName(String value) { teacherNameProperty().set(value); }
        public String getTeacherName() { return teacherNameProperty().get(); }
        public StringProperty teacherNameProperty() { 
            if (teacherName == null) teacherName = new SimpleStringProperty(this, "teacherName");
            return teacherName; 
        } 
        
        
        private StringProperty teacherId;
        public void setTeacherId(String value){ teacherIdProperty().set(value);}
        public String getTeacherId(){ return teacherIdProperty().get();}
        public StringProperty teacherIdProperty() {
        	if(teacherId == null) teacherId = new SimpleStringProperty(this, "teacherId");
        	return teacherId;
        }
        
        private StringProperty courseInClassId;
        public void setCourseInClassId(String value){ courseInClassIdProperty().set(value);}
        public String getCourseInClassId(){ return courseInClassIdProperty().get();}
        public StringProperty courseInClassIdProperty() {
        	if(courseInClassId == null) courseInClassId = new SimpleStringProperty(this, "courseInClassId");
        	return courseInClassId;
        }
        
        public Classes(String className, String teacherName, String teacherId, String courseInClassId){
        	setClassName(className);
        	setTeacherName(teacherName);
        	setTeacherId(teacherId);
        	setCourseInClassId(courseInClassId);
        }
    }

    /**
     * class for the teachers table
     * @author SAMER KINAAN
     *
     */
    public static class Teachers{
	    	private StringProperty hoursLeft;
	        public void setHoursLeft(String value) { hoursLeftProperty().set(value); }
	        public String getHoursLeft() { return hoursLeftProperty().get(); }
	        public StringProperty hoursLeftProperty() { 
	            if (hoursLeft == null) hoursLeft = new SimpleStringProperty(this, "hoursLeft");
	            return hoursLeft; 
	        }
	    
	        private StringProperty teacherName;
	        public void setTeacherName(String value) { teacherNameProperty().set(value); }
	        public String getTeacherName() { return teacherNameProperty().get(); }
	        public StringProperty teacherNameProperty() { 
	            if (teacherName == null) teacherName = new SimpleStringProperty(this, "teacherName");
	            return teacherName; 
	        } 
	        
	        
	        private StringProperty teacherId;
	        public void setTeacherId(String value){ teacherIdProperty().set(value);}
	        public String getTeacherId(){ return teacherIdProperty().get();}
	        public StringProperty teacherIdProperty() {
	        	if(teacherId == null) teacherId = new SimpleStringProperty(this, "teacherId");
	        	return teacherId;
	        }
	        
	        public Teachers(String hoursLeft, String teacherName, String teacherId){
	        	setHoursLeft(hoursLeft);
	        	setTeacherName(teacherName);
	        	setTeacherId(teacherId);
	        }
	    }
}
