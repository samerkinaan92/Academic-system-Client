package Controller;

import java.io.IOException;
import java.net.URL;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * This is the controller class for: "SecretaryMenuBar.fxml"
 * @author Idan Agam
 */

public class SecretaryMenuBarController {

	
	/** Common Actions */
	
	@FXML
	private MenuBar bar;

	/**
	 * log out the user
	 * @param e
	 * @throws IOException
	 */
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
		else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Could not logout!");
			alert.show();
		}
	}
	
	/**
	 * exit the program
	 * @param e
	 */
	public void exit(ActionEvent e){
	
		if (Main.logOut() != 0)
			System.exit(0);
		else{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Could not close program!");
			alert.show();
		}
	}
	
	/**
	 * open user messages
	 * @param e
	 * @throws IOException
	 */
	public void message(ActionEvent e) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/Message.fxml"));
		Main.getRoot().setCenter(pane);
	}
	
	
	/** Common Actions */
	
	
	/** Private Actions */
	
	/**
	 * opens change student controller
	 * @param e
	 */
	public void ChangeStudentAssignment(ActionEvent e){
		String fxml_url = "/FXML/PickStudent.fxml";
		URL paneUrl = getClass().getResource(fxml_url);
		AnchorPane pane;
		try{
			pane = FXMLLoader.load(paneUrl);
			Main.getRoot().setCenter(pane);
		}catch (IOException ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Could not open window!");
			alert.show();
			ex.printStackTrace();
		}
	}
	
	/**
	 * opens change teacher controller
	 * @param e
	 */
	public void ChangeTeacherAssignment(ActionEvent e){
		String fxml_url = "/FXML/SecChangeTeacherPlacement.fxml";
		URL paneUrl = getClass().getResource(fxml_url);
		AnchorPane pane;
		try{
			pane = FXMLLoader.load(paneUrl);
			Main.getRoot().setCenter(pane);
		}catch (IOException ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Could not open window!");
			alert.show();
			ex.printStackTrace();
		}
	}
	
	/**
	 * opens create new semester controller
	 * @param e
	 */
	public void CreateSemester(ActionEvent e){
		
		String fxml_url = "/FXML/SEC_CreateNewSemester.fxml";
		URL paneUrl = getClass().getResource(fxml_url);
		AnchorPane pane;
		try{
			pane = FXMLLoader.load(paneUrl);
			Main.getRoot().setCenter(pane);
		}catch (IOException ex) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText("Could not open window!");
			alert.show();
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * opens define new class controller
	 * @param e
	 * @throws IOException
	 */
	public void DefineClasses(ActionEvent e) throws IOException{

		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/SEC_DefineClasses.fxml"));;
		Main.getRoot().setCenter(pane);
		
	}
	
	/**
	 * opens delete class controller
	 * @param e
	 * @throws IOException
	 */
	public void DeleteClass(ActionEvent e) throws IOException{

		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/SEC_DeleteClass.fxml"));;
		Main.getRoot().setCenter(pane);
		
	}
	
	/**
	 * opens attach courses to class controller
	 * @param e
	 * @throws IOException
	 */
	public void AttachCurseToClass(ActionEvent e) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/SecretaryCourseToClass.fxml"));;
		Main.getRoot().setCenter(pane);
	}
	
	/**
     * opens view personal info controller
     * @param event
     */
    @FXML
    void ViewPerInfo(ActionEvent event) {
    	try {
    		UserViewPersonalInfoController.setUser(Main.user.getID());
		   URL paneOneUrl = getClass().getResource("/FXML/UserViewPersonalInfo.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException exp) {
        	exp.printStackTrace();
          }          
    }
	
	/** Private Actions */
	
	
}
