
	package Controller;

	import java.awt.Button;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import Entity.Teacher;
import Entity.claSS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

	public class PRCPL_ViewSystemDataController implements Initializable{


	    @FXML
	    private ComboBox<String> dataCB;

	    @FXML
	    private Button selectBtn;
	  
	    static int flag;

	    @FXML
	    private Text txt;
	    
	    @FXML
	    private Button chooseBtn;

	    @FXML
	    private Text ChooseLabel;
	    
	    @FXML
	    private ListView<String> firstViewList;
	    
	    @FXML
	    private ListView<String> secondViewList;

	  /* @FXML
	    private Button ChooseTeacher;
	    */
	    ArrayList<Teacher> TeacherArr = new ArrayList<Teacher>();
	    
	    ArrayList<String> organizedTeacherNameList = new ArrayList<String>();
	    
	    ArrayList<String> organizedTeacherIDList = new ArrayList<String>();
	    
	    ArrayList<String> organizedFullTeacherList = new ArrayList<String>();
	   
	    ArrayList<String> organizedClassList = new ArrayList<String>();
	    
	    ArrayList<claSS> classArr = new ArrayList<claSS>();
	    //-----------------------------------------------------------------------------------------------------------------------------

	    //-----------------------------------------------------------------------------------------------------------------------------

	    private final String[] dataType = {"Classes Of Teacher","Teachers Of Class","Courses Of Teacher"};
	    
	    private final ObservableList<String> boxDataType = FXCollections.observableArrayList(dataType);
	  //-----------------------------------------------------------------------------------------------------------------------------
	  
	    ArrayList<String> getTeacherList(ArrayList<Teacher> teacherArr){
			
	    	ArrayList<String> temp = new ArrayList<String>();
			for (int i = 0; i < teacherArr.size(); i++)
				temp.add(teacherArr.get(i).getName());
	    	
	    	return temp;
	    	
	    }
	    
	    


		private ArrayList<String> getClassList(ArrayList<claSS> classArr){
			 
			 ArrayList<String> temp = new ArrayList<String>();
				for (int i = 0; i < classArr.size(); i++)
					temp.add(classArr.get(i).getClassName());
				
				return temp;	
		 }
		/*
		private String getTeacherId(ArrayList<Teacher> TeacherArry){
			 
			 String temp = new String();
				//for (int i = 0; i < classArr.size(); i++)
					temp = classArr.get(0).getClassName();
				
				return temp;	
		 }
		*/
		//-----------------------------------------------------------------------------------------------------------------------------
		
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			dataCB.setItems(boxDataType);
			
			TeacherArr = Teacher.getTeachers();
			for (int i = 0 ; i  < TeacherArr.size() ; i++)
			{
				organizedTeacherNameList.add(TeacherArr.get(i).getName());
				organizedTeacherIDList.add(TeacherArr.get(i).getID());
				organizedFullTeacherList.add(organizedTeacherNameList.get(i)+" - "+organizedTeacherIDList.get(i));
			//	organizedTeacherNameList2
			//	organizedTeacherIDList2
			//	organizedFullTeacherList2
				
			}	
				classArr = claSS.getClasses();
				organizedClassList = getClassList(classArr);
				Collections.sort(organizedClassList);
				
			

			}  
		
	  
		

	    
	    
	    @FXML
	    void clickSelect(ActionEvent event) throws IOException {

	    	String type;
	    	type = dataCB.getSelectionModel().getSelectedItem();
			switch (type){
			
			case "Classes Of Teacher":
				flag = 1;
				ChooseLabel.setText("Please Choose Teacher");
				firstViewList.setItems(FXCollections.observableArrayList(organizedFullTeacherList)); ;
				break;
			case "Teachers Of Class":
				flag = 2;
				ChooseLabel.setText("Please Choose Class");
				firstViewList.setItems(FXCollections.observableArrayList(organizedClassList));;
				break;
			case "Courses Of Teacher": 
				flag = 3;
				ChooseLabel.setText("Please Choose Teacher");
				firstViewList.setItems(FXCollections.observableArrayList(organizedTeacherNameList));;
				break;
			}
		    
			}
	    @FXML
	    void ClickChoose(ActionEvent e) throws IOException {
	    	if (flag == 1)
	    		handle_Classes_Of_Teacher();
	    	if (flag == 2)
	    		handle_Teachers_Of_Class();
	    	if (flag == 3)	
	    		handle_Courses_Of_Teacher();
	    }

    	private void handle_Teachers_Of_Class() {
			// TODO Auto-generated method stub
			
		}




		void handle_Classes_Of_Teacher(){
//s.substring(s.lastIndexOf(' ') + 1);
			String techer;
			String teacherID;
			techer = firstViewList.getSelectionModel().getSelectedItem();
			teacherID = techer.substring((techer.lastIndexOf(' ') + 1));
			ArrayList<String> classesOfTeacher = new ArrayList<String>();	
			classesOfTeacher = Teacher.getTeachersClassAsStringArrList(teacherID);
			secondViewList.setItems(FXCollections.observableArrayList(classesOfTeacher));

			
    	}
    	
    	/*
    	void handle_Teachers_Of_Class(){
    		String className;
    		className = firstViewList.getSelectionModel().getSelectedItem();
    		ArrayList<Teacher> TeachersOfClass = new ArrayList<Teacher>();
    		ArrayList<String> TeachersOfClassIDString = new ArrayList<String>();
    		ArrayList<String> TeachersOfClassIDString = new ArrayList<String>();
    		TeachersOfClass = claSS.getTeachersOfClass(className);
    		for (int i ; i < TeachersOfClass.size() ; i++)
    		TeachersOfClassString = TeachersOfClass.get(i).getID()
    		for (int i=0 ; i < TeachersOfClass.size() ; i++)
				System.out.println(TeachersOfClassIDString.get(i));
    		secondViewList.setItems(FXCollections.observableArrayList(TeachersOfClassString));
    		
    		
    	}
    	*/
    	
    	void handle_Courses_Of_Teacher(){

    	}
	    
	    

	    


	
	    

	}

