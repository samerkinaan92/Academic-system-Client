package Entity;

import java.util.ArrayList;

public class TestCourse {
	
	public ArrayList<Course> courses = new ArrayList<Course>();

	public TestCourse(){
		
		courses.add(new Course(1, "java", 3, "Software"));
		courses.add(new Course(2, "c++", 2, "Software"));
		courses.add(new Course(3, "Algebra1", 5, "Math"));
		courses.add(new Course(4, "Algebra2", 7, "Math"));
	}
	
	
	 /**
     * Check for illegal input.
     * @return if new course is legal
     */
	public boolean checkIlegalInput(String name, String tu, String id, String hours){ // Check for illegal input.
		boolean err = false;
		
		try{
    		Integer.parseInt(id);
    		if (id.isEmpty()){
    			throw new Exception();
    		}
    		if (id.length() > 6){
    			throw new Exception();
    		}
    	}
    	catch(Exception exp1){
    		err = true;
    	}
    	
    	if (name.isEmpty()){
    		
    		err = true;
    	}
    	
    	if (hours == null){
    		
    		err = true;
    	}
    	
    	if (tu == null){
    		
    		err = true;
    	}
    	
    	return err;
	}
}
