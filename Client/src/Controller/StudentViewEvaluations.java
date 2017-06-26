package Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.print.DocFlavor.URL;
import Entity.Teacher;
import Entity.User;
import application.Main;
import Entity.Assignment;
import Entity.Course;
import Entity.EvaluationForm;
import Entity.Semester;
import Entity.StudentCourse;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;


/** 	Controller class for: "StudentViewEvaluation.fxml"	
 * 		@author Tal Asulin
 * */
public class StudentViewEvaluations  implements Initializable{
	
	/**courList - list of coursed for the courses combobox*/
	private ObservableList<String> courList = FXCollections.observableArrayList();
	
	/**assignList - list of assignments for the assignment combobox*/
	private ObservableList<String> assignList = FXCollections.observableArrayList();
	
	/**semList - list of assignments for the assignment combobox*/
	private ObservableList<String> semList = FXCollections.observableArrayList();
	
	/**selecterSem - Use to store the selected semester name.*/
	private String selecterSem;
	
	/**selecterCour - Use to store the selected course name.*/
	private String selecterCour;
	
	/**selectedAssign - Use to store the selected assignment name.*/
	private String selectedAssign;
	
	/**stdEv - Use to store the student evaluation object*/
	private EvaluationForm stdEv;
	
	/**flag & flag2 are use to set the options in the bombobox selection.
	 * When there is no resault the flags will be updated accordingly and set off the selection for the combobox.
	 * */
	private int flag;
	private int flag2;
	
	/**use - Use to store the User object.*/
	private static User usr;
	
	
    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private Resource resources;

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private Button downloadBtn;
    
    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private URL location;

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private Label teachNameL;

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private Pane assignPane;
   
    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private Button backBtn;
    
    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private Label gradeL;
    
    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private ComboBox<String> semCbox;

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private ComboBox<String> courCbox;

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private ComboBox<String> assignCbox;

    /**
	 * Value injected by FXMLLoader
	 */
    @FXML
    private TextArea textArea;
    
    

   /**
    * initialize() - Use to initialize the information on screen.
    * */
    @Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
    	setSemCbox();
    	courCbox.setDisable(true);
    	assignCbox.setDisable(true);
    	if(Main.user.getType().equals("Parent"))
    		backBtn.setVisible(true);
    	
