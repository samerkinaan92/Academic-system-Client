package Entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Student extends User {

	
	private String classRoom;
	private String parentID1;
	private String parentID2;
	
	
	
	
	public static ArrayList<Course> getCourse(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From course_student WHERE course_student.StudentID =" + 
					Main.user.getID());
		
		ArrayList<String> courseResult = sendMsg(msgServer);
		ArrayList<Course> DBcourses = new ArrayList<Course>();
		
		for (int j = 0; j < courseResult.size(); j++){
			HashMap <String,String> msgServer1 = new HashMap <String,String>();
			msgServer1.put("msgType", "select");
			msgServer1.put("query", "Select CourseID,CourseName,WeeklyHours,TUName From course WHERE course.CourseID =" + 
					courseResult.get(j));
			ArrayList<String> result = sendMsg(msgServer1);
			DBcourses.add(new Course(Integer.parseInt(result.get(0)), result.get(1), Integer.parseInt(result.get(2)), result.get(3)));
		}
		return DBcourses;
		
	}
	
	public static ArrayList<Assignment> getAssignments(int courseId){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select * FROM Assignment WHERE Assignment.CourseID = " + courseId);
		
		ArrayList<String> result = sendMsg(msgServer);
		ArrayList<Assignment> DBassignment = new ArrayList<Assignment>();
		
		for (int i = 0; i < result.size(); i+=6)
			DBassignment.add(new Assignment(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), Date.valueOf(result.get(i+3)), Date.valueOf((result.get(i+4))), result.get(i+5)));
		return DBassignment;
	}
	
		
	@SuppressWarnings("unchecked")
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
		ArrayList<String> courseResult = (ArrayList<String>)Main.client.getMessage();
		if (courseResult == null)
			return null;
		return courseResult;
	}
	
	
	
	public String getClassRoom() {
		return classRoom;
	}
	public String getParentID1() {
		return parentID1;
	}
	public String getParentID2() {
		return parentID2;
	}
	
	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}
	public void setParentID1(String parentID1) {
		this.parentID1 = parentID1;
	}
	public void setParentID2(String parentID2) {
		this.parentID2 = parentID2;
	}

}