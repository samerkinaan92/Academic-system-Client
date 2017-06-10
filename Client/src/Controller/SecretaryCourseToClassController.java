package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Entity.Course;
import Entity.Semester;
import Entity.Teacher;
import Entity.TeachingUnit;
import Entity.claSS;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class SecretaryCourseToClassController implements Initializable {
	
	@FXML
	private ComboBox<String> classChooser;
	
	@FXML
	private ListView<String> availableCourses;
	
	@FXML
    private ListView<String> takenCourses;
	
	@FXML
    private Label guiMsg;
	
	@FXML
	private ComboBox<String> semesterChooser;
	
	@FXML
    private ComboBox<String> teacherChooser;
	
	@FXML
    private Button attachBtn;
	
	@FXML
    private Button removeBtn;
	
	@FXML
    private Button submitBtn;
	
	
	ArrayList<claSS> classArr;
	
	ArrayList<Course> course;
	
	ArrayList<Semester> semesterArr;
	
	ArrayList<Teacher> teacherArr;
	 
	ArrayList<String> organizedClassList;
	
	ArrayList<String> organizedSemesterList;
	
	ArrayList<String> allCourses = null;
	
	ArrayList<String> taken = null;
	
	ArrayList<String> available = null;
	
	ArrayList<String> added = null;
	
	ArrayList<String> removed = null;
	
	//-------------------------------------------------------------------------------------------------------------
	
	private ArrayList<String> getSemesterList(ArrayList<Semester> semsterArr){ // Get semesters year & season.
		 
		 ArrayList<String> temp = new ArrayList<String>();
			for (int i = 0; i < semsterArr.size(); i++)
				temp.add(semsterArr.get(i).getYear() + " (" + semsterArr.get(i).getSeason() + ")");
			
			return temp;	
	 }
	
	private ArrayList<String> getClassList(ArrayList<claSS> classArr){ // Get classes names.
		 
		 ArrayList<String> temp = new ArrayList<String>();
			for (int i = 0; i < classArr.size(); i++)
				temp.add(classArr.get(i).getClassName());
			
			return temp;	
	 }
	
	private ArrayList<String> getCourseList(ArrayList<Course> courseArr){ // Get courses names & ID's for list.
	    	
		ArrayList<String> temp = new ArrayList<String>();
			for (int i = 0; i < courseArr.size(); i++)
				temp.add(courseArr.get(i).getName() + " (" + courseArr.get(i).getCourseID() + ")");
			
			return temp;	
		}
	  
	@SuppressWarnings("unchecked")
	private ArrayList<String> getTaken(String clas, ArrayList<String> courses){ // Get taken courses for class.
		  
		  HashMap <String,String> msgServer = new HashMap <String,String>();
		  msgServer.put("msgType", "select");
		  msgServer.put("query", "Select c.CourseID,c.CourseName From class_course cc, course c WHERE cc.ClassName = '" + clas + "'" +
				  "AND cc.CourseID = c.CourseID");
		  
		  try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					System.out.println("Server fatal error!");
				}
			synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}}
			ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
			
			if (result.size() > 0){
				ArrayList<String> temp = new ArrayList<String>();
				for (int i = 0; i < result.size(); i+=2)
					temp.add(result.get(i+1) + " (" + result.get(i) + ")");
				return temp;
			}
			else
				return null;
	  }
	  
	private ArrayList<String> getAvailable(ArrayList<String> courses, ArrayList<String> taken){ // Get available courses for class.
		
		  boolean flag = true;
		  ArrayList<String> temp = new ArrayList<String>();
		  
		  if (taken != null){
			  for (int i = 0; i < courses.size(); i++, flag = true){
				 for(int j = 0; j < taken.size(); j++)
					 if (courses.get(i).equals(taken.get(j))){
						 flag = false;
						 break;
					 }
				 if (flag)
					 temp.add(courses.get(i));
			  }
		  }
		  
		  else{
			  for (int i = 0; i < courses.size(); i++)
				  temp.add(courses.get(i));
		  }
		  
		  if (temp.size() > 0)
			  return temp;
		  else 
			  return null;  
	  }
 	
	//-------------------------------------------------------------------------------------------------------------

	public void chooseTeacher(ActionEvent e){ // Check if teacher can teach course.
		  
		  if (teacherChooser.getSelectionModel().getSelectedItem() == null)
			  return;
		  
		  String selectedCourse = availableCourses.getSelectionModel().getSelectedItem();
		  String selectedTeacherID = teacherChooser.getSelectionModel().getSelectedItem();
		  selectedTeacherID = selectedTeacherID.substring(selectedTeacherID.indexOf('(') + 1, selectedTeacherID.indexOf(')'));
		  int weaklyHours = 0;
		  int maxHours = 0;
		  int sum = Teacher.getSumOfHours(selectedTeacherID);
		  String courseID = selectedCourse.substring(selectedCourse.indexOf('(') + 1, selectedCourse.indexOf(')'));
		  
		  for (int i = 0; i < course.size(); i++){
			  if (course.get(i).getCourseID() == Integer.parseInt(courseID)){
			  	weaklyHours = course.get(i).getWeeklyHours();
			  	break;
			  }
		  }
		  
		  for (int i = 0; i < teacherArr.size(); i++){
			  if (teacherArr.get(i).getID().equals(selectedTeacherID)){
				  maxHours = teacherArr.get(i).getMaxWorkHours();
				  break;
			  }
		  }
		  if (sum + weaklyHours > maxHours){
			  guiMsg.setText("Teacher Cant be assigned to course: Exceeds max hours limit!");
			  return;
		  }
		  attachBtn.setDisable(false);
	  }
	  
	public void chooseSemester(ActionEvent e){ // Load all classes in chosen semester.
		  
		  int id = 0;
		  
		  for (int i = 0; i < semesterArr.size(); i++)
			  if ((semesterArr.get(i).getYear() + " (" + semesterArr.get(i).getSeason() + ")").equals(semesterChooser.getSelectionModel().getSelectedItem())){
				  id = semesterArr.get(i).getId();
				  break;
			  }
				  
		  classArr = claSS.getClasses();
		  int size = classArr.size()-1;
		  
		  while (0 <= size){
			  if (classArr.get(size).getYear() != id)
				  classArr.remove(size--);
			  else
				  size--;
		  }
		  
		  if (classArr.size() > 0){
			  organizedClassList = getClassList(classArr);
			  Collections.sort(organizedClassList);
		  }
		  else{
			  organizedClassList = new ArrayList<String>();
			  organizedClassList.add("No Class in Semester!");
		  }
		  
		  classChooser.setItems(FXCollections.observableArrayList(organizedClassList));
		  classChooser.setDisable(false);
		  classChooser.getSelectionModel().clearSelection();
		  teacherChooser.setDisable(true);
		  teacherChooser.getSelectionModel().clearSelection();
		  attachBtn.setDisable(true);
		  removeBtn.setDisable(true);
	  }
	  
	public void chooseClass(ActionEvent e){ // Load taken & available lists for chosen class.
		
		taken = null;
		available = null;
		removed = null;
		added = null;
		
		String clas = classChooser.getSelectionModel().getSelectedItem();
		
		if (clas.equals("No Class in Semester!")){
			classChooser.getSelectionModel().clearSelection();
			return;
		}
		taken = getTaken(clas, allCourses);
		
		if (taken != null){
			Collections.sort(taken);
			takenCourses.setItems(FXCollections.observableArrayList(taken));
			
			available = getAvailable(allCourses, taken);
			if (available != null){
				Collections.sort(available);
				availableCourses.setItems(FXCollections.observableArrayList(available));
			}
			else{
				availableCourses.setItems(FXCollections.observableArrayList("No Courses Available"));
				available = new ArrayList<String>();
			}
		}
		
		else{
			available = getAvailable(allCourses, null);
			taken = new ArrayList<String>();
			takenCourses.setItems(FXCollections.observableArrayList("No Courses Taken!"));
			availableCourses.setItems(FXCollections.observableArrayList(allCourses));
		}
		
		teacherChooser.setDisable(true);
		teacherChooser.getSelectionModel().clearSelection();
		attachBtn.setDisable(true);
		removeBtn.setDisable(true);
		submitBtn.setDisable(true);
		availableCourses.getSelectionModel().clearSelection();
		takenCourses.getSelectionModel().clearSelection();
	}
	
	public void attach(ActionEvent e){ // Attach course to class.
		
		if (available == null && taken == null){
			guiMsg.setText("Class Was Not Selected: Please Choose Class");
			return;
		}
		
		String selected = availableCourses.getSelectionModel().getSelectedItem();
		
		if (selected == null || selected.isEmpty()){
			guiMsg.setText("Course Was Not Selected: Please Choose Course");
			return;
		}
		
		String selectedTeacherID = teacherChooser.getSelectionModel().getSelectedItem();
		
		if (selectedTeacherID == null || selectedTeacherID.isEmpty()){
			guiMsg.setText("Teacher Was Not Selected: Please Choose Teacher");
			return;
		}
		
		selectedTeacherID = selectedTeacherID.substring(selectedTeacherID.indexOf('(') + 1, selectedTeacherID.indexOf(')'));
	
		for (int i = 0; i < available.size(); i++)
			if (available.get(i).equals(selected)){
				available.remove(i);
				break;
			}
		
		if (taken == null)
			taken = new ArrayList<String>();
		
		if (added == null)
			added = new ArrayList<String>();
				
		added.add(selected + " [" + selectedTeacherID + "]");
		taken.add(selected);
		Collections.sort(taken);
		// Collections.sort(available);
		takenCourses.setItems(FXCollections.observableArrayList(taken));
		availableCourses.setItems(FXCollections.observableArrayList(available));
		
		if (removed != null){
			for (int i = 0; i < removed.size(); i++)
				if (removed.get(i).equals(selected)){
					removed.remove(i);
					break;
				}
		}
		
		attachBtn.setDisable(true);
		teacherChooser.setDisable(true);
		teacherChooser.getSelectionModel().clearSelection();
		submitBtn.setDisable(false);
	}
	
	public void remove(ActionEvent e){ // Remove course from class.
		
		if (available == null && taken == null){
			guiMsg.setText("Class Was Not Selected: Please Choose Class");
			return;
		}
		
		String selected = takenCourses.getSelectionModel().getSelectedItem();
		
		if (selected.isEmpty() || selected == null){
			guiMsg.setText("Course Was Not Selected: Please Choose Course");
			return;
		}
		
		
		
		for (int i = 0; i < taken.size(); i++)
			if (taken.get(i).equals(selected)){
				taken.remove(i);
				break;
			}
		
		
		if (removed == null)
			removed = new ArrayList<String>();
		
		
				
		removed.add(selected);
		available.add(selected);
		// Collections.sort(taken);
		Collections.sort(available);
		takenCourses.setItems(FXCollections.observableArrayList(taken));
		availableCourses.setItems(FXCollections.observableArrayList(available));
		
		if (added != null){
			for (int i = 0; i < added.size(); i++)
				if ((added.get(i).substring(0, added.get(i).indexOf('[') - 1)).equals(selected)){
					added.remove(i);
					break;
				}
			}
		removeBtn.setDisable(true);
		submitBtn.setDisable(false);
	}
	
	public void submit(ActionEvent e){ // Insert changes to data base.
		
		if (added == null && removed == null){
			JOptionPane.showMessageDialog(null, 
					  "No changes were made!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
			
		String selected = classChooser.getSelectionModel().getSelectedItem();
		claSS cLass = null;
		
		for (int i = 0; i < classArr.size(); i++)
			if (classArr.get(i).getClassName().equals(selected)){
				cLass = classArr.get(i);
				break;
			}
		
		if (cLass == null){
			JOptionPane.showMessageDialog(null, 
					  "No class were selected!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int flag = 0;
		if (added != null && added.size() > 0){
			if (claSS.attachCourses(cLass, added) > 0)
				flag = 1;
			else
				flag = 2;
		}
		
		if (removed != null && removed.size() > 0){
			if (claSS.removeCourses(cLass, removed) > 0)
				flag = 1;
			else
				flag = 3;
		}
		
		switch (flag){
		case 0: JOptionPane.showMessageDialog(null, "No changes were made"); break;
		case 1: JOptionPane.showMessageDialog(null, "Submmision Complete"); break;
		case 2: JOptionPane.showMessageDialog(null,"Submmision Failed: Cant attach courses from class!", "Error", JOptionPane.ERROR_MESSAGE); break;
		case 3: JOptionPane.showMessageDialog(null,"Submmision Failed: Cant remove courses from class!", "Error", JOptionPane.ERROR_MESSAGE); break;
		}
		
		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(getClass().getResource("/FXML/SecretaryCourseToClass.fxml"));
		} catch (IOException e1) {
			e1.printStackTrace();
		};
		Main.getRoot().setCenter(pane);
	}
	
	//-------------------------------------------------------------------------------------------------------------
	
	@Override
	public void initialize(URL location, ResourceBundle resources) { // Initialize window.
		
		semesterArr = Semester.getSemesters();
		teacherArr = Teacher.getTeachers();
		organizedSemesterList = getSemesterList(semesterArr);
		Collections.sort(organizedSemesterList);
		semesterChooser.setItems(FXCollections.observableArrayList(organizedSemesterList));
		course = Course.getCourses();
		allCourses = getCourseList(course);
		Collections.sort(allCourses);
		
		// List press listener.
		
		availableCourses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		    	
		    	String selected = availableCourses.getSelectionModel().getSelectedItem();
				String TU = Course.getTU(selected.substring(selected.indexOf('(') + 1, selected.indexOf(')')));
				ArrayList<String> teachersInTu = TeachingUnit.getTeachers(TU);
				
				if (teachersInTu == null){
					teacherChooser.setItems(FXCollections.observableArrayList("No Teachers in TU"));
					teacherChooser.setDisable(false);
					return;
					
				}
					
				ArrayList<String> teacherList = new ArrayList<String>();
				
				for (int i = 0; i < teachersInTu.size(); i++)
					for (int j = 0; j < teacherArr.size(); j++)
						if (teachersInTu.get(i).equals(teacherArr.get(j).getID()))
							teacherList.add(teacherArr.get(j).getName() + " (" + teacherArr.get(j).getID() + ")");
				
				teacherChooser.setItems(FXCollections.observableArrayList(teacherList));
				teacherChooser.getSelectionModel().clearSelection();
				teacherChooser.setDisable(false);
				attachBtn.setDisable(true);
				removeBtn.setDisable(true);
				takenCourses.getSelectionModel().clearSelection();
		    }
		}); 
		
		// List press listener.
		
		takenCourses.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				removeBtn.setDisable(false);
				teacherChooser.setDisable(true);
				teacherChooser.getSelectionModel().clearSelection();
				attachBtn.setDisable(true);
				availableCourses.getSelectionModel().clearSelection();
		    }
		});
	}
}
