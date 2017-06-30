package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import Entity.Course;
import Entity.Message;
import Entity.Secretery;
import Entity.Semester;
import Entity.Student;
import Entity.Teacher;
import Entity.TeachingUnit;
import Entity.User;
import Entity.claSS;
import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

/**
 * This is the controller class for: "SecretaryCourseToClassController.fxml"
 * @author Idan Agam
 * */

public class SecretaryCourseToClassController implements Initializable {
	
	/** ComboBox for all defined classes	*/
	@FXML
	private ComboBox<String> classChooser;
	
	/** ListView for all available courses to selected class	*/
	@FXML
	private ListView<String> availableCourses;
	
	/** ListView for all taken courses to selected class	*/
	@FXML
    private ListView<String> takenCourses;
	
	@FXML
    private Label guiMsg;
	
	/** ComboBox for all defined semesters up from current	*/
	@FXML
	private ComboBox<String> semesterChooser;
	
	/** ComboBox for all defined teachers for specific teaching unit	*/
	@FXML
    private ComboBox<String> teacherChooser;
	
	/** Button to attach course to class	*/
	@FXML
    private Button attachBtn;
	
	/** Button to remove course from class	*/
	@FXML
    private Button removeBtn;
	
	/** Button to submit changes	*/
	@FXML
    private Button submitBtn;
	
	Alert infoMsg = new Alert(AlertType.INFORMATION);
	Alert errMsg = new Alert(AlertType.ERROR);
	
	/** ArrayList to store all classes from data base	*/
	ArrayList<claSS> classArr;
	
	/** ArrayList to store all courses from data base	*/
	ArrayList<Course> course;
	
	/** ArrayList to store all semesters from data base	*/
	ArrayList<Semester> semesterArr;
	
	/** ArrayList to store all teachers from data base	*/
	ArrayList<Teacher> teacherArr;
	 
	/** ArrayList to store all classes (name & id)	*/
	ArrayList<String> organizedClassList;
	
	/** ArrayList to store all semesters (year & season)	*/
	ArrayList<String> organizedSemesterList;
	
	/** ArrayList to store all courses for specific class	*/
	ArrayList<String> allCourses = null;
	
	/** ArrayList to store all taken courses to specific class	*/
	ArrayList<String> taken = null;
	
	/** ArrayList to store all available courses to specific class	*/
	ArrayList<String> available = null;
	
	/** ArrayList to store all added courses to specific class	*/
	ArrayList<String> added = null;
	
	/** ArrayList to store all removed courses from specific class	*/
	ArrayList<String> removed = null;
	
	//-------------------------------------------------------------------------------------------------------------
	
