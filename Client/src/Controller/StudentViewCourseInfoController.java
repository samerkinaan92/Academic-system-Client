package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Entity.ClassCourse;
import Entity.Course;
import Entity.Semester;
import Entity.Teacher;
import Entity.claSS;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**This controller presenting all the courses information by semesters*/
public class StudentViewCourseInfoController implements Initializable{

	/**cTable - Table that presents the course information.*/
    @FXML
    private TableView<Course> cTable;

    /**Cid - present the course ID in the course table.*/
    @FXML
    private TableColumn<Course, String> Cid;

    /**cName - present the course name in the course table.*/
    @FXML
    private TableColumn<Course, String> cName;

    /**cWH - present the course weekly hours in the course table.*/
    @FXML
    private TableColumn<Course, String> cWH;

    /**CTUname - present the course teaching unit in the course table.*/
    @FXML
    private TableColumn<Course, String> CTUname;

    /**cComBox - combo box for presenting the semesters.*/
    @FXML
    private ComboBox<String> cComBox;

    /**teachList - List of teachers name for selected course.*/
    @FXML
    private ListView<String> teachList;

    /**teacherCourLabel - label that present message if teachers for course found.*/
    @FXML
    private Label teacherCourLabel; 

    /**weeklyHouLabel - label that present message the weekly hours for the selected semester.*/
    @FXML
    private Label weeklyHouLabel;

    /**selectedSem - Chosen semester from the semester combobox*/
    private String selectedSem;
    
    /**className - the class name of the student.*/
    private String className;
    
    /**courseArr - Contain the courses of the student.*/
    private ArrayList<Course>courseArr;
    
    /**courseForTable - contain the courses in ObservableList for the courses table.*/
    private ObservableList<Course> courseForTable;
    
    /**semListCB - ObservableList of semesters for the semester combobox.*/ 
    private ObservableList<String> semListCB;
    
    /**teachData - ObservableList of teachers for the teachers list.*/
	private ObservableList<String> teachData = FXCollections.observableArrayList();;
    
	
	/**initialize() - initialize the current semester details.	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<String> organizedListSem = Semester.semList();
		semListCB= FXCollections.observableArrayList(organizedListSem);
		selectedSem=Semester.currSem();
		className=claSS.getClassByStud(Main.user.getID());
		cComBox.getSelectionModel().select(selectedSem);
		cComBox.setItems(semListCB);
		Cid.setCellValueFactory(new PropertyValueFactory<Course, String>("CourseID"));
		cName.setCellValueFactory(new PropertyValueFactory<Course, String>("Name"));
		CTUname.setCellValueFactory(new PropertyValueFactory<Course, String>("TUID"));
		cWH.setCellValueFactory(new PropertyValueFactory<Course, String>("weeklyHours"));
		setCourses(selectedSem);
		setWeekHour();
		teachList.setVisible(false);
		teacherCourLabel.setVisible(false);
	}
    
	
	/**setWeekHour()  - defining the weekly hours for the selected semester.*/
	public void setWeekHour()
	{
		int sum=0;
		
		for(int i=0 ; i<courseArr.size() ; i++)
			sum+=courseArr.get(0).getWeeklyHours();		
			weeklyHouLabel.setText("Weekly hours for semester "+ selectedSem +" is: "+ sum + ".");
	}
	
	
	/**setCourses setting the courses for the selected semester.*/
	public void setCourses(String sem){		
		courseArr= Course.getCoursesBySemStd(sem,Main.user.getID());
		courseForTable = FXCollections.observableArrayList(courseArr);
		cTable.setItems(courseForTable);
		teachList.setVisible(false);
		teacherCourLabel.setVisible(false);
	}
	
	/**cComboBox() - event the listen for changes in the semester selection and set courses in Table according to the selection*/
	@FXML
    void cComboBox(ActionEvent event) {
    	String selectedSem = cComBox.getSelectionModel().getSelectedItem().toString();
    	this.selectedSem=selectedSem;
    	setCourses(selectedSem);
    	setWeekHour();
    	
    }

	/**showTech() - Presenting the teachers of the selected course.*/
    @FXML
    void showTech(MouseEvent event) {
    	Course course;
    	ArrayList<Teacher> courseTech;
    	teachList.setVisible(false);
    	teacherCourLabel.setVisible(false);
    	
    	if((course = cTable.getSelectionModel().getSelectedItem())!=null)
    	{  		
    		if((courseTech = ClassCourse.getTeacherByCourse(String.valueOf(course.getCourseID()),className))!=null) // if there in no teachers for the selected class.
    		{
        		teacherCourLabel.setText("Teachers of course "+course.getName()+":");
        		teacherCourLabel.setVisible(true);
	    		teachData.clear();
	    		for(int i=0; i<courseTech.size() ; i++)
	    			teachData.add(courseTech.get(i).getName());
	    		teachList.setItems(teachData);
	    		teachList.setVisible(true);
    		}
    		else{
        		teacherCourLabel.setText("No teachers for course "+course.getName()+".");
        		teacherCourLabel.setVisible(true);
    		}
    	}

    	
    }

}
