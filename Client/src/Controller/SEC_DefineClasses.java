package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**	Controller class for: SEC_DefineClasses.fxml 
 * @author Nadav Zrihan
 * 	*/

public class SEC_DefineClasses implements Initializable {
	
	/**		holds list of strings (each represents semester) to be displayed @ combo box	*/
	private final ObservableList<String> semestersOBSRlist = FXCollections.observableArrayList();
		
	/**		String list, contains selected SEMESTER query recieved from the Server	*/
	ArrayList<String> semestersFromDB = new ArrayList<String>();
	
	/**		DBsemester list, contains selected SEMESTER query recieved from the Server	*/
	ArrayList<DBSemester> semesters = new ArrayList<DBSemester>();
	
	/**		String list, contains selected CLASS query recieved from the Server	*/
	ArrayList<String> classesFromDB = new ArrayList<String>();
	
	/** used to send MSG to server */
	HashMap<String, String> sentMSG = new HashMap<>();	
	
	/** this class represent Semester object as defined in DataBase */
    class DBSemester {
    	
    	int ID;
    	int year;
    	String season;
    	boolean isCurr=false;
    
    	public DBSemester (){};
    	
    	public DBSemester (int id, int year, String season, boolean isCurr) {
    		this.ID = id;
    		this.year = year;
    		this.season = season;
    		this.isCurr = isCurr;
    	};

		public String toString() {
			String seasonFormat;
			
			if (this.season == "w")
				seasonFormat = "Winter ("+this.season+")";
			else
				seasonFormat = "Spring ("+this.season+")";
			
			return "season: "+seasonFormat+"\t\t"+"year: "+this.year;
		}
    }

    @FXML
    private Button saveClassBTN;

    @FXML
    private TextField classnameTEXTFIELD;

    @FXML
    private ComboBox<String> semestersCOMBOBOX;

    @FXML
    private Label redLABEL;

    @FXML
    private Label blueLABEL;

    /**		actions to be taken when  "Save" button has been pressed	*/
    @FXML
    void saveBTNaction(ActionEvent event) {
    	
    	DBSemester selectedSemester = getSelectedSemester();
    	
    	if (!classnameTEXTFIELD.getText().isEmpty()) {	/* check class name is not empty*/
        	if (validateClass(classnameTEXTFIELD.getText(),selectedSemester.ID)) {	/*if class is NOT already exists*/
        		sendClassToDB(classnameTEXTFIELD.getText(),selectedSemester.ID);
        		redLABEL.setText("");
        		blueLABEL.setText("Class saved successfuly!");
        	}
        	else {
        		blueLABEL.setText("");
        		redLABEL.setText("Class already exists!");
        	}
    	}
    	else {
    		blueLABEL.setText("");
    		redLABEL.setText("Empty class name!");
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		getSemestersFromDB();
		setSemestersInComboBox();
		
	}
	
	/**	get semesters from server[DB] into an array list of DBSemester object's  */
	@SuppressWarnings("unchecked")
	private void getSemestersFromDB() {
		
		sentMSG.put("msgType", "select");
    	sentMSG.put("query", "SELECT semesterID, Year, Season, isCurr FROM semester");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {		
			try {Main.client.wait();}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		semestersFromDB = (ArrayList<String>)Main.client.getMessage();
		
		for (int i=0;i<semestersFromDB.size();i+=4) {
			DBSemester semester = new DBSemester();
			semester.ID = Integer.parseInt(semestersFromDB.get(i)); //id
			semester.year = Integer.parseInt(semestersFromDB.get(i+1)); //year
			semester.season = semestersFromDB.get(i+2); //season
    		if (semestersFromDB.get(i+3).equals("1"))	// isCurr
    			semester.isCurr = true;
    		semesters.add(semester);
    	}
		
	}
	
	/**	load semester list into ComboBox  */
	private void setSemestersInComboBox() {
		
    	for (int i=0;i<semesters.size();i++) {
    		semestersOBSRlist.add(semesters.get(i).toString());
    	}
    	semestersCOMBOBOX.setItems(semestersOBSRlist);			/*	POPULATE COMBOBOX 	*/
    	semestersCOMBOBOX.getSelectionModel().selectFirst();	
	}

	/**		returns true is class name + semesterID are available	
	 * @param className	name of the class
	 * @param semesterID	ID of the semester of this class
	 * 
	 * */

	@SuppressWarnings("unchecked")
	private boolean validateClass(String className, int semesterID) {
		
		sentMSG.put("msgType", "select");
		sentMSG.put("query", "SELECT classname, year FROM class WHERE classname = '"+className+"' and year='"+semesterID+"'");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {
			try { Main.client.wait(); }
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		classesFromDB = (ArrayList<String>)Main.client.getMessage();
		
		if (classesFromDB.isEmpty()) 
			return true;
		return false;
		
	}
	
    /**		get the semester object who matches the selection from the combobox	*/
    private DBSemester getSelectedSemester() {
    	DBSemester sem = new DBSemester();
    	
    	String selectedItem = semestersCOMBOBOX.getSelectionModel().getSelectedItem().toString(); /* get selected string */
    	for (int i=0;i<semesters.size();i++) {	/* get matching semester */
    		if (semesters.get(i).toString().equals(selectedItem)) {
    			sem = semesters.get(i);
    			break;
    		}
    	}
    	return sem;	/*	return selected semester	*/
    }

    /**		send the given CLASS information to database
     * @param className		name of the Class to be saved
     * @param semesterID	assigned SEMESTER to this class
     * 	*/
    private void sendClassToDB(String className, int semesterID) {

    	sentMSG.put("msgType", "update");
		sentMSG.put("query", "INSERT INTO `mat`.`class` (`ClassName`, `Year`) VALUES ('"+className+"', '"+semesterID+"')");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {		
			try {Main.client.wait();}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
    }

}
