package Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import application.Main;
/**
 * Class of Submition of student
 * @author Or Cohen
 *
 */
public class SubmittedAssignment extends Assignment {
	/**SubmittedAssignment()
	 * @param submittedDate - Date of Submittion
	 * @param studentID- Student ID
	 * @param isLate - flag of Late submittion
	 * @param Grade - Grade of Submittion
	 * @param FilePath - File Path of Submittion
	 * @param AssignmentID - Assignment ID of Submittion
	 * */
	private Date submittedDate;
	private String studentID;
	private String isLate;
	private String Grade;
	private String FilePath;
	private int AssignmentID;
	/** Constractor1 */
	public SubmittedAssignment(int Aid, String name, int Cid, java.sql.Date publish, java.sql.Date dead, String path, int Sid) {
		super(Aid, name, Cid, publish, dead, path, Sid);
	}
	/** Constractor2 */
	public SubmittedAssignment(int Aid, String name, int Cid, String publish, String dead, int Sid, String isLate, String Grade,String path) {
		super(Aid, name, Cid, publish, dead);
		this.studentID = Integer.toString(Sid);
		FilePath = path;
		this.Grade = Grade;
		this.isLate = isLate;
	}
	/**
	 * Get the file as byte array from server
	 * @param path The path the file is stored in server.
	 * @return Representation of file as byte array.
	 */
public static byte[] getFile(String path){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "getFile");
		msgServer.put("filePath", path);
		
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
		byte[] result = (byte[])Main.client.getMessage();
		
		if (result != null)
			return result;
		return null;
	}
	
	/**
	 * get all submittions of current user
	 * @return ArrayList<String>
	 */
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
	/**
	 * returns all submittions of selected student
	 * @param StudentID
	 * @return ArrayList<SubmittedAssignment> 
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<SubmittedAssignment> getSubmitionsOfStudent(String StudentID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select assignment.AssignmentID, assignment.AssignmentName ,assignment.CourseID, assignment.publishDate,assignment.deadLine ,submission.StudentStudentID, submission.isLate, submission.Grade,submission.filePath from assignment, submission Where assignment.AssignmentID = submission.AssignmentID and submission.StudentStudentID = '" +StudentID+"';");
		
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
		ArrayList<SubmittedAssignment> DBSubmittion = new ArrayList<SubmittedAssignment>();
		
		for (int i = 0; i < result.size(); i+=9)
			DBSubmittion.add(new SubmittedAssignment(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), result.get(i+3),result.get(i+4),Integer.parseInt(result.get(i+5)), result.get(i+6),result.get(i+7),result.get(i+8)));
		return DBSubmittion;
		
	}
	
	/** Getters & Setters */
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
	public String getisLate() {
		return isLate;
	}
	public String getGrade() {
		return Grade;
	}
	public String getFilePath() {
		return FilePath;
	}

}