package application;
	
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import Entity.User;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class Main extends Application{
	
	// Creating a static client to pass to the controllers
	public static ClientConnection client;
	
	// Creating a static client to pass to the controllers
	public static User user;
	
	// Creating a static root to pass to the controllers
	private static BorderPane root = new BorderPane();

	/**
	 * Just a root getter for the controllers to use
	 */
	public static BorderPane getRoot() {
		return root;
	}
	
	
	@Override
	public void start(Stage stage) throws Exception { // Show login screen on start.
		
		Parent login = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
		stage.getIcons().add(new Image("/user-login-icon-14.png"));
	    
	    Scene scene = new Scene(login);
	    
	    stage.setTitle("Login");
	    stage.setScene(scene);
	    stage.show();
	    }
	
	
	/**openMain()- Main function of the program
	 * @param type - indicate the role of the user.
	 * */
	public static void openMain(String type){
		
		String fxml_url = "/FXML/";
			
			
			switch (type){
			
			case "Student": fxml_url += "StudentMenuBar.fxml"; break;
			case "Parent": fxml_url += "ParentMenuBar.fxml"; break;
			case "Secretary": fxml_url += "SecretaryMenuBar.fxml"; break;
			case "Teacher": fxml_url += "TeacherMenuBar.fxml"; break;
			case "Principal": fxml_url += "PrincipalMenuBar.fxml"; break;
			case "System Manager": fxml_url += "SystemManegerMenuBar.fxml"; break;
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
				ex.printStackTrace();
			}
		    
		 
		    
		    Scene scene = new Scene(root, 640, 480);
		    scene.getStylesheets().add(Main.class.getResource("/FXML/application.css").toExternalForm());
		    
		    Stage primaryStage = new Stage();
		    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		          public void handle(WindowEvent we) {
		              logOut();
		          }
		      });
		    primaryStage.setScene(scene);
		    primaryStage.getIcons().add(new Image("/school_icon.png"));
		    primaryStage.setTitle("Academic system for high school");
		    primaryStage.setResizable(false);
		    primaryStage.show(); 
	}
	
	
	/**logOut() - log out function by the user. close the main scene and replace it with the login screen.
	 * Also sending log out message for updating the DB.
	 * @return - 1: operation succeeded. 0: operation failed.
	 * */
	public static int logOut(){
		HashMap<String, String> msg = new HashMap<>();
		msg.put("msgType", "update");
		msg.put("query", "UPDATE users SET isLogin = 0 WHERE id = '" + user.getID() + "';");
		
		try{
		client.sendMessageToServer(msg);
		}catch(Exception e){
			e.printStackTrace();
		}
			
		synchronized (client){
			try {
				client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int answer = (int)client.getMessage();
		client = null;
		root = new BorderPane(); 
		return answer;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
