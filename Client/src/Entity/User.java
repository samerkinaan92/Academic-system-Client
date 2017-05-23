package Entity;

public class User {

	private String ID;
	private String Name;
	private String Password;
	private String Role;
	
	
	public String getID() {
		return ID;
	}
	public String getName() {
		return Name;
	}
	public String getPassword() {
		return Password;
	}
	public String getRole() {
		return Role;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public void setRole(String role) {
		Role = role;
	}
}