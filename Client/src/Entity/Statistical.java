package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

/**
 * Statistical Entity - use for generate a report by the principal.
 * @author Tal Asulin
 * */
public class Statistical {
	
	/**assignID  - assignment ID.*/
	private String assignID;
	
	/**assignName - assignment name.*/
	private String assignName;
	
	/**stdID - Student ID.*/
	private String stdID;
	
	/**grade - student grade in specific assignment.*/
	private String grade;
	
	/**teachID - teacher ID.*/
	private String teachID;
	
	/**teachName - Teacher name.*/
	private String teachName;
	
	/**semYear - Semester year.*/
	private String semYear;
	
	/**semName - Semester name.*/
	private String semName;
	
	/**courseName - Course name.*/
	private String courseName;
	
	/**courseID - Course ID.*/
	private String courseID;
	
	/**className - Class Name.*/
	private String className;
	
	
	
	/**Statistical() - Constructor for the entity.
	 * @param courID
	 * @parm courName
	 * @param semY
	 * @param semName
	 * @param grade
	 * */
	public Statistical (String courID, String courName, String semY, String semName ,String grade)
	{
		this.courseID=courID;
		this.courseName=courName;
		this.semYear=semY;
		this.semName=semName;
		this.grade=grade;
		
	}
	
	/**Statistical() - Constructor for the entity.
	 * @param className
	 * @param semY
	 * @param semName
	 * @param grade
	 * @param a,b,c
	 * */
	public Statistical (String className, String semY, String semName ,String grade,String a,String b,String c)
	{
		this.className=className;
		this.semYear=semY;
		this.semName=semName;
		this.grade=grade;
		
	}
	
	
	/**Statistical() - Constructor for the entity.
	 * @param teaName
	 * @param semY
	 * @param semName
	 * @param grade
	 * */
	public Statistical (String teaName, String semY, String semName ,String grade)
	{
		this.teachName=teaName;
		this.semYear=semY;
		this.semName=semName;
		this.grade=grade;
		
	}
	
	/**Statistical() - Constructor for the entity.
	 * @param semYear
	 * @param semName
	 * @param teacherID
	 * @param teaName
	 * @param grade
	 * @param courID
	 * */
	public Statistical (String semYear, String semName, String teacherID, String teacherName ,String grade, String courID)
	{
		this.semYear=semYear;
		this.semName=semName;
		this.teachID=teacherID;
		this.teachName=teacherName;
		this.grade=grade;
		this.courseID=courID;
		
	}
	
	
	
	/**Statistical() - Constructor for the entity.
	 * @param semY
	 * @param semT
	 */
	public Statistical(String semY, String semT)
	{
		this.semYear=semY;
		this.semName=semT;
	}
	
	
	
	
	/**getClasses() - 
	 * @return array list of all classes.*/
	public static ArrayList<String> getClasses(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "SELECT DISTINCT className FROM statistical;");

