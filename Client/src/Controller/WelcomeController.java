package Controller;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;

/**
 * controller with welcome screen
 * @author SAM
 *
 */
public class WelcomeController implements Initializable{
    @FXML
    private Text welcomeTxt;
    
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		String stri;
		stri = Main.user.getName();
		welcomeTxt.setText("Welcome "+stri );
	}

}
