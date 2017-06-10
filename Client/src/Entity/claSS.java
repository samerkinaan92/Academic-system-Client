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
	
	public static int attachCourses(claSS cLass, ArrayList<Integer> addedCourses){
		
		
		for (int i = 0; i < addedCourses.size(); i++){
			String msg = "Insert INTO class_course (ClassName, Year, CourseID)";
	    	String values = " VALUES ('" + cLass.getClassName() + "', " + cLass.getYear() + ", " + 
	    			addedCourses.get(i) + ")";
	    	
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

	
	public static int removeCourses(claSS cLass, ArrayList<Integer> removedCourses){
		
		
		for (int i = 0; i < removedCourses.size(); i++){
			String msg = "DELETE FROM class_course WHERE CourseID = " + removedCourses.get(i);
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
