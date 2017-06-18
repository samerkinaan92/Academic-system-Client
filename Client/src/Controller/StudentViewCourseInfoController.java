package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Entity.Semester;
import Entity.StudentCourse;
import Entity.claSS;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**This controller presenting all the courses information by semesters*/
public class StudentViewCourseInfoController implements Initializable{

	/**cTable - Table that presents the course information.*/
    @FXML
    private TableView<StudentCourse> cTable;

    /**Cid - present the course ID in the course table.*/
    @FXML
    private TableColumn<StudentCourse, String> Cid;

    /**cName - present the course name in the course table.*/
    @FXML
    private TableColumn<StudentCourse, String> cName;

    /**cWH - present the course weekly hours in the course table.*/
    @FXML
    private TableColumn<StudentCourse, String> cWH;

    /**CTUname - present the course teaching unit in the course table.*/
    @FXML
    private TableColumn<StudentCourse, String> CTUname;
    
    @FXML
    private TableColumn<StudentCourse, String> teacClumn;

    /**cComBox - combo box for presenting the semesters.*/
    @FXML
    private ComboBox<String> cComBox;


    /**weeklyHouLabel - label that present message the weekly hours for the selected semester.*/
    @FXML
    private Label weeklyHouLabel;

    /**selectedSem - Chosen semester from the semester combobox*/
    private String selectedSem;
    
    
    
    /**className - the class name of the student.*/
    private String className;
    
    /**courseArr - Contain the courses of the student.*/
    private ArrayList<StudentCourse>courseArr;
    
    /**courseForTable - contain the courses in ObservableList for the courses table.*/
    private ObservableList<StudentCourse> courseForTable;
    
    /**semListCB - ObservableList of semesters for the semester combobox.*/ 
    private ObservableList<String> semListCB;
    
    /**teachData - ObservableList of teachers for the teachers list.*/
    
	
	/**initialize() - initialize the current semester details.	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<String> organizedListSem = Semester.semList();
		semListCB= FXCollections.observableArrayList(organizedListSem);
		selectedSem=Semester.currSem();
		className=claSS.getClassByStud(Main.user.getID());
		cComBox.getSelectionModel().select(selectedSem);
		cComBox.setItems(semListCB);
		Cid.setCellValueFactory(new PropertyValueFactory<StudentCourse, String>("courseID"));
		cName.setCellValueFactory(new PropertyValueFactory<StudentCourse, String>("courseName"));
		CTUname.setCellValueFactory(new PropertyValueFactory<StudentCourse, String>("TUname"));
		cWH.setCellValueFactory(new PropertyValueFactory<StudentCourse, String>("weeklyHours"));
		teacClumn.setCellValueFactory(new PropertyValueFactory<StudentCourse, String>("teacherID"));
		setCourses(selectedSem);
		setWeekHour();
	}
    
	
	/**setWeekHour()  - defining the weekly hours for the selected semester.*/
	public void setWeekHour()
	{
		int sum=0;
		
		for(int i=0 ; i<courseArr.size() ; i++)
			sum+=Integer.parseInt(courseArr.get(0).getWeeklyHours());		
			weeklyHouLabel.setText("Weekly hours for semester "+ selectedSem +" is: "+ sum + ".");
	}
	
	
	/**setCourses setting the courses for the selected semester.*/
	public void setCourses(String sem){		
		courseArr= StudentCourse.getCoursesBySemStd(sem,Main.user.getID());
		courseForTable = FXCollections.observableArrayList(courseArr);
		cTable.setItems(courseForTable);
	}
	
	/**cComboBox() - event the listen for changes in the semester selection and set courses in Table according to the selection*/
	@FXML
    void cComboBox(ActionEvent event) {
    	String selectedSem = cComBox.getSelectionModel().getSelectedItem().toString();
    	this.selectedSem=selectedSem;
    	setCourses(selectedSem);
    	setWeekHour();
    	
    }


    	
    

}
