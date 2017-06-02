package Controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import Entity.Assignment;
import Entity.Course;
import Entity.Student;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class StudentSubmitAssignmentController implements Initializable {
	
	  @FXML
	  private ListView<String> courseListView;

	  @FXML
	  private TextField courseSearch;

	  @FXML
	  private ListView<String> assignmentListView;

	  @FXML
	  private TextField assignmentSearch;
	  
	  ArrayList<Course> courseArr;
	  ArrayList<Assignment> assignmentArr;
	  
	  ArrayList<String> organizedCourseList;
	  ArrayList<String> organizedAssignmentList;
	  
	  Student student;
	  File file;


	  //-------------------------------------------------------------------------------------------------------------------
	  
	  private ArrayList<String> getCourseList(ArrayList<Course> courseArr){ // Get courses names & ID's for list.
	    	
			ArrayList<String> temp = new ArrayList<String>();
			for (int i = 0; i < courseArr.size(); i++)
				temp.add(courseArr.get(i).getName() + " (" + courseArr.get(i).getCourseID() + ")");
			
			return temp;	
		}
	  
	  
	  private ArrayList<String> getAssignmentList(ArrayList<Assignment> assignmentArr){ // Get courses names & ID's for list.
	    	
			ArrayList<String> temp = new ArrayList<String>();
			for (int i = 0; i < assignmentArr.size(); i++)
				temp.add(assignmentArr.get(i).getAssignmentName() + " (" + assignmentArr.get(i).getAssignmentID() + ")");
			
			return temp;	
		}
	 	  
	  //-------------------------------------------------------------------------------------------------------------------
	  
	  public void CourseSearch(ActionEvent e){
		  ArrayList<String> temp = new  ArrayList<String>();
	    	
		  for (int i = 0; i < organizedCourseList.size(); i++)
			  if (organizedCourseList.get(i).toLowerCase().contains(courseSearch.getText().toLowerCase()))
				  temp.add(organizedCourseList.get(i));
	    	
	    	 
		  courseListView.setItems(FXCollections.observableArrayList(temp));
	  }
	 
	  
	  
	 

	  public void AssignmentSearch(ActionEvent e){
	  
		  ArrayList<String> temp = new  ArrayList<String>();
	    	
		  for (int i = 0; i < organizedAssignmentList.size(); i++)
			  if (organizedAssignmentList.get(i).toLowerCase().contains(assignmentSearch.getText().toLowerCase()))
				  temp.add(organizedAssignmentList.get(i));
	    	
	    	 
		  assignmentListView.setItems(FXCollections.observableArrayList(temp));
	  }

	  public void chooseAssignment(ActionEvent e){
	  
	  }
	  
	  public void submit(ActionEvent e){
		  
		  
		  
		  
		  
	  }
	  
	  public void browse(ActionEvent e){
		  FileChooser chooser = new FileChooser();
		  chooser.setTitle("Upload ");
		  file = chooser.showOpenDialog(new Stage());
		  
		  courseSearch.setText(file.getAbsolutePath());
	  }

	
	  //-------------------------------------------------------------------------------------------------------------------
	  
	  @Override
	  public void initialize(URL arg0, ResourceBundle arg1) {
		  
		  courseArr = Student.getCourse();
		  organizedCourseList = getCourseList(courseArr);
		  Collections.sort(organizedCourseList);
		  courseListView.setItems(FXCollections.observableArrayList(organizedCourseList));
		  
		  
		  courseListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			    	int courseID = -1;
			    	for (int i = 0; i < organizedCourseList.size(); i++)
			    		if (organizedCourseList.get(i).equals(courseListView.getSelectionModel().getSelectedItem().toString()))
			    			courseID = Integer.parseInt
			    			(courseListView.getSelectionModel().getSelectedItem().toString().substring
			    			(courseListView.getSelectionModel().getSelectedItem().toString().indexOf('(') + 1
			    			, courseListView.getSelectionModel().getSelectedItem().toString().indexOf(')')));
			    				
			    	assignmentArr = Student.getAssignments(courseID);
			    	organizedAssignmentList = getAssignmentList(assignmentArr);
			    	Collections.sort(organizedAssignmentList);
			    	assignmentListView.setItems(FXCollections.observableArrayList(organizedAssignmentList));
			    	
			    }
			});
		  
		  assignmentListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			    @Override
			    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			        // Your action here
			        System.out.println("Selected item: " + newValue);
			    }
			});
		  
		  
			
	  }
		  
	  
	  
}