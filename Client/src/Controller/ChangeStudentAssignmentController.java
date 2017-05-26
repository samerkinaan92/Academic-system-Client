package Controller;

import java.util.ArrayList;
import java.util.HashMap;

import Entity.Course;
import Entity.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ChangeStudentAssignmentController {
	
	Student student;
	ArrayList<Course> courses;

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
    private TableView<?> crsTbl; // Value injected by FXMLLoader

    @FXML
    void search(ActionEvent event) {
    	String id = stdIdTxt.getText();
    	HashMap<String, String> msg = new HashMap<>();
    	msg.put("msgType", "select");
    	
    	
    }
    
    @FXML
    void AssignNewCourse(ActionEvent event) {

    }
}
