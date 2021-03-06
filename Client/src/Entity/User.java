package Entity;
import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

/**User - User entity
 * @author Tal Asulin
 * */
public class User {
	
	/**ID - User ID*/
	private String ID;
	
	/**Name - User name*/
	private String Name;
	
	/**Address - User address*/
	private String Address;
	
	/**Phone - User phone*/
	private String Phone;
	
	/**Email - User Email */
	private String Email;
	
	/**Type - User type*/
	private String Type;
	
	public User() {
	}
	
	/**
	 * Get all user id's with same role.
	 * @param role Role to compare
	 * @return List of users with the same role
	 */
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
	
	/**Contractor*/
	public User(String iD, String name,String email, String address, String phone,String Type) {
		this.ID = iD;
		this.Name = name;
		this.Address=address;
		this.Email=email;
		this.Phone=phone;
		this.Type=Type;
	}
	
	
	/**getUserInfo () - getting user info
	 * @param ID - User ID
	 * */
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