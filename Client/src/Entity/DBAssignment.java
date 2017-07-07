package Entity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;

/**		this class represents assignment object 	*/
public class DBAssignment {
	
	public String name = "";			/*	user input */
	public String deadLine = "";
	public String filePath = "";
	
								/*	auto fill */
	public String publishDate = new SimpleDateFormat("YYYY-MM-dd").format(Calendar.getInstance().getTime());
	public int courseID;		
	public int semesterID;
	
	
	
	
	/**		return false if name does not fit the requierments	*/
	public boolean setName(String name) {					/*	validate and set assignment's name	*/
		
		if (name.length() < 1) 	// check if empty
			return false;
		
								// check if name contains special charachters
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(name);
		boolean b = m.find();
		if (b)
			return false;
		
		this.name = name;		// set name
		return true;
	}
	
	public boolean setPath(String p) {
		
		if (p.length()<3) 
			return false;
        String type = p.substring(p.lastIndexOf('.'));
        if (type.equals(".pdf") || type.equals(".doc") || type.equals(".docx") || type.equals(".txt")) {
    		this.filePath = p;
        	return true;
        }
		return false;
	}
	

	/**		return false if deadline does not fit the requierments	*/
	public boolean setDeadline(String date) {			/*	validate and set assignment's deadline	*/

		 LocalDate recieved = LocalDate.parse(date);	// convert string to date
		 
		 if (recieved.isBefore(LocalDate.now())) 		// check isBefore today
			 return false;
		 
		this.deadLine = date;			// set deadline
		return true;
		
	}
	
	/**	send this SUCCESSFULLY CREATED assignment object to the DB
	 * 
	 * @return true for success
	 * */
	public boolean sendToDB() {
		
		if (isValidAssignment()) {
		
			HashMap<String, String> sentMSG = new HashMap<>();
			sentMSG.put("msgType", "update");
			sentMSG.put("query", "INSERT INTO `mat`.`assignment` (`AssignmentName`, `CourseID`,"
				+ " `publishDate`, `deadLine`, `filePath`, `semesterId`) VALUES"
				+ " ('"+this.name+"', '"+this.courseID+"', '"+this.publishDate+"', '"+this.deadLine+"',"
						+ " '"+this.filePath+"', '"+this.semesterID+"');");
			
			synchronized (Main.client) {
				try {
					Main.client.sendMessageToServer(sentMSG);
					Main.client.wait();
					return true;
					}
				catch (InterruptedException e) {
					e.printStackTrace();
					System.out.println("Thrsead cant move to wait()");
					return false;
				}
			}

		}//if
	
		else
			return false;
		
	}//sendToDB

	public boolean isValidAssignment() {
		
		if (!name.equals("")	&&	!this.deadLine.equals("")	&&	!filePath.equals(""))
			return true;
		
		return false;
	}
	


	
}
