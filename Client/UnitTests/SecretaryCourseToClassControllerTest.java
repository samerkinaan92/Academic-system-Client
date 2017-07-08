package unittests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import Controller.SecretaryCourseToClassController;
import Entity.Course;
import Entity.Semester;
import application.ClientConnection;
import application.Main;

public class SecretaryCourseToClassControllerTest {

	@Before
	public void setUp() throws Exception {
		Main.client = new ClientConnection("localhost", 5555);
	}
		
	@Test
	public void testCantAddFailure()
	{	// 2 - Student ID , 4 - Course ID for algebra 2 
		boolean result  = SecretaryCourseToClassController.cantAdd("2", "4");
		assertTrue(result);		
	}
	
	@Test
	public void testCantAddSuccess()
	{	// 2 - Student ID , 3 - Course ID for algebra 1 
		boolean result  = SecretaryCourseToClassController.cantAdd("2", "3");
		assertFalse(result);
	}
	
	@Test
	public void testGetPreCoursesNull()
	{	//'2' will be the course with no pre courses.
		ArrayList<String>  result  = SecretaryCourseToClassController.getPreCourses("2");
		assertNull(result);
	}
	
	@Test
	public void testGetPreCoursesResult()
	{	//Course ID 3 will be the pre course of course ID 4
		ArrayList<String>  result  = SecretaryCourseToClassController.getPreCourses("4");
		assertEquals("3", result.get(0));
	}

	
	/*Testing if the student have any courses taken, test will be success if there are any*/
	@Test
	public void testGetTakenCoursesSuccess()
	{	
		String stdID="2"; //Student ID
		ArrayList<String>  result  = SecretaryCourseToClassController.getTakenCourses(stdID);
		assertNotNull(result);
	}
	
	/*Testing if the student have any courses taken, test will be success if there is none*/
	@Test
	public void testGetTakenCoursesNull()
	{	
		String stdID="3"; //Student ID which not exist
		ArrayList<String>  result  = SecretaryCourseToClassController.getTakenCourses(stdID);
		assertNull(result);
	}
	
	
	
	/*Returning array list of courses*/
	@Test
	public void testGetCourseListSuccess()
	{	//checking if alg1 (3) will be the return value
		ArrayList<Course> courseArr = new ArrayList<Course>() ;
		courseArr.add(new Course(3,"alg1"));
		
		ArrayList<String>  result  = SecretaryCourseToClassController.getCourseList(courseArr);
		assertEquals(result.get(0), "alg1 (3)");
	}
	
	/*Checking if the class list will be empty*/
	@Test
	public void testGetCourseListEmpty()
	{	//Checking if the list will be empty
		ArrayList<Course> courseArr = new ArrayList<Course>() ;
		
		ArrayList<String>  result  = SecretaryCourseToClassController.getCourseList(courseArr);
		assertTrue(result.isEmpty());
	}
	
	/*Checking if the return value will be list of semesters*/
	@Test
	public void testGetSemesterList()
	{	
		ArrayList<Semester> semArr = new ArrayList<Semester>() ;
		semArr.add(new Semester(1,"a",0,2017));
		
		ArrayList<String>  result  = SecretaryCourseToClassController.getSemesterList(semArr);
		assertEquals(result.get(0), "2017 (a)");
	}
	
	/*Checking if the return value will be empty list*/
	@Test
	public void testGetSemesterListEmpty()
	{	
		ArrayList<Semester> semArr = new ArrayList<Semester>() ;
		
		ArrayList<String>  result  = SecretaryCourseToClassController.getSemesterList(semArr);
		assertTrue(result.isEmpty());
	}
}
