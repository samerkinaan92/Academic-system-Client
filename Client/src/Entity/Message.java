package Entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import application.Main;

/**Message Entity
 * @author Tal Asulin
 * */
public class Message {
	
	private int ID;
	private Date sendTime;
	private String title;
	private String msg;
	private int from;
	private int to;
	
	/**
	 * For display an existing message from DB.
	 * @param ID Message id
	 * @param sendTime Date & Time of creation
	 * @param title Message title
	 * @param msg Message content
	 * @param from Sender user id
	 * @param to recipient user id
	 */
	public Message(int ID, Date sendTime, String title, String msg, int from, int to){ // For display.
		this.ID = ID;
		this.sendTime = sendTime;
		this.title = title;
		this.msg = msg;
		this.from = from;
		this.to = to;
	}
	/**
	 * For create a new message to send.
	 * @param title Message title
	 * @param msg Message content
	 * @param from Sender user id
	 * @param to recipient user id
	 */
	public Message(String title, String msg, int from, int to){ // For create.
		this.title = title;
		this.msg = msg;
		this.from = from;
		this.to = to;
	}
	
	/**
	 * Get all user undeleted messages from DB.
	 * @param userID Id of the user to get all messages.
	 * @return list of all users messages
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<Message> getUserMessages(String userID){ // Get all user undeleted messages.
		
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "SELECT * FROM mat.messages where messages.to = " + userID + ";");
		
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
		ArrayList<Message> messages = new ArrayList<Message>();
		
		for (int i = 0; i < result.size(); i+=6){
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = (Date)formatter.parse(result.get(i+1));
			} catch (ParseException e) {
				return null;
			}
			messages.add(new Message(Integer.parseInt(result.get(i)), date, result.get(i+2), result.get(i+3), Integer.parseInt(result.get(i+4)), Integer.parseInt(result.get(i+5))));
		}
		return messages;
	}
	
	/**
	 * Insert message to DB.
	 * @param message A new defined message (Create constructor).
	 * @return 1 for success, 0 fail
	 */
	public static int sendMsg(Message message){ // Insert message to DB.
		
		String msg = "INSERT INTO `mat`.`messages` (`title`, `message`, `from`, `to`)";
    	String values = " VALUES ('" + message.getTitle() + "', '" + message.getMsg() + "', '" +
    			message.getFrom() + "', '" + message.getTo() + "')"; 
    	
    	HashMap <String,String> msgServer = new HashMap <String,String>();
    	msgServer.put("msgType", "insert");
		msgServer.put("query", msg + values);
    	
    	try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				return 0;
			}
    	synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			return 0;
		}}
		
		return (int)Main.client.getMessage();
	}
		
	/**
	 * Delete message from DB by id
	 * @param msgID
	 * @return
	 */
	public static int deleteMsg(String msgID){
		
		String msg = "DELETE FROM messages WHERE id = " + msgID;
    	HashMap <String,String> msgServer = new HashMap <String,String>();
    	msgServer.put("msgType", "delete");
		msgServer.put("query", msg);
    	
    	try{
			Main.client.sendMessageToServer(msgServer);
			}
			catch(Exception exp){
				return -1;
			}
    	synchronized (Main.client){try {
			Main.client.wait();
		} catch (InterruptedException e) {
			return -1;
		}}
    	return (int) Main.client.getMessage();
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
}
