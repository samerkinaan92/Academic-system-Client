package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;
/**	User - Entity of users.*/
public class Parent extends User {

	/**isBlocked - indicate if the parent is blocked*/
	private boolean isBlocked;
	
	private String ID;
	
	/**Parent()
	 * @param id - Parent ID
	 * @param name- Parent Name
	 * @param email - Parent email
	 * @param phone - Parent phone
	 * @param address - Parent address
	 * @param isBlocked - Parent blocked flag
	 * */
	public Parent(String id, String name, String email, String phone, String address, String isBlocked ) {
		setID(id);
		super.setName(name);
		super.setEmail(email);
		super.setPhone(phone);
		super.setAddress(address);
		if(isBlocked.equals("0"))
		this.isBlocked = false;
		else this.isBlocked = true;
	}
	
	/**Parent()
	 * @param id - Parent ID
	 * @param name- Parent Name
	 * */
	public Parent(String ID, String name){
		
		super(ID,name);
	}
	
	/**Parent()
	 * @param id - Parent ID
	 * @param name- Parent Name
	 * @param email - Parent email
	 * @param phone - Parent phone
	 * @param address - Parent address
	 * */
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
	
	
	/**getParNameByStdID() - get list of parents ID by student ID
	 * @param ID - Student ID
	 * */
	public static ArrayList<String> getParNameByStdID(String ID)
	{
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select Name from parent_student,users where ParentUserID=users.id AND StudentUserID='"+ID+"';");
		ArrayList<String> result = sendMsg(msgServer);		
		return result;
		 
	}
	
	
	/**getParentsOfStudent() - get Parent array list by student ID.
	 * @param StudentID - Student ID
	 * */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getParentsOfStudent(String StudentID){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select parent_student.ParentUserID, users.Name, users.email ,users.phoneNum, users.address, users.isBlocked ,parent_student.StudentUserID from parent_student, users  Where users.ID = parent_student.ParentUserID and StudentUserID = '"+StudentID+"';	");	//, phoneNum, email, address
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
		ArrayList<Parent> DBparents = new ArrayList<Parent>();
		
		for (int i = 0; i < result.size(); i+=7)//3
			DBparents.add(new Parent(result.get(i), result.get(i+1), result.get(i+2) ,result.get(i+3) ,result.get(i+4) ,result.get(i+5)));
		return result;

	}
	
	
	/**getParentsClass() - return Parent array list
	 * @param ClassName - Class name
	 * */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getParentsClass(String ClassName){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select ParentUserID, users.Name, users.email ,users.phoneNum, users.address, users.isBlocked ,parent_student.StudentUserID from parent_student, users  Where users.ID = parent_student.ParentUserID and StudentUserID IN (select StudentID from student_class where student_class.ClassName = '"+ClassName+"') ;");     		//, phoneNum, email, address
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
		ArrayList<Parent> DBparents = new ArrayList<Parent>();
		
		for (int i = 0; i < result.size(); i+=7)//3
		{
			
			DBparents.add(new Parent(result.get(i), result.get(i+1), result.get(i+2) ,result.get(i+3) ,result.get(i+4) ,result.get(i+5)));
		}

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
	
/** Getters & Setters */
	
	public String getIsBlockedStr(){
		if (isBlocked == true)
			return "1";
		else return "0";
		
	}
	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	
}