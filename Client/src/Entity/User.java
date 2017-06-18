package Entity;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

public class User {

	private String ID;
	private String Name;
	private String Address;
	private String Phone;
	private String Email;
	private String Type;
	
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
	
	public User(String Name)
	{
		this.Name=Name;
	}
	
	public User(String iD, String name,String email, String address, String phone,String Type) {
		this.ID = iD;
		this.Name = name;
		this.Address=address;
		this.Email=email;
		this.Phone=phone;
		this.Type=Type;
	}
	
	public static User getUserInfo(String ID) {
		User usr;
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select ID,Name,email,address,phoneNum,Role from users where id='"+ID+"';");
		
		ArrayList<String> result = sendMsg2(msgServer);
		usr = new User(result.get(0),result.get(1),result.get(2),result.get(3),result.get(4),result.get(5));
		
		return usr;
		
	}
	
	
	
	/**sendMsg -Send message to the server*/
	@SuppressWarnings("unchecked")
	public static ArrayList<String> sendMsg2(HashMap <String,String> msgServer){
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

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}
}