	/**
	* Send message to all relevant users.
	*/
	private void sendMsgs(ArrayList<String> students, String cls, int sID){
		
		ArrayList<String> Secretary = User.getUserIdByRole("Secretary");
		ArrayList<String> Principal = User.getUserIdByRole("Principal");
		String title = "New Course Attachments";
		String msg;
		String courses = "";
		Semester sem = null;
		
		if (semesterArr != null){
			for (int i = 0; i < semesterArr.size(); i++){
				if (semesterArr.get(i).getId() == sID){
					sem = semesterArr.get(i);
					break;
				}
			}
		}
		
		if (added != null && !added.isEmpty()){
			if (sem != null){
				msg = "Your class have new courses! " + sem.getYear() + "(" + sem.getSeason() +
					 ") :\n\n";
			}
			else{
				msg = "Your assinged class been attached new courses:\n\n";
			}
			
			for (int i = 0; i < added.size(); i++){
				courses += added.get(i).substring(0, added.get(i).indexOf(')')+1) + "\n";
			}
			msg += courses;
			for (int i = 1; i < students.size(); i+=2){
				Message.sendMsg(new Message(title, msg, Integer.parseInt(Main.user.getID()), Integer.parseInt(students.get(i))));
			}
			
			if (sem == null){
				msg = "Class: " + cls + " been attached new courses:\n\n" + courses;
			}
			else{
				msg = "Class: " + cls +  " been attached new courses " + sem.getYear() + "(" + sem.getSeason() +
						 ") :\n\n";
			}
			
			msg += courses;
			for (int i = 0; i < Principal.size(); i++){
				Message.sendMsg(new Message(title, msg, Integer.parseInt(Main.user.getID()), Integer.parseInt(Principal.get(i))));
			}
			
			for (int i = 0; i < Principal.size(); i++){
				Message.sendMsg(new Message(title, msg, Integer.parseInt(Main.user.getID()), Integer.parseInt(Secretary.get(i))));
			}
		}
		
		if (removed != null && !removed.isEmpty()){
			title = "New Removed Courses";
			courses = "";
			
			if (sem != null){
				msg = "Your assinged class been removed new courses " + sem.getYear() + "(" + sem.getSeason() +
					 ") :\n\n";
			}
			else{
				msg = "Your assinged class been removed new courses:\n\n";
			}
			
			for (int i = 0; i < removed.size(); i++){
				courses += removed.get(i).substring(0, removed.get(i).indexOf(')')+1) + "\n";
			}
			msg += courses;
			for (int i = 1; i < students.size(); i+=2){
				Message.sendMsg(new Message(title, msg, Integer.parseInt(Main.user.getID()), Integer.parseInt(students.get(i))));
			}
			
			if (sem == null){
				msg = "Class: " + cls + " been removed new courses:\n\n" + courses;
			}
			else{
				msg = "Class: " + cls +  " been removed new courses " + sem.getYear() + "(" + sem.getSeason() +
						 ") :\n\n";
			}
			
			msg += courses;
			for (int i = 0; i < Principal.size(); i++){
				Message.sendMsg(new Message(title, msg, Integer.parseInt(Main.user.getID()), Integer.parseInt(Principal.get(i))));
			}
			
			for (int i = 0; i < Principal.size(); i++){
				Message.sendMsg(new Message(title, msg, Integer.parseInt(Main.user.getID()), Integer.parseInt(Secretary.get(i))));
			}
		}
	}
	
	/**
	 * get semesters by year & season
	 * @param semsterArr all semesters from DB
	 * @return semester list (year & season)
	 */
	private ArrayList<String> getSemesterList(ArrayList<Semester> semsterArr){ // Get semesters year & season.
		 
		ArrayList<String> temp = new ArrayList<String>();
		if (semsterArr == null){
			return temp;
		}
		
		for (int i = 0; i < semsterArr.size(); i++){
			if (semsterArr.get(i).getIsCurr() == 0){
				temp.add(semsterArr.get(i).getYear() + " (" + semsterArr.get(i).getSeason() + ")");
			}
			else{
				temp.add(semsterArr.get(i).getYear() + " (" + semsterArr.get(i).getSeason() + ") [Current]");
			}
		}
		return temp;	
	 }
	
	/**
	 * Get classes names.
	 * @param classArr all classes from DB
	 * @return class list (name)
	 */
	private ArrayList<String> getClassList(ArrayList<claSS> classArr){ // Get classes names.
		 
		ArrayList<String> temp = new ArrayList<String>();
		if (classArr == null){
			return temp;
		}
		
		for (int i = 0; i < classArr.size(); i++)
			temp.add(classArr.get(i).getClassName());
			
		return temp;	
	 }
	
	/**
	 * Get courses names & ID's for list.
	 * @param courseArr all courses from DB
	 * @return course list (name & id)
	 */
	private ArrayList<String> getCourseList(ArrayList<Course> courseArr){ // Get courses names & ID's for list.
	    	
		ArrayList<String> temp = new ArrayList<String>();
		if (courseArr == null){
			return temp;
		}
		
		for (int i = 0; i < courseArr.size(); i++)
			temp.add(courseArr.get(i).getName() + " (" + courseArr.get(i).getCourseID() + ")");
			
		return temp;	
		}
	  
