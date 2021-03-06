package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;
/**
 * Class of Teacher Entity
 * @author Or Cohen
 *
 */
public class Teacher extends User {
/** maximum work hours allowed to teacher */
	private int maxWorkHours;
/**
 * constructor1
 * @param name
 */
	public Teacher(String name){
		super(name);
	}
	/**
	 * constructor2
	 * @param name - teacher name
	 * @param id - teacher id
	 * @param hours - teacher maximum work hours
	 */
	public Teacher(String name, String id, int hours){
		super(id, name);
		maxWorkHours = hours;
	}
	/**
	 * constructor3
	 * @param name - teacher name
	 * @param id - teacher id
	 * @param phoneNum - teacher phone number
	 * @param email - teacher email
	 * @param address - teacher address
	 * @param hours - teacher maximum work hours
	 */
	public Teacher(String name, String id,String phoneNum ,String email,String address,int hours ){
		super.setID(id);
		super.setName(name);
		super.setPhone(phoneNum);
		super.setEmail(email);
		super.setAddress(address);
		maxWorkHours = hours;
}
	
	/**
	 * returns selected teacher detail
	 * @param ID
	 * @return String
	 */
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
	
	
	/**
	 * get all teachers names
	 * @return ArrayList<String>
	 */
	@SuppressWarnings({ "unchecked", "null" })
	public static ArrayList<String> getTeachersName(){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select Name From users WHERE Role = 'Teacher';");
		
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
		}
		}
		
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
		ArrayList<String> teachArray = new ArrayList<String>();
		
		
		for(int i = 0 ; i< result.size() ; i++)
			teachArray.add(result.get(i));
		
		return teachArray;
	}
	
	
	
	/**
	 * get all courses teacher teaching
	 * @param ID
	 * @return ArrayList<String> 
	 */
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
	 * get all teachers teaching in a semester
	 * @param semesterID
	 * @return ArrayList<Teacher>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Teacher> getTeachersInSemester(String semesterID){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", " Select distinct users.Name, users.ID, users.phoneNum, users.email, users.address From users,class_course WHERE users.ID = class_course.teacherID AND class_course.semesterId = '"+semesterID+"';");
		//, phoneNum, email, address
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
		}
		}
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
		msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select distinct teacher.TeacherID, teacher.MaxWorkHours From class_course, teacher, users WHERE teacher.TeacherID = class_course.teacherID AND class_course.semesterId = '"+semesterID+"';");
		//Select distinct teacher.TeacherID, teacher.MaxWorkHours From class_course, teacher, users WHERE users.ID = class_course.teacherID AND class_course.semesterId = '"+semesterID+"';
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
		ArrayList<String> result2 = (ArrayList<String>)Main.client.getMessage();
		
		for (int i = 1; i < result.size(); i+=5)//2
			for (int j = 0; j < result2.size(); j+=2)//2
				if (result.get(i).equals(result2.get(j))){
					result.add(i+4, result2.get(j+1));
					i++;
					break;
				}
		
		ArrayList<Teacher> DBteachers = new ArrayList<Teacher>();
		
		for (int i = 0; i < result.size(); i+=6)//3
			DBteachers.add(new Teacher(result.get(i), result.get(i+1), result.get(i+2) ,result.get(i+3) ,result.get(i+4) ,Integer.parseInt(result.get(i+5))));
		//, result.get(i+2) ,result.get(i+3) ,result.get(i+4) ,
		return DBteachers;

}
	/**
	 * get all classes names that teached by a teacher
	 * @param ID
	 * @return ArrayList<String> 
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getClassesOfTeacher(String ID){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select  ClassName From class_course WHERE teacherID = '"+ID+"';");
		
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
	 * get all teaching units of a teacher
	 * @param ID
	 * @return ArrayList<String> 
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getTUOfTeacher(String ID){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select distinct TUName From teachingunit_teacher WHERE teacherID = '"+ID+"';");
		
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
	 * get all courses that a teacher teach in a semester
	 * @param teacherID
	 * @param semesterId
	 * @return ArrayList<String>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getCoursesOfTeacherInSemester(String teacherID , String semesterId){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select  CourseID From class_course WHERE teacherID = '"+teacherID+"' AND semesterId = '"+semesterId+"';");
		
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
		ArrayList<String> res1 = (ArrayList<String>)Main.client.getMessage();
		return res1;
}
	/**
	 * get classes that a teacher teach in a semester
	 * @param teacherID
	 * @param semesterId
	 * @return ArrayList<String>
	 */
	public static ArrayList<String> getClassesOfTeacherInSemester(String teacherID , String semesterId){ // Get list of courses.
		HashMap <String,String> msgServer2 = new HashMap <String,String>();
		msgServer2.put("msgType", "select");
		msgServer2.put("query", "Select  ClassName From class_course WHERE teacherID = '"+teacherID+"' AND semesterId = '"+semesterId+"';");
		
		try{
			Main.client.sendMessageToServer(msgServer2);
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
	 * 
	 * @param ID
	 * @return ArrayList<String>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getSemesterOfCoursesOfTeacher(String ID){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select  semesterId From class_course WHERE teacherID = '"+ID+"';");
		
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
	 * Get all teachers from data base
	 * @return List of all teachers
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Teacher> getAllTeachers(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", " Select users.Name, users.ID, users.phoneNum, users.email, users.address From users WHERE users.Role = 'Teacher' ;");
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
		msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		
		
		msgServer.put("query", "Select teacher.TeacherID, teacher.MaxWorkHours From teacher;");
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
		boolean flag = false;
		for (int i = 1; i < result.size(); i+=5, flag = false){
			for (int j = 0; j < result2.size(); j+=2){
				if (result.get(i).equals(result2.get(j))){
					result.add(i+4, result2.get(j+1));
					i++;
					flag = true;
					break;
				}
			}
			if (!flag){
				result.add(i+4, "0");
				i++;
			}
		}
		ArrayList<Teacher> DBteachers = new ArrayList<Teacher>();
		
		for (int i = 0; i < result.size(); i+=6)//3
			DBteachers.add(new Teacher(result.get(i), result.get(i+1), result.get(i+2) ,result.get(i+3) ,result.get(i+4) ,Integer.parseInt(result.get(i+5))));
		//, result.get(i+2) ,result.get(i+3) ,result.get(i+4) ,
		return DBteachers;
		
	}
	
	/**
	 * Get all teachers from data base
	 * @return List of all teachers
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Teacher> getTeachers(){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", " Select distinct users.Name, users.ID, users.phoneNum, users.email, users.address From users,class_course WHERE users.ID = class_course.teacherID ;");
		//, phoneNum, email, address
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
		}
		}
		ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
		msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select distinct teacher.TeacherID, teacher.MaxWorkHours From class_course, teacher, users WHERE teacher.TeacherID = class_course.teacherID ;");
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
		ArrayList<String> result2 = (ArrayList<String>)Main.client.getMessage();
		
		for (int i = 1; i < result.size(); i+=5)//2
			for (int j = 0; j < result2.size(); j+=2)//2
				if (result.get(i).equals(result2.get(j))){
					result.add(i+4, result2.get(j+1));
					i++;
					break;
				}
		
		ArrayList<Teacher> DBteachers = new ArrayList<Teacher>();
		
		for (int i = 0; i < result.size(); i+=6)//3
			DBteachers.add(new Teacher(result.get(i), result.get(i+1), result.get(i+2) ,result.get(i+3) ,result.get(i+4) ,Integer.parseInt(result.get(i+5))));
		//, result.get(i+2) ,result.get(i+3) ,result.get(i+4) ,
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
	 * 
	 * @param ID
	 * @return ArrayList<String>
	 */
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



	/**
	 * get classes thouth by teacher 
	 * @param ID
	 * @return
	 */

public static ArrayList<claSS> getTeachersClass(String ID){
	
	HashMap <String,String> msgServer = new HashMap <String,String>();
	msgServer.put("msgType", "select");
	msgServer.put("query", " select ClassName from class_course where teacherID ="+ID+";");
	
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
	@SuppressWarnings("unchecked")
	ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
	
	ArrayList<claSS> DBclasses = new ArrayList<claSS>();
	for (int i = 0; i < result.size(); i++)
		DBclasses.add(new claSS(result.get(i)));
	return DBclasses;
	
}


/**
 * get teacher that published Assignment
 * @param assign
 * @param std
 * @param sem
 * @param courseName
 * @return String 
 */
public static String getTeachersIDbyAssign(String assign, String std,String sem,String courseName){
	
	HashMap <String,String> msgServer = new HashMap <String,String>();
	msgServer.put("msgType", "select");
	msgServer.put("query", "select teacherid from submission,assignment,class_course,student_class,course where assignment.AssignmentID=submission.AssignmentID and class_course.CourseID=assignment.CourseID and StudentStudentID='"+std+"' and StudentStudentID=studentid and class_course.classname=student_class.ClassName and assignment.AssignmentID='"+assign+"' and class_course.semesterId='"+sem+"' and class_course.CourseID=assignment.CourseID and assignment.CourseID=course.courseid and coursename='"+courseName+"';");

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
	@SuppressWarnings("unchecked")
	ArrayList<String> result = (ArrayList<String>)Main.client.getMessage();
	return result.get(0);
	
}
/** Getters & Setters */
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