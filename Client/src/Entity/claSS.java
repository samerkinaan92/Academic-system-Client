package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class claSS {
	
	private String ClassName;
	
	
	public claSS(String name){
		ClassName = name;
	}
	
	
	@SuppressWarnings("unchecked")
	public static ArrayList<claSS> getClasses(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select * From class");
		
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
		ArrayList<claSS> DBclasses = new ArrayList<claSS>();
		
		for (int i = 0; i < result.size(); i++)
			DBclasses.add(new claSS(result.get(i)));
		return DBclasses;
		
	}
	
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
				e.printStackTrace();
			}}
		}
		
		
		
		return (int) Main.client.getMessage();
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getStudents(String cls){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select StudentID from student_class where ClassName = '"+cls+"';");
		
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
					System.out.println("Server fatal error!");
				}
			synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
			ArrayList<String> result1 = (ArrayList<String>)Main.client.getMessage();
			students.add(result1.get(0));
			students.add(result1.get(1));
		}
		return students;
	}

	
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
				e.printStackTrace();
			}}
		}
		
		return (int) Main.client.getMessage();
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getTeachersOfClass(String className){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select teacherID from class_course where ClassName = '"+className+"';");
		
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
		}
		}
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
	
		return result;

	}
	
	
	public static String getClassByStud(String ID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select ClassName from student_class where StudentID='"+ID+"';");
		ArrayList<String> result=sendMsg(msgServer);				
		
		return result.get(0);
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

	public String getClassName() {
		return ClassName;
	}


	public void setClassName(String className) {
		ClassName = className;
	}

}
