package Entity;

public class Semester {

	private boolean type;
	private boolean isActive;
	private int year;
	
	
	public boolean isType() {
		return type;
	}
	public boolean isActive() {
		return isActive;
	}
	public int getYear() {
		return year;
	}
	public void setType(boolean type) {
		this.type = type;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public void setYear(int year) {
		this.year = year;
	}

}