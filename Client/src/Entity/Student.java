package Entity;

public class Student extends User {

	private String ID;
	private String classRoom;
	private String parentID1;
	private String parentID2;
	
	
	public String getID() {
		return ID;
	}
	public String getClassRoom() {
		return classRoom;
	}
	public String getParentID1() {
		return parentID1;
	}
	public String getParentID2() {
		return parentID2;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}
	public void setParentID1(String parentID1) {
		this.parentID1 = parentID1;
	}
	public void setParentID2(String parentID2) {
		this.parentID2 = parentID2;
	}

}