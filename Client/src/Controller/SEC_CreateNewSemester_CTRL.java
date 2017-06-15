package Controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This is the controller class for: "SEC_CreateNewSemester_CTRL.fxml"
 * @author Nadav Zrihan
 * */
public class SEC_CreateNewSemester_CTRL implements Initializable {
	
	/**  */
	ArrayList<String> existingSemesters = new ArrayList<String>();
	
	/**  Current Year	*/
	Integer thisYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
	
	/** String's Observable List representing Season to be displayed @ Season's COMBO BOX */
	private  ObservableList<String> seasons;
	
	/** used to send SQL querys to the server */
	HashMap<String, String> MSG = new HashMap<>();
	
	/** false: changeCurrentSemester window is not opened
	 * true: otherwise
	 *  */
	static boolean isOpened = false;
	
    @FXML
    private Spinner<Integer> yearSpinner;
	
	  @FXML
	    private Button createBTN;

	    @FXML
	    private Label seasonLABEL;

	    @FXML
	    private ComboBox<String> seasonCOMBOBOX;

	    @FXML
	    private Label redLABEL;
	    
	    @FXML
	    private Label blueLABEL;
	    
	    @FXML
	    private TextField yearTEXTFIELD;
	    
	    @FXML
	    private CheckBox setcurrentCHECKBOX;

	    @FXML
	    private Button changeCurrentSemesterBTN;

	/** code to be executed when "create" button is pressed */
    @SuppressWarnings("unchecked")
	@FXML
    void createBTNaction(ActionEvent event) {		// need to add current semester replacment in DB in case of a change
    	
    	int isCurr = 0;
    	
    	String selectedSeason = getSeason();
    	int desiredYear = (int)yearSpinner.getValue();

    	//if (!(desiredYear.isEmpty()) && desiredYear.length()==4) {
    		
    		redLABEL.setText("");
    		blueLABEL.setText("");

    		getExistingSemesters(selectedSeason,desiredYear);

    		if(existingSemesters.isEmpty()) {
    			
            	if (setcurrentCHECKBOX.isSelected()) {	// check if to set as current semester

            		Alert alert = new Alert(AlertType.CONFIRMATION);
            		alert.setTitle("Confirmation Dialog");
            		alert.setHeaderText("Semester has been created SUCCUSSFULY!");
            		alert.setContentText("Replace "+getCurrentSemester()+" "
            				+ "as the current semester?");
            		Optional<ButtonType> result = alert.showAndWait();
            			
            		if (result.get() == ButtonType.OK)
            		    isCurr = 1;
            	}
            	
            	if (isCurr==1)
            		clearCurrentSemester ();
            	        	
    			sendNewSemester(selectedSeason,desiredYear,isCurr);
    			blueLABEL.setText("SEMESTER CREATED SUCCESSFULY!");
    			redLABEL.setText("");
    		}
    		else {
    			redLABEL.setText("SEMESTER ALREADY EXISTS!");
    			blueLABEL.setText("");
    		}
    	//}
   // else {
    //	}
    	isCurr = 0;
    }
    	
    /** opens up the "Change Current Semester" dialog ("SEC_ChangeCurrentSemester.fxml") */
    @FXML
    void changeCurrentSemesterBTNaction(ActionEvent event) throws IOException {
    	if (!isOpened) {
    		blueLABEL.setText("");
    		Stage stage = new Stage();
    		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/FXML/SEC_ChangeCurrentSemester.fxml")));
    		stage.setTitle("Change Current Semester");
    		stage.setScene(scene);
    		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
  	          public void handle(WindowEvent we) {
  	        	isOpened = false;
	          }
	      });
    		isOpened = true;
    		stage.show();
    	}
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		seasons = FXCollections.observableArrayList("Winter (a)","Spring (b)");
		seasonCOMBOBOX.setItems(seasons);
		seasonCOMBOBOX.getSelectionModel().selectFirst();
		setcurrentCHECKBOX.setSelected(true);
		
        SpinnerValueFactory<Integer> sVf = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(thisYear, thisYear+10);
        yearSpinner.setValueFactory(sVf);
		
	}
	
	/** Initialize's this class's existingSemesters ArrayList with all of the existing semester from DB */
	@SuppressWarnings("unchecked")
	private void getExistingSemesters (String selectedSeason, int desiredYear) {		// set answer with the server reponse to the select query
		
		MSG.put("msgType", "select");
		MSG.put("query", "SELECT * FROM semester WHERE season='"+selectedSeason+"' and year='"+desiredYear+"'");
		Main.client.sendMessageToServer(MSG);
		
		synchronized (Main.client) {
			try {
				Main.client.wait();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		
		this.existingSemesters = (ArrayList<String>)Main.client.getMessage();	// into arrayList
		
	}
	
	/** Send's a new Semester to the DATABSE
	 * @param selectedSeason	semester's season
	 * @param desiredYear		semester's year
	 * @param isCurr			current semester status
	 *  
	 *  */
	@SuppressWarnings("unchecked")
	private void sendNewSemester (String selectedSeason, int desiredYear, int isCurr) {

		MSG.put("msgType", "update");
		MSG.put("query", "INSERT INTO `mat`.`semester` (`Season`, `Year`, `isCurr`) VALUES ('"+selectedSeason+"', '"+desiredYear+"', '"+isCurr+"')");
		Main.client.sendMessageToServer(MSG);
		
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
	
	/** this method is used to convert the Season item selected in the combobox,
	 * to 'a' or 'b' String values as represented in the DATABASE
	 */
	private String getSeason() {
		
		String s = seasonCOMBOBOX.getSelectionModel().getSelectedItem().toString();
		if (s.equals("Winter (a)")) 
			return "a";
		return "b";
	}
	
	/** This method returns a String representaion of the Current Semester */
	@SuppressWarnings("unchecked")
	private String getCurrentSemester () {

		ArrayList <String> currentSemester;
		MSG.put("msgType", "select");
		MSG.put("query", "SELECT Year,Season,semesterID FROM mat.semester WHERE isCurr='1'");
		Main.client.sendMessageToServer(MSG);
		synchronized (Main.client) {
			try { Main.client.wait();
			}
			catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread cant move to wait()");
			}
		}
		currentSemester = (ArrayList<String>)Main.client.getMessage();
		
		if (!currentSemester.isEmpty())
			return " ("+currentSemester.get(0)+","+currentSemester.get(1)+")";
		return "";
		
	}

	/** This method clear's Current Semester's 'isCurr' status.
	 *in other words: the current semester is no longer the current semester */
	@SuppressWarnings("unchecked")
	private void clearCurrentSemester () {
		
		ArrayList <String> currentSemester;
		MSG.put("msgType", "select");		// get id of current semester
		MSG.put("query", "SELECT semesterID FROM mat.semester WHERE isCurr='1'");
		Main.client.sendMessageToServer(MSG);
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
		if (!currentSemester.isEmpty())	{
			MSG.put("msgType", "update");
			MSG.put("query", "UPDATE semester SET isCurr='0' WHERE semesterId='"+currentSemester.get(0)+"'");
			Main.client.sendMessageToServer(MSG);
			
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

	}

}
