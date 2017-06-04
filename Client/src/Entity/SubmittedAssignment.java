package Entity;

import java.util.Date;

public class SubmittedAssignment extends Assignment {

	public SubmittedAssignment(int Aid, String name, int Cid, java.sql.Date publish, java.sql.Date dead, String path) {
		super(Aid, name, Cid, publish, dead, path);
	}
	private Date submittedDate;
	private String studentID;
	
	
	public Date getSubmittedDate() {
		return submittedDate;
	}
	public String getStudentID() {
		return studentID;
	}
	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

}