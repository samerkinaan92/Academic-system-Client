package Fixtures;

import java.util.HashMap;
import Entity.DBAssignment;
import fit.ActionFixture;

public class NewAssignmentCreation extends ActionFixture {
	
	
	HashMap<String, String> sentMSG = new HashMap<>();
	
	DBAssignment ass;	/*	create the assignment	*/
	
	public void startAssignment() {
		
		ass = new DBAssignment();
		ass.courseID = 4;
		ass.semesterID = 4;
		
	}
	
	public void setName (String name) {
		ass.setName(name);
	}
	
	public void setDeadline (String deadline) {
		ass.setDeadline(deadline);
	}
	
	public void setFilePath (String filepath) {
		ass.setPath(filepath);
	}
	
	public boolean create() {
		
		if (ass.isValidAssignment())
			return true;
		return false;	
	}
	
}
