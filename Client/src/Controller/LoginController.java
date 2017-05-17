package Controller;

import java.io.IOException;
import java.net.URL;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginController {
	
	private static BorderPane root = new BorderPane();
	
	public void OnLogin(ActionEvent e){
	
		((Node)(e.getSource())).getScene().getWindow().hide(); // Close login window.
		
		
	
		
		
		URL menuBarUrl = getClass().getResource("/FXML/StudentMenuBar.fxml");
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
	    scene
	      .getStylesheets()
	      .add(getClass()
	      .getResource("/FXML/application.css")
	      .toExternalForm());
	    
	    Stage primaryStage = new Stage();
	    primaryStage.setScene(scene);
	    primaryStage.getIcons().add(new Image("/school_icon.png"));
	    primaryStage.setTitle("Academic system for high school");
	    primaryStage.setResizable(false);
	    primaryStage.show();
	}

}
