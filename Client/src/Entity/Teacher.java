package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Teacher extends User {

	private String ID;
	private int maxWorkHours;
	
	public Teacher(String Name){
		this.Name = Name;
	}
	
	public String getID() {
		return ID;
	}
	
	public int getMaxWorkHours() {
		return maxWorkHours;
	}
	/*public String getName() {
		return Name;
	}*/
	public void setID(String iD) {
		ID = iD;
	}
	public void setMaxWorkHours(int maxWorkHours) {
		this.maxWorkHours = maxWorkHours;
	}
	
/*
	private static String yyyy(ArrayList<Teacher> TeacherArry){
		 
		 String temp = new String();
		 ArrayList<Teacher> TeacArr;
			for (int i = 0; i < TeacArr.size(); i++)
				temp = TeacArr.get(0).getClassName();
			
			return temp;	
	 }
	*/
	
public static String getTeacherID(String techerName){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select ID From users where users.Name = "+techerName+" and users.Role = "+"Teacher ;");
		

		try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				System.out.println("Server fatal error!");
			}

		String result = (String)Main.client.getMessage();
		System.out.println(result);

		return result;
	}
	
public static ArrayList<Teacher> getTeachersNames(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", " select Name from teacher, users where teacher.TeacherID = users.ID;");
		
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
		ArrayList<Teacher> DBteachers = new ArrayList<Teacher>();
		
		for (int i = 0; i < result.size(); i++)
			DBteachers.add(new Teacher(result.get(i)));
		return DBteachers;
		
	}


public static ArrayList<Teacher> getTeachersClass(){
	
	HashMap <String,String> msgServer = new HashMap <String,String>();
	msgServer.put("msgType", "select");
	msgServer.put("query", " select Name from teacher, users where teacher.TeacherID = users.ID;");
	
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
	ArrayList<Teacher> DBteachers = new ArrayList<Teacher>();
	
	for (int i = 0; i < result.size(); i++)
		DBteachers.add(new Teacher(result.get(i)));
	return DBteachers;
	
}

//@SuppressWarnings("unchecked")
public static ArrayList<Teacher> getTeachers(){
	
	HashMap <String,String> msgServer = new HashMap <String,String>();
	msgServer.put("msgType", "select");
	msgServer.put("query", "Select * From teacher , ");
	
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
	ArrayList<Teacher> DBteachers = new ArrayList<Teacher>();
	
	for (int i = 0; i < result.size(); i++)
		DBteachers.add(new Teacher(result.get(i)));
	return DBteachers;
	
}

}