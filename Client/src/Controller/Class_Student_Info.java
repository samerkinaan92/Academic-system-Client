package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controller.ParentViewInfoController.ParentInfo;
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

public class Class_Student_Info implements Initializable {
	//final ObservableList<String> classes = FXCollections.observableArrayList();
	private final ObservableList<StudentInfo> Studentsdata = FXCollections.observableArrayList();
	final ObservableList<String> classCoursesData = FXCollections.observableArrayList();
	 final ObservableList<String> classNames = FXCollections.observableArrayList(); 
	
	public static int flag;
	 @FXML
    private AnchorPane Select;
	 

	    @FXML
	    private Button ShowAllParents;

	    

    @FXML
    private TableView<StudentInfo> StudentsTableView;

    @FXML
    private TableColumn<StudentInfo, String> IDCollum;

    @FXML
    private TableColumn<StudentInfo, String> NameCollum;

    @FXML
    private TableColumn<StudentInfo, String> MailCollum;

    @FXML
    private TableColumn<StudentInfo, String> AddressNum;

    @FXML
    private TableColumn<StudentInfo, String> PhoneColum;
/*
    @FXML
    private TableColumn<StudentInfo, String> AverageColum;
*/

    @FXML
    private Button ShowParentsBtn;

   // ArrayList<claSS> classlist = claSS.getClasses();

    @FXML
    private ComboBox<String> ClassChooser;

    @FXML
    private Button SelectBtn;

    @FXML
    private ListView<String> CoursesOfClassVL;

    @FXML
    private ComboBox<String> SemesterCB;

    ArrayList<Semester> semesterArr = Semester.getSemesters();
    ArrayList<claSS> ClassArr = claSS.getClasses();
    public static String ChoosedClass = new String();
    public static String ChoosedSemester = new String();
    public static String SelectedStudent = new String();
    

    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
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
		//AverageColum.setCellValueFactory(new PropertyValueFactory<StudentInfo, String>("Average"));
		PhoneColum.setCellValueFactory(new PropertyValueFactory<StudentInfo, String>("telephone"));
		AddressNum.setCellValueFactory(new PropertyValueFactory<StudentInfo, String>("address"));
		
    }
    
	
    @FXML
    void ClichOnClass(ActionEvent event) throws IOException  {
    	SemesterCB.setDisable(false);
    	classCoursesData.clear();
    	//classNames.clear();
    	

    	ChoosedClass = ClassChooser.getSelectionModel().getSelectedItem();
    	//System.out.println(ChoosedClass);
    }

    @FXML
    void ClickOnSemester(ActionEvent event) throws IOException  {
    	classCoursesData.clear();
    	ChoosedSemester = SemesterCB.getSelectionModel().getSelectedItem();
      	 int j = ChoosedSemester.indexOf(" "); // 4

    	   String Year = ChoosedSemester.substring(0, j); // from 0 to 3

    	   String Season = ChoosedSemester.substring(j+1); // after the space to the rest of the line
    	 ChoosedSemester = Semester.getSemesterID(Year, Season).get(0);
    	// System.out.println(Year+" "+Season);

    }
    

    @FXML
    void ClickSelect(ActionEvent event)  throws IOException {
    	classCoursesData.clear();
    	Studentsdata.clear();
	 	ArrayList<Course> courseArray = Course.getCoursesofClass(ChoosedClass, ChoosedSemester);
	 	ArrayList<String> courseList = new ArrayList<String>();
	 	for (int i = 0 ; i < courseArray.size() ; i++)
	 	{
	 	courseList.add(courseArray.get(i).getName());
	 	classCoursesData.add(courseList.get(i));
	 	//System.out.println(courseList.get(i));
	 	}
	 	CoursesOfClassVL.setItems(classCoursesData);
	 	ArrayList<Student> studentsList = claSS.getStudentsOfClass(ChoosedClass);
	 	ShowParentsBtn.setDisable(false);
	 	for (int i = 0 ; i < studentsList.size() ; i++)
		{
	 		StudentInfo temp = new StudentInfo(studentsList.get(i).getID(),
	 										   studentsList.get(i).getName(),
	 										   studentsList.get(i).getMail(),
	 										   studentsList.get(i).getAddress(),
	 										   studentsList.get(i).getPhoneNum());

	 		Studentsdata.add(temp);
		}
	 	StudentsTableView.setItems((FXCollections.observableArrayList(Studentsdata)));//.setItems(FXCollections.observableArrayList(Studentsdata));
    }
    
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

	 




	
	 public static class StudentInfo{

		    
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
	        
	      /*  
	        private StringProperty Average;
	        public void setAverage(String value) { AverageProperty().set(value); }
	        public String getAverage() { return AverageProperty().get(); }
	        public StringProperty AverageProperty() { 
	            if (Average == null) Average = new SimpleStringProperty(this, "Average");
	            return Average; 
	        }
	        */
	        public StudentInfo(String id, String name, String mail, String address, String telephone){
	        	setId(id);
	        	setName(name);
	        	//setAverage(Average);
	        	setMail(mail);
	        	setAddress(address);
	        	setTelephone(telephone);
	        }
	    }
}