package Entity;

public class User {

	private String ID;
	private String Name;
	
	public User() {
	}
	
	public User(String iD, String name) {
		ID = iD;
		Name = name;
	}
	public String getID() {
		return ID;
	}
	public String getName() {
		return Name;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setName(String name) {
		Name = name;
	}
}