package Entity;

public class ParentStudent extends User{
	
	private boolean isParBloc;
	
	
	public ParentStudent(String ID, String Name,boolean isParBloc )
	{
		super(ID,Name);
		this.isParBloc=isParBloc;
		
	}
	
	public boolean getIsParBloc() {
		return isParBloc;
	}
	public void setIsParBloc(boolean isParBloc) {
		this.isParBloc = isParBloc;
	}

}
