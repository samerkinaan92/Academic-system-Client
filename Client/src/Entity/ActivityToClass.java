package Entity;

public class ActivityToClass {

	private int ActivityID;
	private int preReqCourseID;
	private int CourseID;
	
	
	public int getActivityID() {
		return ActivityID;
	}
	public int getPreReqCourseID() {
		return preReqCourseID;
	}
	public int getCourseID() {
		return CourseID;
	}
	public void setActivityID(int activityID) {
		ActivityID = activityID;
	}
	public void setPreReqCourseID(int preReqCourseID) {
		this.preReqCourseID = preReqCourseID;
	}
	public void setCourseID(int courseID) {
		CourseID = courseID;
	}

	
	
}