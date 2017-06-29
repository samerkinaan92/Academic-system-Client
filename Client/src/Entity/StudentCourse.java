package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;
/**
 * Class of Student
 * @author Or Cohen
 *
 */
public class StudentCourse {
	/** StudentCourse()
	 * @param courseID - Course ID
	 * @param teacherID- teacher ID
	 * @param TUname - Teaching Unit
	 * @param courseName - course Name
	 * @param weeklyHours - weekly Hours
	 * */
	private String courseID;
	private String teacherID;
	private String TUname;
	private String courseName;
	private String weeklyHours;
	
	public StudentCourse(String cID, String cName, String cWH, String cTUname, String cTeacherID)
	{
		this.setCourseID(cID);
		this.setCourseName(cName);
		this.setWeeklyHours(cWH);
		this.setTUname(cTUname);
		this.setTeacherID(cTeacherID);
	}

	/**
	 * get courses studied in semester
	 * @param sem
	 * @param stdID
	 * @return  ArrayList<StudentCourse>
	 */
	public static ArrayList<StudentCourse> getCoursesBySemStd(String sem, String stdID){
		HashMap<String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select course.courseid,CourseName,WeeklyHours,tuname,users.name from class_course,course,semester,course_student,users where class_course.CourseID=course.CourseID and class_course.semesterId=semester.semesterid and semester.semesterid='"+sem+"' and classname='A1' and semester.semesterid=course_student.semesterid and course.courseid=course_student.courseid and course_student.StudentID='"+stdID+"' and class_course.teacherID=users.ID;");
		ArrayList<String>result = sendMsg(msgServer);
		
		ArrayList<StudentCourse>courseArr = new ArrayList<StudentCourse>();
		
		for(int i=0 ; i<result.size() ; i+=5)
			courseArr.add(new StudentCourse(result.get(i),result.get(i+1),result.get(i+2),result.get(i+3),result.get(i+4)));
		return courseArr;
	}

	
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
	/** Getters & Setters */
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

	public String getTUname() {
		return TUname;
	}

	public void setTUname(String tUname) {
		TUname = tUname;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getWeeklyHours() {
		return weeklyHours;
	}

	public void setWeeklyHours(String weeklyHours) {
		this.weeklyHours = weeklyHours;
	}
	
}
