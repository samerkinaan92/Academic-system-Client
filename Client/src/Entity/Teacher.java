package Entity;

public class Teacher extends User {

	private String ID;
	private int maxWorkHours;
	
	
	public String getID() {
		return ID;
	}
	public int getMaxWorkHours() {
		return maxWorkHours;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setMaxWorkHours(int maxWorkHours) {
		this.maxWorkHours = maxWorkHours;
	}

}