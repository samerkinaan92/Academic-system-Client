package Entity;

public class Parent extends User {

	private String ID;
	private boolean isBlocked;
	
	
	public String getID() {
		return ID;
	}
	public boolean isBlocked() {
		return isBlocked;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	
}