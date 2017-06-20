package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Secretery extends User {

	private String ID;
	 
	/**
	 * Insert to data base in table newstudentassignment student id and course id, for principle to approve
	 * @param student_course List of student id's & course id's
	 */
	public static void sendExceptionStudents(ArrayList<String> student_course){
		
		for (int i = 0; i < student_course.size(); i+=2){
			String msg = "Insert INTO newstudentassignment (action, StudentID, CourseID)";
	    	String values = " VALUES ('assign', " + student_course.get(i) + ", " + student_course.get(i+1) + ")";
	    	
	    	HashMap <String,String> msgServer = new HashMap <String,String>();
	    	msgServer.put("msgType", "insert");
			msgServer.put("query", msg + values);
	    	
	    	try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					return;
				}
	    	synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException e) {
				return;
			}}
		}	
	}
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

}