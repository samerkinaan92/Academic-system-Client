package Entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Student extends User {

	private String classRoom;
	
	public Student()
	{
	}
	
	public Student(String ID, String stdname){
		super(ID,stdname);
	}
	
	/**
	 * Get student class name by student id
	 * @param id Student id
	 * @return Class name as string
	 */
	public static String getStudentClass(String id){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select ClassName From student_class WHERE student_class.StudentID =" + id);
	
		ArrayList<String> result = sendMsg(msgServer);
		
		if (result == null){
			return null;
		}
		
		if (result.size() > 0)
			return result.get(0);
		return null;
	}
	
	/**
	 * Get all courses for a student
	 * @return List of all student courses
	 */
	public static ArrayList<Course> getCourse(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From course_student WHERE course_student.StudentID =" + 
					Main.user.getID());
		
		ArrayList<String> courseResult = sendMsg(msgServer);
		
		if (courseResult == null){
			return null;
		}
		
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
	
	/**
	 * Attach students to courses
	 * @param sID Semester id
	 * @param student_course List of students & courses
	 * @return If succeed
	 */
	public static boolean attachStudentsToCourses(int sID, ArrayList<String> student_course){
		
		for (int i = 0; i < student_course.size(); i+=2){
			
			String msg = "Insert INTO course_student (CourseID, StudentID, semesterId)";
	    	String values = " VALUES (" + student_course.get(i+1) + ", " + student_course.get(i) + ", " + sID + ")";
	    	
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
				return false;
			}}
		}
		return true;
	}
	
	/**
	 * Get all student taken courses
	 * @param sID Student id
	 * @return List of taken courses by student
	 */
	public static ArrayList<String> getTakenCourses(String sID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From course_student WHERE course_student.StudentID =" + sID);
		
		ArrayList<String> courseResult = sendMsg(msgServer);
		
		if (courseResult == null || courseResult.size() > 0)
			return courseResult;
		return null;	
	}
	
	/**
	 * Get all assignments for course
	 * @param courseId Course id
	 * @return List of assignments for course
	 */
	public static ArrayList<Assignment> getAssignments(int courseId){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select * FROM Assignment WHERE Assignment.CourseID = " + courseId);
		
		ArrayList<String> result = sendMsg(msgServer);
		
		if (result == null){
			return null;
		}
		
		ArrayList<Assignment> DBassignment = new ArrayList<Assignment>();
		
		for (int i = 0; i < result.size(); i+=7)
			DBassignment.add(new Assignment(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), Date.valueOf(result.get(i+3)), Date.valueOf((result.get(i+4))), result.get(i+5), Integer.parseInt(result.get(i+6))));
		return DBassignment;
	}
	
	/**
	 * Send message to server
	 * @param msgServer The message to be send
	 * @return Answer from server
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<String> sendMsg(HashMap <String,String> msgServer){
		try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				return null;
			}
		synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			return null;
		}}
		ArrayList<String> courseResult = (ArrayList<String>)Main.client.getMessage();
		if (courseResult == null)
			return null;
		return courseResult;
	}
	
	public String getClassRoom() {
		return classRoom;
	}
	
	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}


}