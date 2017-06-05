package Entity;

public class NewStudenCoursePlacement {

	private String studentID;
	private String coureID;
	private Action action;
	
	public String getStudentID() {
		return studentID;
	}
	public String getCoureID() {
		return coureID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public void setCoureID(String coureID) {
		this.coureID = coureID;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}

	
}