	/**
	 * Get taken courses for class.
	 * @param clas selected class
	 * @param courses list of all courses
	 * @return list of all taken courses by class
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> getTaken(String clas, ArrayList<String> courses){ // Get taken courses for class.
		  int sem = 0;
		  String str = semesterChooser.getSelectionModel().getSelectedItem();
		for (int i = 0; i < semesterArr.size(); i++){
			if ((semesterArr.get(i).getYear() + " (" + semesterArr.get(i).getSeason() + ")").equals(str)){
				sem = semesterArr.get(i).getId();
			}
		}
		
		
		  HashMap <String,String> msgServer = new HashMap <String,String>();
		  msgServer.put("msgType", "select");
		  msgServer.put("query", "Select c.CourseID,c.CourseName From class_course cc, course c WHERE cc.ClassName = '" + clas + "'" +
				  "AND cc.CourseID = c.CourseID AND semesterId =" + sem);
		  
		  try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					errMsg.setContentText("Send Message To Server Failed!");
					errMsg.showAndWait();
					return null;
				}
			synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException e) {
				errMsg.setContentText("Send Message To Server Failed!");
				errMsg.showAndWait();
				return null;
			}}
			ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
			
			if (result.size() > 0){
				ArrayList<String> temp = new ArrayList<String>();
				for (int i = 0; i < result.size(); i+=2){
					temp.add(result.get(i+1) + " (" + result.get(i) + ")");
				}
				return temp;
			}
			else{
				infoMsg.setContentText("Class Has No Attached Courses!");
				infoMsg.showAndWait();
				return null;
			}
	  }
	  
	/**
	 * Get available courses for class.
	 * @param courses list of all courses
	 * @param taken list of all taken courses for class
	 * @return list of all available courses for class
	 */
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
	
	/**
	 * Add students to courses Or Remove them as necessary, also inform principle for exception students
	 * if exist.
	 * @return
	 */
	private String addRemoveStudents(){ // Add students to courses Or Remove them as necessary.
		
		String cls = classChooser.getSelectionModel().getSelectedItem();
		String exeptions = "Missing Pre Courses\n\n";
		ArrayList<String> students = claSS.getStudents(cls);
		
		ArrayList<String> addedStudents = new ArrayList<String>();
		ArrayList<String> exeptionStudents = new ArrayList<String>();
		ArrayList<String> removedStudents = new ArrayList<String>();
		
		String courseId;
		boolean flag = false;
		
		if (added != null && !added.isEmpty() && !students.isEmpty()){
			for (int i = 1; i < students.size(); i+=2){
				for (int j = 0; j < added.size(); j++){
					courseId = added.get(j).substring(added.get(j).indexOf('(')+1, added.get(j).indexOf(')'));
					if (cantAdd(students.get(i), courseId)){
						exeptions += students.get(i-1) + " (" + students.get(i) + ") course: " +
								added.get(j).substring(0, added.get(j).indexOf(')')+1) + "\n";
						flag = true;
						exeptionStudents.add(students.get(i));
						exeptionStudents.add(added.get(j).substring(added.get(j).indexOf('(')+1, added.get(j).indexOf(')')));
					}
					else{
						addedStudents.add(students.get(i));
						addedStudents.add(added.get(j).substring(added.get(j).indexOf('(')+1, added.get(j).indexOf(')')));
					}
				}
			}
		}
		
		String selected = semesterChooser.getSelectionModel().getSelectedItem();
		int sID = 0;
		for (int i = 0; i < semesterArr.size(); i++){
			if (selected.equals(semesterArr.get(i).getYear() + " (" + semesterArr.get(i).getSeason() + ")")){
				sID = semesterArr.get(i).getId();
				break;
			}
		}
		
		if (!Student.attachStudentsToCourses(sID, addedStudents)){
			errMsg.setContentText("Can't add students to class courses");
			errMsg.showAndWait();
		}
		else{
			sendMsgs(students, cls, sID);
		}
		
		Secretery.sendExceptionStudents(exeptionStudents);
		
		if (removed != null){
			for (int i = 0; i < removed.size(); i++){
				for (int j = 1; j < students.size(); j+=2){
					removedStudents.add(removed.get(i).substring(removed.get(i).indexOf('(')+1, removed.get(i).indexOf(')')));
					removedStudents.add(students.get(j));
				}
			}
			
			if (!Student.removeStudentsfromCourses(sID, removedStudents)){
				infoMsg.setContentText("Could not remove students!");
				infoMsg.showAndWait();
			}
		}
		
		if (flag){
			exeptions += "\nStudents waiting for principal approval";
			return exeptions;
		}
		return null;
	}

