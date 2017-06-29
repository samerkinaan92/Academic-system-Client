package Controller;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.awt.Button;
//import Controller.StudentExceptionalRequest.StudRequestInfo;
import Entity.Course;
import Entity.Semester;
import Entity.Teacher;
//import Entity.claSS;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * This is the controller class for: "ViewTeacherInfo.fxml"
 * @author Or Cohen
 * */
public class ViewTeacherController implements Initializable{
	/** List to be viewd on teachers table */
	private final ObservableList<techertInfo> data = FXCollections.observableArrayList();
	/** List to be viewd on course view list */
	private final ObservableList<String> dataclass = FXCollections.observableArrayList();
	/** List to be viewd on teaching unit view list */
	private final ObservableList<String> dataTU = FXCollections.observableArrayList();
	
	/** Button to show teachers info */
    @FXML
    private Button SelectBtn;
    /** Table view to be show teachers Info*/
    @FXML
    private TableView<techertInfo> TeacherTableView;
    /** Colum in TableView to view ID of teacher */
    @FXML
    private TableColumn<techertInfo, String> IdCollum;
    /** Colum in TableView to view Name of teacher */
    @FXML
    private TableColumn<techertInfo, String> nameCollum;
    /** Colum in TableView to email Name of teacher */
    @FXML
    private TableColumn<techertInfo, String> mailCollum;
    /** Colum in TableView to view Max Work Hours of teacher */
    @FXML
    private TableColumn<techertInfo, String> maxWorkHoursCollum;
    /** Colum in TableView to view Telephone number of teacher */
    @FXML
    private TableColumn<techertInfo, String> telephoneCollum;
    /** Colum in TableView to view address of teacher */
    @FXML
    private TableColumn<techertInfo, String> addressCollum;
    
    /** combobox to choose semester */
    @FXML
    private ComboBox<String> semesterCB;
    /** List View to view teaching unit */
    @FXML
    private ListView<String> TUView;

    /** Array for all the semesters */
    ArrayList<Semester> semesterArr = Semester.getSemesters();
    /** String to hold choosed semester */
    String ChoosedSemester = new String();
    /** List View to view courses of teacher */
    @FXML
    private ListView<String> CoursesOfTeacherView;

    
	@Override
	public void initialize(URL location, ResourceBundle resources) {// Initialize window.
		TeacherTableView.setDisable(true);
		ArrayList<String> semesterIdArr = new ArrayList<String>();
	    for (int i = 0 ; i < semesterArr.size() ; i++)
	    	semesterIdArr.add(Semester.getSemesters().get(i).getYear()+" "+Semester.getSemesters().get(i).getSeason());
	    	//	semesterIdArr.add(Integer.toString(semesterArr.get(i).getId()));
	    semesterIdArr.add("<< All Semesters >>");

		    final ObservableList<String> semesterId = FXCollections.observableArrayList(semesterIdArr); 
		    semesterCB.setItems(semesterId);
		
		//ArrayList<String> TeachersOfClass = new ArrayList<String>();
		
		IdCollum.setCellValueFactory(new PropertyValueFactory<techertInfo, String>("id"));
		nameCollum.setCellValueFactory(new PropertyValueFactory<techertInfo, String>("name"));
		mailCollum.setCellValueFactory(new PropertyValueFactory<techertInfo, String>("mail"));
		maxWorkHoursCollum.setCellValueFactory(new PropertyValueFactory<techertInfo, String>("maxWorkHours"));
		telephoneCollum.setCellValueFactory(new PropertyValueFactory<techertInfo, String>("telephone"));
		addressCollum.setCellValueFactory(new PropertyValueFactory<techertInfo, String>("address"));
		
		
		

		
	
	}
	/**
	 * select and update the selected semester for the teacher info
	 * @param event
	 */
    @FXML
    void semesterChoose(ActionEvent event) {
    	TeacherTableView.setDisable(false);
    	 data.clear();
    	 ArrayList<Teacher> TeacherArr = new ArrayList<Teacher>();
    	 ChoosedSemester = semesterCB.getSelectionModel().getSelectedItem();
    	// String input = "This is a line of text";

    	   int j = ChoosedSemester.indexOf(" "); // 4

    	   String Year = ChoosedSemester.substring(0, j); // from 0 to 3

    	   String Season = ChoosedSemester.substring(j+1); // after the space to the rest of the line
	    	if (ChoosedSemester.equals("<< All Semesters >>"))
	    		TeacherArr = Teacher.getTeachers();
	    		else{
	    				ChoosedSemester = Semester.getSemesterID(Year, Season).get(0);
	    			TeacherArr = Teacher.getTeachersInSemester(ChoosedSemester);
	    			}
	    	for (int i = 0 ; i < TeacherArr.size() ; i++)
			{
				techertInfo temp = new techertInfo(TeacherArr.get(i).getID(),
												   TeacherArr.get(i).getName(),
												   Integer.toString(TeacherArr.get(i).getMaxWorkHours()),
												   TeacherArr.get(i).getEmail(),
												   TeacherArr.get(i).getAddress(),
												   TeacherArr.get(i).getPhone());
				data.add(temp);
			}
			TeacherTableView.setItems(FXCollections.observableArrayList(data));
			if(TeacherTableView.getSelectionModel().isEmpty())
				SelectBtn.setEnabled(false);
    }
    		
