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
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StudentMenuBarController {

	
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
	
    @FXML
    void viewCourseInfo(ActionEvent event) {
    	try {
		   URL paneOneUrl = getClass().getResource("/FXML/StudentViewCourses.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException exp) {
        	exp.printStackTrace();
          }       
    }
	
	public void ViewPersonalInfo(ActionEvent e){
    	try {
		   URL paneOneUrl = getClass().getResource("/FXML/UserViewPersonalInfo.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException exp) {
        	exp.printStackTrace();
          }       
	}
	
	public void SubmitAssignments(ActionEvent e) throws IOException{
		SplitPane pane = FXMLLoader.load(getClass().getResource("/FXML/StudentSubmitAssignment.fxml"));;
		Main.getRoot().setCenter(pane);
	}
	
    @FXML
    void evaluationEvent(ActionEvent event) {

    }

	
	
	/** Private Actions */
	
	
}
