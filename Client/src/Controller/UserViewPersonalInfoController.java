package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Entity.Parent;
import Entity.User;
import Entity.claSS;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**UserViewPersonalInfoController - Presenting the personal details of the users.
 * @author Tal Asulin*/
public class UserViewPersonalInfoController implements Initializable{
	
	
	private static User usr;
    
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    
    /**usrName - user name*/
    @FXML
    private Label usrName;

    /**usrID - user ID*/
    @FXML
    private Label usrID;

    /**usrAddr - user address*/
    @FXML
    private Label usrAddr;

    /**usrPhone - user phone number*/
    @FXML
    private Label usrPhone;

    /**usrEmail - user email*/
    @FXML
    private Label usrEmail;

    /**stdP2 - student parent 2*/
    @FXML
    private Label stdP2;
    
    /**stdP1 - student parent 1*/
    @FXML
    private Label stdP1;
    
    /**sClass - class label*/ 
    @FXML
    private Label sClass;
    
    /**stdClass - student class details*/
    @FXML
    private Label stdClass;
    
    @FXML
    private Button backBtn;

    /**stdpar1 - Student parent details.*/
    @FXML
    private Label stdpar1;

    /**Parent2 - parent2 label*/
    @FXML
    private Label Parent2;

    /**stdpar2 - student parent deatils*/
    @FXML
    private Label stdpar2;
    
    private static String stdID;
    

    @FXML
    private Label titleLabal;

    
    /**initialize() - initialize the details according to the current login user.*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		usr=User.getUserInfo(stdID);
		usrName.setText(usr.getName());
		usrID.setText(usr.getID());
		usrAddr.setText(usr.getAddress());
		usrPhone.setText(usr.getPhone());
		usrEmail.setText(usr.getEmail());
		
		if(Main.user.getType().equals("Parent")){
			backBtn.setVisible(true);
		}
		
		titleLabal.setText(usr.getName());
		
		/*switch case for adding more details according to the user role.*/
		switch (usr.getType()){
		
		case "Student": 
			stdClass.setText(claSS.getClassByStud(usr.getID()));
			sClass.setVisible(true);
			stdClass.setVisible(true);
			ArrayList<String> result=Parent.getParNameByStdID(usr.getID());
			
			if(result.size()==1){
				stdP1.setVisible(true);
				stdpar1.setText(result.get(0));
				stdpar1.setVisible(true);
			}
				
			else{
				stdP1.setVisible(true);
				stdpar1.setText(result.get(0));
				stdpar1.setVisible(true);
				stdP2.setVisible(true);
				stdpar2.setText(result.get(0));
				stdpar2.setVisible(true);
			}
			
		}
						
		
	}
	
	public static void setUser(String ID)
	{
		stdID=ID;
	}
	
	

    @FXML
    void BackStdSelection(ActionEvent event) {
    	try {
		   java.net.URL paneOneUrl = getClass().getResource("/FXML/ParentSelectStd.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException exp) {
        	exp.printStackTrace();
          }  
    }
	

}
