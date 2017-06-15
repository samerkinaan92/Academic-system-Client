package Entity;


import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Assignment {

	private int assignmentID;
	private String AssignmentName;
	private int CourseID;
	private Date publishDate;
	private Date deadLine;
	private String filePath;
	private int semesterID;
	
	
	
	public Assignment(int Aid, String name, int Cid, Date publish, Date dead, String path, int Sid){
		assignmentID = Aid;
		AssignmentName = name;
		CourseID = Cid;
		publishDate = publish;
		deadLine = dead;
		filePath = path;
		semesterID = Sid;
	}
	
	public static byte[] getFile(String path){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "getFile");
		msgServer.put("filePath", path);
		
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
		byte[] result = (byte[])Main.client.getMessage();
		
		return result;
	}
	
	
	
	
	
	public int getAssignmentID() {
		return assignmentID;
	}


	public String getAssignmentName() {
		return AssignmentName;
	}


	public void setAssignmentName(String assignmentName) {
		AssignmentName = assignmentName;
	}


	public int getCourseID() {
		return CourseID;
	}


	public void setCourseID(int courseID) {
		CourseID = courseID;
	}


	public Date getPublishDate() {
		return publishDate;
	}


	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}


	public Date getDeadLine() {
		return deadLine;
	}


	public void setDeadLine(Date deadLine) {
		this.deadLine = deadLine;
	}


	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}





	public int getSemesterID() {
		return semesterID;
	}





	public void setSemesterID(int semesterID) {
		this.semesterID = semesterID;
	}


	
	

	
	
	
	
	
}