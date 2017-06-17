package Controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**	Controller class for: "SEC_DefineClasses.fxml"
 * 
 *   @author Nadav Zrihan*/
public class SEC_DefineClasses implements Initializable {
	
	/** this class represent FREE STUDENT object  */
    class DBFreeStudent{
    	
    	int ID;
    	String Name;
    
    	public DBFreeStudent (){}
    	
    	public DBFreeStudent (int id, String name) {
    		this.ID = id;
    		this.Name = name;
    	}

		public String toString() {
			return this.ID+"\t"+this.Name;
		}
    }
    
    /** used to send MSG to server */
    HashMap<String, String> sentMSG = new HashMap<>();	
    
    /** string's array represent classes in DB */
    ArrayList<String> classesFromDB;
    
    /** string's array represent students without class in DB */
    ArrayList<String> studentsFromDB;
    
    /** DBFreeStudent array represent students obects without class in DB */
    ArrayList<DBFreeStudent> freeStudents;
    
    /**		holds list of strings (each represents free student) to be displayed @ left list	*/
    private final ObservableList<String> leftLISTstudents = FXCollections.observableArrayList();
    
    /**		holds list of strings (each represents free student) to be displayed @ right list	*/
    private final ObservableList<String> rightLISTstudents = FXCollections.observableArrayList();
    
    /**		holds list of char's to be displayed @ spinner	*/
    private final ObservableList<Character> letters = FXCollections.observableArrayList('A','B','C','D','E','F','G');    

    @FXML
    private Label redLABEL;

    @FXML
    private Label blueLABEL;

    @FXML
    private Button checkBTN;

    @FXML
    private Label classNameLABEL;	// prompt label

    @FXML
    private ListView<String> rightLIST;
    
    @FXML
    private ListView<String> leftLIST;

    @FXML
    private Button addBTN;

    @FXML
    private Button delBTN;
    
    @FXML
    private Button saveBTN;
    
    @FXML
    private Label leftLABEL;

    @FXML
    private Label rightLABEL;
    
	 @FXML
	private Label counterLABEL;
	 
	int studentsCounter;
    
    @FXML
    private Spinner<Character> spinner1;

    @FXML
    private Spinner<Integer> spinner2;

    /**	add button event handler */
    @FXML
    void addBTNaction(ActionEvent event) {
    	
    	if (!leftLIST.getItems().isEmpty()) {
    	
    		delBTN.setDisable(false);
    		saveBTN.setDisable(false);
    		rightLIST.setDisable(false);
    		DBFreeStudent std = getSelectedStudentFromLeft();		// get selected student
    		leftLISTstudents.remove(std.toString());		// remove from left, add to right
    		if (leftLISTstudents.isEmpty()) {
    			leftLIST.setDisable(true);
    			addBTN.setDisable(true);
    		}
    		rightLISTstudents.add(std.toString());
    		updateListViews();			// update list views
    		studentsCounter++;
    		counterLABEL.setText("("+studentsCounter+")");
    	}

    }
    
    /**	del button event handler */
    @FXML
    void delBTNaction(ActionEvent event) {
    	if (leftLISTstudents.isEmpty()) {
    		leftLIST.setDisable(false);
    		addBTN.setDisable(false);
    	}
    	if (!rightLIST.getItems().isEmpty()) {
    		DBFreeStudent std = getSelectedStudentFromRight();		// get selected student
    		rightLISTstudents.remove(std.toString());
    		if (rightLISTstudents.isEmpty()) {
    			saveBTN.setDisable(true);
    			rightLIST.setDisable(true);
    			delBTN.setDisable(true);
    		}
    		leftLISTstudents.add(std.toString());		// remove from left, add to right
    		updateListViews();			// update list views
    		studentsCounter--;
    		if (studentsCounter!=0)
    			counterLABEL.setText("("+studentsCounter+")");
    		else
    			counterLABEL.setText("");
    	}
    }

