package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class claSS {
	
	private String ClassName;
	private int year;
	
	
	public claSS(String name, int year){
		ClassName = name;
		this.year = year;
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
		
		for (int i = 0; i < result.size(); i+=2)
			DBclasses.add(new claSS(result.get(i), Integer.parseInt(result.get(i+1))));
		return DBclasses;
		
	}
	
	public static int attachCourses(claSS cLass, ArrayList<String> added){
		
		
		for (int i = 0; i < added.size(); i++){
			String msg = "Insert INTO class_course (ClassName, Year, CourseID, teacherID)";
	    	String values = " VALUES ('" + cLass.getClassName() + "', " + cLass.getYear() + ", " + 
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
	public static ArrayList<Teacher> getTeachersOfClass(String className){
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
		
		msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select TeacherID, MaxWorkHours From teacher");
		
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
		ArrayList<String> result2 = (ArrayList<String>)Main.client.getMessage();
		
		for (int i = 1; i < result.size(); i+=2)
			for (int j = 0; j < result2.size(); j+=2)
				if (result.get(i).equals(result2.get(j))){
					result.add(i+1, result2.get(j+1));
					i++;
					break;
				}
		
		ArrayList<Teacher> DBteachers = new ArrayList<Teacher>();
		
		for (int i = 0; i < result.size(); i+=3)
			DBteachers.add(new Teacher(result.get(i), result.get(i+1), Integer.parseInt(result.get(i+2))));
		return DBteachers;

	}

	public String getClassName() {
		return ClassName;
	}


	public void setClassName(String className) {
		ClassName = className;
	}


	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}

}
