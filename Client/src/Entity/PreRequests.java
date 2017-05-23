package Entity;

public class PreRequests {

	private int ActivityID;
	private int CourseID;
	private int preReqCourseID;
	
	
	public int getActivityID() {
		return ActivityID;
	}
	public int getCourseID() {
		return CourseID;
	}
	public int getPreReqCourseID() {
		return preReqCourseID;
	}
	public void setActivityID(int activityID) {
		ActivityID = activityID;
	}
	public void setCourseID(int courseID) {
		CourseID = courseID;
	}
	public void setPreReqCourseID(int preReqCourseID) {
		this.preReqCourseID = preReqCourseID;
	}

}