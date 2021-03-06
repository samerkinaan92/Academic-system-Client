package Controller;

import java.io.IOException;

import java.net.URL;

import application.Main;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Parent;


/**
 * This is the controller class for: "PrincipalMenuBar.fxml"
 * @author Idan Agam
 */

public class PrincipalMenuBarController {
	
	
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
	 * opens system data controller
	 * @param e
	 * @throws IOException
	 */
	public void ViewSystemData(ActionEvent e) throws IOException{

		
	AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/PRCPL_ViewSystemData.fxml"));;
	Main.getRoot().setCenter(pane);
	}
	public void ViewStatisticalReport(ActionEvent e){
    	try {
		   URL paneOneUrl = getClass().getResource("/FXML/PrincipalViewStatisticReport.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException ex) {
            ex.printStackTrace();
          }    
	}
	
	/**
	 * opens change teacher requests handler controller
	 * @param e
	 */
	public void ApproveTeacherAssignment(ActionEvent e){
		String fxml_url = "/FXML/principal_TeacherExceptionalRequests.fxml";
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
	 * opens students course changes requests handler controller
	 * @param e
	 */
	public void ApproveStudentChange(ActionEvent e){
		String fxml_url = "/FXML/principal_StudExceptionalRequests.fxml";
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
	

    /**OpenParInfo - event that presenting the parent information screen*/
    @FXML
    void OpenParInfo(ActionEvent event) {
    	try {
		   URL paneOneUrl = getClass().getResource("/FXML/PrincipalViewParInfo.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException e) {
            e.printStackTrace();
          }       
    }
    
    /**
     * opens view personal info controller
     * @param event
     */
    @FXML
    void viewPerInfo(ActionEvent event) {
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
