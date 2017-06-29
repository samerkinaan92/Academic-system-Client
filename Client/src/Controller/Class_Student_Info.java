package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controller.ParentViewInfoController.ParentInfo;
import Controller.ViewCoursesDataController.coursetInfo;
import Controller.ViewTeacherController.techertInfo;
import Entity.Course;
import Entity.Semester;
import Entity.Student;
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
 * This is the controller class for: "ViewInfoClass.fxml"
 * @author Or Cohen
 *
 */
public class Class_Student_Info implements Initializable {
	/** list for Student info */
	private final ObservableList<StudentInfo> Studentsdata = FXCollections.observableArrayList();
	/** list for courses info */
	final ObservableList<String> classCoursesData = FXCollections.observableArrayList();
	/** list of class names  */
	final ObservableList<String> classNames = FXCollections.observableArrayList(); 
	/** flag to choose if show parents of specific student or all students*/
	public static int flag;
/** button to show all parents of class */
	 @FXML
	 private Button ShowAllParents;

	    
	 /** Table view to be show students Info*/
    @FXML
    private TableView<StudentInfo> StudentsTableView;
    /** Colum in CoursesTableView to view student ID*/
    @FXML
    private TableColumn<StudentInfo, String> IDCollum;
    /** Colum in CoursesTableView to view student name*/
    @FXML
    private TableColumn<StudentInfo, String> NameCollum;
    /** Colum in CoursesTableView to view student email*/
    @FXML
    private TableColumn<StudentInfo, String> MailCollum;
    /** Colum in CoursesTableView to view student address*/
    @FXML
    private TableColumn<StudentInfo, String> AddressNum;
    /** Colum in CoursesTableView to view student phone number*/
    @FXML
    private TableColumn<StudentInfo, String> PhoneColum;
    /** button to show submittion of selected student */
    @FXML
    private Button SubBtn;
    /** button to show parents of selected student */
    @FXML
    private Button ShowParentsBtn;

  /** combobox to choose class */

    @FXML
    private ComboBox<String> ClassChooser;
/** button to show student of selected class */
    @FXML
    private Button SelectBtn;
/** listView for courses studied in selected course */
    @FXML
    private ListView<String> CoursesOfClassVL;
/** combobox to choose semester */
    @FXML
    private ComboBox<String> SemesterCB;
/** arraylist to holds all semesters */
    ArrayList<Semester> semesterArr = Semester.getSemesters();
    /** arraylist to holds all classes */
    ArrayList<claSS> ClassArr = claSS.getClasses();
    /** String to holds Choosed Class */
    public static String ChoosedClass = new String();
    /** String to holds Choosed Semester*/
    public static String ChoosedSemester = new String();
    /** String to holds Selected Student*/
    public static String SelectedStudent = new String();
    

    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {// Initialize window.
		SubBtn.setDisable(true);
		ShowParentsBtn.setDisable(true);
    	ArrayList<String> semesterIdArr = new ArrayList<String>();
    	ArrayList<String> ClassNamesArr = new ArrayList<String>();
    	
	    for (int i = 0 ; i < semesterArr.size() ; i++)
	    	semesterIdArr.add(Semester.getSemesters().get(i).getYear()+" "+Semester.getSemesters().get(i).getSeason());
	    final ObservableList<String> semesterId = FXCollections.observableArrayList(semesterIdArr); 
	    SemesterCB.setItems(semesterId);
	    for(int i = 0 ; i < ClassArr.size() ; i++)
	    {
	    ClassNamesArr.add(ClassArr.get(i).getClassName());
	    classNames.add(ClassNamesArr.get(i));
	    } 
	   ClassChooser.setItems(classNames);
		
		IDCollum.setCellValueFactory(new PropertyValueFactory<StudentInfo, String>("id"));
		NameCollum.setCellValueFactory(new PropertyValueFactory<StudentInfo, String>("name"));
		MailCollum.setCellValueFactory(new PropertyValueFactory<StudentInfo, String>("mail"));
		PhoneColum.setCellValueFactory(new PropertyValueFactory<StudentInfo, String>("telephone"));
		AddressNum.setCellValueFactory(new PropertyValueFactory<StudentInfo, String>("address"));
		
    }
    
