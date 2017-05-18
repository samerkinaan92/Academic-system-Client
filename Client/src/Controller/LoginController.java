package Controller;

import java.util.HashMap;

import application.ClientConnection;
import application.Main;
import application.MessageThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;



public class LoginController {
	
	 @FXML
	 private TextField id;

	 @FXML
	 private PasswordField password;

	 @FXML
	 private TextField school;

	 @FXML
	 private TextField ip;

	 @FXML
	 private TextField port;
	 
	 @FXML
	 private Label server_msg;
	 


	 
	 private HashMap <String, String> msg = new HashMap <String,String>();
	 
	
	 @SuppressWarnings("unchecked")
	public void OnLogin(ActionEvent e) throws InterruptedException{
	
		//((Node)(e.getSource())).getScene().getWindow().hide(); // Close login window.
		
		
		
		
		if(Main.client == null)
			Main.client = new ClientConnection(ip.getText(), Integer.parseInt(port.getText()));
		
			
		msg.put("msgType", "Login");
		msg.put("id", id.getText());
		msg.put("passwrd", password.getText());
		msg.put("schoolId", school.getText());
		
		Main.client.sendMessageToServer(msg);
		
		MessageThread msgT = new MessageThread(Main.client);
		
		msgT.start();
		
		synchronized (msgT){
			msgT.wait();
		}
		
		
		
		HashMap <String, String> answer = (HashMap <String, String>)Main.client.getMessage();
		
		
		if (answer.get("Valid").equals("true")){
			
			((Node)(e.getSource())).getScene().getWindow().hide(); // Close login window.
			Main.openMain(answer.get("Type"));
		}else
			server_msg.setText((String)answer.get("ErrMsg"));
	 }
	
		 

}
