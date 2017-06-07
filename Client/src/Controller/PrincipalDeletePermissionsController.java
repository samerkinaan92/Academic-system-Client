package Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import Entity.ParentStudent;
import Entity.Student;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class PrincipalDeletePermissionsController{

	
	private HashMap <String, String> msgServer = new HashMap <String,String>();
	
	public ParentStudent students[];
	public int numOfStud;
	public String parID;
	
    @FXML // fx:id="parIDlabel"
    private TextField parIDlabel; // Value injected by FXMLLoader


    @FXML // fx:id="SelectBtn"
    private Button SelectBtn; // Value injected by FXMLLoader
     
    @FXML
    private ListView<String> leftList;

    @FXML
    private ListView<String> rightList;


    @FXML
    private Label pNameLabel;
    
    @FXML
    private Label changLabel;

    
    @FXML
    private Pane paneDetails;

    @FXML
    private Label noRes;
    
    @FXML
    void applyEvent(ActionEvent event) {
    	
    	msgServer.put("msgType", "update");
    	
    	for(int i=0 ; i<numOfStud ; i++){
    		try{
    			msgServer.replace("query", "UPDATE Parent_Student SET isBlocked ='"+ (students[i].getIsParBloc() ? 0 : 1) +"' WHERE ParentUserID = '"+parID+"' and StudentUserID='" +students[i].getID()+"';");
    			Main.client.sendMessageToServer(msgServer);
    		}catch(Exception e){
    			System.out.println("Can't send message to server");
    			e.printStackTrace();
    		}
    		
    		synchronized (Main.client){
    			try {
    				Main.client.wait();
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		
    	}
    	
    	changLabel.setVisible(true);
    }
    	
    	
    	

 
	@SuppressWarnings("unchecked")
	@FXML
    void selectEvent(ActionEvent event) {
    	
    	parID =parIDlabel.getText();
    	msgServer.put("msgType", "select");
    	msgServer.put("query", "myq");
    	int i,numOfStud; 
    	
    	
    	
		msgServer.replace("query", "select StudentUserID,NAME,Parent_Student.isBlocked from Parent_Student,users where ParentUserID='"+parID+"' and StudentUserID=ID;");
		try{
			Main.client.sendMessageToServer(msgServer);
		}catch(Exception e){
			System.out.println("Can't send message to server");
			e.printStackTrace();
		}
		
	synchronized (Main.client){
		try {
			Main.client.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Thread can't move to wait()");
		}
	}
    	
    	
	ArrayList <String> stdArr = new ArrayList<String>();
	stdArr=(ArrayList<String>) Main.client.getMessage();
	numOfStud=stdArr.size();
	numOfStud=numOfStud/3;
	this.numOfStud=numOfStud;
	students= new ParentStudent[numOfStud];
	
	Iterator<String> iterator = stdArr.iterator();
	
	for(i=0;iterator.hasNext();i++){
		students[i]=new ParentStudent(iterator.next(),iterator.next(),iterator.next().equals("0") ? true : false);
	}
	
	
    	if(setParentName(parID)==1){
    		paneDetails.setVisible(true);
    		noRes.setVisible(false);
    		changLabel.setVisible(false);
    		
    		setLists();}
    	else{
    		paneDetails.setVisible(false);
    		noRes.setVisible(true);}
    	
	}
    		
 
	
	
	@SuppressWarnings("unchecked")
	public int setParentName(String pID){
    	msgServer.replace("query", "select Name from users where ID='"+pID+"';");
    	
		try{
		Main.client.sendMessageToServer(msgServer);
		}catch(Exception e){
			System.out.println("Can't send message to server");
			e.printStackTrace();
		}
			
		synchronized (Main.client){
			try {
				Main.client.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Thread can't move to wait()");
			}
		}
		
		ArrayList <String> stdArr = new ArrayList<String>();
		stdArr=(ArrayList<String>) Main.client.getMessage();
		
		if(!stdArr.isEmpty())
			pNameLabel.setText(stdArr.get(0));
		else
			return 0;
		
		return 1;
		
	}
	
	
	
	public void setLists(){
		ObservableList<String> leftStudList = FXCollections.observableArrayList();
		ObservableList<String> rightStudList = FXCollections.observableArrayList();
		
		for(int i=0;i<numOfStud;i++){
			if(students[i].getIsParBloc())
				leftStudList.add(students[i].getName() + " - " + students[i].getID());
			else
				rightStudList.add(students[i].getName() + " - " + students[i].getID());
			}
			
		Collections.sort(leftStudList);
		Collections.sort(rightStudList);
		
		
		leftList.setItems(leftStudList);
		rightList.setItems(rightStudList);
		
		
	}
	
	

    @FXML
    void moveLeft(ActionEvent event) { 	
    	String stdID= new String();
   	
    	try{
    		stdID=rightList.getSelectionModel().getSelectedItem();  
    		if(stdID!=null)
    			for(int i=0;i<numOfStud;i++)
    				if(stdID.contains(students[i].getID()))
	    				students[i].setIsParBloc(true);
    	}catch(Exception e){
    		System.out.println("Can't move right");
    	}
    	  
    	setLists();
    	
    	
    }

    @FXML
    void moveRight(ActionEvent event) {
    	
    	String stdID;
    	
    	try{
    	stdID=leftList.getSelectionModel().getSelectedItem();    	
    	if(stdID!=null)
    		for(int i=0;i<numOfStud;i++)
    			if(stdID.contains(students[i].getID()))
    				students[i].setIsParBloc(false);
    	}
      	catch(Exception e){
    		System.out.println("Can't move right");
    	}
    	setLists();

    }
	
	

}
