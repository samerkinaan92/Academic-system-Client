package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Entity.ParentStudent;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


/**
 * This is the controller class for: "ParentSelectStd.fxml"
 * @author Tal Asulin
 * */
public class ParentChooseStdController implements Initializable{
	
	/**stdPar - ArrayList which use for the relationship between parent and student.*/
	private ArrayList<ParentStudent> stdPar;
	
	/**stdList - List of students.*/
	private ObservableList<String> stdList = FXCollections.observableArrayList();
	
	/**stdList - Saving the selected student from combobox.*/
	private String selectStd;
	
    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private Label titleLabel;
    
    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private Label warnMsg;
    
    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private Button parInfoBtn;

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private Button showEvalBen;

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private Button showCourBtn;

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private ComboBox<String> stdCbox;

    /**initialize() - initialize the screen options on load.*/
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {		
		stdPar=ParentStudent.getStudByPar(Main.user.getID(), Main.user.getID());
		if(stdPar.size()==0)
			warnMsg.setVisible(true);		
		else{
			for(int i=0 ; i< stdPar.size() ; i++)
				stdList.add(stdPar.get(i).getStudName());
			
			stdCbox.setItems(stdList);
		}
		
		stdCbox.valueProperty().addListener(new ChangeListener<String>() {
    		@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
    			if(stdCbox.getSelectionModel().getSelectedItem().toString()!=null)
					{
    				parInfoBtn.setDisable(false);
    				showEvalBen.setDisable(false);
    				showCourBtn.setDisable(false);
    				warnMsg.setVisible(false);
					}
    			}
			});
		
	}
	
    
    /**
     * @param event
     * Use to present the student courses
     * */
    @FXML
    void showStdCourses(ActionEvent event) {
    	
    	try{
	    	if((selectStd=stdCbox.getSelectionModel().getSelectedItem().toString())!=null)
		    	for(int i=0 ; i< stdPar.size() ; i++)
		    		if(stdPar.get(i).getStudName().equals(selectStd))
		    		{
		    			if(stdPar.get(i).getIsParBloc().equals("1")){
		    				warnMsg.setText("There is no permissions for student: "+stdPar.get(i).getStudName());
		    				warnMsg.setVisible(true);
		    			}
		    			else{
		    				warnMsg.setVisible(false);
		    		    	try {
		    		    		StudentViewCourseInfoController.setUser(stdPar.get(i).getStudID(), stdPar.get(i).getStudName());
		    				   URL paneOneUrl = getClass().getResource("/FXML/StudentViewCourses.fxml");
		    				   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		    				   BorderPane border = Main.getRoot();			    
		    				   border.setCenter(paneOne);
		    				   
		    		        } catch (IOException exp) {
		    		        	exp.printStackTrace();
		    		          }       
		    				
		    			}
		    		}
    	}
    	catch (NullPointerException e)
    	{}
    }
    
    
    
    /**
     * @param event
     * Use do present the student evaluations form.
     * */
    @FXML
    void showEval(ActionEvent event) {
    	try{
    	if((selectStd=stdCbox.getSelectionModel().getSelectedItem().toString())!=null)
	    	for(int i=0 ; i< stdPar.size() ; i++)
	    		if(stdPar.get(i).getStudName().equals(selectStd))
	    		{
	    			if(stdPar.get(i).getIsParBloc()=="1"){
	    				warnMsg.setText("There is no permissions for student: "+stdPar.get(i).getStudName());
	    				warnMsg.setVisible(true);
	    			}
	    			else{
	    				warnMsg.setVisible(false);
	    				StudentViewEvaluations.setUsr(stdPar.get(i).getStudID(), stdPar.get(i).getStudName());
	    		    	try {
	    				   URL paneOneUrl = getClass().getResource("/FXML/StudenViewEvaluation.fxml");
	    				   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
	    				   BorderPane border = Main.getRoot();			    
	    				   border.setCenter(paneOne);
	    				   
	    		        } catch (IOException exp) {
	    		        	exp.printStackTrace();
	    		          }       
	    				
	    			}
	    		}	
    	}
    	catch(NullPointerException e){     
    	}

    }
    
    
    /**
     * showPesonalInfo()
     * @param event
     * Use do present the student personal information.
     * */
    @FXML
    void showPesonalInfo(ActionEvent event) {
    	try {
    		if((selectStd=stdCbox.getSelectionModel().getSelectedItem().toString())!=null)
    	    	for(int i=0 ; i< stdPar.size() ; i++)
    	    		if(stdPar.get(i).getStudName().equals(selectStd))
    	    		{    
    	    			if(stdPar.get(i).getIsParBloc()=="1"){
    	    				warnMsg.setText("There is no permissions for student: "+stdPar.get(i).getStudName());
    	    				warnMsg.setVisible(true);
    	    			}else
    	    		try{
					UserViewPersonalInfoController.setUser(stdPar.get(i).getStudID());
					URL paneOneUrl = getClass().getResource("/FXML/UserViewPersonalInfo.fxml");
					AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
					BorderPane border = Main.getRoot();			    
					border.setCenter(paneOne);
    	    		}
    	         	catch (IOException exp) {
    	            	exp.printStackTrace();
    	              }
    	    		}
    	    	}
	    	catch(NullPointerException e)
	    	{
	    	}
    	}
    		

    	
    	
    


    
}
