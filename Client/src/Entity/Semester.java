package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Semester {

	private int id;
	private String season;
	private int isCurr;
	private int year;
	
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
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Semester> getSemesters(){
		

		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select * From semester");
		
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
		ArrayList<Semester> DBsemester = new ArrayList<Semester>();
		
		for (int i = 0; i < result.size(); i+=4)
			DBsemester.add(new Semester(Integer.parseInt(result.get(i+3)), result.get(i), Integer.parseInt(result.get(i+2)), Integer.parseInt(result.get(i+1))));
		return DBsemester;
	}
	
	
	public static String currSem(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select 	semesterid From semester WHERE isCurr='1';");
				
		return sendMsg(msgServer).get(0);	
	}
	
	
	@SuppressWarnings("unchecked")
	public static Semester getCurrent(){
		
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select * From semester WHERE isCurr = 1");
		
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
		
		if (result.size() == 4)
			return new Semester(Integer.parseInt(result.get(3)), result.get(0), Integer.parseInt(result.get(2)), Integer.parseInt(result.get(1)));
		return null;
		
		
	}
	
	
	/*Return list of semesters*/
	public static ArrayList<String> semList(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select 	semesterid From semester");
			
		return sendMsg(msgServer);
		
	}
	
	
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