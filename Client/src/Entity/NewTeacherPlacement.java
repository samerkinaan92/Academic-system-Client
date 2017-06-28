package Entity;


/**
 * class for sending requests to change teacher in class
 * @author Samer Kinaan
 *
 */
public class NewTeacherPlacement {

	private String currTeacherID;
	private String newTeacherID;
	private String courseID;
	
	
	public String getCurrTeacherID() {
		return currTeacherID;
	}
	public String getNewTeacherID() {
		return newTeacherID;
	}
	public String getCourseID() {
		return courseID;
	}
	public void setCurrTeacherID(String currTeacherID) {
		this.currTeacherID = currTeacherID;
	}
	public void setNewTeacherID(String newTeacherID) {
		this.newTeacherID = newTeacherID;
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	
	
}