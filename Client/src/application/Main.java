package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class Main extends Application{
	
	// Creating a static client to pass to the controller
	public static ClientConnection client;
	
	
	// Creating a static root to pass to the controller
	//private static BorderPane root = new BorderPane();

	/**
	 * Just a root getter for the controller to use
	 */
	/*
	public static BorderPane getRoot() {
		return root;
	}
	*/
	
	@Override
	public void start(Stage stage) throws Exception { // Show login screen on start.
		
		Parent login = FXMLLoader.load(getClass().getResource("/FXML/Login.fxml"));
		stage.getIcons().add(new Image("/user-login-icon-14.png"));
	    
	    Scene scene = new Scene(login);
	    
	    stage.setTitle("Login");
	    stage.setScene(scene);
	    stage.show();
	    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
