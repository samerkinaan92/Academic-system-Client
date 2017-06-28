package Entity;

/**AcademicActivity  - Entity which define the type of academic activity type.
 * @author Tal Asulin
 * */
public class AcademicActivity {

	
	private int ActivityID;
	private String semName;
	private String acName;
	private String acYear;
	
	public int getActivityID() {
		return ActivityID;
	}
	public String getSemName() {
		return semName;
	}
	public String getAcName() {
		return acName;
	}
	public String getAcYear() {
		return acYear;
	}
	public void setActivityID(int activityID) {
		ActivityID = activityID;
	}
	public void setSemName(String semName) {
		this.semName = semName;
	}
	public void setAcName(String acName) {
		this.acName = acName;
	}
	public void setAcYear(String acYear) {
		this.acYear = acYear;
	}

	
}