    /**
     * button to show information of teachers in chosen semester
     * @param event
     * @throws IOException
     */
	 @FXML
    void ClickOnSelect(ActionEvent event) throws IOException { 
		 dataclass.clear();
		 dataTU.clear();
		
			//--------------------------------------------------------------------------------------------------------------------
		 
    	techertInfo teachInfo = TeacherTableView.getSelectionModel().selectedItemProperty().get();
    	String selectedTeacher = teachInfo.getId();
    	ArrayList<String> coursesIDArr = new ArrayList<String>();
    	ArrayList<String> coursesArr = new ArrayList<String>();
    	ArrayList<String> semester = new ArrayList<String>();
    	ArrayList<String> str = new ArrayList<String>();
    	coursesIDArr = Teacher.getCoursesOfTeacher(selectedTeacher);
    	ArrayList<String> cclassArr = new ArrayList<String>();
    	
    	//getSemesterOfCoursesOfTeacher
    	if(ChoosedSemester == "<< All Semesters >>") {
    		coursesArr = Teacher.getCoursesOfTeacher(selectedTeacher);
    		cclassArr = Teacher.getClassesOfTeacher(selectedTeacher);
    		semester = Teacher.getSemesterOfCoursesOfTeacher(selectedTeacher);
        	for (int i = 0 ; i < coursesArr.size() ; i++)
        	{
        		str.add(Course.getCourseName(coursesArr.get(i))+" in Class - "+cclassArr.get(i)+" On Semester - "+semester.get(i));
        		dataclass.add(str.get(i));
        	}
    	}
    	else
    	{

        	coursesArr = Teacher.getCoursesOfTeacherInSemester(selectedTeacher , ChoosedSemester );
        	cclassArr = Teacher.getClassesOfTeacherInSemester(selectedTeacher, ChoosedSemester);
        	for (int i = 0 ; i < coursesArr.size() ; i++)
        	{
        		str.add(Course.getCourseName(coursesArr.get(i))+",    in Class - "+cclassArr.get(i)+",    On Semester - "+ChoosedSemester);
        		dataclass.add(str.get(i));
        	}
        	
    	}

  
    	CoursesOfTeacherView.setItems(FXCollections.observableArrayList(dataclass));
    	
    	//-------------------------------------------------------------------------------------------
    	
    	
    	//-------------------------------------------------------------------------------------------
    	ArrayList<String> TU = new ArrayList<String>();
    	TU = Teacher.getTUOfTeacher(selectedTeacher);
    	for (int i = 0 ; i < TU.size() ; i++)
    		dataTU.add(TU.get(i));
    	TUView.setItems(FXCollections.observableArrayList(dataTU));
	 }
    
    /**
     * 
     * @author Or Cohen
     * class to represent teacher info
     */
	 public static class techertInfo{

	    /** gettters and setters */
	        private StringProperty id;
	        public void setId(String value) { idProperty().set(value); }
	        public String getId() { return idProperty().get(); }
	        public StringProperty idProperty() { 
	            if (id == null) id = new SimpleStringProperty(this, "id");
	            return id; 
	        } 
	       /** gettters and setters */
	    	private StringProperty name;
	        public void setName(String value) { nameProperty().set(value); }
	        public String getName() { return nameProperty().get(); }
	        public StringProperty nameProperty() { 
	            if (name == null) name = new SimpleStringProperty(this, "name");
	            return name; 
	        }
	        /** gettters and setters */   
	        private StringProperty address;
	        public void setAddress(String value) { addressProperty().set(value); }
	        public String getAddress() { return addressProperty().get(); }
	        public StringProperty addressProperty() { 
	            if (address == null) address = new SimpleStringProperty(this, "address");
	            return address; 
	        } 
	        /** gettters and setters */    
	        private StringProperty maxWorkHours;
	        public void setMaxWorkHours(String value) { maxWorkHoursProperty().set(value); }
	        public String getMaxWorkHours() { return maxWorkHoursProperty().get(); }
	        public StringProperty maxWorkHoursProperty() { 
	            if (maxWorkHours == null) maxWorkHours = new SimpleStringProperty(this, "Max Work Hours");
	            return maxWorkHours; 
	        }
	        /** gettters and setters */   
	        private StringProperty mail;
	        public void setMail(String value) { mailProperty().set(value); }
	        public String getMail() { return mailProperty().get(); }
	        public StringProperty mailProperty() { 
	            if (mail == null) mail = new SimpleStringProperty(this, "mail");
	            return mail; 
	        }
	        /** gettters and setters */   
	        private StringProperty telephone;
	        public void setTelephone(String value) { telephoneProperty().set(value); }
	        public String getTelephone() { return telephoneProperty().get(); }
	        public StringProperty telephoneProperty() { 
	            if (telephone == null) telephone = new SimpleStringProperty(this, "telephone");
	            return telephone; 
	        } 
	      /** contructor */  
	        public techertInfo(String id, String teachertName, String maxWH, String mail, String address, String telephone){
	        	setId(id);
	        	setName(teachertName);
	        	setMaxWorkHours(maxWH);
	        	setMail(mail);
	        	setAddress(address);
	        	setTelephone(telephone);
	        }
	    }
	

	
	

	

}