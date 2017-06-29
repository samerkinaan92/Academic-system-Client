package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controller.ParentViewInfoController.ParentInfo;
import Controller.ViewTeacherController.techertInfo;
import Entity.Assignment;
import Entity.Course;
import Entity.EvaluationForm;
import Entity.Student;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
/**
 * This is the controller class for: "ShowAssignment.fxml"
 * @author Or Cohen
 *
 */
public class ShowAssignmentController implements Initializable {
/** List of all the assignments of a course */
	private final ObservableList<AssignmentInfo> Assdata = FXCollections.observableArrayList();
   /** define AnchorPane to show the window content */ 
	@FXML
    private AnchorPane AssignmentView;
    /** Define button to download selected assignment */
    @FXML
    private Button DownloadBtn;

    /** array of all the assignments of the course */
    private ArrayList<Assignment> assignmentArr;
    /** Table of all the assignments of the course */
    @FXML
    private TableView<AssignmentInfo> AssignmentTable;
    /** Colum in "AssignmentTable" to view Assignment name*/
    @FXML
    private TableColumn<AssignmentInfo, String> IDColum;
    /** Colum in "AssignmentTable" to view Assignment name*/
    @FXML
    private TableColumn<AssignmentInfo, String> NameColum;
    /** Colum in "AssignmentTable" to view Assignment publish date*/
    @FXML
    private TableColumn<AssignmentInfo, String> PublishDateColum;
    /** Colum in "AssignmentTable" to view Assignment deadline date*/
    @FXML
    private TableColumn<AssignmentInfo, String> DeadlineColum;
    /** Colum in "AssignmentTable" to view Assignment course name*/
    @FXML
    private TableColumn<AssignmentInfo, String> CourseCol;
    
    /** show alrets for download problems */
	  Alert infoMsg = new Alert(AlertType.INFORMATION);
	  Alert errMsg = new Alert(AlertType.ERROR);
	  Alert warMsg = new Alert(AlertType.WARNING);
    	
    
    
    
    
    
    
    
    
    
/**
 * Class for presenting Assignment Info
 * @author Or Cohen
 *
 */
    public static class AssignmentInfo{

	    /** getters & setters */
        private StringProperty id;
        public void setId(String value) { idProperty().set(value); }
        public String getId() { return idProperty().get(); }
        public StringProperty idProperty() { 
            if (id == null) id = new SimpleStringProperty(this, "ID");
            return id; 
        } 
        
    	private StringProperty name;
        public void setName(String value) { nameProperty().set(value); }
        public String getName() { return nameProperty().get(); }
        public StringProperty nameProperty() { 
            if (name == null) name = new SimpleStringProperty(this, "Assignment Name");
            return name; 
        }
        
        private StringProperty PublishDate;
        public void setPublishDate(String value) { PublishDateProperty().set(value); }
        public String getPublishDate() { return PublishDateProperty().get(); }
        public StringProperty PublishDateProperty() { 
            if (PublishDate == null) PublishDate = new SimpleStringProperty(this, "Publish Date");
            return PublishDate; 
        } 
        
