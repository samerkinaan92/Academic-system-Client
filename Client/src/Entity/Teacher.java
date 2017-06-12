package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Teacher extends User {

	private int maxWorkHours;
	

	public Teacher(String name){
		this.Name = name;
	}
	public Teacher(String name, String id, int hours){
		super(id, name);
		maxWorkHours = hours;
	}
	
	/*-------------------------------------  Get Teachers  --------------------------------------*/
	@SuppressWarnings("unchecked")
	public static String getTeachersByID(String ID){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select Name, ID From users WHERE Role = 'Teacher' AND ID ='"+ID+"';");
		
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
		String res = result.get(0);
		
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
		
		return DBteachers.get(0).getName();

	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getCoursesOfTeacher(String ID){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From class_course WHERE teacherID = '"+ID+"';");
		
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
		return result;
	}
	
	

	
	@SuppressWarnings("unchecked")
	public static ArrayList<Teacher> getTeachers(){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select Name, ID From users WHERE Role = 'Teacher'");
		
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
	
	/*-------------------------------------  Get Sum of Hours  --------------------------------------*/
	
	@SuppressWarnings("unchecked")
	public static int getSumOfHours(String teacherID){
		
		int sum = 0;
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From class_course WHERE teacherID = " + teacherID);
		
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
		
		
		for (int i = 0; i < result.size(); i++){
		
			msgServer = new HashMap <String,String>();
			msgServer.put("msgType", "select");
			msgServer.put("query", "Select WeeklyHours From course WHERE CourseID = " + result.get(i));
			
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
			sum += Integer.parseInt(result2.get(0));
		}
		
		return sum;
		
	}
	

	public int getMaxWorkHours() {
		return maxWorkHours;
	}
	/*public String getName() {
		return Name;
	}*/
	public void setID(String iD) {
		super.setID(iD);
	}


	public void setMaxWorkHours(int maxWorkHours) {
		this.maxWorkHours = maxWorkHours;
	}
	

public static ArrayList<claSS> getTeachersClass(String ID){
	
	HashMap <String,String> msgServer = new HashMap <String,String>();
	msgServer.put("msgType", "select");
	msgServer.put("query", " select ClassName from class_course where teacherID ="+ID+";");
	
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
	for (int i=0 ; i < result.size() ; i++)
		System.out.println(result.get(i));
	ArrayList<claSS> DBclasses = new ArrayList<claSS>();
	for (int i = 0; i < result.size(); i++)
		DBclasses.add(new claSS(result.get(i), Integer.parseInt(result.get(i+1))));


	
	return DBclasses;
	
}

public static ArrayList<String> getTeachersClassAsStringArrList(String ID){
	
	HashMap <String,String> msgServer = new HashMap <String,String>();
	msgServer.put("msgType", "select");
	msgServer.put("query", " select ClassName from class_course where teacherID ="+ID+";");
	
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

	return result;
	
}

}