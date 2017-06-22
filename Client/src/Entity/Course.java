package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Course extends AcademicActivity {


	private int CourseID;
	private String Name;
	private String TUID;
	private int weeklyHours;
	
	
	public Course(int CourseID, String Name, int weeklyHours, String TUID){
		this.CourseID = CourseID;
		this.Name = Name;
		this.TUID = TUID;
		this.weeklyHours = weeklyHours;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Course> filterOldCourses(ArrayList<Course> courses){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From mat.course_student WHERE course_student.StudentID = " + Main.user.getID() 
			+ " AND course_student.semesterId = " + Semester.getCurrent().getId() + ";");
		
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
		
		for (int i = courses.size() - 1; i >= 0 ; i--){
			boolean flag = false;
			for (int j = 0; j < result.size(); j++){
				if (String.valueOf(courses.get(i).getCourseID()).equals(result.get(j))){
					flag = true;
					break;
				}
			}
			
			if (!flag){
				courses.remove(i);
			}
		}
		
		return courses;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Course> getCourses(){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID,CourseName, weeklyHours, TUName From course");
		
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
		ArrayList<Course> DBcourses = new ArrayList<Course>();
		
		for (int i = 0; i < result.size(); i+=4)
			DBcourses.add(new Course(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), result.get(i+3)));
		return DBcourses;
	}
	
	
	
	public static String getCourseIDbyName(String CoursName){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From course where CourseName='"+CoursName+"';");
		
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
		@SuppressWarnings("unchecked")
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
		return result.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Course> getCourses(String teachingUnit){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID,CourseName, weeklyHours, TUName From course where TUName = '" + teachingUnit + "'");
		
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
		ArrayList<Course> DBcourses = new ArrayList<Course>();
		
		for (int i = 0; i < result.size(); i+=4)
			DBcourses.add(new Course(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), result.get(i+3)));
		return DBcourses;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getPreCourses(String cID){
		
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select preCourseID From pre_courses WHERE CourseID = " + cID);
		
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
		
		if (result.size() > 0)
			return result;
		return null;
		
		
	}
	
	@SuppressWarnings("unchecked")
	public static String getCourseName(String ID){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseName From course WHERE CourseID ='"+ID+"';");
		
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
		
		String res;	
		res = result.get(0);
		return res;
	}
	
	public boolean insertCourse(){ // Insert course to data base.
		
		String msg = "Insert INTO course (CourseID, CourseName, weeklyHours, TUName)";
    	String values = " VALUES (" + CourseID + ", '" + Name + "', " + 
    			 weeklyHours + ", '" + TUID + "')";
    	
    	HashMap <String,String> msgServer = new HashMap <String,String>();
    	msgServer.put("msgType", "insert");
		msgServer.put("query", msg + values);
    	
    	try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				return false;
			}
    	synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
		
		return true;
	}
	
	public boolean updatePreCourses(ArrayList<String> PreList){ // Update PreCourses for Course in data base.
		
		for (int i = 0; i < PreList.size(); i++){
		
			String msg = "Insert INTO pre_courses (CourseID, preCourseID)";
	    	String values = " VALUES (" + CourseID + ", " + 
	    	PreList.get(i).substring(PreList.get(i).indexOf('(') + 1, PreList.get(i).indexOf(')')) + ")";
	    	
	    	HashMap <String,String> msgServer = new HashMap <String,String>();
	    	msgServer.put("msgType", "insert");
			msgServer.put("query", msg + values);
	    	
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
		}
		return true;
	}
		
	@SuppressWarnings("unchecked")
	public static boolean checkLegal(String CourseID){ // Check if Course ID exist in data base.
		HashMap<String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From course");
		
		try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				exp.printStackTrace();;
			}
		synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
		
		ArrayList<String>	result = (ArrayList<String>)Main.client.getMessage();
		
		for (int i = 0; i < result.size(); i++)
			if (result.get(i).equals(CourseID))
				return false;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static String getTU(String coursID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select TUName From course Where CourseID = '" + coursID + "'");
		
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
		
		if (result.size() > 0)
			return result.get(0);
		else
			return null;
	}
	
	public static ArrayList<Course> getCoursesBySemStd(String sem, String stdID){
		HashMap<String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select course.courseid,CourseName,WeeklyHours,tuname from semester,course_student,course where semester.semesterid=course_student.semesterid and semester.semesterid='"+sem+"' and course.courseid=course_student.courseid and studentid='"+stdID+"';");
		ArrayList<String>result = sendMsg(msgServer);
		
		ArrayList<Course>courseArr = new ArrayList<Course>();
		
		for(int i=0 ; i<result.size() ; i+=4)
			courseArr.add(new Course(Integer.parseInt(result.get(i)),result.get(i+1),Integer.parseInt(result.get(i+2)),result.get(i+3)));
		return courseArr;
	}
	
	private static ArrayList<String> sendMsg(HashMap <String,String> msgServer){
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
		@SuppressWarnings("unchecked")
		ArrayList<String> courseResult = (ArrayList<String>)Main.client.getMessage();
		if (courseResult == null)
			return null;
		return courseResult;
	}
	

	@Override
	public String toString() {	
		return getName() + " (" + getCourseID() + ")";
	}
	
	
	
	public int getCourseID() {
		return CourseID;
	}
	public String getName() {
		return Name;
	}
	public String getTUID() {
		return TUID;
	}
	public int getWeeklyHours() {
		return weeklyHours;
	}
	
	public void setCourseID(int courseID) {
		CourseID = courseID;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setTUID(String tUID) {
		TUID = tUID;
	}
	public void setWeeklyHours(int weeklyHours) {
		this.weeklyHours = weeklyHours;
	}
	
	/*
	public int getActivityID() {
		return ActivityID;
	}
	public void setActivityID(int activityID) {
		ActivityID = activityID;
	}
	*/
}