        private StringProperty Deadline;
        public void setDeadline(String value) { DeadlineProperty().set(value); }
        public String getDeadline() { return DeadlineProperty().get(); }
        public StringProperty DeadlineProperty() { 
            if (Deadline == null) Deadline = new SimpleStringProperty(this, "Deadline");
            return Deadline; 
        }
        private StringProperty Course;
        public void setCourse(String value) { CourseProperty().set(value); }
        public String getCourse() { return CourseProperty().get(); }
        public StringProperty CourseProperty() { 
            if (Course == null) Course = new SimpleStringProperty(this, "Course");
            return Course; 
        }
        /** constructor of Class */
        public AssignmentInfo(String id, String name, String PublishDate, String Deadline, String Course){
        	setId(id);
        	setName(name);
        	setPublishDate(PublishDate);
        	setDeadline(Deadline);
        	setCourse(Course);
        }

 }
/** ArrayList of Assugnments by course and semester */
    ArrayList<Assignment> AssginmentArr = Assignment.getAssignments(ViewCoursesDataController.selectedCourseID, ViewCoursesDataController.semesterID);



 





	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {// Initialize window.
		// TODO Auto-generated method stub
		Assdata.clear();
		IDColum.setCellValueFactory(new PropertyValueFactory<AssignmentInfo, String>("id"));
		NameColum.setCellValueFactory(new PropertyValueFactory<AssignmentInfo, String>("name"));
		PublishDateColum.setCellValueFactory(new PropertyValueFactory<AssignmentInfo, String>("PublishDate"));
		DeadlineColum.setCellValueFactory(new PropertyValueFactory<AssignmentInfo, String>("Deadline"));
		CourseCol.setCellValueFactory(new PropertyValueFactory<AssignmentInfo, String>("Course"));

		assignmentArr = Student.getAssignments(Integer.parseInt(ViewCoursesDataController.selectedCourseID));
		
		   for (int i = 0 ; i < AssginmentArr.size() ; i++)
		    {
			   AssignmentInfo temp = new AssignmentInfo(Integer.toString(AssginmentArr.get(i).getAssignmentID()),
					   												     AssginmentArr.get(i).getAssignmentName(),
					   												     AssginmentArr.get(i).getPublishDateAsString(),
					   												     AssginmentArr.get(i).getDeadLineAsString(),
					   									Course.getCourseName(Integer.toString(AssginmentArr.get(i).getCourseID())) );
			
			   Assdata.add(temp);
					   																							
		    }
		   AssignmentTable.setItems(FXCollections.observableArrayList(Assdata));
	}
	/**
	 * handle click on download button to download Assignment
	 * @param event
	 */
    @FXML
    void ClickDownload(ActionEvent event) {
    	 //Download Assignment from server.
		  
		  Assignment ass = null;
		  String selected = AssignmentTable.getSelectionModel().selectedItemProperty().get().getId();
		 		  
		  if (selected == null){
			  warMsg.setContentText("No Assignment Was Selected!");
			  warMsg.showAndWait();
			  return;
		  }
		
		  
		  
		  for (int i = 0; i < assignmentArr.size(); i++){
			  if (assignmentArr.get(i).getAssignmentID() == Integer.parseInt(selected)){
				  ass = assignmentArr.get(i);
				  break;
			  }
		  }
		  
		  if (ass == null){
			  warMsg.setContentText("Assignment Was Not Found In Data Base!");
			  warMsg.showAndWait();
			  return;
		  }
		  
		  String p = ass.getFilePath();
		  
																				  /*
																				  if (!Files.exists(Paths.get(p))){
																					  warMsg.setContentText("File Is Not In Server File Path!");
																					  warMsg.showAndWait();
																					  return;
																				  }
																				  */
		  
		  byte[] file = Assignment.getFile(p);
		  
		  if (file == null){
			  errMsg.setContentText("Failed Getting File Stream From Server!");
      	  errMsg.showAndWait();
      	  return;
		  }
		  
		  FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Assignment");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Acrobat Reader Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Word Documents", "*.docx")
            );
        String[] fileName = p.split("/");
        String type = fileName[fileName.length - 1];
        type = type.substring(type.indexOf('.'), type.length());
        fileChooser.setInitialFileName(fileName[fileName.length - 1] + type);
        File pa = fileChooser.showSaveDialog(AssignmentTable.getScene().getWindow());
        
        
        if (pa == null){
      	  warMsg.setContentText("Save Path Was Not Selected!");
			  warMsg.showAndWait();
      	  return;
        }
        
        Path savePath = Paths.get(pa.getAbsolutePath());
        FileOutputStream stream;
        
        try {
      	  stream = new FileOutputStream(savePath.toString());
      	  stream.write(file);
      	  stream.close();
      	  infoMsg.setContentText("Assignment Saved");
      	  infoMsg.showAndWait();
        }
        catch (IOException ex) {
      	  ex.printStackTrace();
      	  errMsg.setContentText("Download Failed!");
      	  errMsg.showAndWait();
        } 
	  
    }

}