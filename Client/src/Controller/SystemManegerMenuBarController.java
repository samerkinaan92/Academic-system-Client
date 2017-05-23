package Controller;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;


public class SystemManegerMenuBarController {

	
	/** Common Actions */
	
	public void previous(ActionEvent e){
		
	}
	
	public void main_menu(ActionEvent e){
	
	}

	public void quit(ActionEvent e){
		
	}

	public void logout(ActionEvent e){
	
	}
	
	/** Common Actions */

		
	/** Private Actions */
	
	public void define(ActionEvent e) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/SystemManagerDefineCourses.fxml"));;
		Main.getRoot().setCenter(pane);
	}
	
	/** Private Actions */

}
