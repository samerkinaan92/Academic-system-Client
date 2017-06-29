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
	private String publishDateinString;
	private String deadLineString;
	
	/**
	 * Create new Assignment
	 * @param Aid Assignment ID
	 * @param name Assignment name
	 * @param Cid Course ID
	 * @param publish publish date of Assignment
	 * @param dead Dead Line date of Assignment
	 * @param path Path of the assignment file (server)
	 * @param Sid Semester id
	 */
	public Assignment(int Aid, String name, int Cid, Date publish, Date dead, String path, int Sid){
		assignmentID = Aid;
		AssignmentName = name;
		CourseID = Cid;
		publishDate = publish;
		deadLine = dead;
		filePath = path;
		semesterID = Sid;
	}
	/**
	 * Create new Assignment
	 * @param Aid Assignment ID
	 * @param name Assignment name
	 * @param Cid Course ID
	 * @param publish publish date of Assignment represented as String
	 * @param dead Dead Line date of Assignment represented as String
	 * @param path Path of the assignment file (server)
	 */
	public Assignment(int Aid, String name, int Cid, String publish, String dead, String path){
		assignmentID = Aid;
		AssignmentName = name;
		CourseID = Cid;
		publishDateinString = publish;
		deadLineString = dead;
		setFilePath(path);
	}
	/**
	 * Create new Assignment
	 * @param Aid Assignment ID
	 * @param name Assignment name
	 * @param Cid Course ID
	 * @param publish publish date of Assignment represented as String
	 * @param dead Dead Line date of Assignment represented as String
	 */
	public Assignment(int Aid, String name, int Cid, String publish, String dead){
		assignmentID = Aid;
		AssignmentName = name;
		CourseID = Cid;
		publishDateinString = publish;
		deadLineString = dead;
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

	@SuppressWarnings("unchecked")
	public static ArrayList<Assignment> getAssignments(String CourseID, String SemesterId){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select AssignmentID, AssignmentName,CourseID, publishDate, deadLine,filePath from assignment where CourseID = '"+CourseID+"' and semesterId = '"+SemesterId+"';");
		
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
		ArrayList<Assignment> DBAssignment = new ArrayList<Assignment>();
		
		for (int i = 0; i < result.size(); i+=6)
			DBAssignment.add(new Assignment(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), result.get(i+3),result.get(i+4),result.get(i+5) ));
		return DBAssignment;
	}

	/**
	 * get all assignments of specific Stdent
	 * @param StudentId
	 * @return ArrayList<String>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getAssignmentsOfStudent(String StudentId){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select assignment.AssignmentID from assignment, submission Where assignment.AssignmentID = submission.AssignmentID and submission.StudentStudentID = '"+StudentId+"';");
		
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
	/**
	 * get all information on specific Assignment
	 * @param AssID
	 * @return ArrayList<Assignment>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Assignment> getAssignments(String AssID){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select AssignmentID, AssignmentName,CourseID, publishDate, deadLine,filePath from assignment where AssignmentID = '"+AssID+"';");
		
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
		ArrayList<Assignment> DBAssignment = new ArrayList<Assignment>();
		
		for (int i = 0; i < result.size(); i+=6)
			DBAssignment.add(new Assignment(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), result.get(i+3),result.get(i+4),result.get(i+5) ));
		return DBAssignment;
	}
	
	/**
	 * get all assignments of student in a course
	 * @param sem
	 * @param courseID
	 * @param stdID
	 * @return ArrayList<String>
	 */
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getAssignByCourse(String sem , String courseID, String stdID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select assignment.AssignmentID from submission,assignment where submission.AssignmentID=assignment.AssignmentID and semesterId='"+sem+"' and CourseID='"+courseID+"' and submission.StudentStudentID='"+stdID+"';");
		
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
	 * get course name by ID
	 * @param assignID
	 * @return ArrayList<String>
	 */
	@SuppressWarnings("unchecked")
	public static  ArrayList<String>  getCourNameID(String assignID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select  distinct assignment.CourseID,CourseName from assignment,course_student,submission,course where course_student.CourseID = assignment.CourseID and course_student.StudentID = submission.StudentStudentID and submission.AssignmentID = assignment.AssignmentID and assignment.CourseID = course.CourseID and assignment.AssignmentID='"+assignID+"';");
		
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
	
	
	
	/**
	 * show all information of selected assignment id
	 * @param assignID
	 * @return ArrayList<String>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getAssignDetails(String assignID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select distinct assignment.AssignmentName,semester.year, semester.Season from assignment,semester where assignment.semesterId = semester.semesterId and assignment.AssignmentID='"+assignID+"';");
		
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
	/**
	 * return Class name of student
	 * @param studentID
	 * @return String
	 */
	
	@SuppressWarnings("unchecked")
	public static String getClassName(String studentID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select ClassName from student_class where StudentID='"+studentID+"';");
		
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
		
		return result.get(0);

		
	}
	
	/** Getters & Setters */
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


	public String getDeadLineAsString(){
		return deadLineString;
	}
	
	public String getPublishDateAsString(){
		return publishDateinString;
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