package Controller;


import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import Entity.Action;
import Entity.NewStudenCoursePlacement;
import Entity.Student;
import application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class ChangeStudentAssignmentController {
	
	private Student student;
	private int currSem= -1;
	private NewStudenCoursePlacement newPlacement;
	private final ObservableList<CourseInfo> data =
	        FXCollections.observableArrayList();

	@FXML // fx:id="stdIdTxt"
    private TextField stdIdTxt; // Value injected by FXMLLoader

    @FXML // fx:id="searchBtn"
    private Button searchBtn; // Value injected by FXMLLoader

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
    
    @FXML // fx:id="crsIdtxt"
    private TextField crsIdtxt; // Value injected by FXMLLoader

    @FXML // fx:id="removeCrs"
    private Button removeCrs; // Value injected by FXMLLoader
    
    @FXML // fx:id="errorLbl"
    private Label errorLbl; // Value injected by FXMLLoader


    @FXML
    void search(ActionEvent event) {
    	data.clear();
    	String id = stdIdTxt.getText();
    	
    	int currSem = getCurrSem();
    	if(currSem > 0){
	    	HashMap<String, String> msg = new HashMap<>();
	    	// gets the student name, class from DB
	    	msg.put("msgType", "select");
	    	msg.put("query", "SELECT users.Name, Student_Class.ClassName FROM users, Student_Class WHERE users.ID = '" + id + "' AND Student_Class.StudentID = '" + id + "' AND Student_Class.Year = '" + currSem + "';");
	    	try{
	    		Main.client.sendMessageToServer(msg);
	    		synchronized (Main.client){
	    			Main.client.wait();
	    			ArrayList<String> array = (ArrayList<String>) Main.client.getMessage();
	    	    	if(!array.isEmpty()){
	    	    		//set the fields for student
	    	    		stdNameLbl.setText("Student name: " + array.get(0));
	    	    		ClsLbl.setText("Class room: " + array.get(1));
	    	    		student = new Student();
	    	    		student.setID(id);
	    	    		student.setName(array.get(1));
	    	    		student.setName(array.get(0));
	    	    		// gets all the course the student is taken this semester
	    	    		msg.put("query", "SELECT CourseID FROM Course_Student WHERE StudentID = '" + id + "';");
	    	    		Main.client.sendMessageToServer(msg);
	    	    		Main.client.wait();
	    	    		array = (ArrayList<String>) Main.client.getMessage();
	    	    		setCourses(array);
	    	    	}else{
	    	    		// student id was not found
	    	    		student = null;
	    	    		Thread thread = new Thread(new Runnable(){
	    					@Override
	    					public void run() {
	    						JOptionPane.showMessageDialog(null, 
	    								  "Student id was not found!", "NOT FOUND", JOptionPane.ERROR_MESSAGE);		
	    					}
	    				});
	    	    		thread.start();
	    	    	}
	    		}
	    	}catch(Exception e){
	    		student = null;
	    		e.printStackTrace();
	    	}
    	}else{
    		errorLbl.setText("Connection error!");
    	}
    	
    }
    
    @FXML
    void removeCourse(ActionEvent event) {
    	//checks if student was searched
    	if(student != null){
    		//gets course id from the textBox
	    	String courseId  = crsIdtxt.getText();
	    	try {
				if(isCourseIdOk(courseId)){
					if(isSignedToCourse(courseId)){
						//sends new request to the principal
						newPlacement = new NewStudenCoursePlacement();
						newPlacement.setCoureID(courseId);
						newPlacement.setStudentID(student.getID());
						newPlacement.setAction(Action.remove);
						if(sendNewAssignment(newPlacement) > 0){
							errorLbl.setText("The reqest was sent to the principal.");
							errorLbl.setTextFill(Color.web("#0000ff"));
						}else{
							errorLbl.setText("This request has already been sent.");
							errorLbl.setTextFill(Color.web("#ff0000"));
						}
					}else{
						errorLbl.setText("Student not assigned to course");
						errorLbl.setTextFill(Color.web("#ff0000"));
					}
				}else{
					errorLbl.setText("Course doesn't exist!");
					errorLbl.setTextFill(Color.web("#ff0000"));
				}
			} catch (InterruptedException e) {
				errorLbl.setText("Connection ERROR");
				errorLbl.setTextFill(Color.web("#ff0000"));
				e.printStackTrace();
			}
    	}
    }
    
    @FXML
    void AssignNewCourse(ActionEvent event) {
    	//checks if student was searched
    	if(student != null){
    		//gets course id from the textBox
	    	String courseId  = crsIdtxt.getText();
	    	try {
				if(isCourseIdOk(courseId)){
					//checks if the course id inserted is valid
					if(!isSignedToCourse(courseId)){
						//if the student not signed to the course sends a new request to the principal
						newPlacement = new NewStudenCoursePlacement();
						newPlacement.setCoureID(courseId);
						newPlacement.setStudentID(student.getID());
						newPlacement.setAction(Action.assign);
						if(sendNewAssignment(newPlacement) > 0){
							errorLbl.setText("The reqest was sent to the principal.");
							errorLbl.setTextFill(Color.web("#0000ff"));
						}else{
							errorLbl.setText("This request has already been sent.");
							errorLbl.setTextFill(Color.web("#ff0000"));
						}
					}else{
						errorLbl.setText("Student is allready assigned to course");
						errorLbl.setTextFill(Color.web("#ff0000"));
					}
				}else{
					errorLbl.setText("Course doesn't exist!");
					errorLbl.setTextFill(Color.web("#ff0000"));
				}
			} catch (InterruptedException e) {
				errorLbl.setText("Connection ERROR");
				errorLbl.setTextFill(Color.web("#ff0000"));
				e.printStackTrace();
			}
    	}
    }
    
    //sends new request to the principal
    private int sendNewAssignment(NewStudenCoursePlacement newPlacement) throws InterruptedException{
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
    private boolean isSignedToCourse(String courseId) throws InterruptedException{
    	HashMap<String, String> msg = new HashMap<>();
    	ArrayList<String> msgFromServer;
    	
    	//checks if student signed for course in DB
    	msg.put("msgType", "select");
    	msg.put("query", "SELECT 1 FROM Course_Student WHERE CourseID = '" + courseId + "' AND StudentID = '" + student.getID() + "';");
    	Main.client.sendMessageToServer(msg);
		synchronized (Main.client){
			Main.client.wait();
			msgFromServer = (ArrayList<String>) Main.client.getMessage();
			if(msgFromServer.isEmpty()){
				return false;
			}else{
				return true;
			}
		}
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
				e.printStackTrace();
			}
		}
		return currSem;
    }
    
    //checks if the course id in the textbox is valid
    private boolean isCourseIdOk(String courseId) throws InterruptedException{
    	HashMap<String, String> msg = new HashMap<>();
    	ArrayList<String> msgFromServer;
    	
    	//checks if the course exists in the DB
    	msg.put("msgType", "select");
    	msg.put("query", "SELECT 1 FROM Course WHERE CourseID = '" + courseId + "';");
    	Main.client.sendMessageToServer(msg);
		synchronized (Main.client){
			Main.client.wait();
			msgFromServer = (ArrayList<String>) Main.client.getMessage();
			if(msgFromServer.isEmpty()){
				return false;
			}else{
				return true;
			}
		}
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
    
}
