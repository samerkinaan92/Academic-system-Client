package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;
/**
 * Class of Teaching Unit Entity
 * @author Or Cohen
 *
 */
public class TeachingUnit {

	private int TUName;
	
	/**
	 * get all teaching unit in ArrayList
	 * @return ArrayList<String> 
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getTeachingUnit(){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select TUName From teachingunit");
		
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
	/**
	 * get all teachers of Teaching Unit
	 * @param TU
	 * @return ArrayList<String>
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> getTeachers(String TU){
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "Select TeacherID From teachingunit_teacher WHERE TUName = '" + TU + "'");
		
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
	/** Getters & Setters */

	public int getTUName() {
		return TUName;
	}

	public void setTUName(int tUName) {
		TUName = tUName;
	}
	
	

}