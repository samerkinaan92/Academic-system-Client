package Controller;

import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

import application.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PrincipalMenuBarController {
	
	
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
	

	public void message(ActionEvent e){
	
	}
	
	
	/** Common Actions */
	
	
	

	/** Private Actions */
	
	public void ViewSystemData(ActionEvent e) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/PRCPL_ViewSystemData.fxml"));;
		Main.getRoot().setCenter(pane);
		
	}
	
	public void ViewStatisticalReport(ActionEvent e){
		
	}
	
	public void ApproveTeacherAssignment(ActionEvent e){
		
	}
	
	public void ApproveStudentChange(ActionEvent e){
		
		
	}
	

    @FXML
    void RemovePerEvent(ActionEvent event) throws IOException {
    	 	
 
    	try {
		   URL paneOneUrl = getClass().getResource("/FXML/PrincipalDeletePermissions.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException e) {
            e.printStackTrace();
          }        
    }
    
	
	/** Private Actions */

}
