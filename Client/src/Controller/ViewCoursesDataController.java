package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entity.Course;
import Entity.Semester;
import Entity.TeachingUnit;
import Entity.claSS;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * This is the controller class for: "ViewCoursesInfo.fxml.fxml"
 * @author Or Cohen
 *
 */
public class ViewCoursesDataController  implements Initializable {
/** list of courses for the courseTableView */
	private final ObservableList<coursetInfo> courseData = FXCollections.observableArrayList();
	/** list of precourses for PreCourseVL */	
	private final ObservableList<String> preCoursesData = FXCollections.observableArrayList();
	/** list of classes for classesVL */	
	private final ObservableList<String> ClassInCourse = FXCollections.observableArrayList();
   /** combobox to chose semester */
	@FXML
    private ComboBox<String> SemesterCB;
	/** String to hold ID of Chosen Semester */
    public static String semesterID = new String();
    /** String to hold Chosen Semester */
    public String ChoosedSemester = new String();
    /** combobox to chose teaching unit */
	@FXML
    private ComboBox<String> TUComboBox;
	/** Button to Show Pre-Coursees and classes of course */
    @FXML
    private Button select;
    
    /** Button to Show assignments of course */
    @FXML
    private Button ShowAssignments;
    /** Table view to be show courses Info*/
    @FXML
    private TableView<coursetInfo> CoursesTableView;
    /** Colum in CoursesTableView to view Course ID*/
    @FXML
    private TableColumn<coursetInfo, String> CourseIDColum;
    /** Colum in CoursesTableView to view Course Name*/
    @FXML
    private TableColumn<coursetInfo, String> CourseNameColum;
    /** Colum in CoursesTableView to view Course Weekly Hours*/
    @FXML
    private TableColumn<coursetInfo, String> WeeklyHoursColum;
    /** Colum in CoursesTableView to view Courses teaching unit*/
    @FXML
    private TableColumn<coursetInfo, String> TUColum;
    /** Button to Show Pre-Coursees and classes of course */
    @FXML
    private Button ChooseBtn;
    /** List View to view classes study selected course */
    @FXML
    private ListView<String> classesVL;
    /** List View to view Pre-Courses of course*/
    @FXML
    private ListView<String> PreCourseVL;
	/** ArrayList to get all semesters*/
    ArrayList<Semester> semester = Semester.getSemesters();
    /** string to hold semester name */
    ArrayList<String> semesterStr = new ArrayList<String>(); 
    /** string to hold the selected course ID */
    public static String selectedCourseID = new String();




	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {// Initialize window.
		TUComboBox.setDisable(true);
		  for (int i = 0 ; i < semester.size() ; i++)
			  semesterStr.add(Semester.getSemesters().get(i).getYear()+" "+Semester.getSemesters().get(i).getSeason());
		    	

	  
	    final ObservableList<String> semesterId = FXCollections.observableArrayList(semesterStr); 
	    SemesterCB.setItems(semesterId);
		CourseIDColum.setCellValueFactory(new PropertyValueFactory<coursetInfo, String>("id"));
		CourseNameColum.setCellValueFactory(new PropertyValueFactory<coursetInfo, String>("name"));
		WeeklyHoursColum.setCellValueFactory(new PropertyValueFactory<coursetInfo, String>("WH"));
		TUColum.setCellValueFactory(new PropertyValueFactory<coursetInfo, String>("TU"));
		
		ArrayList<String> tuList = new ArrayList<String>();
		tuList = TeachingUnit.getTeachingUnit();
		for(int i = 0 ; i < tuList.size() ; i++)
			tuList.get(i);
		tuList.add("<< All Teaching Units >>");

		final ObservableList<String> TeachU = FXCollections.observableArrayList(tuList);
		TUComboBox.setItems( TeachU);
	}
	
/**
 * handle Semester combobox selection
 * @param event
 */
    @FXML
    void ChoosedSemester(ActionEvent event) { 
    	TUComboBox.setDisable(true);
    	TUComboBox.setDisable(false);
    	TUComboBox.show();
    	 courseData.clear();
    	 ChoosedSemester = SemesterCB.getSelectionModel().getSelectedItem();
    	 int j = ChoosedSemester.indexOf(" "); // 4

  	   String Year = ChoosedSemester.substring(0, j); // from 0 to 3

  	   String Season = ChoosedSemester.substring(j+1); // after the space to the rest of the line
  	   
  	 semesterID = Semester.getSemesterID(Year, Season).get(0);
  	 
    }

/**
 * 	handle teaching unit combobox selection
 * @param event
 */

