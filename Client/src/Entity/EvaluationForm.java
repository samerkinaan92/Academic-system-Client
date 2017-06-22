package Entity;

import java.util.ArrayList;
import java.util.HashMap;
import application.Main;


/**
 * EvaluationForm - Entity which contain the student evaluation for the assignment.
 * */
public class EvaluationForm {

	
	/**
	 * grade - Assignment grade.*/
	private String grade;
	
	/**
	 * filePath - the file path in the server.
	 * */
	private String filePath;
	
	/**
	 * assignID - Assignment ID.
	 * */
	private String assignID;
	
	/**
	 * stdID - Student iD.
	 * */
	private String stdID;
	
	/**
	 * comments - Evaluation comments.
	 * */
	private String comments;
	
	/**
	 * isLate - Define if the assignment was late in submission.
	 * */
	private String isLate;
	
	/**EvaluationForm() - constructor for the EvaluationForm class*/
	public EvaluationForm(String assID, String stdID, String fileP, String isL, String grade, String ev)
	{
		this.assignID=assID;
		this.stdID=stdID;
		this.filePath=fileP;
		this.isLate=isL;
		this.grade=grade;
		this.comments=ev;
		
	}
	
	
	/**
	 * getEvaluByStdAssign()
	 * @param stdID, assignID
	 * @return the Evaluation Object by student ID and assignment ID
	 * */
	public static EvaluationForm getEvaluByStdAssign(String stdId, String assignID){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select * from submission where StudentStudentID='"+stdId+"' and AssignmentID='"+assignID+"';");
		
		ArrayList<String> result = sendMsg(msgServer);
	
		EvaluationForm stdEv = new EvaluationForm(result.get(0),result.get(1),result.get(2),result.get(3),result.get(4),result.get(5));
		return stdEv;
	
	}
	
	
	/**sendMsg() - send message to the server and return array list of the answer.*/
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getStdID() {
		return stdID;
	}

	public void setStdID(String stdID) {
		this.stdID = stdID;
	}

	public String getAssignID() {
		return assignID;
	}

	public void setAssignID(String assignID) {
		this.assignID = assignID;
	}


	public String getIsLate() {
		return isLate;
	}


	public void setIsLate(String isLate) {
		this.isLate = isLate;
	}

}