    /**	check button event handler */
    @FXML
    void checkBTNaction(ActionEvent event) {
    	
    	String className = getSpinnerValue(); 	/* validate class */
    	
    	if (isClassExist(className)) {
    		
    		showErrorMSG("Class already exist!","ERROR: class name '"+className+" "
    				+ "' is already taken.\nPlease choose another name.");
    		
    	}
    	else {
    		
    		showInfoMSG("Class is available and can be created :)","You can now add "
    				+ "students to the class. \nadd from the list on the left to the list on the right.\n"
    				+ "press the save button when finished.");
    		
    		disableElements(false);
    		checkBTN.setDisable(true);
    		spinner1.setDisable(true);
    		spinner2.setDisable(true);
    		classNameLABEL.setText("'"+className+"'");
    		getStudentsFromDB();
    		setStudentsInList();
    		
    		if (leftLISTstudents.isEmpty()) {
    			
    			showErrorMSG("No student's available!","ERROR: there are 0 free students in the system."
    					+ "\nPlease add new students to the school and then try again.");
    			disableElements(true);
    			classNameLABEL.setText("");
    			checkBTN.setDisable(false);
    			
    		}


    	}
    		

    }

    /**	save button event handler */
    @FXML
    void saveBTNaction(ActionEvent event) throws MalformedURLException {
    	
    	if (!rightLISTstudents.isEmpty()) {
    	
    		Alert alert = new Alert(AlertType.CONFIRMATION);
    		alert.setTitle("Confirmation Dialog");
    		alert.setHeaderText("Are you sure?");
    		alert.setContentText("A new class named '"+getSpinnerValue()+"' will be created."
    				+ "\tContinue?");
    		Optional<ButtonType> result = alert.showAndWait();
    	
    		if (result.get() == ButtonType.OK) {
    		
    			createNewDBclassRow(getSpinnerValue());
    			showInfoMSG("Class Created Succussfuly!!", "Class Name: '"+getSpinnerValue()+"'");
    			disableElements(true);
    			checkBTN.setDisable(false);
        		spinner1.setDisable(false);
        		spinner2.setDisable(false);
    			classNameLABEL.setText("");
    			rightLIST.getItems().clear();
    			leftLIST.getItems().clear();
        		studentsCounter--;
        		studentsCounter=0;
        		counterLABEL.setText("");
    		}
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		disableElements(true);
		initializeSpinners();
		
	}
	
	
	/**	set frame elements disabled
	 * 
	 * @param s	boolean: true to set disabled, false to set enabled
	 * */
	private void disableElements(boolean s) {
		
		if (s) {
			leftLIST.setDisable(true);
			rightLIST.setDisable(true);
			addBTN.setDisable(true);
			delBTN.setDisable(true);
			saveBTN.setDisable(true);
			leftLABEL.setVisible(false);
			rightLABEL.setVisible(false);
		}
		else {
			leftLIST.setDisable(false);
			addBTN.setDisable(false);
			leftLABEL.setVisible(true);
			rightLABEL.setVisible(true);
		}
		
	}
	
	/**	set spinner start values */
	private void initializeSpinners() {

		SpinnerValueFactory<Character> svf1 = // lettes spinner
				new SpinnerValueFactory.ListSpinnerValueFactory<Character>(letters);
		spinner1.setValueFactory(svf1);
		spinner1.setStyle("-fx-font: 12 arial;");
		spinner1.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		
		SpinnerValueFactory<Integer> svf2 = // digits spinner
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1,7);
		spinner2.setStyle("-fx-font: 12 arial;");
		spinner2.setValueFactory(svf2);
		spinner2.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
		spinner2.setMaxSize(10, 5);

	}
	
	/**	get classname from spinners */
	private String getSpinnerValue() {
		return ""+spinner1.getValue()+spinner2.getValue();
		
	}

