package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class Semester {

	private int id;
	private String season;
	private int isCurr;
	private int year;
	
	
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