package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class claSS {
	
	private String ClassName;
	
	/**
	 * Create a new class
	 * @param name Class name
	 */
	public claSS(String name){
		ClassName = name;
	}
	
	/**
	 * Get all defined classes from data base.
	 * @return Array of classes.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<claSS> getClasses(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select * From class");
		
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
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
		ArrayList<claSS> DBclasses = new ArrayList<claSS>();
		
		for (int i = 0; i < result.size(); i++)
			DBclasses.add(new claSS(result.get(i)));
		return DBclasses;
	}
	
	/**
	 * Attach courses to a single class
	 * @param cLass the class the courses will be added to
	 * @param added the courses to be added to class
	 * @return -1 error, 0 no changes, 0 > number of changes in data base
	 */
	public static int attachCourses(claSS cLass, ArrayList<String> added){
	
		for (int i = 0; i < added.size(); i++){
			String msg = "Insert INTO class_course (ClassName, semesterId, CourseID, teacherID)";
	    	String values = " VALUES ('" + cLass.getClassName() + "', " +
	    			added.get(i).substring(added.get(i).indexOf('{')+1, added.get(i).indexOf('}')) + ", " + 
	    			added.get(i).substring(added.get(i).indexOf('(')+1, added.get(i).indexOf(')')) + ", " + 
	    			added.get(i).substring(added.get(i).indexOf('[')+1, added.get(i).indexOf(']')) + ")";
	    	
	    	HashMap <String,String> msgServer = new HashMap <String,String>();
	    	msgServer.put("msgType", "insert");
			msgServer.put("query", msg + values);
	    	
	    	try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					return -1;
				}
	    	synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException e) {
				return -1;
			}}
		}
		return (int) Main.client.getMessage();
	}
	
	/**
	 * Remove courses from a single class
	 * @param cLass the class the courses will be removed from
	 * @param removedCourses the courses to be removed from class
	 * @return -1 error, 0 no changes, 0 > number of changes in data base
	 */
	public static int removeCourses(claSS cLass, ArrayList<String> removedCourses){
		
		for (int i = 0; i < removedCourses.size(); i++){
			String courseID = removedCourses.get(i);
			courseID = courseID.substring(courseID.indexOf('(') + 1, courseID.indexOf(')'));
			String msg = "DELETE FROM class_course WHERE CourseID = " + courseID + " AND ClassName = '" + cLass.getClassName() + "'";
	    	HashMap <String,String> msgServer = new HashMap <String,String>();
	    	msgServer.put("msgType", "delete");
			msgServer.put("query", msg);
	    	
	    	try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					return -1;
				}
	    	synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException e) {
				return -1;
			}}
		}
		
		return (int) Main.client.getMessage();
	}
	
	/**
	 * Get array of students from data base, that take specific class.
	 * @param cls the class to search student in.
	 * @return Array of students.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getStudents(String cls){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select StudentID from student_class where ClassName = '"+cls+"';");
		
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
		}
		}
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
		ArrayList<String> students = new ArrayList<String>();
		for (int i = 0; i < result.size(); i++){
			
			msgServer = new HashMap <String,String>();
			msgServer.put("msgType", "select");
			msgServer.put("query", "select Name,ID from users where ID = "+result.get(i));
			
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
			}
			}
			ArrayList<String> result1 = (ArrayList<String>)Main.client.getMessage();
			students.add(result1.get(0));
			students.add(result1.get(1));
		}
		return students;
	}

	/**
	 * Get all the teachers that teach specific class 
	 * @param className the class to check
	 * @return Array of the teachers
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getTeachersOfClass(String className){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select teacherID from class_course where ClassName = '"+className+"';");
		
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
		}
		}
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
	
		return result;
	}
	
	/**
	 * Get the class a specific student learn at. 
	 * @param ID Student Id
	 * @return The class name.
	 */
	public static String getClassByStud(String ID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select ClassName from student_class where StudentID='"+ID+"';");
		ArrayList<String> result = sendMsg(msgServer);	
		
		if (result == null)
			return null;
		
		return result.get(0);
	}
	
	/**
	 * Send a message to server
	 * @param msgServer The message to send
	 * @return Server answer
	 */
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
		@SuppressWarnings("unchecked")
		ArrayList<String> courseResult = (ArrayList<String>)Main.client.getMessage();
		if (courseResult == null)
			return null;
		return courseResult;
	}

	public String getClassName() {
		return ClassName;
	}

	public void setClassName(String className) {
		ClassName = className;
	}
}
