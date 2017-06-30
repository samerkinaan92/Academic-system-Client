package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

/**ClassCourse - ClassCourse Entity presenting the many to many relationship
 * @author Tal Asulin
 * */
public class ClassCourse {
	
	/**
	 * className - Class name
	 * SemesterID - Semester ID
	 * courseID - Course ID
	 * teacherID - Teacher ID
	 * */
	
	private String className;
	private String SemesterID;
	private String courseID;
	private String teacherID;
	
	
	/**Constractor
	 * @param cName - Class name
	 * @param semID -Semester
	 * @param cID - Course ID
	 * @param teacID - Teacher ID
	 * */ 
	public ClassCourse(String cName, String semID, String cID, String teacID)
	{
		this.className=cName;
		this.SemesterID=semID;
		this.courseID=cID;
		this.teacherID=teacID;
	}
	
	
	/**getTeacherByCourse() - Static funcation for getting array list of teacher details by the parameters:
	 * @param courseID - course ID
	 * @param clasName - Class name
	 * */
	public static  ArrayList<Teacher> getTeacherByCourse(String courseID, String clasName){
		HashMap<String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select users.name from class_course,users,teacher where classname='"+clasName+"' and courseid='"+courseID+"' and users.id=class_course.teacherid and teacher.TeacherID=users.ID and class_course.teacherID=teacher.TeacherID;");
		ArrayList<String>result = sendMsg(msgServer);
		
		ArrayList<Teacher>courseArr = new ArrayList<Teacher>();
		
		for(int i=0 ; i<result.size() ; i++)
			courseArr.add(new Teacher(result.get(i)));		
		
		if(result.size()==0)
			courseArr=null;
		
		return courseArr;
		
	}
	
	
	
	/**sendMsg - Static function which send message to the server*/
	private static ArrayList<String> sendMsg(HashMap <String,String> msgServer){
		synchronized (Main.client){try {
			try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					System.out.println("Server fatal error!");
				}
			Main.client.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
		@SuppressWarnings("unchecked")
		ArrayList<String> courseResult = (ArrayList<String>)Main.client.getMessage();
		if (courseResult == null)
			return null;
		return courseResult;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSemesterID() {
		return SemesterID;
	}
	public void setSemesterID(String semesterID) {
		SemesterID = semesterID;
	}
	public String getCourseID() {
		return courseID;
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	public String getTeacherID() {
		return teacherID;
	}
	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}
	
}
