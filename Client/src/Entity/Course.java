package Entity;

public class Course extends AcademicActivity {

	private int ActivityID;
	private int CourseID;
	private String Name;
	private int TUID;
	private int weeklyHours;
	
	
	public int getActivityID() {
		return ActivityID;
	}
	public int getCourseID() {
		return CourseID;
	}
	public String getName() {
		return Name;
	}
	public int getTUID() {
		return TUID;
	}
	public int getWeeklyHours() {
		return weeklyHours;
	}
	public void setActivityID(int activityID) {
		ActivityID = activityID;
	}
	public void setCourseID(int courseID) {
		CourseID = courseID;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setTUID(int tUID) {
		TUID = tUID;
	}
	public void setWeeklyHours(int weeklyHours) {
		this.weeklyHours = weeklyHours;
	}

	
}