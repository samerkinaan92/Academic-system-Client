
	package Controller;

	import java.awt.Button;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import Entity.Course;
import Entity.Semester;
import Entity.Teacher;
import Entity.claSS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * This is the controller class for: "PRCPL_ViewSystemData.fxml"
 * @author Or Cohen
 *
 */
	public class PRCPL_ViewSystemDataController implements Initializable{
/** combobox to Choose data type */
	    @FXML
	    private ComboBox<String> dataCB;
/** button to present chosen data type */
	    @FXML
	    private Button SelectBtn;

	  /* falg to mark which data type selected */
	    static int flag;
	    @FXML
	    private Text txt;
	    /** text of lable */
	    @FXML
	    private Text ChooseLabel;
	    /** text of lable */
	    @FXML
	    private Text secondTxt;
	    /** ArrayList of Semesters */
	    ArrayList<Semester> semesterArr = Semester.getSemesters();
/** String of choosen semester */
	    static String semesterID;
	    

	    //-----------------------------------------------------------------------------------------------------------------------------

	    //-----------------------------------------------------------------------------------------------------------------------------
/** array of data types */
	    private final String[] dataType = {"Classes & Students & Parents","Teachers","Courses"};
	    /** list to show array of data types */
	    private final ObservableList<String> boxDataType = FXCollections.observableArrayList(dataType);
	  //-----------------------------------------------------------------------------------------------------------------------------

   /*
	    @FXML
    void WhenChoosed2(ActionEvent event) {
    	
    }
*/

		//-----------------------------------------------------------------------------------------------------------------------------
		
		
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {// Initialize window.

			dataCB.setItems(boxDataType);

			}  
		
/**
 * handle button to show choosed data type window 
 * @param event
 * @throws IOException
 */
	    @FXML
	    void clickChoose(ActionEvent event) throws IOException {
	    
	    	String type;
	    	type = dataCB.getSelectionModel().getSelectedItem();
			switch (type){
			
			case "Teachers":
				flag = 1;
				OpenTeachersInfo();
				break;
			case "Classes & Students & Parents":
				flag = 2;
				OpenClassesInfo();
				break;
			case "Courses": 
				flag = 3;
				OpenCoursesInfo();
				break;
			}
		    
		}


/**
 * open window of Teachers Info
 * @throws IOException
 */
		void OpenTeachersInfo() throws IOException{

			Stage TeachersInfoStage = new Stage();
			 
			    
			    // constructing our scene
			    URL url = getClass().getResource("/FXML/ViewTeacherInfo.fxml");
			    AnchorPane pane = FXMLLoader.load( url );
			    Scene scene = new Scene( pane );
			    
			    // setting the stage
			    TeachersInfoStage.setScene( scene );
			    TeachersInfoStage.setTitle( "Teachers Info" );
			    TeachersInfoStage.show();

    	}
    	
    	/**
    	 * open window of classes, students & parents Info
    	 * @throws IOException
    	 */
    	void OpenClassesInfo() throws IOException{
    		

			Stage ClassesInfoStage = new Stage();
			 
			    
			    // constructing our scene
			    URL url = getClass().getResource("/FXML/ViewInfoClass.fxml");
			    AnchorPane pane = FXMLLoader.load( url );
			    Scene scene = new Scene( pane );
			    
			    // setting the stage
			    ClassesInfoStage.setScene( scene );
			    ClassesInfoStage.setTitle( "Classes Info" );
			    ClassesInfoStage.show();

    		
    	}
    	
    	/**
    	 * open window of Courses Info
    	 * @throws IOException
    	 */
    	void OpenCoursesInfo() throws IOException{
    		
			Stage CoursesInfoStage = new Stage();
			 
		    
		    // constructing our scene
		    URL url = getClass().getResource("/FXML/ViewCoursesInfo.fxml");
		    AnchorPane pane = FXMLLoader.load( url );
		    Scene scene = new Scene( pane );
		    
		    // setting the stage
		    CoursesInfoStage.setScene( scene );
		    CoursesInfoStage.setTitle( "Courses Info" );
		    CoursesInfoStage.show();


    	}


	}