    @FXML
    void TUSelect(ActionEvent event) {

		  courseData.clear();
		  ClassInCourse.clear();
		  String type;
	    	type = TUComboBox.getSelectionModel().getSelectedItem();

	    	ArrayList<Course> cousreArr = new ArrayList<Course>();
			if (type.equals("<< All Teaching Units >>"))
					{
				cousreArr = Course.getCoursesofSemester(semesterID);
					}
				else
				{
					cousreArr =	Course.getCoursesByTU(type ,semesterID );
				}

				for( int i = 0 ; i < cousreArr.size() ; i++)
				{
					
					String id = Integer.toString(cousreArr.get(i).getCourseID());
					String name = cousreArr.get(i).getName();
					String WH = Integer.toString(cousreArr.get(i).getWeeklyHours()); 
					String TU = cousreArr.get(i).getTUID();
					
					coursetInfo temp = new coursetInfo(id , name, WH, TU);	
					
					courseData.add(temp);
				}
	    	
				CoursesTableView.setItems(courseData);
	    	
					}
/**
 * handle press button to show information of chosen course
 * @param event
 */
	    @FXML
	    void lickOnChooseBtn(ActionEvent event) {
			preCoursesData.clear();
			ClassInCourse.clear();
	    	coursetInfo ChoosenCourse = CoursesTableView.getSelectionModel().selectedItemProperty().get();  // Choosed Course
	    	selectedCourseID = ChoosenCourse.getId();												// Choosed Course ID
	    	ArrayList<String> preCoursesIDs = Course.getPreCoursesId(selectedCourseID);						// Array Of Pre-Courses (ID)
	    	ArrayList<String> preCoursesNames = new ArrayList<String>();
	    	ArrayList<String> classes = new ArrayList<String>();
	    	classes = claSS.getClassesOfCourse(selectedCourseID , semesterID);
	    	for (int i = 0 ; i < classes.size(); i++)
	    		ClassInCourse.add(classes.get(i));
	    	classesVL.setItems(FXCollections.observableArrayList(ClassInCourse));

	    	for (int i = 0 ; i < preCoursesIDs.size() ; i++)
	    	{
	    		preCoursesNames.add(Course.getCourseName(preCoursesIDs.get(i)));
	    		preCoursesData.add(preCoursesNames.get(i));
	    	}
	    	if (preCoursesData.isEmpty())
	    		preCoursesData.add("No PreCourses!");
	    	PreCourseVL.setItems(FXCollections.observableArrayList(preCoursesData));
	    	
	    }

	    

/**
 * handle press button to show assignments of selected course
 * @param event
 * @throws IOException
 */
@FXML
void ClickOnShowAss(ActionEvent event) throws IOException {

	coursetInfo ChoosenCourse = CoursesTableView.getSelectionModel().selectedItemProperty().get();  // Choosed Course
	selectedCourseID = ChoosenCourse.getId();												// Choosed Course ID
	
	Stage ViewAssignmentsStage = new Stage();
	 
    
    // constructing our scene
    URL url = getClass().getResource("/FXML/ShowAssignment.fxml");
    AnchorPane pane = FXMLLoader.load( url );
    Scene scene = new Scene( pane );
    
    // setting the stage
    ViewAssignmentsStage.setScene( scene );
    ViewAssignmentsStage.setTitle( "Assignment Info" );
    ViewAssignmentsStage.show();

    

}
/**
 * class to present Course information	    	
 * @author Or Cohen
 *
 */
	 public static class coursetInfo{

		    //* getters & setters */
	        private StringProperty id;
	        public void setId(String value) { idProperty().set(value); }
	        public String getId() { return idProperty().get(); }
	        public StringProperty idProperty() { 
	            if (id == null) id = new SimpleStringProperty(this, "id");
	            return id; 
	        } 
	      //* getters & setters */
	    	private StringProperty name;
	        public void setName(String value) { nameProperty().set(value); }
	        public String getName() { return nameProperty().get(); }
	        public StringProperty nameProperty() { 
	            if (name == null) name = new SimpleStringProperty(this, "name");
	            return name; 
	        }
	      //* getters & setters */
	        private StringProperty WH;
	        public void setWH(String value) { WHProperty().set(value); }
	        public String getWH() { return WHProperty().get(); }
	        public StringProperty WHProperty() { 
	            if (WH == null) WH = new SimpleStringProperty(this, "Weekly Hours");
	            return WH; 
	        } 
	      //* getters & setters */
	        private StringProperty TU;
	        public void setTU(String value) { TUProperty().set(value); }
	        public String getTU() { return TUProperty().get(); }
	        public StringProperty TUProperty() { 
	            if (TU == null) TU = new SimpleStringProperty(this, "Teaching Unit");
	            return TU; 
	        }
	      //* constructor */
	        public coursetInfo(String id, String name, String WH, String TU){
	        	setId(id);
	        	setName(name);
	        	setWH(WH);
	        	setTU(TU);
	        }
	
	 }

}