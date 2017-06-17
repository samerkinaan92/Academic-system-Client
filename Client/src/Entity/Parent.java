package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;
/**	User - Entity of users.*/
public class Parent extends User {

	
	public Parent(String ID, String name){
		
		super(ID,name);
	}
	

	public Parent(String ID, String name,String mail, String address, String phone){
		
		super(ID,name,mail,address,phone,"Parent");
	}
	
	
	/** getParByID() - returning a Parent Object of specific student
	 * ID - Student ID.
	 * */
	public static Parent getParByID(String ID)
	{
		Parent par;
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select ID,Name,email,address,phoneNum from users where ID='"+ID+"';");
		
		ArrayList<String> result = sendMsg(msgServer);		
		par= new Parent(result.get(0),result.get(1),result.get(2),result.get(3),result.get(4));		
		
		return par;
	}
	
	public static ArrayList<String> getParNameByStdID(String ID)
	{
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select Name from parent_student,users where ParentUserID=users.id AND StudentUserID='"+ID+"';");
		ArrayList<String> result = sendMsg(msgServer);		
		return result;
		 
	}
	
	/**sendMsg -Send message to the server*/
	@SuppressWarnings("unchecked")
	public static ArrayList<String> sendMsg(HashMap <String,String> msgServer){
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
		ArrayList<String> courseResult = (ArrayList<String>)Main.client.getMessage();
		if (courseResult == null)
			return null;
		return courseResult;
	}
	
	
	
	

	
}