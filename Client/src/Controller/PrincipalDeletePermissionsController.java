package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import Entity.Parent;
import Entity.ParentStudent;
import application.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * This controller is used to change the parent permissions for student and display general data.
 * 
 */
public class PrincipalDeletePermissionsController implements Initializable{
	
	/** The static Object par is for the chosen parent that was selected by the user.*/
	public static Parent par;
	
	/** The static ArrayList stdArr use for storing the students of the selected parent.*/
	public static ArrayList<ParentStudent> stdArr;
	
	/** The static ObservableList stdArr use for displaying the information in Table.*/
	private final ObservableList<tableStud> stdD = FXCollections.observableArrayList();	
	
	
	/**Table of students of the selected parent that will displayed.*/
	@FXML
    private TableView<tableStud> studTable;

    
    /**stdIDColumn set as the Table column for student ID*/
    @FXML
    private TableColumn<tableStud, String> stdIDColumn;

    /**stdNameColumn set as the Table column for student name*/
    @FXML
    private TableColumn<tableStud, String> stdNameColumn;

    /**permColumn set as the Table column for parent permissions for this student.*/ 
    @FXML
    private TableColumn<tableStud, String> permColumn;

    /**parName - Parent name. */
    @FXML
    private Label parName;

    /**parName - Parent ID. */
    @FXML
    private Label IDlabel;

    /**parName - Parent EMAIL. */
    @FXML
    private Label emailLabel;

    /**parName - Parent home address. */
    @FXML
    private Label AddressLabel;

    /**parName - Parent phone number. */
    @FXML
    private Label PhoneLabel;
    
    /**changeBtn - is used to make operation and change the parent permissions for the selected student in DB.*/
    @FXML
    private Button changeBtn;

    
    /**initialize() is used in order to present the parent general information and the status of his permissions.*/
    public void initialize(URL arg0, ResourceBundle arg1) {
    	parName.setText(par.getName());
    	IDlabel.setText(par.getID());
    	emailLabel.setText(par.getEmail());
    	AddressLabel.setText(par.getAddress());
    	PhoneLabel.setText(par.getPhone());
    	stdIDColumn.setCellValueFactory(new PropertyValueFactory<tableStud, String>("ID"));
    	stdNameColumn.setCellValueFactory(new PropertyValueFactory<tableStud, String>("Name"));
    	permColumn.setCellValueFactory(new PropertyValueFactory<tableStud, String>("isBlocked"));	
    	
    	setTable(); 
	

    }
    
    /** setting student table*/
    public void setTable()
    {
    	stdArr=ParentStudent.getStudByPar(par.getID(),par.getName()); //getting the values for the DB
    	stdD.clear(); //clearing old values from the list
    	for(int i = 0 ; i < stdArr.size() ; i++ )
    		stdD.add(new tableStud(stdArr.get(i).getStudID(),stdArr.get(i).getStudName(),(stdArr.get(i).getIsParBloc().equals("1")?"Not approved":"Approved")));

    	studTable.setItems(stdD); // setting the new values in the table
    }
    
    /**ChangePer() - event for changing the permissions value of the parent for the selected student in DB*/
    @FXML
    void ChangePer(ActionEvent event) {
    	
    	tableStud std;
    	std = studTable.getSelectionModel().getSelectedItem();
    	if(confDialog(par.getName(),std.getName(),std.getisBlocked().equals("Not approved")?"enable":"disable")){ // presenting confirmation dialog
	    	updatePrem(par.getID(),std.getID(),std.getisBlocked().equals("Not approved")?"0":"1"); // update values in DB
	    	setTable();
    	}
    	changeBtn.setDisable(true);
    }

    /**Enabling the changeBtn option*/
    @FXML
    void setClick(MouseEvent event) {
    	changeBtn.setDisable(false);   	
    }
    
    /**Disabling the changeBtn option when pressing at the pane*/
    @FXML
    void panePress(MouseEvent event) {
    	changeBtn.setDisable(true); 
    }
  
    /**backBtn - is used for return to the last screen - PrincipalViewParInfo.*/
    @FXML
    void backBtn(ActionEvent event) {
    	try {
		   URL paneOneUrl = getClass().getResource("/FXML/PrincipalViewParInfo.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException e) {
            e.printStackTrace();
          }        
    }
    
    
    /**finishBtn - is used for return to the welcome screen - WelcomeScreen.*/
    @FXML
    void finishBtn(ActionEvent event) {
    	try {
		   URL paneOneUrl = getClass().getResource("/FXML/WelcomeScreen.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException e) {
            e.printStackTrace();
          }       
    }
    
    /**Return the static value of Parent - par*/
    public static Parent getPar() {
		return par;
	}
    
    /**Set the static value of Parent - par*/
	public static void setPar(Parent pare) {
		par=Parent.getParByID(pare.getID());
	}
	
	
	
	/**Sending a message to the server for updateing the parent permissions.*/
	public void updatePrem(String parID,String stdID, String newPer){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "update");
		msgServer.put("query", "update parent_student set IsBlocked='"+newPer+"' where ParentUserID='"+parID+"' and StudentUserID='"+stdID+"';");		
		sendMsg(msgServer);
	}
	
	
	/**
	 * use to send the message and communicate with the server.
	 * msgServer - message type
	 * */
	public  void sendMsg(HashMap <String,String> msgServer){
		try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				System.out.println("Server fatal error!");
			}
		synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
	}
	
	
	/**
	 * Presenting confirmation dialog of the permissions change
	 * parName - parent name.
	 * stdName - student name.
	 * oper - operation type - enable / disable.
	 * */
    public  boolean confDialog(String parName, String stdName, String oper){
    	Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to " +oper+" "+ parName +" permissions for student - "+stdName+"?", ButtonType.YES, ButtonType.CANCEL);
    	alert.showAndWait();

    	if (alert.getResult() == ButtonType.YES) {
        	Alert alert2 = new Alert(AlertType.INFORMATION, ""+parName +" permissions was "+oper+" successfully.", ButtonType.OK);
        	alert2.setHeaderText("Success");
        	alert2.showAndWait();
        	return true;
    	}
    	else return false;
    	
    }
	
	
	
	/**
	 * tableStud - is use for storing the elements that will be used and displayed by the table view.
	 * ID - student ID.
	 * Name - student name.
	 * isBlocked - parent permissions for the student.
	 * */
	public static class tableStud{
		
		private StringProperty ID;
		private StringProperty Name;
		private StringProperty isBlocked;
		
		public tableStud(String ID,String Name,String isBlocked)
		{
			setName(Name);
			setID(ID);
			setisBlocked(isBlocked);
		}

        public void setID(String value) { IDProperty().set(value); }
        public String getID() { return IDProperty().get(); }
        public StringProperty IDProperty() { 
            if (ID == null) ID = new SimpleStringProperty(this, "ID");
            return ID; 
        } 
		
		 public void setName(String value) { NameProperty().set(value); }
        public String getName() { return NameProperty().get(); }
        
        public StringProperty NameProperty() { 
            if (Name == null) Name = new SimpleStringProperty(this, "Name");
            return Name; 
        }
   

        
        public void setisBlocked(String value) { isBlockedProperty().set(value); }
        public String getisBlocked() { return isBlockedProperty().get(); }
        public StringProperty isBlockedProperty() { 
            if (isBlocked == null) isBlocked = new SimpleStringProperty(this, "isBlocked");
            return isBlocked; 
        }
	}
}
