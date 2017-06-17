package Controller;

import java.io.IOException;
import java.net.URL;
import javax.swing.JOptionPane;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SecretaryMenuBarController {

	
	/** Common Actions */
	
	@FXML
	private MenuBar bar;

	
	public void logout(ActionEvent e) throws IOException{
		
		Stage stage;
		Scene scene;
		
		if (Main.logOut() != 0){
	
			stage = (Stage) bar.getScene().getWindow();
		    stage.close();							// Close window.
			
			stage = new Stage();
			Parent login = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
			stage.getIcons().add(new Image("/user-login-icon-14.png"));
		    
		    scene = new Scene(login);
		    
		    stage.setTitle("Login");
		    stage.setScene(scene);
		    stage.show();
		}
		else
			JOptionPane.showMessageDialog(null, 
					  "Could not logout!", "Error", JOptionPane.ERROR_MESSAGE);
	}
	

	public void exit(ActionEvent e){
	
		if (Main.logOut() != 0)
			System.exit(0);
		else
			JOptionPane.showMessageDialog(null, 
					  "Could not close program!", "Error", JOptionPane.ERROR_MESSAGE);
	}
	

	public void message(ActionEvent e) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/Message.fxml"));
		Main.getRoot().setCenter(pane);
	}
	
	
	/** Common Actions */
	
	
	/** Private Actions */
	
	
	public void ChangeStudentAssignment(ActionEvent e){
		String fxml_url = "/FXML/Sec_chng_std_assign.fxml";
		URL paneUrl = getClass().getResource(fxml_url);
		AnchorPane pane;
		try{
			pane = FXMLLoader.load(paneUrl);
			Main.getRoot().setCenter(pane);
		}catch (IOException ex) {
			Thread thread = new Thread(new Runnable(){
				@Override
				public void run() {
			JOptionPane.showMessageDialog(null, 
					  "Could not open window!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			});
    		thread.start();
			ex.printStackTrace();
		}
	}
	
	public void ChangeTeacherAssignment(ActionEvent e){
		String fxml_url = "/FXML/SecChangeTeacherPlacement.fxml";
		URL paneUrl = getClass().getResource(fxml_url);
		AnchorPane pane;
		try{
			pane = FXMLLoader.load(paneUrl);
			Main.getRoot().setCenter(pane);
		}catch (IOException ex) {
			Thread thread = new Thread(new Runnable(){
				@Override
				public void run() {
			JOptionPane.showMessageDialog(null, 
					  "Could not open window!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			});
    		thread.start();
			ex.printStackTrace();
		}
	}
	
	public void CreateSemester(ActionEvent e){
		
		String fxml_url = "/FXML/SEC_CreateNewSemester.fxml";
		URL paneUrl = getClass().getResource(fxml_url);
		AnchorPane pane;
		try{
			pane = FXMLLoader.load(paneUrl);
			Main.getRoot().setCenter(pane);
		}catch (IOException ex) {
			Thread thread = new Thread(new Runnable(){
				@Override
				public void run() {
			JOptionPane.showMessageDialog(null, 
					  "Could not open window!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			});
    		thread.start();
			ex.printStackTrace();
		}
		
	}
	
	public void DefineClasses(ActionEvent e) throws IOException{

		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/SEC_DefineClasses.fxml"));;
		Main.getRoot().setCenter(pane);
		
	}
	
	public void AttachCurseToClass(ActionEvent e) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/SecretaryCourseToClass.fxml"));;
		Main.getRoot().setCenter(pane);
	}
	
	
	
	/** Private Actions */
	
	
}
