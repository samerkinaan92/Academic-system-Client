package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Course {


	private int CourseID;
	private String Name;
	private String TUID;
	private int weeklyHours;
	
	/**
	 * Create new course
	 * @param CourseID Course Id
	 * @param Name Course name
	 * @param weeklyHours Course weekly hours
	 * @param TUID Course teaching unit id
	 */
	public Course(int CourseID, String Name, int weeklyHours, String TUID){
		this.CourseID = CourseID;
		this.Name = Name;
		this.TUID = TUID;
		this.weeklyHours = weeklyHours;
	}
	
	/**
	 * Filter from array courses that different from current semester
	 * @param courses list of courses to filter from
	 * @return The filtered list
	 */
	public Course(int courseID, String courName)
	{
		this.CourseID=courseID;
		this.Name=courName;
		
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Course> filterOldCourses(ArrayList<Course> courses){
		
		if (courses == null)
			return null;
		if (courses.isEmpty())
			return null;
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From mat.course_student WHERE course_student.StudentID = " + Main.user.getID() 
			+ " AND course_student.semesterId = " + Semester.getCurrent().getId() + ";");
		
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
		
		for (int i = courses.size() - 1; i >= 0 ; i--){
			boolean flag = false;
			for (int j = 0; j < result.size(); j++){
				if (String.valueOf(courses.get(i).getCourseID()).equals(result.get(j))){
					flag = true;
					break;
				}
			}
			
			if (!flag){
				courses.remove(i);
			}
		}
		return courses;
	}
	
	/**
	 * Get list of all defined courses in data base.
	 * @return Array of all courses
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Course> getCourses(){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID,CourseName, weeklyHours, TUName From course");
		
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
		ArrayList<Course> DBcourses = new ArrayList<Course>();
		
		for (int i = 0; i < result.size(); i+=4)
			DBcourses.add(new Course(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), result.get(i+3)));
		return DBcourses;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Course> getCoursesofClass(String ClassName, String semester){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select  course.CourseID, course.CourseName, course.weeklyHours, course.TUName  from course, class_course where class_course.CourseID = course.CourseID and class_course.semesterId = '"+semester+"' and class_course.ClassName = '"+ClassName+"';");
		// select  distinct course.CourseID from course, class_course where class_course.CourseID = course.CourseID and class_course.semesterId = '"+4+"';
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
		ArrayList<Course> DBcourses = new ArrayList<Course>();
		
		for (int i = 0; i < result.size(); i+=4)
			DBcourses.add(new Course(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), result.get(i+3)));
		return DBcourses;
	}
	/**
	 * return all courses in semester
	 * @param semester
	 * @return ArrayList<Course>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Course> getCoursesofSemester(String semester){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select  distinct course.CourseID, course.CourseName, course.weeklyHours, course.TUName  from course, class_course where class_course.CourseID = course.CourseID and class_course.semesterId = '"+semester+"';");
		// select  distinct course.CourseID from course, class_course where class_course.CourseID = course.CourseID and class_course.semesterId = '"+4+"';
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
		ArrayList<Course> DBcourses = new ArrayList<Course>();
		
		for (int i = 0; i < result.size(); i+=4)
			DBcourses.add(new Course(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), result.get(i+3)));
		return DBcourses;
	}
	/**
	 * return all pre-courses of a course
	 * @param ID
	 * @return ArrayList<String>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getPreCoursesId(String ID){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select preCourseID From pre_courses Where CourseID = '"+ID+"';");
		
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
	 * Get all pre courses for a specific course
	 * @param cID Course id to check all pre courses
	 * @return list of all pre courses
	 */
	public static String getCourseIDbyName(String CoursName){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From course where CourseName='"+CoursName+"';");
		
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
	/**
	 * returns all courses of teaching unit
	 * @param teachingUnit
	 * @return ArrayList<Course>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Course> getCourses(String teachingUnit){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID,CourseName, weeklyHours, TUName From course where TUName = '" + teachingUnit + "'");
		
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
		ArrayList<Course> DBcourses = new ArrayList<Course>();
		
		for (int i = 0; i < result.size(); i+=4)
			DBcourses.add(new Course(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), result.get(i+3)));
		return DBcourses;
	}
	/**
	 * returns all courses of teaching unit
	 * @param teachingUnit
	 * @return ArrayList<Course>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Course> getCoursesByTU( String TU , String semester){ // Get list of courses.
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select  distinct course.CourseID, course.CourseName, course.weeklyHours, course.TUName "
				+ "From course,class_course WHERE course.TUName = '"+TU+"' and class_course.semesterId = '"+semester+"' and course.CourseID = class_course.CourseID;");
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
		ArrayList<Course> DBcourses = new ArrayList<Course>();
		
		for (int i = 0; i < result.size(); i+=4)
			DBcourses.add(new Course(Integer.parseInt(result.get(i)), result.get(i+1), Integer.parseInt(result.get(i+2)), result.get(i+3)));
		return DBcourses;
	}
	
	
	/**
	 * gets courses id taken by class
	 * 
	 * @param classRoom The class room you want the courses taken by
	 * @param semId The semester id of the desired semester
	 * @return the ids of the courses taken by class in semester
	 * @throws InterruptedException
	 */
	public static ArrayList<String> getCoursesTakenByClass(String classRoom, String semId) throws InterruptedException{
		HashMap <String,String> msgServer = new HashMap <String,String>();
		ArrayList<String> msgFromServer = null;
		
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From Class_Course where semesterId = " + semId);
		
		synchronized (Main.client) {
			Main.client.sendMessageToServer(msgServer);
			Main.client.wait();
			msgFromServer = (ArrayList<String>) Main.client.getMessage();
		}
		
		return msgFromServer;
	}
	/**
	 * 
	 * @param cID
	 * @return ArrayList<String> 
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getPreCourses(String cID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select preCourseID From pre_courses WHERE CourseID = " + cID);
		
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
		
		if (result.size() > 0)
			return result;
		return null;	
	}
	
	/**
	 * Get course name by course id
	 * @param ID Course id
	 * @return The course name
	 */
	@SuppressWarnings("unchecked")
	public static String getCourseName(String ID){ // Get list of courses.
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseName From course WHERE CourseID ='"+ID+"';");
		
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
		
		if (result.size() == 1){
			String res = result.get(0);
			return res;
		}
		
		return null;
	}
	
	/**
	 * Insert new course to data base
	 * @return If succeed
	 */
	public boolean insertCourse(){ // Insert course to data base.
		
		String msg = "Insert INTO course (CourseID, CourseName, weeklyHours, TUName)";
    	String values = " VALUES (" + CourseID + ", '" + Name + "', " + 
    			 weeklyHours + ", '" + TUID + "')";
    	
    	HashMap <String,String> msgServer = new HashMap <String,String>();
    	msgServer.put("msgType", "insert");
		msgServer.put("query", msg + values);
    	
    	try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				return false;
			}
    	synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			return false;
		}}
		
		return true;
	}
	
	/**
	 * Update all PreCourses for Course in data base.
	 * @param PreList All pre courses to update
	 * @return If succeed
	 */
	public boolean updatePreCourses(ArrayList<String> PreList){ // Update PreCourses for Course in data base.
		
		for (int i = 0; i < PreList.size(); i++){
		
			String msg = "Insert INTO pre_courses (CourseID, preCourseID)";
	    	String values = " VALUES (" + CourseID + ", " + 
	    	PreList.get(i).substring(PreList.get(i).indexOf('(') + 1, PreList.get(i).indexOf(')')) + ")";
	    	
	    	HashMap <String,String> msgServer = new HashMap <String,String>();
	    	msgServer.put("msgType", "insert");
			msgServer.put("query", msg + values);
	    	
	    	try{
				Main.client.sendMessageToServer(msgServer);
				}
				catch(Exception exp){
					return false;
				}
	    	synchronized (Main.client){try {
				Main.client.wait();
			} catch (InterruptedException e) {
				return false;
			}}
		}
		return true;
	}
		
	/**
	 * Check if Course ID exist in data base.
	 * @param CourseID Course id to check
	 * @return If exists
	 */
	@SuppressWarnings("unchecked")
	public static boolean checkLegal(String CourseID){ // Check if Course ID exist in data base.
		HashMap<String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select CourseID From course");
		
		try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				return false;
			}
		synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			return false;
		}}
		
		ArrayList<String>	result = (ArrayList<String>)Main.client.getMessage();
		
		for (int i = 0; i < result.size(); i++)
			if (result.get(i).equals(CourseID))
				return false;
		return true;
	}
	
	/**
	 * Get the teaching unit of a specific course.
	 * @param coursID Course id to check
	 * @return Teaching unit name
	 */
	@SuppressWarnings("unchecked")
	public static String getTU(String coursID){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select TUName From course Where CourseID = '" + coursID + "'");
		
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
		
		if (result.size() > 0)
			return result.get(0);
		else
			return null;
	}
	
	public static ArrayList<Course> getCoursesBySemStd(String sem, String stdID){
		HashMap<String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select course.courseid,CourseName,WeeklyHours,tuname from semester,course_student,course where semester.semesterid=course_student.semesterid and semester.semesterid='"+sem+"' and course.courseid=course_student.courseid and studentid='"+stdID+"';");
		ArrayList<String>result = sendMsg(msgServer);
		
		ArrayList<Course>courseArr = new ArrayList<Course>();
		
		for(int i=0 ; i<result.size() ; i+=4)
			courseArr.add(new Course(Integer.parseInt(result.get(i)),result.get(i+1),Integer.parseInt(result.get(i+2)),result.get(i+3)));
		return courseArr;
	}
	
	/**
	 * Send message to server
	 * @param msgServer The message to be send
	 * @return Answer from server
	 */
	@SuppressWarnings("unchecked")	
	public static ArrayList<Course> getCoursesByTech(String teachName){
		HashMap<String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select Distinct class_course.CourseID, course.CourseName  from teachingunit_teacher,class_course,course,users where teachingunit_teacher.TUName = course.TUName and class_course.CourseID = course.CourseID and teachingunit_teacher.teacherid = class_course.teacherID and users.id=teachingunit_teacher.teacherid and users.name='"+teachName+"';");
		ArrayList<String>result = sendMsg(msgServer);
		
		ArrayList<Course>courseArr = new ArrayList<Course>();
		
		for(int i=0 ; i<result.size() ; i+=2)
			courseArr.add(new Course(Integer.parseInt(result.get(i)),result.get(i+1)));
		return courseArr;
	}
	
	/**
	 * returns courses studied by class
	 * @param className
	 * @return ArrayList<Course>
	 */
	public static ArrayList<Course> getCoursesByClass(String className){
		HashMap<String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select class_course.courseid,course.coursename from class_course,course where class_course.CourseID=course.CourseID AND className='"+className+"';");
		ArrayList<String>result = sendMsg(msgServer);
		
		ArrayList<Course>courseArr = new ArrayList<Course>();
		
		for(int i=0 ; i<result.size() ; i+=2)
			courseArr.add(new Course(Integer.parseInt(result.get(i)),result.get(i+1)));
		return courseArr;
	}
	
	
	private static ArrayList<String> sendMsg(HashMap <String,String> msgServer){
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
		
		ArrayList<String> courseResult = (ArrayList<String>)Main.client.getMessage();
		if (courseResult == null)
			return null;
		return courseResult;
	}

	@Override
	public String toString() {	
		return getName() + " (" + getCourseID() + ")";
	}

	/** Getters & Setters */
	 
	public int getCourseID() {
		return CourseID;
	}
	public String getName() {
		return Name;
	}
	public String getTUID() {
		return TUID;
	}
	public int getWeeklyHours() {
		return weeklyHours;
	}
	
	public void setCourseID(int courseID) {
		CourseID = courseID;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setTUID(String tUID) {
		TUID = tUID;
	}
	public void setWeeklyHours(int weeklyHours) {
		this.weeklyHours = weeklyHours;
	}
}