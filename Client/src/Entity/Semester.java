package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

/**Semester - Semester entity
 * @author Tal Asulin
 * */
public class Semester {

	private int id;
	private String season;
	private int isCurr;
	private int year;
	
	/**
	 * Semester()
	 * @param id Semester id
	 * @param season Represents first or second semester
	 * @param isCurr 1 for current semester
	 * @param year year taken
	 */
	public Semester(int semID)
	{
		this.id=semID;		
	}
	
	
	public Semester(int id, String season, int isCurr, int year){
		this.setId(id);
		this.setSeason(season);
		this.setIsCurr(isCurr);
		this.setYear(year);
	}
	
	/**
	 * Get all semesters from data base
	 * @return list of all semesters
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Semester> getSemesters(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select * From semester");
		
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
		
		if (result.isEmpty()){
			return null;
		}
		
		ArrayList<Semester> DBsemester = new ArrayList<Semester>();
		
		for (int i = 0; i < result.size(); i+=4)
			DBsemester.add(new Semester(Integer.parseInt(result.get(i+3)), result.get(i), Integer.parseInt(result.get(i+2)), Integer.parseInt(result.get(i+1))));
		return DBsemester;
	}
	
	/**
	 * Get the current semester
	 * @return the year and type of the current semester
	 * */
	public static String currSem(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select 	semester.Year,semester.Season From semester WHERE isCurr='1';");
		ArrayList<String> res = sendMsg(msgServer);
		String semRes= res.get(0) + "-" + res.get(1);
		return semRes;
	}
	
	/**
	 * semID() - Return the semester ID
	 * @param year
	 * @param type
	 * @return semester ID
	 * */
	public static String semID(String year, String type){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select 	semesterId From semester WHERE Year='"+year+"' and season='"+type+"';");
		ArrayList<String> res = sendMsg(msgServer);

		return res.get(0);
	}
	
	/**
	 * Get instance of current semester
	 * @return  instance of current semester
	 */
	@SuppressWarnings("unchecked")
	public static Semester getCurrent(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select * From semester WHERE isCurr = 1");
		
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
		
		if (result.size() == 4)
			return new Semester(Integer.parseInt(result.get(3)), result.get(0), Integer.parseInt(result.get(2)), Integer.parseInt(result.get(1)));
		return null;
	}
	
	/*Return list of semesters*/
	public static ArrayList<String> semList(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select year,Season from semester");
			
		ArrayList<String> resault = sendMsg(msgServer);
		ArrayList<String> semArr = new ArrayList<String>();
		
		for(int i=0; i<resault.size() ; i+=2)
			semArr.add(resault.get(i)+"-"+resault.get(i+1));
		
		return semArr;
		
	}
	/**
	 * returns semester ID of Year and Season
	 * @param Year
	 * @param Season
	 * @return ArrayList<String>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getSemesterID(String Year, String Season){
		

		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select semesterID From semester Where Year = '"+Year+"' AND Season = '"+Season+"';");
		
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
	 * Send message to server
	 * @param msgServer The message to be send
	 * @return Answer from server
	 */
	
	/*Return list of semesters*/
	public static ArrayList<Semester> semListByTeachName(String teachName){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select distinct semesterId from class_course,users where users.ID = teacherID and users.Name='"+teachName+"'");

		ArrayList<String> res = sendMsg(msgServer);
		ArrayList<Semester> sem = new ArrayList<Semester>();
		
		for(int i=0; i<res.size() ; i++)			
			sem.add(new Semester(Integer.parseInt(res.get(i))));
		
		return sem;
			
		
	}
	
	public static ArrayList<Semester> semListByClass(String className){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select DISTINCT semesterid from class_course where classname='"+className+"';");

		ArrayList<String> res = sendMsg(msgServer);
		ArrayList<Semester> sem = new ArrayList<Semester>();
		
		for(int i=0; i<res.size() ; i++)			
			sem.add(new Semester(Integer.parseInt(res.get(i))));
		
		return sem;
			
		
	}
	
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
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public int getIsCurr() {
		return isCurr;
	}

	public void setIsCurr(int isCurr) {
		this.isCurr = isCurr;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
}