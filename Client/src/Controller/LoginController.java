package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.swing.JOptionPane;

import application.ClientConnection;
import application.Main;
import application.MessageThread;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
	 private Label guiMeg;
	 
	 private int NumOfLogin = -1;
	 
	 private HashMap <String, String> answer = null;
	 private HashMap <String, String> msgServer = new HashMap <String,String>();
	 private static BorderPane root = new BorderPane();
	
	 @SuppressWarnings("unchecked")
	 public void OnLogin(ActionEvent e) throws InterruptedException{
		 
		// Close login after 3 tries.
		 
		if (++NumOfLogin == 3){
			JOptionPane.showMessageDialog(null, "Please contact system manager", "Error" , JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		// Establish connection to server.
		
		if(Main.client == null)
			if (!ip.getText().isEmpty() && !port.getText().isEmpty())
				Main.client = new ClientConnection(ip.getText(), Integer.parseInt(port.getText()));
			else
				guiMeg.setText("Connection to server failed!");
	
		
		// Send message to server with an new thread & wait for answer. 
		
		if (!id.getText().isEmpty() && !password.getText().isEmpty() && !school.getText().isEmpty()){
			msgServer.put("msgType", "Login");
			msgServer.put("id", id.getText());
			msgServer.put("passwrd", password.getText());
			msgServer.put("schoolId", school.getText());
			
			try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				guiMeg.setText("Server fatal error!");
			}
			
			MessageThread msgT = new MessageThread(Main.client);
			msgT.start();
			synchronized (msgT){msgT.wait();}
			answer = (HashMap <String, String>)Main.client.getMessage();
		}
		else if (Main.client != null)
			guiMeg.setText("Please fill al fields..");
			
		// Process answer from server.
		
		if (answer != null && answer.get("Valid").equals("true")){
			
			((Node)(e.getSource())).getScene().getWindow().hide(); // Close login window.
			String fxml_url = "/FXML/";
			
			switch (answer.get("Type")){
			
			case "Student": fxml_url += "StudentMenuBar.fxml"; break;
			case "Parent": fxml_url += "ParentMenuBar.fxml"; break;
			case "Secretary": fxml_url += "SecretaryMenuBar.fxml"; break;
			case "Teacher": fxml_url += "TeacherMenuBar.fxml"; break;
			case "Principal": fxml_url += "PrincipalMenuBar.fxml"; break;
			case "System Manager": fxml_url += "SystemManegerMenuBar.fxml"; break;
			
			}
			
			URL menuBarUrl = getClass().getResource(fxml_url);
		    URL welcomePaneUrl = getClass().getResource("/FXML/WelcomeScreen.fxml");
		    MenuBar bar;
		    AnchorPane pane;
			try {
				bar = FXMLLoader.load(menuBarUrl);
				pane = FXMLLoader.load(welcomePaneUrl);
				// constructing our scene using the static root
	
			    root.setTop(bar);
			    root.setCenter(pane);
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		    
		    Scene scene = new Scene(root, 640, 480);
		    scene.getStylesheets().add(getClass().getResource("/FXML/application.css").toExternalForm());
		    
		    Stage primaryStage = new Stage();
		    primaryStage.setScene(scene);
		    primaryStage.getIcons().add(new Image("/school_icon.png"));
		    primaryStage.setTitle("Academic system for high school");
		    primaryStage.setResizable(false);
		    primaryStage.show();
		}else if (answer != null)
			guiMeg.setText((String)answer.get("ErrMsg"));
	}
}