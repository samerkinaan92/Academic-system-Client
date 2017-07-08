package Fixtures;

import java.util.ArrayList;

import Entity.Course;
import Entity.TestCourse;
import fit.ActionFixture;

public class NewCourseCreation extends ActionFixture {
	
	private TestCourse tc;
	private ArrayList<Course> courses;
	
	private String name = "";
	private String TU = null;
	private String id = "";
	private String hours = null;
	
	

	public void startDefenition(){
		tc = new TestCourse();
		courses = tc.courses;
	}

	public void settingName(String name){
		this.name = name;
	}
	
	public void settingId(String id){
		this.id = id;	
	}
	
	public void settingTu(String Tu){
		this.TU = Tu;
	}
	
	public void settingHours(String hours){
		this.hours = hours;
	}
		
	public boolean create(){
		
		if (tc.checkIlegalInput(name, TU, id, hours)){
			return false;
		}
		
		for (int i = 0; i < courses.size(); i++){
			if (courses.get(i).getCourseID() == Integer.parseInt(id)){
				return false;
			}
		}
		
		courses.add(new Course(Integer.parseInt(id), name, Integer.parseInt(hours), TU));
		
		return true;
	}	
}
