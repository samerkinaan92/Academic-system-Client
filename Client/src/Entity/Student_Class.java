package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Student_Class{
	
	private int StudentID;
	private String StudName;
	private String semID;

	
	public Student_Class(int StudentID, String StudName, String semID)
	{
		this.StudentID=StudentID;
		this.StudName=StudName;
		this.semID=semID;
	}
	
	
	public static ArrayList<Student_Class> getStudentsByClass(String className){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select studentid,name,ClassName From Student_Class,Users where Student_Class.className='"+className+"' and Users.id=studentid;");
		
		ArrayList<String> result = sendMsg(msgServer);
		ArrayList<Student_Class> DBassignment = new ArrayList<Student_Class>();
		
		
		for (int i = 0; i < result.size(); i+=3)
			DBassignment.add(new Student_Class(Integer.parseInt(result.get(i)), result.get(i+1), result.get(i+2)));
		return DBassignment;
	}
	
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> sendMsg(HashMap <String,String> msgServer){
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

	
	public int getStudentID() {
		return StudentID;
	}


	public void setStudentID(int studentID) {
		StudentID = studentID;
	}


	public String getStudName() {
		return StudName;
	}


	public void setStudName(String StudName) {
		this.StudName = StudName;
	}


	public String getSemID() {
		return semID;
	}


	public void setSemID(String semID) {
		this.semID = semID;
	}
	
}