	/**
	 * Check if student can be attached to course.
	 * @param studentID Current student id
	 * @param courseID Current course id
	 * @return if student can be attached to course
	 */
	private boolean cantAdd(String studentID, String courseID){ // Check if student can be attached to course.
		
		ArrayList<String> takenCourses = Student.getTakenCourses(studentID);
		ArrayList<String> preCourses = Course.getPreCourses(courseID);
		
		if (preCourses == null)
			return false;
		if (preCourses != null && takenCourses == null)
			return true;
		
		int flag = preCourses.size();
		
		for (int i = 0; i < preCourses.size(); i++){
			for (int j = 0; j < takenCourses.size(); j++){
				if (preCourses.get(i).equals(takenCourses.get(j)))
					flag--;
			}
		}
		
		if (flag > 0)
			return true;
		return false;
	}
 	
	//-------------------------------------------------------------------------------------------------------------

	/**
	 * Check if teacher has not exceeded max working hours.
	 * @param e
	 */
	public void chooseTeacher(ActionEvent e){ // Check if teacher can teach course.
		  
		  if (teacherChooser.getSelectionModel().getSelectedItem() != null){
			 
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
				  infoMsg.setContentText("Teacher Cant be assigned to course: Exceeds max hours limit!");
				  infoMsg.showAndWait();
				  teacherChooser.getSelectionModel().clearSelection();
			  }
			  attachBtn.setDisable(false);
			  
		  }
	  }
	  
	/**
	 *  Load all classes to ComboBox.
	 * @param e
	 */
	public void chooseSemester(ActionEvent e){ // Load all classes in chosen semester.
		
		classArr = claSS.getClasses();
		organizedClassList = getClassList(classArr);
		Collections.sort(organizedClassList);
		classChooser.setItems(FXCollections.observableArrayList(organizedClassList));
		classChooser.setDisable(false);
		classChooser.getSelectionModel().clearSelection();
		teacherChooser.setDisable(true);
		teacherChooser.getSelectionModel().clearSelection();
		attachBtn.setDisable(true);
		removeBtn.setDisable(true);
	  }
	  
	/**
	 * Load taken & available lists for chosen class, to ListViews.
	 * @param e
	 */
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
	
	/**
	 * Attach course to class & show it in lists.
	 * @param e
	 */
	public void attach(ActionEvent e){ // Attach course to class.
		
		if (available == null && taken == null){
			errMsg.setContentText("Class Was Not Selected: Please Choose Class");
			errMsg.showAndWait();
		}
		
		String selected = availableCourses.getSelectionModel().getSelectedItem();
		
		if (selected == null || selected.isEmpty()){
			errMsg.setContentText("Course Was Not Selected: Please Choose Course");
			errMsg.showAndWait();
		}
		
		String selectedTeacherID = teacherChooser.getSelectionModel().getSelectedItem();
		String selectedSemester = semesterChooser.getSelectionModel().getSelectedItem();
		
		if (selectedTeacherID == null || selectedTeacherID.isEmpty()){
			errMsg.setContentText("Teacher Was Not Selected: Please Choose Teacher");
			errMsg.showAndWait();
		}
		
		selectedTeacherID = selectedTeacherID.substring(selectedTeacherID.indexOf('(') + 1, selectedTeacherID.indexOf(')'));
	
		if (selectedSemester == null || selectedSemester.isEmpty()){
			errMsg.setContentText("Semester Was Not Selected: Please Choose Semester");
			errMsg.showAndWait();
		}
		
		int semesterYear = Integer.parseInt(selectedSemester.substring(0, selectedSemester.indexOf('(')-1));
		String semesterSeason = selectedSemester.substring(selectedSemester.indexOf('(')+1, selectedSemester.indexOf(')'));
		int semesterID = -1;
		for (int i = 0; i < semesterArr.size(); i++){
			if (semesterArr.get(i).getYear() == semesterYear && semesterArr.get(i).getSeason().equals(semesterSeason)){
				semesterID = semesterArr.get(i).getId();
				break;
			}
		}
		
		for (int i = 0; i < available.size(); i++)
			if (available.get(i).equals(selected)){
				available.remove(i);
				break;
			}
		
		if (taken == null)
			taken = new ArrayList<String>();
		
		if (added == null)
			added = new ArrayList<String>();
				
		added.add(selected + " [" + selectedTeacherID + "]" + " {" + semesterID + "}");
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

	/**
	 * Remove course from class & show it in lists.
	 * @param e
	 */
	public void remove(ActionEvent e){ // Remove course from class.
		
		if (available == null && taken == null){
			errMsg.setContentText("Class Was Not Selected: Please Choose Class");
			errMsg.showAndWait();
			return;
		}
		
		String selected = takenCourses.getSelectionModel().getSelectedItem();
		
		if (selected.isEmpty() || selected == null){
			errMsg.setContentText("Course Was Not Selected: Please Choose Course");
			errMsg.showAndWait();
			return;
		}
		
		for (int i = 0; i < taken.size(); i++)
			if (taken.get(i).equals(selected)){
				taken.remove(i);
				break;
			}
		
		if (removed == null)
			removed = new ArrayList<String>();
		try{
		removed.add(selected);
		available.add(selected);
		Collections.sort(available);
		
		takenCourses.setItems(FXCollections.observableArrayList(taken));
		availableCourses.setItems(FXCollections.observableArrayList(available));
		}
		catch (Exception expt){
			System.out.println("oops");
		}
		if (added != null){
			for (int i = 0; i < added.size(); i++)
				if ((added.get(i).substring(0, added.get(i).indexOf('[') - 1)).equals(selected)){
					added.remove(i);
					removed.remove(removed.size()-1);
					break;
				}
			}
		
		removeBtn.setDisable(true);
		submitBtn.setDisable(false);
	}
	
	/**
	 * Insert changes to data base.
	 * @param e
	 */
	public void submit(ActionEvent e){ // Insert changes to data base.
		
		String cls = classChooser.getSelectionModel().getSelectedItem();
		if (claSS.getStudents(cls).isEmpty()){
			errMsg.setContentText("Can't add students to class courses");
			errMsg.showAndWait();
		}
		
		if (added == null && removed == null){
			errMsg.setContentText("No changes were made!");
			errMsg.showAndWait();
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
			errMsg.setContentText("No class were selected!");
			errMsg.showAndWait();
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
		String exeptions = addRemoveStudents();
		switch (flag){
		case 0: infoMsg.setContentText("No changes were made"); infoMsg.showAndWait(); break;
		case 1: if (exeptions != null){ infoMsg.setContentText(exeptions); infoMsg.showAndWait();}
				infoMsg.setContentText("Submmision Complete"); infoMsg.showAndWait(); break;
		case 2: errMsg.setContentText("Submmision Failed: Cant attach courses TO class!"); errMsg.showAndWait(); break;
		case 3: errMsg.setContentText("Submmision Failed: Cant remove courses from class!"); errMsg.showAndWait(); break;
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
		
		infoMsg.setTitle("Operation Successful");
		infoMsg.setHeaderText(null);
		errMsg.setTitle("Error Accord");
		errMsg.setHeaderText(null);
		
		semesterArr = Semester.getSemesters();
		
		if (semesterArr == null){
			infoMsg.setContentText("No Semesters defined in data base!");
			infoMsg.showAndWait();
			return;
		}
		
		Semester cur = Semester.getCurrent();
		if (cur == null){
			infoMsg.setContentText("No Current Semester defined in data base!");
			infoMsg.showAndWait();
			return;
		}
		
		for (int i = semesterArr.size()-1; i >= 0; i--){
			
			if (semesterArr.get(i).getYear() == cur.getYear() && semesterArr.get(i).getSeason().compareTo(cur.getSeason()) < 0){
				semesterArr.remove(i);
			}
			
			else if (semesterArr.get(i).getYear() < cur.getYear()){
				semesterArr.remove(i);
			}
		}
		
		
		teacherArr = Teacher.getTeachers();
		
		if (teacherArr == null){
			infoMsg.setContentText("No Teachers defined in data base!");
			infoMsg.showAndWait();
			return;
		}
		
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
				
				if (!takenCourses.getSelectionModel().getSelectedItem().equals("No Courses Taken!")){
					
					removeBtn.setDisable(false);
					teacherChooser.setDisable(true);
					teacherChooser.getSelectionModel().clearSelection();
					attachBtn.setDisable(true);
					availableCourses.getSelectionModel().clearSelection();
				}
				else{
					takenCourses.getSelectionModel().clearSelection();
				}
				
		    }
		});
	}
}
