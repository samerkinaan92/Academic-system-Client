package Controller;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import Entity.Action;
import Entity.Course;
import Entity.NewStudenCoursePlacement;
import Entity.Student;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class ChangeStudentAssignmentController implements Initializable{
	
	private Student student;
	private int currSem= -1;
	private final ObservableList<CourseInfo> data =
	        FXCollections.observableArrayList();
	
	@FXML // fx:id="stdIdLbl"
	private Label stdIdLbl; // Value injected by FXMLLoader

	@FXML // fx:id="stdIdTxt"
    private TextField stdIdTxt; // Value injected by FXMLLoader


    @FXML // fx:id="stdNameLbl"
    private Label stdNameLbl; // Value injected by FXMLLoader

    @FXML // fx:id="ClsLbl"
    private Label ClsLbl; // Value injected by FXMLLoader

    @FXML // fx:id="newAsnBtn"
    private Button newAsnBtn; // Value injected by FXMLLoader

    @FXML // fx:id="crsTbl"
    private TableView<CourseInfo> crsTbl; // Value injected by FXMLLoader
    
    @FXML // fx:id="nameCln"
    private TableColumn<CourseInfo, String> nameCln; // Value injected by FXMLLoader

    @FXML // fx:id="idCln"
    private TableColumn<CourseInfo, String> idCln; // Value injected by FXMLLoader

    @FXML // fx:id="removeCrs"
    private Button removeCrs; // Value injected by FXMLLoader
    
    @FXML // fx:id="tuChosBox"
    private ChoiceBox<String> tuChosBox; // Value injected by FXMLLoader

    @FXML // fx:id="corsChosBox"
    private ChoiceBox<Course> corsChosBox; // Value injected by FXMLLoader

    
    @FXML
    void removeCourse(ActionEvent event) {
    	//show confirmation dialog
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to remove student from course?");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
	    	CourseInfo courseInfo = crsTbl.getSelectionModel().getSelectedItem();
	    	NewStudenCoursePlacement newPlacement = new NewStudenCoursePlacement(student.getID(), courseInfo.getId(), Action.remove);
	    	try {
				if(sendNewRequest(newPlacement) > 0){
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
    
    @FXML
    void AssignNewCourse(ActionEvent event) {
    	Course selectedCourse = corsChosBox.getSelectionModel().getSelectedItem();
    	String courseId = Integer.toString(selectedCourse.getCourseID());
    	
    	//show confirmation dialog
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to assign student" + student.getID() + " to course " + selectedCourse.getName() + "?");

    	Optional<ButtonType> result = alert.showAndWait();
    	if (result.get() == ButtonType.OK){
	    	if(!isSignedToCourse(courseId)){
	    		NewStudenCoursePlacement newPlacement = new NewStudenCoursePlacement(student.getID(), courseId, Action.assign);
	    		try {
					if(sendNewRequest(newPlacement) > 0){
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
    
    //sends new request to the principal
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
    
    //checks if the student already signed to the course
    private boolean isSignedToCourse(String courseId){
    	for(int i = 0; i < data.size(); i++){
    		if(data.get(i).id.equals(courseId))
    			return true;
    	}
    	return false;
    }
    
    //get the current semester id
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

    
    //set the table items list
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
    
    //class for courses taken by the student
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

	
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	ArrayList<String> teachingunits;
		removeCrs.setDisable(true);
		newAsnBtn.setDisable(true);
		corsChosBox.setDisable(true);
		
		teachingunits = TeachingUnit.getTeachingUnit();
		tuChosBox.setItems(FXCollections.observableArrayList(teachingunits));
		
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
		
		crsTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	removeCrs.setDisable(false);
		    }else{
		    	removeCrs.setDisable(true);
		    }
		});
	}
    
}
