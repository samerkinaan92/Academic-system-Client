package unittests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Controller.StudentSubmitAssignmentController;
import Entity.Assignment;
import Entity.SubmittedAssignment;
import Entity.User;
import application.ClientConnection;
import application.Main;

public class StudentSubmitAssignmentControllerTest {
	StudentSubmitAssignmentController controller;

	@Before
	public void setUp() throws Exception {
		Main.client = new ClientConnection("localhost", 5555);
		Main.user = new User("2", "Samer Kinaan");
	}

	@Test
	public void testAssignmentDownloadGoodPath() {
		String filePath = "C:/M.A.T files/assignments/12-34-20_16.txt";
		byte[] file = Assignment.getFile(filePath);
		assertTrue(file != null);
	}
	
	@Test
	public void testAssignmentDownloadBadPath() {
		String filePath = "C:/M.A.T files/assignments/bad.txt";
		byte[] file = Assignment.getFile(filePath);
		assertTrue(file == null);
	}

	@Test
	public void testSubmitDownloadGoodPath() {
		String filePath = "C:\\M.A.T files\\submissions\\2_2.txt";
		byte[] file = SubmittedAssignment.getFile(filePath);
		assertTrue(file != null);
	}
	
	@Test
	public void testSubmitDownloadBadPath() {
		String filePath = "C:/M.A.T files/assignments/bad.txt";
		byte[] file = SubmittedAssignment.getFile(filePath);
		assertTrue(file == null);
	}
	
	@Test
	public void testSubmitGetSubAssBadUser() {
		Main.user = new User("555", "Samer Kinaan");
		ArrayList<String> result = SubmittedAssignment.getSubmittedAssignments();
		assertNull(result);
	}
	
	@Test
	public void testSubmitGetSubAssGoodUser() {
		Main.user = new User("2", "Samer Kinaan");
		ArrayList<String> result = SubmittedAssignment.getSubmittedAssignments();
		assertEquals("19", result.get(0));
		assertEquals("20", result.get(1));
	}
	
	@Test
	public void testGetStudAssGoodStudId() {
		ArrayList<String> result = Assignment.getAssignmentsOfStudent("2");
		assertEquals("19", result.get(0));
		assertEquals("20", result.get(1));
	}
	
	@Test
	public void testGetStudAssBadStudId() {
		ArrayList<String> result = Assignment.getAssignmentsOfStudent("5555");
		assertEquals(0, result.size());
	}
	
}
