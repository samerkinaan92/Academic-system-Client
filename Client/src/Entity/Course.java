package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Course extends AcademicActivity {

//	private int ActivityID;
	private int CourseID;
	private String Name;
	private String TUID;
	private int weeklyHours;
	
	
	public Course(int CourseID, String Name, String TUID, int weeklyHours){
		this.CourseID = CourseID;
		this.Name = Name;
		this.TUID = TUID;
		this.weeklyHours = weeklyHours;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Course> getCourses(){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID,Name,TUID,weeklyHours From course");
		
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
			DBcourses.add(new Course(Integer.parseInt(result.get(i)), result.get(i+1), result.get(i+2), Integer.parseInt(result.get(i+3))));
		return DBcourses;
	}
	
	public boolean insertCourse(){ // Insert course to data base.
		
		String msg = "Insert INTO course (CourseID, Name, TUID, weeklyHours)";
    	String values = " VALUES (" + CourseID + ", '" + Name + "', '" + 
    			TUID + "', " + weeklyHours + ")";
    	
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
		
			String msg = "Insert INTO PreRequests (CourseID, preReqCourseID)";
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