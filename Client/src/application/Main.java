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
	
	public static void openMain(String type){
		
		String fxml_url = "/FXML/";
			
			
			switch (type){
			
			case "Student": fxml_url += "StudentMenuBar.fxml"; break;
			case "Parent": fxml_url += "ParentMenuBar.fxml"; break;
			case "Secretary": fxml_url += "SecretaryMenuBar.fxml"; break;
			case "Teacher": fxml_url += "TeacherMenuBar.fxml"; break;
			case "Principal": fxml_url += "PrincipalMenuBar.fxml"; break;
			case "SystemManager": fxml_url += "SystemManegerMenuBar.fxml"; break;
			}
			
			
			URL menuBarUrl = Main.class.getResource(fxml_url);
		    URL welcomePaneUrl =  Main.class.getResource("/FXML/WelcomeScreen.fxml");
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
		    scene.getStylesheets().add(Main.class.getResource("/FXML/application.css").toExternalForm());
		    
		    Stage primaryStage = new Stage();
		    primaryStage.setScene(scene);
		    primaryStage.getIcons().add(new Image("/school_icon.png"));
		    primaryStage.setTitle("Academic system for high school");
		    primaryStage.setResizable(false);
		    primaryStage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
