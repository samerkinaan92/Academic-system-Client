package Controller;

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
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class SEC_ChangeCurrentSemester implements Initializable{
	
	/**		semester items ObservableList to be displayed on ComboBox	*/
	private  ObservableList<String> semestersInCOMBOBOX = FXCollections.observableArrayList();
	
	/**		String attaylist represents DBSimpleSemester object's returned from a Server query	*/
	ArrayList<String> semestersFromDB = new ArrayList<String>();
	
	/**		DBSimpleSemester object's returned from a Server query	*/
	ArrayList<DBSimpleSemester> semesters = new ArrayList<DBSimpleSemester>();
	
	/**	Simple representation fo Semester: (Year, Season)*/
    class DBSimpleSemester {
    	
    	int year = 0;
    	String season = "";
    
    	public DBSimpleSemester (){};
    	
    	public DBSimpleSemester (int id, int year, String season, boolean isCurr) {

    		this.year = year;
    		this.season = season;

    	};

		public String toString() {
			return "season: "+this.season+"\t\t"+"year: "+this.year;
		}
    }
	
	/** used to send MSG to server */
	HashMap<String, String> sentMSG = new HashMap<>();	

    @FXML
    private ComboBox<String> chooseSemesterCOMBOBOX;

    @FXML
    private Button setCurrentBTN;

    /** this method handles "Set Current" button */
    @FXML
    void setCurrentBTNaction(ActionEvent event) {
    	
    	DBSimpleSemester sem = getSelectedSemester();
   
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Confirmation Dialog");
    	alert.setHeaderText("Change current Semester?");
    	alert.setContentText("This will set "+sem.season+", "+sem.year+" as the current semester.\n Continue?");
    	Optional<ButtonType> result = alert.showAndWait();
    	
    	if (result.get() == ButtonType.OK){
        	
    		clearCurrentSemester();
        	setNewCurrent(sem.season,sem.year);
        	Stage st = (Stage) setCurrentBTN.getScene().getWindow();
        	SEC_CreateNewSemester_CTRL.isOpened = false;
        	
        	
        	alert = new Alert(AlertType.INFORMATION);
        	alert.setTitle("Success");
        	alert.setHeaderText(null);
        	alert.setContentText("Current semester has been canged!");
        	alert.showAndWait();
        	
        	
        	st.close();
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		setCurrentBTN.setDisable(true);
		getSimpleSemestersFromDB();
		setSimpleSemestersInComboBox();
		
	}		
	
	/**	get semesters from server[DB] into an array list of DBSimpleSemester object's  */
	@SuppressWarnings("unchecked")
	private void getSimpleSemestersFromDB() {
		
		sentMSG.put("msgType", "select");
    	sentMSG.put("query", "SELECT season, year FROM mat.semester WHERE isCurr <> '1';");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {		
			try {Main.client.wait();}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		semestersFromDB = (ArrayList<String>)Main.client.getMessage();
		
		for (int i=0;i<semestersFromDB.size();i+=2) {
			DBSimpleSemester sem = new DBSimpleSemester();
			
			sem.season = semestersFromDB.get(i); // season
			sem.year = Integer.parseInt(semestersFromDB.get(i+1)); //year
    		semesters.add(sem);
    	}
	}
	
	/**	Populate ComboBox with String representations of SimpleSemesters object from DataBase  */
	private void setSimpleSemestersInComboBox() {
		
    	for (int i=0;i<semesters.size();i++) {
    		semestersInCOMBOBOX.add(semesters.get(i).toString());
    	}
    	chooseSemesterCOMBOBOX.setItems(semestersInCOMBOBOX);			/*	POPULATE COMBOBOX 	*/
    	chooseSemesterCOMBOBOX.getSelectionModel().selectFirst();
    	if (!semestersInCOMBOBOX.isEmpty()) 
    		setCurrentBTN.setDisable(false);
	}		
	
	/** This method clear's Current Semester's 'isCurr' status.
	 *in other words: the current semester is no longer the current semester */
	@SuppressWarnings("unchecked")
	private void clearCurrentSemester () {
		
		ArrayList <String> currentSemester;
		sentMSG.put("msgType", "select");		// get id of current semester
		sentMSG.put("query", "SELECT semesterID FROM mat.semester WHERE isCurr='1'");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {
			try { Main.client.wait();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		currentSemester = (ArrayList<String>)Main.client.getMessage();
		
		//UPDATE semester SET isCurr='0' WHERE semesterId='ID'
		
		sentMSG.put("msgType", "update");
		sentMSG.put("query", "UPDATE semester SET isCurr='0' WHERE semesterId='"+currentSemester.get(0)+"'");
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
	}

	private void setNewCurrent(String season, int year) {
		sentMSG.put("msgType", "update");
		sentMSG.put("query", "UPDATE mat.semester SET isCurr='1' WHERE season = '"+season+"' and Year='"+year+"'");
		Main.client.sendMessageToServer(sentMSG);
		synchronized (Main.client) {
			try {Main.client.wait();}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
	}

    /**		get the DBSimpleSemester object who matches the selection from the combobox	*/
    private DBSimpleSemester getSelectedSemester() {
    	DBSimpleSemester sem = new DBSimpleSemester();
    	
    	String selectedItem = chooseSemesterCOMBOBOX.getSelectionModel().getSelectedItem().toString(); /* get selected string */
    	for (int i=0;i<semesters.size();i++) {	/* get matching submission */
    		if (semesters.get(i).toString().equals(selectedItem)) {
    			sem = semesters.get(i);
    			break;
    		}
    	}
    	return sem;	/*	return selected semester	*/
    }
}
