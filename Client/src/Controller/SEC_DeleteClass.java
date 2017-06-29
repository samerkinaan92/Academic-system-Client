package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import Controller.SEC_DefineClasses.DBFreeStudent;
import Entity.Message;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

/**	Controller class for: "SEC_DeleteClass.fxml"
 * @author Nadav Zrihan
 *  */
public class SEC_DeleteClass implements Initializable {
	
	/** this class represent 'CLASS' object in DataBase  */
    class DBclass{	
    	String Name;
    	public DBclass (){}
    	public DBclass (String name) {
    		this.Name = name;
    	}
		public String toString() {
			return this.Name;
		}
    }

	/**	holds list of strings (each represents CLASS ) to be displayed @ combo box	*/
	private final ObservableList<String> classesOBSRlist = FXCollections.observableArrayList();
    
    /** used to send MSG to server */
    HashMap<String, String> sentMSG = new HashMap<>();
    
    /** holds all class objects */
    ArrayList<DBclass> classes = new ArrayList<DBclass>();

    @FXML
    private ComboBox<String> classesCB;

    @FXML
    private Button delBTN;

    /**	delete button event handler */
    @FXML
    void delBTNaction(ActionEvent event) throws IOException {
    	
    	String selectedClass = classesCB.getSelectionModel().getSelectedItem().toString();
    	
    	if ( showsDetails(selectedClass) ) { 	// if user is sure he wants to delete the class
    		alertAllStudentsInClass(selectedClass);
    		alertAllTeachersInClass(selectedClass);
    		deleteClass(selectedClass);
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		getClassesFromDB();
		setClassesInComboBox();
		
		delBTN.setTooltip(new Tooltip("delete selected class"));
		
	}
	
	@SuppressWarnings("unchecked")
	private void alertAllStudentsInClass(String className) {
		
		ArrayList<String> students = null;
		
		sentMSG.put("msgType", "select");
    	sentMSG.put("query","SELECT SC.StudentID FROM student_class SC WHERE SC.ClassName='"+className+"'");
		Main.client.sendMessageToServer(sentMSG);
		
		synchronized (Main.client) {		
			try {
				Main.client.wait();
				students = (ArrayList<String>)Main.client.getMessage();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		String title = "Class Removal!";
		String MSG = "You have been removed from the class: "+className;
		
		for (int i=0;i<students.size();i++)
			Message.sendMsg(new Message(title,MSG, Integer.parseInt(Main.user.getID()),Integer.parseInt(students.get(i))));
		
	}
	
	@SuppressWarnings("unchecked")
	private void alertAllTeachersInClass(String className) {
		
		ArrayList<String> teachers = null;
		
		sentMSG.put("msgType", "select");
    	sentMSG.put("query","SELECT teacherID FROM class_course where ClassName='"+className+"'");
		Main.client.sendMessageToServer(sentMSG);
		
		synchronized (Main.client) {		
			try {
				Main.client.wait();
				teachers = (ArrayList<String>)Main.client.getMessage();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		String title = "Class Removal!";
		String MSG = "You have been removed from the class: "+className+"\n";
		
		for (int i=0;i<teachers.size();i++)
			Message.sendMsg(new Message(title,MSG, Integer.parseInt(Main.user.getID()),Integer.parseInt(teachers.get(i))));
		
	}
	
	/**  Deletes a class from the DB
	 * 
	 * 	@param className Class to be deleted */
	private void deleteClass (String className) {
		
		sentMSG.put("msgType", "update");			/*	delete all attached class_course entries from DB	*/
		sentMSG.put("query", "DELETE FROM `mat`.`class_course` WHERE `ClassName`='"+className+"';");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {
			try {
				Main.client.wait();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		sentMSG.put("msgType", "update");		/*	delete all attached student_class entries from DB	*/
		sentMSG.put("query", "DELETE FROM `mat`.`student_class` WHERE `ClassName`='"+className+"';");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {
			try {
				Main.client.wait();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		sentMSG.put("msgType", "update");		/*	delete attached class entrie from DB	*/
		sentMSG.put("query", "DELETE FROM `mat`.`class` WHERE `ClassName`='"+className+"';");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {
			try {
				Main.client.wait();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		classesOBSRlist.remove(className);
		classesCB.setItems(classesOBSRlist);
		classesCB.getSelectionModel().selectFirst();
		if (classesOBSRlist.isEmpty())
			delBTN.setDisable(true);
		
		showInfoMSG("Class "+className+" has been deleted!","");
		
	}
	
	
	
	/**	shows last message before class is deleted
	 * @param className Class Name
	 * @return true if user sure he want to delete the class
	 *  */
	@SuppressWarnings("unchecked")
	private boolean showsDetails(String className) {
		
		ArrayList<String> studentsFromDB = null;
		ArrayList<String> coursesFromDB = null;
		
		sentMSG.put("msgType", "select");
    	sentMSG.put("query", "SELECT U.ID, U.Name FROM student_class SC, users U WHERE className='"+className+"' and U.ID=SC.StudentID");
		Main.client.sendMessageToServer(sentMSG);
		
		synchronized (Main.client) {		
			try {
				Main.client.wait();
				studentsFromDB = (ArrayList<String>)Main.client.getMessage();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		sentMSG.put("msgType", "select");
    	sentMSG.put("query", "SELECT C.CourseName FROM class_course CC, course C WHERE className ='"+className+"' and CC.courseID=C.courseID");
		Main.client.sendMessageToServer(sentMSG);
		
		synchronized (Main.client) {		
			try {
				Main.client.wait();
				coursesFromDB = (ArrayList<String>)Main.client.getMessage();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		String studentsNames="";
		
		if (!studentsFromDB.isEmpty()) {
			
			studentsNames = "Students: \n";			// get those students
			for (int i=0;i<studentsFromDB.size();i+=2) 
				studentsNames = studentsNames.concat( "(" + studentsFromDB.get(i) + " ," + studentsFromDB.get(i+1) + ")  " );
			
		}
		
		String coursesNames="";
		
		if (!coursesFromDB.isEmpty()) {
			coursesNames = "\nCourses: \n";			// get those students
			for (int i=0;i<coursesFromDB.size();i++) 
				coursesNames = coursesNames.concat( "("+coursesFromDB.get(i)+")  " );
		}
		
		String title ="";
		title = title.concat("Students found in class "+className+":\t"+(studentsFromDB.size()/2)+
				"\n"+"Courses found in class "+className+":\t"+coursesFromDB.size());

		boolean result = showConfirmMSG(title,studentsNames+"\n"+coursesNames+"\n\nAre you sure you want to delete class "+className+"?");
		return result;

	}
	
	/**	get student objectives from DB */
	@SuppressWarnings("unchecked")
	private void getClassesFromDB() {
		
		ArrayList<String> classesFromDB = null;
		sentMSG.put("msgType", "select");
    	sentMSG.put("query", "SELECT * FROM class");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {		
			try {
				Main.client.wait();
				classesFromDB = (ArrayList<String>)Main.client.getMessage();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		
		
		for (int i=0;i<classesFromDB.size();i++) {
			DBclass c = new DBclass();
			c.Name = classesFromDB.get(i);
			classes.add(c);
    	}
	}
	
	/**	initialize class ComboBox with strings representing classes	*/
    private void setClassesInComboBox() {

		for (int i=0;i<classes.size();i++) {
			classesOBSRlist.add(classes.get(i).toString());
    	}
		
		if (classesOBSRlist.isEmpty()) {
			delBTN.setDisable(true);
			classesCB.setDisable(true);
			showInfoMSG("There are 0 classes in the system", "");
		}
		else {
			classesCB.setItems(classesOBSRlist);			/*	POPULATE COMBOBOX COLLECTION	*/
			classesCB.getSelectionModel().selectFirst();
		}
  
    	
    }
    
	/**	shows information message using GUI
	 * 
	 * @param title 'title of the screen'
	 * @param MSG 'message to be displayed'
	 * */
	private void showInfoMSG(String title, String MSG) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(title);
		alert.setContentText(MSG);
		alert.showAndWait();
		
	}



	/**	shows confirmation message using GUI
	 * 
	 * @param title 'title of the screen'
	 * @param MSG 'message to be displayed'
	 * @return true for acceptence of the MSG, false otherwise
	 * */
	private boolean showConfirmMSG (String title, String MSG) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(title);
		alert.setContentText(MSG);

		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK)
			return true;
		return false;
		
	}
}
