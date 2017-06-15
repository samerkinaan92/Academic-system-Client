package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Controller.StudentExceptionalRequest.StudRequestInfo;
import Entity.Student;
import application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.Initializable;

public class PickStudent implements Initializable {
	
	private int currSem= -1;
	private final ObservableList<ClassInfo> clsData =
	        FXCollections.observableArrayList();
	
	private final ObservableList<StudInfo> stdData =
	        FXCollections.observableArrayList();
	
	@FXML // fx:id="clsRomTbl"
    private TableView<ClassInfo> clsRomTbl; // Value injected by FXMLLoader

    @FXML // fx:id="clsRomCln"
    private TableColumn<ClassInfo, String> clsRomCln; // Value injected by FXMLLoader

    @FXML // fx:id="stdTbl"
    private TableView<StudInfo> stdTbl; // Value injected by FXMLLoader

    @FXML // fx:id="stdNmCln"
    private TableColumn<StudInfo, String> stdNmCln; // Value injected by FXMLLoader

    @FXML // fx:id="stdIdCln"
    private TableColumn<StudInfo, String> stdIdCln; // Value injected by FXMLLoader

    @FXML // fx:id="slctClsBtn"
    private Button slctClsBtn; // Value injected by FXMLLoader

    @FXML // fx:id="slctStdBtn"
    private Button slctStdBtn; // Value injected by FXMLLoader

    @FXML
    void selectClass(ActionEvent event) {
    	HashMap<String, String> msgToSrv = new HashMap<>();
    	ArrayList<String> msgFromSrv;
    	
    	ClassInfo classInfo = clsRomTbl.getSelectionModel().getSelectedItem();
    	
    	stdData.clear();
    	
    	msgToSrv.put("msgType", "select");
    	msgToSrv.put("query", "select SC.StudentID, U.Name from Student_Class SC, users U where SC.ClassName = '" + classInfo.getName() + "' AND U.ID = SC.StudentID;");
    	
    	Main.client.sendMessageToServer(msgToSrv);
    	synchronized (Main.client) {
			try {
				Main.client.wait();
				msgFromSrv = (ArrayList<String>) Main.client.getMessage();
				for(int i = 0; i < msgFromSrv.size(); i += 2){
					stdData.add(new StudInfo(msgFromSrv.get(i), msgFromSrv.get(i + 1)));
				}
						
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

    @FXML
    void selectStudent(ActionEvent event) {
    	StudInfo studInfo = stdTbl.getSelectionModel().getSelectedItem();
    	ClassInfo classInfo = clsRomTbl.getSelectionModel().getSelectedItem();
    	Student student = new Student();
    	student.setID(studInfo.getId());
    	student.setName(studInfo.getName());
    	student.setClassRoom(classInfo.getName());
    	
    	String fxml_url = "/FXML/Sec_chng_std_assign.fxml";
		URL paneUrl = getClass().getResource(fxml_url);
		AnchorPane pane;
		ChangeStudentAssignmentController controller;
		FXMLLoader loader = new FXMLLoader();
		try{
			pane = loader.load(paneUrl.openStream());
			Main.getRoot().setCenter(pane);
			controller = (ChangeStudentAssignmentController) loader.getController();
			controller.setStudent(student);
		}catch (IOException ex) {
			//TODO 
			ex.printStackTrace();
		}
    }
    
    private void setClassRooms(){
    	HashMap<String, String> msgToSrv = new HashMap<>();
    	ArrayList<String> msgFromSrv;
    	
    	msgToSrv.put("msgType", "select");
    	msgToSrv.put("query", "select ClassName from Class;");
    	
    	synchronized (Main.client) {
    		Main.client.sendMessageToServer(msgToSrv);
			try {
				Main.client.wait();
				msgFromSrv = (ArrayList<String>) Main.client.getMessage();
				for(int i = 0; i < msgFromSrv.size(); i++){
					clsData.add(new ClassInfo(msgFromSrv.get(i)));
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		slctClsBtn.setDisable(true);
		slctStdBtn.setDisable(true);
		
		clsRomTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	slctClsBtn.setDisable(false);
		    }else{
		    	slctClsBtn.setDisable(true);
		    }
		});
		
		stdTbl.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	slctStdBtn.setDisable(false);
		    }else{
		    	slctStdBtn.setDisable(true);
		    }
		});
		
		clsRomTbl.setItems(clsData);
		stdTbl.setItems(stdData);
		
		clsRomCln.setCellValueFactory(new PropertyValueFactory<ClassInfo, String>("name"));
		stdNmCln.setCellValueFactory(new PropertyValueFactory<StudInfo, String>("name"));
		stdIdCln.setCellValueFactory(new PropertyValueFactory<StudInfo, String>("id"));
		
		setClassRooms();
	}
	
	public static class ClassInfo{
    	private StringProperty name;
        public void setName(String value) { nameProperty().set(value); }
        public String getName() { return nameProperty().get(); }
        public StringProperty nameProperty() { 
            if (name == null) name = new SimpleStringProperty(this, "name");
            return name; 
        }
        
        public ClassInfo(String name){
        	setName(name);
        }
    }
	
	//class for the students requests table
    public static class StudInfo{
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
        
        public StudInfo(String studentId, String studentName){
        	setName(studentName);
        	setId(studentId);
        }
    }

}
