package Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import application.Main;

public class SubmittedAssignment extends Assignment {

	public SubmittedAssignment(int Aid, String name, int Cid, java.sql.Date publish, java.sql.Date dead, String path, int Sid) {
		super(Aid, name, Cid, publish, dead, path, Sid);
	}
	private Date submittedDate;
	private String studentID;
	
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getSubmittedAssignments(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select AssignmentID From submission WHERE StudentStudentID = " + Main.user.getID());
		
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
		
		if (result.size() > 0)
			return result;
		return null;
		
	}
	
	
	public Date getSubmittedDate() {
		return submittedDate;
	}
	public String getStudentID() {
		return studentID;
	}
	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

}