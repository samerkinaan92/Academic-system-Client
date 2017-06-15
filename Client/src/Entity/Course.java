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
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Course> getCourses(String teachingUnit){ // Get list of courses.
		ArrayList<Course> DBcourses = null;
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID ,CourseName, weeklyHours, TUName From course where TUName = '" + teachingUnit + "';");
		
		try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				System.out.println("Server fatal error!");
			}
		synchronized (Main.client){try {
			Main.client.sendMessageToServer(msgServer);
			Main.client.wait();
			ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
			DBcourses = new ArrayList<Course>();
			
			for (int i = 0; i < result.size(); i+=4)
				DBcourses.add(new Course(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), result.get(i+3)));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
		return DBcourses;
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