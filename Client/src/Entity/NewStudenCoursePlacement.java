package Entity;

/**
 * class for sending requests for changing students courses assignment
 * @author Samer Kinaan
 *
 */
public class NewStudenCoursePlacement {

	private String studentID;
	private String courseID;
	private Action action;
	
	
	
	public NewStudenCoursePlacement(String studentID, String courseID, Action action) {
		this.studentID = studentID;
		this.courseID = courseID;
		this.action = action;
	}
	public String getStudentID() {
		return studentID;
	}
	public String getCoureID() {
		return courseID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public void setCoureID(String coureID) {
		this.courseID = coureID;
	}
	public Action getAction() {
		return action;
	}
	public void setAction(Action action) {
		this.action = action;
	}

	
}