		ArrayList<String> res = sendMsg(msgServer);	
		return res;		
	}
	
	/**getSemListByClass()
	 * @param className
	 * @return array list of semesters by the class name.
	 * */
	public static ArrayList<Statistical> getSemListByClass(String className){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select distinct semYear,semName from statistical where className='"+className+"';");

		ArrayList<String> res = sendMsg(msgServer);	
		ArrayList<Statistical> courList = new ArrayList<Statistical>();
		for(int i =0; i< res.size() ; i+=2)
			courList.add(new Statistical(res.get(i),res.get(i+1)));
				
		return courList;	
	}
	
	/**
	 * getCoursesDetailsByClass()
	 * @param className
	 * @return array list of courses details by class name.
	 * */
	public static ArrayList<Statistical> getCoursesDetailsByClass(String className){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select courseID,courseName,semYear,semName,AVG(grade) from statistical where className='"+className+"' group by courseID,semYear");

		ArrayList<String> res = sendMsg(msgServer);	
		ArrayList<Statistical> courList = new ArrayList<Statistical>();
		for(int i =0; i< res.size() ; i+=5)
			courList.add(new Statistical(res.get(i),res.get(i+1),res.get(i+2),res.get(i+3),res.get(i+4)));
		
		
		return courList;		
	}
	
	/**getStatByClassName()
	 * @param className
	 * @return statistical tupels by class name
	 * */
	public static ArrayList<Statistical> getStatByClassName(String className){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select courseID,courseName,semYear,semName,AVG(grade) from statistical where className='"+className+"' group by courseName,semYear,semName");

		ArrayList<String> res = sendMsg(msgServer);	
		ArrayList<Statistical> courList = new ArrayList<Statistical>();
		for(int i =0; i< res.size() ; i+=5)
			courList.add(new Statistical(res.get(i),res.get(i+1),res.get(i+2),res.get(i+3),res.get(i+4)));
		
		
		return courList;		
	}
	
	
	/**
	 * getTeachStatByClassName()
	 * @param className
	 * @return array list of teachers by class name.
	 * */
	public static ArrayList<Statistical> getTeachStatByClassName(String className){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select teacherName,semYear,semName,AVG(grade) from statistical where className='"+className+"' group by semYear,semName,teacherID");

		ArrayList<String> res = sendMsg(msgServer);	
		ArrayList<Statistical> courList = new ArrayList<Statistical>();
		for(int i =0; i< res.size() ; i+=4)
			courList.add(new Statistical(res.get(i),res.get(i+1),res.get(i+2),res.get(i+3)));
		
		
		return courList;		
	}
	
	
	/**
	 * getTeach()
	 * @return array list of all teachers.
	 * */
	public static ArrayList<String> getTeach(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select teacherName from statistical group by teacherID");

		ArrayList<String> res = sendMsg(msgServer);	
		return res;		
	}
	
	
	/**
	 * getTeachByClass()
	 * @param className
	 * @return list of teachers by class name.
	 * */
	public static ArrayList<Statistical> getTeachByClass(String className){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select semYear,semName,teacherID,teacherName,AVG(grade),courseID from statistical where className='"+className+"' group by teacherID,semYear,semName");

		ArrayList<String> res = sendMsg(msgServer);	
		ArrayList<Statistical> courList = new ArrayList<Statistical>();
		for(int i =0; i< res.size() ; i+=6)
			courList.add(new Statistical(res.get(i),res.get(i+1),res.get(i+2),res.get(i+3),res.get(i+4),res.get(i+5)));
		
		
		return courList;		
	}
	
	
	/**
	 * getSemListByTeach()
	 * @param teachName
	 * @return array list of semesters by teacher name.
	 * 
	 * */
	public static ArrayList<Statistical> getSemListByTeach(String teachName){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select semYear,semName from statistical where teacherName='"+teachName+"' group by semYear,semName");

		ArrayList<String> res = sendMsg(msgServer);	
		ArrayList<Statistical> semL = new ArrayList<Statistical>();
		for(int i =0; i< res.size() ; i+=2)
			semL.add(new Statistical(res.get(i),res.get(i+1)));
		
		
		return semL;		
	}
	
	
	/**
	 * getClassesByTeach()
	 * @param teachName
	 * @return array list of classes by teacher.
	 * */
	public static ArrayList<Statistical> getClassesByTeach(String teachName){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select DISTINCT className,semYear,semName,avg(grade) from statistical where teacherName='"+teachName+"' group by className,semName,semYear");

		ArrayList<String> res = sendMsg(msgServer);	
		ArrayList<Statistical> semL = new ArrayList<Statistical>();
		for(int i =0; i< res.size() ; i+=4)
			semL.add(new Statistical(res.get(i),res.get(i+1),res.get(i+2),res.get(i+3),"","",""));
		
		
		return semL;		
	}
	
	/**
	 * sendMsg()
	 * @param msgServer
	 * @return answer from the server
	 * */
	@SuppressWarnings("unchecked")
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
		ArrayList<String> courseResult = (ArrayList<String>)Main.client.getMessage();
		if (courseResult == null)
			return null;
		return courseResult;
	}
	
	public String getAssignID() {
		return assignID;
	}


	public void setAssignID(String assignID) {
		this.assignID = assignID;
	}


	public String getAssignName() {
		return assignName;
	}


	public void setAssignName(String assignName) {
		this.assignName = assignName;
	}


	public String getStdID() {
		return stdID;
	}


	public void setStdID(String stdID) {
		this.stdID = stdID;
	}


	public String getGrade() {
		return grade;
	}


	public void setGrade(String grade) {
		this.grade = grade;
	}


	public String getTeachID() {
		return teachID;
	}


	public void setTeachID(String teachID) {
		this.teachID = teachID;
	}


	public String getTeachName() {
		return teachName;
	}


	public void setTeachName(String teachName) {
		this.teachName = teachName;
	}


	public String getSemYear() {
		return semYear;
	}


	public void setSemYear(String semYear) {
		this.semYear = semYear;
	}


	public String getSemName() {
		return semName;
	}


	public void setSemName(String semName) {
		this.semName = semName;
	}


	public String getCourseName() {
		return courseName;
	}


	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}


	public String getCourseID() {
		return courseID;
	}


	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}


	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}


	public Statistical( String className)
	{
		this.className=className;
		
	}
	
	
	

}
