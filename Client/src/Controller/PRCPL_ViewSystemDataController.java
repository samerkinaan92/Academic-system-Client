
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

	public class PRCPL_ViewSystemDataController implements Initializable{

/*
	    @FXML
	    private ComboBox<String> SemesterCB;
*/
	    @FXML
	    private ComboBox<String> dataCB;

	    @FXML
	    private Button SelectBtn;

	  
	    static int flag;

	    @FXML
	    private Text txt;
	    

	    @FXML
	    private Text ChooseLabel;
	    
	    @FXML
	    private Text secondTxt;
	    
	    ArrayList<Semester> semesterArr = Semester.getSemesters();

	    static String semesterID;
	    

	    //-----------------------------------------------------------------------------------------------------------------------------

	    //-----------------------------------------------------------------------------------------------------------------------------

	    private final String[] dataType = {"Classes & Students & Parents","Teachers","Courses"};
	    
	    private final ObservableList<String> boxDataType = FXCollections.observableArrayList(dataType);
	  //-----------------------------------------------------------------------------------------------------------------------------

   
	    @FXML
    void WhenChoosed2(ActionEvent event) {
    	
    }


		//-----------------------------------------------------------------------------------------------------------------------------
		
		
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {

			dataCB.setItems(boxDataType);
			
				
			

			}  
		
	  
		


	    
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
    	

		void OpenParentInfo() throws IOException{

			Stage ParentInfoStage = new Stage();
			 
			    
			    // constructing our scene
			    URL url = getClass().getResource("/FXML/ViewParentInfo.fxml");
			    AnchorPane pane = FXMLLoader.load( url );
			    Scene scene = new Scene( pane );
			    
			    // setting the stage
			    ParentInfoStage.setScene( scene );
			    ParentInfoStage.setTitle( "Parents Info" );
			    ParentInfoStage.show();

    	}
    	
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
	    
    	void StudentsInfo(){
    		
    	}

	}