    	semCbox.valueProperty().addListener(new ChangeListener<String>() {
    		@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
    			setCourses();
			}});
    	
    	courCbox.valueProperty().addListener(new ChangeListener<String>() {
    		@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
    			if(flag==1)
    				setAssign();
			}});
    	
    	assignCbox.valueProperty().addListener(new ChangeListener<String>() {
    		@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
    			if(flag2==1)
    				setEvaluation();
			}});
    	
    }
    
    
    /**setUsr()
     * @param stdID , stdName
     * Use to set the user ID for the controller.
     * */
    public static void setUsr(String stdID, String stdName)
    {
    	usr=new User(stdID,stdName);
    }
    
    

    /**
     * backOption() - use for the Parent use when viewing student information.
     * @param event
     * */
    @FXML
    void backOption(ActionEvent event) {
    	try {
		   java.net.URL paneOneUrl = getClass().getResource("/FXML/ParentSelectStd.fxml");
		   AnchorPane paneOne = FXMLLoader.load( paneOneUrl );
		   BorderPane border = Main.getRoot();			    
		   border.setCenter(paneOne);
        } catch (IOException exp) {
        	exp.printStackTrace();
          }  

    }
    

    /**
     * DownloadAssign() - Use to download the selected evaluation form.
     * @param event
     * */
	@FXML
	void DownloadAssign(ActionEvent event) { //Download Assignment from server.        	
    	
    	byte[] file = Assignment.getFile(stdEv.getFilePath());	// file obj
		FileChooser fileChooser = new FileChooser();	// create file chooser
		
        fileChooser.setTitle("Save Evaluation");
        /*
        String type = (stdEv.getFilePath()).substring(stdEv.getFilePath().indexOf('.'));
        fileChooser.setInitialFileName(stdEv.getAssignID()+"_"+usr.getID()+type);	// TODO
        */
        String type = (stdEv.getFilePath()).substring(stdEv.getFilePath().indexOf('.'));
        fileChooser.setInitialFileName("BLABLI"+type);
        
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Word Documents", "*.docx")
            );
        File pa = fileChooser.showSaveDialog(downloadBtn.getScene().getWindow());	// open it (returns save path)
        if (pa != null) {
        	Path savePath = Paths.get(pa.getAbsolutePath());	// get save path
        	FileOutputStream stream;
        	try {
        		stream = new FileOutputStream(savePath.toString());
        		stream.write(file);
        		stream.close();
        		showErrorMSG("success!","submission file saved!");        		
        	}
        	catch (IOException ex) {
        		ex.printStackTrace();
        		showErrorMSG("error!","error downloading file.");
        	}
        }
    }

    
	/**
	 * setSemCbox() - Use to set semesters into combobox.
	 * */
	public void setSemCbox(){
		ArrayList<String> semL=	Semester.semList();
		assignList.clear();
		
    	for (int i=0;i<semL.size();i++) 
    		semList.add(semL.get(i).toString());
    	
    	Collections.sort(semList);
    	
    	if (semList.isEmpty()){
    		showErrorMSG("No Courses found for USER ID: "+usr.getID()+ "","");  
    		assignPane.setVisible(false);
    	}
    	else{
    		semCbox.setItems(semList);			/*	POPULATE COMBOBOX COLLECTION	*/
    	flag=0;
    	flag2=0;
    	}
    	assignCbox.setDisable(true);
	}
	
	
	/**setCourses() - Use to set coursed into the courses combobox*/
    @FXML
	void setCourses() {
    	
    	ArrayList<StudentCourse> courL;
    	selecterSem=semCbox.getSelectionModel().getSelectedItem().toString();
    	
    	try{
	    	courL=StudentCourse.getCoursesBySemStd(selecterSem,usr.getID());
	    	
	    	if (courL.isEmpty()){
	    		showErrorMSG("No Courses found for USER ID: "+usr.getID(), "No Courses found for semester "+selecterSem);
	    		courCbox.setDisable(true);
	    		assignCbox.setDisable(true);
	    		flag=0;
	    		flag2=0;
	    		assignPane.setVisible(false);
	    	}else{
	        	courList.clear();
	        	flag=1;	        	
	        	for(int i=0 ; i<courL.size() ; i++)
	        		courList.add(courL.get(i).getCourseName());   	
	        	
	        	Collections.sort(courList);
	    		courCbox.setItems(courList);
	    		courCbox.setDisable(false);
	    	}
    	}catch(NullPointerException e)
    	{
    		System.out.print("No selection.");
    	}
    	}
	
    	

    
    /**setAssign() - Use to set assignments into the assignment combobox.*/
    @FXML
    public void setAssign()
    {
    	
    	ArrayList<String> assignL;
    	selecterCour=courCbox.getSelectionModel().getSelectedItem().toString();
    	
    	assignL=Assignment.getAssignByCourse(selecterSem,Course.getCourseIDbyName(selecterCour),usr.getID());
    	
    	
    	if (assignL.isEmpty()){
    		showErrorMSG("No assignments found.","No assignments found for USER ID: "+usr.getID() +" for course "+selecterCour);
    		assignCbox.setDisable(true);
    		assignPane.setVisible(false);
    		flag2=0;
    	}else{
        	assignList.clear();
    
        	flag2=1;
        	for(int i=0 ; i<assignL.size() ; i++)
        		assignList.add(assignL.get(i));   	
        	
        	Collections.sort(assignList);
    		assignCbox.setItems(assignList);
    		assignCbox.setDisable(false);
    	}
    	
    }
	
    
    /**showErrorMSG() - Presenting error message to the user.
     * @param title , MSG
     * */
    private void showErrorMSG(String title, String MSG) {
		
  		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error!");
		alert.setHeaderText(title);
		alert.setContentText(MSG);
		alert.showAndWait();
		
	}
    
    
    /**setEvaluation() - Set the evaluation form taht selected by the user.*/
	public void setEvaluation()
	{
		selectedAssign=assignCbox.getSelectionModel().getSelectedItem().toString();
		assignPane.setVisible(true);
		setTachName(selectedAssign);
		setEvaluationPane();	
	}

	
	/**setTachName() - Present the teacher name when the user select evaluation to present.
	 * @param assignID
	 * */
	public void setTachName(String assignID)
	{
		String teachID=Teacher.getTeachersIDbyAssign(selectedAssign,usr.getID(),selecterSem,selecterCour);
		User usr = User.getUserInfo(teachID);
		if(usr.getName()!=null){
			teachNameL.setVisible(true);
			teachNameL.setText(usr.getName());
		}
	}
	
	
	/**setEvaluationPane() - set the evaluation details on the evaluation pane.*/
	public void setEvaluationPane(){
		
		stdEv =EvaluationForm.getEvaluByStdAssign(usr.getID(),selectedAssign);
		
		if(stdEv!=null)
		{
			gradeL.setText(stdEv.getGrade());
			textArea.setText(stdEv.getComments());
		}
		
	}




}
