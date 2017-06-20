package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Teacher extends User {

	private int maxWorkHours;

	public Teacher(String name){
		super(name);
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
				return null;
			}
		synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			return null;
		}
		}
		
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
		
		if (result.isEmpty()){
			return null;
		}
		
		
		msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select TeacherID, MaxWorkHours From teacher");
		
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
		ArrayList<String> result2 = (ArrayList<String>)Main.client.getMessage();
		
		if (result2.isEmpty()){
			return null;
		}
		
		boolean flag = false;
		for (int i = 1; i < result.size(); i+=2, flag = false){
			for (int j = 0; j < result2.size(); j+=2)
				if (result.get(i).equals(result2.get(j))){
					result.add(i+1, result2.get(j+1));
					i++;
					flag = true;
					break;
				}
			if (!flag){
				result.add(i+1, "0");
				i++;
			}
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
				return null;
			}
		synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			return null;
		}}
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
		return result;
	}
	
	/**
	 * Get all teachers from data base
	 * @return List of all teachers
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Teacher> getTeachers(){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select Name, ID From users WHERE Role = 'Teacher'");
		
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
		
		if (result.isEmpty()){
			return null;
		}
		
		msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select TeacherID, MaxWorkHours From teacher");
		
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
		ArrayList<String> result2 = (ArrayList<String>)Main.client.getMessage();
		
		if (result2.isEmpty()){
			return null;
		}
		
		boolean flag = false;
		for (int i = 1; i < result.size(); i+=2, flag = false){
			for (int j = 0; j < result2.size(); j+=2)
				if (result.get(i).equals(result2.get(j))){
					result.add(i+1, result2.get(j+1));
					i++;
					flag = true;
					break;
				}
			if (!flag){
				result.add(i+1, "0");
				i++;
			}
		}
		ArrayList<Teacher> DBteachers = new ArrayList<Teacher>();
		
		for (int i = 0; i < result.size(); i+=3)
			DBteachers.add(new Teacher(result.get(i), result.get(i+1), Integer.parseInt(result.get(i+2))));
		
		return DBteachers;
	}
	
	/*-------------------------------------  Get Sum of Hours  --------------------------------------*/
	/**
	 * Get sum of assigned working hours for teacher
	 * @param teacherID Teacher id
	 * @return Sum of working hours
	 */
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
				return -1;
			}
		synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			return -1;
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
					return -1;
				}
			synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException e) {
				return -1;
			}}
			ArrayList<String> result2 = (ArrayList<String>)Main.client.getMessage();
			sum += Integer.parseInt(result2.get(0));
		}
		return sum;
	}
	
	/**
	 * Get all teachers assigned classes
	 * @param ID Teacher id
	 * @return List of assigned classes
	 */
	public static ArrayList<claSS> getTeachersClass(String ID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", " select ClassName from class_course where teacherID ="+ID+";");
		
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
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
		
		if (result == null){
			return null;
		}
		
		ArrayList<claSS> DBclasses = new ArrayList<claSS>();
		for (int i = 0; i < result.size(); i++)
			DBclasses.add(new claSS(result.get(i)));
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
				return null;
			}
		synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			return null;
		}}
		@SuppressWarnings("unchecked")
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
	
		return result;
	}

	public int getMaxWorkHours() {
		return maxWorkHours;
	}
	
	public void setID(String iD) {
		super.setID(iD);
	}
	
	public void setMaxWorkHours(int maxWorkHours) {
		this.maxWorkHours = maxWorkHours;
	}
}