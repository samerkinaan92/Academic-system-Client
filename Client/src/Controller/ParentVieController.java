package Controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Controller.ViewTeacherController.techertInfo;
import Entity.Assignment;
import Entity.Parent;
import Entity.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ParentVieController implements Initializable {
	
	private final ObservableList<ParentInfo> studData = FXCollections.observableArrayList();
    @FXML
    private TableView<ParentInfo> ParentTable;

    @FXML
    private TableColumn<ParentInfo, String> studentIDColum;

    @FXML
    private TableColumn<ParentInfo, String> studNamecolum;
    
    @FXML
    private TableColumn<ParentInfo, String> IdCollum;

    @FXML
    private TableColumn<ParentInfo, String> NameCollum;

    @FXML
    private TableColumn<ParentInfo, String> MailCollum;

    @FXML
    private TableColumn<ParentInfo, String> TelephoneCollum;

    @FXML
    private TableColumn<ParentInfo, String> AddressCollum;

    @FXML
    private TableColumn<ParentInfo, String> isBlockedCollum;
/*
    @FXML
    void ChoosedParent(MouseEvent event) {
    }
*/
    
	 public static class ParentInfo{

	    
	        private StringProperty id;
	        public void setId(String value) { idProperty().set(value); }
	        public String getId() { return idProperty().get(); }
	        public StringProperty idProperty() { 
	            if (id == null) id = new SimpleStringProperty(this, "id");
	            return id; 
	        } 
	        
	    	private StringProperty name;
	        public void setName(String value) { nameProperty().set(value); }
	        public String getName() { return nameProperty().get(); }
	        public StringProperty nameProperty() { 
	            if (name == null) name = new SimpleStringProperty(this, "name");
	            return name; 
	        }
	        
	        private StringProperty address;
	        public void setAddress(String value) { addressProperty().set(value); }
	        public String getAddress() { return addressProperty().get(); }
	        public StringProperty addressProperty() { 
	            if (address == null) address = new SimpleStringProperty(this, "address");
	            return address; 
	        } 
	        
	        private StringProperty isBlocked;
	        public void setisBlocked(String value) { isBlockedProperty().set(value); }
	        public String getisBlocked() { return isBlockedProperty().get(); }
	        public StringProperty isBlockedProperty() { 
	            if (isBlocked == null) isBlocked = new SimpleStringProperty(this, "isBlocked");
	            return isBlocked; 
	        }
	        
	        private StringProperty mail;
	        public void setMail(String value) { mailProperty().set(value); }
	        public String getMail() { return mailProperty().get(); }
	        public StringProperty mailProperty() { 
	            if (mail == null) mail = new SimpleStringProperty(this, "mail");
	            return mail; 
	        }
	        
	        private StringProperty telephone;
	        public void setTelephone(String value) { telephoneProperty().set(value); }
	        public String getTelephone() { return telephoneProperty().get(); }
	        public StringProperty telephoneProperty() { 
	            if (telephone == null) telephone = new SimpleStringProperty(this, "telephone");
	            return telephone; 
	        } 
	        
	        private StringProperty studentId;
	        public void setstudentId(String value) { studentIdProperty().set(value); }
	        public String getstudentId() { return studentIdProperty().get(); }
	        public StringProperty studentIdProperty() { 
	            if (studentId == null) studentId = new SimpleStringProperty(this, "studentId");
	            return studentId; 
	        } 

	        private StringProperty studentName;
	        public void setstudentName(String value) { studentNameProperty().set(value); }
	        public String getstudentName() { return studentNameProperty().get(); }
	        public StringProperty studentNameProperty() { 
	            if (studentName == null) studentName = new SimpleStringProperty(this, "studentName");
	            return studentName; 
	        } 
	        public ParentInfo(String id, String teachertName,String address ,String isBlocked, String mail, String telephone, String studentId, String studentName){
	        	setId(id);
	        	setName(teachertName);
	        	setisBlocked(isBlocked);
	        	setMail(mail);
	        	setAddress(address);
	        	setTelephone(telephone);
	        	setstudentId(studentId);
	        	setstudentName(studentName);
	        }
	    }

	  
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		 ArrayList<String> ParentArr = new ArrayList<String>();
		 if (Class_Student_Info.flag == 1)
			 ParentArr = Parent.getParentsOfStudent(Class_Student_Info.SelectedStudent);	 
		 else ParentArr = Parent.getParentsClass(Class_Student_Info.ChoosedClass);
				 
		
		studData.clear();
		IdCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("id"));
		NameCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("name"));
		AddressCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("address"));
		isBlockedCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("isBlocked"));
		MailCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("mail"));
		TelephoneCollum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("telephone"));
		studentIDColum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("studentId"));
		studNamecolum.setCellValueFactory(new PropertyValueFactory<ParentInfo, String>("studentName"));



		   for (int i = 0 ; i < ParentArr.size() ; i+=7)
		    {
			   ParentInfo temp = new ParentInfo(ParentArr.get(i),
					   ParentArr.get(i+1),
					   ParentArr.get(i+4),
			   		   ParentArr.get(i+5),
			   		   ParentArr.get(i+2),
			   		   ParentArr.get(i+3),
			   		   ParentArr.get(i+6),
			   		   User.getUserInfo(ParentArr.get(i+6)).getName());
			   
			   		   					System.out.println(ParentArr.get(i+1)+" - "+User.getUserInfo(ParentArr.get(i+6)).getName());		 
			   studData.add(temp);
					   																							
		    }
		   ParentTable.setItems(FXCollections.observableArrayList(studData));
	}



}