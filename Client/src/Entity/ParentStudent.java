package Entity;

import java.util.ArrayList;
import java.util.HashMap;

import application.Main;

/**Entity - ParentStudent, describing the many to many relationship contraint.*/
public class ParentStudent{
	
	/**isParBloc - value that indicate if the parent is blocked.*/
	private String isParBloc;
	
	/**std - Student object.*/
	private Student std;
	
	/**par - Parent obj.*/
	private Parent par;
	
	
	/**ParentStudent() - ParentStudent constructor.*/
	public ParentStudent(String stdID, String stdName,String isParBloc,String parID, String parName )
	{
		std= new Student(stdID,stdName);
		par= new Parent(parID,parName);
		setIsParBloc(isParBloc);
		
	}
	
	
	/**getParentsByStud() - returning array list of parent detailes by specific student.
	 * ID - student ID.
	 * stdName - student name.
	 * */
	public static ArrayList<ParentStudent> getParentsByStud(int ID,String stdName){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select parentuserid,Name,Parent_Student.isBlocked from Parent_Student,users where StudentUserID='"+ID+"' and parentuserid=ID;");
		
		ArrayList<String> result = sendMsg(msgServer);
		ArrayList<ParentStudent> DBassignment = new ArrayList<ParentStudent>();
		
		
		for (int i = 0; i < result.size(); i+=3)
			DBassignment.add(new ParentStudent(Integer.toString(ID),stdName,(result.get(i+2)=="1" ? "1" : "0"),result.get(i),result.get(i+1)));
		return DBassignment;
	
	}
	
	
	/**sendMsg() - send message to the server and return array list of the answer.*/
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
	
	
	/**getStudByPar - getting students of specific parent.
	 * parID - parent ID.
	 * parName - parent name.
	 * */
	
	public static ArrayList<ParentStudent> getStudByPar(String parID,String parName){
		HashMap <String,String> msgServer = new HashMap <String,String>();
		msgServer.put("msgType", "select");
		msgServer.put("query", "select StudentUserID,name,Parent_Student.IsBlocked from Parent_Student,users where StudentUserID=ID AND ParentUserID='"+parID+"'");
		
		ArrayList<String> result = sendMsg(msgServer);
		ArrayList<ParentStudent> DBassignment = new ArrayList<ParentStudent>();
		
		
		for (int i = 0; i < result.size(); i+=3)
			DBassignment.add(new ParentStudent(result.get(i),result.get(i+1),result.get(i+2).equals("1")?"1" : "0",parID,parName));

		return DBassignment;
	
	}
	
	
	public String getIsParBloc() {
		return isParBloc;
	}
	public void setIsParBloc(	String isParBloc) {
		this.isParBloc = isParBloc;
	}

	public Student getStd() {
		return std;
	}

	public void setStd(Student std) {
		this.std = std;
	}

	public Parent getPar() {
		return par;
	}

	public void setPar(Parent par) {
		this.par = par;
	}
	
	public String getStudID()
	{		
		return std.getID();
	}
	
	public String getStudName()
	{		
		return std.getName();
	}
	
	public String getParID()
	{		
		return par.getID();
	}
	
	public String getParName()
	{		
		return par.getName();
	}
	

}
