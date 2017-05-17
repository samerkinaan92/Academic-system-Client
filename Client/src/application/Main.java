package application;
	
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application{
	
	// Creating a static client to pass to the controller
	public static ClientConnection client;
	
	
	// Creating a static root to pass to the controller
	private static BorderPane root = new BorderPane();

	/**
	 * Just a root getter for the controller to use
	 */
	public static BorderPane getRoot() {
		return root;
	}
	
	
	@Override
	public void start(Stage stage) throws Exception { // Show login screen on start.
		
		 
		Parent login = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
	    
	    Scene scene = new Scene(login);
	    
	    stage.setTitle("Login");
	    stage.setScene(scene);
	     
	    stage.show();
	    }
	/*
	public void openMain(){
		
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	*/
	
	public static void main(String[] args) {
		launch(args);
	}
}