	/**	returns true only if class exist in DB	
	 * @param className	name of the class
	 * */
	@SuppressWarnings("unchecked")
	private boolean isClassExist(String className) {
		

		synchronized (Main.client) {
			sentMSG.put("msgType", "select");
			sentMSG.put("query", "SELECT classname FROM class WHERE classname = '"+className+"'");
			Main.client.sendMessageToServer(sentMSG);
			
			try { 
				Main.client.wait();
				classesFromDB = (ArrayList<String>)Main.client.getMessage();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		if (!classesFromDB.isEmpty())
			return true;
		return false;
		
	}
	
	/**	shows error message using GUI
	 * 
	 * @param title 'title of the screen'
	 * @param MSG 'message to be displayed'
	 * */
	private void showErrorMSG(String title, String MSG) {
		
  		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error!");
		alert.setHeaderText(title);
		alert.setContentText(MSG);
		alert.showAndWait();
		
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

	/**	get student objectives from DB */
	@SuppressWarnings("unchecked")
	private void getStudentsFromDB() {
		
		sentMSG.put("msgType", "select");
    	sentMSG.put("query", "SELECT U.id, U.Name FROM users U WHERE U.role='Student'"
    			+ " and U.ID NOT IN (SELECT StudentID FROM student_class)");
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
		
		freeStudents = new ArrayList<DBFreeStudent>();
		
		for (int i=0;i<studentsFromDB.size();i+=2) {
			DBFreeStudent std = new DBFreeStudent();
			std.ID = Integer.parseInt(studentsFromDB.get(i)); //id
			std.Name = studentsFromDB.get(i+1);				 //name
			freeStudents.add(std);
    	}
	}
	
	/**	add's students to 'free students' list */
	private void setStudentsInList() {
		
    	for (int i=0;i<freeStudents.size();i++) {
    		leftLISTstudents.add(freeStudents.get(i).toString());
    	}
    	leftLIST.setItems(leftLISTstudents);		/*	POPULATE LIST 	*/
    	if (!leftLISTstudents.isEmpty())
    		leftLIST.getSelectionModel().selectFirst();
	}
	
	/**	returns student's obj of the ones shown on the 'free students' list */
    private DBFreeStudent getSelectedStudentFromLeft() {
    	
    	DBFreeStudent std = new DBFreeStudent();
    	String selectedItem = leftLIST.getSelectionModel().getSelectedItem().toString();
    	
    	
    	for (int i=0;i<freeStudents.size();i++) {	/* get matching semester */
    		if (freeStudents.get(i).toString().equals(selectedItem)) {
    			std = freeStudents.get(i);
    			break;
    		}
    	}
    	return std;	/*	return selected semester	*/
    }
    
	/**	returns student's obj of the ones shown on the 'added students' list */
    private DBFreeStudent getSelectedStudentFromRight() {
		
    	DBFreeStudent std = new DBFreeStudent();
    	String selectedItem = rightLIST.getSelectionModel().getSelectedItem().toString();
    	for (int i=0;i<freeStudents.size();i++) {	/* get matching semester */
    		if (freeStudents.get(i).toString().equals(selectedItem)) {
    			std = freeStudents.get(i);
    			break;
    		}
    	}
    	return std;	/*	return selected semester	*/
    }
    
	/**	refresh view with current information */
    private void updateListViews() {
    	
    	leftLIST.setItems(leftLISTstudents);		/*	UPDATE LEFT LIST 	*/
    	if (!leftLISTstudents.isEmpty())
    		leftLIST.getSelectionModel().selectFirst();
    	
    	rightLIST.setItems(rightLISTstudents);		/*	UPDATE RIGHT LIST 	*/
       	if (!rightLISTstudents.isEmpty())
       		rightLIST.getSelectionModel().selectFirst();
    	 	
    }
    
    /**	create new 'class' table row in data base */
    private void createNewDBclassRow(String className) {

    	sentMSG.put("msgType", "update");
		sentMSG.put("query", "INSERT INTO `mat`.`class` (`ClassName`) VALUES ('"+className+"');");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {		
			try {Main.client.wait();}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		ArrayList<DBFreeStudent> newClassToDB = new ArrayList<DBFreeStudent>();
		
		for (int i=0;i<rightLISTstudents.size();i++) {
			for (int j=0;j<freeStudents.size();j++) {
				if ((freeStudents.get(j).toString()).equals(rightLISTstudents.get(i))) {
					newClassToDB.add(freeStudents.get(j));
					break;
				}
			}
		}
		
		for (int i=0;i<newClassToDB.size();i++) {
			
			sendStudntINclassToDB(newClassToDB.get(i).ID, className);
			
		}		
    }
    
    /**	create new 'student_class' table row in data base */
    private void sendStudntINclassToDB(int studentID, String className) {
    	
       		sentMSG.put("msgType", "update");
    		sentMSG.put("query", "INSERT INTO `mat`.`student_class` (`StudentID`, `ClassName`) VALUES ('"+studentID+"', '"+className+"')");
    		
    		synchronized (Main.client) {		
    			try {
    				Main.client.sendMessageToServer(sentMSG);
    				Main.client.wait();
    				}
    			catch (InterruptedException e) {
    				e.printStackTrace();
    				System.out.println("Thread cant move to wait()");
    			}
    		}
    }
}
