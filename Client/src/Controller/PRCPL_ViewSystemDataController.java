
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

	  /* @FXML
	    private Button ChooseTeacher;
	    */
	    ArrayList<Teacher> TeacherArr;
	    
	    ArrayList<String> organizedTeacherList;
	    
	    ArrayList<String> organizedClassList;
	    
	    ArrayList<claSS> classArr;
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
		
		private String getTeacherId(ArrayList<Teacher> TeacherArry){
			 
			 String temp = new String();
				//for (int i = 0; i < classArr.size(); i++)
					temp = classArr.get(0).getClassName();
				
				return temp;	
		 }
		
		//-----------------------------------------------------------------------------------------------------------------------------
		
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			// TODO Auto-generated method stub
			dataCB.setItems(boxDataType);
			
		
			TeacherArr = Teacher.getTeachersNames();
			organizedTeacherList = getTeacherList(TeacherArr);
			
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
				firstViewList.setItems(FXCollections.observableArrayList(organizedTeacherList)); ;
				break;
			case "Teachers Of Class":
				flag = 2;
				ChooseLabel.setText("Please Choose Class");
				firstViewList.setItems(FXCollections.observableArrayList(organizedClassList));;
				break;
			case "Courses Of Teacher": 
				flag = 3;
				ChooseLabel.setText("Please Choose Teacher");
				firstViewList.setItems(FXCollections.observableArrayList(organizedTeacherList));;
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

    	void handle_Classes_Of_Teacher(){

			String techerName;
			techerName = firstViewList.getSelectionModel().getSelectedItem();
			ArrayList<Teacher> TeacherArry = new ArrayList<Teacher>();

			String techerID = Teacher.getTeacherID(techerName);
			System.out.println(techerID);
			int id = Integer.parseInt(techerID);

			
			
    	}
    	

    	void handle_Teachers_Of_Class(){
  
    	}
    	
    	
    	void handle_Courses_Of_Teacher(){

    	}
	    
	    

	    


	
	    

	}

