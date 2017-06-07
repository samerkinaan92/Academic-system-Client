package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import Entity.Course;
import Entity.claSS;
import application.Main;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class SecretaryCourseToClassController implements Initializable {
	
	@FXML
	private ComboBox<String> courseChooser;
	
	@FXML
	private ListView<String> availableCourses;
	
	@FXML
    private ListView<String> takenCourses;
	
	@FXML
    private Label guiMsg;
	
	
	ArrayList<claSS> classArr;
	
	ArrayList<Course> course;
	 
	ArrayList<String> organizedClassList;
	
	ArrayList<String> allCourses = null;
	
	ArrayList<String> taken = null;
	
	ArrayList<String> available = null;
	
	ArrayList<String> added = null;
	
	ArrayList<String> removed = null;
	
	//-------------------------------------------------------------------------------------------------------------
	
	private ArrayList<String> getClassList(ArrayList<claSS> classArr){
		 
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
	private ArrayList<String> getTaken(String clas, ArrayList<String> courses){
		  
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
	  
	  private ArrayList<String> getAvailable(ArrayList<String> courses, ArrayList<String> taken){
		  boolean flag = true;
		  ArrayList<String> temp = new ArrayList<String>();
		  
		  for (int i = 0; i < courses.size(); i++, flag = true){
			 for(int j = 0; j < taken.size(); j++)
				 if (courses.get(i).equals(taken.get(j))){
					 flag = false;
					 break;
				 }
			 if (flag)
				 temp.add(courses.get(i));
		  }
		  if (temp.size() > 0)
			  return temp;
		  else 
			  return null;
		  
	  }
 	
	//-------------------------------------------------------------------------------------------------------------

	
	public void chooseCourse(ActionEvent e){
		
		String clas = courseChooser.getSelectionModel().getSelectedItem();
		
		course = Course.getCourses();
		
		allCourses = getCourseList(course);
		
		taken = getTaken(clas, allCourses);
		
		if (taken != null){
			Collections.sort(taken);
			takenCourses.setItems(FXCollections.observableArrayList(taken));
			
			available = getAvailable(allCourses, taken);
			if (available != null){
				Collections.sort(available);
				availableCourses.setItems(FXCollections.observableArrayList(available));
			}
			else
				availableCourses.setItems(FXCollections.observableArrayList("No Courses Available"));
		}
		
		else{
			availableCourses.setItems(FXCollections.observableArrayList(allCourses));
			available = new ArrayList<String>();
			for (int i = 0; i < allCourses.size(); i++)
				available.add(allCourses.get(i));
		}
	}
	
	public void attach(ActionEvent e){
		
		if (available == null && taken == null)
			guiMsg.setText("Class Was Not Selected: Please Choose Class");
		
		String selected = availableCourses.getSelectionModel().getSelectedItem();
		
		if (selected.isEmpty() || selected == null)
			guiMsg.setText("Course Was Not Selected: Please Choose Course");
		
		for (int i = 0; i < available.size(); i++)
			if (available.get(i).equals(selected)){
				available.remove(i);
				break;
			}
		
		if (taken == null)
			taken = new ArrayList<String>();
		
		if (added == null)
			added = new ArrayList<String>();
				
		added.add(selected);
		taken.add(selected);
		Collections.sort(taken);
		Collections.sort(available);
		takenCourses.setItems(FXCollections.observableArrayList(taken));
		availableCourses.setItems(FXCollections.observableArrayList(available));
		
	}
	
	public void submit(ActionEvent e){
		
		if (added == null && removed == null){
			JOptionPane.showMessageDialog(null, 
					  "No changes were made!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
			
		
		String selected = courseChooser.getSelectionModel().getSelectedItem();
		
		claSS cLass = null;
		
		ArrayList<Integer> addedCourses = new ArrayList<Integer>();
		ArrayList<Integer> removedCourses = new ArrayList<Integer>();
		
		
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
		
		
		if (added != null){
			for (int i = 0; i < added.size(); i++)
				for (int j = 0; j < course.size(); j++)
					if (Integer.parseInt(added.get(i).substring(added.get(i).indexOf
							('(') + 1, added.get(i).indexOf(')'))) == course.get(j).getCourseID())
						addedCourses.add(course.get(j).getCourseID());
		}
		
		if (removed != null){
			for (int i = 0; i < removed.size(); i++)
				for (int j = 0; j < course.size(); j++)
					if (Integer.parseInt(removed.get(i).substring(removed.get(i).indexOf
							('(') + 1, removed.get(i).indexOf(')'))) == course.get(j).getCourseID())
						removedCourses.add(course.get(j).getCourseID());
		}
		
		
		if (claSS.attachCourses(cLass, addedCourses) > 0)
			JOptionPane.showMessageDialog(null, "Submition Complete");
		else
			JOptionPane.showMessageDialog(null, 
					  "Could not add courses!", "Error", JOptionPane.ERROR_MESSAGE);
		
		if (claSS.removeCourses(cLass, removedCourses) > 0)
			JOptionPane.showMessageDialog(null, "Submition Complete");
		else
			JOptionPane.showMessageDialog(null, 
					  "Could not remove courses!", "Error", JOptionPane.ERROR_MESSAGE);
		
	}
	
	
	
	//-------------------------------------------------------------------------------------------------------------
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		classArr = claSS.getClasses();
		organizedClassList = getClassList(classArr);
		Collections.sort(organizedClassList);
		courseChooser.setItems(FXCollections.observableArrayList(organizedClassList));
		
	}

}
