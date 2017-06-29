package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controller.ShowAssignmentController.AssignmentInfo;
import Entity.Assignment;
import Entity.Course;
import Entity.Student;
import Entity.SubmittedAssignment;
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
 * This is the controller class for: "ViewSubmition.fxml"
 * @author Or Cohen
 *
 */
public class ViewSubmitionController implements Initializable{
	/** List of all the submittions of a student */
	private final ObservableList<SubmitionInfo> SubData = FXCollections.observableArrayList();
	/** define AnchorPane to show the window content */
	@FXML
    private AnchorPane AssignmentView;
	/** Table of all the submittions of the student */
    @FXML
    private TableView<SubmitionInfo> SubmittionTable;
    /** Colum in "SubmittionTable" to view submittion Id*/
    @FXML
    private TableColumn<SubmitionInfo, String> IDColum;
    /** Colum in "SubmittionTable" to view submittion Name*/
    @FXML
    private TableColumn<SubmitionInfo, String> NameColum;
    /** Colum in "SubmittionTable" to view submittion Assignment publish Date*/
    @FXML
    private TableColumn<SubmitionInfo, String> PublishDateColum;
    /** Colum in "SubmittionTable" to view submittion Assignment Deadline Date*/
    @FXML
    private TableColumn<SubmitionInfo, String> DeadlineColum;
    /** Colum in "SubmittionTable" to view submittion course Name*/
    @FXML
    private TableColumn<SubmitionInfo, String> CourseCol;
    /** Colum in "SubmittionTable" to view if submittion late*/
    @FXML
    private TableColumn<SubmitionInfo, String> isLateColum;
    /** Colum in "SubmittionTable" to view submittion grade*/
    @FXML
    private TableColumn<SubmitionInfo, String> GradeColum;
    /** Colum in "SubmittionTable" to view submittion File Path*/
    @FXML
    private TableColumn<SubmitionInfo, String> PathColum;
    /** Define button to download selected submittion */
    @FXML
    private Button DownloadBtn;
    
    /** show alrets for download problems */
      Alert infoMsg = new Alert(AlertType.INFORMATION);
	  Alert errMsg = new Alert(AlertType.ERROR);
	  Alert warMsg = new Alert(AlertType.WARNING);
   
	  /** ArrayList of Submittions of student*/
    ArrayList<SubmittedAssignment> SubmitionArr = new ArrayList<SubmittedAssignment>();
	
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {// Initialize window.
		SubData.clear();
		
		IDColum.setCellValueFactory(new PropertyValueFactory<SubmitionInfo, String>("id"));
		NameColum.setCellValueFactory(new PropertyValueFactory<SubmitionInfo, String>("name"));
		PublishDateColum.setCellValueFactory(new PropertyValueFactory<SubmitionInfo, String>("PublishDate"));
		DeadlineColum.setCellValueFactory(new PropertyValueFactory<SubmitionInfo, String>("Deadline"));
		CourseCol.setCellValueFactory(new PropertyValueFactory<SubmitionInfo, String>("Course"));
		isLateColum.setCellValueFactory(new PropertyValueFactory<SubmitionInfo, String>("isLate"));
		GradeColum.setCellValueFactory(new PropertyValueFactory<SubmitionInfo, String>("Grade"));
		PathColum.setCellValueFactory(new PropertyValueFactory<SubmitionInfo, String>("FilePath"));
		
		SubmitionArr = SubmittedAssignment.getSubmitionsOfStudent( Class_Student_Info.SelectedStudent);
		 for (int j = 0 ; j < SubmitionArr.size() ; j++)
		    {
			
			   SubmitionInfo temp = new SubmitionInfo(
					   Integer.toString(SubmitionArr.get(j).getAssignmentID()),
					   SubmitionArr.get(j).getAssignmentName(),
					   SubmitionArr.get(j).getPublishDateAsString(),
					   SubmitionArr.get(j).getDeadLineAsString(),
					   Course.getCourseName(Integer.toString(SubmitionArr.get(j).getCourseID())),
					   SubmitionArr.get(j).getisLate(),
					   SubmitionArr.get(j).getGrade(),
					   SubmitionArr.get(j).getFilePath());

			   SubData.add(temp);
																							
		    }
		
		  
		   SubmittionTable.setItems(FXCollections.observableArrayList(SubData));
	}
		   /**
		    * handle click on download button to download selected submitin
		    * @param event
		    */
    		@FXML
		    void ClickDownload(ActionEvent event) {

		    	 //Download Assignment from server.
				  
				  Assignment sub = null;
				  String selected = SubmittionTable.getSelectionModel().selectedItemProperty().get().getId();
				 		  
				  if (selected == null){
					  warMsg.setContentText("No Assignment Was Selected!");
					  warMsg.showAndWait();
					  return;
				  }
				
				  
				  
				  for (int i = 0; i < SubmitionArr.size(); i++){
					  if (SubmitionArr.get(i).getAssignmentID() == Integer.parseInt(selected)){
						  sub = SubmitionArr.get(i);
						  break;
					  }
				  }
				  
				  if (sub == null){
					  warMsg.setContentText("Assignment Was Not Found In Data Base!");
					  warMsg.showAndWait();
					  return;
				  }
				  
				  String p = sub.getFilePath();
				  
																						  /*
																						  if (!Files.exists(Paths.get(p))){
																							  warMsg.setContentText("File Is Not In Server File Path!");
																							  warMsg.showAndWait();
																							  return;
																						  }
																						  */
				  
				  byte[] file = SubmittedAssignment.getFile(p);
				  
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
		        type = type.substring(type.lastIndexOf('.'), type.length());
		        fileChooser.setInitialFileName(fileName[fileName.length - 1] + type);
		        File pa = fileChooser.showSaveDialog(SubmittionTable.getScene().getWindow());
		        
		        
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
    		/**
    		 * Class for presenting submitions Info
    		 * @author Or Cohen
    		 *
    		 */
	 public static class SubmitionInfo{
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
	        private StringProperty isLate;
	        public void setisLate(String value) { isLateProperty().set(value); }
	        public String getisLate() { return isLateProperty().get(); }
	        public StringProperty isLateProperty() { 
	            if (isLate == null) isLate = new SimpleStringProperty(this, "isLate");
	            return isLate; 
	        }
	        private StringProperty Grade;
	        public void setGrade(String value) { GradeProperty().set(value); }
	        public String getGrade() { return GradeProperty().get(); }
	        public StringProperty GradeProperty() { 
	            if (Grade == null) Grade = new SimpleStringProperty(this, "Grade");
	            return Grade; 
	        }
			 
		     private StringProperty FilePath;
		     public void setFilePath(String value) { FilePathProperty().set(value); }
		     public String getFilePath() { return FilePathProperty().get(); }
		     public StringProperty FilePathProperty() { 
	         if (FilePath == null) FilePath = new SimpleStringProperty(this, "FilePath");
	         return FilePath; 
     }
		     /** constructor of Class */
	        public SubmitionInfo(String id, String name, String PublishDate, String Deadline, String Course, String isLate, String Grade, String FilePath){
	        	setId(id);
	        	setName(name);
	        	setPublishDate(PublishDate);
	        	setDeadline(Deadline);
	        	setCourse(Course);
	        	setisLate(isLate);
	        	setGrade(Grade);
	        	setFilePath(FilePath);
	        }

	 }
}