	/**
	 * handdle choose class combobox
	 * @param event
	 * @throws IOException
	 */
    @FXML
    void ClichOnClass(ActionEvent event) throws IOException  {
    	SemesterCB.setDisable(false);
    	classCoursesData.clear();
    	//classNames.clear();
    	

    	ChoosedClass = ClassChooser.getSelectionModel().getSelectedItem();
    	//System.out.println(ChoosedClass);
    }
/**
 * handdle choose semester combobox
 * @param event
 * @throws IOException
 */
    @FXML
    void ClickOnSemester(ActionEvent event) throws IOException  {
    	classCoursesData.clear();
    	ChoosedSemester = SemesterCB.getSelectionModel().getSelectedItem();
      	 int j = ChoosedSemester.indexOf(" "); // 4

    	   String Year = ChoosedSemester.substring(0, j); // from 0 to 3

    	   String Season = ChoosedSemester.substring(j+1); // after the space to the rest of the line
    	 ChoosedSemester = Semester.getSemesterID(Year, Season).get(0);
    	 ArrayList<String> courseList = new ArrayList<String>();
    	 ArrayList<Course> courseArray = Course.getCoursesofClass(ChoosedClass, ChoosedSemester);
    	 for (int i = 0 ; i < courseArray.size() ; i++)
 	 	{
 	 	courseList.add(courseArray.get(i).getName());
 	 	classCoursesData.add(courseList.get(i));
 	 	//System.out.println(courseList.get(i));
 	 	
 	 	}
    	 CoursesOfClassVL.setItems(classCoursesData);
    }
    
/**
 * handle click on select button to show students info
 * @param event
 * @throws IOException
 */
    @FXML
    void ClickSelect(ActionEvent event)  throws IOException {
    	classCoursesData.clear();
    	Studentsdata.clear();
	 	
	 	
	 	
	 	
	 	ArrayList<Student> studentsList = claSS.getStudentsOfClass(ChoosedClass);
	 	ShowParentsBtn.setDisable(false);
	 	SubBtn.setDisable(false);
	 	for (int i = 0 ; i < studentsList.size() ; i++)
		{
	 		StudentInfo temp = new StudentInfo(studentsList.get(i).getID(),
	 										   studentsList.get(i).getName(),
	 										   studentsList.get(i).getEmail(),
	 										   studentsList.get(i).getAddress(),
	 										   studentsList.get(i).getPhone());

	 		Studentsdata.add(temp);
		}
	 	StudentsTableView.setItems((FXCollections.observableArrayList(Studentsdata)));//.setItems(FXCollections.observableArrayList(Studentsdata));
    }
    /**
     * handle click on show parents button to show parents of selected student info
     * @param event
     * @throws IOException
     */
    @FXML
    void ClickOnShowParents(ActionEvent event) throws IOException {
    	SelectedStudent = StudentsTableView.getSelectionModel().selectedItemProperty().get().getId();
    	flag = 1;
	
	Stage ViewParentsStage= new Stage();
	 
    
    // constructing our scene
    URL url = getClass().getResource("/FXML/ParentView.fxml");
    AnchorPane pane = FXMLLoader.load( url );
    Scene scene = new Scene( pane );
    
    // setting the stage
    ViewParentsStage.setScene( scene );
    ViewParentsStage.setTitle( "Parents Info" );
    ViewParentsStage.show();

    

}
    
/**
 * handle click on show all parents button to show all parents info
 * @param event
 * @throws IOException
 */
    @FXML
    void ClickShowAll(ActionEvent event)  throws IOException {
       
        	//SelectedStudent = StudentsTableView.getSelectionModel().selectedItemProperty().get().getId();
    	ChoosedClass = ClassChooser.getSelectionModel().getSelectedItem();	
    	flag = 2;
    	
    	Stage ViewParentsStage= new Stage();
    	 
        
        // constructing our scene
        URL url = getClass().getResource("/FXML/ParentView.fxml");
        AnchorPane pane = FXMLLoader.load( url );
        Scene scene = new Scene( pane );
        
        // setting the stage
        ViewParentsStage.setScene( scene );
        ViewParentsStage.setTitle( "Parents Info" );
        ViewParentsStage.show();

    }
/**
 * handle click on show submitionsbutton to show all submittions of selected student
 * @param event
 * @throws IOException
 */
    @FXML
    void ClickOnShowSub(ActionEvent event)  throws IOException {
    	StudentInfo ChoosenStudent = StudentsTableView.getSelectionModel().selectedItemProperty().get();  // Choosed Course
    	SelectedStudent = ChoosenStudent.getId();												// Choosed Course ID
    	
    	Stage ViewAssignmentsStage = new Stage();
    	 
        
        // constructing our scene
        URL url = getClass().getResource("/FXML/ViewSubmition.fxml");
        AnchorPane pane = FXMLLoader.load( url );
        Scene scene = new Scene( pane );
        
        // setting the stage
        ViewAssignmentsStage.setScene( scene );
        ViewAssignmentsStage.setTitle( "Submition Info" );
        ViewAssignmentsStage.show();
    }




	/**
	 * Class to present Student Info
	 * @author Or Cohen
	 *
	 */
	 public static class StudentInfo{

		    /** getters & setters */
	        private StringProperty id;
	        public void setId(String value) { idProperty().set(value); }
	        public String getId() { return idProperty().get(); }
	        public StringProperty idProperty() { 
	            if (id == null) id = new SimpleStringProperty(this, "id");
	            return id; 
	        } 
	        
	    	private StringProperty name;
	        public void setName(String value) { nameProperty().set(value); }
	        public String getName() { return nameProperty().get(); }
	        public StringProperty nameProperty() { 
	            if (name == null) name = new SimpleStringProperty(this, "name");
	            return name; 
	        }
	        
	        private StringProperty address;
	        public void setAddress(String value) { addressProperty().set(value); }
	        public String getAddress() { return addressProperty().get(); }
	        public StringProperty addressProperty() { 
	            if (address == null) address = new SimpleStringProperty(this, "address");
	            return address; 
	        }
	        
	        private StringProperty mail;
	        public void setMail(String value) { mailProperty().set(value); }
	        public String getMail() { return mailProperty().get(); }
	        public StringProperty mailProperty() { 
	            if (mail == null) mail = new SimpleStringProperty(this, "mail");
	            return mail; 
	        }
	        
	        private StringProperty telephone;
	        public void setTelephone(String value) { telephoneProperty().set(value); }
	        public String getTelephone() { return telephoneProperty().get(); }
	        public StringProperty telephoneProperty() { 
	            if (telephone == null) telephone = new SimpleStringProperty(this, "telephone");
	            return telephone; 
	        } 
	        
/** constructor */
	        public StudentInfo(String id, String name, String mail, String address, String telephone){
	        	setId(id);
	        	setName(name);
	        	setMail(mail);
	        	setAddress(address);
	        	setTelephone(telephone);
	        }
	    }
}