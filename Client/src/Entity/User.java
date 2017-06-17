package Entity;


import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class User {

	private String ID;

	// public String Name;

	protected String Name;

	
	public User() {
	}
	
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getUserIdByRole(String role){ // Get all user id's with same role.
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select ID From users WHERE Role = '" + role + "'");
		
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
		
		if (result.size() > 0)
			return result;
		return null;			
	}
	
	
	public User(String iD, String name) {
		ID = iD;
		Name = name;
	}
	public String getID() {
		return ID;
	}
	public String getName() {
		return Name;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setName(String name) {
		Name = name;
	}
}