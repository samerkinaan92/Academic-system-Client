package Controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import Entity.User;
import application.ClientConnection;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This is the controller class for: "SecretaryCourseToClassController.fxml"
 * @author Idan Agam
 * */

public class LoginController implements Initializable {
	
	/** ComboBox to display school choice */
	 @FXML
	 private ComboBox<String> school;
	
	 @FXML
	 private TextField id;

	 /** PasswordField for user password */
	 @FXML
	 private PasswordField password;

	 /** TextField For server ip */
	 @FXML
	 private TextField ip;

	 /** TextField For server port */
	 @FXML
	 private TextField port;
	 
	 /** Label to display messages to user */
	 @FXML
	 private Label guiMeg;
	 
	 /** List o fschools to choose from */
	 private final ObservableList<String> options = FXCollections.observableArrayList("Ort Braude", "Ort Kramim", "Majadim"); // List of schools.
	 
	 /** Answer from server */
	 private HashMap <String, String> answer = null;
	 
	 /** Message to server */
	 private HashMap <String, String> msgServer = new HashMap <String,String>();
	 
	 /** Check all input and display user messages accordingly, if all
	  * Fields are met and valid, the user's specific welcome window open's */
	 @SuppressWarnings("unchecked")
	 public void OnLogin(ActionEvent e) throws InterruptedException{
		
		// Establish connection to server.
		 
		 try{
			 Integer.parseInt(port.getText());
		 }
		 catch(Exception ex){
			 guiMeg.setText("Port field: input not valid!");
			 return;
		 }
		
		if(Main.client == null)
			if (!ip.getText().isEmpty() && !port.getText().isEmpty())
				Main.client = new ClientConnection(ip.getText(), Integer.parseInt(port.getText()));
			else{
				guiMeg.setText("Connection to server failed!");
				return;
			}
	
		
		// Send message to server with an new thread & wait for answer. 
		
		if (!id.getText().isEmpty() && !password.getText().isEmpty() && school.getSelectionModel().getSelectedItem() != null){
			msgServer.put("msgType", "Login");
			msgServer.put("id", id.getText());
			msgServer.put("passwrd", password.getText());
			msgServer.put("schoolId", school.getSelectionModel().getSelectedItem().toString());
			
			try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				guiMeg.setText("Server fatal error!");
				return;
			}
			
			
			synchronized (Main.client){Main.client.wait();}
			answer = (HashMap <String, String>)Main.client.getMessage();
		}
		else if (Main.client != null){
			guiMeg.setText("Please fill al fields..");
			return;
		}
			
		// Process answer from server.
		
		if (answer != null && answer.get("Valid").equals("true")){	
			
			Stage Curstage = (Stage) id.getScene().getWindow();
			Curstage.close();
		    
			//((Node)(e.getSource())).getScene().getWindow().hide(); // Close login window.
			Main.user = new User(id.getText(), answer.get("Name"));
			Main.openMain(answer.get("Type"));
			Main.user.setType(answer.get("Type"));
		}else
			guiMeg.setText((String)answer.get("ErrMsg"));
	 }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		school.setItems(options);
	}
}