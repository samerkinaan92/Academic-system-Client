package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import application.Main;
import application.MessageThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SystemManegerDefineCourseController implements Initializable {
	
	
	@FXML
    private Label nameErr;

    @FXML
    private Label idErr;

    @FXML
    private Label hoursErr;

    @FXML
    private Label unitErr;
	
    @FXML
    private ListView<String> courseList;
    
    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> weaklyHours;

    @FXML
    private ComboBox<String> teachingUnit;

    @FXML
    private TextField courseId;

    @FXML
    private ListView<String> preCourseList;

    @FXML
    private TextField courseName;
    
    String[] hours = {"1", "2", "3", "4"};
    String[] units = {"Software", "Mathematics", "Physics", "English"};
    
   
    
    ArrayList<String> result;
    ArrayList<String> organizedList;
    ArrayList<String> PreList = new ArrayList<String>();
    
    private final ObservableList<String> boxUnit = FXCollections.observableArrayList(units);
    private final ObservableList<String> boxHours = FXCollections.observableArrayList(hours);
    private ObservableList<String> coursesDB;
    
    private HashMap <String, String> msgServer;
    
    
    public void search(ActionEvent e){
    	ArrayList<String> temp = new  ArrayList<String>();
    	
    	for (int i = 0; i < organizedList.size(); i++)
    		if (organizedList.get(i).toLowerCase().contains(searchField.getText().toLowerCase()))
    			temp.add(organizedList.get(i));
    	
    	coursesDB  = FXCollections.observableArrayList(temp);
		courseList.setItems(coursesDB);
    }
    
    public void addPreCourse(ActionEvent e){
    	
    	for (int i = 0; i < PreList.size(); i++)
    		if (courseList.getSelectionModel().getSelectedItem().equals(PreList.get(i)))
    			return;
    	
    	PreList.add(courseList.getSelectionModel().getSelectedItem());
    	Collections.sort(PreList);
    	preCourseList.setItems(FXCollections.observableArrayList(PreList));
    }

    public void removePreCourse(ActionEvent e){
	
    	for (int i = 0; i < PreList.size(); i++)
    		if (preCourseList.getSelectionModel().getSelectedItem().equals(PreList.get(i)))
    			PreList.remove(i);
    	
    	Collections.sort(PreList);
    	preCourseList.setItems(FXCollections.observableArrayList(PreList));	
    }
    
    public void submitCourse(ActionEvent e) throws IOException{
    	
    	boolean err = false;
    	
    	// Reset Screen errors.
    	
    	idErr.setText("");
    	nameErr.setText("");
    	hoursErr.setText("");
    	unitErr.setText("");
    	
    	//--------------------------------------------------------------------------------------------------
    	
    	// Check for illegal input. //
    	
    	try{
    		Integer.parseInt(courseId.getText());
    		if (courseId.getText().isEmpty())
    			throw new Exception();
    	}
    	catch(Exception exp1){
    		idErr.setText("Course Id not valid!");
    		err = true;
    	}
    	
    	if (courseName.getText().isEmpty()){
    		nameErr.setText("Course Name not valid!");
    		err = true;
    	}
    	
    	if (weaklyHours.getSelectionModel().getSelectedItem() == null){
    		hoursErr.setText("Weakly Hours not selected!");
    		err = true;
    	}
    	
    	if (teachingUnit.getSelectionModel().getSelectedItem() == null){
    		unitErr.setText("Teaching Unit not selected!");
    		err = true;
    	}
    	
    	if (err)
    		return;
    	//---------------------------------------------------------------------------------------------------------
    	
    	// Verify course id in data base.
    	
    	if (checkId(courseId.getText())){
    		idErr.setText("Course ID already exists!");
    		return;
    	}
    	
    	//--------------------------------------------------------------------------------------------------------------
    	
    	
    	// Update course in data base. 
    	
    	String msg = "Insert INTO course (CourseID, Name, TUID, weeklyHours)";
    	String values = " VALUES (" + Integer.parseInt(courseId.getText()) + ", '" + courseName.getText() + "', '" + 
    			teachingUnit.getSelectionModel().getSelectedItem().toString() + "', " + 
    			weaklyHours.getSelectionModel().getSelectedItem().toString() + ")";
    	
    	msgServer = new HashMap <String,String>();
    	msgServer.put("msgType", "insert");
		msgServer.put("query", msg + values);
    	
    	try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				System.out.println("Server fatal error!");
			}
		MessageThread msgT = new MessageThread(Main.client);
		msgT.start();
		synchronized (msgT){try {
			msgT.wait();
		} catch (InterruptedException exp) {
			exp.printStackTrace();
		}}
    	
		
    	int numOfChanges = (int)Main.client.getMessage();
    	
    	//------------------------------------------------------------------------------------------------------------
    	
    	
    	// Update Precourses in data base.
    	
    	for (int i = 0; i < PreList.size(); i++){
    	
	    	msg = "Insert INTO PreRequests (CourseID, preReqCourseID)";
	    	values = " VALUES (" + Integer.parseInt(courseId.getText()) + ", " + 
	    	PreList.get(i).substring(PreList.get(i).indexOf('(') + 1, PreList.get(i).indexOf(')')) + ")";
	    	
	    	
	    	msgServer = new HashMap <String,String>();
	    	msgServer.put("msgType", "insert");
			msgServer.put("query", msg + values);
	    	
	    	try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					System.out.println("Server fatal error!");
				}
			msgT = new MessageThread(Main.client);
			msgT.start();
			synchronized (msgT){try {
				msgT.wait();
			} catch (InterruptedException exp) {
				exp.printStackTrace();
			}}
    	}
    	
    	//--------------------------------------------------------------------------------------------------------------
    	
    	numOfChanges += (int)Main.client.getMessage();
    	
    	if (numOfChanges >= 1){
	    	JOptionPane.showMessageDialog(null,"Course definition successful :)");
	    	clearScreen();
    	}
    	else
    		JOptionPane.showMessageDialog(null, 
					  "No changes were made!", "Error", JOptionPane.ERROR_MESSAGE);
    }
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1){
		
		loadDB();
		
		weaklyHours.setItems(boxHours);
		teachingUnit.setItems(boxUnit);
		
	}
	
	private void clearScreen() throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/SystemManagerDefineCourses.fxml"));;
		Main.getRoot().setCenter(pane);
	}
	
	@SuppressWarnings("unchecked")
	private void loadDB(){
		
		msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select Name,CourseID From course");
		
		try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				System.out.println("Server fatal error!");
			}
		MessageThread msgT = new MessageThread(Main.client);
		msgT.start();
		synchronized (msgT){try {
			msgT.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
		
		result = (ArrayList<String>)Main.client.getMessage();
		organizedList = getList(result);
		Collections.sort(organizedList);
		coursesDB  = FXCollections.observableArrayList(organizedList);
		
		courseList.setItems(coursesDB);
	}
	
	private ArrayList<String> getList(ArrayList<String> list){
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < list.size(); i+=2)
			temp.add(list.get(i) + " (" + list.get(i+1) + ")");
		
		return temp;	
	}
	
	private boolean checkId(String id){
		
		for (int i = 1; i < result.size(); i+=2)
			if (result.get(i).equals(id))
				return true;
		return false;
		
	}
	
	
}
